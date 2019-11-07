import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.ActionMySQL;
import utils.MySQLInit;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ValidateDBCreation {
    private static final Logger LGR = Logger.getLogger(ValidateDBCreation.class.getName());
    private String dbName = MySQLInit.getValue("mysql.db");

    @Test
    public void checkDBCreation() {

        try (Connection con = MySQLInit.getConnection()) {
            try (Statement stmt = con.createStatement()) {
                Assertions.assertTrue(ActionMySQL.createDB(stmt, con, dbName));
                Assertions.assertTrue(ActionMySQL.deleteDB(stmt, dbName));
            }
        } catch (SQLException ex) {
            LGR.log(Level.INFO, ex.getMessage(), ex);
        }
    }

}


