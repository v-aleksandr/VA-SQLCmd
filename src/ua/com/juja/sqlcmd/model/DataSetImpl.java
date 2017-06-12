package ua.com.juja.sqlcmd.model;

import java.util.*;

/**
 * Created by Александр on 14.05.17.
 */
public class DataSetImpl implements DataSet {
    
    private Map<String, Object> data = new LinkedHashMap<String, Object>();
    
    @Override
    public String toString() {
        return "{" +
                "names: " + getNames().toString() + ", " +
                "values: " + getValues().toString() +
                "}";
    }
    
    @Override
    public void put(String name, Object value) {
        data.put(name, value);
    }
    
    @Override
    public List<Object> getValues() {
        return new ArrayList<Object>(data.values());
    }
    
    @Override
    public Set<String> getNames() {
        return data.keySet();
    }
    
    @Override
    public Object get(String name) {
        return data.get(name);
    }
    
    @Override
    public void updateFrom(DataSet newvalue) {
        Set<String> columns = newvalue.getNames();
        for (String name : columns) {
            Object data = newvalue.get(name);
            put(name, data);
        }
    }
}
