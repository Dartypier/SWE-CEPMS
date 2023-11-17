package it.unifi.swe.cepms.server_module.business_logic;

import it.unifi.swe.cepms.server_module.dao.UserDao;
import it.unifi.swe.cepms.server_module.domain_model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

public class UserAuthUniTest {

    private static final String URL = "jdbc:postgresql://localhost:5432/server_module";
    private static final String USER = "jacopo";
    private static final String PASS = "rentus";

    @BeforeAll
    static void createUser() {
        //clearing table
        String deleteAllUsers = "delete from users;";
        Connection con = getConnection();
        Statement stmt = null;

        try {
            stmt = con.createStatement();
            stmt.execute(deleteAllUsers);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //creating user
        UserDao userDao = new UserDao();
        User userMock = mock(User.class);
        when(userMock.getEmail()).thenReturn("admin@example.com");
        when(userMock.getName()).thenReturn("Roberto");
        when(userMock.getSurname()).thenReturn("Belli");
        //encoded password for 'pass'
        when(userMock.getEncodedPassword()).thenReturn("62f264d7ad826f02a8af714c0a54b197935b717656b80461686d450f7b3abde4c553541515de2052b9af70f710f0cd8a1a2d3f4d60aa72608d71a63a9a93c0f5");
        userDao.save(userMock, null);
    }

    private static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to DB");
        }
    }

    @Test
    @DisplayName("Login should succeed")
    void testLogin() throws Exception {
        UserAuth userAuth = UserAuth.login("admin@example.com", "pass");
        assertDoesNotThrow(() -> UserAuth.login("admin@example.com", "pass"));
        assertTrue(userAuth.isAuthenticated());
    }

    @Test
    @DisplayName("Logout")
    void testLogout() throws Exception {
        UserAuth userAuth = UserAuth.login("admin@example.com", "pass");
        userAuth.logout();
        assertFalse(userAuth.isAuthenticated());
    }

    @Test
    @DisplayName("Login should not succeed")
    void testLoginWrongCredentials() {
        //not existing email
        assertThrows(Exception.class, () -> UserAuth.login("test@example.com", "pass"));
        //not existing password
        assertThrows(Exception.class, () -> UserAuth.login("admin@example.com", "notapassword"));
        //not existing email and password
        assertThrows(Exception.class, () -> UserAuth.login("test@example.com", "notapassword"));
    }
}
