var app = new Vue({
    el: "#minePage",
    data: {
        myself: {},
        param: '',
        privateMsgArr: []
    },
    mounted: function () {
        this.loadMine();
    },
    created: function () {
        this.param = GetQueryString("openid");
    },
    methods: {
        loadMine: function () {

            $.ajax({
                url: '/pethome/user/' + GetQueryString("openid"),
                type: 'GET',
                dataType: 'json',
                data: null,
                success: function (res) {
                    if (res.code === 1) {
                        app.myself = res.data;
                    }
                }
            });
        },
        goTask: function () {

            /*
            if (!app.myself.approveState) {//未认证 && 认证失败
                $.modal(goAuthormodal);
            }else if (app.myself.approveState == 2) { //认证中
                window.location.href = "./ov-state.html?state=2&type=2&openid=" + this.param;
            } else if (app.myself.approveState == 1) { //认证成功
                $.router.loadPage("./mine/task.html?openid=" + this.param);
            }
            */
            $.router.loadPage("./mine/task.html?type=1&openid=" + this.param);

        },
        goPublish: function () {

            /*
            if (!app.myself.approveState) {//未认证 && 认证失败
                $.modal(goAuthormodal);
            }else if (app.myself.approveState == 2) { //认证中
                window.location.href = "./ov-state.html?state=2&type=2&openid=" + this.param;
            } else if (app.myself.approveState == 1) { //认证成功
                $.router.loadPage("./mine/task.html?openid=" + this.param);
            }*/

            $.router.loadPage("./mine/task.html?type=2&openid=" + this.param);
        },
        goPrivateMsg: function () {

            /*if (!app.myself.approveState) {//未认证 && 认证失败

                $.modal(goAuthormodal);

            }else if (app.myself.approveState == 2) { //认证中

                window.location.href = "./ov-state.html?state=2&type=2&openid=" + this.param;

            } else if (app.myself.approveState == 1) { //认证成功

                $.router.loadPage("./mine/chat.html?openid=" + this.param);
            }*/

            $.router.loadPage("./mine/chat.html?openid=" + this.param);
        },
        goPublishMsg: function () {

/*            if (!app.myself.approveState) {//未认证 && 认证失败

                $.modal(goAuthormodal);

            }else if (app.myself.approveState == 2) { //认证中

                window.location.href = "./ov-state.html?state=2&type=2&openid=" + this.param;

            } else if (app.myself.approveState == 1) { //认证成功

                $.router.loadPage("./mine/interact.html?openid=" + this.param);
            }*/

            $.router.loadPage("./mine/interact.html?openid=" + this.param);
        },
        goApprove:function () {

            if (!app.myself.approveState) {//未认证 && 认证失败

                if(app.myself.approveType == 1){
                    $.modal(goAuthormodal);
                }else {
                    window.location.href = "./ov-state.html?state=0&type=2&openid=" + this.param;
                }

            }else if (app.myself.approveState == 2) { //认证中

                window.location.href = "./ov-state.html?state=2&type=2&openid=" + this.param;

            } else if (app.myself.approveState == 1) { //认证成功

                if(app.myself.approveType == 1){

                    window.location.href = "./pved.html?mobile="+ app.myself.mobile +"&openid=" + this.param;
                }else {
                    window.location.href ="./oved.html?openid=" + this.param;
                }

            }
        }
    }
});
// $.init();
$(document).on("pageInit", function (e, pageId, $page) {

    /**
     * 我的任务 我的发布
     */
    if (pageId == "taskPage") {
        document.title = "我的任务";
        var task = new Vue({
            el: "#taskPage",
            data: {
                isPend: true,
                taskArr: [],
                //待处理
                pendNum:'',
                //我发布得
                mineNum:''
            },
            mounted: function () {
                var type = GetQueryString("type");
                var url = null;
                //待处理
                if(type == 1){
                    url= '/pethome/publish/task/' + GetQueryString("openid");
                    this.isPend = true;
                }else {
                    this.isPend = false;
                    url= '/pethome/publish/' + GetQueryString("openid");
                }

                $.ajax({
                    url: url,
                    type: 'GET',
                    dataType: 'json',
                    data: null,
                    success: function (res) {

                        if (res.code === 1) {

                            if(type == 1){
                                task.pendNum = "（" + res.data.length +"）";

                            }else {
                                task.mineNum = "（" + res.data.length +"）";
                            }
                            task.taskArr = res.data;
                            for (var i = 0; i < task.taskArr.length; i++) {
                                task.taskArr[i].petImage = task.taskArr[i].petImage.split(";")[0];
                            }
                        }
                    }
                });
            },

            methods: {
                waitDeal: function () {
                    this.isPend = true;

                    $.ajax({
                        url: '/pethome/publish/task/' + GetQueryString("openid"),
                        type: 'GET',
                        dataType: 'json',
                        data: null,
                        success: function (res) {
                            if (res.code === 1) {
                                task.taskArr = res.data;
                                task.pendNum = "（" + res.data.length + "）";
                                //task.mineNum = '';
                                for (var i = 0; i < task.taskArr.length; i++) {
                                    task.taskArr[i].petImage = task.taskArr[i].petImage.split(";")[0];
                                }
                            }
                        }
                    });
                },
                myPublish: function () {

                    this.isPend = false;
                    //我的发布
                    $.ajax({
                        url: '/pethome/publish/' + GetQueryString("openid"),
                        type: 'GET',
                        dataType: 'json',
                        data: null,
                        success: function (res) {
                            if (res.code === 1) {
                                task.taskArr = res.data;
                                //task.pendNum = '';
                                task.mineNum = "（"+ res.data.length +"）";
                                for (var i = 0; i < task.taskArr.length; i++) {
                                    task.taskArr[i].petImage = task.taskArr[i].petImage.split(";")[0];
                                }
                            }
                        }
                    });
                },
                findPet: function (index) {

                    if (this.taskArr[index].findState) {
                        return;
                    }

                    $.ajax({
                        url: '/pethome/publish/pet/find/' + GetQueryString("openid"),
                        type: 'POST',
                        dataType: 'json',
                        data: {
                            id: this.taskArr[index].id
                        },
                        success: function (res) {
                            if (res.code) {
                                task.taskArr[index].findState = res.data.findState;
                            }
                        }
                    });

                },
                enterDetail :function (id) {
                    window.location.href = "./../detail.html?openid=" + GetQueryString("openid") + "&id=" + id;
                }
            }
        });
    }


    /**
     * 我的转发
     */
    if (pageId == "msgPage") {
        document.title = "我的转发";
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
                init: function () {

                    $.ajax({
                        url: '/pethome/user/share/' + GetQueryString("openid"),
                        type: 'GET',
                        dataType: 'json',
                        data: {
                            type: 8
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
                    if (type == 8) {
                        message.navActive = true;
                    } else {
                        message.navActive = false;
                    }

                    $.ajax({
                        url: '/pethome/user/share/' + GetQueryString("openid"),
                        type: 'GET',
                        dataType: 'json',
                        data: {
                            type: type
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
    }
    /**
     * 关注
     */
    if (pageId == 'attentionPage') {
        document.title = "我的关注";

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
                init: function () {

                    $.ajax({
                        url: '/pethome/user/like/' + GetQueryString("openid"),
                        type: 'GET',
                        dataType: 'json',
                        data: {
                            type: 2
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
                    if (type == 2) {
                        message.navActive = true;
                    } else {
                        message.navActive = false;
                    }

                    $.ajax({
                        url: '/pethome/user/like/' + GetQueryString("openid"),
                        type: 'GET',
                        dataType: 'json',
                        data: {
                            type: type
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
    }

    /**
     * 私信
     */
    if (pageId == 'chatPage') {

        document.title = "我的私信";

        var interact = new Vue({
            el: "#chatPage",
            data: {
                showMsgList: []
            },
            mounted: function () {
                app.myself.privateMsgCount = 0;
                this.privateMsg();
            },
            methods: {
                privateMsg: function () {

                    $.ajax({
                        url: '/pethome/user/private/' + GetQueryString("openid"),
                        type: 'GET',
                        dataType: 'json',
                        data: null,
                        success: function (res) {
                            if (res.code) {
                                interact.showMsgList = res.data;
                            }
                        }
                    });

                },
                reply: function (index, userIdFrom, talkId, publishId) {
                    $.modal({
                        title: '回复',
                        afterText: '<div class="" ><textarea  id = "replayContent" class="modal-reply-input"></textarea></div>',
                        buttons: [
                            {
                                text: '取消'
                            },
                            {
                                text: '确认',
                                bold: true,
                                onClick: function () {

                                    var content = $("#replayContent").val();
                                    if (!content) {
                                        alert("请输入回复内容");
                                        return;
                                    }

                                    $.ajax({
                                        url: '/pethome/message/private/' + publishId,
                                        type: 'PUT',
                                        dataType: 'json',
                                        data: {
                                            talkId: talkId,
                                            userIdFrom: GetQueryString("openid"),
                                            userIdAccept: userIdFrom,
                                            content: content
                                        },
                                        success: function (res) {
                                            if (res.code === 1) {

                                                var msg = {
                                                    content: res.data.content,
                                                    userIdAccept: res.data.replierAccept,
                                                    userIdAcceptName: res.data.replierAcceptName,
                                                    userIdFrom: res.data.replierFrom,
                                                    userIdFromName: res.data.replierFromName
                                                };
                                                interact.showMsgList[index].detail.push(msg);
                                            } else {
                                                alert(res.msg);
                                            }
                                        }
                                    });
                                }
                            }
                        ]
                    })
                }
            }
        });
    }


    /**
     * 互动
     */
    if (pageId == "interactPage") {

        document.title = "我的互动";

        var interact = new Vue({
            el: "#interactPage",
            data: {
                showMsgList: []
            },
            mounted: function () {

                app.myself.publicMsgCount = 0;
                this.publicMsg();
            },
            methods: {
                publicMsg: function () {

                    $.ajax({
                        url: '/pethome/user/public/' + GetQueryString("openid"),
                        type: 'GET',
                        dataType: 'json',
                        data: null,
                        success: function (res) {
                            if (res.code) {
                                interact.showMsgList = res.data;
                            }
                        }
                    });

                },
                reply: function (index, userIdFrom, talkId, publishId) {
                    $.modal({
                        title: '回复',
                        afterText: '<div class=""><textarea id = "replayContent" class="modal-reply-input"></textarea></div>',
                        buttons: [
                            {
                                text: '取消'
                            },
                            {
                                text: '确认',
                                bold: true,
                                onClick: function () {

                                    var content = $("#replayContent").val();
                                    if (!content) {
                                        alert("请输入回复内容");
                                        return;
                                    }

                                    $.ajax({
                                        url: '/pethome/message/public/' + publishId,
                                        type: 'PUT',
                                        dataType: 'json',
                                        data: {
                                            talkId: talkId,
                                            replierFrom: GetQueryString("openid"),
                                            replierAccept: userIdFrom,
                                            content: content
                                        },
                                        success: function (res) {
                                            if (res.code === 1) {
                                                var msg = {
                                                    content: res.data.content,
                                                    userIdAccept: res.data.replierAccept,
                                                    userIdAcceptName: res.data.replierAcceptName,
                                                    userIdFrom: res.data.replierFrom,
                                                    userIdFromName: res.data.replierFromName
                                                };
                                                interact.showMsgList[index].detail.push(msg);
                                            } else {
                                                $.toast(res.msg);
                                            }
                                        }
                                    });
                                }
                            }
                        ]
                    })
                }
            }
        });
    }

    /**
     * 认证 (未使用)
     */
    if (pageId == "ovedPage") {

        document.title = "我的认证";
        var ovedPage = new Vue({
            el: "#ovedPage",
            data: {
                validateState: true,
                param: "",
            },
            created: function () {
                this.param = GetQueryString("openid");
            },
            mounted: function () {
                this.init();
            },
            methods: {

                init: function () {

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
                                        window.location.href = "./pv.html?openid=" + GetQueryString("openid");
                                    }
                                } else if (res.data.type == "association") {

                                    if (res.data.state == 0) {

                                        window.location.href = "./ov-state.html?openid=" + GetQueryString("openid");

                                    } else if (res.data.state == 1) {

                                        window.location.href = "./oved.html?openid=" + GetQueryString("openid");

                                    } else if (res.data.state == 2) {

                                        window.location.href = "./oved-state.html?openid=" + GetQueryString("openid");
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
    }
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