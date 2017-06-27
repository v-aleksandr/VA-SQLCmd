package ua.com.juja.sqlcmd.integration;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.Main;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

/**
 * Created by Александр on 20.05.17.
 */
public class IntegrationTest {
    
    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;
    
    @Before
    public void setup() {
        in = new ConfigurableInputStream();
        out = new ByteArrayOutputStream();
        
        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @Test
    public void testHelp() {
        
        in.add("help");
        in.add("exit");
        
        Main.main(new String[0]);
        
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Существующие команды:\r\n" +
                "\tconnect|databaseName|userName|password\r\n" +
                "\t\tдля подключения к базе данных, с которой будем работать\r\n" +
                "\ttables\r\n" +
                "\t\tдля получения списка всех таблиц базы, к которой подключились\r\n" +
                "\tcreate|tableName|column1|type1|column2|type2|...|columnN|typeN\r\n" +
                "\t\tдля создания таблицы с полями указанных типов(text или numeric)\r\n" +
                "\tinsert|tableName|column1|value1|column2|value2|...|columnN|valueN\r\n" +
                "\t\tдля создания записи в таблице\r\n" +
                "\tupdate|tableName|column1|value1|column2|value2\r\n" +
                "\t\tдля изменения записей в таблице - в тех записях, в которых column1 == value1, column2 присвоить " +
                "значение value2\r\n" +
                "\tfind|tableName\r\n" +
                "\t\tдля получения содержимого таблицы\r\n" +
                "\tdelete|tableName|column1|value1|column2|value2|...|columnN|valueN\r\n" +
                "\t\tдля удаления записей в таблице - тех записей, в которых columnN == valueN\r\n" +
                "\tclear|tableName\r\n" +
                "\t\tдля очистки всей таблицы\r\n" +
                "\tdrop|tableName\r\n" +
                "\t\tдля удаления таблицы\r\n" +
                "\thelp\r\n" +
                "\t\tдля вывода этого списка на экран\r\n" +
                "\texit\r\n" +
                "\t\tдля выхода из программы\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
    }
    
    public String getData() {
        try {
            String result = new String(out.toByteArray(), "UTF-8");
            return result;
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }
    
    @Test
    public void testExit() {
        
        in.add("exit");
        
        Main.main(new String[0]);
        
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "До скорой встречи!\r\n", getData());
    }
    
    @Test
    public void testListWithoutConnect() {
        
        in.add("tables");
        in.add("exit");
        
        Main.main(new String[0]);
        
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Вы не можете пользоваться командой 'tables' пока не подключитесь с помощью команды " +
                "connect|databaseName|userName|password\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
    }
    
    @Test
    public void testFindWithoutConnect() {
        
        in.add("find|user");
        in.add("exit");
        
        Main.main(new String[0]);
        
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Вы не можете пользоваться командой 'find|user' пока не подключитесь с помощью команды connect|databaseName|userName|password\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
    }
    
    @Test
    public void testUnsupported() {
        
        in.add("unsupported");
        in.add("exit");
        
        Main.main(new String[0]);
        
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Вы не можете пользоваться командой 'unsupported' пока не подключитесь с помощью команды connect|databaseName|userName|password\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
    }
    
    @Test
    public void testUnsupportedAfterConnect() {
        
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("unsupported");
        in.add("exit");
        
        Main.main(new String[0]);
        
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Несуществующая команда: unsupported\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
    }
    
    @Test
    public void testListAfterConnect() {
        
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("tables");
        in.add("exit");
        
        Main.main(new String[0]);
        
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "[user, test]\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
    }
    
    @Test
    public void testFindAfterConnect() {
        
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("find|user");
        in.add("exit");
        
        Main.main(new String[0]);
        
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "┌────┬────────┬──┐\r\n" +
                "│name│password│id│\r\n" +
                "├────┼────────┼──┤\r\n" +
                "└────┴────────┴──┘\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
    }
    
    @Test
    public void testConnectAfterConnect() {
        
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("tables");
        in.add("connect|test|postgres|postgres");
        in.add("tables");
        in.add("exit");
        
        Main.main(new String[0]);
        
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "[user, test]\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "[qwe]\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
    }
    
    @Test
    public void testConnectWithError() {
        
        in.add("connect|sqlcmd|");
        in.add("exit");
        
        Main.main(new String[0]);
        
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Неудача! по причине: Неверное количество параметров, разделенных знаком '|', ожидается 4, но есть: 2\r\n" +
                "Повтори попытку!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
    }
    
    @Test
    public void testFindAfterConnectWithData() {
        
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("clear|user");
        in.add("insert|user|id|13|name|Stiven|password|*****");
        in.add("insert|user|id|14|name|Eva|password|+++++");
        in.add("find|user");
        in.add("exit");
        
        Main.main(new String[0]);
        
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Таблица user была успешно очищена.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Запись { id='13', name='Stiven', password='*****' } была успешно создана в таблице 'user'.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Запись { id='14', name='Eva', password='+++++' } была успешно создана в таблице 'user'.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "┌──────┬────────┬──┐\r\n" +
                "│ name │password│id│\r\n" +
                "├──────┼────────┼──┤\r\n" +
                "│Stiven│  ***** │13│\r\n" +
                "│  Eva │  +++++ │14│\r\n" +
                "└──────┴────────┴──┘\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
    }
    
    @Test
    public void testClearWithError() {
        
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("clear|sadfasd|fsf|fdsf");
        in.add("exit");
        
        Main.main(new String[0]);
        
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Неудача! по причине: Формат команды 'clear|tableName', а ты ввел: clear|sadfasd|fsf|fdsf\r\n" +
                "Повтори попытку!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
    }
    
    
    @Test
    public void testInsertWithErrors() {
        
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("insert|user|error");
        in.add("exit");
        
        Main.main(new String[0]);
        
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Неудача! по причине: Должно быть четное количество параметров не менее 4-х  в формате 'insert|tableName|column1|value1|column2|value2|...columnN|valueN', а ты прислал 'insert|user|error'\r\n" +
                "Повтори попытку!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
    }
    
    @Test
    public void testCreateAndDropAfterConnect() {
        
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("tables");
        in.add("create|tuser|fname|text");
        in.add("tables");
        in.add("drop|tuser");
        in.add("tables");
        in.add("exit");
        
        Main.main(new String[0]);
        
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "[user, test]\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Таблица 'tuser' с полями { fname='text' } была успешно создана.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "[user, test, tuser]\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Таблица 'tuser' была успешно удалена.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "[user, test]\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
    }
    
    @Test
    public void testDeleteWithData() {
        
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("clear|user");
        in.add("insert|user|id|13|name|Stiven|password|*****");
        in.add("insert|user|id|14|name|Eva|password|+++++");
        in.add("insert|user|id|15|name|Stiven|password|-----");
        in.add("find|user");
        in.add("delete|user|id|13");
        in.add("find|user");
        in.add("insert|user|id|13|name|Stiven|password|*****");
        in.add("find|user");
        in.add("delete|user|name|Stiven|password|*****");
        in.add("find|user");
        in.add("insert|user|id|13|name|Stiven|password|*****");
        in.add("find|user");
        in.add("delete|user|name|Stiven");
        in.add("find|user");
        in.add("clear|user");
        in.add("exit");
        
        Main.main(new String[0]);
        
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Таблица user была успешно очищена.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Запись { id='13', name='Stiven', password='*****' } была успешно создана в таблице 'user'.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Запись { id='14', name='Eva', password='+++++' } была успешно создана в таблице 'user'.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Запись { id='15', name='Stiven', password='-----' } была успешно создана в таблице " + "'user'.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "┌──────┬────────┬──┐\r\n" +
                "│ name │password│id│\r\n" +
                "├──────┼────────┼──┤\r\n" +
                "│Stiven│  ***** │13│\r\n" +
                "│  Eva │  +++++ │14│\r\n" +
                "│Stiven│  ----- │15│\r\n" +
                "└──────┴────────┴──┘\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Записи по условию { id='13' } были успешно удалены в таблице 'user'.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "┌──────┬────────┬──┐\r\n" +
                "│ name │password│id│\r\n" +
                "├──────┼────────┼──┤\r\n" +
                "│  Eva │  +++++ │14│\r\n" +
                "│Stiven│  ----- │15│\r\n" +
                "└──────┴────────┴──┘\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Запись { id='13', name='Stiven', password='*****' } была успешно создана в таблице 'user'.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "┌──────┬────────┬──┐\r\n" +
                "│ name │password│id│\r\n" +
                "├──────┼────────┼──┤\r\n" +
                "│  Eva │  +++++ │14│\r\n" +
                "│Stiven│  ----- │15│\r\n" +
                "│Stiven│  ***** │13│\r\n" +
                "└──────┴────────┴──┘\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Записи по условию { name='Stiven', password='*****' } были успешно удалены в таблице 'user'.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "┌──────┬────────┬──┐\r\n" +
                "│ name │password│id│\r\n" +
                "├──────┼────────┼──┤\r\n" +
                "│  Eva │  +++++ │14│\r\n" +
                "│Stiven│  ----- │15│\r\n" +
                "└──────┴────────┴──┘\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Запись { id='13', name='Stiven', password='*****' } была успешно создана в таблице 'user'.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "┌──────┬────────┬──┐\r\n" +
                "│ name │password│id│\r\n" +
                "├──────┼────────┼──┤\r\n" +
                "│  Eva │  +++++ │14│\r\n" +
                "│Stiven│  ----- │15│\r\n" +
                "│Stiven│  ***** │13│\r\n" +
                "└──────┴────────┴──┘\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Записи по условию { name='Stiven' } были успешно удалены в таблице 'user'.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "┌────┬────────┬──┐\r\n" +
                "│name│password│id│\r\n" +
                "├────┼────────┼──┤\r\n" +
                "│ Eva│  +++++ │14│\r\n" +
                "└────┴────────┴──┘\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Таблица user была успешно очищена.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
    }
    
    @Test
    public void testUpdateWithData() {
        
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("clear|user");
        in.add("insert|user|id|13|name|Stiven|password|*****");
        in.add("insert|user|id|14|name|Eva|password|+++++");
        in.add("insert|user|id|15|name|Stiven|password|-----");
        in.add("find|user");
        in.add("update|user|id|13|password|^^^^^");
        in.add("find|user");
        in.add("update|user|name|Eva|password|*****");
        in.add("find|user");
        in.add("update|user|name|Stiven|password|+++++");
        in.add("find|user");
        in.add("update|user|id|15|password|^^^^^");
        in.add("find|user");
        in.add("clear|user");
        in.add("exit");
        
        Main.main(new String[0]);
        
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Таблица user была успешно очищена.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Запись { id='13', name='Stiven', password='*****' } была успешно создана в таблице 'user'.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Запись { id='14', name='Eva', password='+++++' } была успешно создана в таблице 'user'.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Запись { id='15', name='Stiven', password='-----' } была успешно создана в таблице " + "'user'.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "┌──────┬────────┬──┐\r\n" +
                "│ name │password│id│\r\n" +
                "├──────┼────────┼──┤\r\n" +
                "│Stiven│  ***** │13│\r\n" +
                "│  Eva │  +++++ │14│\r\n" +
                "│Stiven│  ----- │15│\r\n" +
                "└──────┴────────┴──┘\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "В таблице 'user' в записях по условию { id='13' } были успешно изменены данные на { password='^^^^^' }.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "┌──────┬────────┬──┐\r\n" +
                "│ name │password│id│\r\n" +
                "├──────┼────────┼──┤\r\n" +
                "│  Eva │  +++++ │14│\r\n" +
                "│Stiven│  ----- │15│\r\n" +
                "│Stiven│  ^^^^^ │13│\r\n" +
                "└──────┴────────┴──┘\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "В таблице 'user' в записях по условию { name='Eva' } были успешно изменены данные на { password='*****' }.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "┌──────┬────────┬──┐\r\n" +
                "│ name │password│id│\r\n" +
                "├──────┼────────┼──┤\r\n" +
                "│Stiven│  ----- │15│\r\n" +
                "│Stiven│  ^^^^^ │13│\r\n" +
                "│  Eva │  ***** │14│\r\n" +
                "└──────┴────────┴──┘\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "В таблице 'user' в записях по условию { name='Stiven' } были успешно изменены данные на { password='+++++' }.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "┌──────┬────────┬──┐\r\n" +
                "│ name │password│id│\r\n" +
                "├──────┼────────┼──┤\r\n" +
                "│  Eva │  ***** │14│\r\n" +
                "│Stiven│  +++++ │15│\r\n" +
                "│Stiven│  +++++ │13│\r\n" +
                "└──────┴────────┴──┘\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "В таблице 'user' в записях по условию { id='15' } были успешно изменены данные на { password='^^^^^' }.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "┌──────┬────────┬──┐\r\n" +
                "│ name │password│id│\r\n" +
                "├──────┼────────┼──┤\r\n" +
                "│  Eva │  ***** │14│\r\n" +
                "│Stiven│  +++++ │13│\r\n" +
                "│Stiven│  ^^^^^ │15│\r\n" +
                "└──────┴────────┴──┘\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Таблица user была успешно очищена.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
    }
    
    @Test
    public void testCreateWithError() {
        
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("create|tuser|fname");
        in.add("exit");
        
        Main.main(new String[0]);
        
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Неудача! по причине: Должно быть четное количество параметров не менее 4-х в формате " +
                "'create|tableName|column1|type1|column2|type2|...|columnN|typeN', а ты прислал " +
                "'create|tuser|fname'\r\n" +
                "Повтори попытку!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
    }
    
    @Test
    public void testDeleteWithError() {
        
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("delete|tuser|fname");
        in.add("exit");
        
        Main.main(new String[0]);
        
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Неудача! по причине: Должно быть четное количество параметров не менее 4-х в формате 'delete|tableName|column1|value1|column2|value2|...columnN|valueN', а ты прислал 'delete|tuser|fname'\r\n" +
                "Повтори попытку!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
    }
    
    @Test
    public void testDropWithError() {
        
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("drop|tuser|fname");
        in.add("exit");
        
        Main.main(new String[0]);
        
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Неудача! по причине: Должно быть всего 2 параметра в формате 'drop|tableName', а ты прислал 'drop|tuser|fname'\r\n" +
                "Повтори попытку!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
    }
    
    @Test
    public void testUpdateWithError() {
        
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("update|user|fname");
        in.add("exit");
        
        Main.main(new String[0]);
        
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Неудача! по причине: Должно быть 6 параметров в формате 'update|tableName|column1|value1|column2|value2', а ты прислал 'update|user|fname'\r\n" +
                "Повтори попытку!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
    }
    
}
