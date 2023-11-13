package wanted.n.budgetmanager.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSignUpRequestDTO {
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Size(min = 10, max = 20, message = "비밀번호는 10자 이상 20자 이하로 입력해 주세요.")
    private String password;

    @NotBlank(message = "계정을 입력해 주세요.")
    private String account;


    public void setPassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
