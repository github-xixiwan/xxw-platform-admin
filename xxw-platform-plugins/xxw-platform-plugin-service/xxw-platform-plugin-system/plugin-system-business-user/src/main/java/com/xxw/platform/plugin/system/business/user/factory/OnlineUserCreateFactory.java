package com.xxw.platform.plugin.system.business.user.factory;
import com.xxw.platform.frame.common.enums.SexEnum;
import com.xxw.platform.plugin.auth.api.pojo.login.LoginUser;
import com.xxw.platform.plugin.system.api.pojo.user.OnlineUserDTO;

/**
 * 当前在线用户的创建工厂
 *
 * @author liaoxiting
 * @date 2021/1/11 22:31
 */
public class OnlineUserCreateFactory {

    /**
     * 登录用户转化为在线用户
     *
     * @author liaoxiting
     * @date 2021/1/11 22:31
     */
    public static OnlineUserDTO createOnlineUser(LoginUser loginUser) {

        OnlineUserDTO onlineUserResponse = new OnlineUserDTO();

        onlineUserResponse.setUserId(loginUser.getUserId());
        onlineUserResponse.setAccount(loginUser.getAccount());
        onlineUserResponse.setSex(SexEnum.codeToMessage(loginUser.getSimpleUserInfo().getSex()));
        onlineUserResponse.setRealName(loginUser.getSimpleUserInfo().getRealName());
        onlineUserResponse.setNickName(loginUser.getSimpleUserInfo().getNickName());

        if (loginUser.getSimpleRoleInfoList() != null && loginUser.getSimpleRoleInfoList().size() > 0) {
            onlineUserResponse.setRoleName(loginUser.getSimpleRoleInfoList().get(0).getRoleName());
        }

        onlineUserResponse.setLoginTime(loginUser.getLoginTime());
        onlineUserResponse.setToken(loginUser.getToken());

        return onlineUserResponse;
    }

}
