package com.example.assignmentexam;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class EmployeeDAO {

//    LINK JDBC DB
    private String jdbcURL = "";

//    USER NAME
    private String jdbcUsername = "";
//    PASSWORD
    private String jdbcPassword = "";

    private static final String INSERT_EMPLOYEE_SQL = "INSERT INTO employees" + "  (full_name, birthday, address, position, department) VALUES " +
            " (?, ?, ?,? ,?);";

    private static final String SELECT_ALL_EMPLOYEE = "select * from employees";

    public EmployeeDAO() {}

    protected Connection getConnection() {
        Connection connection = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void insertEmployee(Employee employee) throws SQLException {
        System.out.println(INSERT_EMPLOYEE_SQL);
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_EMPLOYEE_SQL)) {
            preparedStatement.setString(1, employee.getFull_name());
            preparedStatement.setString(2, employee.getBirthday());
            preparedStatement.setString(3, employee.getAddress());
            preparedStatement.setString(4, employee.getPosition());
            preparedStatement.setString(5, employee.getDepartment());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }



    public List <Employee> selectAllEmployees() {
        List <Employee> employees = new ArrayList <> ();
        // Step 1: Establishing a Connection
        try (Connection connection = getConnection();

             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_EMPLOYEE);) {
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                int id = rs.getInt("id");
                String full_name = rs.getString("full_name");
                String birthday = rs.getString("birthday");
                String address = rs.getString("address");
                String position = rs.getString("position");
                String department = rs.getString("department");
                employees.add(new Employee(id, full_name, birthday, address,position,department));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return employees;
    }





    private void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}