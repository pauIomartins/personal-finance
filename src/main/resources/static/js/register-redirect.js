// Script for form validation
document.addEventListener('DOMContentLoaded', function() {
    // Form validation
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');
    const passwordConfirmationInput = document.getElementById('passwordConfirmation');
    const form = document.querySelector('form');
    
    // If we are on the registration page, set up validation
    if (emailInput && passwordInput && passwordConfirmationInput) {
        // Get the email pattern from the server
        const emailPattern = document.getElementById('emailPatternHidden') ? 
                            document.getElementById('emailPatternHidden').value : '';
        
        // Get the password pattern from the server
        const passwordPattern = document.getElementById('passwordPatternHidden') ? 
                               document.getElementById('passwordPatternHidden').value : '';
        
        // Create regex objects for validation
        const emailRegex = emailPattern ? new RegExp(emailPattern) : null;
        const passwordRegex = passwordPattern ? new RegExp(passwordPattern) : null;
        
        // Add validation for email
        if (emailInput && emailRegex) {
            emailInput.addEventListener('blur', validateEmail);
            emailInput.addEventListener('input', function() {
                // Remove validation styles when the user starts typing again
                this.classList.remove('is-invalid');
                this.classList.remove('is-valid');
                const feedback = document.getElementById('email-feedback');
                if (feedback) {
                    feedback.style.display = 'none';
                }
            });
        }
        
        // Add validation for password
        if (passwordInput && passwordRegex) {
            passwordInput.addEventListener('blur', validatePassword);
            passwordInput.addEventListener('input', function() {
                // Remove validation styles when the user starts typing again
                this.classList.remove('is-invalid');
                this.classList.remove('is-valid');
                const feedback = document.getElementById('password-feedback');
                if (feedback) {
                    feedback.style.display = 'none';
                }
                
                // If confirmation is already filled, validate match again
                if (passwordConfirmationInput.value) {
                    validatePasswordMatch();
                }
            });
        }
        
        // Add validation for password confirmation
        if (passwordInput && passwordConfirmationInput) {
            passwordConfirmationInput.addEventListener('blur', validatePasswordMatch);
            passwordConfirmationInput.addEventListener('input', function() {
                // Remove validation styles when the user starts typing again
                this.classList.remove('is-invalid');
                this.classList.remove('is-valid');
                const feedback = document.getElementById('password-confirmation-feedback');
                if (feedback) {
                    feedback.style.display = 'none';
                }
            });
        }
        
        // Validate the form before submitting
        if (form) {
            form.addEventListener('submit', function(event) {
                let isValid = true;
                
                // Validate email
                if (emailInput && emailRegex) {
                    if (!validateEmail()) {
                        isValid = false;
                        // Add shake animation
                        emailInput.classList.add('shake');
                        // Remove animation after it completes
                        setTimeout(() => {
                            emailInput.classList.remove('shake');
                        }, 650);
                    }
                }
                
                // Validate password
                if (passwordInput && passwordRegex) {
                    if (!validatePassword()) {
                        isValid = false;
                        // Add shake animation
                        passwordInput.classList.add('shake');
                        // Remove animation after it completes
                        setTimeout(() => {
                            passwordInput.classList.remove('shake');
                        }, 650);
                    }
                }
                
                // Validate password confirmation
                if (passwordInput && passwordConfirmationInput) {
                    if (!validatePasswordMatch()) {
                        isValid = false;
                        // Add shake animation
                        passwordConfirmationInput.classList.add('shake');
                        // Remove animation after it completes
                        setTimeout(() => {
                            passwordConfirmationInput.classList.remove('shake');
                        }, 650);
                    }
                }
                
                // Prevent submission if there are errors
                if (!isValid) {
                    event.preventDefault();
                }
            });
        }
        
        // Function to validate email
        function validateEmail() {
            if (!emailInput || !emailRegex) return true;
            
            const email = emailInput.value.trim();
            let emailFeedback = document.getElementById('email-feedback');
            
            if (email === '') {
                return true; // Don't validate empty fields
            }
            
            if (emailRegex.test(email)) {
                // Valid email
                emailInput.classList.remove('is-invalid');
                emailInput.classList.add('is-valid');
                if (emailFeedback) {
                    emailFeedback.style.display = 'none';
                }
                return true;
            } else {
                // Invalid email
                emailInput.classList.remove('is-valid');
                emailInput.classList.add('is-invalid');
                
                if (!emailFeedback) {
                    // Create feedback element if it doesn't exist
                    emailFeedback = document.createElement('div');
                    emailFeedback.id = 'email-feedback';
                    emailFeedback.className = 'invalid-feedback';
                    emailInput.parentNode.appendChild(emailFeedback);
                } else {
                    emailFeedback.style.display = 'block';
                }
                
                // Detailed error message
                emailFeedback.innerHTML = `
                    <strong>Invalid email!</strong> Please provide a valid email address.
                    <ul class="mb-0 mt-1">
                        <li>Must contain an @ character</li>
                        <li>Must have a valid domain (example.com)</li>
                        <li>Cannot contain spaces</li>
                    </ul>
                `;
                
                return false;
            }
        }
        
        // Function to validate password
        function validatePassword() {
            if (!passwordInput || !passwordRegex) return true;
            
            const password = passwordInput.value;
            let passwordFeedback = document.getElementById('password-feedback');
            
            if (password === '') {
                return true; // Don't validate empty fields
            }
            
            if (passwordRegex.test(password)) {
                // Valid password
                passwordInput.classList.remove('is-invalid');
                passwordInput.classList.add('is-valid');
                if (passwordFeedback) {
                    passwordFeedback.style.display = 'none';
                }
                return true;
            } else {
                // Invalid password
                passwordInput.classList.remove('is-valid');
                passwordInput.classList.add('is-invalid');
                
                if (!passwordFeedback) {
                    // Create feedback element if it doesn't exist
                    passwordFeedback = document.getElementById('password-feedback');
                    if (!passwordFeedback) {
                        passwordFeedback = document.createElement('div');
                        passwordFeedback.id = 'password-feedback';
                        passwordFeedback.className = 'invalid-feedback';
                        passwordInput.parentNode.parentNode.appendChild(passwordFeedback);
                    }
                }
                
                passwordFeedback.style.display = 'block';
                
                // Detailed error message
                passwordFeedback.innerHTML = `
                    <strong>Invalid password!</strong> Your password must:
                    <ul class="mb-0 mt-1">
                        <li>Be at least 8 characters long</li>
                        <li>Include at least one digit (0-9)</li>
                        <li>Include at least one lowercase letter (a-z)</li>
                        <li>Include at least one uppercase letter (A-Z)</li>
                        <li>Include at least one special character (@#$%^&+=!)</li>
                        <li>Not contain spaces</li>
                    </ul>
                `;
                
                return false;
            }
        }
        
        // Function to validate password confirmation
        function validatePasswordMatch() {
            if (!passwordInput || !passwordConfirmationInput) return true;
            
            const password = passwordInput.value;
            const confirmation = passwordConfirmationInput.value;
            let confirmationFeedback = document.getElementById('password-confirmation-feedback');
            
            if (confirmation === '') {
                return true; // Don't validate empty fields
            }
            
            if (password === confirmation) {
                // Passwords match
                passwordConfirmationInput.classList.remove('is-invalid');
                passwordConfirmationInput.classList.add('is-valid');
                if (confirmationFeedback) {
                    confirmationFeedback.style.display = 'none';
                }
                return true;
            } else {
                // Passwords don't match
                passwordConfirmationInput.classList.remove('is-valid');
                passwordConfirmationInput.classList.add('is-invalid');
                
                if (!confirmationFeedback) {
                    // Create feedback element if it doesn't exist
                    confirmationFeedback = document.createElement('div');
                    confirmationFeedback.id = 'password-confirmation-feedback';
                    confirmationFeedback.className = 'invalid-feedback';
                    passwordConfirmationInput.parentNode.appendChild(confirmationFeedback);
                } else {
                    confirmationFeedback.style.display = 'block';
                }
                
                // Error message
                confirmationFeedback.innerHTML = `
                    <strong>Passwords do not match!</strong> Please make sure the passwords you entered are identical.
                `;
                
                return false;
            }
        }
    }
});
