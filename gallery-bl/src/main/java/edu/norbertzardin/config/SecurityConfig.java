package edu.norbertzardin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService customUserDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/zkau*", "/login.zul*", "/register.zul*", "/index.zul*").permitAll()
                .antMatchers("/view.zul", "/upload.zul", "/browse.zul").hasRole("USER")
                .and()
                .formLogin()
                .loginPage("/login.zul").permitAll()
                .failureForwardUrl("/login.zul?error=true").permitAll()
                .defaultSuccessUrl("/view.zul", true)
                .and()
                .logout()
                .logoutUrl("/logout").permitAll()
                .logoutSuccessUrl("/login.zul?logout").permitAll()
                .and()
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable();

    }

    @Autowired
    public void configAuthentification(AuthenticationManagerBuilder auth) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        auth.userDetailsService(customUserDetailsService).passwordEncoder(encoder);
    }
}