package com.devluan.proVagas.domain.user.model;

public enum ApplicationRole {
    ADMIN,
    USER,
    COMPANY;

    public String getSpringSecurityRoleName() {
        return "ROLE_" + this.name();
    }
}
