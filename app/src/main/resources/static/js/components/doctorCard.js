/*
 * doctorCard.js
 *
 * Creates and manages doctor cards dynamically.
 * Actions available depend on the current user's role:
 * - Admin: Delete doctor
 * - Patient: Request login before booking
 * - Logged Patient: Book appointment
 */


import { showBookingOverlay } from "../loggedPatient.js";
import { deleteDoctor } from "../services/doctorServices.js";
import { getPatientDetails } from "../services/patientServices.js";


/*
 * Function to create a doctor card element
 */
export function createDoctorCard(doctor) {

    // Create main doctor card container
    const card = document.createElement("div");
    card.className = "doctor-card";


    // Retrieve current user role
    const role = localStorage.getItem("userRole");


    /*
     * Doctor information container
     */
    const doctorInfo = document.createElement("div");
    doctorInfo.className = "doctor-info";


    // Doctor name
    const doctorName = document.createElement("h3");
    doctorName.textContent = doctor.name;


    // Doctor specialization
    const doctorSpecialty = document.createElement("p");
    doctorSpecialty.textContent =
        `Specialization: ${doctor.specialization}`;


    // Doctor email
    const doctorEmail = document.createElement("p");
    doctorEmail.textContent =
        `Email: ${doctor.email}`;


    // Available appointment times
    const availability = document.createElement("p");

    availability.textContent =
        doctor.availableTimes && doctor.availableTimes.length > 0
            ? `Available Times: ${doctor.availableTimes.join(", ")}`
            : "No available appointments";


    // Append doctor information
    doctorInfo.appendChild(doctorName);
    doctorInfo.appendChild(doctorSpecialty);
    doctorInfo.appendChild(doctorEmail);
    doctorInfo.appendChild(availability);



    /*
     * Action buttons container
     */
    const actions = document.createElement("div");
    actions.className = "doctor-actions";



    /*
     * ==========================
     * ADMIN ROLE ACTIONS
     * ==========================
     */
    if (role === "admin") {

        const deleteButton = document.createElement("button");

        deleteButton.textContent = "Delete Doctor";
        deleteButton.className = "adminBtn";


        deleteButton.addEventListener("click", async () => {

            const token = localStorage.getItem("token");


            try {

                const result = await deleteDoctor(
                    doctor.id,
                    token
                );


                alert(result.message || "Doctor deleted successfully");


                // Remove card after successful deletion
                card.remove();


            } catch (error) {

                alert(
                    "Unable to delete doctor. Please try again."
                );

                console.error(error);
            }

        });


        actions.appendChild(deleteButton);
    }



    /*
     * ==========================
     * PATIENT NOT LOGGED IN
     * ==========================
     */
    else if (role === "patient") {


        const bookButton = document.createElement("button");

        bookButton.textContent = "Book Now";
        bookButton.className = "book-btn";


        bookButton.addEventListener("click", () => {

            alert(
                "Please log in before booking an appointment."
            );

        });


        actions.appendChild(bookButton);

    }



    /*
     * ==========================
     * LOGGED-IN PATIENT
     * ==========================
     */
    else if (role === "loggedPatient") {


        const bookButton = document.createElement("button");

        bookButton.textContent = "Book Now";
        bookButton.className = "book-btn";


        bookButton.addEventListener(
            "click",
            async () => {


                const token =
                    localStorage.getItem("token");


                // Redirect if session expired
                if (!token) {

                    alert(
                        "Session expired. Please log in again."
                    );

                    window.location.href = "/";

                    return;
                }


                try {

                    // Retrieve patient information
                    const patient =
                        await getPatientDetails(token);


                    // Display booking overlay
                    showBookingOverlay(
                        doctor,
                        patient
                    );


                } catch (error) {

                    alert(
                        "Unable to load patient information."
                    );

                    console.error(error);

                }

            }
        );


        actions.appendChild(bookButton);

    }



    /*
     * Append card sections
     */
    card.appendChild(doctorInfo);
    card.appendChild(actions);


    /*
     * Return completed doctor card
     */
    return card;
}
/*
Import the overlay function for booking appointments from loggedPatient.js

  Import the deleteDoctor API function to remove doctors (admin role) from docotrServices.js

  Import function to fetch patient details (used during booking) from patientServices.js

  Function to create and return a DOM element for a single doctor card
    Create the main container for the doctor card
    Retrieve the current user role from localStorage
    Create a div to hold doctor information
    Create and set the doctor’s name
    Create and set the doctor's specialization
    Create and set the doctor's email
    Create and list available appointment times
    Append all info elements to the doctor info container
    Create a container for card action buttons
    === ADMIN ROLE ACTIONS ===
      Create a delete button
      Add click handler for delete button
     Get the admin token from localStorage
        Call API to delete the doctor
        Show result and remove card if successful
      Add delete button to actions container
   
    === PATIENT (NOT LOGGED-IN) ROLE ACTIONS ===
      Create a book now button
      Alert patient to log in before booking
      Add button to actions container
  
    === LOGGED-IN PATIENT ROLE ACTIONS === 
      Create a book now button
      Handle booking logic for logged-in patient   
        Redirect if token not available
        Fetch patient data with token
        Show booking overlay UI with doctor and patient info
      Add button to actions container
   
  Append doctor info and action buttons to the car
  Return the complete doctor card element
*/
