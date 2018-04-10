var app = new Vue({
    el: "#detailPage",
    data: {
        //消息类型：互动/私信
        msgListType: "interactMsg",
        //是否认证
        isAuthority: false,
        petFindState: 2,
        imgList: [],
        //当前展示列表
        showMsgList: [],
        //控制展开的配置数组
        msgListConfArr:[],
        msgListConfType:{
            df:{state:0, btnText:''},
            expand:{state:1,btnText:'展开'},
            hide:{state:2,btnText:'收起'}
        },
        //私信列表数据是否为空
        privateMsgListNull: true,
        detailData: {},
        commentContent: '',
        placeholderText: '说点什么',
        petImageArr: []
    },
    mounted: function () {
        this.init()
    },
    methods: {
        init: function () {

            this.showMsgList = new Array();
            $.ajax({
                url: '/pethome/publish/detail/' + GetQueryString("openid"),
                type: 'GET',
                contentType: "application/x-www-form-urlencoded",
                dataType: 'json',
                data: {
                    id: GetQueryString("id")
                },
                success: function (res) {
                    if (res.code === 1) {

                        /*if (res.data.approveState != 1) {
                            window.location.href = "./index.html?openid=" + GetQueryString("openid");
                            return;
                        }*/
                        app.detailData = res.data;
                        app.showMsgList = res.data.publicTalk;
                        //配置展开的数组
                        app.configExpandType(app.showMsgList);
                        app.petImageArr = app.detailData.petImage.split(";");

                        if (app.detailData.publisherId == GetQueryString("openid")) {
                            app.isAuthority = true;
                        }

                        app.wechatInit();

                    } else {
                        $.toast(res.msg);
                    }
                }
            });
        },
        configExpandType:function (arr) {

            if(!arr)
                return false;
            //reply条数过多自动隐藏
            for(var i = 0 ; i < arr.length; i++){
               if(arr[i].length > 3){
                   this.msgListConfArr.push(this.msgListConfType.expand);
               }
               else{
                   this.msgListConfArr.push(this.msgListConfType.df);
               }
            }
        },
        //展开所有评论
        expand:function (index) {
            if(this.msgListConfArr[index].state == 1){
                    //展开
                this.msgListConfArr.splice(index,1,this.msgListConfType.hide);
            }else{
                    //收起
                this.msgListConfArr.splice(index,1,this.msgListConfType.expand);
            }
        },
        wechatInit: function () {

            $.ajax({
                url: '/pethome/wechat/jsApiSignature',
                type: 'GET',
                dataType: 'json',
                data: {
                    url: encodeURI(window.location.href)
                },
                success: function (res) {

                    if (res.code === 1) {
                        wx.config({
                            debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来
                            appId: res.data.appId, // 必填，公众号的唯一标识
                            timestamp: res.data.timestamp, // 必填，生成签名的时间戳
                            nonceStr: res.data.nonceStr, // 必填，生成签名的随机串
                            signature: res.data.signature,// 必填，签名，见附录1
                            jsApiList: [
                                "onMenuShareTimeline",
                                "onMenuShareAppMessage",
                                "onMenuShareQQ",
                                "onMenuShareQZone",
                                "onMenuShareWeibo"
                            ]
                        });
                    }
                }
            });
        },
        //找到了
        found: function (publisherId) {

            if (GetQueryString("openid") != publisherId) {
                alert("您不是主题发布者,不能操作此选项");
            }

            if (app.detailData.findState) {
                $.toast("已经找到了");
            }

            $.ajax({
                url: '/pethome/publish/pet/find/' + GetQueryString("openid"),
                type: 'POST',
                dataType: 'json',
                data: {
                    id: GetQueryString("id")
                },
                success: function (res) {
                    if (res.code) {
                        app.isAuthority = true;
                        app.detailData.findState = 1;
                    }
                }
            });
        },
        //分享
        // share: function () {
        //     openShareRemind();
        // },
        //关注
        attention: function () {

            var params = {
                publishId: app.detailData.id,
                dynamicType: 0
            };
            if (app.detailData.likeState) {
                params.dynamicType = 3;//取关
            } else {
                params.dynamicType = 1;//关注
            }
            //load data
            $.ajax({
                url: '/pethome/dynamic/like/' + GetQueryString("openid"),
                type: 'PUT',
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify(params),
                success: function (res) {
                    if (res.data === true) {
                        if (app.detailData.likeState) {
                            $.toast("已取关");
                        } else {
                            $.toast("已关注");
                        }
                        app.detailData.likeState = !app.detailData.likeState;
                    }
                }
            });
        },
        //切换消息列表
        changeList: function (type) {
            var data = null;
            var url = null;
            if (type == 0) {
                this.placeholderText = '说点什么';
                this.msgListType = "interactMsg"
                url = '/pethome/message/public/' + GetQueryString("id");

            } else {
                this.placeholderText = '我有新线索';
                this.msgListType = "privateMsg"
                url = '/pethome/message/private/' + GetQueryString("id");
                data = {openId: GetQueryString("openid")}//私信
            }

            $.ajax({
                url: url,
                type: 'GET',
                dataType: 'json',
                data: data,
                success: function (res) {


                    if (res.code === 1) {

                        var result = null;
                        if (type == 0) {
                            result = res.data.publicTalk;
                        } else {
                            result = res.data.privateTalk;
                        }

                        if (!result) { //没有值
                            if (!app.showMsgList) {
                                app.showMsgList = new Array();
                            }
                            app.showMsgList.push([res.data]);

                        } else {//有值

                            app.showMsgList = result;
                        }
                    } else if (res.code == 2) {

                        app.showMsgList = [];

                    } else {
                        alert(res.msg);
                    }
                }
            });
        },
        //发送评论
        sendComment: function () {

            if (!this.commentContent) {
                alert("请输入回复内容");
                return;
            }

            var data = null;
            var url = null;

            if (this.msgListType == "interactMsg") {
                this.msgListType = "interactMsg";
                url = '/pethome/message/public/' + GetQueryString("id");
                data = {
                    replierFrom: GetQueryString("openid"),
                    replierAccept: this.detailData.publisherId,
                    content: this.commentContent
                }
            } else {
                this.msgListType = "privateMsg";
                url = '/pethome/message/private/' + GetQueryString("id")
                data = {
                    userIdFrom: GetQueryString("openid"),
                    userIdAccept: this.detailData.publisherId,
                    content: this.commentContent
                }
            }

            $.ajax({
                url: url,
                type: 'PUT',
                dataType: 'json',
                data: data,
                success: function (res) {

                    if (res.code == 1) {
                        if (!app.showMsgList) {
                            app.showMsgList = new Array();
                        }
                        console.log(res.data);
                        app.showMsgList.push([res.data]);
                        app.commentContent = '';
                        if (app.msgListType == "interactMsg")
                            app.detailData.publicMsgCount++;
                    } else {
                        $.toast(res.msg);
                    }
                }
            });
        },

        //回复评论
        reply: function (index, talkId, replierFrom) {
            $.modal({
                title: '回复',
                text: '',
                afterText: '<textarea placeholder="说点什么" class="reply-modal-textarea" id="replayContent"></textarea>',
                buttons: [{
                    text: '确定',
                    onClick: function () {

                        var content = $("#replayContent").val();

                        if (!content) {
                            $.toast("请输入回复内容");
                            return;
                        }

                        var data = null;
                        var url = null;
                        if (app.msgListType == "interactMsg") {
                            url = '/pethome/message/public/' + GetQueryString("id");
                            data = {
                                talkId: talkId,
                                replierFrom: GetQueryString("openid"),
                                replierAccept: replierFrom,
                                content: content
                            }
                        } else {

                            url = '/pethome/message/private/' + GetQueryString("id");
                            data = {
                                talkId: talkId,
                                userIdFrom: GetQueryString("openid"),
                                userIdAccept: replierFrom,
                                content: content
                            }
                        }

                        $.ajax({
                            url: url,
                            type: 'PUT',
                            dataType: 'json',
                            data: data,
                            success: function (res) {
                                if (res.code === 1) {
                                    app.showMsgList[index].push(res.data);
                                } else {
                                    $.toast(res.msg);
                                }
                            }
                        });
                    }
                }]
            })
        }
    }
});


