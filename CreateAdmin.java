package com.busrevervation.com;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Scanner;

public class CreateAdmin {

    public void registerAdmin(Scanner sc) {
        System.out.println("Enter admin key:");
        String admin = sc.next();

        try {
            Dbconnect db = new Dbconnect();
            Connection con = db.getConnection();

            String pass = "SELECT * FROM admin_key WHERE admin_password = ?";
            PreparedStatement ps = con.prepareStatement(pass);
            ps.setString(1, admin);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Admin key verified Successfully");

                boolean registered = false;

                while (!registered) {
                    System.out.print("Enter admin username: ");
                    String username = sc.next();

                    System.out.print("Enter admin password: ");
                    String password = sc.next();

                    try {
                        String sql = "INSERT INTO admin(username, password) VALUES (?, ?)";
                        PreparedStatement ps1 = con.prepareStatement(sql);
                        ps1.setString(1, username);
                        ps1.setString(2, password);

                        int rows = ps1.executeUpdate();

                        if (rows > 0) {
                            System.out.println("Admin registered successfully");
                            registered = true;
                        }

                    } catch (SQLIntegrityConstraintViolationException e) {
                        System.out.println("Username already exists. Please choose a different username.");
                    }
                }

            } else {
                System.out.println("Invalid admin key");
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


