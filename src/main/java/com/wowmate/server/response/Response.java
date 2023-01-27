package com.wowmate.server.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.wowmate.server.response.ResponseStatus.SUCCESS;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class Response <T, E> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String message;
    private final int code;

    //NULL인 값은 뺸다
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data1;

    //NULL인 값은 뺸다
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private E data2;

    //요청 성공
    public Response(T first) {
        this.isSuccess = SUCCESS.isSuccess();
        this.message = SUCCESS.getMessage();
        this.code = SUCCESS.getCode();
        this.data1 = first;
    }

    public Response(T first, E second) {
        this.isSuccess = SUCCESS.isSuccess();
        this.message = SUCCESS.getMessage();
        this.code = SUCCESS.getCode();
        this.data1 = first;
        this.data2 = second;
    }

    //요청 실패
    public Response(ResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.message = status.getMessage();
        this.code = status.getCode();
    }
}
