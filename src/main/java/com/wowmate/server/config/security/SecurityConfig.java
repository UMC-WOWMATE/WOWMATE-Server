package com.wowmate.server.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable() // REST API는 UI를 사용하지 않으므로 기본설정을 비활성화
                .csrf().disable() // REST API는 브라우저 환경을 사용하지 않기에 csrf 보안이 필요 없으므로 비활성화
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT Token 인증방식으로 세션은 필요 없으므로 비활성화

                .and()
                .authorizeRequests() // 리퀘스트에 대한 사용권한 체크
                .antMatchers("/sign-api/sign-in", "/sign-api/sign-up", "/sign-api/exception",
                        "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**", "/sign-api/test")
                .permitAll() // 가입 및 로그인 주소는 허용
                .antMatchers(HttpMethod.GET, "/product/**")
                .permitAll() // product로 시작하는 Get 요청은 허용
                .antMatchers("**exception**")
                .permitAll()
                .anyRequest().hasRole("ADMIN") // 나머지 요청은 인증된 ADMIN만 접근 가능

                // JWT Token 필터를 id/password 인증 필터 이전에 추가
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and().build();
    }

}
