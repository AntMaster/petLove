var app = new Vue({
    el: "#ovPage",
    data: {
        formData: {
            userId: GetQueryString("openid"),
            organizationName: '',
            organizationImage: '',
            dutyer: '',
            dutyerPhone: '',
            messageCode: ''
        }
    },
    methods: {

        getMessageCode: function () {

            if (!this.formData.dutyerPhone){
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
                    mobile: app.formData.dutyerPhone
                },
                success: function (res) {
                    if (res.code != 1) {
                        $.toast('请求服务器后'+res.msg);
                    }else {
                        verify();
                    }
                }
            });
        },
        validate: function () {

            if(this.formData.dutyerPhone.length != 11){
                $.toast("手机号位数不正确");
                return;
            }

            $.ajax({
                url: '/pethome/user/auth',
                type: 'PUT',
                contentType: "application/x-www-form-urlencoded",
                dataType: 'json',
                data: app.formData,
                success: function (res) {
                    if (res.code === 1) {
                        app.dynamicArr = res.data;
                        window.location.href = "./ov-state.html?type=2&state=2&openid=" + GetQueryString("openid");
                    } else {
                        $.toast(res.msg);
                    }
                }
            });
        }
    }
});

$(function () {
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
        var timer = setInterval(function () {
            time--;
            if (time == 0) {
                clearInterval(timer);
                $(that).html(html).removeClass("disabled");
                $(that).html("重新获取");
                onOff = true;
            } else {
                $(that).html(time + "秒");
            }
        }, 1000);
    }
}


$("#upload").change(function (e) {

  /*  var data = new FormData();
    $.each(e.target.files, function (i, file) {
        data.append("file", file);
    });*/


    lrz(this.files[0])
        .then(function (rst) {
            // 处理成功会执行
            $.ajax({
                url: "/pethome/upload/auth",
                type: 'PUT',
                data: rst.formData,
                cache: false,
                contentType: false,
                processData: false,
                success: function (respond) {
                    app.formData.organizationImage = respond.data;
                }
            });
        })
        .catch(function (err) {
            // 处理失败会执行
        })
        .always(function () {
            // 不管是成功失败，都会执行
        });





});
