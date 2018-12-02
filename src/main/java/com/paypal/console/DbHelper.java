package com.paypal.console;



import java.sql.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DbHelper {

    private static final Connection connection = getConnection();

    private static Connection getConnection() {
        try {

            //for my DB, pls change this code code when testing
            Connection connection = DriverManager.getConnection("jdbc:mariadb://127.0.0.1/paypal", "root", "9999");

            return connection;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;


    }

    /**
     * creating new user using SQL Connection,
     * @param firstName
     * @param lastName
     * @return
     */
    static int createUser(String firstName, String lastName) {

        String createUserQuery = "insert into users (first_name, last_name) VALUES ( ?, ?);";
        try {
            PreparedStatement statement = connection.prepareStatement(createUserQuery);
            statement.setString(1,firstName);
            statement.setString(2,lastName);
            statement.execute();
            String getIdQuery = "SELECT MAX(id) FROM users";

            statement = connection.prepareStatement(getIdQuery);
            ResultSet resultSet = statement.executeQuery(getIdQuery);

            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Updates the user balance in database
     * Sets balance = balance + amount
     *
     * @param userId id of the user in users table
     * @param amount double value of the amount to insert
     */
    static void cashFlow(int userId, double amount) {

        if(idCheck(userId)) {
            try {
                Statement statement = connection.createStatement();
                String setBalanceQuery = MessageFormat.format(
                        "UPDATE users SET balance = (balance + {0}) where id = {1}; ", amount, userId);
                // System.out.println(setBalanceQuery);
                statement.execute(setBalanceQuery);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else System.out.println("Uncorrect id!");;

    }

    static void cashOut(int userId, double amount) {
        if(idCheck(userId)) {
            if(amount > balanceCheck(userId)) {
                System.out.println("PARTQOV CHENQ TALI!!! XD");
                return;
            }
                try {
                    Statement statement = connection.createStatement();
                    String setBalanceQuery = MessageFormat.format(
                            "UPDATE users SET balance = (balance - {0}) where id = {1}; ", amount, userId);
                    // System.out.println(setBalanceQuery);
                    statement.execute(setBalanceQuery);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        else throw new IllegalArgumentException();
    }

    /**
     * Emulates a transaction between 2 users;
     * Takes money from one account and adds to another account
     * @param userFrom  source user id
     * @param userTo    target user id
     * @param amount    transaction amount
     */
    static void transaction(int userFrom, int userTo, double amount) {
        if(idCheck(userFrom) && idCheck(userTo)) {
            //TODO NISYA CHKA!!! XD :) :D
            //vercnel userFrom ic
            //tal userTO in
            //grel etqan@ transactions um
            if(amount > balanceCheck(userFrom)) {
                System.out.println("PARTQ MNAL CHKA XD");
                return;
            }

            String takeFromQuery = MessageFormat.format(
                    "UPDATE users SET balance = (balance - {0}) where id = {1};",amount,userFrom);
            String giveToQuery = MessageFormat.format (
                    "UPDATE users SET balance = (balance + {0}) where id = {1};",amount,userTo);
            String writeTransactionQuery = MessageFormat.format(
                    "INSERT INTO transactions (user_from,user_to, transaction_amount) VALUES ({0},{1},{2});",userFrom,userTo,amount);
            System.out.println(takeFromQuery);
            System.out.println(giveToQuery);
            System.out.println(writeTransactionQuery);

            try {
                Statement statement = connection.createStatement();
                statement.execute(takeFromQuery);
                statement.execute(giveToQuery);
                statement.execute(writeTransactionQuery);


            } catch (SQLException e){
                e.printStackTrace();
            }

        }
        else System.out.println("Uncorrecd id");;

    }


    /**
     * returns list of all users
     *
     * @return returns ArrayList<User>, that is contain's users' id, firstName,lastName,balance
     */
    static List<User> listUsers() {
        String getUsersQuery = "SELECT * FROM users";

        try {
            Statement statement  = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getUsersQuery);

            List<User>  userList = new ArrayList<User>();

            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                double balance = resultSet.getDouble("balance");

                userList.add(new User
                                (id, firstName,lastName,balance));

            }
            return userList;


        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * checks if id exists, continue operation
     * @param id source id
     * @return
     */
    static boolean idCheck(int id) {

        try {
            Statement statement = connection.createStatement();
            String getIdListQuery = "Select id from users;";
            ResultSet resultSet = statement.executeQuery(getIdListQuery);
            List<Integer> listId = new ArrayList<Integer>();
            while(resultSet.next()) {
                listId.add(resultSet.getInt(1));
            }
            if(listId.contains(id)) return true;
            else return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    static double balanceCheck(int id) {
        if(idCheck(id)) {
            try {
                String getIdListQuery = "SELECT balance FROM users WHERE id = ?; ";
                PreparedStatement statement = connection.prepareStatement(getIdListQuery);
                statement.setInt(1,id);

                ResultSet resultSet = statement.executeQuery(getIdListQuery);
                resultSet.next();
                double balance = resultSet.getDouble(1) ;
                return balance;
            } catch (SQLException e) {
                e.printStackTrace();
                return -1;
            }
        } else return -1;

    }
}
