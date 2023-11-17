package it.unifi.swe.cepms.server_module.dao;

import it.unifi.swe.cepms.server_module.domain_model.QuarantineFile;

import java.sql.*;
import java.util.HashMap;
import java.util.Optional;

public class QuarantineFileDao implements Dao<Integer, QuarantineFile> {

    private static final String URL = "jdbc:postgresql://localhost:5432/server_module";
    private static final String USER = "jacopo";
    private static final String PASS = "rentus";
    private Connection con = null;
    private PreparedStatement pstmt = null;
    private Statement stmt = null;
    private ResultSet resultSet = null;

    @Override
    public Optional<QuarantineFile> get(String id) {
        con = getConnection();
        String getQuarantineFile = "select * from quarantine_file where id=?;";
        Optional<QuarantineFile> quarantineFile;

        try {
            pstmt = con.prepareStatement(getQuarantineFile);
            pstmt.setInt(1, Integer.parseInt(id));
            resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                quarantineFile = Optional.of(new QuarantineFile(resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6)));
            } else {
                quarantineFile = Optional.empty();
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

        return quarantineFile;
    }

    @Override
    public HashMap<Integer, QuarantineFile> getAll() {
        con = getConnection();
        HashMap<Integer, QuarantineFile> hs = new HashMap<>();
        String getAll = "select * from quarantine_file;";

        try {
            stmt = con.createStatement();
            resultSet = stmt.executeQuery(getAll);
            while (resultSet.next()) {
                hs.put(resultSet.getInt(1), new QuarantineFile(resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6)));
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
    public void save(QuarantineFile quarantineFile, String[] params) {
        con = getConnection();
        String saveQuarantineFile = "insert into quarantine_file values (?, ?, ?, ?, ?, ?);";

        try {
            pstmt = con.prepareStatement(saveQuarantineFile);
            pstmt.setInt(1, quarantineFile.getId());
            pstmt.setInt(2, quarantineFile.getScanScheduleId());
            pstmt.setInt(3, quarantineFile.getEndpointId());
            pstmt.setString(4, quarantineFile.getName());
            pstmt.setString(5, quarantineFile.getDetails());
            pstmt.setString(6, quarantineFile.getDiscoveredDate());
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
    public void update(QuarantineFile quarantineFile, String[] params) {
        //this must not be implemented
        throw new RuntimeException("not implemented");
    }

    @Override
    public void delete(QuarantineFile quarantineFile) {
        con = getConnection();
        String removeQuarantineFile = "delete from quarantine_file where id=?";

        try {
            pstmt = con.prepareStatement(removeQuarantineFile);
            pstmt.setInt(1, quarantineFile.getId());
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
