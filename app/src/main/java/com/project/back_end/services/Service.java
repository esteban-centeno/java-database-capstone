package com.project.back_end.services;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Admin;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@org.springframework.stereotype.Service
public class Service {
    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public Service(TokenService tokenService, AdminRepository adminRepository, DoctorRepository doctorRepository,
                   PatientRepository patientRepository, DoctorService doctorService, PatientService patientService) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    public ResponseEntity<Map<String, String>> validateToken(String token, String role) {
        if (tokenService.validateToken(token, role)) return ResponseEntity.ok(Map.of());
        return stringResponse(HttpStatus.UNAUTHORIZED, "error", "Invalid or expired token");
    }

    public ResponseEntity<Map<String, String>> validateAdmin(Admin admin) {
        try {
            Admin persisted = adminRepository.findByUsername(admin.getUsername());
            if (persisted == null || !Objects.equals(persisted.getPassword(), admin.getPassword())) {
                return stringResponse(HttpStatus.UNAUTHORIZED, "error", "Invalid username or password");
            }
            return stringResponse(HttpStatus.OK, "token", tokenService.generateToken(persisted.getUsername()));
        } catch (RuntimeException exception) {
            return stringResponse(HttpStatus.INTERNAL_SERVER_ERROR, "error", "Unable to authenticate admin");
        }
    }

    public Map<String, Object> filterDoctor(String name, String specialty, String time) {
        boolean hasName = hasText(name), hasSpecialty = hasText(specialty), hasTime = hasText(time);
        if (hasName && hasSpecialty && hasTime) return doctorService.filterDoctorsByNameSpecilityandTime(name, specialty, time);
        if (hasName && hasSpecialty) return doctorService.filterDoctorByNameAndSpecility(name, specialty);
        if (hasName && hasTime) return doctorService.filterDoctorByNameAndTime(name, time);
        if (hasSpecialty && hasTime) return doctorService.filterDoctorByTimeAndSpecility(time, specialty);
        if (hasName) return doctorService.findDoctorByName(name);
        if (hasSpecialty) return doctorService.filterDoctorBySpecility(specialty);
        if (hasTime) return doctorService.filterDoctorsByTime(time);
        return Map.of("doctors", doctorService.getDoctors());
    }

    public int validateAppointment(Long doctorId, LocalDate date, LocalTime appointmentTime) {
        if (!doctorRepository.existsById(doctorId)) return -1;
        return doctorService.getDoctorAvailability(doctorId, date).stream()
                .anyMatch(slot -> slotStartsAt(slot, appointmentTime)) ? 1 : 0;
    }

    public int validateAppointment(Doctor doctor, LocalDateTime appointmentTime) {
        if (doctor == null || doctor.getId() == null) return -1;
        return validateAppointment(doctor.getId(), appointmentTime.toLocalDate(), appointmentTime.toLocalTime());
    }

    public int validateAppointment(Appointment appointment) {
        if (appointment == null) return -1;
        return validateAppointment(appointment.getDoctor(), appointment.getAppointmentTime());
    }

    public boolean validatePatient(Patient patient) {
        return patient != null && patientRepository.findByEmailOrPhone(patient.getEmail(), patient.getPhone()) == null;
    }

    public ResponseEntity<Map<String, String>> validatePatientLogin(Patient patient) {
        try {
            Patient persisted = patientRepository.findByEmail(patient.getEmail());
            if (persisted == null || !Objects.equals(persisted.getPassword(), patient.getPassword())) {
                return stringResponse(HttpStatus.UNAUTHORIZED, "error", "Invalid email or password");
            }
            return stringResponse(HttpStatus.OK, "token", tokenService.generateToken(persisted.getEmail()));
        } catch (RuntimeException exception) {
            return stringResponse(HttpStatus.INTERNAL_SERVER_ERROR, "error", "Unable to authenticate patient");
        }
    }

    public ResponseEntity<Map<String, String>> validatePatientLogin(Login login) {
        Patient patient = new Patient();
        patient.setEmail(login.getEmail());
        patient.setPassword(login.getPassword());
        return validatePatientLogin(patient);
    }

