package com.hlcy.yun.gb28181.service.params;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ScanParams extends DeviceParams {
    private ScanType type;
    private int scanGroupNum;
    private int scanSpeed;

    @Getter
    public enum ScanType {
        START_AUTO_SCAN(0x89, 0x00),
        STOP_AUTO_SCAN(0x00, 0x00),
        SET_LEFT_MARGIN(0x89,0x01),
        SET_RIGHT_MARGIN(0x89, 0x02),
        SET_AUTO_SCAN_SPEED(0x8A, 0x05);

        private int bit4;
        private int bit6;

        ScanType(int bit4, int bit6) {
            this.bit4 = bit4;
            this.bit6 = bit6;
        }
    }
}
