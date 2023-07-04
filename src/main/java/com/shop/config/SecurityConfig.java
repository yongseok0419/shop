package com.shop.config;

import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter { //Override Method(Alt + Insert)
    @Autowired
    MemberService memberService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        /**
         *  static 디렉터리 하위 파일은 인증을 무시하도록 설정합니다.
         */
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         *  http 요청에 대한 보안을 설정합니다. 페이지 권한 설정, 로그인 페이지 설정, 로그아웃 메서드 등에 대한 설정을 작성합니다.
         */
        http.formLogin()
                .loginPage("/members/login")
                .defaultSuccessUrl("/members/")
                .usernameParameter("email")
                .failureUrl("/members/login/error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/members/");

        http.authorizeHttpRequests()
                .mvcMatchers("/", "/members/**", "/item/**", "images/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated(); //mvcMatchers에 해당하는 url 경로를 제외한 모든 경로는 인증(로그인)을 요구하도록 설정

        /**
         *  인증되지 않은 사용자가 리소스에 접근하였을 때 수행되는 핸들러를 등록합니다.
         */
        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        //아래 부분은 CSRF 토큰을 활성화하고, CSRF 토큰을 쿠키에 저장하고 응답 시에 JS가 해당 쿠키를 사용할 수 있도록 설정
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        /**
         *  비밀번호를 데이터베이스에 그대로 저장했을 경우, 데이터베이스가 해킹당하면 고객의 회원 정보가 그대로 노출됩니다.
         *  이를 해결하기 위해 BCryptPasswordEncoder의 해시 함수를 이용하여 비밀번호를 암호화하여 저장합니다.
         */
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService)
                .passwordEncoder((passwordEncoder()));
    }
}
