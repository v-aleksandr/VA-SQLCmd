package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DataSetImpl;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Александр on 21.05.17.
 */
public class Update implements Command {

    private DatabaseManager manager;
    private View view;

    public Update(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("update|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        int count = data.length;
        if (count != 6) {
            throw new IllegalArgumentException(String.format("Должно быть 6 параметров в формате " +
                    "'update|tableName|column1|value1|column2|value2', а ты прислал '%s'", command));
        }
        String tableName = data[1];
        DataSet updateCondition = new DataSetImpl();
        updateCondition.put(data[2], data[3]);
        DataSet updateValue = new DataSetImpl();
        updateValue.put(data[4], data[5]);
        manager.update(tableName, updateCondition, updateValue);
        view.write(String.format("В таблице '%s' в записях по условию %s были успешно изменены данные на %s.",
                tableName, updateCondition, updateValue));
    }
}
