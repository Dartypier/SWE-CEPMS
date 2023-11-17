package it.unifi.swe.cepms.server_module.dao;

import java.sql.*;
import java.util.ArrayList;

public class MapReportToQuarantineFilesDao implements MapperDao<Integer>{
    private static final String URL = "jdbc:postgresql://localhost:5432/server_module";
    private static final String USER = "jacopo";
    private static final String PASS = "rentus";
    private Connection con = null;
    private PreparedStatement pstmt = null;
    private Statement stmt = null;
    private ResultSet resultSet = null;

    @Override
    public ArrayList<Integer> getAll(Integer ReportId) {
        con = getConnection();
        ArrayList<Integer> al = new ArrayList<>();
        String getAssociatedQuarantineFilesId = "select * from map_report_to_quarantine_files where report_id=?;";

        try {
            pstmt = con.prepareStatement(getAssociatedQuarantineFilesId);
            pstmt.setInt(1, ReportId);
            resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                //add associated endpoints IDs
                al.add(resultSet.getInt(2));
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
    public void save(Integer reportId, Integer quarantineFilesId) {
        con = getConnection();
        String saveAssociatedQuarantineFiles = "insert into map_report_to_quarantine_files values (?, ?);";

        try {
            pstmt = con.prepareStatement(saveAssociatedQuarantineFiles);
            pstmt.setInt(1, reportId);
            pstmt.setInt(2, quarantineFilesId);
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
    public void delete(Integer integer, Integer integer1) {
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
