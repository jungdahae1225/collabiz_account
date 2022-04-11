package com.example.collabiz_account;

import com.example.collabiz_account.dtos.AccountDto;
import com.example.collabiz_account.dtos.AccountResponseDto;
import com.example.collabiz_account.dtos.EmailDto;
import com.example.collabiz_account.errors.ErrorResource;
import com.example.collabiz_account.validator.SignUpFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final SignUpFormValidator validator;

    /**
     * 콜라비즈 적용.
     * 1. 이메일인증 버튼 클릭 -> @PostMapping("/emailVerification")으로 들어온다. 이메일 주소가 post 들어와야 한다.
     * 2. EmailDto에 넘어온 이메일 값 담는다.
     * 3. accountRepository.findById(account.getId());이걸로 이미 있는 이메일인가 확인 한다.
     * 4. 그 다음 버라벨의  @PostMapping("/emailCheckToken") 메서드를 여기서 써서 이메일 토큰을 보낸다. -> 이메일 인증번호 전송 완료
     * 5. 프론트에서 받은 토큰의 번호 넘겨줌
     * 6. 넘어온 토큰이 일치 하는지 확인
     * */

    @ResponseBody
    @GetMapping("/post")
    public String de(){
        return "test ok";
    }

    //create 220411 dahae @부분 고쳐주기
    //이메일을 읿력하고 이메일 인증 버튼을 누르면 실행되는 메서드
    @PostMapping("/emailCheck")
    @ResponseBody
    //ResponseEntity
    public String emailCheck(@RequestBody @Valid EmailDto emailDto){
        //1.이미 있는 이메일인가 확인 if문 통과 하면 중복 없는 것
        if (accountService.checkEmailDuplicate(emailDto.getEmail())) {
            //return ResponseEntity.badRequest().build(); //@@중복 Response로 바꿔주기-프론트랑 논의
        }

        EEmail email = accountService.processEmailDtoTOEEmail(emailDto);//EmailDto를 EEmail 엔티티로 매핑(함수 이름 아직 안바꿈)
        accountService.sendEmailCheckToken(email); //이거 하면 이제 이메일 날라감

        //return ResponseEntity.ok().build(); //이메일 잘 보냈으면 ok 프론트로
        System.out.println("emailCheck ok");
        return "emailCheck ok";
    }

    //generate new emailCheckToken & re-send token
    //이제 사용자가 이메일로 받은 토큰을 홈페이지에 맞게 썼는가 검증하는 로직
    //@RequestParam String token으로 사용자가 적은 토큰(인증번호) 프론트에서 넘겨준다.

    @PostMapping("/emailVerification")
    public ResponseEntity emailVerification(@CurrentUser EEmail email, @RequestParam String token){

        //현재 인증 받고 있는 유저를 저장 하여 들고다닌다. @CurrentUser를 사용해서 가지고 온다.
        AccountResponseDto dto = accountService.emailVerification(email, token);
        if(dto == null){
            return ResponseEntity.badRequest().build();
        } // 인증번호 맞지 않음

        return ResponseEntity.ok(dto); //인증 번호 맞으면
    }

    @PostMapping("/signUp") //이메일 인증 완료 후 회원가입 완료 버튼
    public ResponseEntity signUp(@RequestBody @Valid AccountDto accountDto, Errors errors) {
        if (errors.hasErrors()) {
            EntityModel<Errors> jsr303error = ErrorResource.modelOf(errors);
            return ResponseEntity.badRequest().body(jsr303error);
        }
        validator.validate(accountDto, errors);
        if (errors.hasErrors()) {
            EntityModel<Errors> customError = ErrorResource.modelOf(errors);
            return ResponseEntity.badRequest().body(customError);
        }
        Account account = accountService.saveNewAccount(accountDto); //회원가입(accountRepository.save())
        EntityModel<Account> accountResource = AccountResource.modelOf(account);
        return ResponseEntity.ok(accountResource);
    }

}