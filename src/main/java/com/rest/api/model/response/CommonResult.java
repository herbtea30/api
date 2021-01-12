package com.rest.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult {

    @Schema(name = "success", type = "boolean", description = "응답 성공여부 : true/false", required = true)
    private boolean success;
    @Schema(name="code", type = "number", description = "응답 코드 번호 : >= 0 정상, < 0 비정상", required = true)
    private int code;
    @Schema(name="msg", type = "String", description = "응답 메세지")
    private String msg;
}
