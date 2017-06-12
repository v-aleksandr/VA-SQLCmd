package ua.com.juja.sqlcmd.model;

import org.postgresql.util.*;

import java.sql.*;
import java.util.*;

/**
 * Created by Александр on 13.05.17.
 */
public class JDBCDatabaseManager implements DatabaseManager {

    private Connection connection;

    private int getSize(String tableName) {
        try (Statement stmt = connection.createStatement();
             ResultSet rsCount = stmt.executeQuery("SELECT COUNT(*) FROM public." + tableName))
        {
            rsCount.next();
            int size = rsCount.getInt(1);
            return size;
        }catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private String getNamesFormatted(DataSet newvalue, String format) {
        String string = "";
        for (String name : newvalue.getNames()) {
            string += String.format(format, name);
        }
        string = string.substring(0, string.length() - 1);
        return string;
    }

    private String getValuesFormatted(DataSet input, String format) {
        String values = "";
        for (Object value : input.getValues()) {
            values += String.format(format, value);
        }
        values = values.substring(0, values.length() - 1);
        return values;
    }

    @Override
    public List<DataSet> getTableData(String tableName) {
        
//        List<DataSet> result = new ArrayList<DataSet>(getSize(tableName));
        List<DataSet> result = new LinkedList<DataSet>();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM public." + tableName))
        {
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                DataSet dataSet = new DataSet();
                result.add(dataSet);
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    dataSet.put(rsmd.getColumnName(i + 1), rs.getObject(i + 1));
                }
            }
            return result;
        }catch (SQLException e) {
            e.printStackTrace();
            return result;
        }
    }

    @Override
    public Set<String> getTableNames() {
        Set<String> tables = new LinkedHashSet<String>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT table_name " +
                     "FROM information_schema.tables " +
                     "WHERE table_schema='public' AND table_type='BASE TABLE'"))
        {
            while (rs.next()) {
                tables.add(rs.getString("table_name"));
            }
            return tables;
        }catch (SQLException e) {
            e.printStackTrace();
            return tables;
        }
    }

    @Override
    public void connect(String database, String userName, String password) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Please add jdbc jar to project.", e);
        }
        try {
            if (connection != null) {
                connection.close();
            }
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/" + database, userName,
                    password);
        } catch (PSQLException e) {
            connection = null;
            throw new RuntimeException(
                    String.format("Can't open connection to model:%s user:%s",
                            database, userName) ,
                    e);
        } catch (SQLException e) {
            connection = null;
            throw new RuntimeException(
                    String.format("Can't open connection to model:%s user:%s",
                            database, userName) ,
                    e);
        }
    }

    @Override
    public void clear(String tableName) {
        try (Statement stmt = connection.createStatement())
        {
            stmt.executeUpdate("DELETE FROM public." + tableName );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(String tableName, DataSet input) {
        try (Statement stmt = connection.createStatement())
        {
            String names = getNamesFormatted(input, "%s,");
            String values = getValuesFormatted(input, "'%s',");
            stmt.executeUpdate("INSERT INTO public." + tableName + " (" + names + ") " +
                    "VALUES (" + values + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(String tableName, int id, DataSet newvalue) {
        String format = "%s = ?,";
        String string = getNamesFormatted(newvalue, format);
        try (PreparedStatement pst = connection.prepareStatement("UPDATE public." + tableName +
                " SET " + string + " WHERE id = ?"))
        {
            int index = 1;
            for (Object value : newvalue.getValues()) {
                pst.setObject(index++, value);
            }
            pst.setInt(index, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<String> getTableColumns(String tableName) {
        Set<String> columnNames = new LinkedHashSet<String>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT column_name FROM information_schema.columns WHERE table_name = '" +
                     tableName + "'"))
        {
            while (rs.next()) {
                columnNames.add(rs.getString("column_name"));
            }
            return columnNames;
        }catch (SQLException e) {
            e.printStackTrace();
            return columnNames;
        }
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }
}
