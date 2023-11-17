package it.unifi.swe.cepms.client_module.dao;

import it.unifi.swe.cepms.ScanType;
import it.unifi.swe.cepms.client_module.domain_model.ScanSchedule;
import org.quartz.JobKey;
import org.quartz.TriggerKey;

import java.sql.*;
import java.util.HashMap;
import java.util.Optional;

public class ScanScheduleDao implements Dao<ScanSchedule> {

    private String URL = "jdbc:postgresql://localhost:5432/db_endpoint_one";
    private String USER = "jacopo";
    private String PASS = "rentus";
    private Connection con = null;
    private PreparedStatement pstmt = null;
    private Statement stmt = null;
    private ResultSet resultSet = null;

    //ADDED
    public ScanScheduleDao(String DBName, String DBUser, String DBPass){
        this.URL = DBName;
        this.USER = DBUser;
        this.PASS = DBPass;
    }

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
                //create ScanSchedule Object
                scanSchedule = Optional.of(new ScanSchedule(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        ScanType.valueOf(resultSet.getString(3))));
                //assign the ScanSchedule the jobKey and triggerKey
                scanSchedule.get().setJobKey(new JobKey(resultSet.getString(4)));
                scanSchedule.get().setTriggerKey(new TriggerKey(resultSet.getString(5)));
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
    public HashMap<Integer, ScanSchedule> getAll() {
        con = getConnection();
        HashMap<Integer, ScanSchedule> hs = new HashMap<>();
        String getAll = "select * from scan_schedule;";

        try {
            stmt = con.createStatement();
            resultSet = stmt.executeQuery(getAll);
            while (resultSet.next()) {
                ScanSchedule scanSchedule = new ScanSchedule(resultSet.getInt(1),
                        resultSet.getString(2),
                        ScanType.valueOf(resultSet.getString(3)));
                scanSchedule.setJobKey(new JobKey(resultSet.getString(4)));
                scanSchedule.setTriggerKey(new TriggerKey(resultSet.getString(5)));
                hs.put(resultSet.getInt(1), scanSchedule);
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
        String saveScanSchedule = "insert into scan_schedule values (?, ?, ?, ?, ?);";

        try {
            pstmt = con.prepareStatement(saveScanSchedule);
            pstmt.setInt(1, scanSchedule.getId());
            pstmt.setString(2, scanSchedule.getCron());
            pstmt.setString(3, scanSchedule.getScanType().toString());
            pstmt.setString(4, "job_"+scanSchedule.getId());
            pstmt.setString(5, "trigger_"+scanSchedule.getId());
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
    public void update(ScanSchedule scanSchedule, String[] params) {
        con = getConnection();
        String updateScanSchedule = "update scan_schedule set cron=?, scan_type = ? where id=?";
        try {
            pstmt = con.prepareStatement(updateScanSchedule);
            pstmt.setString(1, scanSchedule.getCron());
            pstmt.setString(2, scanSchedule.getScanType().toString());
            pstmt.setInt(3, scanSchedule.getId());
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
        String deleteScanSchedule = "delete from scan_schedule values where id=?";

        try {
            pstmt = con.prepareStatement(deleteScanSchedule);
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
