package org.payLate.controllers;

import org.payLate.entity.UserAccount;
import org.payLate.services.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/user-account")
public class UserAccountController {
    private final UserAccountService userAccountService;

    @Autowired
    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @GetMapping("/{oktaUserId}")
    public ResponseEntity<UserAccount> getUserAccount(@PathVariable("oktaUserId") String oktaUserId) {
        UserAccount user = userAccountService.findOrCreateByOktaUserId(oktaUserId);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserAccount> saveUserAccount(@RequestBody UserAccount userAccount) {
        UserAccount saved = userAccountService.saveOrUpdate(userAccount);
        return ResponseEntity.ok(saved);
    }
}