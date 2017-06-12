package ua.com.juja.sqlcmd.model;

import java.util.Set;

/**
 * Created by Александр on 15.05.17.
 */
public interface DatabaseManager {
    boolean isConnected();

    void connect(String database, String userName, String password);

    void clear(String tableName);

    void create(String tableName, DataSet input);

    void update(String tableName, int id, DataSet newvalue);

    Set<String> getTableNames();

    Set<String> getTableColumns(String tableName);

    DataSet[] getTableData(String tableName);
}