    public ResponseEntity<Map<String, Object>> filterPatient(String token, String condition, String doctorName) {
        try {
            Patient patient = patientRepository.findByEmail(tokenService.extractEmail(token));
            if (patient == null) return objectResponse(HttpStatus.NOT_FOUND, "error", "Patient not found");
            if (hasText(condition) && hasText(doctorName)) return patientService.filterByDoctorAndCondition(doctorName, condition, patient.getId());
            if (hasText(condition)) return patientService.filterByCondition(condition, patient.getId());
            if (hasText(doctorName)) return patientService.filterByDoctor(doctorName, patient.getId());
            return patientService.getPatientAppointment(patient.getId());
        } catch (RuntimeException exception) {
            return objectResponse(HttpStatus.UNAUTHORIZED, "error", "Invalid token");
        }
    }

    private ResponseEntity<Map<String, String>> stringResponse(HttpStatus status, String key, String value) {
        return ResponseEntity.status(status).body(Map.of(key, value));
    }

    private ResponseEntity<Map<String, Object>> objectResponse(HttpStatus status, String key, Object value) {
        return ResponseEntity.status(status).body(Map.of(key, value));
    }

    private boolean hasText(String value) { return value != null && !value.isBlank(); }

    private boolean slotStartsAt(String slot, LocalTime time) {
        try { return LocalTime.parse(slot.split("-")[0].trim()).equals(time); }
        catch (RuntimeException exception) { return false; }
    }
// 1. **@Service Annotation**
// The @Service annotation marks this class as a service component in Spring. This allows Spring to automatically detect it through component scanning
// and manage its lifecycle, enabling it to be injected into controllers or other services using @Autowired or constructor injection.

// 2. **Constructor Injection for Dependencies**
// The constructor injects all required dependencies (TokenService, Repositories, and other Services). This approach promotes loose coupling, improves testability,
// and ensures that all required dependencies are provided at object creation time.

// 3. **validateToken Method**
// This method checks if the provided JWT token is valid for a specific user. It uses the TokenService to perform the validation.
// If the token is invalid or expired, it returns a 401 Unauthorized response with an appropriate error message. This ensures security by preventing
// unauthorized access to protected resources.

// 4. **validateAdmin Method**
// This method validates the login credentials for an admin user.
// - It first searches the admin repository using the provided username.
// - If an admin is found, it checks if the password matches.
// - If the password is correct, it generates and returns a JWT token (using the admin’s username) with a 200 OK status.
// - If the password is incorrect, it returns a 401 Unauthorized status with an error message.
// - If no admin is found, it also returns a 401 Unauthorized.
// - If any unexpected error occurs during the process, a 500 Internal Server Error response is returned.
// This method ensures that only valid admin users can access secured parts of the system.

// 5. **filterDoctor Method**
// This method provides filtering functionality for doctors based on name, specialty, and available time slots.
// - It supports various combinations of the three filters.
// - If none of the filters are provided, it returns all available doctors.
// This flexible filtering mechanism allows the frontend or consumers of the API to search and narrow down doctors based on user criteria.

// 6. **validateAppointment Method**
// This method validates if the requested appointment time for a doctor is available.
// - It first checks if the doctor exists in the repository.
// - Then, it retrieves the list of available time slots for the doctor on the specified date.
// - It compares the requested appointment time with the start times of these slots.
// - If a match is found, it returns 1 (valid appointment time).
// - If no matching time slot is found, it returns 0 (invalid).
// - If the doctor doesn’t exist, it returns -1.
// This logic prevents overlapping or invalid appointment bookings.

// 7. **validatePatient Method**
// This method checks whether a patient with the same email or phone number already exists in the system.
// - If a match is found, it returns false (indicating the patient is not valid for new registration).
// - If no match is found, it returns true.
// This helps enforce uniqueness constraints on patient records and prevent duplicate entries.

// 8. **validatePatientLogin Method**
// This method handles login validation for patient users.
// - It looks up the patient by email.
// - If found, it checks whether the provided password matches the stored one.
// - On successful validation, it generates a JWT token and returns it with a 200 OK status.
// - If the password is incorrect or the patient doesn't exist, it returns a 401 Unauthorized with a relevant error.
// - If an exception occurs, it returns a 500 Internal Server Error.
// This method ensures only legitimate patients can log in and access their data securely.

// 9. **filterPatient Method**
// This method filters a patient's appointment history based on condition and doctor name.
// - It extracts the email from the JWT token to identify the patient.
// - Depending on which filters (condition, doctor name) are provided, it delegates the filtering logic to PatientService.
// - If no filters are provided, it retrieves all appointments for the patient.
// This flexible method supports patient-specific querying and enhances user experience on the client side.


}
