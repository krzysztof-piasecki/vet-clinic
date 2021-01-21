package pl.homework.vetclinic.controller;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import pl.homework.vetclinic.BaseTest;
import pl.homework.vetclinic.model.Doctor;
import pl.homework.vetclinic.service.AppointmentService;
import pl.homework.vetclinic.service.CustomerService;
import pl.homework.vetclinic.service.DoctorService;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DoctorControllerTest extends BaseTest {
    @LocalServerPort
    int randomServerPort;

    @Autowired
    private DoctorService doctorService;

    @Test
    void getDoctorById() {
        port = randomServerPort;

        clearTables();

        //given
        String path = "doctor:{id}";
        Doctor doctor = getFirstDoctor();
        doctorService.saveDoctor(doctor);
        Long id = doctorService.getAllDoctors().iterator().next().getId();

        when()
                .get(path, id)
                .then()
                .statusCode(200)
                .body("firstName", equalTo(doctor.getFirstName()))
                .body("lastName", equalTo(doctor.getLastName()))
                .body("id", hasToString(String.valueOf(doctor.getId())));

        clearTables();
    }

    @Test
    void saveDoctor() {
        port = randomServerPort;

        clearTables();

        //given
        Doctor doctor = getFirstDoctor();
        String path = "doctor";
        String requestString = "{" +
                "\"firstName\": \"" + doctor.getFirstName() + "\"," +
                "\"lastName\": \"" + doctor.getLastName() + "\"}";

        given()
                .contentType(ContentType.JSON)
                .body(requestString)
                .post(path)
                .then()
                .statusCode(200)
                .extract()
                .response();

        Doctor jsonDoctor = doctorService.getAllDoctors().iterator().next();

        assertEquals(jsonDoctor.getFirstName(), doctor.getFirstName());
        assertEquals(jsonDoctor.getLastName(), doctor.getLastName());

        clearTables();
    }

    @Test
    void deleteDoctorById() {
        port = randomServerPort;

        clearTables();

        //given
        String path = "doctor:{id}";
        Doctor doctor = getFirstDoctor();
        doctorService.saveDoctor(doctor);
        Long id = doctorService.getAllDoctors().iterator().next().getId();

        assertFalse(doctorService.getDoctorById(id).isEmpty());

        when()
                .delete(path, id)
                .then()
                .statusCode(200);

        assertTrue(doctorService.getDoctorById(id).isEmpty());

        clearTables();
    }
}
