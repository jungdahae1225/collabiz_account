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
public class Account {
    @Id
    @GeneratedValue
    @Column(name = "account_id")
    private Long id;

    @Email //이게 id
    @Column(unique = true,nullable = false)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "mail_id")
    private EEmail email; //어노테이션이랑 겹쳐서 EEmail로 함






















    //private String username; // 유저id

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
