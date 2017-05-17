package ua.com.juja.sqlcmd.model;

import java.util.Arrays;

/**
 * Created by Александр on 15.05.17.
 */
public class InMemoryDatabaseManager implements DatabaseManager {

    public static final String TABLE_NAME = "user"; //TODO implement multytable
    private DataSet[] data = new DataSet[1000];
    private int freeIndex = 0;


    @Override
    public DataSet[] getTableData(String tableName) {
        validateTable(tableName);
        return Arrays.copyOf(data, freeIndex);
    }

    private void validateTable(String tableName) {
        if(!"user".equals(tableName)) {
            throw new UnsupportedOperationException("Only for 'user' table, but you try to work with:" + tableName);
        }
    }

    @Override
    public String[] getTableNames() {
        return new String[] {TABLE_NAME};
    }

    @Override
    public void connect(String database, String userName, String password) {

    }

    @Override
    public void clear(String tableName) {
        validateTable(tableName);
        data = new DataSet[1000];
        freeIndex = 0;
    }

    @Override
    public void create(String tableName, DataSet input) {
        validateTable(tableName);
        data[freeIndex] = input;
        freeIndex++;

    }

    @Override
    public void update(String tableName, int id, DataSet newvalue) {
        validateTable(tableName);
        for (int index = 0; index < freeIndex; index++) {
            if ((int)data[index].get("id") == id) {
                data[index].updateFrom(newvalue);
            }
        }

    }
}
