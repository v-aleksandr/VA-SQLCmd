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
            if (count < 3) {
                throw new IllegalArgumentException(String.format("Должно быть как минимум 3 параметра в формате " +
                        "'create|tableName|column1|column2|...|columnN', " +
                        "а ты прислал '%s'", command));
            }
            String tableName = data[1];

            DataSet dataset = new DataSetImpl();
            for (int index = 2; index < count; index++) {
                dataset.put(data[index], "text");
            }
            manager.create(tableName, dataset);
            view.write(String.format("Таблица '%s' с полями %s была успешно создана.", tableName, dataset));
    }
}
