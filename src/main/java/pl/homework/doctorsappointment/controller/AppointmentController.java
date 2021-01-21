package pl.homework.doctorsappointment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.homework.doctorsappointment.model.Appointment;
import pl.homework.doctorsappointment.model.Customer;
import pl.homework.doctorsappointment.model.Doctor;
import pl.homework.doctorsappointment.service.AppointmentService;
import pl.homework.doctorsappointment.service.CustomerService;
import pl.homework.doctorsappointment.service.DoctorService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static pl.homework.doctorsappointment.controller.utils.ControllerUtils.*;


@RestController
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private DoctorService doctorService;

    /**
     * To retrieve data from specific appointment the path is "/appointment:{id}" with GET method
     *
     * @param id
     * @return Appointment
     */
    @GetMapping(path = "appointment:{id}")
    public Appointment getAppointmentById(@PathVariable Long id) {
        Optional<Appointment> appointment = appointmentService.getAppointmentById(id);
        checkIfEmpty(appointment, APPOINTMENT_NOT_FOUND);
        return appointment.get();
    }

    /**
     * To create appointment for specific doctor at given date and time the path is "/book/appointment" with POST method
     *
     * @RequestBody :
     * {
     * "digitId": "String",
     * "codePin": "String",
     * "localDate": "String",
     * "localTime": "String",
     * "doctorId": "String"
     * }
     */
    @PostMapping(path = "appointment/book")
    public void makeAppointment(@RequestBody Map<String, String> body) {
        String localDate = body.get(LOCAL_DATE);
        String localTime = body.get(LOCAL_TIME);
        checkIfDateIsBlank(localTime,localDate);

        Optional<Doctor> doctor = doctorService.getDoctorById(Long.parseLong(body.get(DOCTOR_ID)));
        checkIfEmpty(doctor, DOCTOR_NOT_FOUND);

        Optional<Appointment> appointment =
                appointmentService.getAppointmentByDoctorDateAndTime(doctor.get(), parseLocalDate(localDate), parseLocalTime(localTime));
        checkIfEmpty(appointment, APPOINTMENT_NOT_FOUND);
        checkIfAppointmentIsAvailable(appointment.get());

        Optional<Customer> customer = customerService.findByDigitId(body.get(DIGIT_ID));
        checkIfEmpty(customer, WRONG_CUSTOMER_PIN_OR_ID);
        checkIfCorrectCodePin(customer.get(), body.get(CODE_PIN));
        appointment.get().setCustomer(customer.get());
        appointmentService.saveAppointment(appointment.get());
    }

    /**
     * To cancel the appointment the path is "/cancel/appointment:{id}" with DELETE method
     *
     * @param id (appointmentId)
     * @RequestBody :
     * {
     * "digitId": "String",
     * "codePin": "String"
     * }
     */
    @DeleteMapping(path = "appointment/cancel:{id}")
    public void deleteAppointment(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Optional<Customer> customer = customerService.findByDigitId(body.get(DIGIT_ID));
        checkIfEmpty(customer, WRONG_CUSTOMER_PIN_OR_ID);
        checkIfCorrectCodePin(customer.get(), body.get(CODE_PIN));
        Optional<Appointment> appointment = appointmentService.getAppointmentById(id);
        checkIfEmpty(appointment, APPOINTMENT_NOT_FOUND);

        appointment.get().setCustomer(null);
        appointmentService.saveAppointment(appointment.get());
    }

    /**
     * To get all the available appointments the path is "appointment/available" with GET method
     *
     * @return List<Appointment>
     * @RequestBody :
     * {
     * "localDate": "String",
     * "doctorId": "String"
     * }
     */
    @GetMapping(path = "appointment/available")
    public List<Appointment> getAvailableAppointmentByDateAndDoctor(@RequestBody Map<String, String> body) {
        String localDate = body.get(LOCAL_DATE);
        checkIfDateIsBlank(localDate);

        Optional<Doctor> doctor = doctorService.getDoctorById(Long.parseLong(body.get(DOCTOR_ID)));
        checkIfEmpty(doctor, DOCTOR_NOT_FOUND);

        return appointmentService.getAvailableAppointmentByDateAndDoctor(doctor.get(), parseLocalDate(localDate))
                .stream().filter(e -> Objects.equals(e.getCustomer(), null)).collect(Collectors.toList());
    }

    /**
     * To create available appointment for specific doctor from certain date to date
     * and from certain start time and end time the path is "/appointment/doctor" with POST method
     *
     * @RequestBody :
     * {
     * "doctorId": "String",
     * "startAppointmentDate": "String",
     * "endAppointmentDate": "String",
     * "startAppointmentTime": "String",
     * "endAppointmentTime": "String"
     * }
     */
    @PostMapping(path = "appointment/doctor")
    public void createAvailableAppointmentsByDoctorTimeAndDate(@RequestBody Map<String,String> body){
        String startAppointmentDate = body.get(START_APPOINTMENT_DATE);
        String endAppointmentDate = body.get(END_APPOINTMENT_DATE);
        String startAppointmentTime = body.get(START_APPOINTMENT_TIME);
        String endAppointmentTime = body.get(END_APPOINTMENT_TIME);
        Long doctorId = Long.valueOf(body.get(DOCTOR_ID));

        checkIfDateIsBlank(startAppointmentDate,endAppointmentDate,startAppointmentTime,endAppointmentTime);

        Optional<Doctor> doctor = doctorService.getDoctorById(doctorId);
        checkIfEmpty(doctor, DOCTOR_NOT_FOUND);

        appointmentService.saveAppointmentWithStartDateAndTime(
                doctor.get(),
                parseLocalDate(startAppointmentDate),
                parseLocalDate(endAppointmentDate),
                parseLocalTime(startAppointmentTime),
                parseLocalTime(endAppointmentTime)
        );
    }
}
