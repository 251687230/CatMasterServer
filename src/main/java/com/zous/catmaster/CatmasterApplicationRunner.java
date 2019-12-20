package com.zous.catmaster;

import com.zous.catmaster.config.JWTProperties;
import com.zous.catmaster.utils.TokenUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Locale;

@Component
public class CatmasterApplicationRunner implements ApplicationRunner {
    private final ApplicationContext context;
    private final JWTProperties jwtProperties;

    public CatmasterApplicationRunner( ApplicationContext context, JWTProperties jwtProperties) {
        this.context = context;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        LocaleContextHolder.setDefaultLocale(Locale.CHINA);

    }

    private void initializeApplicationArgument() {
        //初始化jwt工具
        TokenUtils.buildDefined(jwtProperties.getExpTime(), jwtProperties.getExpGraTime(), jwtProperties.getIssuser()
                , jwtProperties.getAudience(), jwtProperties.getHeadType(), jwtProperties.getHeadAlg());
    }
}
