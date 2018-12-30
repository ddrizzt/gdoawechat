#!/bin/bash
#Supported linux version, CenterOS, other OS hadn't been tested yet!!!
mkdir -p /mnt/apps/tools
mkdir -p /data/logs/

export JAVA_HOME=/mnt/apps/tools/jdk1.8.0_101/
if [ ! -d "/mnt/apps/tools/jdk1.8.0_101/" ]; then

	echo -e "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>> Initial jdk enviroment...\n"

	rm -rf /mnt/apps/tools/*

    cd /mnt/apps/tools/ && wget -q "https://gdwechat-bj-1252063756.cos.ap-beijing.myqcloud.com/wechatoa_package/apache-maven-3.3.9-bin.tar.gz"
    cd /mnt/apps/tools/ && wget -q "https://gdwechat-bj-1252063756.cos.ap-beijing.myqcloud.com/wechatoa_package/jdk-8u101-linux-x64.gz"
    cd /mnt/apps/tools/ && wget -q "https://gdwechat-bj-1252063756.cos.ap-beijing.myqcloud.com/wechatoa_package/jce_policy-8.zip"

	cd /mnt/apps/tools/
	tar zxf jdk-8u101-linux-x64.gz
	tar zxf apache-maven-3.3.9-bin.tar.gz
	rm -rf UnlimitedJCEPolicyJDK8
	unzip jce_policy-8.zip
	mv -f UnlimitedJCEPolicyJDK8/*.jar jdk1.8.0_101/jre/lib/security/
fi

echo -e "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>> Compile jar files\n"
if [ "$1" == "dev" ]; then
    echo "y" | mv /mnt/wechat-oa-master/src/main/resources/application-dev.properties /mnt/wechat-oa-master/src/main/resources/application.properties
    echo "y" | mv /mnt/wechat-oa-master/src/main/resources/templates/faq-dev.json /mnt/wechat-oa-master/src/main/resources/templates/faq.json
fi

cd /mnt/wechat-oa-master/ && /mnt/apps/tools/apache-maven-3.3.9/bin/mvn clean install

echo -e "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>> Restart oa proxy service...\n"
killall java
sleep 2s
chmod +x /mnt/wechat-oa-master/start_up.sh
rm -f /mnt/oa_token
export LANG="en.UTF-8" && /mnt/wechat-oa-master/start_up.sh


