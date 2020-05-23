###spring boot集成spring security实现Oauth2.0
0. 静态问题处理，应使用WebSecurity
0. 认证路径的拦截，应使用HttpSecurity
0. UserDetails信息存放jdbc数据库
0. ClientDetails信息存放jdbc数据库
0. Approvals信息存放jdbc数据库
0. Code信息存放dbc数据库
0. AccessToken和RefreshToken信息存放jdbc数据库
0. Session信息存放jdbc数据库，参见org/springframework/session/jdbc/schema-*.sql

认证后换取认证代码
http://localhost:8080/oauth/authorize?client_id=70d7c0424190ec81726664d1dd93cfe5&response_type=code&scope=read&redirect_uri=http://www.baidu.com
认证代码换取访问令牌
curl -X POST -H "Content-Type: application/x-www-form-urlencoded" -d "grant_type=authorization_code&code=bIAMD0&scope=read&redirect_uri=http://www.baidu.com" "http://70d7c0424190ec81726664d1dd93cfe5:91ee593b1c46b22874e11ac12278d2f4528d7594@localhost:8080/oauth/token"
访问受限资源
curl -X GET -H "Authorization: bearer 180c9c78-6367-40e0-be14-1e541f129e86" "http://localhost:8080/api/user"
