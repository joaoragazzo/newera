package com.new_era.alpha.security;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import jakarta.servlet.http.HttpServletRequest;

public class SessionValidationFilter extends AbstractPreAuthenticatedProcessingFilter {

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        Object sessionVar = request.getSession().getAttribute("player_id");
        if (sessionVar == null) {
            throw new RuntimeException("Usuário não está logado.");
        }
        return sessionVar;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return null;
    }
}
