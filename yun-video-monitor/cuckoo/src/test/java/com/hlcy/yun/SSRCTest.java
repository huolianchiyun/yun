package com.hlcy.yun;

import com.hlcy.yun.gb28181.ssrc.SSRCManger;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SSRCTest {

    @Test
    public void testSsrcRepeatability() {
        final String playSSRC = SSRCManger.getPlaySSRC();
        System.out.println(playSSRC);
        SSRCManger.releaseSSRC(playSSRC);

        final String playSSRC1 = SSRCManger.getPlaySSRC();
        System.out.println(playSSRC1);
        SSRCManger.releaseSSRC(playSSRC1);
        Assert.assertNotEquals(playSSRC, playSSRC1);
    }
}
