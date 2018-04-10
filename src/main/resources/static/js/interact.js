var interact = new Vue({
    el: "#interactPage",
    data: {
        showMsgList:[]
    },
    mounted: function () {
        this.publicMsg();
    },
    methods: {
        publicMsg :function () {

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
        reply: function (index,replierFrom ,talkId ,publishId) {
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
                                    replierAccept: replierFrom,
                                    content: content
                                },
                                success: function (res) {
                                    if (res.code === 1) {
                                        var msg = {
                                            content : res.data.content,
                                            userIdAccept : res.data.replierAccept,
                                            userIdAcceptName : res.data.replierAcceptName,
                                            userIdFrom : res.data.replierFrom,
                                            userIdFromName :res.data.replierFromName
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