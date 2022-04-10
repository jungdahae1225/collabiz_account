package com.example.collabiz_account.event;

import com.example.collabiz_account.Account;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignUpConfirmEvent {
    private final Account account;

}
