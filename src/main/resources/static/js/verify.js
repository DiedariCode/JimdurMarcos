document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('verificationForm');
    const inputs = document.querySelectorAll('.code-input input');
    const completeCodeInput = document.getElementById('completeCode');
    const resendButton = document.getElementById('resendButton');
    const countdownDisplay = document.getElementById('countdown');

    let timeLeft = 300; // 5 minutos en segundos
    let countdownTimer;

    // Función para actualizar el contador
    function updateCountdown() {
        const minutes = Math.floor(timeLeft / 60);
        const seconds = timeLeft % 60;
        countdownDisplay.textContent = `${minutes}:${seconds.toString().padStart(2, '0')}`;
        
        if (timeLeft <= 0) {
            clearInterval(countdownTimer);
            resendButton.classList.remove('disabled');
            countdownDisplay.textContent = '0:00';
        } else {
            timeLeft--;
        }
    }

    // Iniciar el contador
    updateCountdown();
    countdownTimer = setInterval(updateCountdown, 1000);

    // Manejar el reenvío del código
    resendButton.addEventListener('click', function(e) {
        e.preventDefault();
        if (!this.classList.contains('disabled')) {
            // Reiniciar el contador
            timeLeft = 300;
            updateCountdown();
            this.classList.add('disabled');
            
            // Enviar solicitud para reenviar el código
            fetch('/resend-code', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    email: document.querySelector('input[name="email"]').value
                })
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    showAlert('success', 'Código reenviado exitosamente');
                } else {
                    showAlert('danger', 'Error al reenviar el código');
                }
            })
            .catch(() => {
                showAlert('danger', 'Error al reenviar el código');
            });
        }
    });

    // Función para mostrar alertas
    function showAlert(type, message) {
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
        alertDiv.innerHTML = `
            <i class="fas fa-${type === 'success' ? 'check' : 'exclamation'}-circle me-2"></i>
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        `;
        form.insertBefore(alertDiv, form.firstChild);
    }

    // Manejar la entrada de código
    inputs.forEach((input, index) => {
        // Auto-focus en el primer input
        if (index === 0) input.focus();

        input.addEventListener('input', function(e) {
            // Permitir solo números
            this.value = this.value.replace(/[^0-9]/g, '');

            // Actualizar el valor del código completo
            const code = Array.from(inputs).map(input => input.value).join('');
            completeCodeInput.value = code;

            // Mover al siguiente input
            if (this.value && index < inputs.length - 1) {
                inputs[index + 1].focus();
            }
        });

        input.addEventListener('keydown', function(e) {
            // Manejar la tecla de retroceso
            if (e.key === 'Backspace' && !this.value && index > 0) {
                inputs[index - 1].focus();
            }
            
            // Manejar las flechas izquierda/derecha
            if (e.key === 'ArrowLeft' && index > 0) {
                inputs[index - 1].focus();
            }
            if (e.key === 'ArrowRight' && index < inputs.length - 1) {
                inputs[index + 1].focus();
            }
        });

        // Permitir pegar el código completo
        input.addEventListener('paste', function(e) {
            e.preventDefault();
            const pastedData = e.clipboardData.getData('text');
            const numbers = pastedData.match(/\d/g);
            
            if (numbers) {
                numbers.forEach((number, i) => {
                    if (i < inputs.length) {
                        inputs[i].value = number;
                        if (i < inputs.length - 1) {
                            inputs[i + 1].focus();
                        }
                    }
                });
                
                // Actualizar el código completo
                completeCodeInput.value = Array.from(inputs)
                    .map(input => input.value)
                    .join('');
            }
        });
    });

    // Validar el formulario antes de enviar
    form.addEventListener('submit', function(e) {
        e.preventDefault();
        
        // Verificar que todos los campos estén llenos
        const code = Array.from(inputs).map(input => input.value).join('');
        if (code.length !== 6) {
            showAlert('danger', 'Por favor, ingresa el código completo');
            return;
        }

        // Deshabilitar el botón y mostrar spinner
        const submitButton = this.querySelector('button[type="submit"]');
        submitButton.disabled = true;
        submitButton.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Verificando...';

        // Enviar el formulario
        this.submit();
    });
}); 