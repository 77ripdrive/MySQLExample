package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQLInit {

    private static String propsPath = ("src/main/resources/db.properties");
    private static final Logger LOG = Logger.getLogger(MySQLInit.class.getName());
    private static Properties props, dbProps;


    public static Connection getConnection()  {
        try {
            Class.forName(getValue("mysql.driver"));
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.INFO, ex.getMessage(), ex);
        }
        try {Connection connection=DriverManager.getConnection(getValue("mysql.url"),
                getValue("mysql.username"),
                getValue("mysql.password"));
            return connection;
        } catch (SQLException ex) {
            LOG.log(Level.INFO, ex.getMessage(), ex);
        }
        return null;
    }

    static {
        dbProps = getProps();
    }

    public static String getValue(String string) {
        return dbProps.getProperty(string);
    }

    private static Properties getProps()  {
        props = new Properties();
        try (FileInputStream fis = new FileInputStream((propsPath))) {
            props.load(fis);
        } catch (IOException ex) {
            LOG.log(Level.INFO, ex.getMessage(), ex);
        }
        return props;

    }
}
