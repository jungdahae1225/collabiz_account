package com.example.collabiz_account.event;

import com.example.collabiz_account.Account;
import com.example.collabiz_account.EEmail;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignUpConfirmEvent {
    private final EEmail email;

}
