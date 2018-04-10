var message = new Vue({
    el: "#attentionPage",
    data: {
        navActive: true,//我的关注,关注我的
        attentionArr: []
    },
    mounted: function () {
        this.init();
    },
    methods: {
        init:function () {

            $.ajax({
                url: '/pethome/user/like/' + GetQueryString("openid"),
                type: 'GET',
                dataType: 'json',
                data: {
                    type : 2
                },
                success: function (res) {
                    if (res.code) {
                        message.attentionArr = res.data;
                    }
                }
            });
        },

        navChange: function (type) {
            //this.navActive = !this.navActive;
            if(type==2){
                message.navActive = true;
            }else{
                message.navActive = false;
            }

            $.ajax({
                url: '/pethome/user/like/' + GetQueryString("openid"),
                type: 'GET',
                dataType: 'json',
                data: {
                    type : type
                },
                success: function (res) {
                    if (res.code) {
                        message.attentionArr = res.data;
                    }
                }
            });
        }
    }
});