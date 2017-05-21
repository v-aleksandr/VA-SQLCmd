package ua.com.juja.sqlcmd.integration;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Александр on 20.05.17.
 */
public class LogOutputStream extends OutputStream {

    private String log;

    @Override
    public void write(int b) throws IOException {
        log += String.valueOf((char)b);
    }

    public String getData() {
        return log;
    }
}
