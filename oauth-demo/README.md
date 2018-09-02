http://localhost:8080/oauth/authorize?client_id=70d7c0424190ec81726664d1dd93cfe5&response_type=code&scope=read&redirect_uri=http://www.baidu.com
curl -X POST -H "Content-Type: application/x-www-form-urlencoded" -d "grant_type=authorization_code&code=FcTopo&scope=read&redirect_uri=http://www.baidu.com" "http://70d7c0424190ec81726664d1dd93cfe5:91ee593b1c46b22874e11ac12278d2f4528d7594@localhost:8778/oauth/token"
curl -X GET -H "Authorization: bearer 180c9c78-6367-40e0-be14-1e541f129e86" "http://localhost:8778/user"
