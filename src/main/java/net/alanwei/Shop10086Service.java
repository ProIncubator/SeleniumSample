package net.alanwei;


import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.Map;

public interface Shop10086Service {
    @GET("i?f=rechargecredit&mobileNo=13695589826&amount=100")
    Observable<Response<ResponseBody>> fetchOrder();

    //
//    @Headers({
//            "Accept: application/json, text/javascript, */*; q=0.01",
//            "Referer: https://shop.10086.cn/i/?f=rechargecredit&mobileNo=13695589826&amount=30",
//            "Content-Type: application/json"
//    })
    @POST("i/v1/pay/saveorder/13695589826?provinceId=551")
    Call<ResponseBody> createOrder(@Body Map<String, Object> data);
}
