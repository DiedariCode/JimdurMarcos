document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('registerForm');
    const passwordInput = document.querySelector('input[name="contrasena"]');
    const confirmPasswordInput = document.querySelector('input[name="confirmContrasena"]');
    const passwordStrength = document.getElementById('passwordStrength');
    const togglePasswordButtons = document.querySelectorAll('.toggle-password');
    const step1 = document.getElementById('step-1');
    const step2 = document.getElementById('step-2');
    const step3 = document.getElementById('step-3');
    const nextButton = document.getElementById('next-to-verification');
    const verifyEmailButton = document.getElementById('verify-email');
    const resendCodeButton = document.getElementById('resend-email-code');
    const emailDisplay = document.getElementById('email-display');
    const emailInput = document.querySelector('input[name="email"]');
    const emailCode = document.getElementById('emailCode');
    const countdownElement = document.getElementById('email-countdown');
    const nombresInput = document.querySelector('input[name="nombres"]');
    const apellidosInput = document.querySelector('input[name="apellidos"]');

    // Función para evaluar la fortaleza de la contraseña
    function evaluatePasswordStrength(password) {
        let strength = 0;
        const feedback = {
            class: '',
            text: ''
        };

        // Longitud mínima
        if (password.length >= 8) strength += 1;
        
        // Contiene números
        if (/\d/.test(password)) strength += 1;
        
        // Contiene letras minúsculas
        if (/[a-z]/.test(password)) strength += 1;
        
        // Contiene letras mayúsculas
        if (/[A-Z]/.test(password)) strength += 1;
        
        // Contiene caracteres especiales
        if (/[!@#$%^&*(),.?":{}|<>]/.test(password)) strength += 1;

        // Asignar clase y texto según la fortaleza
        switch (strength) {
            case 0:
                feedback.class = 'very-weak';
                feedback.text = 'Muy débil';
                break;
            case 1:
                feedback.class = 'weak';
                feedback.text = 'Débil';
                break;
            case 2:
                feedback.class = 'medium';
                feedback.text = 'Media';
                break;
            case 3:
                feedback.class = 'strong';
                feedback.text = 'Fuerte';
                break;
            case 4:
            case 5:
                feedback.class = 'very-strong';
                feedback.text = 'Muy fuerte';
                break;
        }

        return feedback;
    }

    // Mostrar/ocultar contraseña
    togglePasswordButtons.forEach(button => {
        button.addEventListener('click', function() {
            const input = this.previousElementSibling;
            const type = input.getAttribute('type') === 'password' ? 'text' : 'password';
            input.setAttribute('type', type);
            
            const icon = this.querySelector('i');
            icon.classList.toggle('fa-eye');
            icon.classList.toggle('fa-eye-slash');
        });
    });

    // Validar fortaleza de contraseña en tiempo real
    if (passwordInput) {
        passwordInput.addEventListener('input', function() {
            const strength = evaluatePasswordStrength(this.value);
            
            // Actualizar indicador visual
            passwordStrength.className = 'password-strength ' + strength.class;
            
            // Actualizar texto de fortaleza
            let strengthText = document.querySelector('.password-strength-text');
            if (!strengthText) {
                strengthText = document.createElement('div');
                strengthText.className = 'password-strength-text';
                passwordStrength.parentNode.insertBefore(strengthText, passwordStrength.nextSibling);
            }
            strengthText.textContent = 'Fortaleza: ' + strength.text;
        });
    }

    // Validar coincidencia de contraseñas
    if (confirmPasswordInput) {
        confirmPasswordInput.addEventListener('input', function() {
            if (this.value !== passwordInput.value) {
                this.setCustomValidity('Las contraseñas no coinciden');
            } else {
                this.setCustomValidity('');
            }
        });
    }

    // Validar formulario antes de enviar
    if (form) {
        form.addEventListener('submit', function(e) {
            e.preventDefault();

            // Validar todos los campos requeridos
            const requiredFields = form.querySelectorAll('[required]');
            let isValid = true;

            requiredFields.forEach(field => {
                if (!field.value.trim()) {
                    isValid = false;
                    field.classList.add('is-invalid');
                } else {
                    field.classList.remove('is-invalid');
                }
            });

            // Validar formato de correo
            const emailInput = form.querySelector('input[type="email"]');
            if (emailInput && !emailInput.value.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) {
                isValid = false;
                emailInput.classList.add('is-invalid');
            }

            // Validar fortaleza de contraseña
            const passwordStrengthValue = evaluatePasswordStrength(passwordInput.value);
            if (['very-weak', 'weak'].includes(passwordStrengthValue.class)) {
                isValid = false;
                passwordInput.classList.add('is-invalid');
                alert('La contraseña es demasiado débil. Por favor, usa una contraseña más segura.');
            }

            // Validar coincidencia de contraseñas
            if (passwordInput.value !== confirmPasswordInput.value) {
                isValid = false;
                confirmPasswordInput.classList.add('is-invalid');
            }

            if (isValid) {
                // Deshabilitar el botón de envío y mostrar spinner
                const submitButton = form.querySelector('button[type="submit"]');
                submitButton.disabled = true;
                submitButton.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Creando cuenta...';
                
                // Enviar el formulario
                form.submit();
            }
        });
    }

    // Remover clase is-invalid al escribir
    form.querySelectorAll('.form-control').forEach(input => {
        input.addEventListener('input', function() {
            this.classList.remove('is-invalid');
        });
    });

    // Animación de entrada para los campos
    const formGroups = document.querySelectorAll('.form-group');
    formGroups.forEach((group, index) => {
        group.style.animationDelay = `${index * 0.1}s`;
    });

    // Mostrar el primer paso al cargar
    step1.style.display = 'block';
    step1.classList.add('active');

    // Manejar el botón "Continuar"
    nextButton.addEventListener('click', async function() {
        if (validateStep1()) {
            const email = emailInput.value;
            const nombres = nombresInput.value;
            const apellidos = apellidosInput.value;

            if (!email || !nombres || !apellidos) {
                alert('Por favor, completa todos los campos obligatorios.');
                return;
            }

            try {
                const response = await fetch(`/api/verification/sendCode?email=${encodeURIComponent(email)}`, {
                    method: 'POST'
                });

                const data = await response.json();

                if (response.ok) {
                    emailDisplay.textContent = email;
                    showStep(step1, step2);
                    startCountdown();
                } else {
                    alert(data.message || 'Error al enviar el código de verificación.');
                }
            } catch (error) {
                console.error('Error al enviar código:', error);
                alert('Ocurrió un error de red. Intenta nuevamente.');
            }
        }
    });

    // Validación del paso 1
    function validateStep1() {
        let isValid = true;
        const requiredFields = step1.querySelectorAll('[required]');

        requiredFields.forEach(field => {
            if (!field.value.trim()) {
                isValid = false;
                field.classList.add('is-invalid');
            } else {
                field.classList.remove('is-invalid');
            }
        });

        // Validar formato de correo
        if (!emailInput.value.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) {
            isValid = false;
            emailInput.classList.add('is-invalid');
        }

        // Validar contraseñas
        if (passwordInput.value !== confirmPasswordInput.value) {
            isValid = false;
            confirmPasswordInput.classList.add('is-invalid');
            alert('Las contraseñas no coinciden');
        }

        const passwordStrengthValue = evaluatePasswordStrength(passwordInput.value);
        if (['very-weak', 'weak'].includes(passwordStrengthValue.class)) {
            isValid = false;
            passwordInput.classList.add('is-invalid');
            alert('La contraseña es demasiado débil. Por favor, usa una contraseña más segura.');
        }

        return isValid;
    }

    // Manejar la verificación del email
    verifyEmailButton.addEventListener('click', async function() {
        const code = emailCode.value.trim();
        const email = emailDisplay.textContent;

        if (!code) {
            alert("Por favor, ingresa el código de verificación.");
            return;
        }

        try {
            const response = await fetch('/api/verification/verifyCode', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email, code })
            });

            const data = await response.json();

            if (data.verified) {
                showStep(step2, step3);
            } else {
                alert("❌ Código incorrecto o expirado. Intenta nuevamente.");
            }
        } catch (err) {
            console.error("Error al verificar código:", err);
            alert("Error de red al verificar el código.");
        }
    });

    // Manejar el reenvío del código
    resendCodeButton.addEventListener('click', async function() {
        if (!resendCodeButton.disabled) {
            const email = emailDisplay.textContent;
            try {
                const response = await fetch(`/api/verification/sendCode?email=${encodeURIComponent(email)}`, {
                    method: 'POST'
                });

                if (response.ok) {
                    startCountdown();
                } else {
                    alert('Error al reenviar el código de verificación.');
                }
            } catch (error) {
                console.error('Error al reenviar código:', error);
                alert('Ocurrió un error de red. Intenta nuevamente.');
            }
        }
    });

    // Contador para el reenvío del código
    function startCountdown() {
        let seconds = 60;
        resendCodeButton.disabled = true;
        countdownElement.style.display = 'block';
        
        const interval = setInterval(() => {
            seconds--;
            countdownElement.textContent = `Reenviar código (${seconds}s)`;
            
            if (seconds <= 0) {
                clearInterval(interval);
                resendCodeButton.disabled = false;
                countdownElement.style.display = 'none';
            }
        }, 1000);
    }

    // Formatear el input del código de verificación
    emailCode.addEventListener('input', function(e) {
        this.value = this.value.replace(/[^0-9]/g, '').substring(0, 6);
    });

    // Manejar el cambio entre pasos
    function showStep(hideStep, showStep) {
        hideStep.classList.remove('active');
        setTimeout(() => {
            hideStep.style.display = 'none';
            showStep.style.display = 'block';
            setTimeout(() => showStep.classList.add('active'), 50);
        }, 300);
    }
}); 