package com.xxw.platform.plugin.system.api;

import com.xxw.platform.plugin.auth.api.pojo.login.LoginUser;
import com.xxw.platform.plugin.system.api.pojo.user.OAuth2AuthUserDTO;
import com.xxw.platform.plugin.system.api.pojo.user.OnlineUserDTO;
import com.xxw.platform.plugin.system.api.pojo.user.SysUserDTO;
import com.xxw.platform.plugin.system.api.pojo.user.UserLoginInfoDTO;
import com.xxw.platform.plugin.system.api.pojo.user.request.OnlineUserRequest;
import com.xxw.platform.plugin.system.api.pojo.user.request.SysUserRequest;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 用户管理服务接口
 *
 * @author liaoxiting
 * @date 2020/10/20 13:20
 */
public interface UserServiceApi {

    /**
     * 获取用户登录需要的详细信息（用在第一次获取登录用户）
     *
     * @param account 账号
     * @return 用户登录需要的详细信息
     * @author liaoxiting
     * @date 2020/10/20 16:59
     */
    UserLoginInfoDTO getUserLoginInfo(String account);

    /**
     * 获取刷新后的登录用户（用在用户登录之后调用）
     * <p>
     * 以往用户登录后直接从session缓存中获取用户信息，不能及时更新，需要退出才可以获取最新信息
     * <p>
     * 本方法解决了实时获取当前登录用户不准确的问题
     *
     * @author liaoxiting
     * @date 2021/7/29 22:03
     */
    LoginUser getEffectiveLoginUser(LoginUser loginUser);

    /**
     * 更新用户的登录信息，一般为更新用户的登录时间和ip
     *
     * @param userId 用户id
     * @param date   用户登录时间
     * @param ip     用户登录的ip
     * @author liaoxiting
     * @date 2020/10/21 14:13
     */
    void updateUserLoginInfo(Long userId, Date date, String ip);

    /**
     * 根据机构id集合删除对应的用户-数据范围关联信息
     *
     * @param organizationIds 组织架构id集合
     * @author liaoxiting
     * @date 2020/10/20 16:59
     */
    void deleteUserDataScopeListByOrgIdList(Set<Long> organizationIds);

    /**
     * 获取用户的角色id集合
     *
     * @param userId 用户id
     * @return 角色id集合
     * @author liaoxiting
     * @date 2020/11/5 下午3:57
     */
    List<Long> getUserRoleIdList(Long userId);

    /**
     * 根据角色id删除对应的用户-角色表关联信息
     *
     * @param roleId 角色id
     * @author liaoxiting
     * @date 2020/11/5 下午3:58
     */
    void deleteUserRoleListByRoleId(Long roleId);

    /**
     * 获取用户单独绑定的数据范围，sys_user_data_scope表中的数据范围
     *
     * @param userId 用户id
     * @return 用户数据范围
     * @author liaoxiting
     * @date 2020/11/21 12:15
     */
    List<Long> getUserBindDataScope(Long userId);

    /**
     * 获取在线用户列表
     *
     * @author liaoxiting
     * @date 2021/1/10 9:56
     */
    List<OnlineUserDTO> onlineUserList(OnlineUserRequest onlineUserRequest);

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId 用户ID
     * @author liaoxiting
     * @date 2021/1/9 19:00
     */
    SysUserDTO getUserInfoByUserId(Long userId);

    /**
     * 根据用户ID列表获取用户信息集合
     *
     * @param userIdSet 用户id集合
     * @return 返回用户所有信息
     * @author liaoxiting
     * @date 2022/9/25 10:14
     */
    List<SysUserDTO> getUserInfoList(List<Long> userIdSet);

    /**
     * 查询全部用户ID(剔除被删除的)
     *
     * @param sysUserRequest 查询参数
     * @return List<Long> 用户id 集合
     * @author liaoxiting
     * @date 2021/1/4 22:09
     */
    List<Long> queryAllUserIdList(SysUserRequest sysUserRequest);

    /**
     * 根据用户id 判断用户是否存在
     *
     * @param userId 用户id
     * @return 用户信息
     * @author liaoxiting
     * @date 2021/1/4 22:55
     */
    Boolean userExist(Long userId);

    /**
     * 获取用户的头像url
     *
     * @author liaoxiting
     * @date 2021/12/29 17:27
     */
    String getUserAvatarUrlByUserId(Long userId);

    /**
     * 创建并保存OAuth2用户信息
     *
     * @author liaoxiting
     * @date 2022/7/1 19:03
     */
    SysUserDTO createAndSaveOAuth2User(OAuth2AuthUserDTO oAuth2AuthUserDTO);

}
