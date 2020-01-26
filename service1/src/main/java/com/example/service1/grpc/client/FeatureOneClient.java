package com.example.service1.grpc.client;

import com.example.service1.grpc.utils.RequestUtils;
import com.example.services.models.featureOne.FeatureOneGrpc;
import com.example.services.models.featureOne.FeatureOneGrpc.FeatureOneBlockingStub;
import com.example.services.models.featureOne.FeatureOneGrpc.FeatureOneFutureStub;
import com.example.services.models.featureOne.FeatureOneGrpc.FeatureOneStub;
import com.example.services.models.featureOne.FeatureOneProto;
import com.google.gson.Gson;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.example.services.model.channel.ChannelProto.ChannelId.TEST;
import static com.example.services.models.enums.operation.OpearationProto.Operation.CALL;

public class FeatureOneClient {

    private static final Logger log = LoggerFactory.getLogger(FeatureOneClient.class);

    protected final ManagedChannel channel;
    private final FeatureOneBlockingStub blockingStub;
    protected final FeatureOneStub stub;
    protected final FeatureOneFutureStub futureStub;

    public FeatureOneClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build());
    }

    public FeatureOneClient(ManagedChannel managedChannel) {
        this.channel = managedChannel;
        this.blockingStub = FeatureOneGrpc.newBlockingStub(channel);
        this.stub = FeatureOneGrpc.newStub(channel);
        this.futureStub = FeatureOneGrpc.newFutureStub(channel);
    }

    public static void main(String[] args) {
        FeatureOneClient featureOneClient = new FeatureOneClient("localhost", 50051);
        featureOneClient.callFeatureOne();
    }

    private void callFeatureOne() {
        Map<String, Object> request = new HashMap<>();
        request.put("Key", "Value");
        log.info("Calling feature one; Request: {}", new Gson().toJson(request));
        FeatureOneProto.FeatureOneRequest featureOneRequest = FeatureOneProto.FeatureOneRequest.newBuilder()
                .setRequest(
                        RequestUtils.formAndGetRequest(null, CALL, "1", "1",
                            TEST, TEST, TEST, null,
                            "1", "", "1", "1", null,
                            request, null))
                .build();
        FeatureOneProto.FeatureOneResponse featureOneResponse = null;
        try {
            featureOneResponse = this.blockingStub.callFeatureOne(featureOneRequest);
        } catch (StatusRuntimeException e) {
            log.warn("RPC failed: {0}", e.getStatus());
            return;
        }
        log.info("Called feature one; Response: {}", new Gson().toJson(featureOneResponse));
    }

}
