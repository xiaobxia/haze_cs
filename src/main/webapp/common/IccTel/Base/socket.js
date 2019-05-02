//以下操作前提：
//1.注册成功软电话(如何注册请联系实施人员)
//2.链接成功,并且签入成功
//除有特殊备注说明,否则下述所有方法调用都遵循上述的前提条件
wsk = {
    //2018-01-04 为电话条功能增加属性
    txtseatno: '',//工号
    txtstation: '',//分机
    txtpassowrd: '',//密码
    isConnect: false,
    docheckin: 0,
    isAlertWin: 1,//1为需要弹频0为不需要
    init: function (serUri) {
        if (serUri == "") {
            this.outms("未设置服务器地址");
        }
        else {
            cc = 0;
            this.InitSocket(serUri);
        }
    },
    InitSocket: function (serUri) {
        wsUri = serUri;
        websocket = new WebSocket(wsUri);
        websocket.onopen = function (evt) { wsk.onOpen(evt) };
        websocket.onclose = function (evt) { wsk.onClose(evt) };
        websocket.onmessage = function (evt) { wsk.onMessage(evt) };
        websocket.onerror = function (evt) { wsk.onError(evt) };
    },
    onOpen: function (evt) {
        this.outms("连接成功");
        wsk.isConnect = true;
        wsk.checkIn();
        this.heartbeat_timer = setInterval(function () { wsk.keepalive(websocket) }, 5000);
    },
    onClose: function (evt) {
        cc = 6;
        wsk.docheckin = 0;
        wsk.isConnect = false;
        ShowCheckIn("whfckIn");
        this.outms("连接关闭");
    },
    onError: function (evt) {
        cc = 6;
        this.outms('<span style="color: red;">ERROR:</span> ' + evt.data);
    },
    OnConnect: function () {
        this.init();
    },
    DisConnect: function () {
        if (websocket != null) {
            websocket.close();
            wsk.isConnect = false;
            websocket = null;
        }
    },
    keepalive: function (ws) {
        cc++;
        if (cc > 6) {
            this.DisConnect();
            return;
        }
        ws.send('{}');
    },
    loginServer: function (url) {
        wsUri = url;
        init();
    },
    checkIn: function () {
        str1 = '{"cmdsn":"100","seatno":"' + wsk.txtseatno + '","caller":"' + wsk.txtstation + '","para":"' + wsk.txtpassowrd + '","cmd":"1"}';
        websocket.send(str1);
        wsk.docheckin = 1;
    },
    checkOut: function () {
        str1 = '{"cmdsn":"101","seatno":"' + wsk.txtseatno + '","caller":"","para":"","cmd":"2"}';
        websocket.send(str1);
        wsk.docheckin = 0;
    },
    makeCall: function (calledtelno, externtion) {
        str1 = '{"cmdsn":"102","seatno":"' + wsk.txtseatno + '","caller":"","para":"' + calledtelno + '","cmd":"3"}';
        websocket.send(str1);
    },
    Callauther: function (callnum) {
        //三方通话(不只限于3人的通话,可以多人一起进行通话)
        //场景说明:当8000在与客户交流中时,8000 可以主动的发起邀请8001,8002,8003 等等参与本次的交流
        str1 = '{"cmdsn":"105","seatno":"' + wsk.txtseatno + '","caller":"' + wsk.txtseatno + '","para":"' + callnum + '","cmd":"11"}';
        websocket.send(str1);
    },
    CallautherStop: function (para) {
        //三方通话退出功能()
        str1 = '{"cmdsn":"105","seatno":"' + wsk.txtseatno + '","caller":"' + wsk.txtseatno + '","para":"' + para + '","cmd":"14"}';
        websocket.send(str1);
    },
    Callback: function (callnum) {
        str1 = '{"cmdsn":"106","seatno":"' + wsk.txtseatno + '","caller":"' + wsk.txtseatno + '","para":"' + callnum + '","cmd":"10"}'
        websocket.send(str1);
    },
    dropCall: function () {
        str1 = '{"cmdsn":"103","seatno":"' + wsk.txtseatno + '","caller":"","para":"","cmd":"4"}';
        websocket.send(str1);
    },
    SetSeatState: function (seatstate) {
        //设置分机状态(可以用来模拟示忙,示闲)
        //常见说明：公司只允许签入上班签出下班,那么中午休息,小休或是上洗手间等,可以设置成繁忙,回到座位后可以设置回空闲
        str1 = '{"cmdsn":"104","seatno":"' + wsk.txtseatno + '","caller":"","para":"' + seatstate + '","cmd":"5"}';
        websocket.send(str1);
    },
    onCallinCallouRing: function (callercalledinfo) {
        this.outms(callercalledinfo);
    },
    outms: function (str) {
        console.info("Icc->" + str);
    },
    alertms: function (str) {
        alert("Icc->" + str);
    },
    onMessage: function (evt) {
        cc = 0;
        //document.getElementById('result').value = evt.data;
        if (evt.data.length > 4) {

            var callercalledinfo = '';
            var json = JSON.parse(evt.data);
            var stateValue = new Array();
            stateValue["-1"] = "需要重新连接";
            stateValue["-2"] = "状态错误";
            stateValue["-3"] = "坐席工号错误";
            stateValue["-4"] = "登录密码错误";
            stateValue["-5"] = "未签入上班";
            stateValue["-6"] = "被叫号码非法";
            stateValue["-7"] = "呼叫没有空闲线路异常";
            stateValue["-8"] = "数据库错误";
            stateValue["-9"] = "其他未知错误";
            stateValue["-10"] = "坐席已在其他浏览器签入";
            stateValue["-11"] = "坐席分机号码在忙或者没注册";
            stateValue["-12"] = "呼叫用户号码失败";
            stateValue["-13"] = "被操作的坐席状态不对,不在服务状态";
            stateValue["-14"] = "坐席没分配队列不能签入上班";
            stateValue["-15"] = "被转接的坐席不是空闲,无法转接";

            /* 回调弹屏说明:
                 {"cmdsn":"123456789","seatno":"1004","caller":"13606060253","para":"05925961526","state":"0"}
                 para :被叫号码
                state=0表示 操作成功
                =1 用户拨入弹屏
                =2 外拨用户弹屏
                =3 用户回电,坐席在忙的弹屏
            
                state<0 表示错误：
                -1：socket 断开，需要重新连接
                -2：状态错误，比如发起一个呼叫未结束又发起一个呼叫
                -3：坐席工号错误
                -4：登录密码错误
                -5：未签入上班，无法进行其他操作
                -6: 被叫号码非法
                -7: 呼叫没有空闲线路异常
                -10: 坐席已在其他浏览器签入,不能从当前浏览器迁出
                -11: 坐席分机号码在忙或者没注册
                -12: 呼叫用户号码失败,可能是没注册上落地网关或者落地网关返回错误.
                -13:被操作的坐席状态不对,不在服务状态,导致班长控制操作失败.
                -14: 坐席没分配队列不能签入上班
                -15:被转接的坐席不是空闲,无法转接.
                -8：数据库错误
                -9：其他未知错误
                业务--websocket--YC中间件--esl--freeswitch===voip网关==E1==运营商IP话机  
                */
            /*2018-01-12 增加坐席状态*/
            var agstate = new Array();
            agstate["0"] = "离线"; agstate["1"] = "空闲"; agstate["3"] = "服务中"; agstate["4"] = "话后";
            agstate["5"] = "分机通话"; agstate["11"] = "小休"; agstate["12"] = "会议"; agstate["13"] = "培训";
            agstate["14"] = "洗手"; agstate["15"] = "就餐"; agstate["16"] = "话后";
            if (json.state == 0) //ok
            {
                if (json.cmdsn == "110") {
                    var temId = json.para.split(',')[0].split('=');

                    setStateLine(agstate[json.agstate], temId[1]);
                }
                else if (json.cmdsn == "104" && json.para == "1") {
                    setbusy("whfbusy");
                    this.outms('坐席=' + json.seatno + ',状态繁忙!');//<span style="color: green;">提示:</span>
                }
                else if (json.cmdsn == "104" && json.para == "0") {
                    serfree("whfbusy");
                    this.outms('坐席=' + json.seatno + ',状态空闲!');//<span style="color: green;">提示:</span> 
                }
                else if (json.cmdsn == "100") {
                    HideCheckIn("whfckIn");
                    s60LineUpStimer();
                    this.outms('坐席=' + json.seatno + ',签入成功!');//<span style="color: green;">提示:</span> 
                }
                else if (json.cmdsn == "101") {
                    ShowCheckIn("whfckIn");
                    s60LineUpEtimer();
                    wsk.DisConnect();
                    this.outms('坐席=' + json.seatno + ',签出成功!');//<span style="color: green;">提示:</span> 
                }
                else if (json.cmdsn == "113") {
                    switch (json.para) {
                        case "0"://恢复
                            keepRegainOper("whfhold1", "whfhold2");
                            break;
                        case "1"://1 保持
                            keeCallOper("whfhold1", "whfhold2");
                            break;
                        case "2"://2 静音
                            keepQuietOper("whfhold2", "whfhold1");
                            break;
                    }
                }
            }
            if (json.state < 0) //fail
            {
                this.alertms('出错了:' + stateValue[json.state]);//<span style="color: green;">提示:</span> 
            }

            if (json.state == 1) //user callin 
            {
                IccCallin(json.caller);
                this.outms('有新来电');
            }
            if (json.state == 2) //station callout
            {
                callercalledinfo = '<span style="color: red;">callout:</span>cmdsn=' + json.cmdsn + ', state=' + json.state + ',seatno=' + json.seatno + ',caller=' + json.caller + ',called=' + json.para;
                IccCallout(json.para);
                this.outms(callercalledinfo);
            }
            if (json.state == 3) {
                //通话开始(无论是呼入还是呼出)
                s60Stimer();
            }
            if (json.state == 4) {
                //通话结束(无论是呼入还是呼出)
                s60Etimer();
            }
        }
    },
    ThreeTalk: function (autherseatno) {
        //强插(强行介入客服与客户之间的交流)
        //方向:客户呼入
        //场景说明:当客户与8000在通话中时,8001分机 点击按钮 参数传8000 时就可以介入8000与客户之间的通话,以便给予更好的服务.
        //注意事项:1.必须是通话中,2每一个通话只能插入一个分机
        str1 = '{"cmdsn":"109","seatno":"' + wsk.txtseatno + '","caller":"","para":"' + autherseatno + '","cmd":"24"}';
        websocket.send(str1);
    },
    KillTalk: function (autherseatno) {
        //强拆(直接强行的挂断通话中的分机)
        //方向:客户呼入
        //场景说明:8000与客户正在通话中时,8001分机 点击按钮 参数传8000 时就可以直接挂断8000与客户的通话
        //注意事项:1.必须是通话中
        str1 = '{"cmdsn":"109","seatno":"' + wsk.txtseatno + '","caller":"","para":"' + autherseatno + '","cmd":"25"}';
        websocket.send(str1);
    },
    TransSeatno: function (autherseatno) {
        //转接坐席(把自己通话中的客户,转移给其他分机)
        //方向:客户呼入
        //场景说明:8000与客户正在通话中时,8000分机 点击按钮 参数传8001 时就可以把本次通话转移给8001
        //注意事项:1.必须是通话中,2.被转移的分机也必须是签入状态
        str1 = '{"cmdsn":"109","seatno":"' + wsk.txtseatno + '","caller":"","para":"' + autherseatno + '","cmd":"12"}';
        websocket.send(str1);
    },
    HoldUser: function (param) {
        //保持,静音,恢复(0:取消保持/静音 1:保持通话 2:静音 )
        //方向:客户呼入
        //场景说明:8000与客户正在通话中时,8000分机 点击按钮 参数传(0/1/2) 时就可以进行(恢复/保持/静音)操作
        //注意事项:1.必须是通话中
        str1 = '{"cmdsn":"113","seatno":"' + wsk.txtseatno + '","caller":"","para":"' + param + '","cmd":"13"}';
        websocket.send(str1);
    },
    GetUserqLines: function () {
        //获取该分机等待排队的号码的数组
    },
    GetGWLines: function () {
        //获取该分机外线状态

    },
    MonitorTalk: function (autherseatno) {
        //监听(就是监听其他客服和客户的通话)
        //方向：客户呼入
        //场景说明:8000与客户正在通话中时,8001分机 点击按钮 参数传8000 时就可以听到8000与客户之间的交流
        //注意事项:1.8000必须是通话中
        str1 = '{"cmdsn":"109","seatno":"' + wsk.txtseatno + '","caller":"","para":"' + autherseatno + '","cmd":"23"}';
        websocket.send(str1);
    },
    GetGroupLineUp: function () {
        //获取用户排队数量以及自己的当前状态
        str1 = '{"cmdsn":"110","seatno":"' + wsk.txtseatno + '","caller":"","para":"","cmd":"28"}';
        websocket.send(str1);
    }
};
window.onbeforeunload = function () {
    wsk.DisConnect();
}
