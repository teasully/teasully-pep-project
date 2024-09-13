package DAO;

import Util.ConnectionUtil;

import java.util.List;
import java.util.ArrayList;
import java.sql.*;

import Model.Message;

public class MessageDAO {

    // Get all messages
    public List<Message> getAllMessages() {
        var messages = new ArrayList<Message>();
        var connection = ConnectionUtil.getConnection();
        try {
            var sql = "SELECT * FROM message";
            var preparedStatement = connection.prepareStatement(sql);

            var rs = preparedStatement.executeQuery();
            while (rs.next()) {
                messages.add(new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }

    // Get all messages for account id
    public List<Message> getAllMessagesByAccountId(int accountId) {
        var messages = new ArrayList<Message>();
        var connection = ConnectionUtil.getConnection();
        try {
            var sql = "SELECT * FROM message WHERE posted_by = ?";
            var preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, accountId);

            var rs = preparedStatement.executeQuery();
            while (rs.next()) {
                messages.add(new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }

    // Get message by id
    public Message getMessageById(int messageId) {
        var connection = ConnectionUtil.getConnection();
        try {
            var sql = "SELECT * FROM message WHERE message_id = ?";
            var preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, messageId);

            var rs = preparedStatement.executeQuery();
            while (rs.next()) {
                return new Message(messageId, rs.getInt("posted_by"), rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    // Add new message
    public Message insertMessage(Message message) {
        var connection = ConnectionUtil.getConnection();
        try {
            var sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            var pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                var generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(),
                        message.getTime_posted_epoch());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    // Update a message's text using a messageId
    public void updateMessage(int messageId, Message message) {
        var connection = ConnectionUtil.getConnection();
        try {
            var sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            var preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, message.getMessage_text());
            preparedStatement.setInt(2, messageId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Delete message by id
    public void deleteMessagebyId(int messageId) {
        var connection = ConnectionUtil.getConnection();
        try {
            var sql = "DELETE FROM message WHERE message_id = ?";
            var preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, messageId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
