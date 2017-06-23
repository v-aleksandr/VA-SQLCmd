package ua.com.juja.sqlcmd.model;

import java.util.*;

/**
 * Created by Александр on 15.05.17.
 */
public class InMemoryDatabaseManager implements DatabaseManager {

    private Map<String, List<DataSet>> tables = new LinkedHashMap<>();
    
    @Override
    public List<DataSet> getTableData(String tableName) {
        return get(tableName);
    }
    
    @Override
    public void drop(String tableName) {
        tables.remove(tableName);
    }
    
    private void validateTable(String tableName) {
        if(!"user".equals(tableName)) {
            throw new UnsupportedOperationException("Only for 'user' table, but you try to work with:" + tableName);
        }
    }

    @Override
    public Set<String> getTableNames() {
        return tables.keySet();
    }

    @Override
    public void connect(String database, String userName, String password) {

    }

    @Override
    public void clear(String tableName) {
        get(tableName).clear();
    }
    
    @Override
    public void create(String tableName, DataSet input) {
        if (!tables.containsKey(tableName)) {
            tables.put(tableName, new LinkedList<DataSet>());
        }
    }
    
    private List<DataSet> get(String tableName) {
        if (!tables.containsKey(tableName)) {
            tables.put(tableName, new LinkedList<DataSet>());
        }
        return tables.get(tableName);
    }
    
    @Override
    public void insert(String tableName, DataSet input) {
//        DataSet dataSet = new DataSetImpl();
//        dataSet.put("name", 6);
//        dataSet.put("password", 8);
//        dataSet.put("id", 3);
//        get(tableName).add(dataSet);
        get(tableName).add(input);
    }

    @Override
    public void update(String tableName, int id, DataSet newvalue) {
        for (DataSet dataSet : get(tableName)) {
            if ((int) dataSet.get("id") == id) {
                dataSet.updateFrom(newvalue);
            }
        }
    }

    @Override
    public DataSet getTableColumns(String tableName) {
        DataSet dataSet = new DataSetImpl();
        switch (tableName) {
            case "user" :
                dataSet.put("name", 4);
                dataSet.put("password", 8);
                dataSet.put("id", 2);
                break;
            case "tuser" :
                dataSet.put("fname", 5);
                break;
            default:
                break;
        }
        return dataSet;
    }
    
    @Override
    public int getSize(String tableName) {
        return get(tableName).size();
    }
    
    @Override
    public boolean isConnected() {
        return true;
    }
}
