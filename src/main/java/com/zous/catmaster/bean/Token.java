package com.zous.catmaster.bean;


import java.io.Serializable;

/**
 * jwt 实体对象
 *
 */
public class Token implements Serializable{
    private static final long serialVersionUID = -5391652691006115018L;

    /** 认证头 **/
    private Head head;
    /** 认证信息有效载荷 **/
    private Playload playload;

    /** 第一部分:base64head头 **/
    private String base64Head;
    /** 第二部分:base64playload荷载 **/
    private String base64PlayLoad;
    /** 第三部分:签证信息 **/
    private String signature;
    /** 最终token字符串 **/
    private String tokenStr;

    /**
     * Token头
     *
     *
     */
    public static class Head implements Serializable{
        private static final long serialVersionUID = -6516084948347601103L;

        /** token类型 **/
        private String typ = "JWT";
        /** token算法 默认:HMAC SHA256**/
        private String alg = "HS256";

        public String getTyp() {
            return typ;
        }
        public void setTyp(String typ) {
            this.typ = typ;
        }
        public String getAlg() {
            return alg;
        }
        public void setAlg(String alg) {
            this.alg = alg;
        }
    }

    /**
     * Token有效载荷
     *
     *
     */
    public static class Playload implements Serializable {
        private static final long serialVersionUID = 3981890375700111920L;

        /** 该token签发者 **/
        private String iss;
        /** 该token的所有人,可以存放用户名 **/
        private String sub;
        /** 接收token的一方 **/
        private String aud;
        /** token的过期时间(时间戳),必须要大于签发时间;大于等于该时间需要刷新token **/
        private long exp;
        /** token生效的开始时间(时间戳),意味着在这个时间之前验证token是会失败的,默认生成token后立即生效 **/
        private long nbf;
        /** token的签发时间 时间戳**/
        private long iat;
        /** token的唯一身份标识,主要用来作为一次性token,从而回避重放攻击 **/
        private String jti;
        /** token验证宽限时间(时间戳) 超过宽限时间需要重新登录,
         * 即该token的真正存活时间,宽限时间的加入是为了解决并发token刷新后新token失效问题
         * **/
        private long gra;
        /** token类型:  后台登录用户,互联网用户,第三方机构 **/
        private String typ;

        public String getIss() {
            return iss;
        }
        public void setIss(String iss) {
            this.iss = iss;
        }
        public String getSub() {
            return sub;
        }
        public void setSub(String sub) {
            this.sub = sub;
        }
        public String getAud() {
            return aud;
        }
        public void setAud(String aud) {
            this.aud = aud;
        }
        public long getExp() {
            return exp;
        }
        public void setExp(long exp) {
            this.exp = exp;
        }
        public long getNbf() {
            return nbf;
        }
        public void setNbf(long nbf) {
            this.nbf = nbf;
        }
        public long getIat() {
            return iat;
        }
        public void setIat(long iat) {
            this.iat = iat;
        }
        public String getJti() {
            return jti;
        }
        public void setJti(String jti) {
            this.jti = jti;
        }
        public long getGra() {
            return gra;
        }
        public void setGra(long gra) {
            this.gra = gra;
        }
        public String getTyp() {
            return typ;
        }
        public void setTyp(String typ) {
            this.typ = typ;
        }


    }

    public Head getHead() {
        return head;
    }
    public void setHead(Head head) {
        this.head = head;
    }
    public Playload getPlayload() {
        return playload;
    }
    public void setPlayload(Playload playload) {
        this.playload = playload;
    }
    public String getSignature() {
        return signature;
    }
    public void setSignature(String signature) {
        this.signature = signature;
    }
    public String getBase64Head() {
        return base64Head;
    }
    public void setBase64Head(String base64Head) {
        this.base64Head = base64Head;
    }
    public String getBase64PlayLoad() {
        return base64PlayLoad;
    }
    public void setBase64PlayLoad(String base64PlayLoad) {
        this.base64PlayLoad = base64PlayLoad;
    }
    public String getTokenStr() {
        return tokenStr;
    }
    public void setTokenStr(String tokenStr) {
        this.tokenStr = tokenStr;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Token [head=" + head + ", playload=" + playload + ", base64Head=" + base64Head + ", base64PlayLoad="
                + base64PlayLoad + ", signature=" + signature + ", tokenStr=" + tokenStr + "]";
    }

}