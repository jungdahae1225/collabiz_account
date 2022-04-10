package com.example.collabiz_account;

import com.example.collabiz_account.dtos.AccountDto;
import com.example.collabiz_account.dtos.AccountResponseDto;
import com.example.collabiz_account.event.SignUpConfirmEvent;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final ApplicationEventPublisher publisher;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    // signUp

    public Account processNewAccount(@Valid AccountDto accountDto) {
        Account account = saveNewAccount(accountDto);
        sendSignUpConfirmEmail(account);
        return account;
    }
    // save account
    public Account saveNewAccount(AccountDto accountDto) {
        Account map = modelMapper.map(accountDto, Account.class);
        map.setPassword(passwordEncoder.encode(map.getPassword()));
        map.generateEmailCheckToken();
        Account saved = accountRepository.save(map);
        return saved;
    }
    // resend emailCheckToken
    private void sendSignUpConfirmEmail(Account account) {
        publisher.publishEvent(new SignUpConfirmEvent(account));
    }

    public void reSendEmailCheckToken(Account account) {
        Account ac = accountRepository.findById(account.getId()).get();
        ac.generateEmailCheckToken();
        Account saved = accountRepository.save(ac);
        sendSignUpConfirmEmail(saved);
    }

    public AccountResponseDto emailVerification(Account account, String token){
        if (!account.isValidToken(token)) {
            return null;
        }

        completeSignUp(account);
        return createAccountResponseDto(account);
    }

    public void completeSignUp(Account find) {
        find.completeSignUp();
    }

    public AccountResponseDto createAccountResponseDto(Account account){
        AccountResponseDto dto = modelMapper.map(account, AccountResponseDto.class);

        return dto;
    }

    public Account findById(Long id) {
        return accountRepository.findById(id).get();
    }

    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
