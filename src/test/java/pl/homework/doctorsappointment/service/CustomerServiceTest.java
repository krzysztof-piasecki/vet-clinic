package pl.homework.doctorsappointment.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.homework.doctorsappointment.BaseTest;
import pl.homework.doctorsappointment.model.Customer;

import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

@SpringBootTest
@ActiveProfiles("test")
class CustomerServiceTest extends BaseTest {

    @Autowired
    private CustomerService customerService;

    @Test
    void saveAndDeleteCustomers(){
        //clear the table
        customerService.deleteAllCustomers();

        //given
        Customer firstCustomer = getFirstCustomer();
        Customer thirdCustomer = getThirdCustomer();
        Customer secondCustomer = getSecondCustomer();

        //when
        customerService.saveCustomer(firstCustomer);
        customerService.saveCustomer(secondCustomer);
        customerService.saveCustomer(thirdCustomer);

        //check if the table contains three items
        assertEquals(3, StreamSupport.stream(customerService.getAllCustomers().spliterator(), false).count());

        //then
        assertEquals(firstCustomer, customerService.getCustomerById(firstCustomer.getId()).get());
        assertNotEquals(secondCustomer, customerService.getCustomerById(firstCustomer.getId()).get());

        assertEquals(secondCustomer,customerService.getCustomerById(secondCustomer.getId()).get());
        assertNotEquals(thirdCustomer, customerService.getCustomerById(secondCustomer.getId()).get());

        assertEquals(thirdCustomer,customerService.getCustomerById(thirdCustomer.getId()).get());
        assertNotEquals(firstCustomer, customerService.getCustomerById(thirdCustomer.getId()).get());

        //delete
        customerService.deleteCustomerById(firstCustomer.getId());

        //then
        assertTrue(customerService.getCustomerById(firstCustomer.getId()).isEmpty());
        assertFalse(customerService.getCustomerById(secondCustomer.getId()).isEmpty());
        //check if the table contains two items
        assertEquals(2, StreamSupport.stream(customerService.getAllCustomers().spliterator(), false).count());

        //clear the table
        customerService.deleteAllCustomers();
        }

    @Test
    void getCustomerById(){
        //clear table
        customerService.deleteAllCustomers();

        //given
        Customer customer = getFirstCustomer();

        //then
        customerService.saveCustomer(customer);
        assertEquals(customer, customerService.getCustomerById(customer.getId()).get());

        //clear table
        customerService.deleteAllCustomers();
    }

    @Test
    void getCustomerByDigitId(){
        //clear table
        customerService.deleteAllCustomers();

        //given
        Customer customer = getFirstCustomer();

        //then
        customerService.saveCustomer(customer);
        assertEquals(customer, customerService.findByDigitId(customer.getDigitId()).get());

        //clear table
        customerService.deleteAllCustomers();
    }
}
