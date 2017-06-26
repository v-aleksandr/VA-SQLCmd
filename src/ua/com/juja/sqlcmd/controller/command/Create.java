package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DataSetImpl;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Александр on 21.05.17.
 */
public class Create implements Command {

    private DatabaseManager manager;
    private View view;

    public Create(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("create|");
    }

    @Override
    public void process(String command) {
            String[] data = command.split("\\|");
            int count = data.length;
            if ((count < 4) || (count % 2 != 0)) {
                throw new IllegalArgumentException(String.format("Должно быть четное количество параметров не менее " +
                        "4-х в формате 'create|tableName|column1|type1|column2|type2|...|columnN|typeN', " +
                        "а ты прислал '%s'", command));
            }
            String tableName = data[1];

            DataSet tableStructure = new DataSetImpl();
            for (int index = 1; index < count / 2; index++) {
                String columnName = data[index * 2];
                String columnType = data[index * 2 + 1];
                tableStructure.put(columnName, columnType);
            }
            manager.create(tableName, tableStructure);
            view.write(String.format("Таблица '%s' с полями %s была успешно создана.", tableName, tableStructure));
    }
}
