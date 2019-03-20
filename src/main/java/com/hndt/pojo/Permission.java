package com.hndt.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hndt.enums.StatusEnum;
import com.hndt.util.EnumUtil;
import lombok.Data;

/**
 * 权限
 *
 * @author Hystar
 * @date 2018/1/9
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Permission {
    /**
     * 权限ID
     */
    private Long id;
    /**
     * 权限名称
     */
    private String name;
    /**
     * URL
     */
    private String url;
    /**
     * 状态 可用、不可用
     */
    private Integer status;

    public Permission() {
    }

    public Permission(Long id, String name, String url, Integer status) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.status = status;
    }

    @JsonIgnore
    public String getStatusValue() {
        return EnumUtil.getByCode(status, StatusEnum.class).getValue();
    }
}