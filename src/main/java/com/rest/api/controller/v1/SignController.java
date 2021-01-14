package com.rest.api.controller.v1;

import com.rest.api.advice.config.security.JwtTokenProvider;
import com.rest.api.advice.exception.CEmailSigninFailedException;
import com.rest.api.entity.User;
import com.rest.api.model.response.CommonResult;
import com.rest.api.model.response.SingleResult;
import com.rest.api.repo.UserJpaRepo;
import com.rest.api.service.ResponseService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class SignController {

    private final UserJpaRepo userJpaRepo;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final PasswordEncoder passwordEncoder;

    @Schema(name = "로그인", description = "이메일 회원 로그인을 한다.")
    @PostMapping(value = "/signin")
    public SingleResult<String> signin(@Parameter(name = "id", description = "회원ID : 이메일",  required = true) @RequestParam String id,
                                       @Parameter(name = "password", description = "비밀번호", required = true) @RequestParam String password) {
        User user = userJpaRepo.findByUid(id).orElseThrow(CEmailSigninFailedException::new);
        if(!passwordEncoder.matches(password, user.getPassword()))
            throw new CEmailSigninFailedException();

        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles()));
    }

    @Schema(name = "가입", description = "회원가입을 한다.")
    @PostMapping(value = "/signup")
    public CommonResult signin(@Parameter(name="id", description = "회원ID : 이메일", required = true) @RequestParam String id,
                               @Parameter(name="password", description = "비밀번호", required = true) @RequestParam String password,
                               @Parameter(name="name", description = "이름", required = true) @RequestParam String name){
        userJpaRepo.save(User.builder()
            .uid(id)
            .password(passwordEncoder.encode(password))
            .name(name)
            .roles(Collections.singletonList("ROLE_USER"))
            .build());
        return responseService.getSucessResult();
    }
}
