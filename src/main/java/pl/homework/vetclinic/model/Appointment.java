package pl.homework.vetclinic.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    public long getId() {
        return id;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public static final class Builder {
        private LocalDate appointmentDate;
        private LocalTime appointmentTime;
        private Customer customer;
        private Doctor doctor;

        public static Builder anAppointment() {
            return new Builder();
        }

        public Builder withAppointmentDate(LocalDate appointmentDate) {
            this.appointmentDate = appointmentDate;
            return this;
        }
        public Builder withAppointmentTime(LocalTime appointmentTime) {
            this.appointmentTime = appointmentTime;
            return this;
        }
        public Builder withCustomer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Builder withDoctor(Doctor doctor) {
            this.doctor = doctor;
            return this;
        }

        public Appointment build() {
            Appointment appointment = new Appointment();
            appointment.appointmentDate = this.appointmentDate;
            appointment.appointmentTime = this.appointmentTime;
            appointment.customer = this.customer;
            appointment.doctor = this.doctor;
            return appointment;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return id == that.id &&
                Objects.equals(appointmentDate, that.appointmentDate) &&
                Objects.equals(appointmentTime, that.appointmentTime) &&
                Objects.equals(customer, that.customer) &&
                Objects.equals(doctor, that.doctor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, appointmentDate, customer, doctor);
    }
}
