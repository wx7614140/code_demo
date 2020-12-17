package org.febsteam.demos.totp.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : wx
 * @version : 1
 * @date : 2020/12/17 10:28
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = -3556773901860313324L;
    private String username;
    private String password;
    private String email;
    private String twoFactorAuthKey;
    private boolean twoFactorAuthEnabled = false;
}
