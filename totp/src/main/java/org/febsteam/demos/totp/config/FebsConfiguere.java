package org.febsteam.demos.totp.config;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wx
 * @version : 1
 * @date : 2020/12/17 13:48
 */
@Configuration
public class FebsConfiguere {

    @Bean
    public GoogleAuthenticator googleAuthenticator(){
        return new GoogleAuthenticator();
    }
}
