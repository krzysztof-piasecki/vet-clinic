package pl.homework.doctorsappointment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.homework.doctorsappointment.model.Doctor;
import pl.homework.doctorsappointment.service.DoctorService;

import java.util.Optional;

import static pl.homework.doctorsappointment.controller.utils.ControllerUtils.*;

@RestController
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    /**
     * To get doctor entity the path is "/doctor:{id}" with GET method
     * @param id (doctor's id)
     */
    @GetMapping(path = "doctor:{id}")
    public Doctor getDoctorById(@PathVariable("id") Long id) {
        Optional<Doctor> doctor = doctorService.getDoctorById(id);
        checkIfEmpty(doctor, DOCTOR_NOT_FOUND);
        return doctorService.getDoctorById(id).get();
    }

    /**
     * To delete doctor entity the path is "/doctor:{id}" with DELETE method
     * @param id (doctor's id)
     */
    @DeleteMapping(path = "doctor:{id}")
    public void deleteDoctorById(@PathVariable("id") Long id) {
        Optional<Doctor> doctor = doctorService.getDoctorById(id);
        checkIfEmpty(doctor, DOCTOR_NOT_FOUND);
        doctorService.deleteDoctorById(id);
    }
    /**
     * To create doctor entity the path is "/doctor" with POST method
     * @RequestBody :
     * {
     * "firstName": "",
     * "lastName": "",
     * }
     */
    @PostMapping(path = "doctor")
    public void saveDoctor(@RequestBody Doctor doctor) {
        doctorService.saveDoctor(doctor);
    }
}

