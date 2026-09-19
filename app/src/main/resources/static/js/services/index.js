/*
 * auth.js
 *
 * Handles Admin and Doctor authentication.
 */

import { openModal } from "../components/modals.js";
import { API_BASE_URL } from "../config/config.js";
import { patientLogin } from "./patientServices.js";


/*
 * API Endpoints
 */
const ADMIN_API = `${API_BASE_URL}/admin/login`;
const DOCTOR_API = `${API_BASE_URL}/doctor/login`;


/*
 * Wait until the DOM is fully loaded
 */
window.addEventListener("load", () => {

    const adminLoginButton = document.getElementById("adminBtn");
    const patientLoginButton = document.getElementById("patientBtn");
    const doctorLoginButton = document.getElementById("doctorBtn");


    if (adminLoginButton) {

        adminLoginButton.addEventListener("click", () => {
            openModal("adminLogin");
        });

    }


    if (patientLoginButton) {

        patientLoginButton.addEventListener("click", () => {
            openModal("patientLogin");
        });

    }

    if (doctorLoginButton) {

        doctorLoginButton.addEventListener("click", () => {
            openModal("doctorLogin");
        });

    }

});


/*
 * Handles Admin login
 */
window.adminLoginHandler = async function () {

    try {

        // Step 1: Read credentials
        const username = document.getElementById("adminUsername").value;
        const password = document.getElementById("adminPassword").value;


        // Step 2: Create request payload
        const admin = {
            username,
            password
        };


        // Step 3: Send login request
        const response = await fetch(ADMIN_API, {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(admin)

        });


        // Step 4: Successful login
        if (response.ok) {

            const data = await response.json();

            localStorage.setItem("token", data.token);

            selectRole("admin");

            return;
        }


        // Step 5: Invalid credentials
        alert("Invalid username or password.");

    }
    catch (error) {

        // Step 6: Network/server errors
        console.error(error);

        alert("An unexpected error occurred. Please try again.");

    }

};


/*
 * Handles Doctor login
 */
window.doctorLoginHandler = async function () {

    try {

        // Step 1: Read credentials
        const email = document.getElementById("doctorEmail").value;
        const password = document.getElementById("doctorPassword").value;


        // Step 2: Create request payload
        const doctor = {
            email,
            password
        };


        // Step 3: Send login request
        const response = await fetch(DOCTOR_API, {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(doctor)

        });


        // Step 4: Successful login
        if (response.ok) {

            const data = await response.json();

            localStorage.setItem("token", data.token);

            selectRole("doctor");

            return;
        }


        // Step 5: Invalid credentials
        alert("Invalid email or password.");

    }
    catch (error) {

        // Step 6: Handle errors
        console.error(error);

        alert("An unexpected error occurred. Please try again.");

    }

};

/*
 * Handles Patient login
 */
window.loginPatient = async function () {
    try {
        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        const data = {
            email,
            password
        }
        console.log("loginPatient :: ", data)
        const response = await patientLogin(data);
        console.log("Status Code:", response.status);
        console.log("Response OK:", response.ok);
        if (response.ok) {
            const result = await response.json();
            console.log(result);
            selectRole('loggedPatient');
            localStorage.setItem('token', result.token)
            window.location.href = '/pages/loggedPatientDashboard.html';
        } else {
            alert('❌ Invalid credentials!');
        }
    }
    catch (error) {
        alert("❌ Failed to Login : ", error);
        console.log("Error :: loginPatient :: ", error)
    }


};
/*
  Import the openModal function to handle showing login popups/modals
  Import the base API URL from the config file
  Define constants for the admin and doctor login API endpoints using the base URL

  Use the window.onload event to ensure DOM elements are available after page load
  Inside this function:
    - Select the "adminLogin" and "doctorLogin" buttons using getElementById
    - If the admin login button exists:
        - Add a click event listener that calls openModal('adminLogin') to show the admin login modal
    - If the doctor login button exists:
        - Add a click event listener that calls openModal('doctorLogin') to show the doctor login modal


  Define a function named adminLoginHandler on the global window object
  This function will be triggered when the admin submits their login credentials

  Step 1: Get the entered username and password from the input fields
  Step 2: Create an admin object with these credentials

  Step 3: Use fetch() to send a POST request to the ADMIN_API endpoint
    - Set method to POST
    - Add headers with 'Content-Type: application/json'
    - Convert the admin object to JSON and send in the body

  Step 4: If the response is successful:
    - Parse the JSON response to get the token
    - Store the token in localStorage
    - Call selectRole('admin') to proceed with admin-specific behavior

  Step 5: If login fails or credentials are invalid:
    - Show an alert with an error message

  Step 6: Wrap everything in a try-catch to handle network or server errors
    - Show a generic error message if something goes wrong


  Define a function named doctorLoginHandler on the global window object
  This function will be triggered when a doctor submits their login credentials

  Step 1: Get the entered email and password from the input fields
  Step 2: Create a doctor object with these credentials

  Step 3: Use fetch() to send a POST request to the DOCTOR_API endpoint
    - Include headers and request body similar to admin login

  Step 4: If login is successful:
    - Parse the JSON response to get the token
    - Store the token in localStorage
    - Call selectRole('doctor') to proceed with doctor-specific behavior

  Step 5: If login fails:
    - Show an alert for invalid credentials

  Step 6: Wrap in a try-catch block to handle errors gracefully
    - Log the error to the console
    - Show a generic error message
*/
