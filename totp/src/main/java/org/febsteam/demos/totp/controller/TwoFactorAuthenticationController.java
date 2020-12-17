package org.febsteam.demos.totp.controller;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.RequiredArgsConstructor;
import org.febsteam.demos.totp.bean.User;
import org.febsteam.demos.totp.util.CacheUtil;
import org.febsteam.demos.totp.util.FebsQRGenerator;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author : wx
 * @version : 1
 * @date : 2020/12/17 10:15
 */
@RestController
@RequiredArgsConstructor
public class TwoFactorAuthenticationController {

    private final GoogleAuthenticator googleAuthenticator;
    private final String GOOGLE_AUTH_ISSUER = "localhost";

    @RequestMapping(value = "/init2fa", method = RequestMethod.GET)
    public void initTwoFactorAuth(String token, HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = getLoggedInUser(token);
        GoogleAuthenticatorKey googleAuthenticatorKey = googleAuthenticator.createCredentials();
        user.setTwoFactorAuthKey(googleAuthenticatorKey.getKey());
        CacheUtil.setUser(token,user);
        FebsQRGenerator.geneQRCode(GOOGLE_AUTH_ISSUER, user.getEmail(), googleAuthenticatorKey,response.getOutputStream());
    }

    @RequestMapping(value = "/confirm2fa", method = RequestMethod.POST)
    @ResponseBody
    public boolean confirmTwoFactorAuth(@RequestParam("token")String token, @RequestParam("code") int code) {
        User user = getLoggedInUser(token);
        boolean result = googleAuthenticator.authorize(user.getTwoFactorAuthKey(), code);
        user.setTwoFactorAuthEnabled(result);
        CacheUtil.setUser(token,user);
        return result;
    }

    @RequestMapping(value = "/disable2fa", method = RequestMethod.GET)
    @ResponseBody
    public void disableTwoFactorAuth(String token) {
        User user = getLoggedInUser(token);
        user.setTwoFactorAuthKey(null);
        user.setTwoFactorAuthEnabled(false);
        CacheUtil.setUser(token,user);
    }

    @RequestMapping(value = "/requires2fa", method = RequestMethod.POST)
    @ResponseBody
    public boolean login(@RequestParam("email") String email) {
        // TODO consider verifying the password here in order not to reveal that a given user uses 2FA
        return CacheUtil.getUserDetailsByEmail(email).isTwoFactorAuthEnabled();
    }
    private User getLoggedInUser(String token){
       User user = CacheUtil.getUser(token);
       if(user == null){
           user = new User();
           user.setEmail("177768063@qq.com");
           user.setUsername("wangxin");
           user.setPassword("123456");
           user.setTwoFactorAuthEnabled(false);
           CacheUtil.setUser(token,user);
       }
       return user;
    }
}
