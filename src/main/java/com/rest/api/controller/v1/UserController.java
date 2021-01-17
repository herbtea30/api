package com.rest.api.controller.v1;

import com.rest.api.advice.exception.CUserNotFoundException;
import com.rest.api.entity.User;
import com.rest.api.model.response.CommonResult;
import com.rest.api.model.response.SingleResult;
import com.rest.api.repo.UserJpaRepo;
import com.rest.api.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tags(value = {})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class UserController {
    private final UserJpaRepo userJpaRepo;
    private final ResponseService responseService;


    @Schema(name = "회원리스트 조회", description = "모든 회원을 조회한다")
    @GetMapping(value = "/user")
    public List<User> findAllUser() {
        return userJpaRepo.findAll();
    }

    @Operation(summary = "Get a User by msrl")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Found a User", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "404", description = "User Not Found", content = @Content)
    })
    @GetMapping(value = "/user/{msrl}")
    public SingleResult<User> findUserById(@Parameter(name = "msrl", description = "회원번호", required = true) @PathVariable long msrl,
                                           @Parameter(name = "lang", description = "언어", example = "ko") @RequestParam String lang) throws Exception {
        return responseService.getSingleResult(userJpaRepo.findById(msrl).orElseThrow(CUserNotFoundException::new));
    }

    @Schema(name = "회원 입력", description = "회원을 입력한다")
    @PostMapping(value = "/user")
    public SingleResult<User> save(@Parameter(name = "uid", description = "회원아이디", required = true) @RequestParam String uid,
                                   @Parameter(name = "name", description = "회원이름", required = true) @RequestParam String name ) {
        User user = User.builder()
                .uid(uid)
                .name(name)
                .build();
        return responseService.getSingleResult(userJpaRepo.save(user));
    }

    @Schema(name = "회원 수정", description = "회원정보를 수정한다")
    @PutMapping(value = "/user")
    public SingleResult<User> modify(
            @Parameter(name = "msrl", description = "회원번호", required = true) @RequestParam long msrl,
            @Parameter(name = "uid",  description = "회원아이디", required = true) @RequestParam String uid,
            @Parameter(name = "name", description = "회원명", required = true) @RequestParam String name){
        User user = User.builder()
                .msrl(msrl)
                .uid(uid)
                .name(name)
                .build();
        return responseService.getSingleResult(userJpaRepo.save(user));
    }

    @Schema(name = "회원 삭제", description = "userId로 회원정보를 삭제한다")
    @DeleteMapping("/user/{msrl}")
    public CommonResult delete(
            @Parameter(name = "msrl", description = "회원번호", required = true) @PathVariable long msrl) {
        userJpaRepo.deleteById(msrl);
        return responseService.getSucessResult();
    }
}
