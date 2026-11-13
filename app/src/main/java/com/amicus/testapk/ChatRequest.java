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
public class ChatRequest {
    String model;
    String reasoning_effort;
    List<Message>messages;

    public ChatRequest(String systemPrompt,String userMessage,String model) {
        this.model = model;
        reasoning_effort = "none";
        messages = new ArrayList<>();
        messages.add(new Message("system",systemPrompt));
        messages.add(new Message("user",userMessage));
    }

    public static class Message{
        String role;
        String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
