package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Александр on 26.05.17.
 */
public class ClearTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Clear(manager, view);
    }

    @Test
    public void testClearTable() {
        command.process("clear|user");
        verify(manager).clear("user");
        verify(view).write("Таблица user была успешно очищена.");
    }

    @Test
    public void testCanProcessClearWithParametersString() {
        boolean canProcess = command.canProcess("clear|user");

        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessClearWithoutParametersString() {
        boolean canProcess = command.canProcess("clear");
        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessQweString() {
        boolean canProcess = command.canProcess("qwe|user");
        assertFalse(canProcess);
    }
}
