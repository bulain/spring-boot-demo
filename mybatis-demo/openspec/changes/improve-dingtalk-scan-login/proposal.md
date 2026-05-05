## Why

项目中已有微信扫码登录的完整实现，但钉钉扫码登录仅存在占位的Controller端点，核心逻辑未实现。随着钉钉在企业场景的广泛使用，需要完善钉钉扫码登录功能以支持企业用户的便捷登录需求。

## What Changes

- 新增 DingTalk 服务接口和实现类，处理OAuth认证流程
- 新增钉钉登录二维码获取接口
- 新增钉钉绑定/解绑接口
- 新增钉钉绑定状态查询接口
- 完善钉钉登录的OAuth认证逻辑
- 添加钉钉开放平台配置类
- 添加钉钉相关DTO类
- 添加钉钉登录的单元测试
- 添加钉钉登录的Controller层测试

## Capabilities

### New Capabilities
- `dingtalk-scan-login`: 钉钉扫码登录功能，包括获取登录二维码、OAuth登录、绑定/解绑钉钉账号

### Modified Capabilities
- (无)

## Impact

- 影响代码：`SysAuthController` 完善 `/dingtalk-login` 端点，新增 `/dingtalk-qrcode`、`/bind-dingtalk`、`/unbind-dingtalk`、`/dingtalk-status` 端点
- 新增文件：`DingtalkLoginService` 接口及实现、配置类、DTO类、测试类
- 新增依赖：无需新增依赖，复用现有 HTTP 工具
- 配置变更：需在 application.yml 中添加钉钉开放平台的 appId、appSecret 配置
