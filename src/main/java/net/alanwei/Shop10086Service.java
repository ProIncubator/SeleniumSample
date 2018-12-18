package net.alanwei;


import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Shop10086Service {
    @GET("i")
    Observable<Response<ResponseBody>> fetchOrder(@Query("f") String f, @Query("mobileNo") String mobile, @Query("amount") int amount);

    default Observable<Response<ResponseBody>> fetchOrder(String mobile, int amount) {
        return this.fetchOrder("rechargecredit", mobile, amount);
    }

    @POST("i/v1/pay/saveorder/{mobile}")
    Observable<Response<ResponseBody>> createOrder(@Header("Referer") String referer, @Path("mobile") String mobile, @Query("provinceId") int provinceId, @Body Map<String, Object> data);
}
