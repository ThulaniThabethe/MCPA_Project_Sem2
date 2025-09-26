document.addEventListener('DOMContentLoaded', function() {
    
    // --- Configuration ---
    // IMPORTANT: Use the full URL to your XAMPP server's htdocs directory
    const API_BASE_URL = 'http://localhost/MCPA'; 
    // NOTE: If you are testing on the emulator/device, you MUST use the IP again:
    // const API_BASE_URL = 'http://192.168.0.100/MCPA'; 

    // --- Utility Functions ---
    function setupPasswordToggle(passwordFieldId, toggleIconId) {
        const passwordField = document.getElementById(passwordFieldId);
        const passwordToggle = document.getElementById(toggleIconId);

        if (passwordToggle && passwordField) {
            passwordToggle.addEventListener('click', function() {
                const type = passwordField.getAttribute('type') === 'password' ? 'text' : 'password';
                passwordField.setAttribute('type', type);
                this.classList.toggle('fa-eye');
                this.classList.toggle('fa-eye-slash');
            });
        }
    }

    function isPasswordValid(password) {
        // A minimum of 8 characters
        if (password.length < 8) return false;
        // At least 1 uppercase character
        if (!/[A-Z]/.test(password)) return false;
        // At least 1 lowercase character
        if (!/[a-z]/.test(password)) return false;
        // At least 1 number
        if (!/[0-9]/.test(password)) return false;
        // At least 1 special character
        if (!/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(password)) return false;
        return true;
    }

    // Function to hide the success modal (needs to be globally accessible)
    window.hideSuccessModal = function() {
        const modal = document.getElementById('registrationSuccessModal');
        if (modal) {
            modal.style.display = 'none';
        }
    }
    
    // NEW: Function to check login status (Local Storage Access)
    window.getLoggedInUser = function() {
        const user = localStorage.getItem('currentUser');
        if (user) {
            return JSON.parse(user);
        }
        return null;
    }

    // NEW: Function to log the user out
    window.logoutUser = function() {
        localStorage.removeItem('currentUser');
        // Optionally clear other user-specific data here
        window.location.href = 'login.html'; // Redirect to login page or index
    }


    // --- Logic for login.html (Sign In) ---
    if (document.getElementById('signInForm')) {
        setupPasswordToggle('password', 'passwordToggle');

        // FIX for the "Register" button not working on the Sign In page
        document.getElementById('registerBtn')?.addEventListener('click', () => window.location.href = 'register.html');
        
        document.querySelector('.forgot-password-link').href = 'forgot_password.html';

        const signInForm = document.getElementById('signInForm');
        const emailInput = document.getElementById('email');
        const passwordInput = document.getElementById('password'); 

        signInForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            
            const email = emailInput.value;
            const password = passwordInput.value;

            try {
                const response = await fetch(`${API_BASE_URL}/login_submit.php`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        email: email,
                        password: password
                    })
                });
                
                // CRITICAL FIX FOR SIGN IN FLOW: 
                // If the server returns a successful HTTP status, assume login is successful and redirect.
                if (response.ok) {
                    console.log('Login successful based on HTTP status. Attempting JSON parse for details.');
                    
                    let result = {};
                    try {
                        result = await response.json();
                    } catch(jsonError) {
                        console.warn('JSON parsing failed on successful login status. Redirecting anyway.', jsonError);
                        // Fallback user ID if JSON fails, but status was OK
                        result.user_id = 'Unknown'; 
                    }

                    // --- NEW CRITICAL STEP: SAVE USER DATA TO LOCAL STORAGE ---
                    const userEmail = emailInput.value;
                    const userId = result.user_id;

                    // Store essential data as JSON for easy retrieval
                    localStorage.setItem('currentUser', JSON.stringify({
                        email: userEmail,
                        user_id: userId,
                        isLoggedIn: true
                    }));
                    // -----------------------------------------------------------
                    
                    // Only show user_id if we successfully parsed the data
                    alert(`Welcome back! User ID: ${userId || 'Unknown'}`);
                    
                    // *** UPDATED: Redirect directly to the profile page ***
                    window.location.href = 'profile.html'; 
                    return; // Stop execution to ensure redirect happens
                }
                
                // --- Handle Failure Status Codes (e.g., 401 Unauthorized) ---
                let result = {};
                try {
                    // Try to get the error message from the server
                    result = await response.json();
                } catch(jsonError) {
                    console.error('JSON parsing failed on failure status:', jsonError);
                    alert(`Sign In Failed: Server returned status ${response.status} but invalid error details. Check PHP output.`);
                    return;
                }
                
                // Display the PHP-driven failure message
                alert(`Sign In Failed: ${result.message}`);


            } catch (error) {
                console.error('Network or PHP connection error:', error);
                alert('A server error occurred. Please ensure XAMPP is running and the API_BASE_URL is correct (localhost/IP).');
            }
        });
    }

    // --- Logic for register.html (AJAX Implementation) ---
    if (document.getElementById('registerForm')) {
        setupPasswordToggle('registerPassword', 'registerPasswordToggle');
        
        const registerForm = document.getElementById('registerForm');
        const emailInput = document.getElementById('registerEmail');
        const passwordInput = document.getElementById('registerPassword');
        const nextBtn = document.getElementById('nextBtn'); // The NEXT button on the register form

        function validateRegistrationFields() {
            // Basic email check for '@' and '.'
            const isEmailValid = emailInput.value.includes('@') && emailInput.value.includes('.');
            const isPasswordStrong = isPasswordValid(passwordInput.value); // Uses existing complexity function
            
            if (isEmailValid && isPasswordStrong) {
                nextBtn.classList.remove('grey-button');
                nextBtn.classList.add('black-button');
                nextBtn.disabled = false;
            } else {
                nextBtn.classList.remove('black-button');
                nextBtn.classList.add('grey-button');
                nextBtn.disabled = true;
            }
        }
        
        // Add event listeners for conditional button styling
        emailInput.addEventListener('input', validateRegistrationFields);
        passwordInput.addEventListener('input', validateRegistrationFields);
        
        // Initialize button state on load
        validateRegistrationFields(); 

        // --- SUBMIT HANDLER for Registration ---
        registerForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            
            const email = emailInput.value;
            const password = passwordInput.value;

            if (nextBtn.disabled || !isPasswordValid(password)) {
                alert("Registration validation failed. Please ensure the email is valid and the password meets all requirements.");
                return;
            }

            try {
                const response = await fetch(`${API_BASE_URL}/register_submit.php`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        email: email,
                        password: password
                    })
                });
                
                // CRITICAL FIX: If the server responds with a successful status (201/200),
                // we assume success and redirect, ignoring the potentially broken JSON.
                if (response.status === 201 || response.status === 200) {
                    console.log(`Server responded with success status ${response.status}. Proceeding with redirect.`);
                    alert(`Registration successful! Please complete your personal details.`);
                    window.location.href = 'personal_details.html?email=' + encodeURIComponent(email);
                    return; // Stop here to prevent further parsing attempts
                }

                // If the status is NOT 200/201 (e.g., 409 Conflict), we attempt to read the JSON for the specific error message.
                let result = {};
                try {
                    // Read and parse the JSON response body
                    result = await response.json(); 
                } catch (jsonError) {
                    // If parsing fails on an error status (e.g., 409), display a generic error.
                    console.error('JSON parsing failed on non-success status:', jsonError);
                    alert(`Server Error (Status: ${response.status}): Could not read the error details. Check PHP logs.`);
                    return; 
                }

                // Check the success flag within the parsed JSON for PHP-driven failure (e.g., email exists)
                if (result.success === false) {
                    // Correct response for a registration failure: show the error message.
                    alert(`Registration failed: ${result.message}`);
                } else {
                     // Catch-all for weird status codes or unexpected successful JSON structure
                     alert(`An unexpected error occurred. Status: ${response.status}.`);
                }

            } catch (error) {
                // This catches network errors or the HTTP error we threw above
                console.error('Network or major server error:', error);
                alert(`A server error occurred: ${error.message}. Please ensure XAMPP is running and API_BASE_URL is correct.`);
            }
        });
    }

    // --- Logic for personal_details.html (AJAX Implementation) ---
    if (document.getElementById('personalDetailsForm')) {
        const form = document.getElementById('personalDetailsForm');
        // The hidden input ID is 'userEmail' and the name is 'email'
        const userEmailInput = document.getElementById('userEmail'); 

        // 1. Get the email from the URL (passed from successful registration)
        const urlParams = new URLSearchParams(window.location.search);
        const emailFromUrl = urlParams.get('email');

        if (emailFromUrl) {
            userEmailInput.value = emailFromUrl; // Populate the hidden email field
        } else {
            // Handle case where user navigates directly without registering first
            // Fallback: Check if user is logged in (e.g., returning to finish form)
            const user = getLoggedInUser();
            if (user && user.email) {
                 userEmailInput.value = user.email;
            } else {
                alert("Error: Missing user registration data. Please sign in or register.");
                window.location.href = 'login.html';
                return; 
            }
        }
        
        // 2. Add form submit listener
        form.addEventListener('submit', async function(e) {
            e.preventDefault();
            
            const formData = new FormData(form);
            const data = {};
            
            // Convert FormData to a JSON object for the fetch request
            formData.forEach((value, key) => {
                // Checkbox values are boolean (true/false)
                if (form.elements[key].type === 'checkbox') {
                    data[key] = form.elements[key].checked; 
                } else {
                    data[key] = value;
                }
            });
            
            // --- DATA CLEANUP AND MAPPING FOR PHP ---
            // 1. Clean Contact Number
            data.contactNumber = data.contactNumber ? data.contactNumber.replace(/[^0-9]/g, '') : '';

            // 2. Map HTML names to expected PHP/DB columns if they differ
            // Assuming your PHP Model/Controller expects these keys:
            data.email = data.email; // Already correct
            data.title = data.title; // Already correct
            data.firstName = data.firstName; // Already correct
            data.lastName = data.lastName; // Already correct
            data.contactNumber = data.contactNumber; // Already correct
            data.date_of_birth = data.dateOfBirth; // **MAPPING FIX: DB column is usually 'date_of_birth'**
            
            // Map promotion checkboxes to simple boolean (0 or 1 is common in DB)
            data.promo_woolworths = data.promoWoolworths ? 1 : 0;
            data.promo_financial = data.promoFinancial ? 1 : 0;
            
            // Remove the original HTML checkbox names if they are not needed by the PHP endpoint
            delete data.dateOfBirth;
            delete data.promoWoolworths;
            delete data.promoFinancial;

            // Optional: Log the final payload being sent
            // console.log("Sending Personal Details Payload:", data);

            try {
                const response = await fetch(`${API_BASE_URL}/personal_details_submit.php`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(data)
                });

                // Check for successful network response status (200-299)
                if (response.ok) {
                    // Attempt to parse JSON. If it fails, assume success because status was OK.
                    let result = {};
                    try {
                        result = await response.json();
                    } catch (jsonError) {
                        console.warn('Personal details JSON parse failed, assuming success based on 200 OK status.', jsonError);
                        result.success = true; // Force success flag
                        result.message = "Details saved successfully! (JSON Parse Error Ignored)";
                    }
                    
                    if (result.success) {
                        // Personal details saved success: user is now fully registered and signed in
                        // Show success alert (like your image_bed55d.png)
                        alert(`Details saved! Welcome to Woolworths, ${data.firstName || 'User'}!`);
                        
                        // NOTE: If this was an update, you might want to refresh the local storage here.
                        
                        // *** MODIFIED REDIRECT: Go to index.html with a flag to show the final success modal ***
                        window.location.href = 'index.html?showSuccess=true'; 
                        
                    } else {
                         // Update failed 
                        alert(`Error saving details: ${result.message}`);
                    }
                } else {
                     // Status was NOT OK (e.g., 500, 400)
                     let errorData = await response.json().catch(() => ({ message: 'Unknown error (Server failed to return valid JSON error message).' }));
                     alert(`Error saving details (Status: ${response.status}): ${errorData.message}`);
                }

            } catch (error) {
                console.error('Network or PHP connection error:', error);
                alert('A server error occurred while saving details. Please check the network connection and XAMPP status.');
            }
        });

        // Handle Cancel button redirection
        document.getElementById('cancelBtn')?.addEventListener('click', () => {
            window.location.href = 'index.html';
        });
    }

    // --- NEW: Logic for profile.html (Fetching and Displaying Details) ---
    if (document.getElementById('profileDetailsPage')) { // Assuming your profile page HTML uses this ID
        const user = window.getLoggedInUser();
        // NOTE: The profile page might use its own simple form structure, not the 'personalDetailsForm'
        // We'll look for fields by their specific IDs.
        
        if (!user || !user.isLoggedIn) {
            alert("Please sign in to view your profile.");
            window.location.href = 'login.html';
            return;
        }
        
        // Function to fetch and populate the fields on the profile page
        async function fetchAndPopulateProfile(userEmail) {
            try {
                // Assuming you create a new PHP file named get_profile.php 
                const response = await fetch(`${API_BASE_URL}/get_profile.php`, {
                    method: 'POST', // Use POST to send the email securely
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ email: userEmail })
                });

                if (response.ok) {
                    const result = await response.json();
                    if (result.success && result.data) {
                        const profile = result.data;
                        
                        console.log("Profile data fetched:", profile);
                        
                        // Populate display fields (adjust IDs based on your profile.html structure)
                        // Example: Assuming you have elements to display this data
                        document.getElementById('profileEmailDisplay').textContent = profile.email || 'N/A';
                        document.getElementById('profileFullNameDisplay').textContent = `${profile.firstName || ''} ${profile.lastName || ''}`;
                        document.getElementById('profileContactDisplay').textContent = profile.contactNumber || 'N/A';
                        document.getElementById('profileDOBDisplay').textContent = profile.dateOfBirth || 'N/A'; 
                        
                        // You could populate an EDIT form here too if it shares the page
                        
                    } else {
                        alert(`Error fetching profile: ${result.message}`);
                    }
                } else {
                    const errorData = await response.json().catch(() => ({ message: 'Server error fetching profile.' }));
                    alert(`Profile Fetch Failed (Status: ${response.status}): ${errorData.message}`);
                }
            } catch (error) {
                console.error('Network error during profile fetch:', error);
                alert('A network error occurred while loading your profile.');
            }
        }

        // Start the fetch process using the stored email
        fetchAndPopulateProfile(user.email);
    }
    // --- END NEW PROFILE LOGIC ---


    // --- Global Navigation Logic (Unchanged) ---
    const allCloseBtns = document.querySelectorAll('.close-btn');
    allCloseBtns.forEach(btn => {
        // Handle 'X' close button on register/login forms
        if (btn.closest('.login-container') && document.title.includes('Register')) {
             btn.addEventListener('click', function(e) { e.preventDefault(); window.location.href = 'index.html'; });
        } else if (btn.getAttribute('href') === 'login.html') {
            btn.addEventListener('click', function(e) { e.preventDefault(); window.location.href = 'login.html'; });
        }
    });

    // --- NEW LOGIC: Handle Success Modal on index.html ---
    const successParams = new URLSearchParams(window.location.search);
    if (successParams.get('showSuccess') === 'true') {
        const modal = document.getElementById('registrationSuccessModal');
        
        if (modal) {
            modal.style.display = 'flex'; // Show the modal
            
            // Add listeners for the buttons inside the modal
            document.getElementById('continueShoppingBtn')?.addEventListener('click', function() {
                hideSuccessModal();
                // Clean the URL history state
                window.history.replaceState({}, document.title, window.location.pathname);
            });
            
            // *** UPDATED: Redirect to profile.html after registration completion ***
            document.getElementById('goToMyAccountsBtn')?.addEventListener('click', function() {
                hideSuccessModal();
                // Redirect user to the new profile page
                window.location.href = 'profile.html'; 
            });
            
             // Clean the URL history state immediately after checking, to prevent refresh loops
             window.history.replaceState({}, document.title, window.location.pathname);
        }
    }
});