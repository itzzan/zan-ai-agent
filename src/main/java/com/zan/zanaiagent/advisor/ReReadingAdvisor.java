package com.zan.zanaiagent.advisor;

import org.springframework.ai.chat.client.advisor.api.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Zan
 * @Create 2025/7/3 15:30
 * @ClassName: ReReadingAdvisor
 * @Description : 自定义 Re2 Advisor，可提高大型语言模型的推理能力
 */
public class ReReadingAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {


    private AdvisedRequest before(AdvisedRequest advisedRequest) {

        Map<String, Object> advisedUserParams = new HashMap<>(advisedRequest.userParams());
        advisedUserParams.put("re2_input_query", advisedRequest.userText());

        return AdvisedRequest.from(advisedRequest)
                .userText("""
                        {re2_input_query}
                        Read the question again: {re2_input_query}
                        """)
                .userParams(advisedUserParams)
                .build();
    }

    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        return chain.nextAroundCall(this.before(advisedRequest));
    }

    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        return Mono.just(advisedRequest)
                .publishOn(Schedulers.boundedElastic())
                .map(request -> {
                    // 请求前处理逻辑
                    return modifyRequest(request);
                })
                .flatMapMany(request -> chain.nextAroundStream(request))
                .map(response -> {
                    // 响应处理逻辑
                    return modifyResponse(response);
                });
    }

    private AdvisedResponse modifyResponse(AdvisedResponse advisedResponse) {
        // 读取上下文
        Object value = advisedResponse.adviseContext().get("key");
        return advisedResponse;
    }

    private AdvisedRequest modifyRequest(AdvisedRequest advisedRequest) {
        advisedRequest = advisedRequest.updateContext(context -> {
            context.put("key", "value");
            return context;
        });
        return advisedRequest;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}

