package it.unifi.swe.cepms.server_module.dao;

import it.unifi.swe.cepms.server_module.domain_model.ScanSchedule;
import it.unifi.swe.cepms.ScanType;

import java.sql.*;
import java.util.Optional;
import java.util.TreeMap;

public class ScanScheduleDao implements Dao<Integer, ScanSchedule>{
    private static final String URL = "jdbc:postgresql://localhost:5432/server_module";
    private static final String USER = "jacopo";
    private static final String PASS = "rentus";
    private Connection con = null;
    private PreparedStatement pstmt = null;
    private Statement stmt = null;
    private ResultSet resultSet = null;

    @Override
    public Optional<ScanSchedule> get(String id) {
        con = getConnection();
        String getScanSchedule = "select * from scan_schedule where id=?;";
        Optional<ScanSchedule> scanSchedule;

        try {
            pstmt = con.prepareStatement(getScanSchedule);
            pstmt.setInt(1, Integer.parseInt(id));
            resultSet = pstmt.executeQuery();


            if (resultSet.next()) {
                scanSchedule = Optional.of(new ScanSchedule(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        ScanType.valueOf(resultSet.getString(4)),
                        resultSet.getBoolean(5)));
            } else {
                scanSchedule = Optional.empty();
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
        return scanSchedule;
    }

    @Override
    public TreeMap<Integer, ScanSchedule> getAll() {
        con = getConnection();
        TreeMap<Integer, ScanSchedule> hs = new TreeMap<>();
        String getAll = "select * from scan_schedule;";

        try {
            stmt = con.createStatement();
            resultSet = stmt.executeQuery(getAll);
            while (resultSet.next()) {
                hs.put(resultSet.getInt(1), new ScanSchedule(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        ScanType.valueOf(resultSet.getString(4)),
                        resultSet.getBoolean(5)));
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
    public void save(ScanSchedule scanSchedule, String[] params) {
        con = getConnection();
        String saveScanSchedule = "insert into scan_schedule values (?, ?, ?, ?, ?)";

        try {
            pstmt = con.prepareStatement(saveScanSchedule);
            pstmt.setInt(1, scanSchedule.getId());
            pstmt.setString(2, scanSchedule.getName());
            pstmt.setString(3, scanSchedule.getCron());
            pstmt.setString(4, scanSchedule.getScanType().toString());
            pstmt.setBoolean(5, scanSchedule.isEnabled());
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

    //can update: name, cron, ScanRecurrence, ScanType, enabled
    @Override
    public void update(ScanSchedule scanSchedule, String[] params) {
        con = getConnection();
        String updateScanSchedule = """
                update scan_schedule
                set name=?, cron=?, scan_type = ?, enabled=? 
                where id=?;
                """;

        try {

            pstmt = con.prepareStatement(updateScanSchedule);
            pstmt.setString(1, params[0]!=null ? params[0] : scanSchedule.getName());
            pstmt.setString(2, params[1]!=null ? params[1] : scanSchedule.getCron());
            pstmt.setString(3, params[2]!=null ? params[2] : scanSchedule.getScanType().toString());
            pstmt.setBoolean(4, Boolean.valueOf(params[3]));
            pstmt.setInt(5, scanSchedule.getId());
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
    public void delete(ScanSchedule scanSchedule) {
        con = getConnection();
        String removeScanSchedule = "delete from scan_schedule where id=?";

        try {
            pstmt = con.prepareStatement(removeScanSchedule);
            pstmt.setInt(1, scanSchedule.getId());
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
