# 🚀 Guia Rápido de Merge para Develop

## ⚡ **EXECUÇÃO AUTOMÁTICA (RECOMENDADO)**

```bash
# Execute o script automatizado
./scripts/safe-merge.sh
```

O script vai:
- ✅ Criar backup automático
- ✅ Verificar conflitos
- ✅ Testar merge em branch temporária
- ✅ Fazer merge oficial
- ✅ Limpar branches temporárias

## 🛠️ **EXECUÇÃO MANUAL (SE PREFERIR)**

### 1. Preparação
```bash
# Backup de emergência
git checkout develop
git checkout -b backup-$(date +%Y%m%d_%H%M%S)
git push origin backup-$(date +%Y%m%d_%H%M%S)
git checkout develop

# Atualizar tudo
git fetch --all --prune
git pull origin develop
```

### 2. Merge Seguro
```bash
# Criar branch de teste
git checkout -b test-merge-$(date +%Y%m%d_%H%M%S)

# Merge de teste
git merge origin/cursor/analisar-e-otimizar-projeto-ccce

# Testar compilação
mvn clean compile

# Se OK, fazer merge oficial
git checkout develop
git merge origin/cursor/analisar-e-otimizar-projeto-ccce
git push origin develop
```

### 3. Verificação Final
```bash
# Verificar credenciais não expostas
grep -r "password.*=" src/ | grep -v "\${" || echo "✅ Seguro"

# Testar aplicação
cp .env.example .env
# Editar .env com credenciais
mvn spring-boot:run
```

## 🆘 **COMANDOS DE EMERGÊNCIA**

### Se algo der errado:
```bash
# Abortar merge em andamento
git merge --abort

# Voltar para backup
git checkout develop
git reset --hard backup-<timestamp>
git push --force-with-lease origin develop
```

### Se push falhar:
```bash
# Resolver conflitos remotos
git pull origin develop
git push origin develop
```

## 📋 **CHECKLIST RÁPIDO**

- [ ] Working directory limpo (`git status`)
- [ ] Backup criado
- [ ] Merge testado em branch temporária
- [ ] Compilação OK (`mvn clean compile`)
- [ ] Credenciais verificadas
- [ ] Merge oficial realizado
- [ ] Push para origin concluído

## 🎯 **COMMITS QUE SERÃO MERGEADOS**

1. **f99ce2c** - Melhorias de segurança:
   - Remoção de credenciais hardcoded
   - Configuração JWT com chaves RSA
   - Estrutura de variáveis de ambiente
   - Documentação completa

2. **9f2daa4** - Frontend implementado:
   - Páginas HTML completas
   - JavaScript para autenticação
   - CSS customizado
   - Integração frontend-backend

## ⏱️ **TEMPO ESTIMADO**

- **Automático**: 5-10 minutos
- **Manual**: 15-20 minutos
- **Com conflitos**: 30+ minutos

## 📞 **SUPORTE**

Se precisar de ajuda:
1. Consulte `MERGE_GUIDE.md` para detalhes completos
2. Use comandos de emergência se algo der errado
3. Não hesite em pedir ajuda se necessário

**✨ Lembre-se: Sempre é melhor ser cauteloso do que rápido!**