package Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Model.Account;
import Model.Message;

import DAO.AccountDAO;
import DAO.MessageDAO;

import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {

        // Inject dependancies
        var accountDAO = new AccountDAO();
        var messageDAO = new MessageDAO();

        accountService = new AccountService(accountDAO);
        messageService = new MessageService(messageDAO, accountDAO);
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    public Javalin startAPI() {

        // Start app
        Javalin app = Javalin.create();

        // Register endpoints
        app.post("/register", this::register);
        app.post("/login", this::login);

        app.post("/messages", this::postMessage);
        app.get("/messages", this::getMessages);

        app.get("/messages/{message_id}", this::getMessage);
        app.delete("/messages/{message_id}", this::deleteMessage);
        app.patch("/messages/{message_id}", this::updateMessage);

        app.get("/accounts/{account_id}/messages", this::getMessagesForAccount);

        return app;
    }

    // Register endpoint
    private void register(Context ctx) {

        // Gather parameters
        var account = getAccountFromContext(ctx);
        if (account == null) {
            ctx.status(400);
            return;
        }

        // Register new user
        account = accountService.registerAccount(account);
        if (account == null)
            ctx.status(400);
        else
            ctx.json(account);
    }

    // Login endpoint
    private void login(Context ctx) {

        // Gather parameters
        var account = getAccountFromContext(ctx);
        if (account == null) {
            ctx.status(400);
            return;
        }

        // Register new user
        account = accountService.authenticateAccount(account);
        if (account == null)
            ctx.status(401);
        else
            ctx.json(account);
    }

    // Gather an account object via context object
    private Account getAccountFromContext(Context ctx) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(ctx.body(), Account.class);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    // Get all messages
    private void getMessages(Context ctx) {

        // Return all messages
        var messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    // Get all messages for an account
    private void getMessagesForAccount(Context ctx) {

        // Gather parameters
        var accountId = Integer.valueOf(ctx.pathParam("account_id"));

        // Return all messages
        var messages = messageService.getAllMessagesFor(accountId);
        ctx.json(messages);
    }

    // Get a message by Id
    private void getMessage(Context ctx) {

        // Gather parameters
        var messageId = Integer.valueOf(ctx.pathParam("message_id"));

        // Get message
        var message = messageService.getMessage(messageId);

        // Return message
        if (message == null)
            ctx.json("");
        else
            ctx.json(message);
    }

    // Post a message
    private void postMessage(Context ctx) {

        // Gather parameters
        var message = getMessageFromContext(ctx);
        if (message == null) {
            ctx.status(400);
            return;
        }

        // Submit a new message
        message = messageService.postMessage(message);
        if (message == null)
            ctx.status(400);
        else
            ctx.json(message);
    }

    // Update message
    private void updateMessage(Context ctx) {

        // Gather parameters
        var messageId = Integer.valueOf(ctx.pathParam("message_id"));
        var message = getMessageFromContext(ctx);
        if (message == null) {
            ctx.status(400);
            return;
        }

        // Update message
        message = messageService.updateMessage(messageId, message);
        if (message == null)
            ctx.status(400);
        else
            ctx.json(message);
    }

    // Delete a message
    private void deleteMessage(Context ctx) {

        // Gather parameters
        var messageId = Integer.valueOf(ctx.pathParam("message_id"));

        // Delete message
        var message = messageService.deleteMessage(messageId);

        // Return deleted message
        if (message == null)
            ctx.json("");
        else
            ctx.json(message);
    }

    // Gather an account object via context object
    private Message getMessageFromContext(Context ctx) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(ctx.body(), Message.class);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

}