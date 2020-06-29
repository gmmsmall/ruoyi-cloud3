package com.ruoyi.system.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.exception.RuoyiException;
import com.ruoyi.common.redis.annotation.RedisCache;
import com.ruoyi.common.redis.util.JWTUtil;
import com.ruoyi.common.redis.util.RedisUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.security.Md5Utils;
import com.ruoyi.system.domain.Aos;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.domain.Token;
import com.ruoyi.system.feign.RemoteIBlockRoleService;
import com.ruoyi.system.feign.RemoteIBlockUserService;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.params.QueryUserParams;
import com.ruoyi.system.params.UserParams;
import com.ruoyi.system.result.*;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.util.DateUtil;
import com.ruoyi.system.util.TokenTreeUtil;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 用户 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysUserServiceImpl implements ISysUserService {
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private RemoteIBlockUserService remoteIBlockUserService;

    @Autowired
    private RemoteIBlockRoleService remoteIBlockRoleService;

    @Autowired
    private RedisUtils redis;

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    @RedisCache(key = "user_perms", fieldKey = "#userId")
    public List<PermsResult> selectPermsByUserId(Long userId) {
        String result = remoteIBlockUserService.queryUserToken(String.valueOf(userId));
        List<Token> tokenList = null;
        if (null != result) {
            FabricResult fabricResult = JSON.parseObject(result, FabricResult.class);
            if (fabricResult.getCode() == FabricResult.RESULT_SUCC) {
                tokenList = fabricResult.getTokenList();
            }
        } else {
            throw new RuoyiException(Constants.CHANAL_CONNECTED_FAILED, 500);
        }
        List<PermsResult> permsResults = new ArrayList<>();
        if (null != tokenList) {
            for (Token token : tokenList) {
                if (StringUtils.isNotEmpty(token.getPerms())) {
                    permsResults.add(new PermsResult(token.getPerms().trim().split(",")));
                }
            }
        }
        return permsResults;
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public ListResult<SysUserResult> selectList(QueryUserParams queryUserParams) {
        List<String> userIds = null;
        if (!StringUtil.isNullOrEmpty(queryUserParams.getRoleName())) {
            String result = remoteIBlockRoleService.queryIdsByRoleName(queryUserParams.getRoleName());
            if (null != result) {
                FabricResult fabricResult = JSON.parseObject(result, FabricResult.class);
                if (fabricResult.getCode() == FabricResult.RESULT_SUCC && fabricResult.getUserIds() != null) {
                    userIds = fabricResult.getUserIds();
                }
            } else {
                throw new RuoyiException(Constants.CHANAL_CONNECTED_FAILED, 500);
            }
        }
        SysUser user = new SysUser();
        BeanUtils.copyProperties(queryUserParams, user);
        String userids = null;
        if (!StringUtil.isNullOrEmpty(queryUserParams.getRoleName()) && null == userIds) {
            return ListResult.list(new ArrayList<>(), 0L, queryUserParams);
        }
        if (null != userIds) {
            userids = Joiner.on(",").join(userIds);
            user.setUserIds(userids);
        }

        Long total = userMapper.selectCount(userids);
//        if (null != queryUserParams.getUserId())
//            user.setUserId(queryUserParams.getUserId());
        if (!StringUtil.isNullOrEmpty(queryUserParams.getUserName()))
            user.setUserName(queryUserParams.getUserName());

        String limit = "limit " + (queryUserParams.getPageNum() - 1) * queryUserParams.getPageSize() + "," + queryUserParams.getPageSize();
        user.setLimit(limit);
        List<SysUser> sysUsers = userMapper.selectUserList(user);
        List<SysUserResult> sysUserResults = new ArrayList<>();
        for (SysUser sysUser : sysUsers) {
            SysUserResult sysUserResult = new SysUserResult();
            sysUserResult.setUserId(sysUser.getUserId());
            sysUserResult.setUserName(sysUser.getUserName());
            sysUserResult.setPhonenumber(sysUser.getPhonenumber());
            sysUserResult.setRoleIds(new Long[0]);
            String result = remoteIBlockUserService.queryUserRole(String.valueOf(sysUser.getUserId()));
            if (null != result) {
                FabricResult fabricResult = JSON.parseObject(result, FabricResult.class);
                if (fabricResult.getCode() == FabricResult.RESULT_SUCC && fabricResult.getRoleList() != null) {
                    int size = fabricResult.getRoleList().size();
                    List<Long> roleIds = new ArrayList<>();
                    List<String> roleNames = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        if (fabricResult.getRoleList().get(i).getRoleId() != 0) {
                            roleIds.add(fabricResult.getRoleList().get(i).getRoleId());
                            roleNames.add(fabricResult.getRoleList().get(i).getRoleName());
                        }
                    }
                    sysUserResult.setRoleIds(roleIds.toArray(new Long[0]));
                    sysUserResult.setRoleName(Joiner.on(",").join(roleNames));
                }
            } else {
                throw new RuoyiException(Constants.CHANAL_CONNECTED_FAILED, 500);
            }
            sysUserResult.setStatus(sysUser.getStatus());
            sysUserResult.setCreateTime(DateUtil.getDateFormat(sysUser.getCreateTime(), DateUtil.FULL_TIME_SPLIT_PATTERN));
            sysUserResults.add(sysUserResult);
        }
        return ListResult.list(sysUserResults, total, queryUserParams);

    }

    @Override
    public TokenTreeResult<TokenPermsResult> tokenList() {
        Long userId = JWTUtil.getUser().getUserId();
        //权限信息
        List<TokenPermsResult> tokenList = new ArrayList<>();
        String tokenResult = remoteIBlockUserService.queryUserToken(String.valueOf(userId));
        if (tokenResult != null) {
            FabricResult tokenFabricResult = JSON.parseObject(tokenResult, FabricResult.class);
            if (tokenFabricResult.getCode() == FabricResult.RESULT_SUCC && tokenFabricResult.getTokenList() != null) {
                for (Token t : tokenFabricResult.getTokenList()) {
                    TokenPermsResult tokenPermsResult = new TokenPermsResult();
                    BeanUtils.copyProperties(t, tokenPermsResult);
                    if (!tokenList.contains(tokenPermsResult)) {
                        tokenList.add(tokenPermsResult);
                    }
                }
            }
        }
        List<TokenTreeResult<TokenPermsResult>> trees = new ArrayList<>();
        tokenList.forEach(token -> {
            TokenTreeResult<TokenPermsResult> tree = new TokenTreeResult<>();
            tree.setTokenNo(token.getTokenNo());
            tree.setParentNo(token.getParentNo());
            tree.setName(token.getName());
            tree.setPerms(token.getPerms());
            tree.setRoute(token.getRoute());
            trees.add(tree);
        });
        TokenTreeResult<TokenPermsResult> tokenTree = TokenTreeUtil.buildResult(trees);
        return tokenTree;
    }

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(SysUser user) {
        return userMapper.selectUserList(user);
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectAllocatedList(SysUser user) {
        return userMapper.selectAllocatedList(user);
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUnallocatedList(SysUser user) {
        return userMapper.selectUnallocatedList(user);
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByLoginName(String userName) {
        return userMapper.selectUserByLoginName(userName);
    }

    @Override
    public List<Aos> getAosPermsByToken(String token) {
        Long userId = redis.get(Constants.ACCESS_TOKEN + token, SysUser.class).getUserId();
        String resutl = remoteIBlockUserService.queryUserAos(String.valueOf(userId));
        if (null != resutl) {
            FabricResult fabricResult = JSON.parseObject(resutl, FabricResult.class);
            if (fabricResult.getCode() == FabricResult.RESULT_SUCC) {
                return fabricResult.getAosList();
            }
        } else {
            throw new RuoyiException(Constants.CHANAL_CONNECTED_FAILED, 500);
        }
        return null;
    }

    /**
     * 通过手机号码查询用户
     *
     * @param phoneNumber 手机号码
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByPhoneNumber(String phoneNumber) {
        return userMapper.selectUserByPhoneNumber(phoneNumber);
    }

    /**
     * 通过邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByEmail(String email) {
        return userMapper.selectUserByEmail(email);
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUserResult selectUserById(Long userId) {
        SysUser sysUser = userMapper.selectUserById(userId);
        SysUserResult sysUserResult = new SysUserResult();
        sysUserResult.setCreateTime(DateUtil.getDateFormat(sysUser.getCreateTime(), DateUtil.FULL_TIME_SPLIT_PATTERN));
        sysUserResult.setUserId(sysUser.getUserId());
        sysUserResult.setUserName(sysUser.getUserName());
        sysUserResult.setRemark(sysUser.getRemark());
        sysUserResult.setPhonenumber(sysUser.getPhonenumber());
//        SysRole sysRole = roleMapper.selectRoleById(sysUser.getRoleId());
//        sysUserResult.setRoleName(sysRole.getRoleName());
        sysUserResult.setStatus(sysUser.getStatus());
//        if ("0".equals(sysUser.getStatus())) {
//            sysUserResult.setStatus("启用");
//        } else {
//            sysUserResult.setStatus("禁用");
//        }
        List<String> roleNames = new ArrayList<>();
        List<Long> roleIds = new ArrayList<>();
        String userRoleResult = remoteIBlockUserService.queryUserRole(String.valueOf(userId));
        if (null != userRoleResult) {
            FabricResult fabricResult = JSON.parseObject(userRoleResult, FabricResult.class);
            if (fabricResult.getCode() == FabricResult.RESULT_SUCC && fabricResult.getRoleList() != null) {
                for (SysRoleResult s : fabricResult.getRoleList()) {
                    String result = remoteIBlockRoleService.queryRolePerms(String.valueOf(s.getRoleId()));
                    if (null != result) {
                        FabricResult roleResult = JSON.parseObject(result, FabricResult.class);
                        if (roleResult.getCode() == FabricResult.RESULT_SUCC && roleResult.getRoleName() != null) {
                            roleNames.add(roleResult.getRoleName());
                            roleIds.add(s.getRoleId());
                        }
                    } else {
                        throw new RuoyiException(Constants.CHANAL_CONNECTED_FAILED, 500);
                    }
                }
                sysUserResult.setRoleName(Joiner.on(",").join(roleNames));
                Long[] roleIdss = new Long[roleIds.size()];
                sysUserResult.setRoleIds(roleIds.toArray(roleIdss));
            }
        } else {
            throw new RuoyiException(Constants.CHANAL_CONNECTED_FAILED, 500);
        }

        return sysUserResult;
    }


    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteUserById(Long userId) {
        // 删除用户与角色关联
//        userRoleMapper.deleteUserRoleByUserId(userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * 批量删除用户信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteUserByIds(String ids) throws BusinessException {
        Long[] userIds = Convert.toLongArray(ids);
        for (Long userId : userIds) {
            if (SysUser.isAdmin(userId)) {
                throw new BusinessException("不允许删除超级管理员用户");
            }
        }
//        userRoleMapper.deleteUserRole(userIds);
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUser(SysUser user) {
        // 新增用户信息
        int rows = userMapper.insertUser(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return rows;
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUser(SysUser user) {
//        Long userId = user.getUserId();
        // 删除用户与角色关联
//        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户个人详细信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserInfo(SysUser user) {
        uploadUserRole(user);
        return userMapper.updateUser(user);
    }

    public int uploadUserRole(SysUser user) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", String.valueOf(user.getUserId()));
        params.put("roleIds", user.getRoleIds());
        String result = remoteIBlockUserService.uploadUserRole(params);
        return result != null && JSON.parseObject(result, FabricResult.class).getCode() == FabricResult.RESULT_SUCC ? 1 : 0;
    }

    /**
     * 修改用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetUserPwd(SysUser user) {
        return updateUserInfo(user);
    }

    /**
     * 修改用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int changeUserPwd(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser getUserById(Long userId) {
        return userMapper.selectUserById(userId);
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user) {
        List<Long> roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles)) {
            // 新增用户与角色管理
//            SysUserRole ur = new SysUserRole();
//            ur.setUserId(user.getUserId());
//            ur.setRoleId(roles);

//            userRoleMapper.insertUserRole(ur);
            uploadUserRole(user);
        }
    }


    /**
     * 校验用户名称是否唯一
     *
     * @param loginName 用户名
     * @return
     */
    @Override
    public String checkLoginNameUnique(String loginName) {
        int count = userMapper.checkLoginNameUnique(loginName);
        if (count > 0) {
            return UserConstants.USER_NAME_NOT_UNIQUE;
        }
        return UserConstants.USER_NAME_UNIQUE;
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkPhoneUnique(UserParams userParams) {
        SysUser info = userMapper.checkPhoneUnique(userParams.getPhonenumber());
        if (StringUtils.isNotNull(info)) {
            return UserConstants.USER_PHONE_NOT_UNIQUE;
        }
        return UserConstants.USER_PHONE_UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkEmailUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.USER_EMAIL_NOT_UNIQUE;
        }
        return UserConstants.USER_EMAIL_UNIQUE;
    }

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(userList) || userList.size() == 0) {
            throw new BusinessException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = "123456";
        for (SysUser user : userList) {
            try {
                // 验证是否存在这个用户
                SysUser u = userMapper.selectUserByLoginName(user.getLoginName());
                if (StringUtils.isNull(u)) {
                    user.setPassword(Md5Utils.hash(user.getLoginName() + password));
                    user.setCreateBy(operName);
                    this.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getLoginName() + " 导入成功");
                } else if (isUpdateSupport) {
                    user.setUpdateBy(operName);
                    this.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getLoginName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getLoginName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getLoginName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new BusinessException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    /**
     * 用户状态修改
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int changeStatus(SysUser user) {
        if (SysUser.isAdmin(user.getUserId())) {
            throw new BusinessException("不允许修改超级管理员用户");
        }
        return userMapper.updateUser(user);
    }

    /* (non-Javadoc)
     * @see com.ruoyi.system.service.ISysUserService#selectUserHasRole(java.lang.Long)
     */
    @Override
    public Set<Long> selectUserIdsHasRoles(Long[] roleIds) {
        return ArrayUtil.isNotEmpty(roleIds) ? userMapper.selectUserIdsHasRoles(roleIds) : null;
    }
}
