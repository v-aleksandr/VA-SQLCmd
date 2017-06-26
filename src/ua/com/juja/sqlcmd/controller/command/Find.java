package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.List;
import java.util.Set;

/**
 * Created by Александр on 19.05.17.
 */
public class Find implements Command {
    
    private DatabaseManager manager;
    private View view;
    
    public Find(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }
    
    @Override
    public boolean canProcess(String command) {
        return command.startsWith("find|");
    }
    
    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            throw new IllegalArgumentException("Формат команды 'find|tableName', а ты ввел: " + command);
        }
        String tableName = data[1];
        
        List<DataSet> tableData = manager.getTableData(tableName);
        DataSet tableColumns = manager.getTableColumns(tableName);
        synchronizeColumnsWidths(tableColumns, tableData);
        printHeader(tableColumns);
        printTable(tableData, tableColumns);
        printFooter(tableColumns);
    }
    
    private void synchronizeColumnsWidths(DataSet tableColumns, List<DataSet> tableData) {
        for (String name : tableColumns.getNames()) {
            tableColumns.put(name, name.length());
        }
        if (tableData.size() != 0) {
            for (DataSet rowTableData : tableData) {
                for (String name : rowTableData.getNames()) {
                    int rowColumnSpaces = rowTableData.get(name).toString().length();
                    int columnSpaces = (int) tableColumns.get(name);
                    if (rowColumnSpaces > columnSpaces) {
                        tableColumns.put(name, rowColumnSpaces);
                    }
                }
            }
        }
    }
    
    private void printHeader(DataSet tableColumns) {
        String result = "│";
        String lineBefore = "┌";
        String lineAfter = "├";
        for (String name : tableColumns.getNames()) {
            int countSpaces = (int) tableColumns.get(name);
            result = wrapInSpaces(result, name, countSpaces);
            for (int i = 0; i < countSpaces; i++) {
                lineBefore += "─";
                lineAfter += "─";
            }
            result += "│";
            lineBefore += "┬";
            lineAfter += "┼";
        }
        lineBefore = lineBefore.substring(0, lineBefore.length() - 1).concat("┐");
        lineAfter = lineAfter.substring(0, lineAfter.length() - 1).concat("┤");
//        view.write(" ┌─┐┬┼┴└┘│┤├ ");
        view.write(lineBefore);
        view.write(result);
        view.write(lineAfter);
    }
    
    private void printTable(List<DataSet> tableData, DataSet tableColumns) {
        
        for (DataSet row : tableData) {
            String result = "│";
            for (String columnName : row.getNames()) {
                int countSpaces = (int) tableColumns.get(columnName);
                result = wrapInSpaces(result, row.get(columnName).toString(), countSpaces) + "│";
            }
//            List<Object> values = row.getValues();
//            for (Object value : values) {
//                result += value ;
//            }
            view.write(result);
        }
    }
    
    private void printFooter(DataSet tableColumns) {
        String lineAfter = "└";
        for (String name : tableColumns.getNames()) {
            int countSpaces = (int) tableColumns.get(name);
            for (int i = 0; i < countSpaces; i++) {
                lineAfter += "─";
            }
            lineAfter += "┴";
        }
        lineAfter = lineAfter.substring(0, lineAfter.length() - 1).concat("┘");
        view.write(lineAfter);
    }
    
    private String wrapInSpaces(String result, String name, int countSpaces) {
        int countRightSpaces = (countSpaces - name.length()) / 2;
        int countLeftSpaces = countSpaces - name.length() - countRightSpaces;
        for (int i = 0; i < countLeftSpaces; i++) {
            result += " ";
        }
        result += name;
        for (int i = 0; i < countRightSpaces; i++) {
            result += " ";
        }
        return result;
    }
}


