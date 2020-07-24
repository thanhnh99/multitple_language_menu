package com.multiple_language_menu.services.authorize;

import org.springframework.security.core.Authentication;

import java.util.List;

public interface IAppAuthorizer {
    boolean authorize(Authentication authentication, List<String> roles);

}
