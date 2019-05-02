var Iccsettimer;//计时器
var IcclineUpargstate;//排队和状态
var IcctimeIndex = 0;
//加载完成执行
window.onload = function () {
    var $regularlyItems = document.getElementsByClassName("first viewport-flip");
    IccPhone.each($regularlyItems, function () {
        var _$this = this;
        _$this.childNodes[0].className = "flip-box flip out";
        setTimeout(function () {
            _$this.childNodes[1].className = "flip-box flip in";
            _$this.childNodes[0].style.display = "none";
        }, 225);
    });
    hover($regularlyItems[0], function () {
        var _$this = this;
        _$this.childNodes[1].className = "flip-box flip out";
        setTimeout(function () {
            _$this.childNodes[0].className = "flip-box flip in";
            _$this.childNodes[0].style.display = "";
            _$this.childNodes[1].style.display = "none";
        }, 240);
    }, function () {
        var _$this = this;
        _$this.childNodes[0].className = "flip-box flip out";
        setTimeout(function () {
            _$this.childNodes[1].className = "flip-box flip in";
            _$this.childNodes[1].style.display = "";
            _$this.childNodes[0].style.display = "none";
        }, 240);
    });
}

//签入操作
function checkIn() {
    //先做链接然后再做签入
    //先赋值
    wsk.txtseatno = IccPhone.options.seatNo;
    wsk.txtstation = IccPhone.options.seatExt;
    wsk.txtpassowrd = IccPhone.options.seatPassWord;
    if (!wsk.isConnect) {
        wsk.init(IccPhone.options.iccSerVicePath);
    }
    else {
        wsk.checkIn();
    }
}

//签出
function checkOut() {
    //签出操作
    wsk.checkOut();
}

//拨号
function callTel() {
    var txtObj = document.getElementById("Icctxttel");
    if (txtObj != undefined) {
        var tel = txtObj.value;
        if (/^1[3|4|5|7|8]\d{9}$/i.test(tel)) {
            wsk.makeCall(tel, '');
        }
        else {
            alert("请在文本框中输入正确的手机号码");
            txtObj.focus();
        }
    }
    else {
        alert("请在初始化【items】,加上['txtTel']");
    }
}

//挂机操作
function dropTel() {
    wsk.dropCall();
}

//最早版本三方通话
function threeCall() {
    var txtObj = document.getElementById("Icctxttel");
    if (txtObj != undefined) {
        var tel = txtObj.value;
        if (tel != "") {
            wsk.Callauther(tel);
        }
        else {
            alert("请在文本框中输入第三方的号码");
            txtObj.focus();
        }
    }
    else {
        alert("请在初始化【items】,加上['txtTel']");
    }
}

//转评价
function dialBack() {
    wsk.Callback(1);
}

//强插
function threeTalk() {
    var txtObj = document.getElementById("Icctxttel");
    if (txtObj != undefined) {
        var tel = txtObj.value;
        if (tel != "") {
            wsk.ThreeTalk(tel);
        }
        else {
            alert("请在文本框中输入要强插的分机号");
            txtObj.focus();
        }
    }
    else {
        alert("请在初始化【items】,加上['txtTel']");
    }
}

//强拆
function killTalk() {
    var txtObj = document.getElementById("Icctxttel");
    if (txtObj != undefined) {
        var tel = txtObj.value;
        if (tel != "") {
            wsk.KillTalk(tel);
        }
        else {
            alert("请在文本框中输入要强拆的分机号");
            txtObj.focus();
        }
    }
    else {
        alert("请在初始化【items】,加上['txtTel']");
    }
}

//转接
function transSeatno() {
    var txtObj = document.getElementById("Icctxttel");
    if (txtObj != undefined) {
        var tel = txtObj.value;
        if (tel != "") {
            wsk.TransSeatno(tel);
        }
        else {
            alert("请在文本框中输入要转接的号码");
            txtObj.focus();
        }
    }
    else {
        alert("请在初始化【items】,加上['txtTel']");
    }
}

