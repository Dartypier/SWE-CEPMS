package it.unifi.swe.cepms.client_module.dao;

import it.unifi.swe.cepms.client_module.domain_model.Endpoint;
import org.quartz.SchedulerException;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

public class EndpointDao implements Dao<Endpoint> {
    private String URL = "jdbc:postgresql://localhost:5432/db_endpoint_one";
    private String USER = "jacopo";
    private String PASS = "rentus";
    private Connection con = null;
    private PreparedStatement pstmt = null;
    private Statement stmt = null;
    private ResultSet resultSet = null;

    //ADDED: constructor defines login params
    public EndpointDao(String DBName, String DBUser, String DBPass){
        this.URL= DBName;
        this.USER = DBUser;
        this.PASS=DBPass;
    }

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
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
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
        throw new RuntimeException("not implemented");
    }

    @Override
    public void save(Endpoint endpoint, String[] params) {
        con = getConnection();
        String saveEndpoint = "insert into endpoint values (?, ?, ?);";

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
        con = getConnection();
        String updateName = "update endpoint set name = ?, otk = ? where id = ?";

        try {
            pstmt = con.prepareStatement(updateName);
            pstmt.setString(1, endpoint.getName());
            pstmt.setString(2, endpoint.getOTK());
            pstmt.setInt(3, endpoint.getId());
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

    //TODO: maybe to implement
    @Override
    public void delete(Endpoint endpoint) {
        throw new RuntimeException("not implemented");
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to DB");
        }
    }
}
