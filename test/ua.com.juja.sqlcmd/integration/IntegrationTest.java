package ua.com.juja.sqlcmd.integration;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.Main;
import ua.com.juja.sqlcmd.controller.command.Connect;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    public void setup(){
        in = new ConfigurableInputStream();
        out = new ByteArrayOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

//    @Before
//    public void clearIn() throws IOException {
//        in.reset();
//    }

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
                "\tclear|tableName\r\n" +
                "\t\tдля очистки всей таблицы\r\n" +
                "\tcreate|tableName|column1|value1|column2|value2|...columnN|valueN\r\n" +
                "\t\tдля создания записи в таблице\r\n" +
                "\tfind|tableName\r\n" +
                "\t\tдля получения содержимого таблицы 'tableName'\r\n" +
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
//            out.reset();
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
                "----------------------------\r\n" +
                "|name|password|id|\r\n" +
                "----------------------------\r\n" +
                "----------------------------\r\n" +
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
//        in.add("connect|sqlcmd|postgres|postgres");
//        in.add("tables");
//        in.add("connect|test|postgres|postgres");
//        in.add("tables");
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
        in.add("create|user|id|13|name|Stiven|password|*****");
        in.add("create|user|id|14|name|Eva|password|+++++");
        in.add("find|user");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Таблица user была успешно очищена.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Запись {names: [id, name, password], values: [13, Stiven, *****]} была успешно создана в таблице 'user'.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Запись {names: [id, name, password], values: [14, Eva, +++++]} была успешно создана в таблице 'user'.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "----------------------------\r\n" +
                "|name|password|id|\r\n" +
                "----------------------------\r\n" +
                "|Stiven|*****|13|\r\n" +
                "|Eva|+++++|14|\r\n" +
                "----------------------------\r\n" +
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
    public void testCreateWithErrors() {

        in.add("connect|sqlcmd|postgres|postgres");
        in.add("create|user|error");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Неудача! по причине: Должно быть четное количество параметров в формате 'create|tableName|column1|value1|column2|value2|...columnN|valueN', а ты прислал 'create|user|error'\r\n" +
                "Повтори попытку!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());

    }
}
