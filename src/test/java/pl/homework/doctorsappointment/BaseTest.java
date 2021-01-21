package pl.homework.doctorsappointment;

import pl.homework.doctorsappointment.model.Customer;
import pl.homework.doctorsappointment.model.Doctor;

public class BaseTest {

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
}
