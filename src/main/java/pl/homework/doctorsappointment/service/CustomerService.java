package pl.homework.doctorsappointment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.homework.doctorsappointment.model.Customer;
import pl.homework.doctorsappointment.repository.CustomerRepository;

import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public void saveCustomer(Customer customer){
        customerRepository.save(customer);
    }

    public void deleteCustomerById(long id){
        customerRepository.deleteById(id);
    }

    public Optional<Customer> getCustomerById(long id){
        return customerRepository.findById(id);
    }

    public Iterable<Customer> getAllCustomers() {return customerRepository.findAll();}

    public void deleteAllCustomers() {customerRepository.deleteAll();}

    public Optional<Customer> findByDigitId(String digitId){return customerRepository.findByDigitId(digitId);}
}
