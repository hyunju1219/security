package com.study.SpringSecurity.Dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RespJwtDto {
    private String accessToken;
    private String refreshToken; //지금은 안씀
}
