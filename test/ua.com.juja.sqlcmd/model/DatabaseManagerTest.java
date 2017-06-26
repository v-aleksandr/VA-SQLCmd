package ua.com.juja.sqlcmd.model;

import org.junit.Before;
import org.junit.Test;

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
        manager.getTableData("user");
        manager.getTableData("test");
        
        Set<String> tableNames = manager.getTableNames();
        assertEquals("[user, test]", tableNames.toString());
    }

    @Test
    public void testGetTableData() {
        manager.clear("user");
        DataSet input = new DataSetImpl();
        input.put("name", "Stiven");
        input.put("password", "pass");
        input.put("id", "111");
        manager.insert("user", input);
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
        manager.insert("user", input);

        DataSet newvalue = new DataSetImpl();
        newvalue.put("password", "pass2");
        DataSet condition = new DataSetImpl();
        condition.put("id", 111);
        manager.update("user", condition, newvalue);

        List<DataSet> users = manager.getTableData("user");
        assertEquals(1, users.size());

        DataSet user = users.get(0);
        assertEquals("[name, password, id]",user.getNames().toString());
        assertEquals("[Stiven, pass2, 111]",user.getValues().toString());
    }

    @Test
    public void testGetColumnNamesFromUser() {
        manager.clear("user");
        DataSet columnNames = manager.getTableColumns("user");

        assertEquals("[name, password, id]",columnNames.getNames().toString());
    }
    @Test
    public void testGetColumnNamesFromTuser() {
//        manager.clear("tuser");
        String tableName = "tuser";
        DataSet input = new DataSetImpl();
        input.put("fname", "text");
        manager.create(tableName, input);
    
        DataSet columnNames = manager.getTableColumns(tableName);
        manager.drop(tableName);
        
        assertEquals("[fname]",columnNames.getNames().toString());
    }
    @Test
    public void testIsConnected() {
        assertTrue(manager.isConnected());
    }
    
    @Test
    public void testGetTableSize() {
        manager.clear("user");
        assertEquals(0, manager.getSize("user"));
        
        DataSet input = new DataSetImpl();
        input.put("name", "Stiven");
        input.put("password", "pass");
        input.put("id", 111);
        manager.insert("user", input);
        assertEquals(1, manager.getSize("user"));
        
        input.put("name", "Eva");
        input.put("password", "zzzz");
        input.put("id", 112);
        manager.insert("user", input);
        assertEquals(2, manager.getSize("user"));
    }

}
