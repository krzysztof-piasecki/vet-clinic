package pl.homework.vetclinic.controller;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import pl.homework.vetclinic.BaseTest;
import pl.homework.vetclinic.model.Appointment;
import pl.homework.vetclinic.model.Customer;
import pl.homework.vetclinic.model.Doctor;
import pl.homework.vetclinic.service.AppointmentService;
import pl.homework.vetclinic.service.CustomerService;
import pl.homework.vetclinic.service.DoctorService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AppointmentControllerTest extends BaseTest {
    @LocalServerPort
    int randomServerPort;

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private CustomerService customerService;


    @Test
    void getAppointmentById() {
        port = randomServerPort;
        clearTables();

        //given
        LocalDate localDate = LocalDate.of(2021, Month.JANUARY, 19);
        LocalTime localTime = LocalTime.of(9, 0, 0);
        String path = "appointment:{id}";
        Doctor doctor = getFirstDoctor();
        doctorService.saveDoctor(doctor);

        Appointment firstAppointment = new Appointment.Builder()
                .withAppointmentDate(localDate)
                .withAppointmentTime(localTime)
                .withDoctor(doctor)
                .build();

        //when
        appointmentService.saveAppointment(firstAppointment);
        Long id = appointmentService.getAllAppointments().iterator().next().getId();
        String doctorString = "{firstName=Sanitariusz, lastName=Mariusz, id=" + doctor.getId() + "}";
        when()
                .get(path, id)
                .then()
                .statusCode(200)
                .body("appointmentDate", hasToString(firstAppointment.getAppointmentDate().toString()))
                .body("doctor", hasToString(doctorString));
    }

    @Test
    void deleteAppointment() {
        port = randomServerPort;
        clearTables();

        //given
        LocalDate localDate = LocalDate.of(2021, Month.JANUARY, 19);
        LocalTime localTime = LocalTime.of(9, 0, 0);
        String path = "appointment/cancel:{id}";
        Doctor doctor = getFirstDoctor();
        Customer customer = getFirstCustomer();

        doctorService.saveDoctor(doctor);
        customerService.saveCustomer(customer);

        Appointment firstAppointment = new Appointment.Builder()
                .withAppointmentDate(localDate)
                .withAppointmentTime(localTime)
                .withDoctor(doctor)
                .withCustomer(customer)
                .build();

        appointmentService.saveAppointment(firstAppointment);
        Long id = appointmentService.getAllAppointments().iterator().next().getId();

        assertNotNull(appointmentService.getAppointmentById(id).get().getCustomer());

        String requestBody = "{" +
                "\"digitId\": \"" + customer.getDigitId() + "\"," +
                "\"codePin\": \"" + customer.getCodePIN() + "\"}";
        //then
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .delete(path, id)
                .then()
                .statusCode(200);

        assertNull(appointmentService.getAppointmentById(id).get().getCustomer());
    }

    @Test
    void createAvailableAppointmentsByDoctorTimeAndDate() {
        port = randomServerPort;
        clearTables();

        //given
        String path = "/appointment/doctor";
        String startLocalDate = "2021-01-19";
        String endLocalDate = "2021-01-21";
        String startLocalTime = "09:00:00";
        String endLocalTime = "14:00:00";
        Doctor firstDoctor = getFirstDoctor();
        Customer customer = getFirstCustomer();

        doctorService.saveDoctor(firstDoctor);
        customerService.saveCustomer(customer);

        String requestBody = "{" +
                "\"startAppointmentDate\": \"" + startLocalDate + "\"," +
                "\"endAppointmentDate\": \"" + endLocalDate + "\"," +
                "\"startAppointmentTime\": \"" + startLocalTime + "\"," +
                "\"endAppointmentTime\": \"" + endLocalTime + "\"," +
                "\"doctorId\": \"" + doctorService.getAllDoctors().iterator().next().getId() + "\"}";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(path)
                .then()
                .statusCode(200)
                .extract()
                .response();

        assertEquals(5, appointmentService.getAvailableAppointmentByDateAndDoctor(firstDoctor, LocalDate.parse(startLocalDate)).size());

        Appointment appointmentTakenByCustomer = appointmentService.getAllAppointments().iterator().next();
        appointmentTakenByCustomer.setCustomer(customer);

        appointmentService.saveAppointment(appointmentTakenByCustomer);

        assertEquals(4, appointmentService.getAvailableAppointmentByDateAndDoctor(firstDoctor, LocalDate.parse(startLocalDate)).size());

        clearTables();
    }

    @Test
    void getAvailableAppointmentByDateAndDoctor() {
        port = randomServerPort;
        clearTables();

        //given
        String path = "appointment/available";
        LocalDate startLocalDate = LocalDate.of(2021, Month.JANUARY, 19);
        LocalDate endLocalDate = LocalDate.of(2021, Month.JANUARY, 21);
        LocalTime startLocalTime = LocalTime.of(9, 0);
        LocalTime endLocalTime = LocalTime.of(14, 0);
        Doctor firstDoctor = getFirstDoctor();
        Customer customer = getFirstCustomer();

        doctorService.saveDoctor(firstDoctor);
        customerService.saveCustomer(customer);

        appointmentService.saveAppointmentWithStartDateAndTime(firstDoctor, startLocalDate, endLocalDate, startLocalTime, endLocalTime);

        String requestBody = "{" +
                "\"localDate\": \"" + startLocalDate + "\"," +
                "\"doctorId\": \"" + doctorService.getAllDoctors().iterator().next().getId() + "\"}";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .get(path)
                .then()
                .statusCode(200)
                .body("size()", is(5));

        Appointment appointmentTakenByCustomer = appointmentService.getAllAppointments().iterator().next();
        appointmentTakenByCustomer.setCustomer(customer);

        appointmentService.saveAppointment(appointmentTakenByCustomer);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .get(path)
                .then()
                .statusCode(200)
                .body("size()", is(4));
    }

    @Test
    void makeAppointment() {
        port = randomServerPort;
        clearTables();

        //given
        String path = "appointment/book";
        String startLocalDate = "2021-01-19";
        String startLocalTime = "09:00:00";
        Doctor firstDoctor = getFirstDoctor();
        Customer customer = getFirstCustomer();

        doctorService.saveDoctor(firstDoctor);
        customerService.saveCustomer(customer);
        
        Appointment appointment = new Appointment.Builder()
                .withAppointmentDate(LocalDate.parse(startLocalDate))
                .withAppointmentTime(LocalTime.parse(startLocalTime))
                .withDoctor(firstDoctor)
                .build();
        
        appointmentService.saveAppointment(appointment);
        
        String requestBody = "{" +
                "\"digitId\": \"" + customer.getDigitId() + "\"," +
                "\"codePin\": \"" + customer.getCodePIN() + "\"," + 
                "\"localDate\": \"" + startLocalDate + "\"," + 
                "\"localTime\": \"" + startLocalTime + "\"," + 
                "\"doctorId\": \"" + doctorService.getAllDoctors().iterator().next().getId() + "\"}";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(path)
                .then()
                .statusCode(200)
                .extract()
                .response();

        appointment = appointmentService.getAllAppointments().iterator().next();

        assertEquals(customer, appointment.getCustomer());
        assertEquals(firstDoctor, appointment.getDoctor());
        assertEquals(LocalDate.parse(startLocalDate), appointment.getAppointmentDate());
        assertEquals(LocalTime.parse(startLocalTime), appointment.getAppointmentTime());
        clearTables();
    }
}