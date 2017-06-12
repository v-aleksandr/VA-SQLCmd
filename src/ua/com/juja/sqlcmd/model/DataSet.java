package ua.com.juja.sqlcmd.model;

import java.util.List;
import java.util.Set;

/**
 * Created by Александр on 12.06.17.
 */
public interface DataSet {
    void put(String name, Object value);
    
    Object get(String name);
    
    void updateFrom(DataSet newvalue);
    
    List<Object> getValues();
    
    Set<String> getNames();
}
