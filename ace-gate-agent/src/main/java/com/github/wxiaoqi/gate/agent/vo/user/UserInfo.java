package com.github.wxiaoqi.gate.agent.vo.user;

import java.io.Serializable;

/**
 * 用户
 *
 * @author wanghaobin
 * @create 2017-06-21 8:12
 */
public class UserInfo implements Serializable{
    /**
     * 编号
     */
    public String id;
    /**
     * 账号
     */
    public String username;
    /**
     * 密码
     */
    public String password;
    /**
     * 名称
     */
    public String name;
    /**
     * 描述
     */
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
