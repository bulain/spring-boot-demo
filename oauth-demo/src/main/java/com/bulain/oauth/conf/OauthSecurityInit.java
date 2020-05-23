package com.bulain.oauth.conf;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

@EnableWebSecurity
@Configuration
public class OauthSecurityInit extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Bean
	public UserDetailsService userDetailsService() {
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
		jdbcUserDetailsManager.setDataSource(dataSource);
		return jdbcUserDetailsManager;
	}

	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder());
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/static/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
	    /*.requestMatchers()
	        .requestMatchers(
	                new OrRequestMatcher(
	                        new AntPathRequestMatcher("/login"),
	                        new AntPathRequestMatcher("/logout"),
	                        new AntPathRequestMatcher("/error"),
	                        new AntPathRequestMatcher("/oauth/authorize"),
	                        new AntPathRequestMatcher("/oauth/token"),
	                        new AntPathRequestMatcher("/oauth/check_token"),
	                        new AntPathRequestMatcher("/oauth/confirm_access"),
	                        new AntPathRequestMatcher("/oauth/error")
	                )
	        )
	        .and()*/
		.antMatcher("/**")
	    .authorizeRequests()
        	.anyRequest()
        	.authenticated()
	        .and()
        /*.authorizeRequests()
        	.antMatchers("/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs", "/webjars/**")
        	.permitAll()
        	.and()*/
        .formLogin()
        	.loginPage("/login")
        	.defaultSuccessUrl("/home")
        	.permitAll()
        	.and()
        .logout()
        	.logoutUrl("/logout")
        	.permitAll()
        	.and()
        /*.csrf()
        	.disable()*/
        .cors()
        	.disable()
        ;
		
    }

}
