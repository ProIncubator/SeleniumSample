package net.alanwei.mobile.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderResponse {
    private String orderId;
    private String serialNo;
    private String payTime;
    private String amount;
    private String chargeMoney;
    private String payUrl;
    private String payWay;
    private String accountType;
    private String associatedNum;
}
