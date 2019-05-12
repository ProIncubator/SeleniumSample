package net.alanwei.telecom;

import com.jayway.jsonpath.JsonPath;
import io.reactivex.Observable;
import net.alanwei.AlipayQrCode;
import net.alanwei.CommonRequestFactory;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import retrofit2.Response;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

//电信手机号充值例子
public class TeleComTopUpSample {
    private final static String PHONE_NUMBER = "153_114_379_57".replaceAll("_", ""); //防止爬虫爬手机号

    public static void main(String[] args) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.followRedirects(false);

        CommonRequestFactory.CommonRequest req = CommonRequestFactory.create(null);

        String timeStamp = String.valueOf(System.currentTimeMillis());
        Map<String, String> cookies = getCookies();
        StringBuilder reqCookieValues = new StringBuilder(cookies.entrySet().stream().map(kv -> kv.getKey() + "=" + kv.getValue()).collect(Collectors.joining("; ")));

        Observable<Response<ResponseBody>> telecomResponse = TopUpService.captcha(timeStamp, reqCookieValues.toString()).switchMap(response -> {
            //获取验证码
            String jSessionCookie = response.headers().values("Set-Cookie").get(0).split(";")[0];
            reqCookieValues.append("; " + jSessionCookie);

            byte[] data = response.body().bytes();
            String captchaPath = TeleComTopUpSample.class.getClassLoader().getResource("").getPath() + timeStamp + ".png";
            FileUtils.writeByteArrayToFile(new File(captchaPath), data);

            System.out.println("验证码文件存储在: " + captchaPath + ", 请输入文件中的验证码按回车键继续: ");
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            String captcha = stdin.readLine();

            return TopUpService.doTrade(PHONE_NUMBER, captcha, 50, reqCookieValues.toString());
        }).switchMap(response -> {
            //获取订单号
            String data = new String(response.body().bytes(), StandardCharsets.UTF_8);
            String orderId = JsonPath.read(data, "$.dataObject.orderId");
            return TopUpService.doOrder(orderId, reqCookieValues.toString());
        }).switchMap(response -> {
            String html = new String(response.body().bytes(), StandardCharsets.UTF_8);
            Document doc = Jsoup.parse(html);
            Element reqParams = doc.selectFirst("input[name='request_params']");
            String values = reqParams.val();


            return req.postForm(
                    "http://paygo.189.cn:9778/189pay/service",
                    new HashMap<String, String>() {{
                        put("Cookie", reqCookieValues.toString());
                    }},
                    new HashMap<String, Object>() {{
                        put("request_params", values);
                    }});

        }).switchMap(response -> {
            String html = new String(response.body().bytes(), StandardCharsets.UTF_8);
            Document doc = Jsoup.parse(html);
            Map<String, Object> body = doc.select("input[type=hidden]").stream().collect(Collectors.toMap(ele -> ele.attr("name"), ele -> (Object) ele.val()));

            return req.postForm("https://mapi.alipay.com/gateway.do?_input_charset=utf-8", new HashMap<>(), body);
        });

        AlipayQrCode.getPayQrCodeUrl(telecomResponse);
    }

    private static Map<String, String> getCookies() {
        Supplier<String> get16String = () -> UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);

        Map<String, String> cookies = new HashMap<>();
        cookies.put("svid", get16String.get());
        cookies.put("s_fid", get16String.get() + "-" + get16String.get());
        cookies.put("loginStatus", "non-logined");
        cookies.put("lvid", UUID.randomUUID().toString().replaceAll("-", ""));
        cookies.put("nvid", "1");
        cookies.put("trkId", UUID.randomUUID().toString());
        cookies.put("s_cc", "true");
        cookies.put("trkHmClickCoords", "587%2C1060%2C2108");
        return cookies;
    }
}
