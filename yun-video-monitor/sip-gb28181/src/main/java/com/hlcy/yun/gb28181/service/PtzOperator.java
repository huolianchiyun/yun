package com.hlcy.yun.gb28181.service;

import com.hlcy.yun.gb28181.bean.api.PtzParams;

/**
 * 云台操作器
 */
public interface PtzOperator {

    void control(PtzParams ptzParams);

}
