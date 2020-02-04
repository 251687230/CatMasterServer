package com.zous.catmaster.utils;

import com.zous.catmaster.bean.Token;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class TokenUtilsTest {
    @Test
    public void testTokenCreate(){
        String userId = "12";
        TokenUtils tokenUtils = TokenUtils.defaultUtil();
        String tokenstr = tokenUtils.create(UUID.randomUUID().toString(), "default", String.valueOf(userId)).getTokenStr();
        Token token = tokenUtils.parse(tokenstr);
        Assert.assertNotNull(token);
        Assert.assertEquals(token.getPlayload().getSub(),userId);
    }

    @Test
    public void testTokenRefresh(){
        String userId = "12";
        TokenUtils tokenUtils = TokenUtils.defaultUtil();
        String tokenstr = tokenUtils.create(UUID.randomUUID().toString(), "default", String.valueOf(userId)).getTokenStr();
        Token newToken = tokenUtils.parseAndRefresh(tokenstr);
        Assert.assertNotNull(newToken);
        Assert.assertEquals(newToken.getPlayload().getSub(),userId);

        String newTokenStr = newToken.getTokenStr();
        Token validateToken = tokenUtils.parse(newTokenStr);
        Assert.assertNotNull(validateToken);
    }
}
