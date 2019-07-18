package org.ria.ifzz.RiaApp.security;

import org.ria.ifzz.RiaApp.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.ria.ifzz.RiaApp.security.SecurityConstants.H2_URL;
import static org.ria.ifzz.RiaApp.security.SecurityConstants.SIGN_UP_URLS;


@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(
//        securedEnabled = true,
//        jsr250Enabled = true,
//        prePostEnabled = true
//)
public class SecurityConfig
//        extends WebSecurityConfigurerAdapter
{

//    @Autowired
//    private JwtAuthenticationEntryPoint unauthorizedHandler;
//
//    @Autowired
//    private CustomUserDetailsService customUserDetailsService;
//
//    @Bean
//    public JwtAuthenticationFilter authenticationFilter(){return new JwtAuthenticationFilter();}
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder)
//            throws Exception {
//        authenticationManagerBuilder
//                .userDetailsService(customUserDetailsService)
//                .passwordEncoder(bCryptPasswordEncoder);
//    }
//
//    @Override
//    @Bean(BeanIds.AUTHENTICATION_MANAGER)
//    protected AuthenticationManager authenticationManager() throws Exception {
//        return super.authenticationManager();
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http.cors().and().csrf().disable()
//                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .headers().frameOptions().sameOrigin()// enable H2 console
//                .and()
//                .authorizeRequests()
//                .antMatchers(
//                        "/",
//                        "/favicon.ico",
//                        "/**/*.png",
//                        "/**/*.gif",
//                        "/**/*.svg",
//                        "/**/*.jpg",
//                        "/**/*.html",
//                        "/**/*.css",
//                        "/**/*.js"
//                ).permitAll().antMatchers("/api/users/**").permitAll()
//                .antMatchers(SIGN_UP_URLS).permitAll()
//                .antMatchers(H2_URL).permitAll()
//                .anyRequest().authenticated();
//
//        http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//    }
}
