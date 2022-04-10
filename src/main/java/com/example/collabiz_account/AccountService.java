package com.example.collabiz_account;

import com.example.collabiz_account.dtos.AccountDto;
import com.example.collabiz_account.dtos.AccountResponseDto;
import com.example.collabiz_account.dtos.EmailDto;
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

    public EEmail processNewAccount(@Valid EmailDto emailDto) { //
        EEmail email = modelMapper.map(emailDto, EEmail.class);
        return email;
    }

    // resend emailCheckToken
    private void sendSignUpConfirmEmail(EEmail email) {
        publisher.publishEvent(new SignUpConfirmEvent(email));
    }

    public void reSendEmailCheckToken(EEmail email) {//Account account
        //우리는 저장 되어 있는 엔티티를 찾아서 다시 토큰 주는게 아니라 아예 처음 만드는 것이므로, 원래 있던 3줄 코드 필요 없다.
        email.generateEmailCheckToken();
        //Account saved = accountRepository.save(ac);
        sendSignUpConfirmEmail(email);
    }

    public AccountResponseDto emailVerification(EEmail email, String token){
        if (!email.isValidToken(token)) {
            return null;
        }

        completeSignUp(email);
        return createAccountResponseDto(email);
    }

    // save account
    public Account saveNewAccount(AccountDto accountDto) {
        Account map = modelMapper.map(accountDto, Account.class);
        map.setPassword(passwordEncoder.encode(map.getPassword()));
        //map.generateEmailCheckToken(); 이메일 토큰 처리는 분리해 주었으니 이제 없어도 된다.
        Account saved = accountRepository.save(map);
        return saved;
    }

    public void completeSignUp(EEmail find) {
        find.completeSignUp();
    }

    public AccountResponseDto createAccountResponseDto(EEmail email){
        AccountResponseDto dto = modelMapper.map(email, AccountResponseDto.class);

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

    //create 220411 dahae
    public boolean checkEmailDuplicate(String email) {
        return accountRepository.existsByEmail(email);
    }
}
