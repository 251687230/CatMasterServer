package com.zous.catmaster.utils;

import com.zous.catmaster.bean.Token;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


/**
 * JWT token 认证工具
 *
 * @author huwei
 *
 */
public class TokenUtils {
    /*
     * 过期时间 7天
     */
    private Long expTime = Long.valueOf(7* 24 * 60 * 60 * 1000);
    /*
     * 宽限时间，即最终过期时间 14天
     */
    private Long tokeGraTime = Long.valueOf(expTime * 2);
    /*
     * token 签发主体
     */
    private String issuser = "AUTHENTATICTION_CENTER";
    /*
     * jwt的接受对象
     */
    private String audience = "CLIENT";
    /**
     * token 头类型
     */
    private String headType = "JWT";
    /**
     * Secret
     */
    private String secret = "zous.catmaster@2019";
    /**
     * 加密方式
     */
    private String headALG = "HS256";
    private TokenUtils() {}
    private TokenUtils(Long expTime, Long tokeGraTime, String issuser, String audience, String headType,
                         String headALG) {
        this.expTime = expTime == null ? this.expTime : expTime;
        this.tokeGraTime = tokeGraTime == null ? this.tokeGraTime : tokeGraTime;
        this.issuser = issuser == null ? this.issuser : issuser;
        this.audience = audience == null ? this.audience : audience;
        this.headType = headType == null ? this.headType : headType;
        this.headALG = headALG == null ? this.headALG : headALG;
    }
    /**
     * 默认
     */
    final static private TokenUtils DEFAULT = new TokenUtils();
    /**
     * 用户自定义
     */
    static private TokenUtils userDefined = null;
    /**
     * 该方法只允许初始化一次，其目的是：防止多次调用该方法后，造成jwt认证信息不一致，导致jwt认证失败；
     *
     * @param expTime     过期时间
     * @param tokeGraTime 宽限时间
     * @param issuser     发行人
     * @param audience    接受人
     * @param headType    token类型
     * @param headALG     算法
     */
    static public void buildDefined(Long expTime, Long tokeGraTime, String issuser, String audience, String headType,
                                    String headALG) {
        synchronized (TokenUtils.class) {
            if (userDefined != null) {
                throw new RuntimeException("该方法只能初始化一次");
            }
            userDefined = new TokenUtils(expTime, tokeGraTime, issuser, audience, headType, headALG);
        }
    }
    /**
     * 获取自定义jwt工具
     *
     * @return
     */
    static public  TokenUtils definedUtil() {
        if (userDefined == null) {
            throw new RuntimeException("用户尚未自定义jwt信息，请调用buildDefined()方法初始化；");
        }
        return userDefined;
    }
    /**
     * 获取默认jwt工具
     *
     * @return
     */
    static public  TokenUtils defaultUtil() {
        return DEFAULT;
    }
    /**
     * 创建token
     * @param tokenId   当前tokenid token的唯一标识
     * @param userType token持有者类型，可分辨是哪类用户访问
     * @param userInfo  用户信息；
     * @return
     */
    public Token create( String tokenId, String userType, String userInfo) {
        Token token = new Token();
        // 创建头
        Token.Head head = new Token.Head();
        head.setAlg(this.headALG);
        head.setTyp(this.headType);
        // 创建载荷
        // 签发时间
        long iat = System.currentTimeMillis();
        // 过期时间
        long exp = iat + this.expTime;
        // 最后存活时间
        long gra = iat + this.tokeGraTime;
        Token.Playload playload = new Token.Playload();
        playload.setAud(this.audience); // 接收token的一方
        playload.setIat(iat); // 签发时间
        playload.setExp(exp);
        playload.setGra(gra);
        playload.setIss(this.issuser);// 签发者
        playload.setJti(tokenId); // token唯一身份标识
        playload.setNbf(iat);// 生效时间,立即生效
        playload.setSub(userInfo); // token的所属者,可以存放用户名
        playload.setTyp(userType.toUpperCase());
        // 创建token
        String headBase64 = TokenUtil.headToBase64(head);
        String playloadBase64 = TokenUtil.playloadToBase64(playload);
        String signature = TokenUtil.secretToBase64(headBase64, playloadBase64, secret);// token签名
        String tokenStr = headBase64 + "." + playloadBase64 + "." + signature; // token字符串
        // 组装token对象
        token.setHead(head);
        token.setPlayload(playload);
        token.setBase64Head(headBase64);
        token.setBase64PlayLoad(playloadBase64);
        token.setSignature(signature);
        token.setTokenStr(tokenStr);
        return token;
    }
    /**
     * 解析token字符串为token对象 ，如果token字符串信息错误，则返回null
     * @param token 签发的token字符串信息
     * @return
     */
    public Token parse(String token) {
        String[] tokens = null;
        // 检查参数错误和token非法；
        if (token == null || secret == null || (tokens = token.split("\\.")) == null || tokens.length != 3) {
            return null;
        }
        // token分解
        String base64Head = tokens[0].trim(); // token头
        String base64Playload = tokens[1].trim(); // token载荷
        String signature = tokens[2].trim(); // token签名
        // 验证签名是否为合法的
        String signaturedStr = TokenUtil.secretToBase64(base64Head, base64Playload, secret.trim()); // token签名
        //	token被修改
        if (!signature.equals(signaturedStr)) {
            return null;
        }
        Token.Head head = TokenUtil.base64ToHead(base64Head);
        Token.Playload playLoad = TokenUtil.base64ToPlayload(base64Playload);
        Token rs = new Token();
        rs.setBase64Head(base64Head);
        rs.setBase64PlayLoad(base64Playload);
        rs.setHead(head);
        rs.setPlayload(playLoad);
        rs.setSignature(signature);
        rs.setTokenStr(token);
        return rs;
    }
    /**
     * 解析并更新token
     * token非法或token已失效，则返回null
     * @param token token字符串
     * @return
     */
    public Token parseAndRefresh(String token) {
        Token t = parse(token);
        return t == null || isInvalid(t) ? null : refresh(t);
    }
    /**
     * 检查token字符串是否非法
     * @param token
     * @return true 非法token， false 正确token；
     */
    public boolean isIllegality(String token) {
        return parse(token) == null;
    }
    /**
     * 检查token是否过期
     * @param token
     * @return true 过期 ，false反之
     */
    public boolean isTimeout(String token) {
        return isTimeout(parse(token));
    }
    /**
     * 检查token是否过期
     * @param token
     * @return true 过期 ，false反之
     */
    public boolean isTimeout(Token token) {
        Token.Playload pl = token.getPlayload();
        return System.currentTimeMillis() - pl.getNbf() >= pl.getExp() - pl.getNbf();
    }
    /**
     * 检查token是否无效
     * @param token
     * @return true 无效 ，false 反之
     */
    public boolean isInvalid(String token ,String secret) {
        return isInvalid(parse(token));
    }
    /**
     * 检查token是否无效
     * @param token
     * @return true 无效 ，false 反之
     */
    public boolean isInvalid(Token token) {
        Token.Playload pl = token.getPlayload();
        return System.currentTimeMillis() - pl.getNbf() >= pl.getGra() - pl.getNbf();
    }
    /**
     * 刷token的过期时间和最长宽限时间；
     * @param token
     */
    public Token refresh(Token token) {
        Token.Playload pl = token.getPlayload();
        pl.setNbf(System.currentTimeMillis());
        pl.setExp(pl.getNbf() + this.expTime);
        pl.setGra(pl.getNbf() + this.tokeGraTime);
        return token;
    }
    /**
     * token工具
     */
    static private class TokenUtil {
        final static private String CONCAT_CHARACTER = ".";
        static public String headToBase64(Token.Head head) {
            return objToBase64(head);
        }
        static public String playloadToBase64(Token.Playload playload) {
            return objToBase64(playload);
        }
        static public Token.Head base64ToHead(String headBase64) {
            return BeanUtils.toBean(Base64.getDecoder().decode(headBase64.getBytes()), Token.Head.class);
        }
        static public Token.Playload base64ToPlayload(String playBase64) {
            return BeanUtils.toBean(Base64.getDecoder().decode(playBase64), Token.Playload.class);
        }
        static public String secretToBase64(String headBase64, String playloadBase64, String secret) {
            String signature = (headBase64 + CONCAT_CHARACTER + playloadBase64 + CONCAT_CHARACTER + secret);
            return Base64.getEncoder().encodeToString(SHA256Encode(signature));
        }
        /*
         * 使用SHA256加密
         */
        static private byte[] SHA256Encode(String str) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                digest.update(str.getBytes());
                return digest.digest();
            } catch (NoSuchAlgorithmException e) {
                //
            }
            return null;
        }
        static private String objToBase64(Object obj) {
            return Base64.getEncoder().encodeToString(BeanUtils.toJsonAsBytes(obj));
        }
    }

//	public static void main(String[] args) throws InterruptedException {
//		 TokenUtils jwt =  TokenUtils.defaultUtil();
//		Token token = jwt.create("1", "1", "web user", "user ,pwd");
//		System.out.println(token);
//		System.out.println(token.getTokenStr());
//		Token token2 = jwt.parse(token.getTokenStr(), "1");
//		System.out.println(token2);
//		System.out.println(token2.getTokenStr());
//		System.out.println("生效时间：" + token2.getPlayload().getNbf());
//		System.out.println("过期时间：" + token2.getPlayload().getExp());
//		System.out.println("无效时间：" + token2.getPlayload().getGra());
//		System.out.println("非法："+jwt.isIllegality(token2.getTokenStr() ,"1"));
//		System.out.println("过期：" + jwt.isTimeout(token2));
//		System.out.println("无效：" + jwt.isInvalid(token2));
//		jwt.refresh(token2);
//		System.out.println(token2);
//	}
}