import com.example.service1.RequestProto;
import com.example.service1.ResponseProto;

import java.util.UUID;

public class ResponseUtils {

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
