package com.teaspo.services.utils;

import com.teaspo.pojo.enams.RolesEnum;
import com.teaspo.exceptions.AuthRequiredException;
import com.teaspo.exceptions.ForbiddenException;
import com.teaspo.persistence.dao.UsersRepository;
import com.teaspo.persistence.entities.PermissionEntity;
import com.teaspo.persistence.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrii on 15.11.2016.
 */
@Service
public class SessionUtils {

    @Autowired
    private UsersRepository usersRepository;

    public UserEntity getCurrentUser() {
        if (isAuthorized()) {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return usersRepository.findByEmail(userDetails.getUsername());
        } else
            return null;
    }
    public boolean isAuthorized() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken))
            return true;
        else
            return false;
    }

    public void authorized() throws AuthRequiredException {
        if(!isAuthorized()){
            throw new AuthRequiredException();
        }
    }

    public boolean isUserWithRole(RolesEnum... userRoles){
        if(isAuthorized()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            for (RolesEnum rolesEnum : userRoles) {
                if (authentication.getAuthorities().contains(ROLES_MAP.get(rolesEnum.name()))) {
                    return true; // user has this role, so it's not forbidden
                }
            }
        }
        return false;
    }

    public void userHasRole(RolesEnum ... userRoles) throws AuthRequiredException, ForbiddenException {
        authorized();
        if (!isUserWithRole(userRoles))
            throw new ForbiddenException();
    }

    static final Map<String , SimpleGrantedAuthority> ROLES_MAP = new HashMap<String , SimpleGrantedAuthority>() {{
        put("admin",    new SimpleGrantedAuthority("ROLE_ADMIN"));
        put("moderator", new SimpleGrantedAuthority("ROLE_MODERATOR"));
        put("user",   new SimpleGrantedAuthority("ROLE_USER"));
    }};

    public void logeInUser(UserEntity entity) {
        UserEntity user = usersRepository.findByEmail(entity.getEmail());
        boolean enabled = true;
        if (!user.isActive())
            enabled = false;
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                enabled, true, true, true, getGrantedAuthorities(user));
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }


    private List<GrantedAuthority> getGrantedAuthorities(UserEntity user){
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (PermissionEntity perm:user.getRoleEntity().getPermissions()){
            authorities.add(new SimpleGrantedAuthority(perm.getName()));
        }
        return authorities;
    }
}
