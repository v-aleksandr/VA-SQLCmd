package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DataSetImpl;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Александр on 21.05.17.
 */
public class Insert implements Command {

    private DatabaseManager manager;
    private View view;

    public Insert(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("insert|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        int count = data.length;
        if ((count < 4) || (count % 2 != 0)) {
            throw new IllegalArgumentException(String.format("Должно быть четное количество параметров не менее " +
                    "4-х  в формате 'insert|tableName|column1|value1|column2|value2|...columnN|valueN', " +
                    "а ты прислал '%s'", command));
        }
        String tableName = data[1];
        DataSet dataset = new DataSetImpl();
        for (int index = 1; index < (count / 2); index++) {
            String columnName = data[index * 2];
            String columnValue = data[index * 2 + 1];
            dataset.put(columnName, columnValue);
        }
        manager.insert(tableName, dataset);
        view.write(String.format("Запись %s была успешно создана в таблице '%s'.", dataset, tableName));
    }
}
