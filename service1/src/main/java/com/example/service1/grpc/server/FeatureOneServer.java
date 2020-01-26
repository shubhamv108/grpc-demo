package com.example.service1.grpc.server;

import com.example.service1.grpc.utils.ResponseUtils;
import com.example.services.models.featureOne.FeatureOneGrpc;
import com.example.services.models.featureOne.FeatureOneProto.FeatureOneRequest;
import com.example.services.models.featureOne.FeatureOneProto.FeatureOneResponse;
import com.example.services.models.request.RequestProto;
import com.google.gson.Gson;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

//@Slf4j
public class FeatureOneServer {

    private static final Logger log = LoggerFactory.getLogger(FeatureOneServer.class);

    private Server server;

    public static void main(String[] args) throws IOException, InterruptedException {
        FeatureOneServer featureOneServer = new FeatureOneServer();
        featureOneServer.start();
        featureOneServer.blockUntilShutdown();
    }

    private void start() throws IOException {
        int port = 50051;
        server = ServerBuilder.forPort(port)
                .addService(new FeatureOneService())
                .build()
                .start();
        log.info("Server started, listening on {}", port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                FeatureOneServer.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (null != server) {
            server.awaitTermination();
        }
    }

    private void stop() {
        if (null != server) {
            server.shutdown();
        }
    }

    public static class FeatureOneService extends FeatureOneGrpc.FeatureOneImplBase {

        @Override
        public void callFeatureOne(FeatureOneRequest request, StreamObserver<FeatureOneResponse> responseObserver) {
            responseObserver.onNext(process(request));
            responseObserver.onCompleted();
        }

        private FeatureOneResponse process(FeatureOneRequest featureOneRequest) {
            RequestProto.Request request  = featureOneRequest.getRequest();
            java.util.Map<java.lang.String, com.google.protobuf.Any> requestMap = request.getRequestMap();
            log.info("Request: {}", new Gson().toJson(requestMap));

            log.info("Response: {}", "");
            return FeatureOneResponse.newBuilder()
                    .setResponse(ResponseUtils.successResponse(request, null, null, null))
                    .build();
        }
    }
}
