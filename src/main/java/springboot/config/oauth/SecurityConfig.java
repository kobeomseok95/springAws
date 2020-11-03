package springboot.config.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import springboot.domain.user.Role;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable().headers().frameOptions().disable()
                .and()
                    .authorizeRequests()
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()  //모두에게 오픈
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name())    ///api/v1/들은 USER라는 권한이 있는 사람만
                    .anyRequest().authenticated()   //나머지는 인증된 사용자에게만
                .and()
                    .logout()
                        .logoutSuccessUrl("/")
                .and()
                    .oauth2Login()
                        .userInfoEndpoint() //로그인 성공 후 사용자의 정보를 가져오는것 
                            .userService(customOAuth2UserService);  //로그인 성공후 후속 조치를 진행할 UserService의 구현체

    }
}
