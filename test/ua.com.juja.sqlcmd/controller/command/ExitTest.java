package ua.com.juja.sqlcmd.controller.command;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Александр on 25.05.17.
 */
public class ExitTest {

    private FakeView view = new FakeView();

    @Test
    public void testCanProcessExitString() {
        Command command = new Exit(view);

        boolean canProcess = command.canProcess("exit");

        assertTrue(canProcess);


    }

    @Test
    public void testCanProcessQweString() {
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

        assertEquals("До скорой встречи!\n",view.getContent());

    }
}