$(function () {

    wx.ready(function () {

        var wechatParams = {};
        var publishType = app.detailData.publishType;
        if (publishType == 1) {
            wechatParams.title = '"寻宠"宝贝快回家';
            wechatParams.desc = "宠物丢了别着急，一键上传丢失爱宠信息，全平台发布及时反馈助力爱宠找回更容易。";
        } else {
            wechatParams.title = '"寻主”主人你在哪？';
            wechatParams.desc = "路遇流浪动物不糟心，随手上传为它创立云端信息，云端储存快速匹配传爱心。";
        }
        wechatParams.link = getAuthURL() + 'detail.html?id=' + GetQueryString("id");
        wechatParams.imgUrl = getResourceUlr() + 'upload/picture/petLove.png';

        /**
         * 分享好友
         */
        wx.onMenuShareAppMessage({

            title: wechatParams.title,
            desc: wechatParams.desc,
            link: wechatParams.link,
            imgUrl: wechatParams.imgUrl,
            trigger: function () {
            },
            success: function () {
                /**
                 * 分享次数加1
                 */
                var params = {
                    publishId: GetQueryString("id"),
                    dynamicType: 2
                };
                $.ajax({
                    url: '/pethome/dynamic/share/' + GetQueryString("openid"),
                    type: 'PUT',
                    contentType: 'application/json',
                    dataType: 'json',
                    data: JSON.stringify(params),
                    success: function (res) {
                        if (res.data === true) {
                            //$.toast("分享成功")
                        }
                    }
                });
            },
            cancel: function (res) {
            },
            fail: function (res) {
            }
        });

        /**
         * 朋友圈
         */
        wx.onMenuShareTimeline({
            title: wechatParams.title,
            desc: wechatParams.desc,
            link: wechatParams.link,
            imgUrl: wechatParams.imgUrl,
            success: function () {

                var params = {
                    publishId: GetQueryString("id"),
                    dynamicType: 2
                };
                $.ajax({
                    url: '/pethome/dynamic/share/' + GetQueryString("openid"),
                    type: 'PUT',
                    contentType: 'application/json',
                    dataType: 'json',
                    data: JSON.stringify(params),
                    success: function (res) {
                        if (res.data === true) {
                            $.toast("分享成功")
                        }
                    }
                });
            }, cancel: function () {
            }
        });
    });
});