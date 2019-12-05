package com.zous.catmaster;

import com.zous.catmaster.entity.Role;
import com.zous.catmaster.mapper.RoleMapper;
import com.zous.catmaster.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    final
    RoleMapper roleMapper;
    final
    ApplicationContext context;

    public CatmasterApplicationRunner(RoleMapper roleMapper, ApplicationContext context) {
        this.roleMapper = roleMapper;
        this.context = context;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        LocaleContextHolder.setDefaultLocale(Locale.CHINA);

        roleMapper.deleteAll();

        List<Role> roles = new ArrayList<>();
        roles.add(new Role(Role.ROLE_MASTER,context.getMessage("role.roleType.master",null, LocaleContextHolder.getLocale())));
        roleMapper.saveAll(roles);
    }
}
