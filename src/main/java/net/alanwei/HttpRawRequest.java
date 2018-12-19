package net.alanwei;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface HttpRawRequest {
    @GET
    @Headers({
            "Connection: keep-alive",
            "Pragma: no-cache",
            "Cache-Control: no-cache",
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36",
            "DNT: 1",
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
            "Accept-Encoding: gzip, deflate, br",
            "Accept-Language: en,zh-CN;q=0.9,zh;q=0.8"
    })
    Observable<Response<ResponseBody>> getUrl(@Url String url, @HeaderMap Map<String, String> headers);

    default Observable<Response<ResponseBody>> getUrl(String url) {
        return this.getUrl(url, new HashMap<>());
    }
}