//空闲->繁忙/繁忙->空闲
function freeCall(seatstate) {
    wsk.SetSeatState(seatstate);
}
//空闲->繁忙
function setbusy(_Id) {
    var Obj = document.getElementById(_Id);
    Obj.setAttribute("onclick", "freeCall(0)");
    Obj.childNodes[0].childNodes[0].innerText = IccPhone.IccText["busyCall"];
}
//繁忙->空闲
function serfree(_Id) {
    var Obj = document.getElementById(_Id);
    Obj.setAttribute("onclick", "freeCall(1)");
    Obj.childNodes[0].childNodes[0].innerText = IccPhone.IccText["freeCall"];
}

//保持功能
function keepCall(_param) {
    wsk.HoldUser(_param);
}
//恢复功能
function keepRegain(_param) {
    wsk.HoldUser(_param);
}

//静音功能
function keepQuiet(_param) {
    wsk.HoldUser(_param);
}

//保持后对应操作
function keeCallOper(_Fid, _Sid) {
    //保持后静音不可以用,自己隐藏恢复出现
    var _FObj = document.getElementById(_Fid);
    var _SObj = document.getElementById(_Sid);
    _FObj.style.display = "none";
    if (_SObj != undefined) {
        //当主键存在静音功能时
        _SObj.setAttribute("onclick", "");
        addClass(_SObj, "Icc-l-btn-disabled");
    }
    document.getElementById("whfhold01").style.display = "";
}

//保持静音后恢复
function keepRegainOper(_Fid, _Sid) {
    var _FObj = document.getElementById(_Fid);
    var _SObj = document.getElementById(_Sid);
    if (_FObj.style.display == "none") {
        //如果保持是隐藏的
        _FObj.style.display = "";
        if (_SObj != undefined) {
            //当主键存在静音功能时
            _SObj.setAttribute("onclick", IccPhone.IccOnclick[_SObj.name]);
            removeClass(_SObj, "Icc-l-btn-disabled");
        }
        document.getElementById("whfhold01").style.display = "none";
    }
    else {
        _SObj.style.display = "";
        if (_FObj != undefined) {
            _FObj.setAttribute("onclick", IccPhone.IccOnclick[_FObj.name]);
            removeClass(_FObj, "Icc-l-btn-disabled");
        }
        document.getElementById("whfhold02").style.display = "none";
    }
}

//静音后对应操作
function keepQuietOper(_Fid, _Sid) {
    //保持后静音不可以用,自己隐藏恢复出现
    var _FObj = document.getElementById(_Fid);
    var _SObj = document.getElementById(_Sid);
    _FObj.style.display = "none";
    if (_SObj != undefined) {
        //当主键存在静音功能时
        _SObj.setAttribute("onclick", "");
        addClass(_SObj, "Icc-l-btn-disabled");
    }
    document.getElementById("whfhold02").style.display = "";
}

/*2018-01-11 增加功能,操作按钮方法*/
//隐藏签入显示其余按钮
function HideCheckIn(_Id) {
    var selObj = document.getElementById(_Id);
    selObj.setAttribute("onclick", "");
    selObj.className = "whflink Icc-l-btn Icc-l-btn-disabled";
    //addClass(selObj, "Icc-l-btn-disabled");
    var CallNameArr = document.getElementsByClassName("whflink");
    for (var i = 0, j = CallNameArr.length; i < j; i++) {
        if (CallNameArr[i].id != _Id) {
            CallNameArr[i].setAttribute("onclick", IccPhone.IccOnclick[CallNameArr[i].name]);
            CallNameArr[i].className = "whflink Icc-l-btn";
            //removeClass(CallNameArr[i], "Icc-l-btn-disabled")
        }
    }
}
//显示签入隐藏其余按钮
function ShowCheckIn(_Id) {
    var selObj = document.getElementById(_Id);
    selObj.setAttribute("onclick", IccPhone.IccOnclick[selObj.name]);
    selObj.className = "whflink Icc-l-btn";
    //removeClass(selObj, "Icc-l-btn-disabled");
    var CallNameArr = document.getElementsByClassName("whflink");
    for (var i = 0, j = CallNameArr.length; i < j; i++) {
        if (CallNameArr[i].id != _Id) {
            CallNameArr[i].setAttribute("onclick", "");
            CallNameArr[i].className = "whflink Icc-l-btn Icc-l-btn-disabled";
            //addClass(CallNameArr[i], "Icc-l-btn-disabled")
        }
    }
}

