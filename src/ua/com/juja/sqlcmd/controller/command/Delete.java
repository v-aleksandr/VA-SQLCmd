package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DataSetImpl;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Александр on 21.05.17.
 */
public class Delete implements Command {

    private DatabaseManager manager;
    private View view;

    public Delete(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("delete|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        int count = data.length;
        if ((count < 4) || (count % 2 != 0)) {
            throw new IllegalArgumentException(String.format("Должно быть четное количество параметров не менее " +
                    "4-х в формате 'delete|tableName|column1|value1|column2|value2|...columnN|valueN', " +
                    "а ты прислал '%s'", command));
        }
        String tableName = data[1];
        DataSet deleteCondition = new DataSetImpl();
        for (int index = 1; index < count / 2; index++) {
            String columnName = data[index * 2];
            String columnValue = data[index * 2 + 1];
            deleteCondition.put(columnName, columnValue);
        }
        manager.delete(tableName, deleteCondition);
        view.write(String.format("Записи по условию %s были успешно удалены в таблице '%s'.", deleteCondition,
                tableName));
    }
}
