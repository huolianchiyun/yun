package com.hlcy.yun;

import com.hlcy.yun.common.spring.redis.RedisUtils;
import com.hlcy.yun.gb28181.service.params.player.PlayParams;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.Operation;
import com.hlcy.yun.gb28181.ssrc.SSRCManger;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = CuckooApplication.class)
public class SSRCTest {

    @Autowired
    RedisUtils redisUtils;

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

    @Test
    public void testStoreContext2Redis() {
        Assert.assertTrue(redisUtils.hset("H::SSRC:FlowContext", "1234567890", new FlowContext(Operation.PLAY, new PlayParams())));
        Assert.assertNotNull(redisUtils.hget("H::SSRC:FlowContext", "1234567890"));
        final FlowContext context = (FlowContext) redisUtils.hget("H::SSRC:FlowContext", "1234567890");
        System.out.println(context);
    }
}
