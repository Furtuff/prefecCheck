package fr.furtuff.somwhere.prefecCheck.Service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Prefecture {
    @Headers({"User-agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:63.0) Gecko/20100101 Firefox/63.0",
            "Connection: keep-alive",
            "Accept: */*",
            "Accept-Language: fr,fr-FR;q=0.8,en-US;q=0.5,en;q=0.3",
            "Cache-Control: no-cache",
            "Accept-Encoding: gzip, deflate",
            "Content-Length: 40",
            "Cookie: cookies-accepte=oui; eZSESSID=lhama37n4os8mspe4u8brvboi6",
            "Host: www.haute-garonne.gouv.fr",
            "origin: moz-extension://64213cac-79da-4774-bf87-940c86b76158"})
    @POST("1")
    @FormUrlEncoded
    Call<ResponseBody> loadPlanning(@Field("nextButton") String nextButton, @Field("planning") Integer number);
}
