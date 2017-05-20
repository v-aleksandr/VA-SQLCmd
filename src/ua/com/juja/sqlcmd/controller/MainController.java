package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.controller.command.*;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Александр on 17.05.17.
 */
public class MainController {

    private Command[] commands;
    private View view;


    public  MainController(View view, DatabaseManager manager) {
        this.view = view;
        this.commands = new Command[] {
                new Connect(manager, view),
                new Help(view),
                new Exit(view),
                new IsConnected(manager, view),
                new List(manager, view),
                new Find(manager, view),
                new Unsupported(view)};
    }

    public void run() {
        view.write("Привет юзер!");
        view.write("Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|username|password");
        while (true) {
            String input = view.read();
            for (Command command : commands) {
                if (command.canProcess(input)) {
//                    connectToDb();
                    command.process(input);
                    break;
                }
            }
            view.write("Введи команду (или help для помощи):");
        }
    }


}
