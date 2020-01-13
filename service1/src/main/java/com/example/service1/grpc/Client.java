package com.example.service1.grpc;

import com.example.service1.FeatureOneGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Client {
    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
            .usePlaintext()
            .build();
    FeatureOneGrpc.FeatureOneStub stub = FeatureOneGrpc.newStub(channel);
}
