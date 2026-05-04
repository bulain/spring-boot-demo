# 删除 PagedService 公共类实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 移除 PagedService、PagedServiceImpl、PagedMapper 三个未使用的公共抽象类，所有 Service 和 Mapper 直接继承 MyBatis Plus 标准类。

**Architecture:** 自下而上逐步修改继承关系：先 Mapper，再 Service 接口，再 Service 实现类，最后删除核心类和更新测试代码，每步独立可验证。

**Tech Stack:** Java 17, MyBatis Plus 3.5.16, Spring Boot 3.5.x, JUnit 5

---

## Task 1: 更新 Sys* Mapper 继承关系

**Files:**
- Modify: `src/main/java/com/bulain/mybatis/sys/dao/SysUserMapper.java:9`
- Modify: `src/main/java/com/bulain/mybatis/sys/dao/SysRoleMapper.java`
- Modify: `src/main/java/com/bulain/mybatis/sys/dao/SysPermissionMapper.java`
- Modify: `src/main/java/com/bulain/mybatis/sys/dao/SysUserRoleMapper.java`
- Modify: `src/main/java/com/bulain/mybatis/sys/dao/SysRolePermissionMapper.java`
- Modify: `src/main/java/com/bulain/mybatis/demo/dao/BlogMapper.java`
- Test: `mvn test -Dtest=BlogMapperDemo`

- [ ] **Step 1: 修改 SysUserMapper**

```java
package com.bulain.mybatis.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bulain.mybatis.sys.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}
```

- [ ] **Step 2: 修改 SysRoleMapper**

```java
package com.bulain.mybatis.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bulain.mybatis.sys.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

}
```

- [ ] **Step 3: 修改 SysPermissionMapper**

```java
package com.bulain.mybatis.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bulain.mybatis.sys.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

}
```

- [ ] **Step 4: 修改 SysUserRoleMapper**

```java
package com.bulain.mybatis.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bulain.mybatis.sys.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

}
```

- [ ] **Step 5: 修改 SysRolePermissionMapper**

```java
package com.bulain.mybatis.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bulain.mybatis.sys.entity.SysRolePermission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {

}
```

- [ ] **Step 6: 修改 BlogMapper**

```java
package com.bulain.mybatis.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bulain.mybatis.demo.entity.Blog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

}
```

- [ ] **Step 7: 编译验证**

Run: `mvn compile -q`
Expected: BUILD SUCCESS

- [ ] **Step 8: 提交**

```bash
git add src/main/java/com/bulain/mybatis/sys/dao/*.java src/main/java/com/bulain/mybatis/demo/dao/BlogMapper.java
git commit -m "refactor: 更新 Sys* Mapper 和 BlogMapper 继承关系为 BaseMapper"
```

---

## Task 2: 更新 Sys* Service 接口继承关系

**Files:**
- Modify: `src/main/java/com/bulain/mybatis/sys/service/SysUserService.java:17`
- Modify: `src/main/java/com/bulain/mybatis/sys/service/SysRoleService.java:16`
- Modify: `src/main/java/com/bulain/mybatis/sys/service/SysPermissionService.java`
- Modify: `src/main/java/com/bulain/mybatis/sys/service/SysUserRoleService.java`
- Modify: `src/main/java/com/bulain/mybatis/sys/service/SysRolePermissionService.java`
- Modify: `src/main/java/com/bulain/mybatis/demo/service/BlogService.java:7`

- [ ] **Step 1: 修改 SysUserService**

```java
package com.bulain.mybatis.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.sys.dto.CreateUserDTO;
import com.bulain.mybatis.sys.dto.UpdateUserDTO;
import com.bulain.mybatis.sys.dto.UserQueryDTO;
import com.bulain.mybatis.sys.entity.SysRole;
import com.bulain.mybatis.sys.entity.SysUser;

import java.util.List;
import java.util.Set;

/**
 * 用户服务接口
 */
public interface SysUserService extends IService<SysUser> {
    // 方法保持不变...
}
```

- [ ] **Step 2: 修改 SysRoleService**

