package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Александр on 19.05.17.
 */
public class Help implements Command {

    private View view;

    public Help (View view) {
        this.view = view;
    }
    @Override
    public boolean canProcess(String command) {
        return command.equals("help");
    }

    @Override
    public void process(String command) {
        view.write("Существующие команды:");

        view.write("\tconnect|databaseName|userName|password");
        view.write("\t\tдля подключения к базе данных, с которой будем работать");

        view.write("\ttables");
        view.write("\t\tдля получения списка всех таблиц базы, к которой подключились");
    
        view.write("\tcreate|tableName|column1|column2|...|columnN");
        view.write("\t\tдля создания таблицы с указанными полями");
    
        view.write("\tinsert|tableName|column1|value1|column2|value2|...|columnN|valueN");
        view.write("\t\tдля создания записи в таблице");
    
        //TODO update
        view.write("\tupdate|tableName|column1|value1|column2|value2");
        view.write("\t\tдля изменения записей в таблице - в тех записях, в которых column1 == value1, column2 " +
                "присвоить значение value2");
        
        view.write("\tfind|tableName");
        view.write("\t\tдля получения содержимого таблицы");
        
        //TODO delete
        view.write("\tdelete|tableName|column|value");
        view.write("\t\tдля удаления записей в таблице - тех записей, в которых column == value");
        
        view.write("\tclear|tableName");
        view.write("\t\tдля очистки всей таблицы");
    
        view.write("\tdrop|tableName");
        view.write("\t\tдля удаления таблицы");
        
        view.write("\thelp");
        view.write("\t\tдля вывода этого списка на экран");

        view.write("\texit");
        view.write("\t\tдля выхода из программы");
    }
}