package org.payLate.services;

import org.payLate.entity.UserAccount;
import org.payLate.repository.UserAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService {
    private final UserAccountRepository userAccountRepository;

    public UserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public UserAccount findOrCreateByOktaUserId(String oktaUserId) {
        return userAccountRepository.findByOktaUserId(oktaUserId)
                .orElseGet(() -> {
                    UserAccount newUser = new UserAccount();
                    newUser.setOktaUserId(oktaUserId);
                    return userAccountRepository.save(newUser);
                });
    }

    public UserAccount saveOrUpdate(UserAccount userAccount) {

        UserAccount existing = userAccountRepository.findByOktaUserId(userAccount.getOktaUserId())
                .orElse(new UserAccount());
        existing.setOktaUserId(userAccount.getOktaUserId());
        existing.setFirstName(userAccount.getFirstName());
        existing.setLastName(userAccount.getLastName());
        existing.setMiddleName(userAccount.getMiddleName());
        existing.setCity(userAccount.getCity());
        existing.setZipCode(userAccount.getZipCode());
        existing.setNickName(userAccount.getNickName());
        existing.setMobilePhone(userAccount.getMobilePhone());
        existing.setPostalAddress(userAccount.getPostalAddress());
        return userAccountRepository.save(existing);
    }
}