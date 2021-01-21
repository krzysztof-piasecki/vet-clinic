package pl.homework.vetclinic;

import org.springframework.beans.factory.annotation.Autowired;
import pl.homework.vetclinic.model.Customer;
import pl.homework.vetclinic.model.Doctor;
import pl.homework.vetclinic.service.AppointmentService;
import pl.homework.vetclinic.service.CustomerService;
import pl.homework.vetclinic.service.DoctorService;

public class BaseTest {

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private CustomerService customerService;

    protected Customer getFirstCustomer(){
        return new Customer.Builder()
                .withFirstName("Jan")
                .withLastName("Kowalski")
                .withCodePIN("0000")
                .withDigitId("0000")
                .build();
    }

    protected Customer getSecondCustomer(){
        return new Customer.Builder()
                .withFirstName("Jurand")
                .withLastName("Ze Spychowa")
                .withCodePIN("1111")
                .withDigitId("1111")
                .build();
    }

    protected Customer getThirdCustomer(){
        return new Customer.Builder()
                .withFirstName("Tomasz")
                .withLastName("Nowak")
                .withCodePIN("2222")
                .withDigitId("2222")
                .build();
    }

    protected Doctor getFirstDoctor(){
        return new Doctor.Builder()
                .withFirstName("Sanitariusz")
                .withLastName("Mariusz")
                .build();
    }

    protected Doctor getSecondDoctor(){
        return new Doctor.Builder()
                .withFirstName("Holy")
                .withLastName("Paladin")
                .build();
    }

    protected void clearTables() {
        appointmentService.deleteAllAppointments();
        customerService.deleteAllCustomers();
        doctorService.deleteAllDoctors();
    }
}
