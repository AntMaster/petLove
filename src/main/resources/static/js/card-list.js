//宠卡列表
var app = new Vue({
    el: "#cardListPage",
    mounted: function () {
        this.loadPetList();
        this.params = GetQueryString("openid");
    },
    data: {
        petArr: [],
        params: '',
        varietyArrDataSource: '',
        varietyName: ''
    },
    updated: function () {
        //卡片自适应
        petCardAdaptive();
    },
    methods: {
        loadPetList: function () {
            //load data
            $.ajax({
                url: '/pethome/pet/list/' + GetQueryString("openid"),
                type: 'GET',
                dataType: 'json',
                data: null,
                success: function (res) {
                    if (res.code === 1) {
                        app.petArr = res.data;
                    }
                }
            });
        },
        goPetAlbum: function (index) {
            window.location.href = "./card-album.html?openid=" + GetQueryString("openid") + "&petid=" + index;
        }
    }
});

$(function () {
    //petCardAdaptive();
});

// $(function () {
//     //对窄机型做适配 width < 350
//     var petCardWidth = $(".petCard-item").width();
//     var ScreenWidth = document.body.clientWidth;
//     if (ScreenWidth < 350) {
//         $(".petCard-item").css("height", petCardWidth * 0.7);
//         $(".avatar").css({"height": "3rem", "width": "3rem", "border-radius": "1.5rem"})
//         $(".addPetCard-btn").css({"height": "2.5rem", "line-height": "2.5rem"});
//     } else {
//         $(".petCard-item").css("height", petCardWidth * 0.62);
//     }
// });