package jpabook.jpashop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.DispatcherType;

@Configuration
public class SpringSecurityConfig {


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {

        http.csrf().disable().cors().disable()
                .authorizeRequests(request -> request
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .antMatchers("/", "/members/new", "/api/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(
                        login -> login
                                .loginPage("/login")	// [A] 커스텀 로그인 페이지 지정
/*                                .loginProcessingUrl("/login-process")	// [B] submit 받을 url
                                .usernameParameter("userid")	// [C] submit할 아이디
                                .passwordParameter("pw")	// [D] submit할 비밀번호*/
                                .defaultSuccessUrl("/", true)
                                .permitAll()
                )
                .logout(logout -> logout.logoutSuccessUrl("/"));

        return http.build();

    }


}
