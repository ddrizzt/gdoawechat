# Useful API References

## Token:
`13_jnWBFU9SGjF3R4JPtL4L04Y9RADHJNNMU-5oXNO3TrK-w8mN1BHkF2Rz1bMYlmSmz-MkAD2Jmoj1aAlKnrctWPb9SwHO4OjBkcDspO50__VnfY_bhL1LEbyT7mEoQzO0u8klOUBQ2DckhKsMNUZgADAAHM`

## Refresh Token:
```
curl -X GET 'https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxbe9a77c9f3338cb9&secret=cdc438d14caef913fe06f08a0e3bcf35'`
```

## Get CS List:
```
curl -X GET 'https://api.weixin.qq.com/cgi-bin/customservice/getkflist?access_token=13_jnWBFU9SGjF3R4JPtL4L04Y9RADHJNNMU-5oXNO3TrK-w8mN1BHkF2Rz1bMYlmSmz-MkAD2Jmoj1aAlKnrctWPb9SwHO4OjBkcDspO50__VnfY_bhL1LEbyT7mEoQzO0u8klOUBQ2DckhKsMNUZgADAAHM'`
```

## Send Message to a User:
```
curl -X POST \
'https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=13_jnWBFU9SGjF3R4JPtL4L04Y9RADHJNNMU-5oXNO3TrK-w8mN1BHkF2Rz1bMYlmSmz-MkAD2Jmoj1aAlKnrctWPb9SwHO4OjBkcDspO50__VnfY_bhL1LEbyT7mEoQzO0u8klOUBQ2DckhKsMNUZgADAAHM' \
-d '{
    "touser":"omQRb1eBuXdsBpn80zc0MEE1r8JU",
    "msgtype":"text",
    "text":
    {
         "content":"点这里 <a href=\"http://www.qq.com\">qq.com</a>"
    }
}'
```

## GET IP
```
curl -X GET 'https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=13_jnWBFU9SGjF3R4JPtL4L04Y9RADHJNNMU-5oXNO3TrK-w8mN1BHkF2Rz1bMYlmSmz-MkAD2Jmoj1aAlKnrctWPb9SwHO4OjBkcDspO50__VnfY_bhL1LEbyT7mEoQzO0u8klOUBQ2DckhKsMNUZgADAAHM'
```

## Get Union ID
```
curl -X GET \# Useful API References
'https://api.weixin.qq.com/cgi-bin/user/info?access_token=13_jnWBFU9SGjF3R4JPtL4L04Y9RADHJNNMU-5oXNO3TrK-w8mN1BHkF2Rz1bMYlmSmz-MkAD2Jmoj1aAlKnrctWPb9SwHO4OjBkcDspO50__VnfY_bhL1LEbyT7mEoQzO0u8klOUBQ2DckhKsMNUZgADAAHM&openid=omQRb1XXNx1oWeNS1Y24Xi5l8gq8'
```

## User Tag
```
curl -X GET 'https://api.weixin.qq.com/cgi-bin/tags/get?access_token=13_jnWBFU9SGjF3R4JPtL4L04Y9RADHJNNMU-5oXNO3TrK-w8mN1BHkF2Rz1bMYlmSmz-MkAD2Jmoj1aAlKnrctWPb9SwHO4OjBkcDspO50__VnfY_bhL1LEbyT7mEoQzO0u8klOUBQ2DckhKsMNUZgADAAHM'
```

## Menu Info:
#### Delete menu:
```
curl -X GET 'https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=13_jnWBFU9SGjF3R4JPtL4L04Y9RADHJNNMU-5oXNO3TrK-w8mN1BHkF2Rz1bMYlmSmz-MkAD2Jmoj1aAlKnrctWPb9SwHO4OjBkcDspO50__VnfY_bhL1LEbyT7mEoQzO0u8klOUBQ2DckhKsMNUZgADAAHM'
```

#### List menu:
```
curl -X GET 'https://api.weixin.qq.com/cgi-bin/menu/get?access_token=13_jnWBFU9SGjF3R4JPtL4L04Y9RADHJNNMU-5oXNO3TrK-w8mN1BHkF2Rz1bMYlmSmz-MkAD2Jmoj1aAlKnrctWPb9SwHO4OjBkcDspO50__VnfY_bhL1LEbyT7mEoQzO0u8klOUBQ2DckhKsMNUZgADAAHM'
```

#### Delete additional menu:
```
curl -X POST \
'https://api.weixin.qq.com/cgi-bin/menu/delconditional?access_token=13_jnWBFU9SGjF3R4JPtL4L04Y9RADHJNNMU-5oXNO3TrK-w8mN1BHkF2Rz1bMYlmSmz-MkAD2Jmoj1aAlKnrctWPb9SwHO4OjBkcDspO50__VnfY_bhL1LEbyT7mEoQzO0u8klOUBQ2DckhKsMNUZgADAAHM' \
-d '{"menuid": "431014545"}'
```

