package pl.homework.doctorsappointment.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.homework.doctorsappointment.model.Appointment;
import pl.homework.doctorsappointment.model.Doctor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends CrudRepository<Appointment, Long> {
    @Query("SELECT a " +
            "FROM Appointment a " +
            "WHERE a.doctor=?1 " +
            "AND DATE(a.appointmentDate) LIKE DATE(?2)" +
            "AND a.customer is null")
    List<Appointment> getAvailableAppointmentByDateAndDoctor(Doctor doctor,
                                                       LocalDate selectedDate);
    @Query("SELECT a " +
            "FROM Appointment a " +
            "WHERE a.doctor=?1 " +
            "AND DATE(a.appointmentDate) LIKE DATE(?2)" +
            "AND TIME(a.appointmentTime) LIKE TIME(?3)")
    Optional <Appointment> getAppointmentByDoctorDateAndTime(Doctor doctor,
                                                           LocalDate selectedDate,
                                                           LocalTime localTime);
}
