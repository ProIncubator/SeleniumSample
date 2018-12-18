package net.alanwei;


import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Shop10086Service {
    @GET("i?f=rechargecredit&mobileNo=13695589826&amount=100")
    Observable<Response<ResponseBody>> fetchOrder();

    @POST("i/v1/pay/saveorder/13695589826?provinceId=551")
    Observable<Response<ResponseBody>> createOrder(@Header("Referer") String referer, @Body Map<String, Object> data);
}
