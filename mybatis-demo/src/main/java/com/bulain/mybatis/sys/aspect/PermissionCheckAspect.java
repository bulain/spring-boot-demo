package com.bulain.mybatis.sys.aspect;

import com.bulain.mybatis.sys.annotation.RequiresPermission;
import com.bulain.mybatis.sys.annotation.RequiresRole;
import com.bulain.mybatis.sys.entity.SysRole;
import com.bulain.mybatis.sys.service.SysJwtService;
import com.bulain.mybatis.sys.service.SysUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Set;

/**
 * 权限校验切面
 */
@Aspect
@Component
public class
 PermissionCheckAspect {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysJwtService sysJwtService;

    @Around("@annotation(com.bulain.mybatis.sys.annotation.RequiresPermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RequiresPermission annotation = signature.getMethod().getAnnotation(RequiresPermission.class);

        String requiredPermission = annotation.value();
        if (requiredPermission == null || requiredPermission.isEmpty()) {
            return joinPoint.proceed();
        }

        // TODO: 从当前登录用户获取用户ID
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("用户未登录");
        }

        Set<String> userPermissions = sysUserService.getUserPermissionCodes(currentUserId);
        if (!userPermissions.contains(requiredPermission)) {
            throw new RuntimeException("权限不足，需要权限: " + requiredPermission);
        }

        return joinPoint.proceed();
    }

    @Around("@annotation(com.bulain.mybatis.sys.annotation.RequiresRole)")
    public Object checkRole(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RequiresRole annotation = signature.getMethod().getAnnotation(RequiresRole.class);

        String requiredRole = annotation.value();
        if (requiredRole == null || requiredRole.isEmpty()) {
            return joinPoint.proceed();
        }

        // TODO: 从当前登录用户获取用户ID
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("用户未登录");
        }

        List<SysRole> userRoles = sysUserService.getUserRoles(currentUserId);
        boolean hasRole = userRoles.stream()
                .anyMatch(role -> role.getCode().equals(requiredRole));
        if (!hasRole) {
            throw new RuntimeException("角色权限不足，需要角色: " + requiredRole);
        }

        return joinPoint.proceed();
    }

    private Long getCurrentUserId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (sysJwtService.validateToken(token)) {
                    return sysJwtService.getUserIdFromToken(token);
                }
            }
        }
        return null;
    }

}
