package com.github.wxiaoqi.gate.agent.vo.log;

import java.io.Serializable;
import java.util.Date;

/**
 * 日志
 *
 * @author wanghaobin
 * @create 2017-07-01 11:18
 */
public class LogInfo implements Serializable{
    /**
     * 菜单
     */
    private String menu;
    /**
     * 操作
     */
    private String opt;
    /**
     * 资源路径
     */
    private String uri;
    /**
     * 创建时间
     */
    private Date crtTime;

    /**
     * 操作人ID
     */
    private String crtUser;

    /**
     * 操作人
     */
    private String crtName;

    /**
     * 操作主机
     */
    private String crtHost;

    public LogInfo(String menu, String option, String uri,  Date crtTime, String crtUser, String crtName, String crtHost) {
        this.menu = menu;
        this.opt = option;
        this.uri = uri;
        this.crtTime = crtTime;
        this.crtUser = crtUser;
        this.crtName = crtName;
        this.crtHost = crtHost;
    }

    public LogInfo() {
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getOpt() {
        return opt;
    }

    public void setOpt(String option) {
        this.opt = option;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Date getCrtTime() {
        return crtTime;
    }

    public void setCrtTime(Date crtTime) {
        this.crtTime = crtTime;
    }

    public String getCrtUser() {
        return crtUser;
    }

    public void setCrtUser(String crtUser) {
        this.crtUser = crtUser;
    }

    public String getCrtName() {
        return crtName;
    }

    public void setCrtName(String crtName) {
        this.crtName = crtName;
    }

    public String getCrtHost() {
        return crtHost;
    }

    public void setCrtHost(String crtHost) {
        this.crtHost = crtHost;
    }
}
