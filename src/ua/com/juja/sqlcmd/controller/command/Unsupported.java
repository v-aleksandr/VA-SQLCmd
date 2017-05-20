package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.Command;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Александр on 20.05.17.
 */
public class Unsupported implements Command {
    private View view;

    public Unsupported(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return true;
    }

    @Override
    public void process(String command) {
        view.write("Несуществующая команда: " + command);
    }
}
