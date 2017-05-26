package ua.com.juja.sqlcmd.controller.command;

import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.*;

/**
 * Created by Александр on 25.05.17.
 */
public class ExitTestWithMockito {

    private View view = Mockito.mock(View.class);

    @Test
    public void testCanProcessExitString() {
        Command command = new Exit(view);

        boolean canProcess = command.canProcess("exit");

        assertTrue(canProcess);


    }

    @Test
    public void testCantProcessQweString() {
        Command command = new Exit(view);

        boolean canProcess = command.canProcess("qwe");

        assertFalse(canProcess);


    }

    @Test
    public void testProcessExitCommand_throwsExitException() {
        Command command = new Exit(view);

        try {
            command.process("exit");
            fail("Expected ExitException");
        }catch (ExitException e) {

        }
        Mockito.verify(view).write("До скорой встречи!");

    }
}
