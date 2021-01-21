package pl.homework.vetclinic.repository;

import org.springframework.data.repository.CrudRepository;
import pl.homework.vetclinic.model.Doctor;

public interface DoctorRepository extends CrudRepository<Doctor, Long> {
}
