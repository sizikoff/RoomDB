package com.amicus.testapk;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by sizik on 13.11.2026.
 */

// Интерфейс для взаимодействия с DeepSeek API
public interface DeepSeekApi {

    // Отправляем POST-запрос на chat/completions
    @Headers("Content-Type: application/json")
    @POST("chat/completions")
    Call<ChatResponse> sendMessage(

            // Передаем ключ API в HTTP-заголовке
            @Header("Authorization") String apiKey,

            // Передаем тело запроса
            @Body ChatRequest request
    );
}
