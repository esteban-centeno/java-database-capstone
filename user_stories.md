# User Stories

## Admin User Stories

**Title:**
_As an Admin, I want Log into the portal with your username and password, so that I can manage the platform securely._

**Acceptance Criteria:**
1. The login is successful is valid username and password are provided
2. The login is unsuccessful is either username or password are not valid.

**Priority:** High

**Story Points:** [Estimated Effort in Points]

**Notes:**
- [Additional information or edge cases]

---

**Title:**
_As an Admin, I want to log out of the portal, so that system access is protected._

**Acceptance Criteria:**
1. Logout is successful
2. Admin functionality is no longer available

**Priority:** [High/Medium/Low]

**Story Points:** [Estimated Effort in Points]

**Notes:**
- [Additional information or edge cases]

---

**Title:**
_As an Admin, I want to delete doctor's profile from the portal, so that it is no longer available._

**Acceptance Criteria:**
1. The doctor's profile is remove from the application.

**Priority:** [High/Medium/Low]

**Story Points:** [Estimated Effort in Points]

**Notes:**
- [Additional information or edge cases]

---

**Title:**
_As an Admin, I want to run a stored procedure in MySQL CLI, so that I can get the number of appointments per month and track usage statistics._

**Acceptance Criteria:**
1. Stored procedure is created.
2. The stored procedure can be run by the Admin.
3. The number of appointments and track usage is available.

**Priority:** [High/Medium/Low]

**Story Points:** [Estimated Effort in Points]

**Notes:**
- [Additional information or edge cases]

---

**Title:**
_As an admin, I want to add doctors to the portal, so that new doctors are registered._

**Acceptance Criteria:**
1. Doctors are available in the application.

**Priority:** [High/Medium/Low]

**Story Points:** [Estimated Effort in Points]

**Notes:**
- [Additional information or edge cases]

---

## Patient User Stories

**Title:**  
_As a Patient, I want to view a list of doctors without logging in, so that I can explore my options before registering._

**Acceptance Criteria:**
1. The patient can access the list of available doctors without being authenticated.
2. The list displays relevant information for each doctor, including at least the doctor's name and specialty.
3. The patient is not required to create an account or log in to view the list of doctors.

**Priority:** High/Medium/Low

**Story Points:** TBD

**Notes:**
- The doctor list should be publicly accessible.
- Appointment booking and other patient-specific actions require authentication.

---

**Title:**  
_As a Patient, I want to sign up using my email and password, so that I can book appointments._

**Acceptance Criteria:**
1. The patient can create an account by providing a valid email address and password.
2. The system prevents registration using an email address that is already associated with an existing account.
3. Upon successful registration, the patient account is created and is available for login.

**Priority:** High/Medium/Low

**Story Points:** TBD

**Notes:**
- Email and password validation should follow the application's authentication requirements.
- Password storage should follow secure practices.

---

**Title:**  
_As a Patient, I want to log into the portal, so that I can manage my bookings._

**Acceptance Criteria:**
1. The patient can log in using a registered email address and password.
2. The system authenticates the patient when valid credentials are provided.
3. The system displays an appropriate error message when invalid credentials are entered.
4. After successful authentication, the patient is granted access to booking management features.

**Priority:** High/Medium/Low

**Story Points:** TBD

**Notes:**
- Only authenticated patients can access protected portal features.

---

**Title:**  
_As a Patient, I want to log out of the portal, so that I can secure my account._

**Acceptance Criteria:**
1. The patient can log out from an authenticated session.
2. The system terminates the active session after logout.
3. Accessing protected pages after logout requires the patient to authenticate again.

**Priority:** High/Medium/Low

**Story Points:** TBD

**Notes:**
- Logging out should invalidate the current session.
- The patient should be redirected to an appropriate page after logging out.

---

**Title:**  
_As a Patient, I want to log in and book an hour-long appointment with a doctor, so that I can consult with the doctor._

**Acceptance Criteria:**
1. Only authenticated patients can book appointments.
2. The patient can select a doctor and an available one-hour appointment slot.
3. The system confirms the appointment when the selected time slot is available.
4. The system prevents booking a time slot that has already been reserved.