#### Create additional menu:
* Chinese menu
```
curl -X POST \
'https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token=<TOKEN>' \
-d '{
      "button": [
        {
          "name": "产品",
          "sub_button": [
            {
              "type": "view",
              "name": "域名",
              "url": "https://sg.godaddy.com/zh/domains",
              "sub_button": []
            },
            {
              "type": "view",
              "name": "SSL证书",
              "url": "https://sg.godaddy.com/zh/web-security",
              "sub_button": []
            },
            {
              "type": "view",
              "name": "热门促销",
              "url": "https://sg.godaddy.com/zh/promos/hot-deals",
              "sub_button": []
            },
            {
              "type": "view",
              "name": "更多产品",
              "url": "https://sg.godaddy.com/zh",
              "sub_button": []
            },
            {
              "type": "view",
              "name": "内部测试",
              "url": "https://www.gaoyongsheng.cn/app_download.html",
              "sub_button": []
            }
          ]
        },
        {
          "name": "客服",
          "sub_button": [
            {
              "type": "miniprogram",
              "name": "拨打电话",
              "url": "https://sg.godaddy.com/zh/contact-us.aspx",
              "sub_button": [],
              "appid": "wx03073c4b1d0c3cdd",
              "pagepath": "pages/support/support"
            },
            {
              "type": "view",
              "name": "常见问题",
              "url": "http://mp.weixin.qq.com/mp/homepage?__biz=MzU3OTUxMTk2MQ==&hid=8&sn=f4700a414eaf7a315eb5f07128b5a2e4&scene=18#wechat_redirect",
              "sub_button": []
            },
            {
              "type": "view",
              "name": "系统状态",
              "url": "https://sg.godaddy.com/zh/system-status",
              "sub_button": []
            },
            {
              "type": "view",
              "name": "域名搜索",
              "url": "https://sg.godaddy.com/zh/offers/domains/godaddy-domains",
              "sub_button": []
            },
            {
              "type": "view",
              "name": "域名评估",
              "url": "https://sg.godaddy.com/domain-value-appraisal",
              "sub_button": []
            }
          ]
        },
        {
          "name": "我的",
          "sub_button": [
            {
              "type": "miniprogram",
              "name": "我的产品",
              "url": "https://account.godaddy.com/products",
              "sub_button": [],
              "appid": "wx03073c4b1d0c3cdd",
              "pagepath": "pages/myaccount/myaccount?activeIndex=1"
            },
            {
              "type": "miniprogram",
              "name": "我的续费",
              "url": "https://account.godaddy.com/billing",
              "sub_button": [],
              "appid": "wx03073c4b1d0c3cdd",
              "pagepath": "pages/myaccount/myaccount?activeIndex=3"
            },
            {
              "type": "miniprogram",
              "name": "我的订单",
              "url": "https://account.godaddy.com/orders",
              "sub_button": [],
              "appid": "wx03073c4b1d0c3cdd",
              "pagepath": "pages/myaccount/myaccount?activeIndex=2"
            },
            {
              "type": "miniprogram",
              "name": "账户信息",
              "url": "https://account.godaddy.com/",
              "sub_button": [],
              "appid": "wx03073c4b1d0c3cdd",
              "pagepath": "pages/myaccount/myaccount?activeIndex=0"
            },
            {
              "type": "view",
              "name": "个性菜单中国",
              "url": "https://sg.godaddy.com/zh",
              "sub_button": []
            }
          ]
        }
      ],
      "matchrule": {
        "language": "zh_CN"
      }
}'
```
* English menu
```
curl -X POST \
'https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token=<TOKEN>' \
-d '{
      "button": [
        {
          "name": "Products",
          "sub_button": [
            {
              "type": "view",
              "name": "Domain",
              "url": "https://sg.godaddy.com/zh/domains",
              "sub_button": []
            },
            {
              "type": "view",
              "name": "SSL Certificate",
              "url": "https://sg.godaddy.com/zh/web-security",
              "sub_button": []
            },
            {
              "type": "view",
              "name": "Hot Deals",
              "url": "https://sg.godaddy.com/zh/promos/hot-deals",
              "sub_button": []
            },
            {
              "type": "view",
              "name": "More Products",
              "url": "https://sg.godaddy.com/zh",
              "sub_button": []
            },
            {
              "type": "view",
              "name": "Internal Testing",
              "url": "https://www.gaoyongsheng.cn/",
              "sub_button": []
            }
          ]
        },
        {
          "name": "Support",
          "sub_button": [
            {
              "type": "miniprogram",
              "name": "Call Us",
              "url": "https://sg.godaddy.com/zh/contact-us.aspx",
              "sub_button": [],
              "appid": "wx03073c4b1d0c3cdd",
              "pagepath": "pages/support/support"
            },
            {
              "type": "view",
              "name": "FAQ",
              "url": "http://mp.weixin.qq.com/mp/homepage?__biz=MzU3OTUxMTk2MQ==&hid=8&sn=f4700a414eaf7a315eb5f07128b5a2e4&scene=18#wechat_redirect",
              "sub_button": []
            },
            {
              "type": "view",
              "name": "System Status",
              "url": "https://sg.godaddy.com/zh/system-status",
              "sub_button": []
            },
            {
              "type": "view",
              "name": "Domain Search",
              "url": "https://sg.godaddy.com/zh/offers/domains/godaddy-domains",
              "sub_button": []
            },
            {
              "type": "view",
              "name": "Domain Appraisal",
              "url": "https://sg.godaddy.com/domain-value-appraisal",
              "sub_button": []
            }
          ]
        },
        {
          "name": "My",
          "sub_button": [
            {
              "type": "miniprogram",
              "name": "My Products",
              "url": "https://account.godaddy.com/products",
              "sub_button": [],
              "appid": "wx03073c4b1d0c3cdd",
              "pagepath": "pages/myaccount/myaccount?activeIndex=1"
            },
            {
              "type": "miniprogram",
              "name": "My Renewals",
              "url": "https://account.godaddy.com/billing",
              "sub_button": [],
              "appid": "wx03073c4b1d0c3cdd",
              "pagepath": "pages/myaccount/myaccount?activeIndex=3"
            },
            {
              "type": "miniprogram",
              "name": "My Orders",
              "url": "https://account.godaddy.com/orders",
              "sub_button": [],
              "appid": "wx03073c4b1d0c3cdd",
              "pagepath": "pages/myaccount/myaccount?activeIndex=2"
            },
            {
              "type": "miniprogram",
              "name": "My Account",
              
              "sub_button": [],
              "appid": "wx03073c4b1d0c3cdd",
              "pagepath": "pages/myaccount/myaccount?activeIndex=0"
            },
            {
              "type": "view",
              "name": "Menu for US",
              "url": "https://sg.godaddy.com",
              "sub_button": []
            }
          ]
        }
      ],
      "matchrule": {
        "language": "en"
      }
    }
}'
```

