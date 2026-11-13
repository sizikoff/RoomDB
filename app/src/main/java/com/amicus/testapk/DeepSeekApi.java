package com.amicus.testapk;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by sizik on 13.11.2026.
 */
public interface DeepSeekApi {
    @Headers("Content-Type: application/json")
    @POST("chat/completions")
    Call<ChatResponse> sendMessage(@Header("Authorization")String apiKey, @Body ChatRequest request);
}
