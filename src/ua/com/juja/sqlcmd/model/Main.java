package ua.com.juja.sqlcmd.model;

import java.sql.*;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Александр on 13.05.17.
 */
public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // connect to db
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/sqlcmd", "postgres",
                    "postgres");
        Statement stmt = connection.createStatement();
//        insert
        stmt.executeUpdate("INSERT INTO public.user (name, password) " +
                "VALUES ('Stiven', 'Pupkin')");
        stmt.close();
        //      table names
        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT table_name " +
                "FROM information_schema.tables " +
                "WHERE table_schema='public' AND table_type='BASE TABLE'");
        String[] tables = new String[100];
        int index = 0;
        while (rs.next()) {
            tables[index++] = rs.getString("table_name");
        }
        tables = Arrays.copyOf(tables, index - 1, String[].class);
        rs.close();
        stmt.close();
        System.out.println(Arrays.toString(tables));
//        select
        stmt = connection.createStatement();
        rs = stmt.executeQuery("SELECT * FROM public.user WHERE id > 10");
        while (rs.next()) {
            System.out.println("id:" + rs.getString("id"));
            System.out.println("name:" + rs.getString("name"));
            System.out.println("password:" + rs.getString("password"));
            System.out.println("----------------------");
        }
        rs.close();
        stmt.close();
//        delete
        stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM public.user " +
                "WHERE id > 10 AND id < 100");
        stmt.close();

//        update
        /*stmt = connection.createStatement();
        String sql = "UPDATE Registration " +
                "SET age = 30 WHERE id in (100, 101)";
        stmt.executeUpdate(sql);
        stmt.close();*/
        PreparedStatement pst = connection.prepareStatement("UPDATE public.user SET password = ? WHERE id > 3");
        pst.setString(1, "password_" + new Random().nextInt());
        pst.executeUpdate();
        pst.close();
    }
}
