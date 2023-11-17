package it.unifi.swe.cepms.server_module.dao;

import it.unifi.swe.cepms.server_module.domain_model.Endpoint;
import it.unifi.swe.cepms.server_module.domain_model.QuarantineFile;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class MapScanScheduleToEndpointDao implements MapperDao<Integer> {

    private static final String URL = "jdbc:postgresql://localhost:5432/server_module";
    private static final String USER = "jacopo";
    private static final String PASS = "rentus";
    private Connection con = null;
    private PreparedStatement pstmt = null;
    private Statement stmt = null;
    private ResultSet resultSet = null;


    @Override
    public ArrayList<Integer> getAll(Integer scanScheduleId) {
        con = getConnection();
        ArrayList<Integer> al = new ArrayList<>();
        String getAssociatedEndpointsId = "select endpoint_id from map_scan_schedule_to_endpoint where scan_schedule_id=?;";

        try {
            pstmt = con.prepareStatement(getAssociatedEndpointsId);
            pstmt.setInt(1, scanScheduleId);
            resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                //add associated endpoints IDs
                al.add(resultSet.getInt(1));
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
        return al;
    }

    @Override
    public void save(Integer scanScheduleId, Integer endpointId) {
        con = getConnection();
        String saveAssociatedEndpoint = "insert into map_scan_schedule_to_endpoint values (?, ?);";

        try {
            pstmt = con.prepareStatement(saveAssociatedEndpoint);
            pstmt.setInt(1, scanScheduleId);
            pstmt.setInt(2, endpointId);
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
    public void delete(Integer endpointId, Integer scanScheduleId) {
        con = getConnection();
        String deassociateEndpoint = "delete from map_scan_schedule_to_endpoint where endpoint_id=? and scan_schedule_id=?";

        try {
            pstmt = con.prepareStatement(deassociateEndpoint);
            pstmt.setInt(1, endpointId);
            pstmt.setInt(2, scanScheduleId);
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
