package net.alanwei;


import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Shop10086Service {
    @GET("i?f=rechargecredit&mobileNo=13684975948&amount=30")
    Observable<ResponseBody> hello();
}
