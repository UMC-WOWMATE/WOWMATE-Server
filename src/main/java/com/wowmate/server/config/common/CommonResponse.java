package com.wowmate.server.config.common;

public enum CommonResponse {

    SUCCESS(200, "SUCCESS!"), FAIL(403, "Fail"), DUPLICATION(500, "이미 가입된 회원입니다.");

    private final int code;
    private final String msg;

    CommonResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


}
