package com.busrevervation.com;

import java.sql.SQLException;
import java.util.Scanner;

public class login {


    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        showLoginMenu();
    }

    public static void showLoginMenu() throws SQLException {
        System.out.println("---------------Bus Reservation System----------------");
        System.out.println("Select any operation:");
        System.out.println("1. User login");
        System.out.println("2. Admin login");
        System.out.println("3. New register");
        System.out.println("4. Exit");
        System.out.print("Enter choice: ");

        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1:
                User user = new User(sc);
                user.loginUser();
                break;

            case 2:
                AdminValidate adminValidate = new AdminValidate(sc);
                adminValidate.loginAdmin();
                break;

            case 3:
                System.out.println("Register as:\n1. User\n2. Admin");
                int registerChoice = sc.nextInt();
                sc.nextLine();

                if (registerChoice == 1) {
                    CreateUser userReg = new CreateUser();
                    userReg.registerUser(sc);
                } else if (registerChoice == 2) {
                    CreateAdmin adminReg = new CreateAdmin();
                    adminReg.registerAdmin(sc);
                } else {
                    System.out.println("Invalid option");
                }
                break;

            case 4:
                System.out.println("Operations exited ");
                sc.close();
                return;

            default:
                System.out.println("Invalid choice ");
        }

        showLoginMenu();
    }
}


