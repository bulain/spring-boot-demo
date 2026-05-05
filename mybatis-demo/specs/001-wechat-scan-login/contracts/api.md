# 微信扫码登录 - API 接口契约

**创建日期**: 2026-05-05  
**功能**: 微信扫码登录

## 接口概览

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/sys/auth/wechat-qrcode | GET | 获取微信登录二维码参数 |
| /api/sys/auth/wechat-callback | GET | 微信授权回调接口 |
| /api/sys/auth/wechat-login | POST | 微信登录（已有，完善实现） |
| /api/sys/user/bind-wechat | POST | 绑定微信账号 |
| /api/sys/user/unbind-wechat | POST | 解绑微信账号 |
| /api/sys/user/wechat-status | GET | 查询微信绑定状态 |

---

## 1. 获取微信登录二维码参数

**接口**: `GET /api/sys/auth/wechat-qrcode`

**说明**: 生成微信 OAuth2 授权链接所需的参数，前端根据此参数构建二维码显示

### 请求参数

无

### 响应示例

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "qrCodeUrl": "https://open.weixin.qq.com/connect/qrconnect?appid=xxx&redirect_uri=xxx&response_type=code&scope=snsapi_login&state=xxx#wechat_redirect",
    "state": "a1b2c3d4e5f6g7h8i9j0",
    "expireSeconds": 300
  }
}
```

### 响应字段说明

| 字段 | 类型 | 说明 |
|------|------|------|
| qrCodeUrl | string | 微信二维码授权完整 URL，前端可直接显示 |
| state | string | 防 CSRF 随机字符串，用于回调验证 |
| expireSeconds | int | 二维码有效时间（秒） |

---

## 2. 微信授权回调接口

**接口**: `GET /api/sys/auth/wechat-callback`

**说明**: 微信授权成功后的回调地址，接收 code 和 state 参数

### 请求参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| code | string | 是 | 微信授权码，用于换取 access_token |
| state | string | 是 | 回调状态参数，用于防 CSRF 验证 |

### 响应

回调成功后前端页面自动跳转至主页，失败则跳转至登录页并携带错误参数

---

## 3. 微信登录（完善已有接口）

**接口**: `POST /api/sys/auth/wechat-login`

**说明**: 使用微信授权码完成登录（前后端分离场景下前端获取 code 后调用此接口）

### 请求体

```json
{
  "code": "01123456789abcdef",
  "state": "a1b2c3d4e5f6g7h8i9j0"
}
```

### 字段说明

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| code | string | 是 | 微信授权码 |
| state | string | 是 | 防 CSRF 状态参数 |

### 响应示例

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userInfo": {
      "id": "123456",
      "username": "wechat_user_abc",
      "name": "张三",
      "email": null,
      "phone": null,
      "wechatOpenid": "o1234567890abcdef",
      "status": 1
    },
    "permissionCodes": ["user:view", "user:edit"]
  }
}
```

### 错误码

| HTTP 状态码 | 说明 |
|-------------|------|
| 400 | 授权码无效或已过期 |
| 401 | state 验证失败 |
| 429 | 请求过于频繁 |
| 500 | 微信接口调用失败 |

---

## 4. 绑定微信账号

**接口**: `POST /api/sys/user/bind-wechat`

**说明**: 已登录用户绑定微信账号

### 请求头

| 名称 | 说明 |
|------|------|
| Authorization | Bearer {token} |

### 请求体

```json
{
  "code": "01123456789abcdef",
  "state": "a1b2c3d4e5f6g7h8i9j0"
}
```

### 响应示例

```json
{
  "code": 200,
  "message": "绑定成功",
  "data": null
}
```

### 错误场景

- 该微信已绑定其他账号
- 当前账号已绑定微信（需先解绑）
- 授权码无效

---

## 5. 解绑微信账号

**接口**: `POST /api/sys/user/unbind-wechat`

**说明**: 已登录用户解绑微信账号

### 请求头

| 名称 | 说明 |
|------|------|
| Authorization | Bearer {token} |

### 请求体

```json
{
  "password": "current_password"
}
```

### 字段说明

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| password | string | 是 | 当前账号密码，用于身份确认 |

### 响应示例

```json
{
  "code": 200,
  "message": "解绑成功",
  "data": null
}
```

### 错误场景

- 账号未绑定微信
- 密码验证失败
- 解绑后无可用登录方式（无密码且无手机号）

---

## 6. 查询微信绑定状态

**接口**: `GET /api/sys/user/wechat-status`

**说明**: 查询当前用户的微信绑定状态

### 请求头

| 名称 | 说明 |
|------|------|
| Authorization | Bearer {token} |

### 响应示例

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "binded": true,
    "wechatNickname": "张三"
  }
}
```

## 公共错误码

| code | 说明 |
|------|------|
| 200 | 成功 |
| 400 | 参数错误 |
| 401 | 未授权或 token 过期 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |
