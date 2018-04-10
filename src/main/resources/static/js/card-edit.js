var app = new Vue({
    el: "#card-edit",
    data: {
        show: true,
        isMiao: false,
        varietyName: '',
        cardFormModel: {
            id: '',
            nickName: '',
            headImgUrl: '',
            classifyId: 3,
            //默认宠物品种id
            varietyId: 9,
            birthday: '',
            sex: true,
            contraception: 0,
            description: '',
            chipNo: ''
        },
        varietyArr: []
    },
    mounted: function () {
        this.init();
    },
    methods: {
        init: function () {

            this.varietyArrDataSource = varietyArrDataSource;
            var petId = GetQueryString('petid');
            var openId = GetQueryString('openid');
            $.ajax({
                url: '/pethome/pet/one/' + openId,
                type: 'GET',
                dataType: 'json',
                data: {
                    petId: petId
                },
                success: function (res) {

                    if (res.code === 1) {
                        app.cardFormModel = res.data;
                        if (res.data.classifyId == 2) {
                            app.varietyArr = varietyArrDataSource['2'];
                        } else {
                            app.varietyArr = varietyArrDataSource['3'];
                        }
                        for (var i = 0; i < app.varietyArr.length; i++) {
                            if (app.varietyArr[i].id == app.cardFormModel.varietyId) {
                                app.varietyName = app.varietyArr[i].name;
                            }
                        }
                    }
                }
            });
            //初始化宠物品类
        },
        selectClass: function (classId) {
            if (classId == 2) {
                //切换类别为猫
                this.cardFormModel.classifyId = 2;
                this.varietyArr = this.varietyArrDataSource['2'];
                this.varietyName = this.varietyArr[0].name;
                this.cardFormModel.varietyId = 3;
            } else {
                this.cardFormModel.classifyId = 3;
                this.varietyArr = this.varietyArrDataSource['3'];
                this.varietyName = this.varietyArr[0].name;
                this.cardFormModel.varietyId = 9;
            }
        },
        selectSex: function (sex) {
            sex == 0 ? this.cardFormModel.sex = 0 : this.cardFormModel.sex = 1;
        },
        selectContraceptionState: function (state) {
            this.cardFormModel.contraception = state;
        },
        //选择品种
        selectVarietyArr: function (id, name) {

            //id用于上传，name用于绑定model显示中文
            app.cardFormModel.varietyId = id;
            app.varietyName = name;
        },
        submit: function () {

            if(!app.cardFormModel.nickName){
                $.toast("爱宠昵称不能为空");
                return ;
            }

            app.cardFormModel.birthday = app.cardFormModel.birthday + " 00:00:00";
            $.ajax({
                url: '/pethome/pet/' + GetQueryString("openid"),
                type: 'POST',
                contentType: "application/json",
                dataType: 'json',
                data: JSON.stringify(app.cardFormModel),
                success: function (res) {
                    if (res.code === 1) {
                        window.location.href = "./card-list.html?openid=" + GetQueryString("openid")
                    } else {
                        $.toast(res.msg);
                    }
                }
            });
        }
    }
});
$(function () {
    //set ui
    var cardWidth = $(".card-create").width();
    $(".card-create").css("height", cardWidth * 1.3);

    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth() +1 ;
    var day = date.getDate();
    var today = year + "-" + month + "-" + day;
    //初始化插件
    $("#birth-selector").calendar({
        maxDate:today,
        monthNames: ['一月', '二月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
        dayNamesShort: ['天', '一', '二', '三', '四', '五', '六'],
        onChange: function (p, values, displayValues) {
            app.cardFormModel.birthday = displayValues[0];
        }
    });
    //判断设备宽度
    var ScreenWidth = document.body.clientWidth;
    var ScreenHeight = document.body.clientHeight;
    if (ScreenWidth < 340 || ScreenHeight < 590) {
        //当前为iphone5宽度
        $(".item-title").css("font-size", ".65rem");
        $(".sex-input span").css("margin-right", ".6rem");
        $(".card-form-item").css("margin-bottom", ".5rem");
        $(".avatar img").css({"width": "3rem", "height": "3rem", "border-radius": "1.5rem"});
    }
    //card rotate
    // var cardF = document.querySelector(".card-front");
    // var cardB = document.querySelector(".card-back");
    // cardF.onclick = function() {
    //     cardF.style.transform = "rotateY(180deg)";
    //     cardB.style.transform = "rotateY(0deg)";
    // }
});

$("#datetime-picker").datetimePicker({
    toolbarTemplate: '<header class="bar bar-nav">\
    <button class="button button-link pull-right close-picker">确定</button>\
    <h1 class="title">选择日期和时间</h1>\
    </header>',
    onClose: function () {
        app.cardFormModel.birthday = $("#datetime-picker").val()
    }
});

$(".avatar-upload").change(function (e) {

    var data = new FormData();
    $.each(e.target.files, function (i, file) {
        data.append("file", file);
    });

    $.ajax({
        url: "/pethome/upload/pet",
        type: 'PUT',
        data: data,
        cache: false,
        contentType: false,
        processData: false,
        success: function (respond) {
            if (respond.code)
                app.cardFormModel.headImgUrl = respond.data;
        }
    });
});

function getBirth() {
    app.cardFormModel.birthday = $("#datetime-picker").val();
}
