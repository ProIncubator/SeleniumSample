package net.alanwei.mobile.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse<T> {
    private String retCode;
    private String retMsg;
    private String sOperTime;
    private T data;
}
