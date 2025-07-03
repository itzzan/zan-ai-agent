package com.zan.zanaiagent.advisor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.advisor.api.*;
import reactor.core.publisher.Flux;

/**
 * @Author Zan
 * @Create 2025/7/3 15:18
 * @ClassName: MyCustomAdvisor
 * @Description : 自定义拦截器
 */
@Slf4j
public class MyCustomAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        // 1. 处理请求（前置处理）
        AdvisedRequest modifiedRequest = processRequest(advisedRequest);

        // 2. 调用链中的下一个Advisor
        AdvisedResponse response = chain.nextAroundCall(modifiedRequest);

        // 3. 处理响应（后置处理）
        return processResponse(response);
    }

    private AdvisedResponse processResponse(AdvisedResponse response) {
        log.debug("After advised response: {}", response);
        return response;
    }

    private AdvisedRequest processRequest(AdvisedRequest advisedRequest) {
        log.debug("Before advised request: {}", advisedRequest);
        return advisedRequest;
    }

    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        // 1. 处理请求
        AdvisedRequest modifiedRequest = processRequest(advisedRequest);

        // 2. 调用链中的下一个Advisor并处理流式响应
        return chain.nextAroundStream(modifiedRequest)
                .map(response -> processResponse(response));
    }

    @Override
    public String getName() {
        return "Zan自定义的 Advisor";
    }

    @Override
    public int getOrder() {
        // 值越小优先级越高，越先执行
        return 100;
    }

}
