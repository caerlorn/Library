package controller;

import model.Book;

import java.sql.*;

public class DatabaseTools {

    private static final String JDBC_DRIVER = "org.h2.Driver";

    private static final String conUrl = "jdbc:h2:mem:library"; // in-memory database
    //private static final String conUrl = "jdbc:h2:~/test"; //if you have h2 installed and want a persistent database

    private static final String user = "sa";
    private static final String pass = "";
    private static Connection conn = null;
    private static String deleteSQL = "delete from books where isbn = ?";
    private static String insertSQL = "insert into books values (?, ?, ?, ?, ?)";
    private static String updateSQL = "UPDATE books SET FloorNumber = ?, ShelfNumber = ?, Available = ? WHERE ISBN = ?";

    //Connect to DB
    public static Connection dbConnect() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(conUrl);
            //conn = DriverManager.getConnection(conUrl, user, pass); //if you change the things previously mentioned
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found" + e);
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console" + e);
            e.printStackTrace();
        }
        return conn;
    }


    //Initial database query function to populate the database
    public static void initialDB() {
        dbConnect();
        try {
            Statement st = conn.createStatement();

            st.execute("create table if not exists books(ISBN int primary key, BookName varchar, " +
                    "FloorNumber int, ShelfNumber int, Available boolean)");

            st.executeUpdate("insert into books (ISBN, BookName, FloorNumber, ShelfNumber, Available ) values " +
                    "(1001, 'Tortoise Of Glory', 2, 23, TRUE),(1002, 'Strangers Of The End', 3, 34, FALSE)," +
                    "(1003, 'Thieves Of Next Year', 1, 13, FALSE),(1004, 'Creators And Butchers', 4, 23, TRUE)," +
                    "(1005, 'Annihilation Without Sin', 2, 26, TRUE),(1006, 'Sounds In The Titans', 1, 67, TRUE)," +
                    "(2001, 'Separated In The Sea', 3, 64, TRUE),(2002, 'Harry Potter', 4, 89, TRUE)," +
                    "(2003, 'Eisenhorn', 2, 98, TRUE),(2004, 'Litany of the Emperor', 1, 68, FALSE)," +
                    "(2005, 'Wolves Unleashed', 1, 90, FALSE),(3001, 'Liber Daemonicum', 3, 3, TRUE)," +
                    "(3002, 'Codex Astartes', 1, 21, TRUE)");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //To get the resultset after the initial database population
    public static ResultSet getInitDB() {
        ResultSet res = null;
        try {
            Statement st = conn.createStatement();
            res = st.executeQuery("select * from books");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    //delete book from database
    public static void deleteBook(int isbn) {

        try {
            PreparedStatement st = conn.prepareStatement(deleteSQL);
            st.setInt(1, isbn);
            int test = st.executeUpdate();
            System.out.println(test + " delete");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //insert book into database
    public static void insertBook(Book book) throws SQLException {
        try {
            PreparedStatement st = conn.prepareStatement(insertSQL);
            st.setInt(1, book.getIsbn());
            st.setString(2, book.getBookName());
            st.setInt(3, book.getFloorNumber());
            st.setInt(4, book.getShelfNumber());
            st.setBoolean(5, book.isAvailability());
            int test = st.executeUpdate();
            System.out.println(test + " insert");
        } catch (SQLException e) {
            throw e;
        }
    }

    public static void updateBookStatus(Book book) throws SQLException {
        try {
            PreparedStatement st = conn.prepareStatement(updateSQL);
            st.setInt(1, book.getFloorNumber());
            st.setInt(2, book.getShelfNumber());
            st.setBoolean(3, book.isAvailability());
            st.setInt(4, book.getIsbn());
            int test = st.executeUpdate();
            System.out.println(test + " update");
        } catch (SQLException e) {
            throw e;
        }

    }

}
