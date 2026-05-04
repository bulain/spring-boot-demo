## 1. 基础服务层

- [x] 1.1 创建验证码服务接口 VerificationCodeService
- [x] 1.2 实现 VerificationCodeService - Redis 存储验证码
- [x] 1.3 实现图片验证码生成功能（4 位字符 + Base64 图片）
- [x] 1.4 实现次数限制功能（IP 限制、手机号限制）
- [x] 1.5 创建短信服务接口 SmsService 及 Mock 实现

## 2. 控制器层

- [x] 2.1 新增获取图片验证码接口 GET /api/sys/auth/image-captcha
- [x] 2.2 新增发送短信验证码接口 POST /api/sys/auth/send-code
- [x] 2.3 修改 phone-login 接口，添加短信验证码验证逻辑
- [x] 2.4 移除 TODO 注释并清理代码

## 3. DTO 和常量

- [x] 3.1 创建 ImageCaptchaResponse DTO
- [x] 3.2 创建 SendCodeRequest DTO
- [x] 3.3 添加验证码相关常量（过期时间、次数限制）

## 4. 测试验证

- [ ] 4.1 编写 VerificationCodeService 单元测试（可选，根据需要）
- [ ] 4.2 编写控制器集成测试（可选，根据需要）
- [x] 4.3 代码编译成功，核心功能实现完成
