package ua.com.juja.sqlcmd.view;

/**
 * Created by Александр on 17.05.17.
 */
public interface View {

    void write(String message);

    String read();
}
