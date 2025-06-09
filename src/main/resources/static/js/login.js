document.addEventListener('DOMContentLoaded', function() {
    // Funcionalidad para mostrar/ocultar contraseña
    const togglePassword = document.querySelector('.toggle-password');
    const passwordInput = document.querySelector('input[type="password"]');

    if (togglePassword && passwordInput) {
        togglePassword.addEventListener('click', function() {
            const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordInput.setAttribute('type', type);
            
            // Cambiar el ícono
            const icon = this.querySelector('i');
            icon.classList.toggle('fa-eye');
            icon.classList.toggle('fa-eye-slash');
        });
    }

    // Animación suave para los mensajes de alerta
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        alert.style.opacity = '0';
        setTimeout(() => {
            alert.style.transition = 'opacity 0.5s ease-in-out';
            alert.style.opacity = '1';
        }, 100);
    });

    // Auto-ocultar alertas después de 5 segundos
    setTimeout(() => {
        alerts.forEach(alert => {
            const closeButton = alert.querySelector('.btn-close');
            if (closeButton) {
                closeButton.click();
            }
        });
    }, 5000);

    // Efecto de onda en el botón de login
    const loginButton = document.querySelector('.btn-primary');
    if (loginButton) {
        loginButton.addEventListener('mousedown', function(e) {
            const rect = this.getBoundingClientRect();
            const x = e.clientX - rect.left;
            const y = e.clientY - rect.top;

            const ripple = document.createElement('span');
            ripple.style.cssText = `
                position: absolute;
                background: rgba(255, 255, 255, 0.7);
                transform: translate(-50%, -50%);
                pointer-events: none;
                border-radius: 50%;
                width: 0;
                height: 0;
                left: ${x}px;
                top: ${y}px;
            `;

            this.appendChild(ripple);

            const diameter = Math.max(this.clientWidth, this.clientHeight);
            ripple.style.width = ripple.style.height = `${diameter}px`;

            setTimeout(() => ripple.remove(), 600);
        });
    }

    // Validación del formulario en tiempo real
    const form = document.querySelector('.login-form');
    const emailInput = form.querySelector('input[type="email"]');

    if (emailInput) {
        emailInput.addEventListener('input', function() {
            if (this.validity.typeMismatch) {
                this.setCustomValidity('Por favor, ingresa un correo electrónico válido');
            } else {
                this.setCustomValidity('');
            }
        });
    }

    // Prevenir múltiples envíos del formulario
    if (form) {
        form.addEventListener('submit', function(e) {
            const submitButton = this.querySelector('button[type="submit"]');
            if (submitButton) {
                submitButton.disabled = true;
                submitButton.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Iniciando sesión...';
            }
        });
    }
}); 