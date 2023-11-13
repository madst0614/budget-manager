package wanted.n.budgetmanager.server.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import wanted.n.budgetmanager.server.config.filter.JwtFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    /* Spring Security 필터 체인 설정
    *   jwtFilter 추가
    *  */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .cors(Customizer.withDefaults())
                .headers(headers -> headers.frameOptions().disable())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests(authorize -> authorize
                        .antMatchers("/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                );

        return http.build();
    }

    /* 비밀번호 암호화에 사용할 BcryptPasswordEncoder 빈 등록*/
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}