package net.alanwei;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class JsoupSample {
    private final static String HOME_DIR = System.getProperty("user.dir");
    private final static String SEPERATOR = System.getProperty("file.separator");

    public static void main(String[] args) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://shop.10086.cn/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        Shop10086Service service = retrofit.create(Shop10086Service.class);
        service.fetchOrder("13695589826", 30)
                .switchMap(response -> {
                    String data = response.body().string();
                    Map<String, Object> reqData = new HashMap<>();
                    reqData.put("channel", "00");
                    reqData.put("amount", 29.94);
                    reqData.put("chargeMoney", 30);
                    reqData.put("choseMoney", 30);
                    reqData.put("operateId", 1552);
                    reqData.put("homeProv", "551");
                    reqData.put("source", "");
                    reqData.put("numFlag", "0");

                    return service.createOrder("https://shop.10086.cn/i/?f=rechargecredit&mobileNo=13695589826&amount=30", "13695589826", 221, reqData);
                })
                .map(response -> response.body())
                .subscribe(body -> {
                    String data = body.string();
                    System.out.println(data);
                });

    }
}