```java
package com.bulain.mybatis.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.sys.dto.CreateRoleDTO;
import com.bulain.mybatis.sys.dto.RoleQueryDTO;
import com.bulain.mybatis.sys.dto.UpdateRoleDTO;
import com.bulain.mybatis.sys.entity.SysPermission;
import com.bulain.mybatis.sys.entity.SysRole;

import java.util.List;

/**
 * 角色服务接口
 */
public interface SysRoleService extends IService<SysRole> {
    // 方法保持不变...
}
```

- [ ] **Step 3: 修改 SysPermissionService**

```java
package com.bulain.mybatis.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.sys.dto.PermissionQueryDTO;
import com.bulain.mybatis.sys.entity.SysPermission;

/**
 * 权限服务接口
 */
public interface SysPermissionService extends IService<SysPermission> {
    // 方法保持不变...
}
```

- [ ] **Step 4: 修改 SysUserRoleService**

```java
package com.bulain.mybatis.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bulain.mybatis.sys.entity.SysUserRole;

public interface SysUserRoleService extends IService<SysUserRole> {

}
```

- [ ] **Step 5: 修改 SysRolePermissionService**

```java
package com.bulain.mybatis.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bulain.mybatis.sys.entity.SysRolePermission;

public interface SysRolePermissionService extends IService<SysRolePermission> {

}
```

- [ ] **Step 6: 修改 BlogService**

```java
package com.bulain.mybatis.demo.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bulain.mybatis.demo.entity.Blog;

public interface BlogService extends IService<Blog> {

}
```

- [ ] **Step 7: 编译验证**

Run: `mvn compile -q`
Expected: BUILD SUCCESS

- [ ] **Step 8: 提交**

```bash
git add src/main/java/com/bulain/mybatis/sys/service/*.java src/main/java/com/bulain/mybatis/demo/service/BlogService.java
git commit -m "refactor: 更新 Service 接口继承关系为 IService"
```

---

## Task 3: 更新 Sys* ServiceImpl 继承关系

**Files:**
- Modify: `src/main/java/com/bulain/mybatis/sys/service/impl/SysUserServiceImpl.java:34`
- Modify: `src/main/java/com/bulain/mybatis/sys/service/impl/SysRoleServiceImpl.java:29`
- Modify: `src/main/java/com/bulain/mybatis/sys/service/impl/SysPermissionServiceImpl.java`
- Modify: `src/main/java/com/bulain/mybatis/sys/service/impl/SysUserRoleServiceImpl.java`
- Modify: `src/main/java/com/bulain/mybatis/sys/service/impl/SysRolePermissionServiceImpl.java`
- Modify: `src/main/java/com/bulain/mybatis/demo/service/impl/BlogServiceImpl.java:13`

- [ ] **Step 1: 修改 SysUserServiceImpl**

```java
package com.bulain.mybatis.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.sys.dao.SysPermissionMapper;
import com.bulain.mybatis.sys.dao.SysRoleMapper;
import com.bulain.mybatis.sys.dao.SysUserMapper;
import com.bulain.mybatis.sys.dao.SysUserRoleMapper;
import com.bulain.mybatis.sys.dto.CreateUserDTO;
import com.bulain.mybatis.sys.dto.UpdateUserDTO;
import com.bulain.mybatis.sys.dto.UserQueryDTO;
import com.bulain.mybatis.sys.entity.SysPermission;
import com.bulain.mybatis.sys.entity.SysRole;
import com.bulain.mybatis.sys.entity.SysUser;
import com.bulain.mybatis.sys.entity.SysUserRole;
import com.bulain.mybatis.sys.service.SysRoleService;
import com.bulain.mybatis.sys.service.SysUserRoleService;
import com.bulain.mybatis.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    // 方法保持不变...
}
```

- [ ] **Step 2: 修改 SysRoleServiceImpl**

```java
package com.bulain.mybatis.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.sys.dao.SysRoleMapper;
import com.bulain.mybatis.sys.dto.CreateRoleDTO;
import com.bulain.mybatis.sys.dto.RoleQueryDTO;
import com.bulain.mybatis.sys.dto.UpdateRoleDTO;
import org.springframework.util.StringUtils;
import com.bulain.mybatis.sys.entity.SysRole;
import com.bulain.mybatis.sys.entity.SysRolePermission;
import com.bulain.mybatis.sys.service.SysPermissionService;
import com.bulain.mybatis.sys.service.SysRolePermissionService;
import com.bulain.mybatis.sys.service.SysRoleService;
import com.bulain.mybatis.sys.entity.SysPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色服务实现类
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    // 方法保持不变...
}
```

