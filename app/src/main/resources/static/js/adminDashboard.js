/*
 * adminDashboard.js
 *
 * Handles the admin dashboard functionality:
 * - Loads doctor cards
 * - Filters doctors
 * - Adds new doctors
 */

import { openModal, closeModal } from "./components/modals.js";
import {
    getDoctors,
    filterDoctors,
    saveDoctor
} from "./services/doctorServices.js";
import { createDoctorCard } from "./components/doctorCard.js";


/*
 * Open Add Doctor modal
 */
const addDoctorButton = document.getElementById("addDocBtn");

if (addDoctorButton) {

    addDoctorButton.addEventListener("click", () => {
        openModal("addDoctor");
    });

}


/*
 * Load doctors once the page is ready
 */
document.addEventListener("DOMContentLoaded", () => {

    loadDoctorCards();

});


/*
 * Function: loadDoctorCards
 *
 * Fetches and renders all doctors.
 */
export async function loadDoctorCards() {

    try {

        const doctors = await getDoctors();

        renderDoctorCards(doctors);

    } catch (error) {

        console.error("Error loading doctors:", error);

    }

}


/*
 * Filter event listeners
 */
const searchBar = document.getElementById("searchBar");
const timeFilter = document.getElementById("timeFilter");
const specialtyFilter = document.getElementById("specialtyFilter");

if (searchBar) {
    searchBar.addEventListener("input", filterDoctorsOnChange);
}

if (timeFilter) {
    timeFilter.addEventListener("change", filterDoctorsOnChange);
}

if (specialtyFilter) {
    specialtyFilter.addEventListener("change", filterDoctorsOnChange);
}


/*
 * Function: filterDoctorsOnChange
 *
 * Filters doctors using the current search criteria.
 */
async function filterDoctorsOnChange() {

    try {

        const name =
            searchBar && searchBar.value.trim() !== ""
                ? searchBar.value.trim()
                : null;

        const time =
            timeFilter && timeFilter.value !== ""
                ? timeFilter.value
                : null;

        const specialty =
            specialtyFilter && specialtyFilter.value !== ""
                ? specialtyFilter.value
                : null;


        const result = await filterDoctors(
            name,
            time,
            specialty
        );

        if (result.doctors.length > 0) {

            renderDoctorCards(result.doctors);

        } else {

            const content =
                document.getElementById("content");

            content.innerHTML =
                "<p>No doctors found with the given filters.</p>";

        }

    } catch (error) {

        console.error(error);

        alert("Unable to filter doctors.");

    }

}


/*
 * Function: renderDoctorCards
 *
 * Renders a list of doctor cards.
 */
function renderDoctorCards(doctors) {

    const content = document.getElementById("content");

    content.innerHTML = "";

    doctors.forEach(doctor => {

        const card = createDoctorCard(doctor);

        content.appendChild(card);

    });

}


/*
 * Function: adminAddDoctor
 *
 * Saves a new doctor.
 */
window.adminAddDoctor = async function () {

    const doctor = {

        name: document.getElementById("doctorName").value,
        email: document.getElementById("doctorEmail").value,
        phone: document.getElementById("doctorPhone").value,
        password: document.getElementById("doctorPassword").value,
        specialization: document.getElementById("doctorSpecialty").value,
        availableTimes:
            document.getElementById("doctorAvailableTimes")
                .value
                .split(",")
                .map(time => time.trim())

    };


    const token = localStorage.getItem("token");

    if (!token) {

        alert("Authentication required.");

        return;

    }


    const result = await saveDoctor(
        doctor,
        token
    );


    if (result.success) {

        alert(result.message);

        closeModal();

        window.location.reload();

    } else {

        alert(result.message);

    }

};
/*
  This script handles the admin dashboard functionality for managing doctors:
  - Loads all doctor cards
  - Filters doctors by name, time, or specialty
  - Adds a new doctor via modal form


  Attach a click listener to the "Add Doctor" button
  When clicked, it opens a modal form using openModal('addDoctor')


  When the DOM is fully loaded:
    - Call loadDoctorCards() to fetch and display all doctors


  Function: loadDoctorCards
  Purpose: Fetch all doctors and display them as cards

    Call getDoctors() from the service layer
    Clear the current content area
    For each doctor returned:
    - Create a doctor card using createDoctorCard()
    - Append it to the content div

    Handle any fetch errors by logging them


  Attach 'input' and 'change' event listeners to the search bar and filter dropdowns
  On any input change, call filterDoctorsOnChange()


  Function: filterDoctorsOnChange
  Purpose: Filter doctors based on name, available time, and specialty

    Read values from the search bar and filters
    Normalize empty values to null
    Call filterDoctors(name, time, specialty) from the service

    If doctors are found:
    - Render them using createDoctorCard()
    If no doctors match the filter:
    - Show a message: "No doctors found with the given filters."

    Catch and display any errors with an alert


  Function: renderDoctorCards
  Purpose: A helper function to render a list of doctors passed to it

    Clear the content area
    Loop through the doctors and append each card to the content area


  Function: adminAddDoctor
  Purpose: Collect form data and add a new doctor to the system

    Collect input values from the modal form
    - Includes name, email, phone, password, specialty, and available times

    Retrieve the authentication token from localStorage
    - If no token is found, show an alert and stop execution

    Build a doctor object with the form values

    Call saveDoctor(doctor, token) from the service

    If save is successful:
    - Show a success message
    - Close the modal and reload the page

    If saving fails, show an error message
*/
