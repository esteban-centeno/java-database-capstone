package com.project.back_end.services;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public PatientService(PatientRepository patientRepository, AppointmentRepository appointmentRepository,
                          TokenService tokenService) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    public int createPatient(Patient patient) {
        try { patientRepository.save(patient); return 1; }
        catch (RuntimeException exception) { return 0; }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> getPatientAppointment(Long patientId) {
        try {
            return appointmentsResponse(toDtos(appointmentRepository.findByPatientId(patientId)));
        } catch (RuntimeException exception) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to retrieve appointments");
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> filterByCondition(String condition, Long patientId) {
        Integer status = statusFor(condition);
        if (status == null) return error(HttpStatus.BAD_REQUEST, "Condition must be 'past' or 'future'");
        try {
            return appointmentsResponse(toDtos(
                    appointmentRepository.findByPatient_IdAndStatusOrderByAppointmentTimeAsc(patientId, status)));
        } catch (RuntimeException exception) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to filter appointments");
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> filterByDoctor(String doctorName, Long patientId) {
        try {
            return appointmentsResponse(toDtos(appointmentRepository.filterByDoctorNameAndPatientId(doctorName, patientId)));
        } catch (RuntimeException exception) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to filter appointments");
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> filterByDoctorAndCondition(String doctorName, String condition, Long patientId) {
        Integer status = statusFor(condition);
        if (status == null) return error(HttpStatus.BAD_REQUEST, "Condition must be 'past' or 'future'");
        try {
            return appointmentsResponse(toDtos(
                    appointmentRepository.filterByDoctorNameAndPatientIdAndStatus(doctorName, patientId, status)));
        } catch (RuntimeException exception) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to filter appointments");
        }
    }

    public ResponseEntity<Map<String, Object>> getPatientDetails(String token) {
        try {
            Patient patient = patientRepository.findByEmail(tokenService.extractEmail(token));
            return patient == null ? error(HttpStatus.NOT_FOUND, "Patient not found")
                    : ResponseEntity.ok(Map.of("patient", patient));
        } catch (RuntimeException exception) {
            return error(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }

    private Integer statusFor(String condition) {
        if ("future".equalsIgnoreCase(condition)) return 0;
        if ("past".equalsIgnoreCase(condition)) return 1;
        return null;
    }

    private List<AppointmentDTO> toDtos(List<Appointment> appointments) {
        return appointments.stream().map(appointment -> new AppointmentDTO(
                appointment.getId(), appointment.getDoctor().getId(), appointment.getDoctor().getName(),
                appointment.getPatient().getId(), appointment.getPatient().getName(),
                appointment.getPatient().getEmail(), appointment.getPatient().getPhone(),
                appointment.getPatient().getAddress(), appointment.getAppointmentTime(), appointment.getStatus())).toList();
    }

    private ResponseEntity<Map<String, Object>> appointmentsResponse(List<AppointmentDTO> appointments) {
        return ResponseEntity.ok(Map.of("appointments", appointments));
    }

    private ResponseEntity<Map<String, Object>> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("error", message));
    }
// 1. **Add @Service Annotation**:
//    - The `@Service` annotation is used to mark this class as a Spring service component. 
//    - It will be managed by Spring's container and used for business logic related to patients and appointments.
//    - Instruction: Ensure that the `@Service` annotation is applied above the class declaration.

// 2. **Constructor Injection for Dependencies**:
//    - The `PatientService` class has dependencies on `PatientRepository`, `AppointmentRepository`, and `TokenService`.
//    - These dependencies are injected via the constructor to maintain good practices of dependency injection and testing.
//    - Instruction: Ensure constructor injection is used for all the required dependencies.

// 3. **createPatient Method**:
//    - Creates a new patient in the database. It saves the patient object using the `PatientRepository`.
//    - If the patient is successfully saved, the method returns `1`; otherwise, it logs the error and returns `0`.
//    - Instruction: Ensure that error handling is done properly and exceptions are caught and logged appropriately.

// 4. **getPatientAppointment Method**:
//    - Retrieves a list of appointments for a specific patient, based on their ID.
//    - The appointments are then converted into `AppointmentDTO` objects for easier consumption by the API client.
//    - This method is marked as `@Transactional` to ensure database consistency during the transaction.
//    - Instruction: Ensure that appointment data is properly converted into DTOs and the method handles errors gracefully.

// 5. **filterByCondition Method**:
//    - Filters appointments for a patient based on the condition (e.g., "past" or "future").
//    - Retrieves appointments with a specific status (0 for future, 1 for past) for the patient.
//    - Converts the appointments into `AppointmentDTO` and returns them in the response.
//    - Instruction: Ensure the method correctly handles "past" and "future" conditions, and that invalid conditions are caught and returned as errors.

// 6. **filterByDoctor Method**:
//    - Filters appointments for a patient based on the doctor's name.
//    - It retrieves appointments where the doctor’s name matches the given value, and the patient ID matches the provided ID.
//    - Instruction: Ensure that the method correctly filters by doctor's name and patient ID and handles any errors or invalid cases.

// 7. **filterByDoctorAndCondition Method**:
//    - Filters appointments based on both the doctor's name and the condition (past or future) for a specific patient.
//    - This method combines filtering by doctor name and appointment status (past or future).
//    - Converts the appointments into `AppointmentDTO` objects and returns them in the response.
//    - Instruction: Ensure that the filter handles both doctor name and condition properly, and catches errors for invalid input.

// 8. **getPatientDetails Method**:
//    - Retrieves patient details using the `tokenService` to extract the patient's email from the provided token.
//    - Once the email is extracted, it fetches the corresponding patient from the `patientRepository`.
//    - It returns the patient's information in the response body.
    //    - Instruction: Make sure that the token extraction process works correctly and patient details are fetched properly based on the extracted email.

// 9. **Handling Exceptions and Errors**:
//    - The service methods handle exceptions using try-catch blocks and log any issues that occur. If an error occurs during database operations, the service responds with appropriate HTTP status codes (e.g., `500 Internal Server Error`).
//    - Instruction: Ensure that error handling is consistent across the service, with proper logging and meaningful error messages returned to the client.

// 10. **Use of DTOs (Data Transfer Objects)**:
//    - The service uses `AppointmentDTO` to transfer appointment-related data between layers. This ensures that sensitive or unnecessary data (e.g., password or private patient information) is not exposed in the response.
//    - Instruction: Ensure that DTOs are used appropriately to limit the exposure of internal data and only send the relevant fields to the client.



}
