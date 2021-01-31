package com.hlcy.yun.gb28181.service.command.control;


import com.hlcy.yun.gb28181.service.params.control.DragZoomControlParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;

/**
 * 拖动放大/缩放指令
 */
public class DragZoomCmd extends AbstractControlCmd<DragZoomControlParams> {
    private final String InStartTag = "<DragZoomIn>";
    private final String InEndTag = "</DragZoomIn>";
    private final String OutStartTag = "<DragZoomOut>";
    private final String OutEndTag = "</DragZoomOut>";

    public DragZoomCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(DragZoomControlParams dragZoomParams) {

        return new StringBuilder(200)
                .append(getStartTag(dragZoomParams.getZoomType()))
                .append("<Length>").append(dragZoomParams.getWindowLength()).append("</Length>")
                .append("<Width>").append(dragZoomParams.getWindowWidth()).append("</Width>")
                .append("<MidPointX>").append(dragZoomParams.getMidPointX()).append("</MidPointX>")
                .append("<MidPointY>").append(dragZoomParams.getMidPointY()).append("</MidPointY>")
                .append("<LengthX>").append(dragZoomParams.getLengthX()).append("</LengthX>")
                .append("<LengthY>").append(dragZoomParams.getLengthY()).append("</LengthY>")
                .append(getEndTag(dragZoomParams.getZoomType()))
                .toString();
    }

    private String getStartTag(DragZoomControlParams.ZoomType zoomType) {
        return DragZoomControlParams.ZoomType.IN == zoomType ? InStartTag : OutStartTag;
    }

    private String getEndTag(DragZoomControlParams.ZoomType zoomType) {
        return DragZoomControlParams.ZoomType.IN == zoomType ? InEndTag : OutEndTag;
    }
}
