package com.multiple_language_menu.models.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class GranAuthorityImpl implements GrantedAuthority {

    private String authority;


    @Override
    public String getAuthority() {
        return this.authority;
    }
}
