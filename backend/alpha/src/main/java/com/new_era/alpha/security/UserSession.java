package com.new_era.alpha.security;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@SessionScope
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserSession {
    private Integer player_id;
}
