package pl.homework.doctorsappointment.repository;

import org.springframework.data.repository.CrudRepository;
import pl.homework.doctorsappointment.model.Doctor;

public interface DoctorRepository extends CrudRepository<Doctor, Long> {
}
