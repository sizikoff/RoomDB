package com.amicus.testapk;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//https://api.deepseek.com/chat/completions

public class MainActivity extends AppCompatActivity {

    private final String API_KEY = "sk-bd3d6422f39e4ca5a51c309f3ec82e38";
    private final String BASE_URL = "https://api.deepseek.com/";
    //private final String MODEL = "deepseek-flash";
    private final String MODEL = "deepseek-v4-pro";
    private final String SYSTEM_PROMPT = "Ты должен отвечать в стиле Ивана Грозного.Используй характерную для эпохи лексику но сохраняй понятность и точность ответов";

    EditText editText;
    Button button;
    TextView textView;

    DeepSeekApi api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(DeepSeekApi.class);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_message = editText.getText().toString().trim();
                if (user_message.isEmpty()) {
                    textView.setError("Введите запрос");
                    return;
                }
                textView.setText("Загрузка...");
                button.setEnabled(false);

                ChatRequest request = new ChatRequest(SYSTEM_PROMPT,user_message,MODEL);
                api.sendMessage("Bearer " + API_KEY, request)
                        .enqueue(new Callback<ChatResponse>() {
                            @Override
                            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                                button.setEnabled(true);
                                editText.setText("");
                                if (response.isSuccessful() && response.body() != null &&
                                        response.body().choices != null &&
                                        !response.body().choices.isEmpty() &&
                                        response.body().choices.get(0).message != null) {
                                    String answer = response.body().choices.get(0).message.content;
                                    textView.setText(answer != null ? answer : "Пустой ответ от сервера");
                                } else {
                                    String error = response.errorBody() != null ? response.errorBody().toString() : "Неизвестная ошибка";
                                    textView.setText("Ошибка API "+response.code()+"\n"+error);
                                }
                            }

                            @Override
                            public void onFailure(Call<ChatResponse> call, Throwable throwable) {
                                button.setEnabled(true);
                                    textView.setText("ошибка соединения "+throwable.getMessage());
                            }
                        });
            }
        });
    }
}
