package org.example.config;

import org.example.service.LWUsersDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    LWUsersDetailsService lwUsersDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(lwUsersDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/user/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login") // Указываем страницу входа
                .defaultSuccessUrl("/user/cabinet") // После успешной аутентификации перенаправляем сюда
                .permitAll() // Разрешаем всем доступ к странице входа
                .and()
                .logout()
                .logoutUrl("/logout") // Указываем URL для выхода
                .logoutSuccessUrl("/login?logout") // После выхода перенаправляем сюда
                .permitAll() // Разрешаем всем доступ к странице выхода
                .and()
                .exceptionHandling()
                .accessDeniedPage("/")
                .and()
                .csrf().disable();

        return http.build();
    }
}
