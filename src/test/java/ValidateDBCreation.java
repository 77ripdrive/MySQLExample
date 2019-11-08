import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import utils.ActionMySQL;
import utils.MySQLInit;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@RunWith(JUnitPlatform.class)
public class ValidateDBCreation {
    private static final Logger LGR = Logger.getLogger(ValidateDBCreation.class.getName());
    private String dbName = MySQLInit.getValue("mysql.db");

    @Test
    public void checkDBCreation() {

        try (Connection con = MySQLInit.getConnection()) {
            try (Statement stmt = Objects.requireNonNull(con).createStatement()) {
                Assertions.assertTrue(ActionMySQL.createDB(stmt, dbName));
                Assertions.assertTrue(ActionMySQL.deleteDB(stmt, dbName));
            }
        } catch (SQLException ex) {
            LGR.log(Level.INFO, ex.getMessage(), ex);
        }
    }

}


