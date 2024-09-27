package com.example.service;
import com.example.entity.Account;
import com.example.exception.BadRequestException;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service

public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    // The registration will be successful if and only if the username is not blank, the password is at least 4 characters long, 
//and an Account with that username does not already exist.
// If all these conditions are met, the response body should contain a JSON of the Account, including its accountId. The response status should be 200 OK, which is the default. The new account should be persisted to the database.
//  - If the registration is not successful due to a duplicate username, the response status should be 409. (Conflict)
//   - If the registration is not successful for some other reason, the response status should be 400. (Client error)

         public Account register(Account newAccount) {
        // Check if the username is not blank
        if (newAccount.getUsername() == null || newAccount.getUsername().trim().isEmpty()) {
            throw new BadRequestException("Username cannot be blank.");
        }

        // Check if the password is at least 4 characters long
        if (newAccount.getPassword() == null || newAccount.getPassword().length() < 4) {
            throw new BadRequestException("Password must be at least 4 characters.");
        }
         // check if the account with same username already exists
        
          // Directly check if an account with the same username already exists and throw an exception
        accountRepository.findByUsername(newAccount.getUsername())
        .ifPresent(existingAccount -> {
            throw new BadRequestException("Username already exists.");
        });      

    // If all conditions are met, save the new account to the repository (database)
     return accountRepository.save(newAccount);
         }
         

         public Account login(String username, String password) {

            //Fetch account by username
            Account account = accountRepository.findByUsername(username)
                   .orElseThrow(() -> new BadRequestException("Invalid username or password"));
          //check if the password matches

          if(!account.getPassword().equals(password)) {
            throw new BadRequestException("Invalid username or password.");
          }

           // Return the account if login is successful
        return account;
         }
 }
