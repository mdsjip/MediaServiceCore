package com.liskovsoft.youtubeapi;

import com.liskovsoft.youtubeapi.models.AccessToken;
import com.liskovsoft.youtubeapi.models.UserCode;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BrowserAuth {
    @FormUrlEncoded
    @POST("https://www.youtube.com/o/oauth2/device/code")
    Call<UserCode> getUserCode(@Field("client_id") String clientId, @Field("scope") String scope);

    @FormUrlEncoded
    @POST("https://www.youtube.com/o/oauth2/token")
    Call<AccessToken> getAuthToken(@Field("client_id") String clientId,
                                   @Field("code") String deviceCode,
                                   @Field("client_secret") String clientSecret,
                                   @Field("grant_type") String grantType);
}
