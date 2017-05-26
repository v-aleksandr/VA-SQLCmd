package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.assertEquals;

/**
 * Created by Александр on 26.05.17.
 */
public class FindTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Find(manager, view);
    }

    @Test
    public void testPrintTableData() {
        when(manager.getTableColumns("user")).thenReturn(new String[] {"id", "name", "password"});
        DataSet user1 = new DataSet();
        user1.put("id",12);
        user1.put("name","Stiven");
        user1.put("password","******");
        DataSet user2 = new DataSet();
        user2.put("id",13);
        user2.put("name","Eva");
        user2.put("password","++++++");
        DataSet[] data = new DataSet[] {user1, user2};
        when(manager.getTableData("user")).thenReturn(data);
        command.process("find|user");

        shouldPrint("[----------------------------, " +
                "|id|name|password|, " +
                "----------------------------, " +
                "|12|Stiven|******|, " +
                "|13|Eva|++++++|, " +
                "----------------------------]");
    }

    @Test
    public void testCanProcessFindWithParametersString() {
        boolean canProcess = command.canProcess("find|user");

        assertTrue(canProcess);


    }

    @Test
    public void testCanProcessFindWithoutParametersString() {
        boolean canProcess = command.canProcess("find");
        assertFalse(canProcess);
    }

    @Test
    public void testCantProcessQweString() {
        boolean canProcess = command.canProcess("qwe|user");
        assertFalse(canProcess);
    }

    @Test
    public void testPrintEmptyTableData() {
        when(manager.getTableColumns("user")).thenReturn(new String[] {"id", "name", "password"});

        when(manager.getTableData("user")).thenReturn(new DataSet[0]);
        command.process("find|user");

        shouldPrint("[----------------------------, " +
                "|id|name|password|, " +
                "----------------------------, " +
                "----------------------------]");
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void testPrintTableDataWithOneColumn() {
        when(manager.getTableColumns("test")).thenReturn(new String[] {"id"});
        DataSet user1 = new DataSet();
        user1.put("id",12);
        DataSet user2 = new DataSet();
        user2.put("id",13);
        DataSet[] data = new DataSet[] {user1, user2};
        when(manager.getTableData("test")).thenReturn(data);
        command.process("find|test");

        shouldPrint("[----------------------------, " +
                "|id|, " +
                "----------------------------, " +
                "|12|, " +
                "|13|, " +
                "----------------------------]");
    }
}
