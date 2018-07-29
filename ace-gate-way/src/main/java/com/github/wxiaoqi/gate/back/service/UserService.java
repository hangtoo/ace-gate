package com.github.wxiaoqi.gate.back.service;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.gate.agent.rest.ApiGateSecurity;
import com.github.wxiaoqi.gate.agent.vo.authority.PermissionInfo;
import com.github.wxiaoqi.gate.agent.vo.user.UserInfo;
import com.github.wxiaoqi.gate.back.biz.ElementBiz;
import com.github.wxiaoqi.gate.back.biz.MenuBiz;
import com.github.wxiaoqi.gate.back.biz.UserBiz;
import com.github.wxiaoqi.gate.back.constant.CommonConstant;
import com.github.wxiaoqi.gate.back.entity.Element;
import com.github.wxiaoqi.gate.back.entity.Menu;
import com.github.wxiaoqi.gate.back.entity.User;
import com.github.wxiaoqi.gate.back.vo.MenuTree;
import com.github.wxiaoqi.gate.common.util.TreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-21 8:15
 */
@Controller
@RequestMapping("api")
public class UserService {
    @Autowired
    private UserBiz userBiz;
    @Autowired
    private MenuBiz menuBiz;
    @Autowired
    private ElementBiz elementBiz;

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    /**
     * 根据用户账号获取用户
     * @param username
     * @return
     */
    @RequestMapping(value = "/user/username/{username}",method = RequestMethod.GET, produces="application/json")
    public  @ResponseBody
    UserInfo getUserByUsername(@PathVariable("username")String username) {
        UserInfo info = new UserInfo();
        User user = userBiz.getUserByUsername(username);
        BeanUtils.copyProperties(user,info);
        info.setId(user.getId().toString());
        return info;
    }

    /**
     * 根据用户账号获取权限
     * @param username
     * @return
     */
    @RequestMapping(value = "/user/un/{username}/permissions", method = RequestMethod.GET)
    public @ResponseBody List<PermissionInfo> getPermissionByUsername(@PathVariable("username") String username){
        User user = userBiz.getUserByUsername(username);
        List<Menu> menus = menuBiz.getUserAuthorityMenuByUserId(user.getId());
        List<PermissionInfo> result = new ArrayList<PermissionInfo>();
        PermissionInfo info = null;
        for(Menu menu:menus){
            if(StringUtils.isBlank(menu.getHref()))
                continue;
            info = new PermissionInfo();
            info.setCode(menu.getCode());
            info.setType(CommonConstant.RESOURCE_TYPE_MENU);
            info.setName(CommonConstant.RESOURCE_ACTION_VISIT);
            String uri = menu.getHref();
            if(!uri.startsWith("/"))
                uri = "/"+uri;
            info.setUri(uri);
            info.setMethod(CommonConstant.RESOURCE_REQUEST_METHOD_GET);
            result.add(info
            );
            info.setMenu(menu.getTitle());
        }
        List<Element> elements = elementBiz.getAuthorityElementByUserId(user.getId()+"");
        for(Element element:elements){
            info = new PermissionInfo();
            info.setCode(element.getCode());
            info.setType(element.getType());
            info.setUri(element.getUri());
            info.setMethod(element.getMethod());
            info.setName(element.getName());
            info.setMenu(element.getMenuId());
            result.add(info);
        }
        return result;
    }
    @ApiGateSecurity
    @RequestMapping(value = "/user/un/{username}/system", method = RequestMethod.GET)
    @ResponseBody
    public String getSystemsByUsername(@PathVariable("username") String username){
        int userId = userBiz.getUserByUsername(username).getId();
        return JSONObject.toJSONString(menuBiz.getUserAuthoritySystemByUserId(userId));
    }

    /**
     * 通过用户账号获取菜单
     * @param username
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/user/un/{username}/menu/parent/{parentId}", method = RequestMethod.GET)
    @ResponseBody
    public String getMenusByUsername(@PathVariable("username") String username,@PathVariable("parentId")Integer parentId){
        int userId = userBiz.getUserByUsername(username).getId();
        try {
            if (parentId == null||parentId<0) {
                List<Menu> list=menuBiz.getUserAuthoritySystemByUserId(userId);
                if(list.size()>0) {
                    parentId = menuBiz.getUserAuthoritySystemByUserId(userId).get(0).getId();
                }else{
                    return JSONObject.toJSONString(new ArrayList<MenuTree>());
                }
            }
        } catch (Exception e) {
            logger.error("通过用户账号获取菜单异常",e);
            return JSONObject.toJSONString(new ArrayList<MenuTree>());
        }
        return JSONObject.toJSONString(getMenuTree(menuBiz.getUserAuthorityMenuByUserId(userId), parentId));
    }

    /**
     * 获取菜单树
     * @param menus
     * @param root
     * @return
     */
    private List<MenuTree> getMenuTree(List<Menu> menus,int root) {
        List<MenuTree> trees = new ArrayList<MenuTree>();
        MenuTree node = null;
        for (Menu menu : menus) {
            node = new MenuTree();
            BeanUtils.copyProperties(menu, node);
            trees.add(node);
        }
        return TreeUtil.bulid(trees, root) ;
    }
}
