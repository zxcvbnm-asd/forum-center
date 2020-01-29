package cn.hegongda.controller;

import cn.hegongda.pojo.TAdmin;
import cn.hegongda.pojo.TPermission;
import cn.hegongda.pojo.TRole;
import cn.hegongda.service.admin.AdminUserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *  springSecurity 安全框架登陆校验
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private AdminUserService adminUserService ;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TAdmin admin = adminUserService.findAdminByUsername(username);
        if (admin == null) {
            // 没有进行登陆
            return null ;
        }
        UserDetails user = new User(username,"{noop}"+admin.getPassword(),getSimpleGrantedAuthorityList(admin));

        return user;
    }

    // 添加权限
    private List<SimpleGrantedAuthority> getSimpleGrantedAuthorityList(TAdmin admin) {
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        List<TRole> roles = admin.getRoles();
        for (TRole role : roles) {
            list.add(new SimpleGrantedAuthority(role.getKeyword()));
            List<TPermission> permissions = role.getPermissions();
            for (TPermission permission : permissions) {
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        return list;
    }
}
