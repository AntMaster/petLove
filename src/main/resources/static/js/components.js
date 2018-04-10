//tabbar
Vue.component('mc-tabbar', {
    data: function () {
        return {
            index: false,
            search: false,
            card: false,
            mine: false,
            publish: false
        }
    },
    props: ['page', 'lv'],
    template: '<div class="mc-tabbar">' +
    '<div class="left">' +
    '<a :href="toIndex()" class="item external" :class="{active:index}">' +
    '<div class="icon"><img :src="tabbarIcon(1)" /></div><span class="name">首页</span>' +
    '</a>' +
    '<a :href="toSearch()" class="item external" :class="{active:search}">' +
    '<div class="icon"><img :src="tabbarIcon(2)" /></div><span  class="name">搜索</span>' +
    '</a>' +
    '</div>' +
    '<div class="center">' +
    '<a href="javascript:;" @click="openMenu" class="action external" :class="{active:publish}"><img :src="tabbarIcon(3)" /><span>发布</span></a>' +
    '</div>' +
    '<div class="right">' +
    '<a href="javacript:;" @click="toCard" class="item external" :class="{active:card}">' +
    '<div class="icon"><img :src="tabbarIcon(4)" /></div><span  class="name">宠卡</span>' +
    '</a>' +
    '<a :href="toMine()" class="item external" :class="{active:mine}">' +
    '<div class="icon"><img :src="tabbarIcon(5)" /></div><span  class="name">我的</span>' +
    '</a>' +
    '</div>' +
    '</div>',
    mounted: function () {
        switch (this.page) {
            case "index":
                this.index = true;
                break;
            case "search":
                this.search = true;
                break;
            case "card":
                this.card = true;
                break;
            case "mine":
                this.mine = true;
                break;
            case "publish":
                this.publish = true;
                break;
            default:
                break;
        }
    },
    methods: {
        openMenu:function(){
            openMenu();
        },
        toIndex: function () {

            if (this.lv == 2) return "../index.html?openid=" + GetQueryString("openid");
            return "index.html?openid=" + GetQueryString("openid");
        },
        toSearch: function () {
            if (this.lv == 2) return "../search.html?openid=" + GetQueryString("openid");
            return "search.html?openid=" + GetQueryString("openid");
        },
        // toPublish: function () {
        //     if (this.lv == 2) return "../fpet.html?openid=" + GetQueryString("openid");
        //     return "fpet.html?openid=" + GetQueryString("openid");
        // },
        toCard: function () {
            //load data

            var temp_lv = this.lv;
            $.ajax({
                url: '/pethome/pet/list/' + GetQueryString("openid"),
                type: 'GET',
                dataType: 'json',
                data: null,
                success: function (res) {
                    if (res.code === 1) {

                        if (temp_lv == 2) {
                            window.location.href = "../card-list.html?openid=" + GetQueryString("openid");
                        } else {
                            window.location.href = "card-list.html?openid=" + GetQueryString("openid");
                        }

                    } else {

                        if (temp_lv == 2) {
                            window.location.href = "../card-index.html?openid=" + GetQueryString("openid");
                        } else {
                            window.location.href = "card-index.html?openid=" + GetQueryString("openid");
                        }
                    }
                }
            });
        },
        toMine: function () {

            if (this.lv == 2) return "../mine.html?openid=" + GetQueryString("openid");
            return "mine.html?openid=" + GetQueryString("openid");
        },
        tabbarIcon:function(i) {

            switch (i) {
                case 1:
                {
                    if (this.lv == 2) {
                        if (this.index) {
                            return "../img/icon/bar-home-active.png";
                        } else {
                            return "../img/icon/bar-home.png";
                        }
                    } else {
                        if (this.index) {
                            return "img/icon/bar-home-active.png";
                        } else {
                            return "img/icon/bar-home.png";
                        }
                    }
                }
                    break;
                case 2:
                {
                    if (this.lv == 2) {
                        if (this.search) {
                            return "../img/icon/bar-search-active.png";
                        } else {
                            return "../img/icon/bar-search.png";
                        }
                    } else {
                        if (this.search) {
                            return "img/icon/bar-search-active.png";
                        } else {
                            return "img/icon/bar-search.png";
                        }
                    }
                }
                    break;
                case 3:
                {
                    if (this.lv == 2) return "../img/icon/icon-publish.png";
                    return "img/icon/icon-publish.png";
                }
                    break;
                case 4:
                {
                    if (this.lv == 2) {
                        if (this.card) {
                            return "../img/icon/bar-card-active.png";
                        } else {
                            return "../img/icon/bar-card.png";
                        }
                    } else {
                        if (this.card) {
                            return "img/icon/bar-card-active.png";
                        } else {
                            return "img/icon/bar-card.png";
                        }
                    }
                }
                    break;
                case 5:
                {
                    if (this.lv == 2) {
                        if (this.mine) {
                            return "../img/icon/bar-mine-active.png";
                        } else {
                            return "../img/icon/bar-mine.png";
                        }
                    } else {
                        if (this.mine) {
                            return "img/icon/bar-mine-active.png";
                        } else {
                            return "img/icon/bar-mine.png";
                        }
                    }
                }
                    break;
                default:
                    break;
            }
        }
    }
});