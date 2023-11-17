package it.unifi.swe.cepms.server_module.dao;

import it.unifi.swe.cepms.server_module.domain_model.Endpoint;
import it.unifi.swe.cepms.server_module.domain_model.User;

import java.sql.*;
import java.util.HashMap;
import java.util.Optional;

public class EndpointDao implements Dao<Integer, Endpoint> {

    private static final String URL = "jdbc:postgresql://localhost:5432/server_module";
    private static final String USER = "jacopo";
    private static final String PASS = "rentus";
    private Connection con = null;
    private PreparedStatement pstmt = null;
    private Statement stmt = null;
    private ResultSet resultSet = null;

    @Override
    public Optional<Endpoint> get(String id) {
        con = getConnection();
        String getEndpoint = "select * from endpoint where id=?;";
        Optional<Endpoint> endpoint;

        try {
            pstmt = con.prepareStatement(getEndpoint);
            pstmt.setInt(1, Integer.parseInt(id));
            resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                endpoint = Optional.of(new Endpoint(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3)));
            } else {
                endpoint = Optional.empty();
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
        return endpoint;
    }

    @Override
    public HashMap<Integer, Endpoint> getAll() {
        con = getConnection();
        HashMap<Integer, Endpoint> hs = new HashMap<>();
        String getAll = "select * from endpoint;";

        try {
            stmt = con.createStatement();
            resultSet = stmt.executeQuery(getAll);
            while (resultSet.next()) {
                hs.put(resultSet.getInt(1), new Endpoint(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3)));
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
    public void save(Endpoint endpoint, String[] params) {
        con = getConnection();
        String saveEndpoint = "insert into endpoint values (?, ?, ?)";

        try {
            pstmt = con.prepareStatement(saveEndpoint);
            pstmt.setInt(1, endpoint.getId());
            pstmt.setString(2, endpoint.getName());
            pstmt.setString(3, endpoint.getOTK());
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
    public void update(Endpoint endpoint, String[] params) {
        //only updates name
        con = getConnection();
        String changeName = "update endpoint set name=? where id=?";

        try {

            PreparedStatement pstmt = con.prepareStatement(changeName);

            pstmt.setString(1, params[0]);
            pstmt.setInt(2, endpoint.getId());
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
    public void delete(Endpoint endpoint) {
        con = getConnection();
        String removeEndpoint = "delete from endpoint where id=?";

        try {
            pstmt = con.prepareStatement(removeEndpoint);
            pstmt.setInt(1, endpoint.getId());
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

