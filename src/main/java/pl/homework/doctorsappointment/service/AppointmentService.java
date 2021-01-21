package pl.homework.doctorsappointment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.homework.doctorsappointment.model.Appointment;
import pl.homework.doctorsappointment.model.Doctor;
import pl.homework.doctorsappointment.repository.AppointmentRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public void saveAppointment(Appointment appointment){appointmentRepository.save(appointment);}

    public void saveAppointmentWithStartDateAndTime(Doctor doctor, LocalDate startAppointmentDate,LocalDate endAppointmentDate,
                                                    LocalTime startAppointmentTime, LocalTime endAppointmentTime){
        List<Appointment> appointments = new ArrayList<>();
        while(!startAppointmentDate.isAfter(endAppointmentDate)){
            LocalTime startAppointmentTimeLoop = LocalTime.of(startAppointmentTime.getHour(), startAppointmentTime.getMinute());
            while(startAppointmentTimeLoop.isBefore(endAppointmentTime)){
                appointments.add(new Appointment.Builder()
                        .withDoctor(doctor)
                        .withAppointmentDate(startAppointmentDate)
                        .withAppointmentTime(startAppointmentTimeLoop)
                        .build());
                startAppointmentTimeLoop = startAppointmentTimeLoop.plusHours(1);
            }
            startAppointmentDate = startAppointmentDate.plusDays(1); }
        appointmentRepository.saveAll(appointments);
    }

    public void deleteAppointmentById(long id){
        appointmentRepository.deleteById(id);
    }

    public Optional<Appointment> getAppointmentById(long id){
        return appointmentRepository.findById(id);
    }

    public Iterable<Appointment> getAllAppointments(){ return appointmentRepository.findAll(); }

    public void deleteAllAppointments() {appointmentRepository.deleteAll(); }

    public List<Appointment> getAvailableAppointmentByDateAndDoctor(Doctor doctor, LocalDate appointmentDate){
        return appointmentRepository.getAvailableAppointmentByDateAndDoctor(doctor,appointmentDate);
    }

    public Optional<Appointment> getAppointmentByDoctorDateAndTime(Doctor doctor, LocalDate localDate, LocalTime localTime){
        return appointmentRepository.getAppointmentByDoctorDateAndTime(doctor, localDate, localTime);
    }
}
