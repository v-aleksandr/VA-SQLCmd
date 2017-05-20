package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.controller.command.*;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Александр on 17.05.17.
 */
public class MainController {

    private Command[] commands;
    private View view;
    private DatabaseManager manager;


    public  MainController(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
        this.commands = new Command[] {new Exit(view), new Help(view),
                new List(manager, view), new Find(manager, view)};
    }

    public void run() {
        connectToDb();
        while (true) {
            view.write("Введи команду (или help для помощи):");
            String command = view.read();
            if (commands[2].canProcess(command)) {
                commands[2].process(command);
            } else if (commands[1].canProcess(command)) {
                commands[1].process(command);
            } else if (commands[3].canProcess(command)) {
                commands[3].process(command);
            } else if (commands[0].canProcess(command)) {
                commands[0].process(command);
            } else {
                view.write("Несуществующая команда: " + command);
            }
        }
//
//
//
    }




    private void connectToDb() {
        view.write("Привет юзер!");
        view.write("Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: database|username|password");
        while (true) {
            try {
                String string = view.read();
                String[] data = string.split("\\|");
                if (data.length != 3) {
                    throw new IllegalArgumentException("Неверное количество параметров, разделенных знаком '|', ожидается 3, но есть: " + data.length);
                }
                String databaseName = data[0];
                String userName = data[1];
                String password = data[2];

                manager.connect(databaseName, userName, password);
                break;
            } catch (Exception e) {
                printError(e);
            }
        }
        view.write("Успех!");
    }

    private void printError(Exception e) {
        String message = e.getMessage();
        Throwable cause = e.getCause();
        if (cause != null) {
            message += " " + cause.getMessage();
        }
        view.write("Неудача по причине: " + message);
        view.write("Повтори попытку!");
    }
}
