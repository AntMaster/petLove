var app = new Vue({
    el: "#fpet",
    data: {
        petType: '汪',
        maleActive: true,
        femaleActive: false,
        varietyArrDataSource: [],
        varietyArr: [],
        varietyName: '',
        petImageArr: [],
        formData: {
            publishType: 1,
            classifyId: 3,
            varietyId: 9,
            petName: null,
            petSex: 1,
            petDescription: null,
            petImage: null,
            lostTime: null,
            lostLocation: null,
            latitude: 0.0,
            longitude: 0.0,
            ownerName: null,
            ownerContact: null,
            publisherId : GetQueryString("openid")
        }
    },
    mounted: function () {
        this.init();
    },
    updated:function(){

    },
    methods: {
        init: function () {

            this.varietyArrDataSource = varietyArrDataSource;
            this.varietyArr = this.varietyArrDataSource["3"];
            this.varietyName = this.varietyArr[0].name;

            //初始化时间
            var date = new Date().Format("yyyy-MM-dd HH:mm:ss");
            this.formData.lostTime = date;
                //date.toLocaleString();
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
        selectSex: function (sex) {
            if (sex == 1) {
                //公
                this.maleActive = !this.maleActive;
                this.femaleActive = false;
                this.maleActive ? this.formData.petSex = 1 : this.formData.petSex = 0;
            } else {
                //母
                this.femaleActive = !this.femaleActive;
                this.maleActive = false;
                this.femaleActive ? this.formData.petSex = 0 : this.formData.petSex = 1;
            }
        },
        submitRelease: function () {


            if(this.formData.publisherId != GetQueryString("openid")){}

            if (this.petImageArr.length == 0) {
                $.toast("宠物照片必填");
                return;
            }

            if(this.formData.ownerContact.length != 11){
                $.toast("手机号位数不正确");
                return;
            }

            $.showPreloader();

            this.formData.petImage = this.petImageArr.join(";");
                $.ajax({
                    url: '/pethome/publish/pet/'+GetQueryString("openid"),
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
                //改变类别id同时需要一个默认品种
                app.formData.varietyId = 9;
                app.petType = '汪';
                app.varietyArr = app.varietyArrDataSource['3'];
                app.varietyName = app.varietyArr[0].name;
            }
        },
        {
            text: '喵',
            onClick: function () {
                app.formData.classifyId = 2;
                //改变类别id同时需要一个默认品种
                app.formData.varietyId = 3;
                app.petType = '喵';
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
            mounted:function () {
                //alert('已更新');
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
                handleLocationSuccess:function (res) {
                    $.toast(res);
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
  /*  var data = new FormData();
    $.each(e.target.files, function (i, file) {
        data.append("file", file);
    });*/


    lrz(this.files[0])
        .then(function (rst) {
            // 处理成功会执行
            $.ajax({
                url: "/pethome/upload/publish",
                type: 'PUT',
                data: rst.formData,
                cache: false,
                contentType: false,
                processData: false,
                success: function (respond) {
                    app.petImageArr.push(respond.data);
                    app.formData.petImage = app.petImageArr.join(";");
                }
            });

        })
        .catch(function (err) {
            // 处理失败会执行
        })
        .always(function () {
            // 不管是成功失败，都会执行
        });


    /*
        //创建FormData对象
        var data = new FormData();
        //为FormData对象添加数据
        //
        $.each($('#inputFile')[0].files, function (i, file) {
            data.append('file', file);
        });
        $.ajax({
            url: '/pethome/upload/publish',
            type: 'POST',
            data: data,
            cache: false,
            contentType: false,    //不可缺
            processData: false,    //不可缺
            success: function (data) {
                console.log(data)

            }
        });
    */

});