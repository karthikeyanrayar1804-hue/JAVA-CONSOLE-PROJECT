package com.busrevervation.com;

import java.sql.*;
import java.util.Scanner;

public class Admin {

    private Scanner sc;
    private Dbconnect db = new Dbconnect();
    
    public Admin(Scanner sc) {
    	this.sc=sc;
    }

    public void adminMenu() {
        boolean run = true;

        while (run) {

            System.out.println("\n------ ADMIN MENU ------");
            System.out.println("1. Add New Bus");
            System.out.println("2. View Bus Details");
            System.out.println("3. Update Bus Details");
            System.out.println("4. Delete Bus Details");
            System.out.println("5. View All Users");
            System.out.println("6. View All Bookings");
            System.out.println("7. Logout");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            try {
                Connection con = db.getConnection();

                switch (choice) {


                    case 1:
                        System.out.print("Bus Name: ");
                        String name = sc.next();

                        System.out.print("Source: ");
                        String src = sc.next();

                        System.out.print("Destination: ");
                        String dest = sc.next();

                        System.out.print("Seats: ");
                        int seats = sc.nextInt();

                        String insert ="INSERT INTO bus(bus_name, source, destination, seats) VALUES (?, ?, ?, ?)";
                        PreparedStatement ps1 = con.prepareStatement(insert);
                        ps1.setString(1, name);
                        ps1.setString(2, src);
                        ps1.setString(3, dest);
                        ps1.setInt(4, seats);
                        ps1.executeUpdate();

                        System.out.println("Bus added successfully ");
                        break;
                   
                    case 2:
                    	
                    	String view = "SELECT * FROM bus";
                        PreparedStatement psView = con.prepareStatement(view);
                        ResultSet rs = psView.executeQuery();

                        System.out.println("\n--- BUS DETAILS ---");
                        System.out.println("ID | Name | Source | Destination | Seats");
                        System.out.println("-------------------------------------------");

                        while (rs.next()) {
                            System.out.println(
                                    rs.getInt("bus_id") + " | " +
                                    rs.getString("bus_name") + " | " +
                                    rs.getString("source") + " | " +
                                    rs.getString("destination") + " | " +
                                    rs.getInt("seats")
                            );
                        }

                        break;
                   
               
                    case 3:
                    	
                        System.out.print("Bus ID: ");
                        int bid = sc.nextInt();

                        System.out.print("New Seats: ");
                        int newSeats = sc.nextInt();

                        String update = "UPDATE bus SET seats=? WHERE bus_id=?";
                        PreparedStatement ps2 = con.prepareStatement(update);
                        ps2.setInt(1, newSeats);
                        ps2.setInt(2, bid);

                        int rows = ps2.executeUpdate();
                        System.out.println(rows > 0 ? "Bus updated " : "Bus not found ");
                        break;

                    case 4:
                        System.out.print("Bus ID to delete: ");
                        int delId = sc.nextInt();

                        String delete = "DELETE FROM bus WHERE bus_id=?";
                        PreparedStatement ps3 = con.prepareStatement(delete);
                        ps3.setInt(1, delId);

                        int delRows = ps3.executeUpdate();
                        System.out.println(delRows > 0 ? "Bus deleted " : "Bus not found ");
                        break;

                    case 5:
                        String user = "SELECT * FROM users";
                        PreparedStatement ps4 = con.prepareStatement(user);
                        ResultSet rs1 = ps4.executeQuery();

                        System.out.println("\n--- USERS ---");
                        while (rs1.next()) {
                            System.out.println(
                                    rs1.getInt("user_id") + " | " +
                                    rs1.getString("name") + " | " +
                                    rs1.getString("email")
                            );
                        }
                        break;

                    case 6:
                        String book = "SELECT * FROM bookings";
                        PreparedStatement ps5 = con.prepareStatement(book);
                        ResultSet rs2 = ps5.executeQuery();

                        System.out.println("\n--- BOOKINGS ---");
                        while (rs2.next()) {
                            System.out.println(rs2.getInt("booking_id") + " | " +rs2.getInt("user_id") + " | " +rs2.getInt("bus_id") );
                        }
                        break;

                    case 7:
                        System.out.println("Admin logged out ");
                        run = false;
                        break;

                    default:
                        System.out.println("Invalid option ");
                }

            } 
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
