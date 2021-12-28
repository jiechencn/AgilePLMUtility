package zigzagsoft.agile.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by jijichen on 7/4/2014.
 */
public class ConnectionFactory {
    private static ConnectionFactory instance = null;
    private static Connection connection = null;

    public static ConnectionFactory getInstance(String driver){
        if (instance != null)
            return instance;
        return instance = new ConnectionFactory(driver );
    }


    public ConnectionFactory(String driver) {
        try {
            Class factoryClass = Class.forName(driver);
            factoryClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection(String url, String user, String pwd) {
        try {
            if (connection != null && !connection.isClosed())
                return connection;
            Connection connection = DriverManager.getConnection(url, user, pwd);
            connection.setAutoCommit(false);
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static final void close(Connection conn, Statement ps, ResultSet rset) {
        try {
            if (rset != null) {
                rset.close();
            }
        } catch (Exception e) {
        }
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (Exception e) {
        }
        try {
            if (conn != null) {
                try {
                    conn.commit();
                } catch (Throwable t) {
                }
            }
            conn.close();
        } catch (Exception e) {
        }
    }
}




