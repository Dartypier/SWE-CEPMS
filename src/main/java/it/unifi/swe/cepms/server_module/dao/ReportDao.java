package it.unifi.swe.cepms.server_module.dao;

import it.unifi.swe.cepms.server_module.domain_model.Report;

import java.sql.*;
import java.util.HashMap;
import java.util.Optional;

public class ReportDao implements Dao<Integer, Report> {
    private static final String URL = "jdbc:postgresql://localhost:5432/server_module";
    private static final String USER = "jacopo";
    private static final String PASS = "rentus";
    private Connection con = null;
    private PreparedStatement pstmt = null;
    private Statement stmt = null;
    private ResultSet resultSet = null;

    @Override
    public Optional<Report> get(String id) {
        con = getConnection();
        String getReport = "select * from report where id=?;";
        Optional<Report> report;

        try {
            pstmt = con.prepareStatement(getReport);
            pstmt.setInt(1, Integer.parseInt(id));
            resultSet = pstmt.executeQuery();


            if (resultSet.next()) {
                report = Optional.of(new Report(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3)));
            } else {
                report = Optional.empty();
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
        return report;
    }

    @Override
    public HashMap<Integer, Report> getAll() {
        con = getConnection();
        HashMap<Integer, Report> hs = new HashMap<>();
        String getAll = "select * from report;";

        try {
            stmt = con.createStatement();
            resultSet = stmt.executeQuery(getAll);
            while (resultSet.next()) {
                hs.put(resultSet.getInt(1), new Report(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3)));
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
    public void save(Report report, String[] params) {
        con = getConnection();
        String saveReport = "insert into report values (?, ?, ?)";

        try {
            pstmt = con.prepareStatement(saveReport);
            pstmt.setInt(1, report.getId());
            pstmt.setString(2, report.getName());
            pstmt.setInt(3, report.getScanScheduleId());
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
    public void update(Report report, String[] params) {
        //not implemented because if a scanSchedule associated goes deleted
        //the DB set the scan_schedule_id to NULL
        throw new RuntimeException("not implemented");
    }

    @Override
    public void delete(Report report) {
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
