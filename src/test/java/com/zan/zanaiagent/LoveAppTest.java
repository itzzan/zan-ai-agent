package com.zan.zanaiagent;

import cn.hutool.core.lang.UUID;
import com.zan.zanaiagent.app.LoveApp;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.stream.Collectors;

/**
 * @Author Zan
 * @Create 2025/7/3 15:14
 * @ClassName: LoveAppTest
 * @Description : TODO 请用一句话描述该类的功能 
 */
@SpringBootTest
class LoveAppTest {

    @Resource
    private LoveApp loveApp;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是程序员Zan";
        String answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第二轮
        message = "我想让另一半（编程导航）更爱我";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "我的另一半叫什么来着？刚跟你说过，帮我回忆一下";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void testChatStream() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮对话
        String message1 = "你好，我是程序员Zan";
        Flux<String> response1 = loveApp.doChatStream(message1, chatId);

        StepVerifier.create(response1.collect(Collectors.joining()))
                .assertNext(fullResponse -> {
                    Assertions.assertNotNull(fullResponse);
                    Assertions.assertFalse(fullResponse.isEmpty());
                    // 可选：添加更具体的断言
                    // Assertions.assertTrue(fullResponse.contains("你好"));
                })
                .verifyComplete();

        // 第二轮对话
        String message2 = "我想让另一半（编程导航）更爱我";
        Flux<String> response2 = loveApp.doChatStream(message2, chatId);

        StepVerifier.create(response2.collect(Collectors.joining()))
                .assertNext(fullResponse -> {
                    Assertions.assertNotNull(fullResponse);
                    Assertions.assertFalse(fullResponse.isEmpty());
                })
                .verifyComplete();

        // 第三轮对话 - 验证记忆功能
        String message3 = "我的另一半叫什么来着？刚跟你说过，帮我回忆一下";
        Flux<String> response3 = loveApp.doChatStream(message3, chatId);

        StepVerifier.create(response3.collect(Collectors.joining()))
                .assertNext(fullResponse -> {
                    Assertions.assertNotNull(fullResponse);
                    Assertions.assertFalse(fullResponse.isEmpty());
                    // 验证AI记住了之前的对话
                    Assertions.assertTrue(
                            fullResponse.contains("编程导航") ||
                                    fullResponse.contains("另一半"),
                            "响应应包含先前提到的名称"
                    );
                })
                .verifyComplete();
    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是程序员Zan，我想让另一半（编程导航）更爱我，但我不知道该怎么做";
        LoveApp.LoveReport loveReport = loveApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(loveReport);
    }

}

