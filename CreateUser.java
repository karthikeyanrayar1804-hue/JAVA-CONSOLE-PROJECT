package com.busrevervation.com;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Scanner;

public class CreateUser {

    public void registerUser(Scanner sc) {

        try {
        	Dbconnect db = new Dbconnect();
            Connection con = db.getConnection();
            
            System.out.print("Enter username: ");
            String name = sc.next();
            System.out.print("Enter password: ");
            String password = sc.next();
            System.out.print("Enter email id:");
            String email=sc.next();

           try {
	            String sql = "INSERT INTO users(name, password,email) VALUES (?, ?,?)";
	            PreparedStatement ps = con.prepareStatement(sql);
	            ps.setString(1, name);
	            ps.setString(2, password);
	            ps.setString(3, email);
	
	            int rows = ps.executeUpdate();
	            if (rows > 0) {
	                System.out.println("User registered successfully ");
	                
	            }
           }catch (SQLIntegrityConstraintViolationException e) {
        	   if (e.getErrorCode() == 1062) {

        	        String message = e.getMessage();

        	        if (message.contains("name")) {
        	            System.out.println("Username already exists. Please choose a different username.");

        	        } else if (message.contains("email")) {
        	            System.out.println("Email already exists. Please use a different email.");

        	        } 
	            }
           }

        } catch (Exception e) {
            e.printStackTrace();
        }
           
    }
}
