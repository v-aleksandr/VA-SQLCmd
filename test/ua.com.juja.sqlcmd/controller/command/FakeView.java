package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Александр on 26.05.17.
 */
public class FakeView implements View {

    private  String messages = "";
    private String input = null;

    @Override
    public void write(String message) {
        messages += message + "\n";
    }

    @Override
    public String read() {
        if (this.input == null) {
            throw new IllegalStateException("Для работы проинициализируйте метод read.");
        }
        String result = this.input;
        this.input = null;
        return result;
    }

    public void addRead(String input) {
        this.input = input;
    }

    public String getContent() {
        return messages;
    }
}
