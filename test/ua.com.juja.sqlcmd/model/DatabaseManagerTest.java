package ua.com.juja.sqlcmd.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Александр on 14.05.17.
 */
public abstract class DatabaseManagerTest {

    private DatabaseManager manager;

    public abstract DatabaseManager getDatabaseManager();

    @Before
    public void setup() {
        manager = getDatabaseManager();
        manager.connect("sqlcmd", "postgres", "postgres");
    }


    @Test
    public void testGetAllTableNames() {
        Set<String> tableNames = manager.getTableNames();
        assertEquals("[user]", tableNames.toString());
    }

    @Test
    public void testGetTableData() {
        manager.clear("user");
        DataSet input = new DataSetImpl();
        input.put("name", "Stiven");
        input.put("password", "pass");
        input.put("id", "111");
        manager.create("user", input);
        List<DataSet> users = manager.getTableData("user");
        assertEquals(1, users.size());

        DataSet user = users.get(0);
        assertEquals("[name, password, id]",user.getNames().toString());
        assertEquals("[Stiven, pass, 111]",user.getValues().toString());
    }

    @Test
    public void testUpdateTableData() {
        manager.clear("user");
        DataSet input = new DataSetImpl();
        input.put("name", "Stiven");
        input.put("password", "pass");
        input.put("id", 111);
        manager.create("user", input);

        DataSet newvalue = new DataSetImpl();
        newvalue.put("password", "pass2");
        manager.update("user", 111, newvalue);

        List<DataSet> users = manager.getTableData("user");
        assertEquals(1, users.size());

        DataSet user = users.get(0);
        assertEquals("[name, password, id]",user.getNames().toString());
        assertEquals("[Stiven, pass2, 111]",user.getValues().toString());
    }

    @Test
    public void testGetColumnNames() {
        manager.clear("user");
        Set<String> columnNames = manager.getTableColumns("user");

        assertEquals("[name, password, id]",columnNames.toString());
    }

    @Test
    public void testIsConnected() {
        assertTrue(manager.isConnected());
    }

}
