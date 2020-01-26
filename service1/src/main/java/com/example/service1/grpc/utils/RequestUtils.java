package com.example.service1.grpc.utils;

import com.example.services.model.channel.ChannelProto;
import com.example.services.model.channel.ChannelProto.ChannelId;
import com.example.services.models.enums.operation.OpearationProto.Operation;
import com.example.services.models.request.RequestProto;
import com.example.services.models.service.ServiceProto;
import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class RequestUtils {

    private static final Logger log = LoggerFactory.getLogger(RequestUtils.class);

    private static final String DEFAULT_VERSION = "1.0";
    private static final String DEFAULT_PREVIOUS_SERVICE_ID = "1.0";

    public static RequestProto.Request formAndGetRequest(String version, Operation operation, String fromServiceId, String toSericeId,
                                           ChannelId originChannel, ChannelId channel, ChannelId endChannel, List<ChannelId> channelRoute,
                                           String originServiceId, String previousServiceId, String serviceId, String nextServiceId, List<String> serviceRoute,
                                           Map<String, Object> requestMap, Map<String, String> metaData) {
        RequestProto.Request.Builder requestBuilder = RequestProto.Request.newBuilder()
                .setId(generateAndGetId())
                .setVersion(StringUtils.isNotBlank(version) ? version : DEFAULT_VERSION)
                .setTimestamp(System.currentTimeMillis() + "")
                .setOperation(operation)
//                .setType("")
                .setRequestParams(RequestProto.RequestParams.newBuilder()
                                .setRequestId(generateAndGetRequestId(fromServiceId, toSericeId))
                                .setMessageId(generateAndGetMessageId())
//                                .setSourceId("")
//                                .setDeviceId("")
                                .setChannelInfo(formAndGetChannelInfo(originChannel, channel, endChannel, channelRoute))
                                .setServiceInfo(formAndGetServiceInfo(originServiceId, previousServiceId, serviceId, nextServiceId, serviceRoute))
                                .build());
        Optional.ofNullable(requestMap)
                .ifPresent(e ->
                        e.forEach(
                                (k, v) -> requestBuilder.putRequest(k, Any.newBuilder().setValue(ByteString.copyFrom(getBytes(v))).build())));
        Optional.ofNullable(requestMap)
                .ifPresent(e ->
                        e.forEach(
                                (k, v) -> requestBuilder.putMetaData(k, Any.newBuilder().setValue(ByteString.copyFrom(getBytes(v))).build())));
        return requestBuilder.build();
    }

    private static byte[] getBytes(Object v) {
        byte[] result = null;
        if (v instanceof String) {
            result = ((String) v).getBytes();
        } else if (v instanceof List) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(bos);
                oos.writeObject(v);
            } catch (IOException e) {
                log.error("Error while getting bytes fom list to request", e);
            }
            result = bos.toByteArray();
        }
        return result;
    }

    private static ChannelProto.ChannelInfo formAndGetChannelInfo(ChannelId originChannel, ChannelId channel, ChannelId endChannel, List<ChannelId> channelRoute) {
        ChannelProto.ChannelInfo channelInfo = ChannelProto.ChannelInfo.newBuilder()
                .setOriginChannel(originChannel)
                .setChannel(channel)
                .setEndChannel(endChannel)
                .addChannelRoute(channel)
                .addChannelRoute(channel)
                .build();
        Optional.ofNullable(channelRoute).ifPresent(e -> channelInfo.toBuilder().addAllChannelRoute(e));
        return channelInfo;
    }

    private static ServiceProto.ServiceInfo formAndGetServiceInfo(String originServiceId, String previousServiceId, String serviceId, String nextServiceId, List<String> serviceRoute) {
        ServiceProto.ServiceInfo serviceInfo = ServiceProto.ServiceInfo.newBuilder()
                .setOriginServiceId(originServiceId)
                .setPreviousServiceId(StringUtils.isBlank(previousServiceId) ? DEFAULT_PREVIOUS_SERVICE_ID : previousServiceId)
                .setServiceId(serviceId)
                .setNextServiceId(nextServiceId)
                .addServiceRoute(serviceId)
                .build();
        Optional.ofNullable(serviceRoute).ifPresent(e -> serviceInfo.toBuilder().addAllServiceRoute(e));
        return serviceInfo;
    }

    private static String generateAndGetId() {
        return "ID" + getUUID();
    }

    private static String generateAndGetRequestId(String fromServiceId, String toServiceId) {
        return "REQ-" + fromServiceId + "_" + toServiceId + "-" + System.currentTimeMillis();
    }

    private static String generateAndGetMessageId() {
        return "REQ" + "MID" + getUUID();
    }

    private static String getUUID() {
        return UUID.randomUUID().toString();
    }

}
