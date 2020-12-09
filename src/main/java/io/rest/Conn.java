package io.rest;

import org.springframework.context.annotation.Bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conn {

    public static Connection conn;
    public static Statement statmt;
    public static ResultSet resSet;

    // --------ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ--------
    public Conn(String dbUrl) throws ClassNotFoundException, SQLException
    {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection(dbUrl);

        System.out.println("База " + dbUrl + " подключена!");
    }

    // -------- Вывод таблицы--------
    public static void ReadDB() throws ClassNotFoundException, SQLException
    {
        statmt = conn.createStatement();
        resSet = statmt.executeQuery("SELECT * FROM users");

        while(resSet.next())
        {
            int id = resSet.getInt("id");
            String  name = resSet.getString("name");
            String  phone = resSet.getString("phone");
            System.out.println( "ID = " + id );
            System.out.println( "name = " + name );
            System.out.println( "phone = " + phone );
            System.out.println();
        }

        System.out.println("Таблица выведена");
    }

    // --------Закрытие--------
    public static void CloseDB() throws ClassNotFoundException, SQLException
    {
        conn.close();
        statmt.close();
        resSet.close();

        System.out.println("Соединения закрыты");
    }

    public static Connection getConn() {
        return conn;
    }

}