package net.alanwei;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class JsoupSample {
    private final static String HOME_DIR = System.getProperty("user.dir");
    private final static String SEPERATOR = System.getProperty("file.separator");

    public static void main(String[] args) throws Throwable {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://shop.10086.cn/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        Shop10086Service service = retrofit.create(Shop10086Service.class);
//        service.fetchOrder().subscribe(response -> {
//            Set<String> names = response.headers().names();
//            for (String name : names) {
//                String value = response.headers().get(name);
//                System.out.println(name + ": " + value);
//            }
//        });
        Map<String, Object> reqData = new HashMap<>();
        //{"channel":"00","amount":29.94,"chargeMoney":30,"choseMoney":30,"operateId":1552,"homeProv":"551","source":"","numFlag":"0"}
        reqData.put("channel", "00");
        reqData.put("amount", 29.94);
        reqData.put("chargeMoney", 30);
        reqData.put("choseMoney", 30);
        reqData.put("operateId", 1552);
        reqData.put("homeProv", "551");
        reqData.put("source", "");
        reqData.put("numFlag", "0");

        service.createOrder("https://shop.10086.cn/i/?f=rechargecredit&mobileNo=13695589826&amount=30", reqData).subscribe(response -> {
            String data = response.body().string();
            Files.write(Paths.get(HOME_DIR + SEPERATOR + "response.html"), data.getBytes(StandardCharsets.UTF_8));
            System.out.println(data);
        });

        /**
         * curl -X POST \
         *   'https://shop.10086.cn/i/v1/pay/saveorder/13695589826?provinceId=551' \
         *   -H 'Content-Type: application/json' \
         *   -H 'Postman-Token: b2aff112-1fef-471d-abdb-1d6750392e88' \
         *   -H 'Referer: https://shop.10086.cn/i/?f=rechargecredit&mobileNo=13695589826&amount=30' \
         *   -H 'cache-control: no-cache' \
         *   -d '{"channel":"00","amount":29.94,"chargeMoney":30,"choseMoney":30,"operateId":1552,"homeProv":"551","source":"","numFlag":"0"}'
         */
    }
}
