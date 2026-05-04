## MODIFIED Requirements

### Requirement: Image Captcha Generation

系统 SHALL 支持生成图片验证码，扩展支持登录场景。

#### Scenario: 成功获取图片验证码（登录场景）
- **WHEN** 用户在登录失败达到阈值后请求图片验证码
- **THEN** 系统生成 4 位随机字符验证码
- **AND** 将验证码存储到 Redis（过期时间 5 分钟）
- **AND** 返回 Base64 编码的图片和 captchaId

#### Scenario: 登录场景下验证图片验证码
- **WHEN** 登录接口验证用户提供的图片验证码
- **THEN** 验证通过后才进行用户名密码验证
- **AND** 验证失败则返回验证码错误，不进行密码校验
