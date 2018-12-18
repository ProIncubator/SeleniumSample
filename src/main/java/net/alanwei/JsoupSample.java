package net.alanwei;

import net.alanwei.models.BaseResponse;
import net.alanwei.models.CreateOrderResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.HashMap;
import java.util.Map;

public class JsoupSample {
    private final static String HOME_DIR = System.getProperty("user.dir");
    private final static String SEPERATOR = System.getProperty("file.separator");
    private final static String MOBILE = "13695589826";

    public static void main(String[] args) {

        Shop10086Service shop10086 = new Retrofit.Builder()
                .baseUrl("https://shop.10086.cn/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(Shop10086Service.class);


        PayShop10086Service payShop10086 = new Retrofit.Builder()
                .baseUrl("https://pay.shop.10086.cn/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(PayShop10086Service.class);

        shop10086.fetchOrder(MOBILE, 30)
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

                    String referer = response.raw().request().url().toString();
                    return shop10086.createOrder(referer, MOBILE, 221, reqData);
                })
                .switchMap(response -> {
                    BaseResponse<CreateOrderResponse> tempResponse = response.body();
                    Document doc = Jsoup.connect(tempResponse.getData().getPayUrl()).get();
                    Elements formInputs = doc.select("#payment input");
                    Map<String, String> values = new HashMap<>();
                    formInputs.forEach(ele -> {
                        values.put(ele.attr("name"), ele.attr("value"));
                    });
                    values.put("bankAbb", "ALIPAY");
                    values.put("ConfirmPay.x", "62");
                    values.put("ConfirmPay.y", "8");

                    String referer = tempResponse.getData().getPayUrl();
                    return payShop10086.pay(referer, values);
                })
                .subscribe(response -> {
                    String data = response.body().string();
                    System.out.println(data);
                });

    }
}