* Default menu body:
```


curl -X POST \
'https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token=<TOKEN>' \
-d '{
    "button": [
        {
            "name": "产品", 
            "sub_button": [
                {
                    "type": "view", 
                    "name": "域名", 
                    "url": "https://sg.godaddy.com/zh/domains"
                },
                {
                    "type": "view", 
                    "name": "SSL证书", 
                    "url": "https://sg.godaddy.com/zh/web-security"
                },
                {
                    "type": "view", 
                    "name": "热门促销", 
                    "url": "https://sg.godaddy.com/zh/promos/hot-deals"
                },
                {
                    "type": "view", 
                    "name": "更多产品", 
                    "url": "https://sg.godaddy.com/zh"
                },
                {
                    "type": "view", 
                    "name": "内部测试", 
                    "url": "https://www.gaoyongsheng.cn/"
                }                
            ]
        },
        {
            "name": "客服",
            "sub_button": [
                {
                    "type": "miniprogram",
                    "name": "拨打电话",
                    "appid": "wx03073c4b1d0c3cdd",
                    "pagepath": "pages/support/support"
                },
                {
                    "type": "view", 
                    "name": "常见问题", 
                    "url": "http://mp.weixin.qq.com/mp/homepage?__biz=MzU3OTUxMTk2MQ==&hid=8&sn=f4700a414eaf7a315eb5f07128b5a2e4&scene=18#wechat_redirect"
                },
                {
                    "type": "view", 
                    "name": "系统状态", 
                    "url": "https://sg.godaddy.com/zh/system-status"
                },
                {
                    "type": "view", 
                    "name": "域名搜索", 
                    "url": "https://sg.godaddy.com/zh/offers/domains/godaddy-domains"
                },
                {
                    "type": "view", 
                    "name": "域名评估", 
                    "url": "https://sg.godaddy.com/domain-value-appraisal"
                }                
            ]
        },
        {
            "name": "我的", 
            "sub_button": [
                {
                    "type": "miniprogram", 
                    "name": "我的产品", 
                    "url": "https://account.godaddy.com/products",
                    "appid": "wx03073c4b1d0c3cdd", 
                    "pagepath": "pages/myaccount/myaccount?activeIndex=1"
                },
                {
                    "type": "miniprogram", 
                    "name": "我的续费", 
                    "url": "https://account.godaddy.com/billing",
                    "appid": "wx03073c4b1d0c3cdd", 
                    "pagepath": "pages/myaccount/myaccount?activeIndex=3"
                },
                {
                    "type": "miniprogram", 
                    "name": "我的订单", 
                    "url": "https://account.godaddy.com/orders",
                    "appid": "wx03073c4b1d0c3cdd", 
                    "pagepath": "pages/myaccount/myaccount?activeIndex=2"
                },
                {
                    "type": "miniprogram", 
                    "name": "账户信息", 
                    "url": "https://account.godaddy.com/",
                    "appid": "wx03073c4b1d0c3cdd", 
                    "pagepath": "pages/myaccount/myaccount?activeIndex=0"
                }              
            ]
        }
    ]
}'
```