/**********************************/

function IccCallin(tel) {
    if (IccPhone.options.isCallinAlert) {
        //配置需要呼入弹屏就弹
        var FnStr = IccPhone.options.CallinFn;
        if (IccPhone.isFunction(FnStr)) {
            FnStr(tel);
        }
        else {
            alert("呼入弹屏-请配置[CallinFn]");
        }
    }
}

function IccCallout(tel) {
    if (IccPhone.options.isCalloutAlert) {
        //配置需要呼入弹屏就弹
        var FnStr = IccPhone.options.CalloutFn;
        if (IccPhone.isFunction(FnStr)) {
            FnStr(tel);
        }
        else {
            alert("呼出弹屏-请配置[CalloutFn]");
        }
    }
}

/*******JS 仿效Jq封装方式**********/
function addClass(obj, cls) {
    var obj_class = obj.className,//获取 class 内容.
    blank = (obj_class != '') ? ' ' : '';//判断获取到的 class 是否为空, 如果不为空在前面加个'空格'.
    added = obj_class + blank + cls;//组合原来的 class 和需要添加的 class.
    obj.className = added;//替换原来的 class.
}

function removeClass(obj, cls) {
    var obj_class = ' ' + obj.className + ' ';//获取 class 内容, 并在首尾各加一个空格. ex) 'abc    bcd' -> ' abc    bcd '
    obj_class = obj_class.replace(/(\s+)/gi, ' '),//将多余的空字符替换成一个空格. ex) ' abc    bcd ' -> ' abc bcd '
    removed = obj_class.replace(' ' + cls + ' ', ' ');//在原来的 class 替换掉首尾加了空格的 class. ex) ' abc bcd ' -> 'bcd '
    removed = removed.replace(/(^\s+)|(\s+$)/g, '');//去掉首尾空格. ex) 'bcd ' -> 'bcd'
    obj.className = removed;//替换原来的 class.
}

function hasClass(obj, cls) {
    var obj_class = obj.className,//获取 class 内容.
    obj_class_lst = obj_class.split(/\s+/);//通过split空字符将cls转换成数组.
    x = 0;
    for (x in obj_class_lst) {
        if (obj_class_lst[x] == cls) {//循环数组, 判断是否包含cls
            return true;
        }
    }
    return false;
}

function hover(ele, over, out) {
    ele.addEventListener('mouseenter', over, false);
    ele.addEventListener('mouseleave', out, false);
}
/*******JS 仿效Jq封装方式**********/

//开始计时
function s60Stimer() {
    IcctimeIndex = 4;
    Iccsettimer = setInterval(function () {
        IccsetTime();
    }, 1000);
}

//结束计时
function s60Etimer() {
    clearInterval(Iccsettimer);
}

//计时器方法
function IccsetTime() {
    var hour = parseInt(IcctimeIndex / 3600);    // 计算时 
    var minutes = parseInt((IcctimeIndex % 3600) / 60);    // 计算分 
    var seconds = parseInt(IcctimeIndex % 60);    // 计算秒  
    hour = hour < 10 ? "0" + hour : hour;
    minutes = minutes < 10 ? "0" + minutes : minutes;
    seconds = seconds < 10 ? "0" + seconds : seconds;
    document.getElementById("IcctimerSpan").innerHTML = (hour + ":" + minutes + ":" + seconds);
    IcctimeIndex++;
}

function s60LineUpStimer() {
    IcclineUpargstate = setInterval(function () {
        wsk.GetGroupLineUp();
    }, 2000);
}

function s60LineUpEtimer() {
    document.getElementById("Iccstate").innerHTML = "离线";
    clearInterval(IcclineUpargstate);
}

function setStateLine(state, lineup) {
    document.getElementById("Iccstate").innerHTML = state;
    document.getElementById("IcclineUp").innerHTML = lineup;
}