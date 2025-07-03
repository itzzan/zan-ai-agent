package com.zan.zanaiagent.demo.langchain4j;

import com.zan.zanaiagent.demo.TestApiKey;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;

/**
 * @Author Zan
 * @Create 2025/7/3 14:09
 * @ClassName: LangChainAiInvoke
 * @Description : TODO 请用一句话描述该类的功能 
 */
public class LangChainAiInvoke {

    public static void main(String[] args) {
        ChatLanguageModel qwenModel = QwenChatModel.builder()
                .apiKey(TestApiKey.API_KEY)
                .modelName("qwen-max")
                .build();
        String answer = qwenModel.chat("我是程序员Zan");
        System.out.println(answer);
    }
}

