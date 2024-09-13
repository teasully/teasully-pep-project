package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {

    // Query account by id
    public Account getAccountById(int accountId) {
        var connection = ConnectionUtil.getConnection();
        try {
            var sql = "SELECT * FROM account WHERE account_id = ?";
            var preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, accountId);

            var rs = preparedStatement.executeQuery();
            while (rs.next()) {
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    // Query account by username
    public Account getAccountByUsername(String username) {
        var connection = ConnectionUtil.getConnection();
        try {
            var sql = "SELECT * FROM account WHERE username = ?";
            var preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);

            var rs = preparedStatement.executeQuery();
            while (rs.next()) {
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    // Query account by username and password
    public Account getAccountByUsernameAndPassword(String username, String password){
        var connection = ConnectionUtil.getConnection();
        try {
            var sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            var preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            var rs = preparedStatement.executeQuery();
            while (rs.next()) {
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    // Add new account
    public Account insertAccount(Account account) {
        var connection = ConnectionUtil.getConnection();
        try {
            var sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();
            var pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                var generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

}