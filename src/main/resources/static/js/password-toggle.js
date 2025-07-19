// Funcionalidade para mostrar/ocultar senha
document.addEventListener('DOMContentLoaded', function() {
    const togglePassword = document.getElementById('toggle-password');
    const passwordInput = document.getElementById('password');
    const passwordIcon = document.getElementById('password-icon');

    if (togglePassword && passwordInput && passwordIcon) {
        togglePassword.addEventListener('click', function() {
            // Alternar tipo do input
            const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordInput.setAttribute('type', type);
            
            // Alternar ícone
            if (type === 'text') {
                passwordIcon.classList.remove('fa-eye');
                passwordIcon.classList.add('fa-eye-slash');
                togglePassword.setAttribute('title', 'Ocultar senha');
            } else {
                passwordIcon.classList.remove('fa-eye-slash');
                passwordIcon.classList.add('fa-eye');
                togglePassword.setAttribute('title', 'Mostrar senha');
            }
        });

        // Adicionar efeito hover no botão
        togglePassword.addEventListener('mouseenter', function() {
            this.style.backgroundColor = '#e9ecef';
        });

        togglePassword.addEventListener('mouseleave', function() {
            this.style.backgroundColor = '';
        });
    }
}); 