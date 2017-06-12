package ua.com.juja.sqlcmd.model;

import java.util.*;

/**
 * Created by Александр on 15.05.17.
 */
public class InMemoryDatabaseManager implements DatabaseManager {

    public static final String TABLE_NAME = "user"; //TODO implement multytable
    private LinkedList<DataSet> data = new LinkedList<DataSet>();

    @Override
    public List<DataSet> getTableData(String tableName) {
        validateTable(tableName);
        return data;
    }

    private void validateTable(String tableName) {
        if(!"user".equals(tableName)) {
            throw new UnsupportedOperationException("Only for 'user' table, but you try to work with:" + tableName);
        }
    }

    @Override
    public Set<String> getTableNames() {
        return new LinkedHashSet<String>(Arrays.asList(TABLE_NAME));
    }

    @Override
    public void connect(String database, String userName, String password) {

    }

    @Override
    public void clear(String tableName) {
        validateTable(tableName);
        data.clear();
    }

    @Override
    public void create(String tableName, DataSet input) {
        validateTable(tableName);
        data.add(input);
    }

    @Override
    public void update(String tableName, int id, DataSet newvalue) {
        validateTable(tableName);
        for (DataSet dataSet : data) {
            if ((int) dataSet.get("id") == id) {
                dataSet.updateFrom(newvalue);
            }
        }

    }

    @Override
    public Set<String> getTableColumns(String tableName) {
        return new LinkedHashSet<String>(Arrays.asList("name", "password", "id"));
    }

    @Override
    public boolean isConnected() {
        return true;
    }
}
