package org.zeropage.cache;

import java.io.File;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 * <P> Implementation of SqliteCacheStorage. Key-Value pairs are stored in storage. </P>
 * <P> SqliteCacheStorage used by CacheLinkSource </P>
 *
 * @see CacheLinkSource
 */
public class SqliteCacheStorage implements CacheStorage {
    private static final String TABLE_NAME = "CachedStorage";
    private Connection connection = null;


    /**
     *
     * @param file file path stored db
     * @throws ClassNotFoundException if No driver of SQLite
     * @throws SQLException if SQL Query raise errors.
     */
    public SqliteCacheStorage(File file) throws ClassNotFoundException, SQLException {


        Class.forName("org.sqlite.JDBC");
        try {
            if (file.getParent() != null) {
                File directory = new File(file.getParent());
                if (!directory.exists()) {
                    directory.mkdirs();
                }
            }

            connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
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


    public void finalize() throws Throwable {
        super.finalize();
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Check whether storage has key
     * @param key signature of your data.
     * @return true if it has key. if not, return false.
     */
    @Override
    public synchronized boolean hasKey(String key) {
        String sql = "select Data from " + TABLE_NAME + " where Key = ?";
        ResultSet rs;
        try {

            PreparedStatement selectStatement = connection.prepareStatement(sql);
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

    /**
     * Get data of the key.
     * @param key signature of your data.
     * @return Set of strings for the key. if no data of key, return null or empty set.
     */
    @Override
    public synchronized Set<String> getData(String key) {

        Set<String> resultSet = new HashSet<>();
        String sql = "select Data from " + TABLE_NAME + " where Key = ?";
        ResultSet rs;

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

    /**
     * Set data of a key.
     * @param key signature of your data.
     * @param data Set of strings to be stored.
     */
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
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
