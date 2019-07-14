package org.wizard.featurexcas.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wizard.common.model.CommonResult;
import org.wizard.security.core.common.Constants;
import org.wizard.security.core.model.dto.QuerySysUserDto;
import org.wizard.security.core.model.entity.SysUser;
import org.wizard.security.core.service.SysUserService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sys/user")
public class SysUserController extends BaseController {

    @Resource
    private SysUserService sysUserService;

    @GetMapping("/info")
    public CommonResult info(){
        return CommonResult.ok().put("user", getUser());
    }

    /**
     * 所有用户列表
     */
    @GetMapping("/list")
    public CommonResult list(QuerySysUserDto dto){
        //只有超级管理员，才能查看所有管理员列表
        if(getUserId() != Constants.SUPER_ADMIN){
            dto.setCreateUserId(getUserId());
        }
        Page<SysUser> page = sysUserService.listUser(dto);
        return CommonResult.ok().put("page", page);
    }
}
