package Service;

import static org.mockito.ArgumentMatchers.eq;

import java.util.List;

import Model.Message;

import DAO.MessageDAO;
import DAO.AccountDAO;

public class MessageService {

    MessageDAO messageDAO;
    AccountDAO accountDAO;

    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    // Get all messages
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

        // Get all messages by account id
        public List<Message> getAllMessagesFor(int accountID) {
            return messageDAO.getAllMessagesByAccountId(accountID);
        }

    // Get a message by id
    public Message getMessage(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    // Create (post) a new message
    public Message postMessage(Message message) {

        // Sanitize input
        if (!ValidateMessage(message))
            return null;

        // Check posting user exists
        if (accountDAO.getAccountById(message.posted_by) == null)
            return null;

        // Insert new message
        var newMessage = messageDAO.insertMessage(message);

        // Return new message
        return newMessage;
    }

    // Update a message using a message id and a new message
    public Message updateMessage(int messageId, Message message) {

        // Sanitize input
        if (!ValidateMessage(message))
            return null;

        // Check message exists
        var currentMessage = messageDAO.getMessageById(messageId);
        if (currentMessage == null)
            return null;

        // Update message
        messageDAO.updateMessage(messageId, message);

        // Return updated message
        currentMessage.setMessage_text(message.getMessage_text());
        return currentMessage;
    }

    // Delete a message using a message id
    public Message deleteMessage(int messageId) {

        // Check message exists
        var message = messageDAO.getMessageById(messageId);
        if (message == null)
            return null;

        // Delete message
        messageDAO.deleteMessagebyId(messageId);

        // Return deleted message
        return message;
    }

    //
    static private boolean ValidateMessage(Message message) {
        var messageLength = message.getMessage_text().length();
        if (messageLength == 0 || messageLength > 255)
            return false;
        return true;
    }
}