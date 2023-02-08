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
                .antMatchers("/sign-in", "/sign-up", "/sign-api/exception",
                        "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**", "/emailConfirm", "/univ")
                .permitAll() // 가입 및 로그인 주소는 허용
                .antMatchers("/admin-api/**")
                .hasRole("ADMIN")
                .antMatchers("**exception**")
                .permitAll()
                .anyRequest().hasRole("USER") // 나머지 요청은 인증된 USER만 접근 가능
                /**
                 * Spring Security에서 Role의 경우에는 prefix로 "ROLE_" 이라는 문자를 붙인다.
                 * 따라서 다음의 둘 중 하나를 선택해서 적용해야 한다.
                 *
                 * 1. DB상 role 정보에 prefix로 "ROLE_"을 붙여준다.
                 * 2. hasRole 대신 hasAuthority를 사용한다.
                 *
                 * hasRole 안에 "ROLE_" prefix를 직접 넣어주면 spring security가 넣어줘서 판단하니 직접 넣지 말라는 컴파일 에러도 발생한다.
                 */
                // JWT Token 필터를 id/password 인증 필터 이전에 추가
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and().build();
    }

}