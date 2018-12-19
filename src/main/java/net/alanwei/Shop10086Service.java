package net.alanwei;


import java.util.Map;

import io.reactivex.Observable;
import net.alanwei.models.BaseResponse;
import net.alanwei.models.CreateOrderResponse;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Shop10086Service {
    @GET("i/")
    Observable<Response<ResponseBody>> fetchOrder(@Query("f") String f, @Query("mobileNo") String mobile, @Query("amount") int amount);

    default Observable<Response<ResponseBody>> fetchOrder(String mobile, int amount) {
        return this.fetchOrder("rechargecredit", mobile, amount);
    }

    @POST("i/v1/pay/saveorder/{mobile}")
    Observable<Response<BaseResponse<CreateOrderResponse>>> createOrder(@Header("Referer") String referer, @Path("mobile") String mobile, @Query("provinceId") int provinceId, @Body Map<String, Object> data);

    @GET
    @Headers({"User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36"})
    Observable<Response<ResponseBody>> getUrl(@Url String url);

    @GET
    @Headers({"User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36"})
    Observable<Response<ResponseBody>> getUrlWithHeaders(@Url String url, @HeaderMap Map<String, String> headers);
}
