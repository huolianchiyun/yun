package com.hlcy.yun.admincenter.model.$do;

import com.hlcy.yun.common.model.BaseDO;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * 树结构存储表，每条记录为树的节点（根节点、枝节点、叶子节点）
 * 表 t_vm_tree_node
 * @author ASUS
 * @date 2020-12-11 21:22:52
 */
@Getter
@Setter
public class TreeNode extends BaseDO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 父节点ID
     */
    private Long pid;

    /**
     * 节点名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 设备ID
     */
    private String deviceId;

}
