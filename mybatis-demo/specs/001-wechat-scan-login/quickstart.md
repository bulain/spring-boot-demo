# 微信扫码登录 - 快速开始

**创建日期**: 2026-05-05  
**功能**: 微信扫码登录

## 前置条件

### 1. 微信开放平台账号

1. 注册微信开放平台账号：https://open.weixin.qq.com/
2. 创建网站应用，获取 AppID 和 AppSecret
3. 配置授权回调域名（与项目部署域名一致）

### 2. 环境配置

在 `application.yml` 或环境变量中配置以下参数：

```yaml
wechat:
  open-platform:
    app-id: wx1234567890abcdef
    app-secret: 1234567890abcdef1234567890abcdef
    redirect-uri: https://your-domain.com/api/sys/auth/wechat-callback
```

或使用环境变量：

```bash
WECHAT_APP_ID=wx1234567890abcdef
WECHAT_APP_SECRET=1234567890abcdef1234567890abcdef
WECHAT_REDIRECT_URI=https://your-domain.com/api/sys/auth/wechat-callback
```

## 前端集成指南

### 方案一：扫码后前端跳转，调用登录接口

1. **获取二维码参数**

```javascript
const response = await fetch('/api/sys/auth/wechat-qrcode');
const { qrCodeUrl, state, expireSeconds } = (await response.json()).data;
```

2. **显示二维码**

使用 iframe 或 QRCode 库显示：

```html
<iframe 
  src="${qrCodeUrl}" 
  width="300" 
  height="400" 
  frameborder="0">
</iframe>
```

3. **轮询或等待回调**

用户扫码授权后，微信会跳转到回调地址，前端在回调页面接收参数并调用登录接口：

```javascript
// 回调页面逻辑
const urlParams = new URLSearchParams(window.location.search);
const code = urlParams.get('code');
const state = urlParams.get('state');

if (code && state) {
  // 调用登录接口
  const loginResponse = await fetch('/api/sys/auth/wechat-login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ code, state })
  });
  
  const { token, userInfo } = (await loginResponse.json()).data;
  
  // 保存 token，跳转到首页
  localStorage.setItem('token', token);
  window.location.href = '/dashboard';
}
```

### 方案二：使用微信 JS SDK 弹窗扫码

参考微信官方 JS SDK 文档实现。

## 后端集成检查清单

开发完成后，请确认以下功能已验证：

- [ ] 微信二维码参数生成接口正常返回
- [ ] state 参数验证机制生效（5分钟过期，使用后删除）
- [ ] 通过 code 能正确获取 openid 和用户信息
- [ ] 首次微信登录自动创建用户并关联 openid
- [ ] 已绑定微信的用户扫码直接登录
- [ ] 用户绑定微信功能正常（检查微信是否被其他账号占用）
- [ ] 用户解绑微信功能正常（验证密码，确保有其他登录方式）
- [ ] 各种异常场景有友好提示（code 过期、网络错误、用户取消授权等）
- [ ] 登录安全机制生效（限流、锁定等）
- [ ] 在至少 2 种数据库上验证通过

## 测试用例

### 正向测试

1. **首次微信登录**
   - 使用未绑定的微信扫码
   - 预期：自动创建用户，登录成功，返回 token

2. **已有用户微信登录**
   - 使用已绑定的微信扫码
   - 预期：直接登录到已有的用户账号

3. **绑定微信**
   - 登录后调用绑定接口
   - 预期：绑定成功，用户 wechat_openid 字段更新

4. **解绑微信**
   - 登录后调用解绑接口（需输入密码）
   - 预期：解绑成功，wechat_openid 字段清空

### 反向测试

1. **使用无效的 code**
   - 预期：返回"授权已过期"错误

2. **使用已失效的 state**
   - 预期：返回 CSRF 验证失败错误

3. **绑定已被其他账号占用的微信**
   - 预期：返回"该微信已被其他账号绑定"错误

4. **解绑后无其他登录方式**
   - 预期：返回"解绑后无法登录，请先设置密码或绑定手机号"错误

## 常见问题

### Q: 微信回调提示"redirect_uri 参数错误"

A: 检查以下几点：
1. 微信开放平台配置的授权域名与实际域名一致
2. 回调地址必须使用 https（测试环境可用 http）
3. 回调地址不能包含端口号（除非是 80 或 443）

### Q: 获取 access_token 时返回 40029 错误

A: code 无效或已使用。code 只能使用一次，且有 5 分钟有效期。

### Q: 如何在本地开发测试微信登录？

A: 使用内网穿透工具（如 ngrok）将本地端口映射到公网域名，在微信开放平台配置该域名。

## 部署说明

1. 确保生产环境配置正确的微信 AppID 和 AppSecret
2. 配置正确的回调域名（必须与微信开放平台一致）
3. 确保服务器能访问微信开放平台接口（网络连通性）
4. 建议配置微信接口调用的重试和熔断机制
