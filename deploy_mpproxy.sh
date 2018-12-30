#!/bin/bash
#Supported linux version, CenterOS, other OS hadn't been tested yet!!!
mkdir -p /mnt/apps/tools
mkdir -p /data/logs/

#Install nodjs
yum erase -y nodejs
yum install -y gcc-c++ make
curl -sL https://rpm.nodesource.com/setup_8.x | sudo -E bash -
yum install -y nodejs

#Download code package from GitHub.
cd /mnt && curl -H "Authorization: token e280bf479d351af3a96b887ecc764ca19bf4a165" -L https://github.com/GD-China-WeChat/wechat-mini/archive/master.zip > wechat-mini-master.zip
cd /mnt/ && unzip wechat-mini-master.zip
#Install app related modules
cd /mnt/wechat-mini-master/server && npm install -g pm2 && npm install -S phantom@6.0.3 && npm install
cd /mnt/wechat-mini-master/server/node_modules/wafer-node-sdk && sed -i '/.*rootPathname, useQcloudLogin.*ERRORS.ERR_INIT_SDK_LOST_CONFIG.*/d' index.js
killall node
sleep 2s

#Generate static page
mkdir -p /mnt/wechat-oa-master/gaoyongsheng/receiptImage
node /mnt/wechat-mini-master/server/controllers/getAgreementsDaily.js
node /mnt/wechat-mini-master/server/controllers/getPhoneList.js

#Install fonts
rm /usr/share/fonts/SourceHanSans-Normal.*
cd /usr/share/fonts/ && wget https://github.com/be5invis/source-han-sans-ttf/releases/download/v1.04.20170811/SourceHanSans-Normal.ttf
fc-cache -fv

#Restart nodejs app
chmod +x /mnt/wechat-mini-master/server/startup.sh
/mnt/wechat-mini-master/server/startup.sh
