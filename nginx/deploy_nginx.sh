#!/bin/bash
#Supported linux version, CenterOS, other OS had been tested yet!!!
yum -y erase nginx
yum -y install nginx
rm -rf /etc/nginx/nginx.conf
cp /mnt/wechat-oa-master/nginx/nginx.conf /etc/nginx/
cp /mnt/wechat-oa-master/nginx/proxy.conf /etc/nginx/conf.d/
cp /mnt/wechat-oa-master/nginx/1_gaoyongsheng.cn_bundle.crt /etc/ssl/
cp /mnt/wechat-oa-master/nginx/2_gaoyongsheng.cn.key /etc/ssl/
cd /usr/share/nginx/html && ln -s /mnt/wechat-oa-master/gaoyongsheng gaoyongsheng
service nginx restart


