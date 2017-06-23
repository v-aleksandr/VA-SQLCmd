package ua.com.juja.sqlcmd.model;

import java.util.List;
import java.util.Set;

/**
 * Created by Александр on 15.05.17.
 */
public interface DatabaseManager {
    boolean isConnected();
    
    //String logonName();

    void connect(String database, String userName, String password);

    void clear(String tableName);
    
    void create(String tableName, DataSet input);

    void insert(String tableName, DataSet input);

    void update(String tableName, int id, DataSet newvalue);

    Set<String> getTableNames();
    
    DataSet getTableColumns(String tableName);
    
    int getSize(String tableName);
    
    List<DataSet> getTableData(String tableName);
    
    void drop(String tableName);
}
