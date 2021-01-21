package pl.homework.doctorsappointment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.homework.doctorsappointment.model.Doctor;
import pl.homework.doctorsappointment.repository.DoctorRepository;

import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    public void saveDoctor(Doctor doctor){
        doctorRepository.save(doctor);
    }

    public void deleteDoctorById(long id){
        doctorRepository.deleteById(id);
    }

    public Optional<Doctor> getDoctorById(long id){
        return doctorRepository.findById(id);
    }

    public Iterable<Doctor> getAllDoctors() {return doctorRepository.findAll(); }

    public void deleteAllDoctors() {doctorRepository.deleteAll(); }

}
