package com.zhangbin.yun.yunrights.modules.rights.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DeptSmallDTO implements Serializable {

    private Long id;

    private String deptName;
}
