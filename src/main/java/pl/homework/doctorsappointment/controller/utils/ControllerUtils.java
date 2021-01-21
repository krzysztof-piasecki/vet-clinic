package pl.homework.doctorsappointment.controller.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pl.homework.doctorsappointment.model.Appointment;
import pl.homework.doctorsappointment.model.Customer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

public class ControllerUtils {

    private ControllerUtils(){}
    public static final String WRONG_CUSTOMER_PIN_OR_ID = "Wrong customer pin or id";
    public static final String DATE_OR_TIME_NOT_FOUND = "Date or time not found";
    public static final String APPOINTMENT_NOT_FOUND = "Appointment not found";
    public static final String DOCTOR_NOT_FOUND = "Doctor not found";
    public static final String DIGIT_ID = "digitId";
    public static final String DOCTOR_ID = "doctorId";
    public static final String CODE_PIN = "codePin";
    public static final String LOCAL_DATE = "localDate";
    public static final String LOCAL_TIME = "localTime";
    public static final String START_APPOINTMENT_DATE = "startAppointmentDate";
    public static final String END_APPOINTMENT_DATE = "endAppointmentDate";
    public static final String START_APPOINTMENT_TIME = "startAppointmentTime";
    public static final String END_APPOINTMENT_TIME = "endAppointmentTime";

    public static void checkIfEmpty(Optional<?> optional, String message){
        if (optional.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, message
            );
        }
    }
    public static  void checkIfCorrectCodePin(Customer customer, String codePin){
        if (!customer.getCodePIN().equals(codePin)){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, WRONG_CUSTOMER_PIN_OR_ID);
        }
    }

    public static  void checkIfAppointmentIsAvailable(Appointment appointment){
        if (!Objects.equals(appointment.getCustomer(), null)){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, DATE_OR_TIME_NOT_FOUND);
        }
    }

    public static void checkIfDateIsBlank(String... localDates) {
        for(String localDate: localDates){
            if (localDate.isBlank()){
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, DATE_OR_TIME_NOT_FOUND);
            }
        }
    }

    public static LocalDate parseLocalDate (String localDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(localDate, formatter);
    }

    public static LocalTime parseLocalTime (String localTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalTime.parse(localTime, formatter);
    }
}
