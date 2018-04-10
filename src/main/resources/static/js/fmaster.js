var app = new Vue({
    el: "#fpet",
    data: {
        petType: '',
        maleActive: false,
        femaleActive: false,
        petImageArr: [],
        //控制显示
        classifyName:'汪',
        varietyName:'喜乐蒂',
        //控制品种
        varietyArrDataSource:[],
        varietyArr:[],
        formData: {
            publishType: 2,
            classifyId: 3,
            varietyId: 9,
            petSex: 1,
            petDescription: '',
            petImage: '',
            lostTime: '',
            lostLocation: '',
            latitude: 0.0,
            longitude: 0.0,
            ownerName: '',
            ownerContact: '',
            publisherId : GetQueryString("openid")
        }
    },
    mounted: function () {
        this.init();
    },
    methods: {
        init: function () {
            //加载宠物类别
            this.varietyArrDataSource = varietyArrDataSource;
            this.varietyArr = this.varietyArrDataSource["3"];
            this.varietyName = this.varietyArr[0].name;

            //初始化时间
            var date = new Date().Format("yyyy-MM-dd HH:mm:ss");
            this.formData.lostTime = date;
        },
        selectSex: function (sex) {
            if (sex == 1) {
                //公
                this.maleActive = !this.maleActive;
                this.femaleActive = false;
                this.maleActive ? this.formData.PetSex = 1 : this.formData.PetSex = '';
            } else {
                //母
                this.femaleActive = !this.femaleActive;
                this.maleActive = false;
                this.femaleActive ? this.formData.PetSex = 0 : this.formData.PetSex = '';
            }
        },
        hasNullItem:function (obj) {
            return hasNullItem(obj);
        },
        //选择品种
        selectVarietyArr: function (id, name) {
            //id用于上传，name用于绑定model显示中文
            app.formData.varietyId = id;
            app.varietyName = name;
        },
        submitRelease: function () {

            if (this.petImageArr.length == 0) {
                $.toast("请上传宠物照片哟~");
                return;
            }
            this.formData.petImage = this.petImageArr.join(";");

            if(this.formData.ownerContact.length != 11){
                $.toast("手机号位数不正确");
                return;
            }

            $.showPreloader();

            $.ajax({
                url: '/pethome/publish/master/'+GetQueryString("openid"),
                type: 'PUT',
                contentType: "application/x-www-form-urlencoded",
                dataType: 'json',
                data: app.formData,
                success: function (res) {
                    if (res.code === 1) {
                        app.dynamicArr = res.data;
                        window.location.href = "./index.html?openid=" + GetQueryString("openid");
                    } else {
                        $.toast(res.msg);
                    }
                    $.hidePreloader();
                }
            });
        },
        removePetImg: function (index) {
            this.petImageArr.splice(index, 1);
        }
    }
});

//event monitor
$(document).on('click', '.create-actions', function () {
    var buttons1 = [{
        text: '请选择',
        label: true
    },
        {
            text: '汪',
            onClick: function () {
                app.formData.classifyId = 3;
                //切换宠物类别后给一个默认的品种id(默认为第一个)
                app.formData.varietyId = 9;
                app.classifyName = '汪';
                app.varietyArr = app.varietyArrDataSource['3'];
                app.varietyName = app.varietyArr[0].name;
            }
        },
        {
            text: '喵',
            onClick: function () {
                app.formData.classifyId = 2;
                //切换宠物类别后给一个默认的品种id(默认为第一个)
                app.formData.varietyId = 3;
                app.classifyName = '喵';
                app.varietyArr = app.varietyArrDataSource['2'];
                app.varietyName = app.varietyArr[0].name;
            }
        }
    ];
    var buttons2 = [{
        text: '取消'
    }];
    var groups = [buttons1, buttons2];
    $.actions(groups);
});
$(document).on('open', '.popup-petVariety', function () {
    $(".mask").fadeIn(500);
});
$(document).on('close', '.popup-petVariety', function () {
    $(".mask").fadeOut(500);
});
$("#datetime-picker").datetimePicker({
    toolbarTemplate: '<header class="bar bar-nav">\
    <button class="button button-link pull-right close-picker">确定</button>\
    <h1 class="title">选择日期和时间</h1>\
    </header>',
    onClose: function () {

        app.formData.lostTime = $("#datetime-picker").val()
    }
});

$(document).on("pageInit", function (e, pageId, $page) {
    if (pageId == 'locationPage') {
        //地图组件初始化
        Vue.use(VueBaiduMap.default, {
            ak: 'LOnzr56cOpw0LoZ5dt8GSGdej9YRjGrn'
        });
        //选择地址页面
        var locationPage = new Vue({
            el: "#locationPage",
            data: {
                testPoint: "{lng: 30.4660040000, lat: 114.4239750000}",
                scrollWheelOpen: true,
                keyword: '',
                maskShow: false,
                resultShow: false,
                location: "武汉",
                resList: []
            },
            methods: {
                search: function (w) {
                    //alert(w);
                },
                handleSearchComplete: function (res) {
                    //获取检索结果
                    if (res == undefined) return false;
                    if (res.zr.length > 0) {
                        this.resList = res.zr;
                    }
                },
                selectAddress: function (address, point) {
                    $.router.back("fpet.html");
                    app.formData.lostLocation = address;
                    app.formData.latitude = point.lat;
                    app.formData.longitude = point.lng;
                }
            },
            watch: {
                keyword: function (newStr, oldStr) {
                    if (newStr.length != 0) {
                        this.maskShow = true;
                        this.resultShow = true;
                    } else {
                        this.maskShow = false;
                        this.resultShow = false;
                    }
                }
            },
        });
    }
});

$(".petImg-upload").change(function (e) {

    //var type = $(this).data().type;

    if(app.petImageArr.length >=3 ){
        $.toast("图片最多只能上传三张");
        return;
    }

    var data = new FormData();
    $.each(e.target.files, function (i, file) {
        data.append("file", file);
    });

    $.ajax({
        url: "/pethome/upload/publish",
        type: 'PUT',
        data: data,
        cache: false,
        contentType: false,
        processData: false,
        success: function (respond) {
            app.petImageArr.push(respond.data);
            app.formData.petImage = app.petImageArr.join(";");
        }
    });
});

/**
 *
 *
 $(document).on("pageInit", function (e, pageId, $page) {
    if (pageId == 'locationPage') {
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
        //选择地址页面
        var app = new Vue({
            el: "#locationPage",
            data: {
                testPoint: "{lng: 30.4660040000, lat: 114.4239750000}",
                searchStr: '',
                maskShow: false,
                resultShow: false,
            },
            methods: {
                search: function (w) {
                    alert(w);
                }
            },
            watch: {
                searchStr: function (newStr, oldStr) {
                    if (newStr.length != 0) {
                        this.maskShow = true;
                        this.resultShow = true;
                    } else {
                        this.maskShow = false;
                        this.resultShow = false;
                    }
                }
            },
        });
    }
});
 *
 *
 */
