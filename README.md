# Godaddy WeChat OA Backend Service
This service will handle message and any click action from our 上海域宁 OA.

### POC test through OA message box
* OA message: `myinfo`, return the WeChat user info    

```
{
    "subscribe": 1, 
    "openid": "o6_bmjrPTlm6_2sgVt7hMZOPfL2M", 
    "nickname": "Band", 
    "sex": 1, 
    "language": "zh_CN", 
    "city": "广州", 
    "province": "广东", 
    "country": "中国", 
    "headimgurl":"http://thirdwx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
    "subscribe_time": 1382694957,
    "unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL"
    "remark": "",
    "groupid": 0,
    "tagid_list":[128,2],
    "subscribe_scene": "ADD_SCENE_QR_CODE",
    "qr_scene": 98765,
    "qr_scene_str": ""
}
```

* OA message: `getjwt [user] [password]`. Request Godaddy SSO API to get JWTToken

```
eyJhbGciOiAiUlMyNTYiLCAia2lkIjogIlFSSF9PVU5CNmcifQ.eyJhdXRoIjogImJhc2ljIiwgImZ0YyI6IDEsICJpYXQiOiAxNTM3NTE4NTMzLCAianRpIjogIkJuUDlqU1VycjVlZDZsUkEwRVVZcXciLCAidHlwIjogImlkcCIsICJmYWN0b3JzIjogeyJrX3B3IjogMTUzNzUxODUzM30sICJoYmkiOiAxNTM3NTE4NTMzLCAic2hvcHBlcklkIjogImlnaiIsICJjaWQiOiAiZDk1MDFjODItNWY4Ni00YmFiLTlmNDItZTQ0Yjg3MWRjNjMwIiwgInVzZXJuYW1lIjogImlnaiIsICJmaXJzdG5hbWUiOiAiRGF3biIsICJsYXN0bmFtZSI6ICJHYW8iLCAicGxpZCI6ICIxIiwgInBsdCI6IDF9.Wh2kj0bMKtYZJeBoMotKFDOlRTO5dERYqdSKqRUUwi4yK2p1__ZA11nv--pRCrzYT79JFkVXytSHHXwnfBF3R68KI5W3u58eA31mn6rfIsuhVFx7JerPrYVKBqE-1AJ-ORvDX7hkvsi_3CPNs-TTYELCZQ1M8rTlEffR6VuQ5KY
```

### Deployment:
./deploy_all.sh : This will initial everything from a raw CentOS. It include install nginx, compile & deploy OA proxy, deploy mini-program proxy


./deploy_mpproxy.sh : Intall nodejs and related modules, deploy mini-program proxy.

./deploy_oaproxy.sh : Intall jdk and recompile the OA proxy.

nginx/deploy_nginx.sh : Install nginx, config SSL, configure proxy on 443 / 8080(oa proxy) / 5757(mini-program proxy) 

### References
* OA API document: https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1445241432    
* Github: https://github.com/GD-China-WeChat/wechat-oa     
 * src/main/resources/oa_cmd.txt:   
 * This file store the useful CURL command to WeChat OA API.    
 * Only workable on www.gaoyonghseng.cn server.
