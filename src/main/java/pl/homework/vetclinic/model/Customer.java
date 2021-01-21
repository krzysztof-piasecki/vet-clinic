package pl.homework.vetclinic.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String digitId;
    private String codePIN;
    private String firstName;
    private String lastName;


    public long getId() {
        return id;
    }

    public String getDigitId() {
        return digitId;
    }

    public void setDigitId(String digitId) {
        this.digitId = digitId;
    }

    public String getCodePIN() {
        return codePIN;
    }

    public void setCodePIN(String codePIN) {
        this.codePIN = codePIN;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public static final class Builder {
        private String digitId;
        private String codePIN;
        private String firstName;
        private String lastName;

        public static Builder aCustomer() {
            return new Builder();
        }

        public Builder withDigitId(String digitId) {
            this.digitId = digitId;
            return this;
        }

        public Builder withCodePIN(String codePIN) {
            this.codePIN = codePIN;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Customer build() {
            Customer customer = new Customer();
            customer.firstName = this.firstName;
            customer.digitId = this.digitId;
            customer.lastName = this.lastName;
            customer.codePIN = this.codePIN;
            return customer;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id &&
                Objects.equals(digitId, customer.digitId) &&
                Objects.equals(codePIN, customer.codePIN) &&
                Objects.equals(firstName, customer.firstName) &&
                Objects.equals(lastName, customer.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, digitId, codePIN, firstName, lastName);
    }
}
