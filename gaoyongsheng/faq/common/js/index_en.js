window.onload = function(){
    var homeData = [
        {
            "url" :"pages/howToResetAnAccountPassword_en.html",
            "title": "1. Reset my GoDaddy password",
            "content": "Go to our Reset Password page. Enter your Username or your Customer #. Complete the Security challenge, and then click Submit. You'll receive an email with a link to reset your password."
        },
        {
            "url" :"pages/howToRetrieveAUserName_en.html",
            "title": "2. Forgot your username to log in to GoDaddy?",
            "content": "Go to the Retrieve Username page. Follow the steps to find your login names."
        },
        {
            "url" :"pages/howDoITurnOnTwo-stepValidation_en.html",
            "title": "3. Enable two-step verification",
            "content": "You can increase your account's security by using two-step verification."
        },
        {
            "url" :"pages/howDoITurnOffTwo-stepTalidation_en.html",
            "title": "4. Turn off two-step verification",
            "content": "Starting immediately, your account will no longer require two-step verification."
        },
        {
            "url" :"pages/howToViewProductAvailability_en.html",
            "title": "5. Find the expiration dates of my products",
            "content": "You can find all your product records with expiration dates in the WeChat official account."
        },
        {
            "url" :"pages/howToViewHistoricalOrders_en.html",
            "title": "6. View my order history",
            "content": "You can find your order history in the WeChat official account."
        },
        {
            "url" :"pages/whetherToProvideVATInvoice_en.html",
            "title": "7. See my GoDaddy receipts",
            "content": "You can see your GoDaddy receipts here."
        },
        {
            "url" :"pages/howToCancelAutomaticRenewal_en.html",
            "title": "8. Turn off auto renew",
            "content": "Follow these 4 steps to turn off auto renew."
        },
        {
            "url" :"pages/howToChangeDomainNameContactInformation_en.html",
            "title": "9. Change domain contact information",
            "content": "You can change a domain's contact information at any time. Domains have four sets of contacts, which are listed below after the instructions."
        },
        {
            "url" :"pages/howToMakeDomainNameAccountChanges_en.html",
            "title": "10. Move a domain to another GoDaddy account",
            "content": "If you want to move a domain name from one account with us to another, you're in the right place to get started - it's called an Account Change. If you need to move a domain name to a different registrar, see Transfer domain to another registrar."
        },
        {
            "url" :"pages/howToDoDomainNameResolution_en.html",
            "title": "11. Change nameservers for my domains",
            "content": "To connect your domain name to your website you’ll need to change your nameservers. How you change your nameservers depends on where your domain is registered and where your website is registered. "
        },
        {
            "url" :"pages/theDomainNameInto_en.html",
            "title": "12. Transfer my domain to GoDaddy",
            "content": "You'll complete some steps with your current registrar, and some steps with GoDaddy."
        },
        {
            "url" :"pages/howDoDomainNameTransfer_en.html",
            "title": "13. Transfer domain to another registrar",
            "content": "You'll complete some steps with GoDaddy and some steps with the new registrar. The total process can take up to 10 days to complete."
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