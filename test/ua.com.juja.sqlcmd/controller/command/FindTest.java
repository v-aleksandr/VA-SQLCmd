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

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
    }

    @Test
    public void testPrintTableData() {
        Command command = new Find(manager, view);
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

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals("[----------------------------, " +
                "|id|name|password|, " +
                "----------------------------, " +
                "|12|Stiven|******|, " +
                "|13|Eva|++++++|, " +
                "----------------------------]", captor.getAllValues().toString());
    }

    @Test
    public void testCanProcessFindWithParametersString() {
        Command command = new Find(manager, view);

        boolean canProcess = command.canProcess("find|user");

        assertTrue(canProcess);


    }

    @Test
    public void testCanProcessFindWithoutParametersString() {
        Command command = new Find(manager, view);

        boolean canProcess = command.canProcess("find");

        assertFalse(canProcess);


    }

    @Test

    public void testCanProcessQweString() {
        Command command = new Find(manager, view);

        boolean canProcess = command.canProcess("qwe|user");

        assertFalse(canProcess);


    }

    @Test
    public void testPrintEmptyTableData() {
        Command command = new Find(manager, view);
        when(manager.getTableColumns("user")).thenReturn(new String[] {"id", "name", "password"});

        DataSet[] data = new DataSet[0];
        when(manager.getTableData("user")).thenReturn(data);
        command.process("find|user");

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals("[----------------------------, " +
                "|id|name|password|, " +
                "----------------------------, " +
                "----------------------------]", captor.getAllValues().toString());
    }
}
