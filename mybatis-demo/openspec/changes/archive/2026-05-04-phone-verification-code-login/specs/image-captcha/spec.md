## ADDED Requirements

### Requirement: Image Captcha Generation

系统 SHALL 支持生成图片验证码。

#### Scenario: 成功获取图片验证码
- **WHEN** 用户请求获取图片验证码
- **THEN** 系统生成 4 位随机字符验证码
- **AND** 将验证码存储到 Redis（过期时间 5 分钟）
- **AND** 返回 Base64 编码的图片

### Requirement: Image Captcha Rate Limiting

系统 SHALL 对图片验证码请求进行次数限制。

#### Scenario: 未超过次数限制
- **WHEN** 同一 IP 在 1 分钟内请求图片验证码次数小于 10 次
- **THEN** 正常生成并返回验证码

#### Scenario: 超过次数限制
- **WHEN** 同一 IP 在 1 分钟内请求图片验证码次数大于等于 10 次
- **THEN** 返回错误提示："请求过于频繁，请稍后再试"
