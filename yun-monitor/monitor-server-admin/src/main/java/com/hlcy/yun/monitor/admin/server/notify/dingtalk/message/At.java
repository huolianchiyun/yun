package com.hlcy.yun.monitor.admin.server.notify.dingtalk.message;

import java.util.List;

class At {
    private boolean isAtAll;
    private List<String> atMobiles;

    public boolean isAtAll() {
        return isAtAll;
    }

    public void setAtAll(boolean atAll) {
        isAtAll = atAll;
    }

    public List<String> getAtMobiles() {
        return atMobiles;
    }

    public void setAtMobiles(List<String> atMobiles) {
        this.atMobiles = atMobiles;
    }
}
