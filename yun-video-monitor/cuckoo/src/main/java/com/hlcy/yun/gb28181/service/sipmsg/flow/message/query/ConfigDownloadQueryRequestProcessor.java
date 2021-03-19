package com.hlcy.yun.gb28181.service.sipmsg.flow.message.query;

import com.hlcy.yun.gb28181.bean.ConfigDownload;
import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.MessageRequestProcessor;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;
import org.springframework.util.StringUtils;

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
                    DeferredResultHolder.CALLBACK_CMD_QUERY_CONFIG_DOWNLOAD + configDownload.configType() + configDownload.getDeviceId(),
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
        final ConfigDownload configDownload = new ConfigDownload()
                .setDeviceId(getTextOfChildTagFrom(rootElement, "DeviceID"));

        JUMP_END:
        {
            final Element basicParamElement = rootElement.element("BasicParam");
            if (basicParamElement != null) {
                configDownload.setBasicParam(extractBasicParam(basicParamElement));
                break JUMP_END;
            }

            final Element videoParamOptElement = rootElement.element("VideoParamOpt");
            if (videoParamOptElement != null) {
                configDownload.setVideoParamOpt(extractVideoParamOpt(videoParamOptElement));
                break JUMP_END;
            }

            final Element svacEncodeConfigElement = rootElement.element("SVACEncodeConfig");
            if (svacEncodeConfigElement != null) {
                configDownload.setSvacEncodeConfig(extractSVACEncodeConfig(svacEncodeConfigElement));
                break JUMP_END;
            }

            final Element svacDecodeConfigElement = rootElement.element("SVACDecodeConfig");
            if (svacDecodeConfigElement != null) {
                configDownload.setSvacDecodeConfig(extractSVACDecodeConfig(svacDecodeConfigElement));
            }
        }
        return configDownload;
    }

    private ConfigDownload.BasicParam extractBasicParam(Element basicParamElement) {
        final ConfigDownload.BasicParam basicParam = new ConfigDownload.BasicParam()
                .setDeviceName(getTextOfChildTagFrom(basicParamElement, "Name"))
                .setExpiration(getTextOfChildTagFrom(basicParamElement, "Expiration"))
                .setHeartBeatInterval(Integer.parseInt(getTextOfChildTagFrom(basicParamElement, "HeartBeatInterval")))
                .setHeartBeatCount(Integer.parseInt(getTextOfChildTagFrom(basicParamElement, "HeartBeatCount")))
                .setPositionCapability(Integer.parseInt(getTextOfChildTagFrom(basicParamElement, "PositionCapability")));

        final String longitude = getTextOfChildTagFrom(basicParamElement, "Longitude");
        if (!StringUtils.isEmpty(longitude)) {
            basicParam.setLongitude(Double.parseDouble(longitude));
        }
        final String latitude = getTextOfChildTagFrom(basicParamElement, "Latitude");
        if (!StringUtils.isEmpty(latitude)) {
            basicParam.setLatitude(Double.parseDouble(latitude));
        }
        return basicParam;
    }

    private ConfigDownload.VideoParamOpt extractVideoParamOpt(Element videoParamOptElement) {
        return new ConfigDownload.VideoParamOpt()
                .setDownloadSpeed(getTextOfChildTagFrom(videoParamOptElement, "DownloadSpeed"))
                .setResolution(getTextOfChildTagFrom(videoParamOptElement, "Resolution"));
    }

    private ConfigDownload.SVACEncodeConfig extractSVACEncodeConfig(Element svacEncodeConfigElement) {
        ConfigDownload.SVACEncodeConfig svacEncodeConfig = null;
        if (svacEncodeConfigElement != null) {
            svacEncodeConfig = new ConfigDownload.SVACEncodeConfig()
                    .setRoiParam(extractROIParam(svacEncodeConfigElement))
                    .setAudioParam(extractAudioParam(svacEncodeConfigElement))
                    .setSurveillanceParam(extractSurveillanceParamForEncode(svacEncodeConfigElement))
                    .setSvcParam(extractSvcParamForEncode(svacEncodeConfigElement));
        }
        return svacEncodeConfig;
    }

    private ConfigDownload.SVACEncodeConfig.ROIParam extractROIParam(Element svacEncodeConfigElement) {
        ConfigDownload.SVACEncodeConfig.ROIParam roiParam = null;
        final Element roiParamElement = svacEncodeConfigElement.element("ROIParam");
        if (roiParamElement != null) {
            roiParam = new ConfigDownload.SVACEncodeConfig.ROIParam();

            final String roiFlag = getTextOfChildTagFrom(roiParamElement, "ROIFlag");
            if (!StringUtils.isEmpty(roiFlag)) {
                roiParam.setRoiFlag(Integer.parseInt(roiFlag));
            }

            final String roiNumber = getTextOfChildTagFrom(roiParamElement, "ROINumber");
            if (!StringUtils.isEmpty(roiNumber)) {
                roiParam.setRoiNumber(Integer.parseInt(roiNumber));
            }

            final String backGroundQP = getTextOfChildTagFrom(roiParamElement, "BackGroundQP");
            if (!StringUtils.isEmpty(backGroundQP)) {
                roiParam.setBackGroundQP(Integer.parseInt(backGroundQP));
            }

            final String backGroundSkipFlag = getTextOfChildTagFrom(roiParamElement, "BackGroundSkipFlag");
            if (!StringUtils.isEmpty(backGroundSkipFlag)) {
                roiParam.setBackGroundSkipFlag(Integer.parseInt(backGroundSkipFlag));
            }

            final Element itemElement = roiParamElement.element("Item");
            if (itemElement != null) {
                final ConfigDownload.SVACEncodeConfig.ROIParam.Item item = new ConfigDownload.SVACEncodeConfig.ROIParam.Item();
                final String roiSeq = getTextOfChildTagFrom(itemElement, "ROISeq");
                if (!StringUtils.isEmpty(roiSeq)) {
                    item.setRoiSeq(Integer.parseInt(roiSeq));
                }

                final String topLeft = getTextOfChildTagFrom(itemElement, "TopLeft");
                if (!StringUtils.isEmpty(topLeft)) {
                    item.setTopLeft(Integer.parseInt(topLeft));
                }

                final String bottomRight = getTextOfChildTagFrom(itemElement, "BottomRight");
                if (!StringUtils.isEmpty(bottomRight)) {
                    item.setBottomRight(Integer.parseInt(bottomRight));
                }

                final String roiQP = getTextOfChildTagFrom(itemElement, "ROIQP");
                if (!StringUtils.isEmpty(roiQP)) {
                    item.setRoiQP(Integer.parseInt(roiQP));
                }
                roiParam.setItem(item);
            }
        }
        return roiParam;
    }

    private ConfigDownload.SVACEncodeConfig.SVCParam extractSvcParamForEncode(Element svacEncodeConfigElement) {
        ConfigDownload.SVACEncodeConfig.SVCParam svcParam = null;
        final Element svcParamElement = svacEncodeConfigElement.element("SVCParam");
        if (svcParamElement != null) {
            svcParam = new ConfigDownload.SVACEncodeConfig.SVCParam();

            final String svcSpaceDomainMode = getTextOfChildTagFrom(svcParamElement, "SVCSpaceDomainMode");
            if (!StringUtils.isEmpty(svcSpaceDomainMode)) {
                svcParam.setSvcSpaceDomainMode(Integer.parseInt(svcSpaceDomainMode));
            }

            final String svcTimeDomainMode = getTextOfChildTagFrom(svcParamElement, "SVCTimeDomainMode");
            if (!StringUtils.isEmpty(svcTimeDomainMode)) {
                svcParam.setSvcTimeDomainMode(Integer.parseInt(svcTimeDomainMode));
            }

            final String svcSpaceSupportMode = getTextOfChildTagFrom(svcParamElement, "SVCSpaceSupportMode");
            if (!StringUtils.isEmpty(svcSpaceSupportMode)) {
                svcParam.setSvcSpaceSupportMode(Integer.parseInt(svcSpaceSupportMode));
            }

            final String svcTimeSupportMode = getTextOfChildTagFrom(svcParamElement, "SVCTimeSupportMode");
            if (!StringUtils.isEmpty(svcTimeSupportMode)) {
                svcParam.setSvcTimeSupportMode(Integer.parseInt(svcTimeSupportMode));
            }
        }
        return svcParam;
    }

    private ConfigDownload.SVACEncodeConfig.SurveillanceParam extractSurveillanceParamForEncode(Element svacEncodeConfigElement) {
        ConfigDownload.SVACEncodeConfig.SurveillanceParam surveillanceParam = null;
        final Element surveillanceParamElement = svacEncodeConfigElement.element("SurveillanceParam");
        if (surveillanceParamElement != null) {
            surveillanceParam = new ConfigDownload.SVACEncodeConfig.SurveillanceParam();

            final String timeFlag = getTextOfChildTagFrom(surveillanceParamElement, "TimeFlag");
            if (!StringUtils.isEmpty(timeFlag)) {
                surveillanceParam.setTimeFlag(Integer.parseInt(timeFlag));
            }

            final String eventFlag = getTextOfChildTagFrom(surveillanceParamElement, "EventFlag");
            if (!StringUtils.isEmpty(eventFlag)) {
                surveillanceParam.setEventFlag(Integer.parseInt(eventFlag));
            }

            final String alertFlag = getTextOfChildTagFrom(surveillanceParamElement, "AlertFlag");
            if (!StringUtils.isEmpty(alertFlag)) {
                surveillanceParam.setAlertFlag(Integer.parseInt(alertFlag));
            }
        }
        return surveillanceParam;
    }

    private ConfigDownload.SVACEncodeConfig.AudioParam extractAudioParam(Element svacEncodeConfigElement) {
        ConfigDownload.SVACEncodeConfig.AudioParam audioParam = null;
        final Element audioParamElement = svacEncodeConfigElement.element("AudioParam");
        if (audioParamElement != null) {
            audioParam = new ConfigDownload.SVACEncodeConfig.AudioParam();
            final String audioRecognitionFlag = getTextOfChildTagFrom(audioParamElement, "AudioRecognitionFlag");
            if (!StringUtils.isEmpty(audioRecognitionFlag)) {
                audioParam.setAudioRecognitionFlag(Integer.parseInt(audioRecognitionFlag));
            }
        }
        return audioParam;
    }

    private ConfigDownload.SVACDecodeConfig extractSVACDecodeConfig(Element svacDecodeConfigElement) {
        ConfigDownload.SVACDecodeConfig svacDecodeConfig = null;
        if (svacDecodeConfigElement != null) {
            svacDecodeConfig = new ConfigDownload.SVACDecodeConfig()
                    .setSvcParam(extractSvcParamForDecode(svacDecodeConfigElement))
                    .setSurveillanceParam(extractSurveillanceParamForDecode(svacDecodeConfigElement));
        }
        return svacDecodeConfig;
    }

    private ConfigDownload.SVACDecodeConfig.SVCParam extractSvcParamForDecode(Element svacDecodeConfigElement) {
        ConfigDownload.SVACDecodeConfig.SVCParam svcParam = null;
        final Element svcParamElement = svacDecodeConfigElement.element("SVCParam");
        if (svcParamElement != null) {
            svcParam = new ConfigDownload.SVACDecodeConfig.SVCParam();

            final String svcSpaceSupportMode = getTextOfChildTagFrom(svcParamElement, "SVCSpaceSupportMode");
            if (!StringUtils.isEmpty(svcSpaceSupportMode)) {
                svcParam.setSvcSpaceSupportMode(Integer.parseInt(svcSpaceSupportMode));
            }

            final String svcTimeSupportMode = getTextOfChildTagFrom(svcParamElement, "SVCTimeSupportMode");
            if (!StringUtils.isEmpty(svcTimeSupportMode)) {
                svcParam.setSvcTimeSupportMode(Integer.parseInt(svcTimeSupportMode));
            }
        }
        return svcParam;
    }

    private ConfigDownload.SVACDecodeConfig.SurveillanceParam extractSurveillanceParamForDecode(Element svacDecodeConfigElement) {
        ConfigDownload.SVACDecodeConfig.SurveillanceParam surveillanceParam = null;
        final Element surveillanceParamElement = svacDecodeConfigElement.element("SurveillanceParam");
        if (surveillanceParamElement != null) {
            surveillanceParam = new ConfigDownload.SVACDecodeConfig.SurveillanceParam();

            final String timeFlag = getTextOfChildTagFrom(surveillanceParamElement, "TimeShowFlag");
            if (!StringUtils.isEmpty(timeFlag)) {
                surveillanceParam.setTimeShowFlag(Integer.parseInt(timeFlag));
            }

            final String eventFlag = getTextOfChildTagFrom(surveillanceParamElement, "EventShowFlag");
            if (!StringUtils.isEmpty(eventFlag)) {
                surveillanceParam.setEventShowFlag(Integer.parseInt(eventFlag));
            }

            final String alertFlag = getTextOfChildTagFrom(surveillanceParamElement, "AlerShowtFlag");
            if (!StringUtils.isEmpty(alertFlag)) {
                surveillanceParam.setAlertShowFlag(Integer.parseInt(alertFlag));
            }
        }
        return surveillanceParam;
    }
}
