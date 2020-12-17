package org.febsteam.demos.totp.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.febsteam.demos.totp.bean.User;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author : wx
 * @version : 1
 * @date : 2020/12/17 10:56
 */
public class CacheUtil {
    private static final Cache<String, User> storage = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(1, TimeUnit.DAYS)
            .build();

    public static User getUser(String token){
        return storage.getIfPresent(token);
    }
    public static void setUser(String token,User user){
        storage.put(token,user);
    }

    public static User getUserDetailsByEmail(String email) {
        Optional<Map.Entry<String,User>> optional = storage.asMap().entrySet().stream().filter(v-> v.getValue().isTwoFactorAuthEnabled()).findAny();
        return optional.isPresent()? optional.get().getValue():null;
    }
}
