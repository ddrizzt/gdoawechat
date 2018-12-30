function getURLParameter(name) {
    if (name = (new RegExp('[?&]' + encodeURIComponent(name) + '=([^&]*)')).exec(location.search)) {
        return decodeURIComponent(name[1]);
    }
}

var browser = {
    versions: function () {
        var u = navigator.userAgent,
            app = navigator.appVersion;
        return {
            trident: u.indexOf('Trident') > -1, //IE内核
            presto: u.indexOf('Presto') > -1, //opera内核
            webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
            gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
            mobile: !!u.match(/AppleWebKit.*Mobile.*/) || !!u.match(/AppleWebKit/), //是否为移动终端
            ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
            android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
            iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, //是否为iPhone或者QQHD浏览器
            iPad: u.indexOf('iPad') > -1, //是否iPad
            webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
        };
    }()
}

var u = navigator.userAgent;
if (browser.versions.android) { //判断android移动端
    var winHeight = $(window).height();

    function is_weixin() {
        var ua = navigator.userAgent.toLowerCase();
        return (ua.match(/MicroMessenger/i) == "micromessenger");
    }

    var isWeixin = is_weixin();
    if (isWeixin) {

        if (getURLParameter('lang') == 'en') {
            $(".weixin-tip img").attr("src", "./downloadbuild/images/android_e.png")
        } else {
            $(".weixin-tip img").attr("src", "./downloadbuild/images/android_c.png")
        }

        $(".weixin-tip").css("height", winHeight);
        $(".weixin-tip").show();

    } else {
        if (location.search.includes('oid=')) {
            location.href = 'https://www.gaoyongsheng.cn/oa/redir/account?nopayment=1&path=billing%3ffilter%3dexpires%26subFilter%3d90&oid=' + getURLParameter('oid');
        } else {
            location.href = 'https://www.gaoyongsheng.cn/oa/redir/account?nopayment=1&path=billing%3ffilter%3dexpires%26subFilter%3d90&code=' + getURLParameter('code');
        }

    }
}

if (browser.versions.ios || browser.versions.iPhone) { //ios判断移动端
    var winHeight = $(window).height();

    function is_weixin() {
        var ua = navigator.userAgent.toLowerCase();
        return (ua.match(/MicroMessenger/i) == "micromessenger");
    }

    var isWeixin = is_weixin();
    if (isWeixin) {

        if (getURLParameter('lang') == 'en') {
            $(".weixin-tip img").attr("src", "./downloadbuild/images/ios_e.png")
        } else {
            $(".weixin-tip img").attr("src", "./downloadbuild/images/ios_c.png")
        }


        $(".weixin-tip").css("height", winHeight);
        $(".weixin-tip").show();


    } else {
        if (location.search.includes('oid=')) {
            location.href = 'https://www.gaoyongsheng.cn/oa/redir/account?nopayment=1&path=billing%3ffilter%3dexpires%26subFilter%3d90&oid=' + getURLParameter('oid');
        } else {
            location.href = 'https://www.gaoyongsheng.cn/oa/redir/account?nopayment=1&path=billing%3ffilter%3dexpires%26subFilter%3d90&code=' + getURLParameter('code');
        }
    }
}

// 正则
function isContains(str, substr) {
    return new RegExp(substr).test(str);
}


