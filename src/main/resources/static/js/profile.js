document.addEventListener('DOMContentLoaded', function () {
    // Verificar se o usuário está logado
    const token = localStorage.getItem('jwt_token');
    if (!token) {
        window.location.href = '/login';
        return;
    }

    // Carregar informações do usuário
    loadUserProfile();
    loadProfileData();

    // Event listeners
    document.getElementById('logout-btn').addEventListener('click', logout);
    document.getElementById('edit-profile-btn').addEventListener('click', editProfile);
});

// Avatar dinâmico
function setUserAvatar(name) {
    const avatar = document.getElementById('user-avatar');
    if (avatar && name) {
        const initials = name.split(' ').map(n => n[0]).join('').toUpperCase();
        avatar.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(initials)}&background=6a11cb&color=fff`;
    }
}

async function loadUserProfile() {
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
            // Atualizar nome do usuário na navbar
            document.getElementById('user-name').textContent = userData.name;
            setUserAvatar(userData.name);
        } else {
            console.error('Erro ao carregar perfil do usuário');
            logout();
        }
    } catch (error) {
        console.error('Erro ao carregar perfil:', error);
        logout();
    }
}

async function loadProfileData() {
    const loadingDiv = document.getElementById('profile-loading');
    const contentDiv = document.getElementById('profile-content');
    const errorDiv = document.getElementById('profile-error');

    // Mostrar loading
    loadingDiv.style.display = 'block';
    contentDiv.style.display = 'none';
    errorDiv.style.display = 'none';

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
            
            // Atualizar dados da página
            document.getElementById('profile-name').textContent = userData.name;
            document.getElementById('profile-email').textContent = userData.email;
            document.getElementById('profile-full-name').textContent = userData.name;
            document.getElementById('profile-email-display').textContent = userData.email;
            
            // Formatar data de criação
            const createdAt = new Date(userData.createdAt);
            const formattedDate = createdAt.toLocaleDateString('pt-BR');
            document.getElementById('profile-created-at').textContent = formattedDate;

            // Mostrar conteúdo
            loadingDiv.style.display = 'none';
            contentDiv.style.display = 'block';
            
        } else {
            throw new Error('Erro ao carregar dados do perfil');
        }
    } catch (error) {
        console.error('Erro ao carregar dados do perfil:', error);
        
        // Mostrar erro
        loadingDiv.style.display = 'none';
        errorDiv.style.display = 'block';
        document.getElementById('profile-error-message').textContent = 
            'Erro ao carregar dados do perfil. Tente novamente mais tarde.';
    }
}

async function logout() {
    try {
        const token = localStorage.getItem('jwt_token');
        
        // Chamar API de logout
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
        // Remover token e redirecionar
        localStorage.removeItem('jwt_token');
        window.location.href = '/login';
    }
}

function editProfile() {
    // Por enquanto, apenas mostrar mensagem
    // TODO: Implementar página de edição de perfil
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

// Verificar token periodicamente
setInterval(() => {
    const token = localStorage.getItem('jwt_token');
    if (!isTokenValid(token)) {
        logout();
    }
}, 60000); // Verificar a cada minuto 