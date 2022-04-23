package com.example.collabiz_account.dtos;

import com.example.collabiz_account.EEmail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDto {
    //@Email
    @NotBlank
    private String email;

    public EEmail toEntity() {
        return EEmail.builder()
                .email(email)
                .build();
    }
}
