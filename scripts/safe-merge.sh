#!/bin/bash

# Script para merge seguro das alterações para develop
# Uso: ./scripts/safe-merge.sh

set -e  # Para se houver erro

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Funções auxiliares
log_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

log_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

log_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

log_error() {
    echo -e "${RED}❌ $1${NC}"
}

confirm() {
    read -p "$(echo -e ${YELLOW}$1${NC}) [y/N]: " response
    case "$response" in
        [yY][eE][sS]|[yY]) 
            return 0
            ;;
        *)
            return 1
            ;;
    esac
}

# Verificações iniciais
check_prerequisites() {
    log_info "Verificando pré-requisitos..."
    
    # Verificar se estamos no diretório do projeto
    if [[ ! -f "pom.xml" ]]; then
        log_error "Execute este script na raiz do projeto (onde está o pom.xml)"
        exit 1
    fi
    
    # Verificar se git está disponível
    if ! command -v git &> /dev/null; then
        log_error "Git não encontrado. Instale o Git primeiro."
        exit 1
    fi
    
    # Verificar se working directory está limpo
    if [[ -n $(git status --porcelain) ]]; then
        log_error "Working directory não está limpo. Faça commit ou stash das alterações."
        git status
        exit 1
    fi
    
    log_success "Pré-requisitos verificados"
}

# Criar backup
create_backup() {
    local timestamp=$(date +%Y%m%d_%H%M%S)
    local backup_branch="backup-before-merge-${timestamp}"
    
    log_info "Criando backup: ${backup_branch}"
    
    git checkout develop
    git checkout -b "${backup_branch}"
    git push origin "${backup_branch}"
    git checkout develop
    
    log_success "Backup criado: ${backup_branch}"
    echo "${backup_branch}" > .last_backup_branch
}

# Atualizar referências
update_references() {
    log_info "Atualizando referências remotas..."
    
    git fetch --all --prune
    git pull origin develop
    
    log_success "Referências atualizadas"
}

# Analisar mudanças
analyze_changes() {
    log_info "Analisando mudanças a serem mergeadas..."
    
    local source_branch="origin/cursor/analisar-e-otimizar-projeto-ccce"
    
    echo
    log_info "Commits que serão mergeados:"
    git log --oneline develop..${source_branch}
    
    echo
    log_info "Arquivos que serão modificados:"
    git diff --name-only develop ${source_branch}
    
    echo
    if ! confirm "Deseja continuar com o merge?"; then
        log_warning "Merge cancelado pelo usuário"
        exit 0
    fi
}

# Teste de merge
test_merge() {
    local timestamp=$(date +%Y%m%d_%H%M%S)
    local test_branch="test-merge-${timestamp}"
    local source_branch="origin/cursor/analisar-e-otimizar-projeto-ccce"
    
    log_info "Criando branch de teste: ${test_branch}"
    git checkout -b "${test_branch}"
    
    log_info "Fazendo merge de teste..."
    if git merge "${source_branch}"; then
        log_success "Merge de teste bem-sucedido"
    else
        log_error "Conflitos detectados no merge"
        log_info "Resolva os conflitos manualmente e execute:"
        log_info "  git add <arquivos-resolvidos>"
        log_info "  git commit"
        log_info "  git checkout develop"
        log_info "  git merge ${test_branch}"
        exit 1
    fi
    
    # Testar compilação
    log_info "Testando compilação..."
    if mvn clean compile -q; then
        log_success "Compilação bem-sucedida"
    else
        log_error "Falha na compilação após merge"
        git checkout develop
        git branch -D "${test_branch}"
        exit 1
    fi
    
    # Verificar se credenciais não estão expostas
    log_info "Verificando exposição de credenciais..."
    if grep -r "password.*=" src/ | grep -v "\${" | grep -v Binary; then
        log_error "Credenciais hardcoded detectadas!"
        git checkout develop
        git branch -D "${test_branch}"
        exit 1
    else
        log_success "Nenhuma credencial exposta encontrada"
    fi
    
    git checkout develop
    echo "${test_branch}" > .last_test_branch
    log_success "Teste de merge completo"
}

# Merge oficial
official_merge() {
    local test_branch=$(cat .last_test_branch 2>/dev/null || echo "")
    local source_branch="origin/cursor/analisar-e-otimizar-projeto-ccce"
    
    if [[ -z "${test_branch}" ]]; then
        log_error "Nenhuma branch de teste encontrada. Execute o teste primeiro."
        exit 1
    fi
    
    log_info "Fazendo merge oficial para develop..."
    
    if git merge "${source_branch}"; then
        log_success "Merge oficial bem-sucedido"
    else
        log_error "Erro no merge oficial (isso não deveria acontecer após teste)"
        exit 1
    fi
    
    # Push das alterações
    log_info "Fazendo push para origin/develop..."
    if git push origin develop; then
        log_success "Push bem-sucedido"
    else
        log_error "Falha no push. Verifique conflitos remotos."
        exit 1
    fi
}

# Limpeza
cleanup() {
    local test_branch=$(cat .last_test_branch 2>/dev/null || echo "")
    
    if [[ -n "${test_branch}" ]]; then
        log_info "Removendo branch de teste: ${test_branch}"
        git branch -D "${test_branch}" 2>/dev/null || true
    fi
    
    rm -f .last_test_branch .last_backup_branch
    
    log_info "Atualizando referências remotas..."
    git remote prune origin
    
    log_success "Limpeza concluída"
}

# Função principal
main() {
    echo "🔀 Script de Merge Seguro para ProVagas"
    echo "======================================"
    echo
    
    check_prerequisites
    
    if confirm "Criar backup da branch develop atual?"; then
        create_backup
    fi
    
    update_references
    analyze_changes
    
    if confirm "Executar teste de merge?"; then
        test_merge
    fi
    
    if confirm "Executar merge oficial para develop?"; then
        official_merge
    fi
    
    if confirm "Executar limpeza de branches temporárias?"; then
        cleanup
    fi
    
    echo
    log_success "🎉 Merge concluído com sucesso!"
    log_info "Verifique se tudo está funcionando:"
    echo "  1. Configure .env: cp .env.example .env"
    echo "  2. Execute: mvn spring-boot:run"
    echo "  3. Teste: http://localhost:8080"
}

# Tratamento de erros
trap 'log_error "Script interrompido. Execute cleanup manual se necessário."' INT

# Executar função principal
main "$@"