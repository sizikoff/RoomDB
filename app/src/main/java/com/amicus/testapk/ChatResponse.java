package com.amicus.testapk;

import java.util.List;

/**
 * Created by sizik on 13.11.2026.
 */
/*
{
  "id": "88beaa90-17fd-45fc-939c-2ef75e2d913c",
  "object": "chat.completion",
  "created": 1793084842,
  "model": "deepseek-flash",
  "choices": [
    {
      "index": 0,
      "message": {
        "role": "assistant",
        "content": "Hello! 👋 How can I help you today?"
      },
      "logprobs": null,
      "finish_reason": "stop"
    }
  ],
  "usage": {
    "prompt_tokens": 7,
    "completion_tokens": 11,
    "total_tokens": 18,
    "prompt_tokens_details": {
      "cached_tokens": 0
    },
    "prompt_cache_hit_tokens": 0,
    "prompt_cache_miss_tokens": 7
  },
  "system_fingerprint": "e72d3c7f663d3e1383b00b3884cb4501"
}
 */

// Класс описывает ответ от DeepSeek
public class ChatResponse {

    // Список вариантов ответа
    List<Choice> choices;

    // Один вариант ответа
    public static class Choice {

        // Сообщение нейросети
        Message message;
    }

    // Данные сообщения
    public static class Message {

        // Роль отправителя
        String role;

        // Текст ответа
        String content;
    }
}
