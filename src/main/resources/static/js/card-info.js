var app = new Vue({
    el: "#card-info",
    data: {
        petType: '',
        maleActive: false,
        femaleActive: false,
        formData: {

        }
    },
    methods: {}
});

//新建相册
$(document).on('click', '.album-new', function() {
    $.modal({
        title: '新建相册',
        text: '<input type="" class="album-create" placeholder="请添加相册名称"/>',
        buttons: [{
            text: '取消',
            bold: true,
            onClick: function() {
                $.alert('取消了')
            }
        }, {
            text: '完成',
            bold: true,
            onClick: function() {
                $.alert('完成了')
            }
        }, ]
    })
});

//删除相册
$(document).on('click', '.del-mask', function() {
    $.modal({
        title: '删除相册',
        text: '确定删除本相册，并清空里面的所有照片',
        buttons: [{
            text: '取消',
            bold: true,
            onClick: function() {
                $.alert('取消了')
            }
        }, {
            text: '删除',
            bold: true,
            onClick: function() {
                $.alert('删除成功')
            }
        }, ]
    })
});

$(function() {
    //初始化一些样式
    var awidth = $(".album-pic").width();
    $(".album-pic").css("height", awidth);
    $(".album-new").css("height", awidth);
    petCardAdaptive();
})