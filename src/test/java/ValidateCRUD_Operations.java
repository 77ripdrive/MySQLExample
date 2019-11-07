import entity.Worker;
import org.junit.jupiter.api.*;
import utils.ActionMySQL;
import utils.MySQLInit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ValidateCRUD_Operations {

    private static final Logger LGR = Logger.getLogger(ValidateCRUD_Operations.class.getName());
    private final Worker expectWorker1 = new Worker(1, "Ivan", "Dev", 1000);
    private final Worker updateWorker = new Worker(1, "Sasha", "QA", 1100);
    private static String sqlDbName = MySQLInit.getValue("mysql.db");
    private static String sqlCreateTable = MySQLInit.getValue("mysql.create.table");
    private static String sqlUseDataBase = MySQLInit.getValue("mysql.use");
    private static String sqlInsertFirstWorker = MySQLInit.getValue("mysql.first.worker");
    private static String sqlInsertSecondWorker = MySQLInit.getValue("mysql.second.worker");
    private static Connection dbConnect;
    private Worker worker;

    @BeforeAll
    public static void createMySqlTable() throws SQLException {
        dbConnect = MySQLInit.getConnection();
        Statement stmt = dbConnect.createStatement();
        ActionMySQL.createDB(stmt, dbConnect, sqlDbName);
        ActionMySQL.createTable(stmt, sqlUseDataBase, sqlCreateTable);
        ActionMySQL.insertInToTable(stmt, sqlInsertFirstWorker);
        ActionMySQL.insertInToTable(stmt, sqlInsertSecondWorker);
    }

    @Test
    @Order(1)
    public void whenReadFirstLineInTable_ThenEntityCorrect() throws SQLException {

        try (PreparedStatement pstmt = dbConnect.prepareStatement("select * from workers where Id < 2 ");
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                worker = ActionMySQL.createWorker(rs);
            }
        }
        Assertions.assertEquals(expectWorker1, worker);
    }

    @Test
    @Order(2)
    public void whenInsertNewEntity_ThenNumberRowTableIsCorrect() throws SQLException {
        int rowCount = 0;
        try (PreparedStatement pstmt = dbConnect
                .prepareStatement("select * from workers ", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = pstmt.executeQuery()) {
            rs.moveToInsertRow();
            rs.updateString("firstName", "Sasha");
            rs.updateString("position", "QA");
            rs.updateDouble("salary", 900);
            rs.insertRow();
            rs.moveToCurrentRow();
            rs.last();
            rowCount = rs.getRow();
        }
        Assertions.assertEquals(3, rowCount);
    }

    @Test
    @Order(3)
    public void whenUpdateEntityField_ThenChangeIsCorrect() throws SQLException {
        Worker worker = null;
        dbConnect.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
        try (Statement pstmt = dbConnect
                .createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, ResultSet.HOLD_CURSORS_OVER_COMMIT)) {
            dbConnect.setAutoCommit(false);
            ResultSet rs = pstmt.executeQuery("select * from workers");
            while (rs.next()) {
                if (rs.getString("firstName")
                        .equalsIgnoreCase("Sasha")) {
                    rs.updateInt("salary", 1100);
                    rs.updateRow();
                    dbConnect.commit();
                    worker = ActionMySQL.createWorker(rs);
                }
            }
            rs.last();
        }
        Assertions.assertEquals(1100, updateWorker.getSalary());
    }

    @Test
    @Order(4)
    public void whenDeleteRow_ThenNumberRowIsCorrect() throws SQLException {
        int numOfRows = 0;
        try (PreparedStatement pstmt = dbConnect
                .prepareStatement("select * from workers", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = pstmt.executeQuery()) {
            rs.absolute(1);
            rs.deleteRow();
        }
        try (PreparedStatement pstmt = dbConnect
                .prepareStatement("select * from workers", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = pstmt.executeQuery()) {
            rs.last();
            numOfRows = rs.getRow();
        }
        Assertions.assertEquals(2, numOfRows);
    }

    @Test
    @Order(5)
    public void whenCreateListOfTableEntity_ThenResultCorrect() {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Worker> listOfWorkers = new ArrayList<Worker>();
        try {
            pstmt = dbConnect
                    .prepareStatement("select * from workers", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pstmt.setFetchSize(1);
            rs = pstmt.executeQuery();
            rs.setFetchSize(1);
            while (rs.next()) {
                worker = ActionMySQL.createWorker(rs);
                listOfWorkers.add(worker);
            }
        } catch (SQLException ex) {
            LGR.log(Level.INFO, ex.getMessage(), ex);
            ;
        }
        Assertions.assertEquals(2, listOfWorkers.size());
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        String sqlDeleteDB = format("DROP DATABASE %s", sqlDbName);
        PreparedStatement deleteStmt = dbConnect
                .prepareStatement(sqlDeleteDB, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        deleteStmt.execute();
        dbConnect.close();
    }

}