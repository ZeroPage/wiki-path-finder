package org.zeropage.cache;

import java.io.File;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class SqliteCacheStorage implements CacheStorage {
    private static final String TABLE_NAME = "CachedStorage";
    private File file = null;
    private Connection connection = null;

    public SqliteCacheStorage(File file_) throws ClassNotFoundException, SQLException {
        this.file = file_;

        Class.forName("org.sqlite.JDBC");
        try {
            if (this.file.getParent() != null) {
                File directory = new File(this.file.getParent());
                if (!directory.exists()) {
                    directory.mkdirs();
                }
            }

            connection = DriverManager.getConnection("jdbc:sqlite:" + this.file.getAbsolutePath());
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE " + TABLE_NAME +
                    "(Key TEXT NOT NULL, " +
                    "Data TEXT NOT NULL, Primary key(Key, Data))";
            statement.executeUpdate(sql);

        } catch (SQLException e) {
            if (e.getErrorCode() != 0) {
                throw (e);
            }
        }
    }


    public void finalize() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public synchronized boolean hasKey(String key) {
        String sql = "select Data from " + TABLE_NAME + " where Key = '" + key + "'";
        ResultSet rs = null;
        try {

            PreparedStatement selectStatement = connection.prepareStatement("select Data from " + TABLE_NAME + " where Key = ?");
            selectStatement.setString(1, key);
            rs = selectStatement.executeQuery();
            if (rs.next()) {
                return true;
            }

        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    @Override
    public synchronized Set<String> getData(String key) {

        Set<String> resultSet = new HashSet<>();
        String sql = "select Data from " + TABLE_NAME + " where Key = ?";
        ResultSet rs = null;

        try {
            PreparedStatement selectStatement = connection.prepareStatement(sql);
            selectStatement.setString(1, key);
            rs = selectStatement.executeQuery();

            while (rs.next()) {
                resultSet.add(rs.getString("Data"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    @Override
    public synchronized void setData(String key, Set<String> data) {
        if (key == null || data == null) return;

        try {
            connection.setAutoCommit(false);
            for (String d : data) {
                String query = "insert into " + TABLE_NAME + " values (?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, key);
                preparedStatement.setString(2, d);
                preparedStatement.execute();
            }
            connection.commit();
        } catch (SQLException e) {
            if (e.getErrorCode() != 19) { // not already inserted.
                e.printStackTrace();
            }

            try {
                connection.rollback();
            } catch (SQLException ignored) {
            }
        }
    }
}
