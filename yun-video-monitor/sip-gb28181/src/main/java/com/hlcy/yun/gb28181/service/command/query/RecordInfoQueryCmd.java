package com.hlcy.yun.gb28181.service.command.query;

import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.params.query.RecordInfoQueryParams;

import java.time.LocalDateTime;

/**
 * 设备录像（历史视频）查询
 */
public class RecordInfoQueryCmd extends AbstractQueryCmd<RecordInfoQueryParams> {
    public RecordInfoQueryCmd(GB28181Properties properties) {
        super(properties);
    }


    @Override
    protected String buildCmdXML(RecordInfoQueryParams params) {
        final StringBuilder cmd = new StringBuilder(200)
                .append("<StartTime>").append(params.getStartTime().withNano(0)).append("</StartTime>")
                .append("<EndTime>").append(params.getStartTime().withNano(0)).append("</EndTime>");
        final String filePath = params.getFilePath();
        if (filePath != null && !filePath.isEmpty()) {
            cmd.append("<FilePath>").append(filePath).append("</FilePath>");
        }
        cmd.append("<Address>Address 1</Address>")
                .append("<Secrecy>").append(params.getSecrecy()).append("</Secrecy>")
                .append("<Type>").append(params.getType()).append("</Type>");
        final String recorderID = params.getRecorderID();
        if (recorderID != null && !recorderID.isEmpty()) {
            cmd.append("<RecorderID>").append(recorderID).append("</RecorderID>");
        }
        cmd.append("<IndistinctQuery>").append(params.getIndistinctQuery()).append("</IndistinctQuery>");
        return cmd.toString();
    }
}
