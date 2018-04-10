//地图组件初始化
Vue.use(VueBaiduMap.default, {
    ak: 'LOnzr56cOpw0LoZ5dt8GSGdej9YRjGrn',
    methods: {
        locationSuccess: function () {
            alert("locate success");
        },
        locationError: function () {
            alert("locate error");
        }
    }
});
//搜索页面
var app = new Vue({
    el: "#searchPage",
    data: {
        testPoint: "{lng: 30.4660040000, lat: 114.4239750000}",
        //1：寻宠，2：寻主
        publishType: -1,
        //2：猫  3：狗
        classifyID: -1,
        varietyID: -1,
        petSex: -1,
        catVarietyArr: [
            {name: "橘猫", id: "39"},
            {name: "波斯猫", id: "19"},
            {name: "英短", id: "13"},
            {name: "美短", id: "14"},
            {name: "加菲猫", id: "15"},
            {name: "蓝猫", id: "16"},
            {name: "田园猫", id: "3"},
            {name: "其他", id: -1}
        ],
        dogVarietyArr: [
            {name: "田园犬", id: "9"},
            {name: "金毛", id: "5"},
            {name: "哈士奇", id: "6"},
            {name: "萨摩耶", id: "20"},
            {name: "泰迪", id: "10"},
            {name: "拉布拉多", id: "7"},
            {name: "雪纳瑞", id: "25"},
            {name: "阿拉斯加", id: "18"},
            {name: "比熊", id: "22"},
            {name: "边牧", id: "29"},
            {name: "博美", id: "21"},
            {name: "其他", id: ""}
        ],
        showVarietyArr: [],
        //选中的宠物的品种序号，用于控制active状态
        svIndex: -1,
        //搜索关键字
        keyword: '',
        //初始化坐标点
        annotationList: [],
        //大头针配置
        annotationConf: {
            icon_fpet: {url: 'img/icon/pin_fpet.png', size: {width: 42, height: 46}},
            icon_fpet_big: {url: 'img/icon/pin_fpet_big.png', size: {width: 42, height: 46}},
            icon_fowner: {url: 'img/icon/pin_fowner.png', size: {width: 42, height: 46}},
            icon_fowner_big: {url: 'img/icon/pin_fowner_big.png', size: {width: 42, height: 46}}
        },
        //大头针配置数组
        annotationConfArr: [],
        //当前处于点开状态的大头针
        tempActivePin: {
            index: '',
            publishType: ''
        },
        //当前处于展示状态的卡片
        tempActiveCard: '',
    },
    mounted: function () {
        this.init();
    },
    created: function () {
        this.fitFilterItem();
    },
    methods: {
        init: function () {
            $.showIndicator();
            $.ajax({
                url: "/pethome/search/init",
                dataType: "json",
                type: "GET",
                success: function (res) {
                    if (res.code == 1) {
                        app.annotationList = res.data;
                        //配置一个数组用于控制大头针状态
                        app.confPinIcon();
                    }
                    $.hideIndicator();
                }
            });
            this.laodPetVariety();
        },
        closeDetail: function (event) {
            if (!event.overlay) {
                $(".marker-card").fadeOut();
            }
        },
        laodPetVariety: function () {
            this.showVarietyArr = this.dogVarietyArr;
        },
        confPinIcon: function () {
            //初始化大头针
            for (var i = 0; i < this.annotationList.length; i++) {
                this.annotationList[i].publishType == 1 ? this.annotationConfArr.push(this.annotationConf.icon_fpet) : this.annotationConfArr.push(this.annotationConf.icon_fowner);
            }
        },
        search: function () {

            /*if (!this.keyword) {
                $.toast("搜索关键字不能为空~");
                return;
            }*/
            $.showIndicator();
            $.ajax({
                url: "/pethome/search/",
                dataType: "json",
                type: "GET",
                data: {
                    publishType: app.publishType,
                    classifyId: app.classifyID,
                    varietyId: app.varietyID,
                    petSex: app.petSex,
                    keyWord: app.keyword
                },
                success: function (res) {
                    if (res.code == 1) {
                        app.annotationList = res.data;
                        //配置一个数组用于控制大头针状态
                        if(app.publishType == 1){
                            //寻宠
                            app.annotationConfArr = new Array();
                            for(var i = 0 ; i < app.annotationList.length; i++){
                                app.annotationConfArr.push(app.annotationConf.icon_fpet);
                            }
                        }
                        if(app.publishType == 2){
                            //寻主
                            app.annotationConfArr = new Array();
                            for(var i = 0 ; i < app.annotationList.length; i++){
                                app.annotationConfArr.push(app.annotationConf.icon_fowner);
                            }
                        }
                        if(app.publishType == -1){
                            app.annotationConfArr = new Array();
                            for(var i = 0 ; i < app.annotationList.length; i++){
                               if(app.annotationList[i].publishType == 1){
                                   app.annotationConfArr.push(app.annotationConf.icon_fpet);
                               }else {
                                   app.annotationConfArr.push(app.annotationConf.icon_fowner);
                               }
                            }
                        }
                        $.hideIndicator();
                    }else if(res.code == 2){
                        $.toast("未查询到相关的宠物");
                        $.hideIndicator();
                    }
                }
            });
        },
        draw: function () {
            //关闭已经打开的卡片
            $(".marker-card").fadeOut();
        },
        reset: function () {
            //重置筛选信息
            this.publishType = -1;
            this.classifyID = -1;
            this.varietyID = -1;
            this.svIndex = -1;
            this.petSex = -1;
        },
        showCard: function () {
            $(".marker-card").fadeIn().css("diplay", "flex");
        },
        //发布类别
        changePublishType: function (type) {
            type == 1 ? this.publishType = 2 : this.publishType = 1;
        },
        //宠物类别
        changePetClass: function (type) {
            type == 2 ? this.classifyID = 2 : this.classifyID = 3;
            this.classifyID == 2 ? this.showVarietyArr = this.catVarietyArr : this.showVarietyArr = this.dogVarietyArr;
        }
        ,
        //宠物性别
        changePetSex: function (sex) {
            console.log(sex);
            sex == 1 ? this.petSex = 1 : this.petSex = 0;
        },
        fitFilterItem: function () {
            var ScreenWidth = document.body.clientWidth;
            if (ScreenWidth < 350) {
                $(".filter-item").css("margin-bottom", "1rem");
            }
        },
        selectVariety: function (index, vid) {

            this.svIndex = index;
            this.varietyID = vid;
        },
        //打开详情
        openDetail: function (item, index) {
            //重置上一次点击状态
            if (this.tempActivePin.publishType == 2 || this.tempActivePin.publishType == 1) {
                if (this.tempActivePin.publishType == 1) {
                    this.annotationConfArr.splice(this.tempActivePin.index, 1, this.annotationConf.icon_fpet);
                } else {
                    this.annotationConfArr.splice(this.tempActivePin.index, 1, this.annotationConf.icon_fowner);
                }
            }
            //设置此次点击状态
            item.publishType == 1 ? this.annotationConfArr.splice(index, 1, this.annotationConf.icon_fpet_big) : this.annotationConfArr.splice(index, 1, this.annotationConf.icon_fowner_big);
            //设置tempActivePin
            this.tempActivePin = {
                index: index,
                publishType: item.publishType
            };
            //展示卡片
            this.openCard(item);
        },
        //打开卡片
        openCard: function (item) {
            item.petImage = item.petImage.split(";")[0];
            item.lostTime = item.lostTime.split(" ")[0];
            item.createTime = item.createTime.split(" ")[0];

            this.tempActiveCard = item;
            $(".marker-card").fadeIn();
        },
        goDetail: function (tempActiveCard) {

            //认证流程
            $.ajax({
                url: '/pethome/user/auth/' + GetQueryString("openid"),
                type: 'GET',
                dataType: 'json',
                data: null,
                success: function (res) {
                    if (res.code === 1) {

                        if (res.data.type == "person") {

                            if (res.data.state == 0) {
                                $.modal(goAuthormodal);
                            } else {
                                window.location.href = "./detail.html?openid=" + GetQueryString("openid") + "&id=" + tempActiveCard.id;
                            }
                        } else if (res.data.type == "association") {

                            if (res.data.state == 0) {

                                $.modal(goAuthormodal);

                            } else if (res.data.state == 1) {

                                window.location.href = "./detail.html?openid=" + GetQueryString("openid") + "&id=" + tempActiveCard.id;

                            } else if (res.data.state == 2) {

                                window.location.href = "./ov-state.html?openid=" + GetQueryString("openid") + "&id=" + tempActiveCard.id;
                            }
                        }
                    } else {
                        $.modal(goAuthormodal);
                    }
                }
            });
        }
    }
});
//抽屉监听
$(document).on("open", ".panel", function () {
    $(".mask").fadeIn(500);
});
$(document).on("close", ".panel", function () {
    $(".mask").fadeOut();
});


