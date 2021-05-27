package ru.otus.service;

import org.eclipse.jetty.security.AbstractLoginService;
import org.eclipse.jetty.security.RolePrincipal;
import org.eclipse.jetty.security.UserPrincipal;
import org.eclipse.jetty.util.security.Password;
import ru.otus.db.model.RoleDataSet;
import ru.otus.db.model.UserDataSet;
import ru.otus.db.service.DBServiceUser;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class LoginServiceImpl extends AbstractLoginService {

    private final DBServiceUser userService;

    public LoginServiceImpl(DBServiceUser userService) {
        this.userService = userService;
    }

    @Override
    protected List<RolePrincipal> loadRoleInfo(UserPrincipal user) {
        String username = user.getName();
        System.out.printf("LoginService#loadRoleInfo(%s)\n", username);
        Optional<UserDataSet> dbUser = userService.getByLogin(username);
        if (dbUser.isPresent()) {
            Set<RoleDataSet> roles = dbUser.get().getRoles();
            return roles.stream().map(r -> new RolePrincipal(r.getName())).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    protected UserPrincipal loadUserInfo(String username) {
        System.out.printf("LoginService#loadUserInfo(%s)\n", username);
        Optional<UserDataSet> dbUser = userService.getByLogin(username);
        return dbUser.map(u -> new UserPrincipal(u.getLogin(), new Password(u.getPassword()))).orElse(null);
    }
}
