package net.alanwei;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class JsoupSample {
    public static void main(String[] args) throws Throwable {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://shop.10086.cn/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        Shop10086Service service = retrofit.create(Shop10086Service.class);
        ResponseBody response = service.hello();
    }
}
