import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class SqliteCacheStorage implements CacheStorage {
    private static final String TABLE_NAME = "CachedStorage";
    private String filename = null;
    private Connection connection = null;
    private Statement statement = null;


    public SqliteCacheStorage(String filename_) throws ClassNotFoundException, SQLException {
        this.filename = filename_;

        Class.forName("org.sqlite.JDBC");
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:"+this.filename);
            statement = connection.createStatement();
            String sql = "CREATE TABLE " + TABLE_NAME +
                    "(Key TEXT NOT NULL, " +
                    "Data TEXT NOT NULL, Primary key(Key, Data))";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            if(e.getErrorCode() != 0){
                throw(e);
            }
        }
    }

    @Override
    public boolean hasKey(String key)
    {
        String sql = "select Data from "+TABLE_NAME +" where Key = '"+key +"'";
        ResultSet rs = null;
        try {
            rs = statement.executeQuery(sql);

            if(rs.next()) {
                return true;
            }

        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    @Override
    public Set<String> getData(String key){

        Set<String> resultSet = new HashSet<>();
        String sql = "select Data from "+TABLE_NAME +" where Key = '"+key +"'";

        ResultSet rs = null;
        try {
            rs = statement.executeQuery(sql);

            while(rs.next())
            {
                resultSet.add(rs.getString("Data"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    @Override
    public void setData(String key, Set<String> data) {
        try {
            connection.setAutoCommit(false);
            for (String d : data) {
                String sql = "insert into " + TABLE_NAME + " values ('" + key + "','" + d + "')";
                statement.execute(sql);
            }
            connection.commit();
        } catch (SQLException e) {
            if(e.getErrorCode() != 19) { // not already inserted.
                e.printStackTrace();
            }

            try {
                connection.rollback();
            } catch (SQLException ignored) {}
        }
    }
}
