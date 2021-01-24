package com.hlcy.yun.gb28181.operation.command.control;


import com.hlcy.yun.gb28181.operation.params.DragZoomParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;

/**
 * 拖动放大/缩放指令
 */
public class DragZoomCmd extends AbstractControlCmd<DragZoomParams> {
    private final String InStartTag = "<DragZoomIn>";
    private final String InEndTag = "</DragZoomIn>";
    private final String OutStartTag = "<DragZoomOut>";
    private final String OutEndTag = "</DragZoomOut>";

    public DragZoomCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(DragZoomParams dragZoomParams) {

        return new StringBuilder(200)
                .append(getStartTag(dragZoomParams.getZoomType()))
                .append("<Length>").append(dragZoomParams.getWindowLength()).append("</Length>")
                .append("<Width>").append(dragZoomParams.getWindowWidth()).append("</Width>")
                .append("<MidPointX>").append(dragZoomParams.getMidPointX()).append("</MidPointX>")
                .append("<MidPointY>").append(dragZoomParams.getMidPointY()).append("</MidPointY>")
                .append("<LengthX>").append(dragZoomParams.getLengthX()).append("</LengthX>")
                .append("<LengthY>").append(dragZoomParams.getLengthY()).append("</Length>")
                .append(getEndTag(dragZoomParams.getZoomType()))
                .toString();
    }

    private String getStartTag(DragZoomParams.ZoomType zoomType) {
        return DragZoomParams.ZoomType.IN == zoomType ? InStartTag : OutStartTag;
    }

    private String getEndTag(DragZoomParams.ZoomType zoomType) {
        return DragZoomParams.ZoomType.IN == zoomType ? InEndTag : OutEndTag;
    }
}
