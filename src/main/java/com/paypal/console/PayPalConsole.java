package com.paypal.console;

import java.util.List;
import java.util.Scanner;

public class PayPalConsole {
    public static Scanner scanner  = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to PayPal");
        System.out.println("Enter command");
        StringBuilder commandList = new StringBuilder(
                    "(C) -> Create new user \n")
                        .append("(L) -> List users \n")
                        .append("(+) -> Cash in\n")
                        .append("(-) -> Cash out\n")
                        .append("(T) -> Transaction\n")
                        .append("(Q) -> Quit\n");


        while(true) {
            System.out.println(commandList.toString());
            String command = scanner.nextLine();
            switch (command) {
                case "C" :
                    createUser();
                    break;

                case "L" :
                    ListUsers();
                    break;

                case "+" :
                    cashIn();
                    break;

                case "-" :
                    cashOut();
                    break;

                case "T" :
                    Transaction();
                    break;

                case "Q" :
                    System.out.println("Bye");
                    return;


            }
        }



    }

    private static void Transaction() {

        int userFrom = getIdFromConsole("Take from");
        int userTo = getIdFromConsole("Give to");
        double amount = getAmountFromConsole();

        DbHelper.transaction(userFrom,userTo,amount);
    }

    private static void cashOut() {

        int userId = getIdFromConsole(null);
        double amount = getAmountFromConsole();

        DbHelper.cashOut(userId,amount);
    }

    private static void ListUsers() {

        List<User> users = DbHelper.listUsers();
        if(users == null) return;

        for(User user : users) {
            System.out.println(user);
        }


     }

    private static void createUser() {
        System.out.println("Please enter first name for user");
        String firstName = scanner.nextLine();
        System.out.println("Please enter last name for user");
        String lastName = scanner.nextLine();

        DbHelper.createUser(firstName,lastName);

    }

    private static void cashIn() {
        int userId = getIdFromConsole(null);
        double amount = getAmountFromConsole();

        DbHelper.cashFlow(userId,amount);
    }


    private static int  getIdFromConsole(String message) {
       if(message != null) System.out.println(message);
        System.out.println("Please enter user's id");
        while (true) {
            {
                int userId;

                try {
                    userId = Integer.parseInt(scanner.nextLine());

                    return userId;
                } catch (Exception e) {
                    System.out.println("Please enter correct arguments");
                }
            }
        }
    }

    private static double getAmountFromConsole() {
        while(true) {
           try {
               System.out.println("Enter amount");
               double amount = Double.parseDouble(scanner.nextLine());
               return  amount;
           } catch (Exception e) {
               System.out.println("Please enter correct arguments");

           }
       }

    }


}
