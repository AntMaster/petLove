var message = new Vue({
    el: "#msgPage",
    data: {
        navActive: true,//我的转发,转发我的
        attentionArr: []
    },
    mounted: function () {
        this.init();
    },
    methods: {
        init:function () {

            $.ajax({
                url: '/pethome/user/share/' + GetQueryString("openid"),
                type: 'GET',
                dataType: 'json',
                data: {
                    type : 8
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
            if(type==8){
                message.navActive = true;
            }else{
                message.navActive = false;
            }

            $.ajax({
                url: '/pethome/user/share/' + GetQueryString("openid"),
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