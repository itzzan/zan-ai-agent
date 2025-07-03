package com.zan.zanaiagent.demo.springai;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @Author Zan
 * @Create 2025/7/3 14:06
 * @ClassName: SpringAiAiInvoke
 * @Description : TODO 请用一句话描述该类的功能 
 */
@Component
public class SpringAiAiInvoke implements CommandLineRunner {

    @Resource
    private ChatModel dashscopeChatModel;

    @Override
    public void run(String... args) throws Exception {
        AssistantMessage output = dashscopeChatModel.call(new Prompt("你好，我是Zan"))
                .getResult()
                .getOutput();
        System.out.println(output.getText());
    }
}
