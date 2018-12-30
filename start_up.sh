#!/bin/bash
export LANG="en_US.UTF-8"

COUNT=`ps x | grep java | grep gd-oa-proxy`

if [ "$COUNT" = "" ]; then
    echo "Restart OA proxy service at: `date "+%Y-%m-%d %H:%M:%S"`"
    cd /mnt/wechat-oa-master/ && /mnt/apps/tools/jdk1.8.0_101/bin/java -Xmx300M -jar target/gd-oa-proxy-0.1.0.jar >> /data/logs/server_oa.log &
fi

