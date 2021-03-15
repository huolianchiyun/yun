package com.hlcy.yun.gb28181.service.sipmsg.flow.message.query;

import com.hlcy.yun.gb28181.bean.ConfigDownload;
import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.MessageRequestProcessor;
import com.hlcy.yun.gb28181.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;
import javax.sip.RequestEvent;
import static com.hlcy.yun.gb28181.util.XmlUtil.getTextOfChildTagFrom;

/**
 * 设备配置查询请求处理器
 */
@Slf4j
public class ConfigDownloadQueryRequestProcessor extends MessageRequestProcessor {

    @Override
    protected void doProcess(RequestEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Receive a CmdType <ConfigDownload> request, message: {}.", event.getRequest());
        }

        Element rootElement = getRootElementFrom(event);
        if (!isMessageError(rootElement)) {
            final ConfigDownload configDownload = extractConfigDownloadFrom(rootElement);
                DeferredResultHolder.setDeferredResultForRequest(
                        DeferredResultHolder.CALLBACK_CMD_QUERY_CONFIG_DOWNLOAD + getTextOfChildTagFrom(rootElement, "DeviceID"),
                        configDownload);
        }
    }

    private boolean isMessageError(Element rootElement) {
        final String result = getTextOfChildTagFrom(rootElement, "Result");
        if (CLIENT_RESPONSE_REQUEST_RESULT_ERROR.equalsIgnoreCase(result)) {
            String deviceId = getTextOfChildTagFrom(rootElement, "DeviceID");
            DeferredResultHolder.setErrorDeferredResultForRequest(
                    DeferredResultHolder.CALLBACK_CMD_QUERY_CONFIG_DOWNLOAD + deviceId,
                    getTextOfChildTagFrom(rootElement, "Reason"));
            return true;
        }
        return false;
    }

    private ConfigDownload extractConfigDownloadFrom(Element rootElement) {
        return new ConfigDownload()
                .setDeviceId(XmlUtil.getTextOfChildTagFrom(rootElement, "DeviceID"))
                .setBasicParam(extractBasicParam(rootElement))
                .setVideoParamOp(extractVideoParamOpt(rootElement))
                .setSvacEncodeConfig(extractSVACEncodeConfig(rootElement))
                .setSvacDecodeConfig(extractSVACDecodeConfig(rootElement));
    }

    private  ConfigDownload.BasicParam extractBasicParam(Element rootElement) {
        final Element basicParamElement = rootElement.element("BasicParam");
        return new ConfigDownload.BasicParam().setDeviceName(getTextOfChildTagFrom(basicParamElement, "Name"))
                .setExpiration(getTextOfChildTagFrom(basicParamElement, "Expiration"))
                .setHeartBeatInterval(Integer.parseInt(getTextOfChildTagFrom(basicParamElement, "HeartBeatInterval")))
                .setHeartBeatCount(Integer.parseInt(getTextOfChildTagFrom(basicParamElement, "HeartBeatCount")))
                .setPositionCapability(Integer.parseInt(getTextOfChildTagFrom(basicParamElement, "PositionCapability")))
                .setLongitude(Double.parseDouble(getTextOfChildTagFrom(basicParamElement, "Longitude")))
                .setLatitude(Double.parseDouble(getTextOfChildTagFrom(basicParamElement, "Latitude")));
    }

    private ConfigDownload.VideoParamOpt extractVideoParamOpt(Element rootElement) {
        final Element videoParamOptElement = rootElement.element("VideoParamOpt");
        return new ConfigDownload.VideoParamOpt()
                .setDownloadSpeed(getTextOfChildTagFrom(videoParamOptElement, "DownloadSpeed"))
                .setResolution(getTextOfChildTagFrom(videoParamOptElement, "Resolution"));
    }

    private ConfigDownload.SVACEncodeConfig extractSVACEncodeConfig(Element rootElement) {
        final Element SVACEncodeConfigElement = rootElement.element("SVACEncodeConfig");
        final ConfigDownload.SVACEncodeConfig svacEncodeConfig = new ConfigDownload.SVACEncodeConfig();
//        final ROIParam roiParam = new ROIParam();
        return svacEncodeConfig;
    }

    private ConfigDownload.SVACDecodeConfig extractSVACDecodeConfig(Element rootElement) {
        final Element SVACDecodeConfigElement = rootElement.element("SVACDecodeConfig");
        return new ConfigDownload.SVACDecodeConfig();
    }
}
