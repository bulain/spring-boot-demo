# 删除 PagedService 公共类设计文档

## 背景

PagedService、PagedServiceImpl、PagedMapper 三个公共类提供了基于 Search 对象的通用分页查询方法，但经过审查发现：

1. 这些方法从未在业务代码（src/main）中被实际调用
2. OrderService 已经迁移到直接继承 MyBatis Plus 标准类
3. 所有 Sys*Service 都自行实现特定的分页方法，不使用继承来的通用方法
4. BlogService 的继承方法仅在测试代码中使用

## 设计决策

采用**方案一：完全删除**

删除三个公共抽象类，所有 Service 和 Mapper 直接继承 MyBatis Plus 的标准基类。

## 架构变更

### 删除的类

1. `com.bulain.mybatis.core.service.PagedService<T> extends IService<T>`
2. `com.bulain.mybatis.core.service.PagedServiceImpl<M extends PagedMapper<T>, T> extends ServiceImpl<M, T> implements PagedService<T>`
3. `com.bulain.mybatis.core.dao.PagedMapper<T> extends BaseMapper<T>`

### 继承关系变更

| 类型 | 当前继承 | 修改为 |
|------|---------|--------|
| Service 接口 | `extends PagedService<T>` | `extends IService<T>` |
| Service 实现 | `extends PagedServiceImpl<M, T>` | `extends ServiceImpl<M, T>` |
| Mapper | `extends PagedMapper<T>` | `extends BaseMapper<T>` |

## 受影响文件清单

### Service 接口
- `SysUserService`
- `SysRoleService`
- `SysPermissionService`
- `SysUserRoleService`
- `SysRolePermissionService`
- `BlogService`

### Service 实现
- `SysUserServiceImpl`
- `SysRoleServiceImpl`
- `SysPermissionServiceImpl`
- `SysUserRoleServiceImpl`
- `SysRolePermissionServiceImpl`
- `BlogServiceImpl`

### Mapper
- `SysUserMapper`
- `SysRoleMapper`
- `SysPermissionMapper`
- `SysUserRoleMapper`
- `SysRolePermissionMapper`
- `BlogMapper`

## 测试代码更新

`BlogServiceImplTest.java` 中使用了 `blogService.find(search)` 和 `blogService.page(search)`：
- 删除这两个测试用例，或
- 改为使用 MyBatis Plus 标准的 `list(wrapper)` 和 `page(page, wrapper)` 方法

## 设计原则

- **YAGNI** - 删除从未被业务代码使用的抽象
- **最小化继承** - 直接使用框架提供的标准基类
- **一致性** - 与 OrderService/OrderServiceImpl 的现有模式统一
- **无功能损失** - 所有实际使用的功能都通过自定义方法保留

## 实施步骤

1. 更新所有 Service 接口的继承关系
2. 更新所有 Service 实现类的继承关系
3. 更新所有 Mapper 的继承关系
4. 更新 BlogServiceImplTest 测试代码
5. 删除三个核心类文件
6. 运行测试验证
