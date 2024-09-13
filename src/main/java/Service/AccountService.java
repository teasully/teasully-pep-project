package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {

    AccountDAO accountDAO;
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    // Register a new account
    public Account registerAccount(Account account) {

        // Sanitize input
        if (account.getUsername().length() == 0)
            return null;
        if (account.getPassword().length() < 4)
            return null;

        // Check user exists
        if (accountDAO.getAccountByUsername(account.getUsername()) != null) {
            return null;
        }

        // Create account
        var newAccount = accountDAO.insertAccount(account);
        System.out.println("inserted new account: " + (newAccount == null));

        // Return new account
        return newAccount;
    }

    // Authenticate account
    public Account authenticateAccount(Account account) {
        return accountDAO.getAccountByUsernameAndPassword(account.getUsername(), account.getPassword());
    }

}