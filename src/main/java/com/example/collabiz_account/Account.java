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
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "mail_id")
    private EEmail email; //어노테이션이랑 겹쳐서 EEmail로 함

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    private String nickname; //유저 회사 이름

    //이하 코드 EEmail로 옮김!!!

}
