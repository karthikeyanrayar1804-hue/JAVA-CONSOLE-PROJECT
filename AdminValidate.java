package com.busrevervation.com;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class AdminValidate {
	private Scanner sc;
	public AdminValidate(Scanner sc) {
		this.sc=sc;
	}

    public void loginAdmin() {
        try  {
            System.out.print("Enter admin username: ");
            String username = sc.next();

            System.out.print("Enter admin password: ");
            String password = sc.next();

            Dbconnect db = new Dbconnect();
            Connection con = db.getConnection();

            String sql = "SELECT * FROM admin WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Login successful ");
                Admin admin = new Admin(sc);
                admin.adminMenu(); 
            } else {
                System.out.println("Invalid username or password ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}





