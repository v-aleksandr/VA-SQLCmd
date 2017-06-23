package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DataSetImpl;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Александр on 21.05.17.
 */
public class Drop implements Command {

    private DatabaseManager manager;
    private View view;

    public Drop(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("drop|");
    }

    @Override
    public void process(String command) {
            String[] data = command.split("\\|");
            int count = data.length;
            if (count != 2) {
                throw new IllegalArgumentException(String.format("Должно быть всего 2 параметра в формате " +
                        "'drop|tableName', а ты прислал '%s'", command));
            }
            String tableName = data[1];

            manager.drop(tableName);
            view.write(String.format("Таблица '%s' была успешно удалена.", tableName));
    }
}
