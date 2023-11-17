package it.unifi.swe.cepms.server_module.business_logic;

import it.unifi.swe.cepms.server_module.dao.UserDao;
import it.unifi.swe.cepms.server_module.domain_model.User;
import it.unifi.swe.cepms.server_module.helpers.HashedPassword;

import java.util.HashMap;

class UsersPageController {
    HashMap<String, User> users;
    UserDao userDao;

    public UsersPageController() {
        userDao = new UserDao();
        users = userDao.getAll();
    }

    public HashMap<String, User> getAllUsers() {
        return users;
    }

    public void newUser(String email, String name, String surname, String password) {
        User user = new User(email, name, surname, password);
        users.put(email, user);
        userDao.save(user, null);
    }

    public void removeUser(String email) {
        userDao.delete(users.get(email));
        users.remove(email);
    }

    public void editUser(String emailId, String newEmail, String name, String surname, String password) {
        User user = users.get(emailId);
        if(password != null){
            password = HashedPassword.createHash(password);
        }
        userDao.update(user, new String[]{newEmail, name, surname, password});
        if (newEmail != null) {
            if (users.get(newEmail) != null)
                throw new RuntimeException("email already exists");
            else {
                user.setEmail(newEmail);
            }
        }
        if (name != null) {
            user.setName(name);
        }
        if (surname != null) {
            user.setSurname(surname);
        }
        if (password != null) {
            user.setEncodedPassword(HashedPassword.createHash(password));
        }
    }
}
