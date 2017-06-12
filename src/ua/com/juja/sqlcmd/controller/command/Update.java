package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Александр on 19.05.17.
 */
public class Update implements Command {

    private View view;

    public Update(View view) {
        this.view = view;
    }
    @Override
    public boolean canProcess(String command) {
        return command.startsWith("update");
    }

    @Override
    public void process(String command) {
        view.write("До скорой встречи!");
//        System.exit(0);
        throw new ExitException();
    }
}
