package com.bulain.mybatis.demo.dao;

import com.alibaba.fastjson.JSON;
import com.microsoft.aad.msal4j.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Collections;

@Slf4j
public class IamDemo {

    @Test
    @SneakyThrows
    public void acquireToken() {
        String secret = "***";
        String clientId = "***";
        String tenantId = "***";
        String tokenUrl = "https://login.microsoftonline.com/%s/oauth2/v2.0/token";
        String authCode = "***";
        String redirectUri = "https://yywmsuat.jebsen.com/logining/sso.html";

        IClientCredential credential = ClientCredentialFactory.createFromSecret(secret);
        ConfidentialClientApplication cca =
                ConfidentialClientApplication
                        .builder(clientId, credential)
                        .authority(String.format(tokenUrl, tenantId))
                        .build();
        AuthorizationCodeParameters acp = AuthorizationCodeParameters
                .builder(authCode, new URI(redirectUri))
                .scopes(Collections.singleton("https://graph.microsoft.com/.default"))
                .build();

        IAuthenticationResult result = cca.acquireToken(acp).get();
        log.info("{}", JSON.toJSONString(result));

        log.info("accessToken: {}", result.accessToken());
        log.info("username: {}", result.account().username());

    }

}