- [ ] **Step 3: 修改 SysPermissionServiceImpl**

```java
package com.bulain.mybatis.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.sys.dao.SysPermissionMapper;
import com.bulain.mybatis.sys.dto.PermissionQueryDTO;
import com.bulain.mybatis.sys.entity.SysPermission;
import com.bulain.mybatis.sys.service.SysPermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 权限服务实现类
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {
    // 方法保持不变...
}
```

- [ ] **Step 4: 修改 SysUserRoleServiceImpl**

```java
package com.bulain.mybatis.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bulain.mybatis.sys.dao.SysUserRoleMapper;
import com.bulain.mybatis.sys.entity.SysUserRole;
import com.bulain.mybatis.sys.service.SysUserRoleService;
import org.springframework.stereotype.Service;

@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

}
```

- [ ] **Step 5: 修改 SysRolePermissionServiceImpl**

```java
package com.bulain.mybatis.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bulain.mybatis.sys.dao.SysRolePermissionMapper;
import com.bulain.mybatis.sys.entity.SysRolePermission;
import com.bulain.mybatis.sys.service.SysRolePermissionService;
import org.springframework.stereotype.Service;

@Service
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements SysRolePermissionService {

}
```

- [ ] **Step 6: 修改 BlogServiceImpl**

```java
package com.bulain.mybatis.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.bulain.mybatis.demo.dao.BlogMapper;
import com.bulain.mybatis.demo.entity.Blog;
import com.bulain.mybatis.demo.service.BlogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

}
```

- [ ] **Step 7: 编译验证**

Run: `mvn compile -q`
Expected: BUILD SUCCESS

- [ ] **Step 8: 提交**

```bash
git add src/main/java/com/bulain/mybatis/sys/service/impl/*.java src/main/java/com/bulain/mybatis/demo/service/impl/BlogServiceImpl.java
git commit -m "refactor: 更新 ServiceImpl 继承关系为 ServiceImpl"
```

---

## Task 4: 删除 PagedService 核心类文件

**Files:**
- Delete: `src/main/java/com/bulain/mybatis/core/service/PagedService.java`
- Delete: `src/main/java/com/bulain/mybatis/core/service/PagedServiceImpl.java`
- Delete: `src/main/java/com/bulain/mybatis/core/dao/PagedMapper.java`

- [ ] **Step 1: 删除三个核心类文件**

```bash
git rm src/main/java/com/bulain/mybatis/core/service/PagedService.java
git rm src/main/java/com/bulain/mybatis/core/service/PagedServiceImpl.java
git rm src/main/java/com/bulain/mybatis/core/dao/PagedMapper.java
```

- [ ] **Step 2: 编译验证**

Run: `mvn compile -q`
Expected: BUILD SUCCESS

- [ ] **Step 3: 提交**

```bash
git commit -m "refactor: 删除 PagedService、PagedServiceImpl、PagedMapper 公共类"
```

---

## Task 5: 更新 BlogServiceImplTest 测试代码

**Files:**
- Modify: `src/test/java/com/bulain/mybatis/demo/service/BlogServiceImplTest.java:80-110`

- [ ] **Step 1: 删除使用继承方法的测试用例**

删除 `testFind()` 和 `testPage()` 两个测试方法，因为它们测试的是已删除的继承方法。

- [ ] **Step 2: 运行测试验证**

Run: `mvn test -Dtest=BlogServiceImplTest -q`
Expected: BUILD SUCCESS (剩余测试通过)

- [ ] **Step 3: 提交**

```bash
git add src/test/java/com/bulain/mybatis/demo/service/BlogServiceImplTest.java
git commit -m "test: 删除 BlogService 中已移除的 find/page 方法测试"
```

---

## Task 6: 完整测试验证

- [ ] **Step 1: 运行所有单元测试**

Run: `mvn test -q -DexcludedGroups=integration`
Expected: BUILD SUCCESS

- [ ] **Step 2: 运行集成测试（可选，需要数据库）**

Run: `mvn test -Dtest=BlogMapperDemo`
Expected: 测试可用时应通过

- [ ] **Step 3: 最终提交（可选 - 如果有后续修改）**

```bash
git status
```
