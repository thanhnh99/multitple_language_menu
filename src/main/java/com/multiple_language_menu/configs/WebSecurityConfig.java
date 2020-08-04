package com.multiple_language_menu.configs;

import com.multiple_language_menu.filters.JWTFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(
//        prePostEnabled = true,
//        securedEnabled = true,
//        jsr250Enabled = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSourceConfig dataSource;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                .antMatchers(HttpMethod.POST, "/login").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .addFilterBefore(new JWTFilter(), UsernamePasswordAuthenticationFilter.class)
//                .csrf().disable()
//                .cors().disable()
//                // disable page caching
//                .headers().cacheControl();
        httpSecurity
                .authorizeRequests()
                .antMatchers( "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .cors().disable()
                // disable page caching
                .headers().cacheControl();
        httpSecurity
                .addFilterBefore(new JWTFilter(), UsernamePasswordAuthenticationFilter.class);
    }
//    @Autowired
//    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication().dataSource(dataSource.dataSource())
//                .usersByUsernameQuery("select username,password,enable from users where username=?")
//                .authoritiesByUsernameQuery("select users.username,  roles.name from roles inner join user_role on roles.id = user_role.role_id" +
//                        " inner join users on user_role.user_id = users.id where users.username=?");
//                .passwordEncoder(new BCryptPasswordEncoder());
//    }

}
