package com.example.service1.grpc.utils;

import com.example.service1.ResponseProto;
import com.example.services.model.channel.ChannelProto;
import com.example.services.models.request.RequestProto;
import com.example.services.models.service.ServiceProto;
import com.google.protobuf.Any;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

public class ResponseUtils {

    private static final Logger log = LoggerFactory.getLogger(ResponseUtils.class);

    private static final String DEFAULT_SUCCESS_MESSAGE = "Successfully processed";
    private static final String DEFAULT_FAILURE_MESSAGE = "Failure while processing";

    public static ResponseProto.Response successResponse(RequestProto.Request request, String message, java.util.Map<String, Object> response, java.util.Map<String, Object> metadata) {
        ResponseProto.Response builtResponse = formAndGetResponse(request, StringUtils.isBlank(message) ? DEFAULT_SUCCESS_MESSAGE : message, 200, "SUCCESS", "000", null);
        if (MapUtils.isNotEmpty(response)) {
            Map<String, Any> responseMap  = builtResponse.getResponseMap();
            response.forEach((k, v) -> responseMap.put(k, (Any) v));
        }
        if (MapUtils.isNotEmpty(metadata)) {
            Map<String, Any> metaDataMap  = builtResponse.getResponseMap();
            metadata.forEach((k, v) -> metaDataMap.put(k, (Any) v));
        }
        return builtResponse;
    }

    public static ResponseProto.Response formAndGetResponse(RequestProto.Request request, String message, Integer responseCode, String status, String code, ResponseProto.ErrorMessages errorMessages) {
        return ResponseProto.Response.newBuilder()
                .setId("")
                .setVersion(request.getVersion())
                .setTimestamp(System.currentTimeMillis() + " ")
                .setOperation(request.getOperation())
                .setType(request.getType())
                .setResponseParams(
                        ResponseProto.ResponseParams.newBuilder()
                                .setResponseId(UUID.randomUUID().toString())
                                .setMessageId(request.getRequestParams().getMessageId())
                                .setResponseMessageId(UUID.randomUUID().toString())
                                .setSourceId(request.getRequestParams().getSourceId())
                                .setDeviceId(request.getRequestParams().getDeviceId())
                                .setChannelInfo(ChannelProto.ChannelInfo.newBuilder().build())
                                .setServiceInfo(ServiceProto.ServiceInfo.newBuilder().build())
                                .setResponseStatus(ResponseProto.ResponseStatus.newBuilder().build())
                                .setResponseCode(responseCode)
                                .build())
            .build();
    }

}