**Priority:** High/Medium/Low

**Story Points:** TBD

**Notes:**
- Appointment duration is fixed at one hour.
- Appointment availability is based on the selected doctor's schedule.

---

**Title:**  
_As a Patient, I want to view my upcoming appointments, so that I can prepare accordingly._

**Acceptance Criteria:**
1. Only authenticated patients can view their upcoming appointments.
2. The system displays all future appointments associated with the patient's account.
3. Each appointment displays the doctor's name, appointment date, appointment time, and duration.
4. If the patient has no upcoming appointments, the system displays an appropriate message.

**Priority:** High/Medium/Low

**Story Points:** TBD

**Notes:**
- Only future appointments are included in this view.
- Past appointments are outside the scope of this user story.

---

## Doctor User Stories

**Title:**  
_As a Doctor, I want to log into the portal, so that I can manage my appointments._

**Acceptance Criteria:**
1. The doctor can log in using a registered email address and password.
2. The system authenticates the doctor when valid credentials are provided.
3. The system displays an appropriate error message when invalid credentials are entered.
4. After successful authentication, the doctor is granted access to appointment management features.

**Priority:** High/Medium/Low

**Story Points:** TBD

**Notes:**
- Only authenticated doctors can access protected portal features.

---

**Title:**  
_As a Doctor, I want to log out of the portal, so that I can protect my data._

**Acceptance Criteria:**
1. The doctor can log out from an authenticated session.
2. The system terminates the active session after logout.
3. Accessing protected pages after logout requires the doctor to authenticate again.

**Priority:** High/Medium/Low

**Story Points:** TBD

**Notes:**
- Logging out should invalidate the current session.
- The doctor should be redirected to an appropriate page after logging out.

---

**Title:**  
_As a Doctor, I want to view my appointment calendar, so that I can stay organized._

**Acceptance Criteria:**
1. Only authenticated doctors can view their appointment calendar.
2. The system displays all upcoming appointments assigned to the logged-in doctor.
3. Each appointment includes the patient's name, appointment date, appointment time, and duration.
4. If there are no scheduled appointments, the system displays an appropriate message.

**Priority:** High/Medium/Low

**Story Points:** TBD

**Notes:**
- Only the logged-in doctor's appointments should be displayed.
- Past appointments are outside the scope of this user story.

---

**Title:**  
_As a Doctor, I want to mark my unavailability, so that patients are shown only my available appointment slots._

**Acceptance Criteria:**
1. Only authenticated doctors can mark periods of unavailability.
2. The doctor can specify the date and time range during which they are unavailable.
3. The system prevents patients from booking appointments during marked unavailable periods.
4. Existing appointments are not affected when new unavailable periods are created.

**Priority:** High/Medium/Low

**Story Points:** TBD

**Notes:**
- The system should prevent overlapping unavailable periods where applicable.
- Handling conflicts with existing appointments is outside the scope of this story unless otherwise specified.

---

**Title:**  
_As a Doctor, I want to update my profile with my specialization and contact information, so that patients have up-to-date information._

**Acceptance Criteria:**
1. Only authenticated doctors can update their profile information.
2. The doctor can update their specialization.
3. The doctor can update their contact information.
4. The updated profile information is displayed to patients after the changes are saved.

**Priority:** High/Medium/Low

**Story Points:** TBD

**Notes:**
- Profile updates should only affect the logged-in doctor's profile.
- Contact information should follow the application's validation rules.

---

**Title:**  
_As a Doctor, I want to view the details of patients for upcoming appointments, so that I can be prepared._

**Acceptance Criteria:**
1. Only authenticated doctors can view patient details for their upcoming appointments.
2. The system displays patient information associated with each upcoming appointment.
3. Patient details are accessible only for appointments assigned to the logged-in doctor.
4. The system prevents doctors from viewing patient details for appointments assigned to other doctors.

**Priority:** High/Medium/Low

**Story Points:** TBD

**Notes:**
- Patient information displayed should be limited to the information required for the appointment.
- Access to patient information should comply with applicable privacy and security requirements.

