package com.example.service1.grpc.utils;

import com.example.service1.RequestProto;
import com.example.service1.ResponseProto;
import com.google.protobuf.Any;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import java.util.Map;

import java.util.UUID;

public class ResponseUtils {

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

    public static ResponseProto.Response formAndGetResponse(RequestProto.Request request, String message, Integer responseStatus, String status, String code, ResponseProto.ErrorMessages errorMessages) {
        return ResponseProto.Response.newBuilder()
                .setId("")
                .setVersion(request.getVersion())
                .setTimestamp(System.currentTimeMillis() + "")
                .setOperation(request.getOperation())
                .setType(request.getType())
                .setResponseParams(
                        ResponseProto.ResponseParams.newBuilder()
                                .setResponseId(UUID.randomUUID().toString())
                                .setMessageId(request.getRequestParams().getMessageId())
                                .setResponseMessageId(UUID.randomUUID().toString())
                                .setSourceId(request.getRequestParams().getSourceId())
                                .setDeviceId(request.getRequestParams().getDeviceId())
                                .setChannel(request.getRequestParams().getChannel())
                                .setResponseStatus(responseStatus)
                                .setStatus(status)
                                .setMessage(message)
                                .setCode(code)
                                .setErrorMessages(errorMessages)
                                .build())
            .build();
    }

}
