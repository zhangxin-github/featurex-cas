package org.wizard.featurexcas.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wizard.security.core.model.dto.SysMenuDto;
import org.wizard.security.core.model.entity.SysMenu;
import org.wizard.security.core.service.SysMenuService;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends BaseController {

  @Resource private SysMenuService sysMenuService;

  @GetMapping("/nav")
  public Map nav() {
    List<SysMenuDto> menuList = sysMenuService.findUserMenu(getUserId());
    Set<String> permissions = sysMenuService.findUserPermissions(getUserId());

    Map map = Maps.newHashMap();
    map.put("code", 0);
    map.put("menuList", menuList);
    map.put("permissions", permissions);
    return map;
  }

  @GetMapping("/list")
  @RequiresPermissions("sys:menu:list")
  public List<SysMenuDto> list()
      throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    List<SysMenuDto> menuDtoList = Lists.newArrayList();
    SysMenuDto sysMenuDto;
    List<SysMenu> menuList = sysMenuService.list();
    for (SysMenu sysMenu : menuList) {
      sysMenuDto = new SysMenuDto();
      PropertyUtils.copyProperties(sysMenuDto, sysMenu);
      SysMenu parentSysMenu = sysMenuService.getById(sysMenu.getParentId());
      if (parentSysMenu != null) {
        sysMenuDto.setParentName(parentSysMenu.getName());
      }
      menuDtoList.add(sysMenuDto);
    }

    return menuDtoList;
  }
}
