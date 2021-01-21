package pl.homework.doctorsappointment.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.homework.doctorsappointment.BaseTest;
import pl.homework.doctorsappointment.model.Appointment;
import pl.homework.doctorsappointment.model.Customer;
import pl.homework.doctorsappointment.model.Doctor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

@SpringBootTest
@ActiveProfiles("test")
class AppointmentServiceTest extends BaseTest {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private AppointmentService appointmentService;


    @Test
    void saveAndDeleteAppointment(){
        //clear tables
        appointmentService.deleteAllAppointments();
        customerService.deleteAllCustomers();
        doctorService.deleteAllDoctors();

        //given
        LocalDate startLocalDate = LocalDate.of(2021, Month.JANUARY,19);
        LocalDate endLocalDate = LocalDate.of(2021, Month.JANUARY,21);
        LocalTime startLocalTime = LocalTime.of(9,0);
        LocalTime endLocalTime = LocalTime.of(18,0);
        Doctor firstDoctor = getFirstDoctor();

        //save
        doctorService.saveDoctor(firstDoctor);


        //given
        Appointment firstTestAppointment = new Appointment.Builder()
                .withAppointmentDate(startLocalDate)
                .withAppointmentTime(startLocalTime)
                .withDoctor(firstDoctor).build();
        Appointment lastTestAppointment = new Appointment.Builder()
                .withAppointmentDate(endLocalDate)
                .withAppointmentTime(endLocalTime)
                .withDoctor(firstDoctor).build();
        appointmentService.saveAppointment(firstTestAppointment);
        appointmentService.saveAppointment(lastTestAppointment);
        appointmentService.saveAppointmentWithStartDateAndTime(firstDoctor, startLocalDate, endLocalDate, startLocalTime, endLocalTime);

        List<Appointment> appointments = StreamSupport.stream(appointmentService.getAllAppointments().spliterator(), false)
                .collect(Collectors.toList());
        Appointment firstAppointmentFromList = appointments.stream().findFirst().get();

        //then
        assertEquals(firstTestAppointment,appointmentService.getAppointmentById(firstTestAppointment.getId()).get());
        assertEquals(lastTestAppointment,appointmentService.getAppointmentById(lastTestAppointment.getId()).get());
        assertNotEquals(firstTestAppointment,appointmentService.getAppointmentById(lastTestAppointment.getId()).get());
        assertEquals(29, StreamSupport.stream(appointmentService.getAllAppointments().spliterator(), false).count());


        //when
        appointmentService.deleteAppointmentById(firstAppointmentFromList.getId());

        //then
        assertTrue(appointmentService.getAppointmentById(firstAppointmentFromList.getId()).isEmpty());

        //clear tables
        appointmentService.deleteAllAppointments();
        customerService.deleteAllCustomers();
        doctorService.deleteAllDoctors();

    }

    @Test
    void getAvailableAppointmentByDateAndDoctor(){
        //clear tables
        appointmentService.deleteAllAppointments();
        customerService.deleteAllCustomers();
        doctorService.deleteAllDoctors();

        //given
        LocalDate startLocalDate = LocalDate.of(2021, Month.JANUARY,19);
        LocalDate endLocalDate = LocalDate.of(2021, Month.JANUARY,21);
        LocalTime startLocalTime = LocalTime.of(9,0);
        LocalTime endLocalTime = LocalTime.of(11,0);
        Doctor firstDoctor = getFirstDoctor();
        Doctor secondDoctor = getSecondDoctor();
        Customer firstCustomer = getFirstCustomer();

        //save
        doctorService.saveDoctor(firstDoctor);
        doctorService.saveDoctor(secondDoctor);
        customerService.saveCustomer(firstCustomer);

        //when
        appointmentService.saveAppointmentWithStartDateAndTime(firstDoctor, startLocalDate, endLocalDate, startLocalTime, endLocalTime);
        appointmentService.saveAppointmentWithStartDateAndTime(secondDoctor, startLocalDate, endLocalDate, startLocalTime, endLocalTime.plusHours(3));

        //when
        List<Appointment> appointmentsListFirstDoctor = appointmentService.getAvailableAppointmentByDateAndDoctor(firstDoctor, startLocalDate);
        List<Appointment> appointmentsListSecondDoctor = appointmentService.getAvailableAppointmentByDateAndDoctor(secondDoctor, endLocalDate);

        //then
        assertEquals(2, appointmentsListFirstDoctor.size());
        assertEquals(5, appointmentsListSecondDoctor.size());

        Appointment appointmentWithCustomer = appointmentsListFirstDoctor.stream().findFirst().get();
        appointmentWithCustomer.setCustomer(firstCustomer);
        appointmentService.saveAppointment(appointmentWithCustomer);


        //then
        assertEquals(1, appointmentService.getAvailableAppointmentByDateAndDoctor(firstDoctor, startLocalDate).size());

        //clear tables
        appointmentService.deleteAllAppointments();
        customerService.deleteAllCustomers();
        doctorService.deleteAllDoctors();
    }

    @Test
    void getAppointmentByDoctorDateAndTime(){
        //clear tables
        appointmentService.deleteAllAppointments();
        customerService.deleteAllCustomers();
        doctorService.deleteAllDoctors();

        //given
        LocalDate firstLocalDate = LocalDate.of(2021, Month.JANUARY,19);
        LocalDate secondLocalDate = LocalDate.of(2021, Month.JANUARY,21);
        LocalTime firstLocalTime = LocalTime.of(9,0);
        LocalTime secondLocalTime = LocalTime.of(11,0);
        Doctor firstDoctor = getFirstDoctor();
        Doctor secondDoctor = getSecondDoctor();
        Customer firstCustomer = getFirstCustomer();
        //save
        doctorService.saveDoctor(firstDoctor);
        doctorService.saveDoctor(secondDoctor);
        customerService.saveCustomer(firstCustomer);

        Appointment firstAppointment = new Appointment.Builder()
                .withAppointmentDate(firstLocalDate)
                .withAppointmentTime(firstLocalTime)
                .withDoctor(firstDoctor)
                .build();
        Appointment secondAppointment = new Appointment.Builder()
                .withAppointmentDate(secondLocalDate)
                .withAppointmentTime(secondLocalTime)
                .withDoctor(secondDoctor)
                .withCustomer(firstCustomer)
                .build();
        Appointment thirdAppointment = new Appointment.Builder()
                .withAppointmentDate(firstLocalDate)
                .withAppointmentTime(firstLocalTime)
                .withDoctor(secondDoctor)
                .build();

        appointmentService.saveAppointment(firstAppointment);
        appointmentService.saveAppointment(secondAppointment);
        appointmentService.saveAppointment(thirdAppointment);

        //when
        Appointment firstAppointmentTestMethod =
                appointmentService.getAppointmentByDoctorDateAndTime(firstDoctor,firstLocalDate,firstLocalTime).get();
        Appointment secondAppointmentTestMethod =
                appointmentService.getAppointmentByDoctorDateAndTime(secondDoctor,secondLocalDate,secondLocalTime).get();
        Appointment thirdAppointmentTestMethod =
                appointmentService.getAppointmentByDoctorDateAndTime(secondDoctor,firstLocalDate,firstLocalTime).get();
        //then
        assertEquals(firstAppointment, firstAppointmentTestMethod);
        assertEquals(secondAppointment, secondAppointmentTestMethod);
        assertEquals(thirdAppointment, thirdAppointmentTestMethod);
        assertNotEquals(secondAppointment, firstAppointmentTestMethod);
    }
}
