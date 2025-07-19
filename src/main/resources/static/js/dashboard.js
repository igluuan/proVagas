// Avatar dinâmico
function setUserAvatar(name) {
    const avatar = document.getElementById('user-avatar');
    if (avatar && name) {
        const initials = name.split(' ').map(n => n[0]).join('').toUpperCase();
        avatar.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(initials)}&background=6a11cb&color=fff`;
        console.log('Avatar atualizado para:', name);
    }
}

// Função para carregar perfil do usuário
async function loadUserProfile() {
    console.log('Carregando perfil do usuário...');
    
    try {
        const token = localStorage.getItem('jwt_token');
        if (!token) {
            console.log('Token não encontrado');
            return;
        }

        const response = await fetch('/api/users/profile', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        console.log('Resposta da API:', response.status);

        if (response.ok) {
            const userData = await response.json();
            console.log('Dados do usuário:', userData);
            
            // Atualizar nome do usuário na navbar
            const userNameElement = document.getElementById('user-name');
            const welcomeNameElement = document.getElementById('welcome-name');
            
            if (userNameElement) {
                userNameElement.textContent = userData.name;
                console.log('Nome atualizado na navbar:', userData.name);
            }
            
            if (welcomeNameElement) {
                welcomeNameElement.textContent = userData.name;
                console.log('Nome atualizado no welcome:', userData.name);
            }
            
            setUserAvatar(userData.name);
            
        } else if (response.status === 401) {
            console.error('Token inválido ou expirado');
            logout();
        } else {
            console.error('Erro ao carregar perfil do usuário:', response.status);
            const userNameElement = document.getElementById('user-name');
            const welcomeNameElement = document.getElementById('welcome-name');
            
            if (userNameElement) userNameElement.textContent = 'Usuário';
            if (welcomeNameElement) welcomeNameElement.textContent = 'Usuário';
        }
    } catch (error) {
        console.error('Erro ao carregar perfil:', error);
        const userNameElement = document.getElementById('user-name');
        const welcomeNameElement = document.getElementById('welcome-name');
        
        if (userNameElement) userNameElement.textContent = 'Usuário';
        if (welcomeNameElement) welcomeNameElement.textContent = 'Usuário';
    }
}

// Função para carregar dados do perfil no modal
async function loadProfileData() {
    const loadingDiv = document.getElementById('profile-loading');
    const contentDiv = document.getElementById('profile-content');
    const errorDiv = document.getElementById('profile-error');

    if (loadingDiv) loadingDiv.style.display = 'block';
    if (contentDiv) contentDiv.style.display = 'none';
    if (errorDiv) errorDiv.style.display = 'none';

    try {
        const token = localStorage.getItem('jwt_token');
        const response = await fetch('/api/users/profile', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const userData = await response.json();
            
            // Atualizar dados do modal
            const profileName = document.getElementById('profile-name');
            const profileEmail = document.getElementById('profile-email');
            const profileFullName = document.getElementById('profile-full-name');
            const profileEmailDisplay = document.getElementById('profile-email-display');
            const profileCreatedAt = document.getElementById('profile-created-at');
            
            if (profileName) profileName.textContent = userData.name;
            if (profileEmail) profileEmail.textContent = userData.email;
            if (profileFullName) profileFullName.textContent = userData.name;
            if (profileEmailDisplay) profileEmailDisplay.textContent = userData.email;
            
            if (profileCreatedAt) {
                const createdAt = new Date(userData.createdAt);
                const formattedDate = createdAt.toLocaleDateString('pt-BR');
                profileCreatedAt.textContent = formattedDate;
            }

            if (loadingDiv) loadingDiv.style.display = 'none';
            if (contentDiv) contentDiv.style.display = 'block';
            
        } else {
            throw new Error('Erro ao carregar dados do perfil');
        }
    } catch (error) {
        console.error('Erro ao carregar dados do perfil:', error);
        
        if (loadingDiv) loadingDiv.style.display = 'none';
        if (errorDiv) {
            errorDiv.style.display = 'block';
            const errorMessage = document.getElementById('profile-error-message');
            if (errorMessage) {
                errorMessage.textContent = 'Erro ao carregar dados do perfil. Tente novamente mais tarde.';
            }
        }
    }
}

// Função de logout
async function logout() {
    try {
        const token = localStorage.getItem('jwt_token');
        
        await fetch('/api/auth/logout', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });
    } catch (error) {
        console.error('Erro no logout:', error);
    } finally {
        localStorage.removeItem('jwt_token');
        window.location.href = '/login';
    }
}

// Função para editar perfil
function editProfile() {
    const modal = bootstrap.Modal.getInstance(document.getElementById('profileModal'));
    if (modal) {
        modal.hide();
    }
    
    alert('Funcionalidade de edição de perfil será implementada em breve!');
}

// Função para verificar se o token é válido
function isTokenValid(token) {
    if (!token) return false;
    
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        const currentTime = Date.now() / 1000;
        return payload.exp > currentTime;
    } catch (error) {
        return false;
    }
}

// Inicialização quando o DOM estiver carregado
document.addEventListener('DOMContentLoaded', function () {
    console.log('Dashboard carregado');
    
    // Verificar se o usuário está logado
    const token = localStorage.getItem('jwt_token');
    if (!token) {
        console.log('Token não encontrado, redirecionando para login');
        window.location.href = '/login';
        return;
    }

    console.log('Token encontrado, carregando perfil do usuário');
    
    // Carregar informações do usuário
    loadUserProfile();

    // Event listeners
    const logoutBtn = document.getElementById('logout-btn');
    const editProfileBtn = document.getElementById('edit-profile-btn');
    
    if (logoutBtn) {
        logoutBtn.addEventListener('click', logout);
    }
    
    if (editProfileBtn) {
        editProfileBtn.addEventListener('click', editProfile);
    }

    // Carregar perfil quando o modal for aberto
    const profileModal = document.getElementById('profileModal');
    if (profileModal) {
        profileModal.addEventListener('show.bs.modal', function () {
            loadProfileData();
        });
    }

    // Alternância de tema claro/escuro
    const themeToggle = document.getElementById('theme-toggle');
    if (themeToggle) {
        // Carregar preferência do localStorage
        if (localStorage.getItem('theme') === 'dark') {
            document.body.classList.add('dark-mode');
            themeToggle.innerHTML = '<i class="fas fa-sun"></i>';
        }
        themeToggle.addEventListener('click', function () {
            document.body.classList.toggle('dark-mode');
            const isDark = document.body.classList.contains('dark-mode');
            themeToggle.innerHTML = isDark ? '<i class="fas fa-sun"></i>' : '<i class="fas fa-moon"></i>';
            localStorage.setItem('theme', isDark ? 'dark' : 'light');
        });
    }
});

// Verificar token periodicamente
setInterval(() => {
    const token = localStorage.getItem('jwt_token');
    if (!isTokenValid(token)) {
        logout();
    }
}, 60000); // Verificar a cada minuto 