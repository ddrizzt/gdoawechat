#!/bin/bash
#Supported linux version, CenterOS, other OS hadn't been tested yet!!!

### Code for launch config (User data).
: '

rm -rf /mnt/*
cd /mnt/ && curl -H "Authorization: token e280bf479d351af3a96b887ecc764ca19bf4a165" -L https://github.com/GD-China-WeChat/wechat-oa/archive/master.zip > wechat-oa-master.zip
cd /mnt/ && unzip wechat-oa-master.zip
cd /mnt/wechat-oa-master && chmod +x *.sh
/mnt/wechat-oa-master/deploy_all.sh [dev]

#Below deploy_all is for OpenStack on us.
/mnt/wechat-oa-master/deploy_all_us.sh [dev]

'


### End launch configure.
ENV=$1
if [ "$ENV" != "dev" ]; then
    ENV=""
fi

export LANG='en.UTF-8'
echo "---------------------------------------------------------------------------"
echo "----------------------> Starting deployment: " $ENV `date '+%Y/%m/%d %H:%M:%S'`
echo "---------------------------------------------------------------------------"
mkdir -p /mnt/apps/tools
mkdir -p /data/logs/

echo -e "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>> Deploy nginx...\n"
chmod +x /mnt/wechat-oa-master/nginx/deploy_nginx.sh && /mnt/wechat-oa-master/nginx/deploy_nginx.sh

echo -e "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>> Deploy oa proxy...\n"
/mnt/wechat-oa-master/deploy_oaproxy.sh $ENV

echo -e "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>> Deploy cleanup crontab...\n"
cd /mnt/wechat-oa-master/ && crontab truncatelog.cron

echo "---------------------------------------------------------------------------"
echo "----------------------> All DONE." `date '+%Y/%m/%d %H:%M:%S'`
echo "---------------------------------------------------------------------------"


