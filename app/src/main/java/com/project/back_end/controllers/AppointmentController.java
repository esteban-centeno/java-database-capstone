package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
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
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final Service service;

    public AppointmentController(AppointmentService appointmentService, Service service) {
        this.appointmentService = appointmentService;
        this.service = service;
    }

    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<?> getAppointments(@PathVariable LocalDate date, @PathVariable String patientName,
                                              @PathVariable String token) {
        if (!isAuthorized(token, "doctor")) return unauthorized();
        return ResponseEntity.ok(appointmentService.getAppointments(date, patientName, token));
    }

    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> bookAppointment(@Valid @RequestBody Appointment appointment,
                                                                 @PathVariable String token) {
        if (!isAuthorized(token, "patient")) return unauthorized();
        int result = service.validateAppointment(appointment);
        if (result == -1) return message(HttpStatus.NOT_FOUND, "Doctor not found");
        if (result == 0) return message(HttpStatus.CONFLICT, "Requested appointment time is unavailable");
        return appointmentService.bookAppointment(appointment) == 1
                ? message(HttpStatus.CREATED, "Appointment booked")
                : message(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to book appointment");
    }

    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateAppointment(@Valid @RequestBody Appointment appointment,
                                                                   @PathVariable String token) {
        if (!isAuthorized(token, "patient")) return unauthorized();
        return appointmentService.updateAppointment(appointment, token);
    }

    @DeleteMapping("/{appointmentId}/{token}")
    public ResponseEntity<Map<String, String>> cancelAppointment(@PathVariable Long appointmentId,
                                                                   @PathVariable String token) {
        if (!isAuthorized(token, "patient")) return unauthorized();
        return appointmentService.cancelAppointment(appointmentId, token);
    }

    private boolean isAuthorized(String token, String role) { return service.validateToken(token, role).getStatusCode().is2xxSuccessful(); }
    private ResponseEntity<Map<String, String>> unauthorized() { return message(HttpStatus.UNAUTHORIZED, "Invalid or expired token"); }
    private ResponseEntity<Map<String, String>> message(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("message", message));
    }

// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST API controller.
//    - Use `@RequestMapping("/appointments")` to set a base path for all appointment-related endpoints.
//    - This centralizes all routes that deal with booking, updating, retrieving, and canceling appointments.


// 2. Autowire Dependencies:
//    - Inject `AppointmentService` for handling the business logic specific to appointments.
//    - Inject the general `Service` class, which provides shared functionality like token validation and appointment checks.


// 3. Define the `getAppointments` Method:
//    - Handles HTTP GET requests to fetch appointments based on date and patient name.
//    - Takes the appointment date, patient name, and token as path variables.
//    - First validates the token for role `"doctor"` using the `Service`.
//    - If the token is valid, returns appointments for the given patient on the specified date.
//    - If the token is invalid or expired, responds with the appropriate message and status code.


// 4. Define the `bookAppointment` Method:
//    - Handles HTTP POST requests to create a new appointment.
//    - Accepts a validated `Appointment` object in the request body and a token as a path variable.
//    - Validates the token for the `"patient"` role.
//    - Uses service logic to validate the appointment data (e.g., check for doctor availability and time conflicts).
//    - Returns success if booked, or appropriate error messages if the doctor ID is invalid or the slot is already taken.


// 5. Define the `updateAppointment` Method:
//    - Handles HTTP PUT requests to modify an existing appointment.
//    - Accepts a validated `Appointment` object and a token as input.
//    - Validates the token for `"patient"` role.
//    - Delegates the update logic to the `AppointmentService`.
//    - Returns an appropriate success or failure response based on the update result.


// 6. Define the `cancelAppointment` Method:
//    - Handles HTTP DELETE requests to cancel a specific appointment.
//    - Accepts the appointment ID and a token as path variables.
//    - Validates the token for `"patient"` role to ensure the user is authorized to cancel the appointment.
//    - Calls `AppointmentService` to handle the cancellation process and returns the result.


}
