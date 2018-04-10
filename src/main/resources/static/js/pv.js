var app = new Vue({
    el: "#pvPage",
    data: {
        mobile: '',
        messageCode: ''
    },
    methods: {

        getMessageCode: function () {
            if (!this.mobile){
                $.toast("请填写手机号码~");
                return;
            }

            if(!onOff){
                $.toast("验证码已发送,请稍后再获取~");
            }

            $.ajax({
                url: '/pethome/user/sms/' + GetQueryString("openid"),
                type: 'GET',
                dataType: 'json',
                data: {
                    mobile: app.mobile
                },
                success: function (res) {
                    if (res.code != 1) {
                        $.toast(res.msg);
                    }else {
                        verify();
                    }
                }
            });
        },
        submit: function () {

            if(!(this.mobile && this.messageCode)){
                $.toast("请输入认证信息哟~");
                return;
            }

            if(this.mobile.length != 11){
                $.toast("手机号位数不正确");
                return;
            }

            $.ajax({
                url: '/pethome/user/sms/' + GetQueryString("openid"),
                type: 'POST',
                dataType: 'json',
                data: {
                    mobile: app.mobile,
                    code: app.messageCode
                },
                success: function (res) {

                    if (res.code != 1) {
                        $.toast(res.msg);
                    } else {
                        //验证成功后
                        $('.validate-remind').fadeIn();
                    }
                }
            });
        },
        goIndex: function () {
            window.location.href = "./index.html?openid=" + GetQueryString("openid")
        }
    }
});

$(function () {
    //验证码
    /*发送验证码间隔*/
    $(".verify-btn").click(function () {
        //verify();
    });
});


var onOff = true;
var getVerifyBtn = $(".verify-btn");

function verify() {
    if (onOff) {
        onOff = false;
        var that = getVerifyBtn;
        var html = $(this).html();
        var time = 60;
        $(that).html(time + "秒").addClass("verify-btn-disabled");
        $(that).disabled = true;
        var timer = setInterval(function() {
            time--;
            if (time == 0) {
                clearInterval(timer);
                $(that).html(html).removeClass("disabled");
                $(that).html("重新获取");
                onOff = true;
                $(that).disabled = false;
            } else {
                $(that).html(time + "秒");
            }
        }, 1000);
    }
}