/**
 * 提示认证
 */
function closeGoAuthorModal() {
    $.closeModal();
}

//选择认证类型
function goAuthor() {
    $.modal(selectAuthorTypeModal);
}

function toAuthorPage(type) {

    type == 0 ? window.location.href = "pv.html?openid=" + GetQueryString("openid") : window.location.href = "ov.html?openid=" + GetQueryString("openid");
}

//提示需要认证modal
var goAuthormodal = {
    title: '',
    afterText: '<div class="author-modal">' +
    '<div class="close" onClick="closeGoAuthorModal()"><img src="img/icon/icon-modal-close.png"/></div>' +
    '<div class="avatar"><img src="img/icon/goValidate.png"/></div>' +
    '<div class="modal-msg">实名认证后可以解锁更多走丢宠物信息</div>' +
    '<a class="modal-btn" onClick="goAuthor()">去认证</a>' +
    '</div>',
};
//选择认证类型modal
var selectAuthorTypeModal = {
    title: '',
    afterText: '<div class="author-modal">' +
    '<div class="close" onClick="closeGoAuthorModal()"><img src="img/icon/icon-modal-close.png"/></div>' +
    //个人认证
    '<div class="author-item" onClick="toAuthorPage(0)" style="margin-top:1.5rem;">' +
    '<div class="author-item-inner">' +
    '<img src="img/icon/pv.png"/><span>个人认证</span>' +
    '</div>' +
    '</div>' +
    '<div class="author-item" onClick="toAuthorPage(1)" style="margin-bottom:1.3rem;">' +
    '<div class="author-item-inner">' +
    '<img src="img/icon/ov.png"><span>组织或协会认证</span>' +
    '</div>' +
    '</div>' +
    //组织或协会认证
    '</div>'
};