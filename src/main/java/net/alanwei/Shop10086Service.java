package net.alanwei;


import io.reactivex.Observable;
import net.alanwei.models.BaseResponse;
import net.alanwei.models.CreateOrderResponse;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.*;

import java.util.Map;

public interface Shop10086Service {
    @GET("i/")
    Observable<Response<ResponseBody>> fetchOrder(@Query("f") String f, @Query("mobileNo") String mobile, @Query("amount") int amount);

    default Observable<Response<ResponseBody>> fetchOrder(String mobile, int amount) {
        return this.fetchOrder("rechargecredit", mobile, amount);
    }

    @POST("i/v1/pay/saveorder/{mobile}")
    Observable<Response<BaseResponse<CreateOrderResponse>>> createOrder(@Header("Referer") String referer, @Path("mobile") String mobile, @Query("provinceId") int provinceId, @Body Map<String, Object> data);

    @GET
    Observable<Response<ResponseBody>> getUrl(@Url String url);
}
