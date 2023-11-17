package it.unifi.swe.cepms.server_module.dao;

import it.unifi.swe.cepms.server_module.domain_model.User;
import it.unifi.swe.cepms.server_module.helpers.HashedPassword;

import java.sql.*;
import java.util.HashMap;
import java.util.Optional;

public class UserDao implements Dao<String, User> {

    private static final String URL = "jdbc:postgresql://localhost:5432/server_module";
    private static final String USER = "jacopo";
    private static final String PASS = "rentus";
    private Connection con = null;
    private PreparedStatement pstmt = null;
    private Statement stmt = null;
    private ResultSet resultSet = null;

    @Override
    public Optional<User> get(String email) {
        con = getConnection();
        String getUser = "select * from users where email=?;";
        Optional<User> user;

        try {
            pstmt = con.prepareStatement(getUser);
            pstmt.setString(1, email);
            resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                user = Optional.of(new User(resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        ""));
                user.get().setEncodedPassword(resultSet.getString(4));
            } else {
                user = Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (pstmt != null)
                    pstmt.close();
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return user;
    }

    @Override
    public HashMap<String, User> getAll() {
        con = getConnection();
        HashMap<String, User> hs = new HashMap<>();
        String getAll = "select * from users;";

        try {
            stmt = con.createStatement();
            resultSet = stmt.executeQuery(getAll);
            while (resultSet.next()) {
                hs.put(resultSet.getString(1), new User(resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4)));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (pstmt != null)
                    pstmt.close();
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return hs;
    }

    @Override
    public void save(User user, String[] password) {
        con = getConnection();
        String saveUser = "insert into users values (?, ?, ?, ?)";

        try {
            pstmt = con.prepareStatement(saveUser);
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getSurname());
            pstmt.setString(4, user.getEncodedPassword());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (pstmt != null)
                    pstmt.close();
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(User user, String[] params) {
        con = getConnection();
        String updateUser = """
                update users
                set email = ?,
                name = ?,
                surname = ?,
                encoded_password = ?
                where email = ?""";

        try {

            PreparedStatement pstmt = con.prepareStatement(updateUser);

            pstmt.setString(1, params[0]!=null ? params[0] : user.getEmail());
            pstmt.setString(2, params[1]!=null ? params[1] : user.getName());
            pstmt.setString(3, params[2]!=null ? params[2] : user.getSurname());
            pstmt.setString(4, params[3]!=null ? params[3] : user.getEncodedPassword());
            pstmt.setString(5, user.getEmail());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (pstmt != null)
                    pstmt.close();
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void delete(User user) {
        con = getConnection();
        String removeUser = "delete from users where email=?";

        try {
            pstmt = con.prepareStatement(removeUser);
            pstmt.setString(1, user.getEmail());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (pstmt != null)
                    pstmt.close();
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to DB");
        }
    }


}