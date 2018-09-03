package com.bulain.oauth.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.TokenStore;

@EnableResourceServer
@Configuration
public class OauthResourceInit extends ResourceServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Bean
    public TokenExtractor tokenExtractor() {
        return new BearerTokenExtractor();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore).tokenExtractor(tokenExtractor()).resourceId(null);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().antMatcher("/api/**").authorizeRequests().anyRequest().authenticated();
    }

}
