package ua.com.juja.sqlcmd.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

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
        String[] tableNames = manager.getTableNames();
        assertEquals("[user]", Arrays.toString(tableNames));
    }

    @Test
    public void testGetTableData() {
        manager.clear("user");
        DataSet input = new DataSet();
        input.put("name", "Stiven");
        input.put("password", "pass");
        input.put("id", "111");
        manager.create("user", input);
        DataSet[] users = manager.getTableData("user");
        assertEquals(1, users.length);

        DataSet user = users[0];
        assertEquals("[name, password, id]",Arrays.toString(user.getNames()));
        assertEquals("[Stiven, pass, 111]",Arrays.toString(user.getValues()));
    }

    @Test
    public void testUpdateTableData() {
        manager.clear("user");
        DataSet input = new DataSet();
        input.put("name", "Stiven");
        input.put("password", "pass");
        input.put("id", 111);
        manager.create("user", input);

        DataSet newvalue = new DataSet();
        newvalue.put("password", "pass2");
        manager.update("user", 111, newvalue);

        DataSet[] users = manager.getTableData("user");
        assertEquals(1, users.length);

        DataSet user = users[0];
        assertEquals("[name, password, id]",Arrays.toString(user.getNames()));
        assertEquals("[Stiven, pass2, 111]",Arrays.toString(user.getValues()));
    }
}
