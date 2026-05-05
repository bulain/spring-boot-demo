## 1. 配置和常量定义

- [x] 1.1 创建 DingtalkLoginConstants 常量类，定义 API URL、Redis key 前缀、过期时间等
- [x] 1.2 创建 DingtalkOpenPlatformConfig 配置类，读取 appId、appSecret 配置

## 2. DTO 类创建

- [x] 2.1 创建 BindDingtalkDTO，包含 state、code 字段
- [x] 2.2 创建 UnbindDingtalkDTO，包含 password 字段
- [x] 2.3 创建 DingtalkQrCodeResponse，包含 qrCodeUrl、state、expireSeconds 字段

## 3. Service 层实现

- [x] 3.1 创建 DingtalkLoginService 接口，定义 getQrCode、dingtalkLogin、bindDingtalk、unbindDingtalk、getDingtalkStatus 方法
- [x] 3.2 创建 DingtalkLoginServiceImpl 实现类
- [x] 3.3 实现 getQrCode 方法：生成 state、构建二维码 URL、存入 Redis
- [x] 3.4 实现 dingtalkLogin 方法：验证 state、通过 code 换取 access_token 和 userId、查询/创建用户、生成 token
- [x] 3.5 实现 bindDingtalk 方法：验证 state、换取 userId、检查绑定冲突、执行绑定
- [x] 3.6 实现 unbindDingtalk 方法：验证密码、检查解绑后登录方式、执行解绑
- [x] 3.7 实现 getDingtalkStatus 方法：查询用户绑定状态

## 4. Controller 层完善

- [x] 4.1 完善 /dingtalk-login 端点实现
- [x] 4.2 新增 /dingtalk-qrcode 端点
- [x] 4.3 新增 /bind-dingtalk 端点
- [x] 4.4 新增 /unbind-dingtalk 端点
- [x] 4.5 新增 /dingtalk-status 端点

## 5. 单元测试

- [x] 5.1 创建 DingtalkLoginServiceTest，测试 Service 层各方法
- [x] 5.2 创建 SysAuthControllerDingtalkTest，测试 Controller 层端点

## 6. 配置文件更新

- [x] 6.1 在 application.yml 中添加钉钉开放平台配置示例
