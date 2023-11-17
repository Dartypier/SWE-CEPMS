package it.unifi.swe.cepms.server_module.business_logic;

import it.unifi.swe.cepms.server_module.dao.Dao;
import it.unifi.swe.cepms.server_module.dao.UserDao;
import it.unifi.swe.cepms.server_module.domain_model.User;
import it.unifi.swe.cepms.server_module.helpers.HashedPassword;

import java.util.Optional;

public class UserAuth {

    private User user;

    private UserAuth(String email, String password) throws Exception {

        Dao<String, User> userDao = new UserDao();
        Optional<User> user1 = userDao.get(email);
        if (user1.isPresent() && HashedPassword.createHash(password).equals(user1.get().getEncodedPassword()))
            user = user1.get();

        else
            throw new Exception("Not Authenticated");

    }

    public static UserAuth login(String email, String password) throws Exception {
        return new UserAuth(email, password);
    }

    public void logout() {
        user = null;
    }

    boolean isAuthenticated() {
        return user != null;
    }
}