package com.amicus.testapk;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//https://api.deepseek.com/chat/completions


public class MainActivity extends AppCompatActivity {

    // Элементы интерфейса
    private EditText editText;
    private Button button;
    private TextView textView;

    // API-ключ DeepSeek
    private static final String API_KEY = "sk-bd3d6422f39e4ca5a51c309f3ec82e38";

    private final String BASE_URL = "https://api.deepseek.com/";

    // Выбираем модель
    // Для Pro достаточно заменить название на deepseek-v4-pro
    private static final String MODEL = "deepseek-flash";
    //private final String MODEL = "deepseek-v4-pro";

    // Системная инструкция для нейросети
    private final String SYSTEM_PROMPT = "Ты должен отвечать в стиле Ивана Грозного.Используй характерную для эпохи лексику но сохраняй понятность и точность ответов";

    // Объект для работы с DeepSeek API
    private DeepSeekApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Находим элементы интерфейса
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);

        // Создаем перехватчик HTTP-запросов
        HttpLoggingInterceptor logging =
                new HttpLoggingInterceptor();

        // Скрываем API-ключ в логах
        logging.redactHeader("Authorization");

        // Включаем подробное логирование запросов и ответов
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Создаем HTTP-клиент OkHttp
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        // Создаем объект Retrofit
        Retrofit retrofit = new Retrofit.Builder()

                // Базовый URL DeepSeek
                .baseUrl(BASE_URL)

                // Подключаем HTTP-клиент
                .client(client)

                // Подключаем Gson для работы с JSON
                .addConverterFactory(GsonConverterFactory.create())

                // Завершаем создание Retrofit
                .build();

        // Создаем реализацию API-интерфейса
        api = retrofit.create(DeepSeekApi.class);

        // Обрабатываем нажатие кнопки
        button.setOnClickListener(v -> {

            // Получаем сообщение пользователя
            String userMessage = editText.getText()
                    .toString()
                    .trim();

            // Проверяем, что поле не пустое
            if (userMessage.isEmpty()) {
                editText.setError("Введите сообщение");
                return;
            }

            // Показываем загрузку
            textView.setText("DeepSeek думает...");

            // Блокируем кнопку на время запроса
            button.setEnabled(false);

            // Создаем объект запроса
            // Передаем системную инструкцию и сообщение пользователя
            ChatRequest request = new ChatRequest(
                    SYSTEM_PROMPT,
                    userMessage
            );

            // Устанавливаем выбранную модель
            request.model = MODEL;

            // Отправляем запрос асинхронно
            api.sendMessage(
                    "Bearer " + API_KEY,
                    request
            ).enqueue(new Callback<ChatResponse>() {

                // Метод вызывается, когда получен HTTP-ответ
                @Override
                public void onResponse(
                        Call<ChatResponse> call,
                        Response<ChatResponse> response) {

                    // Разблокируем кнопку
                    button.setEnabled(true);

                    // Проверяем успешность ответа
                    if (response.isSuccessful()
                            && response.body() != null
                            && response.body().choices != null
                            && !response.body().choices.isEmpty()
                            && response.body().choices.get(0).message != null) {

                        // Получаем текст ответа
                        String answer = response.body()
                                .choices.get(0)
                                .message.content;

                        // Отображаем ответ в TextView
                        if (answer != null && !answer.isEmpty()) {
                            textView.setText(answer);
                        } else {
                            textView.setText("Сервер вернул пустой ответ");
                        }

                    } else {

                        // Обрабатываем ошибку API
                        String error = "Неизвестная ошибка";

                        if (response.errorBody() != null) {
                            try {
                                error = response.errorBody().string();
                            } catch (IOException e) {
                                error = e.getMessage();
                            }
                        }

                        // Показываем код HTTP-ошибки
                        textView.setText(
                                "Ошибка API: " + response.code()
                                        + "\n" + error
                        );
                    }
                }

                // Метод вызывается при сетевой ошибке
                @Override
                public void onFailure(
                        Call<ChatResponse> call,
                        Throwable t) {

                    // Разблокируем кнопку
                    button.setEnabled(true);

                    // Показываем причину ошибки
                    textView.setText(
                            "Ошибка соединения: " + t.getMessage()
                    );
                }
            });
        });
    }
}
