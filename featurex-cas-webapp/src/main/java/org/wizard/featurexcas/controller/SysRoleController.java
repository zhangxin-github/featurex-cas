package org.wizard.featurexcas.controller;

import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wizard.common.model.CommonResult;
import org.wizard.security.core.common.Constants;
import org.wizard.security.core.model.dto.QuerySysRoleDto;
import org.wizard.security.core.model.entity.SysRole;
import org.wizard.security.core.service.SysRoleService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends BaseController {

    @Resource
    private SysRoleService sysRoleService;

    @GetMapping("/select")
    @RequiresPermissions("sys:role:select")
    public CommonResult select(){
        Map<String, Object> map = Maps.newHashMap();

        //如果不是超级管理员，则只查询自己所拥有的角色列表
        if(getUserId() != Constants.SUPER_ADMIN){
            map.put("create_user_id", getUserId());
        }
        List<SysRole> list = (List<SysRole>) sysRoleService.listByMap(map);
        return CommonResult.ok().put("list", list);
    }

    @GetMapping("/list")
    @RequiresPermissions("sys:role:list")
    public CommonResult list(QuerySysRoleDto dto){
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constants.SUPER_ADMIN){
            dto.setCreateUserId(getUserId());
        }

        Page<SysRole> page = sysRoleService.listRole(dto);
        return CommonResult.ok().put("page", page);
    }

//    /**
//     * 角色信息
//     */
//    @GetMapping("/info/{roleId}")
//    @RequiresPermissions("sys:role:info")
//    public CommonResult info(@PathVariable("roleId") Long roleId){
//        SysRole role = sysRoleService.getById(roleId);
//
//        //查询角色对应的菜单
//        List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
//        role.setMenuIdList(menuIdList);
//
//        return CommonResult.ok().put("role", role);
//    }
//
//    /**
//     * 保存角色
//     */
//    @PostMapping("/save")
//    @RequiresPermissions("sys:role:save")
//    public CommonResult save(@RequestBody SysRoleEntity role){
//        ValidatorUtils.validateEntity(role);
//
//        role.setCreateUserId(getUserId());
//        sysRoleService.saveRole(role);
//
//        return CommonResult.ok();
//    }
//
//    /**
//     * 修改角色
//     */
//    @PostMapping("/update")
//    @RequiresPermissions("sys:role:update")
//    public CommonResult update(@RequestBody SysRoleEntity role){
//        ValidatorUtils.validateEntity(role);
//
//        role.setCreateUserId(getUserId());
//        sysRoleService.update(role);
//
//        return CommonResult.ok();
//    }
//
//    /**
//     * 删除角色
//     */
//    @PostMapping("/delete")
//    @RequiresPermissions("sys:role:delete")
//    public CommonResult delete(@RequestBody Long[] roleIds){
//        sysRoleService.deleteBatch(roleIds);
//
//        return CommonResult.ok();
//    }
}
