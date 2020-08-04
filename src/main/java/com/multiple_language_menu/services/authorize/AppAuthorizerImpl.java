package com.multiple_language_menu.services.authorize;

import com.multiple_language_menu.models.entities.Roles;
import com.multiple_language_menu.models.entities.Users;
import com.multiple_language_menu.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.*;
@Service("appAuthorizer")
public class AppAuthorizerImpl implements IAppAuthorizer {
    @Autowired
    IUserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(AppAuthorizerImpl.class);

    @Override
    public boolean authorize(Authentication authentication, List<String> roles) {
//        String securedPath = extractSecuredPath(callerObj);
//        if (securedPath==null || "".equals(securedPath.trim())) {//login, logout
//            return true;
//        }
//        String menuCode = securedPath.substring(1);//Bỏ dấu "/" ở đầu Path
        for (String role : roles)
        {
            System.out.print(role + " - ");
        }
        System.out.println(" only proceed this task!!!!!");
        boolean isAllow = false;
        try {
            UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) authentication;
            if (user==null){
                return isAllow;
            }
            String username = (String)user.getPrincipal();
            if (username==null || "".equals(username.trim())) {
                return isAllow;
            }
            //Truy vấn vào CSDL theo userId + menuCode + action
            //Nếu có quyền thì
            Users userEntity = userRepository.findByUsername(username);
            if(userEntity != null && userEntity.getEnable())
            {
                for(Roles roleEntity : userEntity.getRoles())
                {
                    for (String role : roles)
                    {
                        if(roleEntity.getCode().equals(role))
                        {
                            isAllow = true;
                            break;
                        }
                    }
                    if (isAllow == true)
                    {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
            throw e;
        }
        return isAllow;
    }

//    // Lay ra securedPath duoc Annotate RequestMapping trong Controller
//    private String extractSecuredPath(Object callerObj) {
//        Class<?> clazz = ResolvableType.forClass(callerObj.getClass()).getRawClass();
//        Optional<Annotation> annotation = Arrays.asList(clazz.getAnnotations()).stream().filter((ann) -> {
//            return ann instanceof RequestMapping;
//        }).findFirst();
//        logger.debug("FOUND CALLER CLASS: {}", ResolvableType.forClass(callerObj.getClass()).getType().getTypeName());
//        if (annotation.isPresent()) {
//            return ((RequestMapping) annotation.get()).value()[0];
//        }
//        return null;
//    }
}