window.onload = function(){
    var homeData = [
        {
            "url" :"pages/howToResetAnAccountPassword.html",
            "title": "1. 如何重置账户密码",
            "content": "需登录 GoDaddy 网站完成两步操作，之后我们将给您发送一封附链接的电子邮件以供您重置密码使用。"
        },
        {
            "url" :"pages/howToRetrieveAUserName.html",
            "title": "2. 如何找回用户名",
            "content": "需登录 GoDaddy 网站完成两步操作，之后我们通过电子邮件向您发送您的客户编号。"
        },
        {
            "url" :"pages/howDoITurnOnTwo-stepValidation.html",
            "title": "3. 如何开启两步验证",
            "content": "您可以通过开启两部验证来提高您账号的安全性。"
        },
        {
            "url" :"pages/howDoITurnOffTwo-stepTalidation.html",
            "title": "4. 如何关闭两步验证",
            "content": "关闭两部验证后，您的账号将不再要求两步验证。"
        },
        {
            "url" :"pages/howToViewProductAvailability.html",
            "title": "5. 如何查看产品有效期",
            "content": "您可以通过两个平台来查看所有产品的购买记录：GoDaddy 微信公众号和官网。"
        },
        {
            "url" :"pages/howToViewHistoricalOrders.html",
            "title": "6. 如何查看历史订单",
            "content": "您可以通过两个平台查看历史订单: GoDaddy 微信公众号和官网。"
        },
        {
            "url" :"pages/whetherToProvideVATInvoice.html",
            "title": "7. 是否提供增值税发票",
            "content": "很遗憾不能提供增值税发票，但可查看订单收据。"
        },
        {
            "url" :"pages/howToCancelAutomaticRenewal.html",
            "title": "8. 如何取消产品自动续费",
            "content": "取消产品自动续费，需要在 GoDaddy 网站进行四步操作。"
        },
        {
            "url" :"pages/howToChangeDomainNameContactInformation.html",
            "title": "9. 如何变更域名联系人信息",
            "content": "域名有四种联系人，您可以随时更改域名的联系人信息。"
        },
        {
            "url" :"pages/howToMakeDomainNameAccountChanges.html",
            "title": "10. 如何做域名账户变更",
            "content": "如果需要将域名从我们这里的一个账户移动到另一个，则需要在被称为账户更该的正确位置开始，如果需要将域名移动到其他注册商，请参阅将域名转移至其他注册商。"
        },
        {
            "url" :"pages/howToDoDomainNameResolution.html",
            "title": "11. 如何做域名解析",
            "content": "做域名解析需要更改域名服务器，而更改域名服务器取决于域名注册地和网站注册地。"
        },
        {
            "url" :"pages/theDomainNameInto.html",
            "title": "12. 如何做域名转入",
            "content": "域名转入需要在当前的注册商处完成一些步骤， 并在 GoDaddy 处完成一些步骤。"
        },
        {
            "url" :"pages/howDoDomainNameTransfer.html",
            "title": "13. 如何做域名转出",
            "content": "域名转移至其他注册商的过程比较花时间，整个过程可能需要最多10天来完成。"
        },

    ];
    var html = "";
    for(var i = 0; i < homeData.length; i++){
        html += "<a class='list_item js_post' href="+ homeData[i].url +">"+
                    "<div class='cover'>"+
                        "<img class='img js_img' src="+'./common/images/'+ i +" alt=''+>"+
                    "</div>"+
                    "<div class='cont'>"+
                        "<h2 class='title js_title'>"+ homeData[i].title +"</h2>"+
                        "<p class='desc'>"+ homeData[i].content +"</p>"+
                    "</div>"+
                "</a>"
    }
    this.document.getElementById("namespace_0").innerHTML = html;
};