package pl.homework.doctorsappointment.repository;

import org.springframework.data.repository.CrudRepository;
import pl.homework.doctorsappointment.model.Customer;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    Optional<Customer> findByDigitId(String digitId);
}
