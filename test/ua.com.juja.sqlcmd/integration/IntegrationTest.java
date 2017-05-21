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
                "\tlist\r\n" +
                "\t\tдля получения списка всех таблиц базы, к которой подключились\r\n" +
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

        in.add("list");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Вы не можете пользоваться командой 'list' пока не подключитесь с помощью команды connect|databaseName|userName|password\r\n" +
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
        in.add("list");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "[user]\r\n" +
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
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());

    }

    @Test
    public void testConnectAfterConnect() {

        in.add("connect|sqlcmd|postgres|postgres");
        in.add("list");
        in.add("connect|test|postgres|postgres");
        in.add("list");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "[user]\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "[qwe]\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());

    }
}
