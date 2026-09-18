package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {
    private final DoctorService doctorService;
    private final Service service;

    public DoctorController(DoctorService doctorService, Service service) {
        this.doctorService = doctorService;
        this.service = service;
    }

    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<?> getDoctorAvailability(@PathVariable String user, @PathVariable Long doctorId,
                                                    @PathVariable LocalDate date, @PathVariable String token) {
        if (!isAuthorized(token, user)) return unauthorized();
        return ResponseEntity.ok(Map.of("availableTimes", doctorService.getDoctorAvailability(doctorId, date)));
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctor() {
        return ResponseEntity.ok(Map.of("doctors", doctorService.getDoctors()));
    }

    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> saveDoctor(@Valid @RequestBody Doctor doctor, @PathVariable String token) {
        if (!isAuthorized(token, "admin")) return unauthorized();
        int result = doctorService.saveDoctor(doctor);
        if (result == 1) return message(HttpStatus.CREATED, "Doctor created");
        if (result == -1) return message(HttpStatus.CONFLICT, "A doctor with this email already exists");
        return message(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create doctor");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> doctorLogin(@RequestBody Login login) {
        return doctorService.validateDoctor(login);
    }

    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateDoctor(@Valid @RequestBody Doctor doctor, @PathVariable String token) {
        if (!isAuthorized(token, "admin")) return unauthorized();
        int result = doctorService.updateDoctor(doctor);
        if (result == 1) return message(HttpStatus.OK, "Doctor updated");
        if (result == -1) return message(HttpStatus.NOT_FOUND, "Doctor not found");
        return message(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update doctor");
    }

    @DeleteMapping("/{doctorId}/{token}")
    public ResponseEntity<Map<String, String>> deleteDoctor(@PathVariable Long doctorId, @PathVariable String token) {
        if (!isAuthorized(token, "admin")) return unauthorized();
        int result = doctorService.deleteDoctor(doctorId);
        if (result == 1) return message(HttpStatus.OK, "Doctor deleted");
        if (result == -1) return message(HttpStatus.NOT_FOUND, "Doctor not found");
        return message(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete doctor");
    }

    @GetMapping("/{name}/{time}/{specialty}")
    public Map<String, Object> filter(@PathVariable String name, @PathVariable String time,
                                      @PathVariable String specialty) {
        return service.filterDoctor(name, specialty, time);
    }

    private boolean isAuthorized(String token, String role) { return service.validateToken(token, role).getStatusCode().is2xxSuccessful(); }
    private ResponseEntity<Map<String, String>> unauthorized() { return message(HttpStatus.UNAUTHORIZED, "Invalid or expired token"); }
    private ResponseEntity<Map<String, String>> message(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("message", message));
    }

// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST controller that serves JSON responses.
//    - Use `@RequestMapping("${api.path}doctor")` to prefix all endpoints with a configurable API path followed by "doctor".
//    - This class manages doctor-related functionalities such as registration, login, updates, and availability.


// 2. Autowire Dependencies:
//    - Inject `DoctorService` for handling the core logic related to doctors (e.g., CRUD operations, authentication).
//    - Inject the shared `Service` class for general-purpose features like token validation and filtering.


// 3. Define the `getDoctorAvailability` Method:
//    - Handles HTTP GET requests to check a specific doctor’s availability on a given date.
//    - Requires `user` type, `doctorId`, `date`, and `token` as path variables.
//    - First validates the token against the user type.
//    - If the token is invalid, returns an error response; otherwise, returns the availability status for the doctor.


// 4. Define the `getDoctor` Method:
//    - Handles HTTP GET requests to retrieve a list of all doctors.
//    - Returns the list within a response map under the key `"doctors"` with HTTP 200 OK status.


// 5. Define the `saveDoctor` Method:
//    - Handles HTTP POST requests to register a new doctor.
//    - Accepts a validated `Doctor` object in the request body and a token for authorization.
//    - Validates the token for the `"admin"` role before proceeding.
//    - If the doctor already exists, returns a conflict response; otherwise, adds the doctor and returns a success message.


// 6. Define the `doctorLogin` Method:
//    - Handles HTTP POST requests for doctor login.
//    - Accepts a validated `Login` DTO containing credentials.
//    - Delegates authentication to the `DoctorService` and returns login status and token information.


// 7. Define the `updateDoctor` Method:
//    - Handles HTTP PUT requests to update an existing doctor's information.
//    - Accepts a validated `Doctor` object and a token for authorization.
//    - Token must belong to an `"admin"`.
//    - If the doctor exists, updates the record and returns success; otherwise, returns not found or error messages.


// 8. Define the `deleteDoctor` Method:
//    - Handles HTTP DELETE requests to remove a doctor by ID.
//    - Requires both doctor ID and an admin token as path variables.
//    - If the doctor exists, deletes the record and returns a success message; otherwise, responds with a not found or error message.


// 9. Define the `filter` Method:
//    - Handles HTTP GET requests to filter doctors based on name, time, and specialty.
//    - Accepts `name`, `time`, and `speciality` as path variables.
//    - Calls the shared `Service` to perform filtering logic and returns matching doctors in the response.


}
