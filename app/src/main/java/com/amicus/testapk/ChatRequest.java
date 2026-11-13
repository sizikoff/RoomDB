package com.amicus.testapk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sizik on 13.11.2026.
 */

/*
curl https://api.deepseek.com/chat/completions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${DEEPSEEK_API_KEY}" \
  -d '{
        "model": "deepseek-flash",
        "messages": [
          {"role": "system", "content": "You are a helpful assistant."},
          {"role": "user", "content": "Hello!"}
        ],
        "thinking": {"type": "enabled"},
        "reasoning_effort": "high",
        "stream": false
      }'

 */

// Класс описывает JSON-запрос к DeepSeek
public class ChatRequest {

    // Название используемой модели
    String model;

    // Максимальное количество генерируемых токенов
    int max_tokens;

    // Настройки режима рассуждений
    Thinking thinking;

    // Список сообщений для нейросети
    List<Message> messages;

    // Конструктор принимает системную инструкцию и вопрос пользователя
    public ChatRequest(String systemPrompt, String userMessage) {

        // Выбираем модель
        model = "deepseek-flash";

        // Ограничиваем длину ответа
        max_tokens = 300;

        // Отключаем режим Thinking
        thinking = new Thinking("disabled");

        // Создаем список сообщений
        messages = new ArrayList<>();

        // Добавляем системную инструкцию
        messages.add(new Message("system", systemPrompt));

        // Добавляем сообщение пользователя
        messages.add(new Message("user", userMessage));
    }

    // Класс для настроек режима Thinking
    public static class Thinking {

        String type;

        public Thinking(String type) {
            this.type = type;
        }
    }

    // Класс описывает одно сообщение
    public static class Message {

        // Роль отправителя
        String role;

        // Текст сообщения
        String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
