# 导入模板下载 - API接口契约

**创建日期**: 2026-05-05
**功能**: 导入模板下载

## 接口概览

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/sys/users/template | GET | 下载用户导入模板 |
| /api/sys/roles/template | GET | 下载角色导入模板 |

---

## 1. 下载用户导入模板

**接口**: `GET /api/sys/users/template`

**说明**: 下载用户导入Excel模板，包含字段说明和示例数据

### 请求头

| 名称 | 说明 |
|------|------|
| Authorization | Bearer {token}，需管理员权限 |

### 响应

**成功响应**:
- HTTP状态码：200 OK
- Content-Type: `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet`
- Content-Disposition: `attachment; filename*=UTF-8''%E7%94%A8%E6%88%B7%E5%AF%BC%E5%85%A5%E6%A8%A1%E6%9D%BF.xlsx`
- Body: Excel文件二进制流

**文件结构**:
- Sheet 1: "填写说明" - 字段说明、格式要求、示例
- Sheet 2: "用户数据" - 表头 + 一行示例数据

**错误响应**:
```json
{
  "code": 401,
  "message": "未登录或Token已过期",
  "data": null
}
```

```json
{
  "code": 403,
  "message": "无权限访问",
  "data": null
}
```

---

## 2. 下载角色导入模板

**接口**: `GET /api/sys/roles/template`

**说明**: 下载角色导入Excel模板，包含字段说明和示例数据

### 请求头

| 名称 | 说明 |
|------|------|
| Authorization | Bearer {token}，需管理员权限 |

### 响应

**成功响应**:
- HTTP状态码：200 OK
- Content-Type: `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet`
- Content-Disposition: `attachment; filename*=UTF-8''%E8%A7%92%E8%89%B2%E5%AF%BC%E5%85%A5%E6%A8%A1%E6%9D%BF.xlsx`
- Body: Excel文件二进制流

**文件结构**:
- Sheet 1: "填写说明" - 字段说明、格式要求、示例
- Sheet 2: "角色数据" - 表头 + 一行示例数据

**错误响应**:
```json
{
  "code": 401,
  "message": "未登录或Token已过期",
  "data": null
}
```

```json
{
  "code": 403,
  "message": "无权限访问",
  "data": null
}
```

---

## 公共错误码

| code | 说明 |
|------|------|
| 200 | 成功 |
| 401 | 未授权 |
| 403 | 无权限 |
| 500 | 服务器内部错误 |
