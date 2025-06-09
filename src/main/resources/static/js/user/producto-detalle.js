document.addEventListener('DOMContentLoaded', function() {
    // Manejo de miniaturas
    const thumbnails = document.querySelectorAll('.thumbnail-btn');
    const mainImage = document.getElementById('mainImage');
    const imageModal = document.getElementById('imageModal');
    const modalImage = document.getElementById('modalImage');

    thumbnails.forEach(thumb => {
        thumb.addEventListener('click', function() {
            // Actualizar imagen principal
            const imgUrl = this.getAttribute('data-img');
            mainImage.src = imgUrl;
            
            // Actualizar clase activa
            thumbnails.forEach(t => t.classList.remove('active'));
            this.classList.add('active');
        });
    });

    // Zoom y Modal
    mainImage.addEventListener('click', function() {
        modalImage.src = this.src;
        const modal = new bootstrap.Modal(imageModal);
        modal.show();
    });

    // Zoom con mousemove en el modal
    modalImage.addEventListener('mousemove', function(e) {
        const bounds = this.getBoundingClientRect();
        const x = (e.clientX - bounds.left) / bounds.width * 100;
        const y = (e.clientY - bounds.top) / bounds.height * 100;
        
        this.style.transformOrigin = `${x}% ${y}%`;
    });

    // Restablecer zoom al salir
    imageModal.addEventListener('hidden.bs.modal', function() {
        modalImage.style.transform = 'scale(1)';
    });

    // Toggle zoom al hacer clic en la imagen modal
    let isZoomed = false;
    modalImage.addEventListener('click', function() {
        isZoomed = !isZoomed;
        this.style.transform = isZoomed ? 'scale(2.5)' : 'scale(1)';
        this.style.cursor = isZoomed ? 'zoom-out' : 'zoom-in';
    });
}); 