package com.zous.catmaster.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="jwt")
@PropertySource(value = { "classpath:jwt.properties" }, encoding = "utf8")
public class JWTProperties {
    private String audience;
    private String issuser;
    private Long expGraTime;
    private Long expTime;
    private String headAlg;
    private String headType;

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public String getIssuser() {
        return issuser;
    }

    public void setIssuser(String issuser) {
        this.issuser = issuser;
    }

    public Long getExpGraTime() {
        return expGraTime;
    }

    public void setExpGraTime(Long expGraTime) {
        this.expGraTime = expGraTime;
    }

    public Long getExpTime() {
        return expTime;
    }

    public void setExpTime(Long expTime) {
        this.expTime = expTime;
    }

    public String getHeadAlg() {
        return headAlg;
    }

    public void setHeadAlg(String headAlg) {
        this.headAlg = headAlg;
    }

    public String getHeadType() {
        return headType;
    }

    public void setHeadType(String headType) {
        this.headType = headType;
    }
}
