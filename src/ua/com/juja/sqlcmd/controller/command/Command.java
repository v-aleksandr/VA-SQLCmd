package ua.com.juja.sqlcmd.controller.command;

/**
 * Created by Александр on 19.05.17.
 */
public interface Command {
    boolean canProcess(String command);

    void process(String command);
    
//    String format();
//    String description();
}
