package com.example.service1.grpc.server;

import com.example.service1.FeatureOneGrpc;
import com.example.service1.FeatureOneProto;
import com.example.service1.RequestProto;
import com.example.service1.grpc.utils.ResponseUtils;
import com.google.gson.Gson;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeatureOneServer {


    public static void main(String[] args) {
        FeatureOneServer featureOneServer = new FeatureOneServer();
        featureOneServer.s
    }

    public static class FeatureOneService extends FeatureOneGrpc.FeatureOneImplBase {

        @Override
        public void callFeatureOne(FeatureOneProto.FeatureOneRequest request, StreamObserver<FeatureOneProto.FeatureOneResponse> responseObserver) {
            responseObserver.onNext(process(request));
            responseObserver.onCompleted();
        }

        private FeatureOneProto.FeatureOneResponse process(FeatureOneProto.FeatureOneRequest featureOneRequest) {
            RequestProto.Request request  = featureOneRequest.getRequest();
            java.util.Map<java.lang.String, com.google.protobuf.Any> requestMap = request.getRequestMap();
            log.info(new Gson().toJson(requestMap));

            return FeatureOneProto.FeatureOneResponse.newBuilder()
                    .setResponse(ResponseUtils.successResponse(request, null, null, null))
                    .build();
        }
    }
}
