package pl.homework.doctorsappointment.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import pl.homework.doctorsappointment.BaseTest;
import pl.homework.doctorsappointment.model.Appointment;
import pl.homework.doctorsappointment.model.Doctor;
import pl.homework.doctorsappointment.service.AppointmentService;
import pl.homework.doctorsappointment.service.CustomerService;
import pl.homework.doctorsappointment.service.DoctorService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static io.restassured.RestAssured.port;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

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
    void getAppointmentById(){
        port = randomServerPort;

        //clear tables
        appointmentService.deleteAllAppointments();
        customerService.deleteAllCustomers();
        doctorService.deleteAllDoctors();

        //given
        LocalDate localDate = LocalDate.of(2021, Month.JANUARY,19);
        LocalTime localTime = LocalTime.of(9,0,0);
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
        String doctorString = "{firstName=Sanitariusz, lastName=Mariusz, id="+ doctor.getId() + "}";
        when()
                .get(path, id)
                .then()
                .statusCode(200)
                .body("appointmentDate", hasToString(firstAppointment.getAppointmentDate().toString()))
                .body("doctor", hasToString(doctorString));
    }
}