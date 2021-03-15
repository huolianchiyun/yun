package com.hlcy.yun.gb28181.bean;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


@Getter
@Setter
@Accessors(chain = true)
public class ConfigDownload {

    private String deviceId;

    private BasicParam basicParam;

    private VideoParamOpt videoParamOp;

    private SVACEncodeConfig svacEncodeConfig;

    private SVACDecodeConfig svacDecodeConfig;


    /**
     * 基本参数
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class BasicParam{

        private String deviceName;
        /**
         * 注册过期时间
         */
        private String expiration;
        /**
         * 心跳间隔时间
         */
        private int heartBeatInterval;

        /**
         * 心跳超时次数
         */
        private int heartBeatCount;

        /**
         * 定位功能支持情况
         * 取值: 0-不支持; 1-支持GPS定位; 2-支持北斗定位(可选,默认取值为 0)
         */
        private int positionCapability;

        /**
         * 经度
         */
        private double longitude;

        /**
         * 纬度
         */
        private double latitude;

    }

    /**
     * 视频参数范围
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class VideoParamOpt {
        /**
         * 载倍速范围(可选),各可选参数以“/”分隔, 如设备支持 1,2,4 倍速下载则应写为 “1/2/4”
         */
        private String downloadSpeed;

        /**
         * 摄像机支持的分辨率(可选),可有多个分辨率值,各个取值间以“/”分隔。分辨率取值参见附录F中 SDP f字段规定
         */
        private String resolution;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class SVACEncodeConfig{

        /**
         * 感兴趣区域参数
         */
        @Getter
        @Setter
        @Accessors(chain = true)
        class ROIParam{
            /**
             * 感兴趣区域开关,取值0:关闭,1:打开
             */
            private int roiFlag;

            /**
             * 感兴趣区域数量,取值范围 0~16
             */
            private int roiNumber;

            private Item item;

            /**
             * 背景区域编码质量等级,取值 0:一般,1:较好,2:好,3:很好
             */
            private int backGroundQP;

            /**
             * 背景跳过开关,取值 0:关闭,1:打开
             */
            private int backGroundSkipFlag;


            /**
             * 感兴趣区域
             */
            @Getter
            @Setter
            @Accessors(chain = true)
            class Item{
                /**
                 * 感兴趣区域编号,取值范围 1~16
                 */
                 private int roiSeq;

                /**
                 * 感兴趣区域左上角坐标,参考 GB/T25724—2010的5.2.4.4.2定义,取值范围 0~19683
                 */
                private int topLeft;

                /**
                 * 感兴趣区域右下角坐标,参考 GB/T25724—2010的 5.2.4.4.2定义,取值范围 0~19683
                 */
                private int bottomRight;

                /**
                 * ROI区域编码质量等级,取值 0:一般,1:较好,2:好,3:很好
                 */
                private int roiQP;

            }

        }

        @Getter
        @Setter
        @Accessors(chain = true)
        class SVCParam{

            /**
             * 空域编码方式,取值 0:基本层;1:1级增强(1个增强层);2:2级增强(2个增强层);3:3级增强(3个增强层)
             */
            private int svcSpaceDomainMode;

            /**
             * 时域编码方式,取值 0:基本层;1:1级增强;2:2级增强;3:3级增强
             */
            private int svcTimeDomainMode;

            /**
             * 空域编码能力,取值 0:不支持;1:1级增强(1个增强层);2:2级增强(2个增强层);3:3级增强(3个增强层)
             */
            private int svcSpaceSupportMode;

            /**
             * 时域编码能力,取值 0:不支持;1:1级增强;2:2级增强;3:3级增强
             */
            private int svcTimeSupportMode;

        }

        /**
         * 监控专用信息参数
         */
        @Getter
        @Setter
        @Accessors(chain = true)
        class SurveillanceParam{

            /**
             * 绝对时间信息开关,取值 0:关闭,1:打开
             */
            private int timeFlag;

            /**
             * 监控事件信息开关,取值0:关闭,1:打开
             */
            private int eventFlag;

            /**
             * 报警信息开关,取值0:关闭,1:打开
             */
            private int alertFlag;
        }

        @Getter
        @Setter
        @Accessors(chain = true)
        class AudioParam{
            /**
             * 声音识别特征参数开关,取值 0:关闭,1:打开
             */
            private int audioRecognitionFlag;
        }
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    public static  class SVACDecodeConfig{

        @Getter
        @Setter
        @Accessors(chain = true)
        class SVCParam{
            /**
             * 空域编码能力,取值 0:不支持; 1:1级增强(1个增强层); 2:2级增强(2个增强层); 3:3级增强(3个增强层)
             */
            private int svcSpaceSupportMode;

            /**
             * 时域编码能力,取值0:不支持;1:1级增强;2:2级增强;3:3级增强
             */
            private int svcTimeSupportMode;
        }

        /**
         * 监控专用信息参数
         */
        @Getter
        @Setter
        @Accessors(chain = true)
        class SurveillanceParam{

            /**
             * 绝对时间信息显示开关,取值 0:关闭, 1:打开
             */
            private int timeShowFlag;

            /**
             * 监控事件信息显示开关,取值0:关闭,1:打开
             */
            private int eventShowFlag;

            /**
             * 报警信息显示开关,取值 0:关闭, 1:打开
             */
            private int alertShowFlag;
        }
    }
}
