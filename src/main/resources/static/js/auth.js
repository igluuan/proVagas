document.addEventListener('DOMContentLoaded', function () {
    const loginForm = document.getElementById('login-form');
    const registerForm = document.getElementById('register-form');

    if (loginForm) {
        loginForm.addEventListener('submit', async function (e) {
            e.preventDefault();

            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            const errorDiv = document.getElementById('login-error');

            try {
                const response = await fetch('/api/auth/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ email, password })
                });

                if (response.ok) {
                    const data = await response.json();
                    localStorage.setItem('jwt_token', data.token);
                    window.location.href = '/dashboard'; // Redirecionar para o dashboard
                } else {
                    const errorData = await response.json();
                    errorDiv.textContent = errorData.message || 'Falha no login. Verifique suas credenciais.';
                    errorDiv.style.display = 'block';
                }
            } catch (error) {
                errorDiv.textContent = 'Ocorreu um erro ao tentar fazer login. Tente novamente mais tarde.';
                errorDiv.style.display = 'block';
            }
        });
    }

    if (registerForm) {
        registerForm.addEventListener('submit', async function (e) {
            e.preventDefault();

            const name = document.getElementById('name').value;
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            const errorDiv = document.getElementById('register-error');

            try {
                const response = await fetch('/api/auth/register', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ name, email, password })
                });

                if (response.ok) {
                    window.location.href = '/login'; // Redirecionar para o login
                } else {
                    const errorData = await response.json();
                    errorDiv.textContent = errorData.message || 'Falha no registro. Verifique os dados informados.';
                    errorDiv.style.display = 'block';
                }
            } catch (error) {
                errorDiv.textContent = 'Ocorreu um erro ao tentar se registrar. Tente novamente mais tarde.';
                errorDiv.style.display = 'block';
            }
        });
    }
});
