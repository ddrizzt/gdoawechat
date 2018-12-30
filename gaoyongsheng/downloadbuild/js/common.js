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
        $(".weixin-tip img").attr("src", "./downloadbuild/images/android.png")
        $(".weixin-tip").css("height", winHeight);
        $(".weixin-tip").show();
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
        $(".weixin-tip img").attr("src", "./downloadbuild/images/ios@2x.png")

        $(".weixin-tip").css("height", winHeight);
        $(".weixin-tip").show();
    }
}

$(".load").on("click", function () {
    if (browser.versions.ios || browser.versions.iPhone) { //判断ios iPhone
        // location.href = "https://account.godaddy.com/billing?filter=expires&subFilter=90";
        location.href = "https://sg.godaddy.com/zh";
    } else if (browser.versions.android) { //判断android
        location.href = "https://sg.godaddy.com/zh";
    } else {
        alert("404 NO Foud")
    }

});

// 正则
function isContains(str, substr) {
    return new RegExp(substr).test(str);
}


