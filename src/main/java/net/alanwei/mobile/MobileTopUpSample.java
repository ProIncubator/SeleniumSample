package net.alanwei.mobile;

import io.reactivex.Observable;
import net.alanwei.AlipayQrCode;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

//移动手机号充值例子
public class MobileTopUpSample {
    private final static String MOBILE = "1_3_6_9_5_589_82_6".replaceAll("_", "");

    public static void main(String[] args) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.followRedirects(false);

        Shop10086Service shop10086 = new Retrofit.Builder()
                .baseUrl("https://shop.10086.cn/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build())
                .build()
                .create(Shop10086Service.class);


        PayShop10086Service payShop10086 = new Retrofit.Builder()
                .baseUrl("https://pay.shop.10086.cn/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build())
                .build()
                .create(PayShop10086Service.class);


        Observable<Response<ResponseBody>> mobileResp = shop10086.fetchOrder(MOBILE, 30)
                .switchMap(response -> {
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
                .switchMap(response -> shop10086.getUrl(response.body().getData().getPayUrl()))
                .switchMap(response -> {
                    String html = response.body().string();
                    Document doc = Jsoup.parse(html);
                    Elements formInputs = doc.select("#payment input");
                    Map<String, String> values = new HashMap<>();
                    formInputs.forEach(ele -> {
                        values.put(ele.attr("name"), ele.attr("value"));
                    });

                    Map<String, String> postBody = new HashMap<>();
                    postBody.put("bankAbbr", "ALIPAY");
                    postBody.put("orderId", values.get("orderId"));
                    postBody.put("type", values.get("type"));
                    postBody.put("ipAddress", values.get("ipAddress"));
                    postBody.put("ts", values.get("ts"));
                    postBody.put("hmac", values.get("hmac"));
                    postBody.put("channelId", values.get("channelId"));
                    postBody.put("ConfirmPay.x", "45");
                    postBody.put("ConfirmPay.y", "15");

                    String parameters = postBody.entrySet().stream().map(key -> String.format("%s=%s", key, postBody.get(key))).collect(Collectors.joining("&"));

                    String referer = response.raw().request().url().toString();
                    return payShop10086.pay(referer, postBody);
                })
                .switchMap(response -> {
                    //Request -> mapi.alipay.com
                    String location = response.headers().get("Location");
                    return shop10086.getUrl(location);
                });
        AlipayQrCode.getPayQrCodeUrl(mobileResp);
    }

}
