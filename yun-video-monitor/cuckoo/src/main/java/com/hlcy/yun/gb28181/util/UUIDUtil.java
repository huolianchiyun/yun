package com.hlcy.yun.gb28181.util;

import java.util.UUID;

public final class UUIDUtil {
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
