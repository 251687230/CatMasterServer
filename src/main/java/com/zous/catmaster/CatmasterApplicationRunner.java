package com.zous.catmaster;

import com.zous.catmaster.config.JWTProperties;
import com.zous.catmaster.entity.Role;
import com.zous.catmaster.mapper.RoleMapper;
import com.zous.catmaster.utils.TokenUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class CatmasterApplicationRunner implements ApplicationRunner {
    private final RoleMapper roleMapper;
    private final ApplicationContext context;
    private final JWTProperties jwtProperties;

    public CatmasterApplicationRunner(RoleMapper roleMapper, ApplicationContext context, JWTProperties jwtProperties) {
        this.roleMapper = roleMapper;
        this.context = context;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        LocaleContextHolder.setDefaultLocale(Locale.CHINA);

        roleMapper.deleteAll();

        List<Role> roles = new ArrayList<>();
        roles.add(new Role(Role.ROLE_MASTER,context.getMessage("role.roleType.master",null, LocaleContextHolder.getLocale())));
        roleMapper.saveAll(roles);
    }

    private void initializeApplicationArgument() {
        //初始化jwt工具
        TokenUtils.buildDefined(jwtProperties.getExpTime(), jwtProperties.getExpGraTime(), jwtProperties.getIssuser()
                , jwtProperties.getAudience(), jwtProperties.getHeadType(), jwtProperties.getHeadAlg());
    }
}
