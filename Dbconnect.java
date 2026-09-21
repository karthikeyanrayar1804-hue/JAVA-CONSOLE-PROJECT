package com.busrevervation.com;

import java.sql.Connection;
import java.sql.DriverManager;

public class Dbconnect {

    public Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bus_reservation","root","8925263789");
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
}

