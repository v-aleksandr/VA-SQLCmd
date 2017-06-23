package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DataSetImpl;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;

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
        setupTableColumns("user", "id", "name", "password");
        DataSet user1 = new DataSetImpl();
        user1.put("id",12);
        user1.put("name","Stiven");
        user1.put("password","******");
        DataSet user2 = new DataSetImpl();
        user2.put("id",13);
        user2.put("name","Eva");
        user2.put("password","++++++");
        when(manager.getTableData("user")).thenReturn(Arrays.asList(user1, user2));
        command.process("find|user");

        shouldPrint("[┌──┬──────┬────────┐, " +
                "│id│ name │password│, " +
                "├──┼──────┼────────┤, " +
                "│12│Stiven│ ****** │, " +
                "│13│  Eva │ ++++++ │, " +
                "└──┴──────┴────────┘]");
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
        setupTableColumns("user", "id", "name", "password");

        when(manager.getTableData("user")).thenReturn(new ArrayList<DataSet>());
        command.process("find|user");

        shouldPrint("[┌──┬────┬────────┐, " +
                "│id│name│password│, " +
                "├──┼────┼────────┤, " +
                "└──┴────┴────────┘]");
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void testPrintTableDataWithOneColumn() {
        setupTableColumns("test", "id");
        DataSet user1 = new DataSetImpl();
        user1.put("id",12);
        DataSet user2 = new DataSetImpl();
        user2.put("id",13);
        when(manager.getTableData("test")).thenReturn(Arrays.asList(user1, user2));
        command.process("find|test");

        shouldPrint("[┌──┐, " +
                "│id│, " +
                "├──┤, " +
                "│12│, " +
                "│13│, " +
                "└──┘]");
    }
    
    private void setupTableColumns(String tableName, String... columns) {
        DataSet dataSet = new DataSetImpl();
        for (String name : columns) {
            dataSet.put(name, name.length());
        }
        when(manager.getTableColumns(tableName)).thenReturn(dataSet);
    }
    
    @Test
    public void testErrorWhenBadCommandFormat() {
        try {
            command.process("find|user|sgf");
            fail("Expected exception!");
        }catch (IllegalArgumentException e) {
            assertEquals("Формат команды 'find|tableName', а ты ввел: find|user|sgf", e.getMessage());
        }
    }
}
