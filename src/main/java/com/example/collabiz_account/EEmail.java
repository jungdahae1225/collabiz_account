package com.example.collabiz_account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class EEmail {
    @Id
    @GeneratedValue
    @Column(name = "email_id")
    private Long id;

    public EEmail(String email) {
        this.email = email;
    }

    @Email
    @Column(unique = true,nullable = false)
    private String email;

    @OneToOne(mappedBy = "email", fetch = FetchType.LAZY)
    private Account account;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    private boolean emailVerified;

    @JsonIgnore
    private String emailCheckToken;

    //private LocalDateTime emailCheckTokenGeneratedAt;

    public void generateEmailCheckToken() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        uuid = uuid.substring(0, 10);
        this.emailCheckToken = uuid;
        //this.emailCheckTokenGeneratedAt = LocalDateTime.now();
    }

    public boolean isValidToken(String token) {
        return this.emailCheckToken.equals(token);
    }

    public void completeSignUp() {
        this.setEmailVerified(true);
    }

}
