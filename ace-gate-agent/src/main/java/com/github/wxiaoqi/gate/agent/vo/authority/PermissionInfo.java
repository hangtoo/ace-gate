package com.github.wxiaoqi.gate.agent.vo.authority;

import java.io.Serializable;

/**
 * 权限，拥有一定数量资源的集成，亦可以是资源的载体
 * @author wanghaobin
 * @create 2017-06-22 15:19
 */
public class PermissionInfo implements Serializable{
    /**
     * 编码
     */
    private String code;
    /**
     * 类型
     */
    private String type;
    /**
     * 地址
     */
    private String uri;
    /**
     * 方法
     */
    private String method;
    /**
     * 名称
     */
    private String name;
    /**
     * 菜单
     */
    private String menu;

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
