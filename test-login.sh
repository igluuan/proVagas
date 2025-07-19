#!/bin/bash

echo "🧪 Testando fluxo de login..."

# 1. Fazer login
echo "1. Fazendo login..."
LOGIN_RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"marcos@example.com","password":"luan123"}')

if [ $? -eq 0 ]; then
    echo "✅ Login bem-sucedido"
    TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
    echo "Token obtido: ${TOKEN:0:50}..."
else
    echo "❌ Falha no login"
    exit 1
fi

# 2. Testar endpoint de perfil
echo "2. Testando endpoint de perfil..."
PROFILE_RESPONSE=$(curl -s -X GET http://localhost:8080/api/users/profile \
  -H "Authorization: Bearer $TOKEN")

if [ $? -eq 0 ] && [[ $PROFILE_RESPONSE == *"marcos"* ]]; then
    echo "✅ Perfil carregado com sucesso"
    echo "Resposta: $PROFILE_RESPONSE"
else
    echo "❌ Falha ao carregar perfil"
    echo "Resposta: $PROFILE_RESPONSE"
    exit 1
fi

echo "🎉 Todos os testes passaram!" 