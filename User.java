package com.busrevervation.com;

import java.sql.*;
import java.util.Scanner;

public class User {

    private Scanner sc;
    private Dbconnect db = new Dbconnect();
    private int loggedUserId;

    public User(Scanner sc) {
        this.sc = sc;
    }

    public void usermenu() {

        while (true) {
            System.out.println("\n------ USER MENU ---------");
            System.out.println("1. View Available Buses");
            System.out.println("2. Search Bus (source-destination)");
            System.out.println("3. Book Tickets");
            System.out.println("4. View Booking History");
            System.out.println("5. Cancel Booking");
            System.out.println("6. Logout");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    viewBuses();
                    break;
                case 2:
                    searchBus();
                    break;
                case 3:
                    bookTicket();
                    break;
                case 4:
                    viewBookings();
                    break;
                case 5:
                    cancelBooking();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    public void loginUser() {

        try (Connection con = db.getConnection()) {

            System.out.print("Enter Username: ");
            String name = sc.next();
            System.out.print("Enter Password: ");
            String password = sc.next();

            String sql = "SELECT * FROM users WHERE name=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                loggedUserId = rs.getInt("user_id");
                System.out.println("Login Successful!");
                usermenu();
            } else {
                System.out.println("Invalid Credentials!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewBuses() {
        try (Connection con = db.getConnection()) {

            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM bus");

            while (rs.next()) {
                System.out.println("Bus ID: " + rs.getInt("bus_id")
                        + " | Name: " + rs.getString("bus_name")
                        + " | From: " + rs.getString("source")
                        + " | To: " + rs.getString("destination")
                        + " | Seats: " + rs.getInt("seats"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchBus() {
        try (Connection con = db.getConnection()) {

            System.out.print("Enter Source: ");
            String source = sc.next();

            System.out.print("Enter Destination: ");
            String destination = sc.next();

            String sql = "SELECT * FROM bus WHERE source=? AND destination=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, source);
            ps.setString(2, destination);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("Bus ID: " + rs.getInt("bus_id")
                        + " | Name: " + rs.getString("bus_name")
                        + " | Seats: " + rs.getInt("seats"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bookTicket() {
        try (Connection con = db.getConnection()) {

            System.out.print("Enter Bus ID: ");
            int busId = sc.nextInt();

            System.out.print("Enter Number of Seats: ");
            int seatsToBook = sc.nextInt();

            if (seatsToBook <= 0) {
                System.out.println("Invalid number of seats!");
                return;
            }

            String busSql = "SELECT seats FROM bus WHERE bus_id=?";
            PreparedStatement busPs = con.prepareStatement(busSql);
            busPs.setInt(1, busId);
            ResultSet rs = busPs.executeQuery();

            if (!rs.next()) {
                System.out.println("Invalid Bus ID!");
                return;
            }

            int availableSeats = rs.getInt("seats");

            if (availableSeats < seatsToBook) {
                System.out.println("Not enough seats available!");
                return;
            }


            String bookSql = "INSERT INTO bookings(user_id, bus_id, seats_booked) VALUES (?, ?, ?)";
            PreparedStatement bookStmt = con.prepareStatement(bookSql);
            bookStmt.setInt(1, loggedUserId);
            bookStmt.setInt(2, busId);
            bookStmt.setInt(3, seatsToBook);
            bookStmt.executeUpdate();


            String updateSql = "UPDATE bus SET seats = seats - ? WHERE bus_id = ?";
            PreparedStatement updateStmt = con.prepareStatement(updateSql);
            updateStmt.setInt(1, seatsToBook);
            updateStmt.setInt(2, busId);
            updateStmt.executeUpdate();

            System.out.println("Booking Successful!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewBookings() {
        try (Connection con = db.getConnection()) {

            String sql = "SELECT * FROM bookings WHERE user_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, loggedUserId);

            ResultSet rs = ps.executeQuery();

            boolean hasBooking = false;

            System.out.println("\n------ Your Bookings ------");
            while (rs.next()) {
                hasBooking = true;

                int bookingId = rs.getInt("booking_id");
                int busId = rs.getInt("bus_id");
                int seatsBooked = rs.getInt("seats_booked");
                String bookingDate = rs.getString("booking_date");

                if (bookingDate == null) {
                    bookingDate = "N/A";
                }

                System.out.println("Booking ID: " + bookingId
                        + " | Bus ID: " + busId
                        + " | Seats Booked: " + seatsBooked
                        + " | Booking Date: " + bookingDate);
            }

            if (!hasBooking) {
                System.out.println("No bookings found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelBooking() {
        try (Connection con = db.getConnection()) {

            System.out.print("Enter Booking ID to cancel: ");
            int bookingId = sc.nextInt();

            String getSql = "SELECT bus_id, seats_booked FROM bookings WHERE booking_id = ? AND user_id = ?";
            PreparedStatement getPs = con.prepareStatement(getSql);
            getPs.setInt(1, bookingId);
            getPs.setInt(2, loggedUserId);

            ResultSet rs = getPs.executeQuery();

            if (rs.next()) {
                int busId = rs.getInt("bus_id");
                int seats = rs.getInt("seats_booked");

          
                String deleteSql = "DELETE FROM bookings WHERE booking_id = ?";
                PreparedStatement deletePs = con.prepareStatement(deleteSql);
                deletePs.setInt(1, bookingId);
                deletePs.executeUpdate();

                String updateSql = "UPDATE bus SET seats = seats + ? WHERE bus_id = ?";
                PreparedStatement updatePs = con.prepareStatement(updateSql);
                updatePs.setInt(1, seats);
                updatePs.setInt(2, busId);
                updatePs.executeUpdate();

                System.out.println("Booking Cancelled Successfully!");
            } else {
                System.out.println("Invalid Booking ID!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}