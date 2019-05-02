/*******************************************************************************
* MaiXinIcc - Icc for Web
* 
* @author whf<whf@toowell.cn>
* @version 1.1.10 (2017-12-09)
*******************************************************************************/

(function (window, undefined) {
    //工号,分机,分机密码,icc服务地址
    var seatNo = '8008', seatExt = '8008', seatPassWord = '123456', iccSerVicePath = 'ws://icctest.maixintech.com:5061';

    var _VERSION = '1.1.10 (2017-12-09)',/*版本号*/
        _QUIRKS = document.compatMode != 'CSS1Compat',
        _TIME = new Date().getTime();/*时间*/
    function _isArray(val) {
        if (!val) {
            return false;
        }
        return Object.prototype.toString.call(val) === '[object Array]';
    }
    function _isFunction(val) {
        if (!val) {
            return false;
        }
        return Object.prototype.toString.call(val) === '[object Function]';
    }
    function _inArray(val, arr) {
        for (var i = 0, len = arr.length; i < len; i++) {
            if (val === arr[i]) {
                return i;
            }
        }
        return -1;
    }
    function _each(obj, fn) {
        if (_isArray(obj)) {
            for (var i = 0, len = obj.length; i < len; i++) {
                if (fn.call(obj[i], i, obj[i]) === false) {
                    break;
                }
            }
        } else {
            for (var key in obj) {
                if (obj.hasOwnProperty(key)) {
                    if (fn.call(obj[key], key, obj[key]) === false) {
                        break;
                    }
                }
            }
        }
    }
    function _trim(str) {
        return str.replace(/(?:^[ \t\n\r]+)|(?:[ \t\n\r]+$)/g, '');
    }
    function _inString(val, str, delimiter) {
        delimiter = delimiter === undefined ? ',' : delimiter;
        return (delimiter + str + delimiter).indexOf(delimiter + val + delimiter) >= 0;
    }
    function _addUnit(val, unit) {
        unit = unit || 'px';
        return val && /^\d+$/.test(val) ? val + unit : val;
    }
    function _removeUnit(val) {
        var match;
        return val && (match = /(\d+)/.exec(val)) ? parseInt(match[1], 10) : 0;
    }
    function _escape(val) {
        return val.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
    }
    function _unescape(val) {
        return val.replace(/&lt;/g, '<').replace(/&gt;/g, '>').replace(/&quot;/g, '"').replace(/&amp;/g, '&');
    }
    function _toCamel(str) {
        var arr = str.split('-');
        str = '';
        _each(arr, function (key, val) {
            str += (key > 0) ? val.charAt(0).toUpperCase() + val.substr(1) : val;
        });
        return str;
    }
    function _toHex(val) {
        function hex(d) {
            var s = parseInt(d, 10).toString(16).toUpperCase();
            return s.length > 1 ? s : '0' + s;
        }
        return val.replace(/rgb\s*\(\s*(\d+)\s*,\s*(\d+)\s*,\s*(\d+)\s*\)/ig,
            function ($0, $1, $2, $3) {
                return '#' + hex($1) + hex($2) + hex($3);
            }
        );
    }
    function _toMap(val, delimiter) {
        delimiter = delimiter === undefined ? ',' : delimiter;
        var map = {}, arr = _isArray(val) ? val : val.split(delimiter), match;
        _each(arr, function (key, val) {
            if ((match = /^(\d+)\.\.(\d+)$/.exec(val))) {
                for (var i = parseInt(match[1], 10) ; i <= parseInt(match[2], 10) ; i++) {
                    map[i.toString()] = true;
                }
            } else {
                map[val] = true;
            }
        });
        return map;
    }
    function _toArray(obj, offset) {
        return Array.prototype.slice.call(obj, offset || 0);
    }
    function _undef(val, defaultVal) {
        return val === undefined ? defaultVal : val;
    }
    function _invalidUrl(url) {
        return !url || /[<>"]/.test(url);
    }
    function _addParam(url, param) {
        return url.indexOf('?') >= 0 ? url + '&' + param : url + '?' + param;
    }
    function _extend(child, parent, proto) {
        if (!proto) {
            proto = parent;
            parent = null;
        }
        var childProto;
        if (parent) {
            var fn = function () { };
            fn.prototype = parent.prototype;
            childProto = new fn();
            _each(proto, function (key, val) {
                childProto[key] = val;
            });
        } else {
            childProto = proto;
        }
        childProto.constructor = child;
        child.prototype = childProto;
        child.parent = parent ? parent.prototype : null;
    }
    function _json(text) {
        var match;
        if ((match = /\{[\s\S]*\}|\[[\s\S]*\]/.exec(text))) {
            text = match[0];
        }
        var cx = /[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g;
        cx.lastIndex = 0;
        if (cx.test(text)) {
            text = text.replace(cx, function (a) {
                return '\\u' + ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
            });
        }
        if (/^[\],:{}\s]*$/.
        test(text.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, '@').
        replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, ']').
        replace(/(?:^|:|,)(?:\s*\[)+/g, ''))) {
            return eval('(' + text + ')');
        }
        throw 'JSON parse error';
    }
    var _round = Math.round;
    var P = {
        DEBUG: false,
        VERSION: _VERSION,
        each: _each,
        isArray: _isArray,
        isFunction: _isFunction,
        inArray: _inArray,
        inString: _inString,
        trim: _trim,
        addUnit: _addUnit,
        removeUnit: _removeUnit,
        escape: _escape,
        unescape: _unescape,
        toCamel: _toCamel,
        toHex: _toHex,
        toMap: _toMap,
        toArray: _toArray,
        undef: _undef,
        invalidUrl: _invalidUrl,
        addParam: _addParam,
        extend: _extend,
        json: _json
    };
    function _getBasePath() {
        var els = document.getElementsByTagName('script'), src;
        for (var i = 0, len = els.length; i < len; i++) {
            src = els[i].src || '';
            if (/Tel[\w\-\.]*\.js/.test(src)) {
                return src.substring(0, src.lastIndexOf('/') + 1);
            }
        }
        return '';
    }
    P.basePath = _getBasePath();


    //属性
    var _ITEMS = ('checkIn,checkOut,txtTel,callTel,dropTel,threeCall,dialBack,threeTalk,killTalk,transSeatno,freeCall,keepCall,keepQuiet').split(',');
    P.options = {
        seatNo: seatNo,
        seatExt: seatExt,
        seatPassWord: seatPassWord,
        iccSerVicePath: iccSerVicePath,
        isCallinAlert: true,
        CallinFn: "",
        //CallinUrl: "",
        isCalloutAlert: true,
        CalloutFn: "",
        //CalloutUrl: "",
        langPath: P.basePath + 'lang/',//语言地址
        themesPath: P.basePath + 'Base/',//样式地址
        langType: 'zh_CN',
        zIndex: 9999,
        items: [
            'checkIn', 'checkOut', 'txtTel', 'callTel', 'dropTel', 'threeCall', 'dialBack',
            'threeTalk', 'killTalk', 'transSeatno', 'freeCall', 'keepCall', 'keepQuiet'
        ],
    };
    //加载初始
    var _useCapture = false;
    function _bindEvent(el, type, fn) {
        if (el.addEventListener) {
            el.addEventListener(type, fn, _useCapture);
        } else if (el.attachEvent) {
            el.attachEvent('on' + type, fn);
        }
    }
    var _EVENT_PROPS = ('screenX,screenY,shiftKey,srcElement,target,toElement,view,wheelDelta,which').split(',');
    function KEvent(el, event) {
        this.init(el, event);
    }
    _extend(KEvent, {
        init: function (el, event) {
            var self = this, doc = el.ownerDocument || el.document || el;
            self.event = event;
            _each(_EVENT_PROPS, function (key, val) {
                self[val] = event[val];
            });
            if (!self.target) {
                self.target = self.srcElement || doc;
            }
            if (self.target.nodeType === 3) {
                self.target = self.target.parentNode;
            }
            if (!self.relatedTarget && self.fromElement) {
                self.relatedTarget = self.fromElement === self.target ? self.toElement : self.fromElement;
            }
            if (self.pageX == null && self.clientX != null) {
                var d = doc.documentElement, body = doc.body;
                self.pageX = self.clientX + (d && d.scrollLeft || body && body.scrollLeft || 0) - (d && d.clientLeft || body && body.clientLeft || 0);
                self.pageY = self.clientY + (d && d.scrollTop || body && body.scrollTop || 0) - (d && d.clientTop || body && body.clientTop || 0);
            }
            if (!self.which && ((self.charCode || self.charCode === 0) ? self.charCode : self.keyCode)) {
                self.which = self.charCode || self.keyCode;
            }
            if (!self.metaKey && self.ctrlKey) {
                self.metaKey = self.ctrlKey;
            }
            if (!self.which && self.button !== undefined) {
                self.which = (self.button & 1 ? 1 : (self.button & 2 ? 3 : (self.button & 4 ? 2 : 0)));
            }
            switch (self.which) {
                case 186:
                    self.which = 59;
                    break;
                case 187:
                case 107:
                case 43:
                    self.which = 61;
                    break;
                case 189:
                case 45:
                    self.which = 109;
                    break;
                case 42:
                    self.which = 106;
                    break;
                case 47:
                    self.which = 111;
                    break;
                case 78:
                    self.which = 110;
                    break;
            }
            if (self.which >= 96 && self.which <= 105) {
                self.which -= 48;
            }
        }
    });
    var _eventExpendo = 'IccTel_' + _TIME, _eventId = 0, _eventData = {};
    function _getId(el) {
        return el[_eventExpendo] || null;
    }
    function _setId(el) {
        el[_eventExpendo] = ++_eventId;
        return _eventId;
    }
    function _bind(el, type, fn) {
        if (type.indexOf(',') >= 0) {
            _each(type.split(','), function () {
                _bind(el, this, fn);
            });
            return;
        }
        var id = _getId(el);
        if (!id) {
            id = _setId(el);
        }
        if (_eventData[id] === undefined) {
            _eventData[id] = {};
        }
        var events = _eventData[id][type];
        if (events && events.length > 0) {
            _unbindEvent(el, type, events[0]);
        } else {
            _eventData[id][type] = [];
            _eventData[id].el = el;
        }
        events = _eventData[id][type];
        if (events.length === 0) {
            events[0] = function (e) {
                var kevent = e ? new KEvent(el, e) : undefined;
                _each(events, function (i, event) {
                    if (i > 0 && event) {
                        event.call(el, kevent);
                    }
                });
            };
        }
        if (_inArray(fn, events) < 0) {
            events.push(fn);
        }
        _bindEvent(el, type, events[0]);
    }
    var _readyFinished = false;
    function _ready(fn) {
        if (_readyFinished) {
            fn(P);
            return;
        }
        var loaded = false;
        function readyFunc() {
            if (!loaded) {
                loaded = true;
                fn(P);
                _readyFinished = true;
            }
        }
        function ieReadyFunc() {
            if (!loaded) {
                try {
                    document.documentElement.doScroll('left');
                } catch (e) {
                    setTimeout(ieReadyFunc, 100);
                    return;
                }
                readyFunc();
            }
        }
        function ieReadyStateFunc() {
            if (document.readyState === 'complete') {
                readyFunc();
            }
        }
        if (document.addEventListener) {
            _bind(document, 'DOMContentLoaded', readyFunc);
        } else if (document.attachEvent) {
            _bind(document, 'readystatechange', ieReadyStateFunc);
            var toplevel = false;
            try {
                toplevel = window.frameElement == null;
            } catch (e) { }
            if (document.documentElement.doScroll && toplevel) {
                ieReadyFunc();
            }
        }
        _bind(window, 'load', readyFunc);
    }
    P.ready = _ready;

    function _formatUrl(url, mode, host, pathname) {
        mode = _undef(mode, '').toLowerCase();
        if (url.substr(0, 5) != 'data:') {
            url = url.replace(/([^:])\/\//g, '$1/');
        }
        if (_inArray(mode, ['absolute', 'relative', 'domain']) < 0) {
            return url;
        }
        host = host || location.protocol + '//' + location.host;
        if (pathname === undefined) {
            var m = location.pathname.match(/^(\/.*)\//);
            pathname = m ? m[1] : '';
        }
        var match;
        if ((match = /^(\w+:\/\/[^\/]*)/.exec(url))) {
            if (match[1] !== host) {
                return url;
            }
        } else if (/^\w+:/.test(url)) {
            return url;
        }
        function getRealPath(path) {
            var parts = path.split('/'), paths = [];
            for (var i = 0, len = parts.length; i < len; i++) {
                var part = parts[i];
                if (part == '..') {
                    if (paths.length > 0) {
                        paths.pop();
                    }
                } else if (part !== '' && part != '.') {
                    paths.push(part);
                }
            }
            return '/' + paths.join('/');
        }
        if (/^\//.test(url)) {
            url = host + getRealPath(url.substr(1));
        } else if (!/^\w+:\/\//.test(url)) {
            url = host + getRealPath(pathname + '/' + url);
        }
        function getRelativePath(path, depth) {
            if (url.substr(0, path.length) === path) {
                var arr = [];
                for (var i = 0; i < depth; i++) {
                    arr.push('..');
                }
                var prefix = '.';
                if (arr.length > 0) {
                    prefix += '/' + arr.join('/');
                }
                if (pathname == '/') {
                    prefix += '/';
                }
                return prefix + url.substr(path.length);
            } else {
                if ((match = /^(.*)\//.exec(path))) {
                    return getRelativePath(match[1], ++depth);
                }
            }
        }
        if (mode === 'relative') {
            url = getRelativePath(host + pathname, 0).substr(2);
        } else if (mode === 'absolute') {
            if (url.substr(0, host.length) === host) {
                url = url.substr(host.length);
            }
        }
        return url;
    }
    P.formatUrl = _formatUrl;

    function _contains(nodeA, nodeB) {
        if (nodeA.nodeType == 9 && nodeB.nodeType != 9) {
            return true;
        }
        while ((nodeB = nodeB.parentNode)) {
            if (nodeB == nodeA) {
                return true;
            }
        }
        return false;
    }
    var _getSetAttrDiv = document.createElement('div');
    _getSetAttrDiv.setAttribute('className', 't');
    var _GET_SET_ATTRIBUTE = _getSetAttrDiv.className !== 't';
    function _getAttr(el, key) {
        key = key.toLowerCase();
        var val = null;
        if (!_GET_SET_ATTRIBUTE && el.nodeName.toLowerCase() != 'script') {
            var div = el.ownerDocument.createElement('div');
            div.appendChild(el.cloneNode(false));
            var list = _getAttrList(_unescape(div.innerHTML));
            if (key in list) {
                val = list[key];
            }
        } else {
            try {
                val = el.getAttribute(key, 2);
            } catch (e) {
                val = el.getAttribute(key, 1);
            }
        }
        if (key === 'style' && val !== null) {
            val = _formatCss(val);
        }
        return val;
    }
    function _queryAll(expr, root) {
        var exprList = expr.split(',');
        if (exprList.length > 1) {
            var mergedResults = [];
            _each(exprList, function () {
                _each(_queryAll(this, root), function () {
                    if (_inArray(this, mergedResults) < 0) {
                        mergedResults.push(this);
                    }
                });
            });
            return mergedResults;
        }
        root = root || document;
        function escape(str) {
            if (typeof str != 'string') {
                return str;
            }
            return str.replace(/([^\w\-])/g, '\\$1');
        }
        function stripslashes(str) {
            return str.replace(/\\/g, '');
        }
        function cmpTag(tagA, tagB) {
            return tagA === '*' || tagA.toLowerCase() === escape(tagB.toLowerCase());
        }
        function byId(id, tag, root) {
            var arr = [],
                doc = root.ownerDocument || root,
                el = doc.getElementById(stripslashes(id));
            if (el) {
                if (cmpTag(tag, el.nodeName) && _contains(root, el)) {
                    arr.push(el);
                }
            }
            return arr;
        }
        function byClass(className, tag, root) {
            var doc = root.ownerDocument || root, arr = [], els, i, len, el;
            if (root.getElementsByClassName) {
                els = root.getElementsByClassName(stripslashes(className));
                for (i = 0, len = els.length; i < len; i++) {
                    el = els[i];
                    if (cmpTag(tag, el.nodeName)) {
                        arr.push(el);
                    }
                }
            } else if (doc.querySelectorAll) {
                els = doc.querySelectorAll((root.nodeName !== '#document' ? root.nodeName + ' ' : '') + tag + '.' + className);
                for (i = 0, len = els.length; i < len; i++) {
                    el = els[i];
                    if (_contains(root, el)) {
                        arr.push(el);
                    }
                }
            } else {
                els = root.getElementsByTagName(tag);
                className = ' ' + className + ' ';
                for (i = 0, len = els.length; i < len; i++) {
                    el = els[i];
                    if (el.nodeType == 1) {
                        var cls = el.className;
                        if (cls && (' ' + cls + ' ').indexOf(className) > -1) {
                            arr.push(el);
                        }
                    }
                }
            }
            return arr;
        }
        function byName(name, tag, root) {
            var arr = [], doc = root.ownerDocument || root,
                els = doc.getElementsByName(stripslashes(name)), el;
            for (var i = 0, len = els.length; i < len; i++) {
                el = els[i];
                if (cmpTag(tag, el.nodeName) && _contains(root, el)) {
                    if (el.getAttribute('name') !== null) {
                        arr.push(el);
                    }
                }
            }
            return arr;
        }
        function byAttr(key, val, tag, root) {
            var arr = [], els = root.getElementsByTagName(tag), el;
            for (var i = 0, len = els.length; i < len; i++) {
                el = els[i];
                if (el.nodeType == 1) {
                    if (val === null) {
                        if (_getAttr(el, key) !== null) {
                            arr.push(el);
                        }
                    } else {
                        if (val === escape(_getAttr(el, key))) {
                            arr.push(el);
                        }
                    }
                }
            }
            return arr;
        }
        function select(expr, root) {
            var arr = [], matches;
            matches = /^((?:\\.|[^.#\s\[<>])+)/.exec(expr);
            var tag = matches ? matches[1] : '*';
            if ((matches = /#((?:[\w\-]|\\.)+)$/.exec(expr))) {
                arr = byId(matches[1], tag, root);
            } else if ((matches = /\.((?:[\w\-]|\\.)+)$/.exec(expr))) {
                arr = byClass(matches[1], tag, root);
            } else if ((matches = /\[((?:[\w\-]|\\.)+)\]/.exec(expr))) {
                arr = byAttr(matches[1].toLowerCase(), null, tag, root);
            } else if ((matches = /\[((?:[\w\-]|\\.)+)\s*=\s*['"]?((?:\\.|[^'"]+)+)['"]?\]/.exec(expr))) {
                var key = matches[1].toLowerCase(), val = matches[2];
                if (key === 'id') {
                    arr = byId(val, tag, root);
                } else if (key === 'class') {
                    arr = byClass(val, tag, root);
                } else if (key === 'name') {
                    arr = byName(val, tag, root);
                } else {
                    arr = byAttr(key, val, tag, root);
                }
            } else {
                var els = root.getElementsByTagName(tag), el;
                for (var i = 0, len = els.length; i < len; i++) {
                    el = els[i];
                    if (el.nodeType == 1) {
                        arr.push(el);
                    }
                }
            }
            return arr;
        }
        var parts = [], arr, re = /((?:\\.|[^\s>])+|[\s>])/g;
        while ((arr = re.exec(expr))) {
            if (arr[1] !== ' ') {
                parts.push(arr[1]);
            }
        }
        var results = [];
        if (parts.length == 1) {
            return select(parts[0], root);
        }
        var isChild = false, part, els, subResults, val, v, i, j, k, length, len, l;
        for (i = 0, lenth = parts.length; i < lenth; i++) {
            part = parts[i];
            if (part === '>') {
                isChild = true;
                continue;
            }
            if (i > 0) {
                els = [];
                for (j = 0, len = results.length; j < len; j++) {
                    val = results[j];
                    subResults = select(part, val);
                    for (k = 0, l = subResults.length; k < l; k++) {
                        v = subResults[k];
                        if (isChild) {
                            if (val === v.parentNode) {
                                els.push(v);
                            }
                        } else {
                            els.push(v);
                        }
                    }
                }
                results = els;
            } else {
                results = select(part, root);
            }
            if (results.length === 0) {
                return [];
            }
        }
        return results;
    }
    function _query(expr, root) {
        var arr = _queryAll(expr, root);
        return arr.length > 0 ? arr[0] : null;
    }
    P.query = _query;
    P.queryAll = _queryAll;

    function _get(val) {
        return P(val)[0];
    }
    function _getDoc(node) {
        if (!node) {
            return document;
        }
        return node.ownerDocument || node.document || node;
    }
    function _getWin(node) {
        if (!node) {
            return window;
        }
        var doc = _getDoc(node);
        return doc.parentWindow || doc.defaultView;
    }

    function _getNodeName(node) {
        if (!node || !node.nodeName) {
            return '';
        }
        return node.nodeName.toLowerCase();
    }

    function KNode(node) {
        this.init(node);
    }
    _extend(KNode, {
        init: function (node) {
            var self = this;
            node = _isArray(node) ? node : [node];
            var length = 0;
            for (var i = 0, len = node.length; i < len; i++) {
                if (node[i]) {
                    self[i] = node[i].constructor === KNode ? node[i][0] : node[i];
                    length++;
                }
            }
            self.length = length;
            self.doc = _getDoc(self[0]);
            self.name = _getNodeName(self[0]);
            self.type = self.length > 0 ? self[0].nodeType : null;
            self.win = _getWin(self[0]);
        }
    })
    var _P = P;
    P = function (expr, root) {
        if (expr === undefined || expr === null) {
            return;
        }
        function newNode(node) {
            if (!node[0]) {
                node = [];
            }
            return new KNode(node);
        }
        if (typeof expr === 'string') {
            if (root) {
                root = _get(root);
            }
            var length = expr.length;
            if (expr.charAt(0) === '@') {
                expr = expr.substr(1);
            }
            if (expr.length !== length || /<.+>/.test(expr)) {
                var doc = root ? root.ownerDocument || root : document,
                    div = doc.createElement('div'), list = [];
                div.innerHTML = '<img id="__icctel_temp_tag__" width="0" height="0" style="display:none;" />' + expr;
                for (var i = 0, len = div.childNodes.length; i < len; i++) {
                    var child = div.childNodes[i];
                    if (child.id == '__icctel_temp_tag__') {
                        continue;
                    }
                    list.push(child);
                }
                return newNode(list);
            }
            return newNode(_queryAll(expr, root));
        }
        if (expr && expr.constructor === KNode) {
            return expr;
        }
        if (expr.toArray) {
            expr = expr.toArray();
        }
        if (_isArray(expr)) {
            return newNode(expr);
        }
        return newNode(_toArray(arguments));
    };
    _each(_P, function (key, val) {
        P[key] = val;
    });
    //为加载语言换一个方法
    window.IccPhone = P;

    //加载js
    function _loadScript(url, fn) {
        var head = document.getElementsByTagName('head')[0] || (_QUIRKS ? document.body : document.documentElement),
            script = document.createElement('script');
        head.appendChild(script);
        script.src = url;
        script.charset = 'utf-8';
        script.type = "text/javascript";
    }
    function _chopQuery(url) {
        var index = url.indexOf('?');
        return index > 0 ? url.substr(0, index) : url;
    }
    //加载样式
    function _loadStyle(url) {
        var head = document.getElementsByTagName('head')[0] || (_QUIRKS ? document.body : document.documentElement),
            link = document.createElement('link'),
            absoluteUrl = _chopQuery(_formatUrl(url, 'absolute'));
        var links = P('link[rel="stylesheet"]', head);
        for (var i = 0, len = links.length; i < len; i++) {
            if (_chopQuery(_formatUrl(links[i].href, 'absolute')) === absoluteUrl) {
                return;
            }
        }
        head.appendChild(link);
        link.href = url;
        link.rel = 'stylesheet';
    }
    P.loadScript = _loadScript;
    P.loadStyle = _loadStyle;

    //组件读取数组
    var _plugins = {};
    function _plugin(name, fn) {
        if (name === undefined) {
            return _plugins;
        }
        if (!fn) {
            return _plugins[name];
        }
        _plugins[name] = fn;
    }
    //语言方法
    var _language = {};
    function _parseLangKey(key) {
        var match, ns = 'core';
        if ((match = /^(\w+)\.(\w+)$/.exec(key))) {
            ns = match[1];
            key = match[2];
        }
        return { ns: ns, key: key };
    }
    function _lang(mixed, langType) {
        langType = langType === undefined ? K.options.langType : langType;
        if (typeof mixed === 'string') {
            if (!_language[langType]) {
                return 'no language';
            }
            var pos = mixed.length - 1;
            if (mixed.substr(pos) === '.') {
                return _language[langType][mixed.substr(0, pos)];
            }
            var obj = _parseLangKey(mixed);
            return _language[langType][obj.ns][obj.key];
        }
        _each(mixed, function (key, val) {
            var obj = _parseLangKey(key);
            if (!_language[langType]) {
                _language[langType] = {};
            }
            if (!_language[langType][obj.ns]) {
                _language[langType][obj.ns] = {};
            }
            _language[langType][obj.ns][obj.key] = val;
        });
    }


    //创建窗体
    function _create(expr, options) {
        options = options || {};
        P.options.seatNo = _undef(options.seatNo, P.options.seatNo);
        P.options.seatExt = _undef(options.seatExt, P.options.seatExt);
        P.options.seatPassWord = _undef(options.seatPassWord, P.options.seatPassWord);
        P.options.iccSerVicePath = _undef(options.iccSerVicePath, P.options.iccSerVicePath);
        P.options.isCallinAlert = _undef(options.isCallinAlert, P.options.isCallinAlert);
        P.options.CallinFn = _undef(options.CallinFn, P.options.CallinFn);
        //P.options.CallinUrl = _undef(options.CallinUrl, P.options.CallinUrl);
        P.options.isCalloutAlert = _undef(options.isCalloutAlert, P.options.isCalloutAlert);
        P.options.CalloutFn = _undef(options.CalloutFn, P.options.CalloutFn);
        //P.options.CalloutUrl = _undef(options.CalloutUrl, P.options.CalloutUrl);
        P.options.langType = _undef(options.langType, P.options.langType);
        P.options.items = _undef(options.items, P.options.items)
        P.basePath = _undef(options.basePath, P.basePath);//根目录
        P.options.langPath = _undef(options.langPath, P.basePath + 'lang/');//语言目录
        P.options.themesPath = _undef(options.themesPath, P.basePath + 'Base/');//样式目录
        //加载样式
        _loadStyle(P.options.themesPath + 'Tel.css');
        _loadStyle(P.options.themesPath + 'Telicon.css');
        _loadStyle(P.basePath + 'iconfont/iconfont.css');
        _loadStyle(P.options.themesPath + 'index-content.css');
        _loadScript(P.options.themesPath + 'socket.js');
        _loadScript(P.options.themesPath + 'Phone.js');

        var knode = P(expr);
        if (!knode || knode.length === 0) {
            return;
        }
        //获取到页面上的载体
        //再载体前面增加一个容器
        parentDiv(knode[0]);
        createTelButton(P.options.items);
        createRightInfo();
    };

    //创建一个主容器
    var container;
    function parentDiv(knode) {
        var self = knode;
        var width = knode.style.width || 994;
        var height = knode.style.height || 42;
        container = document.createElement("div");
        container.className = "icctel-container";
        container.setAttribute("style", "width:" + width + "px;height:" + height + "px;");
        for (var i = 0, j = self.parentNode.childNodes.length; i < j; i++) {
            var node = self.parentNode.childNodes[i];
            if (node === knode) {
                self.parentNode.insertBefore(container, node);
            }
        }
    }

    //LinkButton Input
    var ICCLINK = ',checkIn,checkOut,callTel,dropTel,threeCall,dialBack,threeTalk,killTalk,transSeatno,freeCall,keepCall,keepRegain,keepQuiet,';
    var ICCINPUT = ',txtTel,';
    //ID组合
    var IccId = new Array();
    IccId["checkIn"] = "whfckIn"; IccId["checkOut"] = "whfchOut"; IccId["txtTel"] = "Icctxttel";
    IccId["freeCall"] = "whfbusy"; IccId["keepCall"] = "whfhold1"; IccId["keepRegain"] = "whfhold0"; IccId["keepQuiet"] = "whfhold2";
    //样式组合
    var IccClass = new Array();
    IccClass["checkIn"] = "whflink Icc-l-btn";
    IccClass["IccAll"] = "whflink Icc-l-btn Icc-l-btn-disabled";//
    //点击事件
    var IccOnclick = new Array();
    IccOnclick["checkIn"] = "checkIn()"; IccOnclick["checkOut"] = "checkOut()"; IccOnclick["callTel"] = "callTel()"; IccOnclick["dropTel"] = "dropTel()";
    IccOnclick["threeCall"] = "threeCall()"; IccOnclick["dialBack"] = "dialBack()"; IccOnclick["threeTalk"] = "threeTalk()"; IccOnclick["killTalk"] = "killTalk()";
    IccOnclick["transSeatno"] = "transSeatno()"; IccOnclick["freeCall"] = "freeCall(1)"; IccOnclick["keepCall"] = "keepCall(1)"; IccOnclick["keepRegain"] = "keepRegain(0)";
    IccOnclick["keepQuiet"] = "keepQuiet(2)";

    P.IccOnclick = IccOnclick;
    //Icon图标
    var IccIcon = new Array();
    IccIcon["checkIn"] = "Icc-l-btn-icon icon-cc-denglu color-shenlan"; IccIcon["checkOut"] = "Icc-l-btn-icon icon-cc-dengchu color-shenlan"; IccIcon["callTel"] = "Icc-l-btn-icon icon-cc-huawu color-shenlan";
    IccIcon["dropTel"] = "Icc-l-btn-icon icon-cc-guaji color-shenlan"; IccIcon["threeCall"] = "Icc-l-btn-icon icon-cc-sanfang color-shenlan"; IccIcon["dialBack"] = "Icc-l-btn-icon icon-cc-pingjia color-shenlan";
    IccIcon["threeTalk"] = "Icc-l-btn-icon icon-answer color-shenlan"; IccIcon["killTalk"] = "Icc-l-btn-icon icon-hangup color-shenlan"; IccIcon["transSeatno"] = "Icc-l-btn-icon icon-link color-shenlan";
    IccIcon["freeCall"] = "Icc-l-btn-icon icon-tousuguanli color-shenlan"; IccIcon["keepCall"] = "Icc-l-btn-icon icon-ringpause color-shenlan"; IccIcon["keepRegain"] = "Icc-l-btn-icon icon-ring color-shenlan";
    IccIcon["keepQuiet"] = "Icc-l-btn-icon icon-ringpause color-shenlan";
    //增加text文件
    var IccButtonText = new Array();
    IccButtonText["checkIn"] = "_language[\"@langType@\"].core.checkIn"; IccButtonText["checkOut"] = "_language[\"@langType@\"].core.checkOut";
    IccButtonText["callTel"] = "_language[\"@langType@\"].core.callTel"; IccButtonText["dropTel"] = "_language[\"@langType@\"].core.dropTel";
    IccButtonText["threeCall"] = "_language[\"@langType@\"].core.threeCall"; IccButtonText["dialBack"] = "_language[\"@langType@\"].core.dialBack";
    IccButtonText["threeTalk"] = "_language[\"@langType@\"].core.threeTalk"; IccButtonText["killTalk"] = "_language[\"@langType@\"].core.killTalk";
    IccButtonText["transSeatno"] = "_language[\"@langType@\"].core.transSeatno"; IccButtonText["freeCall"] = "_language[\"@langType@\"].core.freeCall";
    IccButtonText["keepCall"] = "_language[\"@langType@\"].core.keepCall"; IccButtonText["keepRegain"] = "_language[\"@langType@\"].core.keepBack";
    IccButtonText["keepQuiet"] = "_language[\"@langType@\"].core.keepQuiet"; IccButtonText["busyCall"] = "_language[\"@langType@\"].core.busyCall";
    var keepID = ["1", "2"];
    var IccText = new Array();
    //创建按钮
    function createTelButton(items) {
        //var res = eval("_language[P.options.langType].core.checkIn");
        _each(items, function (key, val) {
            if (P.inArray(val, _ITEMS) != -1) {
                //首先必须是系统规定组件之一
                if (ICCLINK.indexOf(',' + val + ',') > -1) {
                    createLink(val, "");
                    IccText["busyCall"] = eval(IccButtonText["busyCall"].replace("@langType@", P.options.langType));
                }
                else if (ICCINPUT.indexOf(',' + val + ',') > -1) {
                    createInput(val);
                }
            }
        });
    }

    function createLink(_this, arr) {
        //按钮主部分
        var node = document.createElement("a");
        if (_undef(IccId[_this], "") != "") {
            node.id = IccId[_this] + (arr === '' ? "" : keepID[arr]);

        }
        node.name = _this;
        node.href = "javascript:void(0);";
        if (_undef(IccOnclick[_this], "") != "" && _this == "checkIn") {
            node.setAttribute("onclick", IccOnclick[_this]);
        }
        if (_this == "keepRegain") {
            node.style = "display: none;";
        }
        node.className = _undef(IccClass[_this], _undef(IccClass["IccAll"], ""));
        container.appendChild(node);
        //内容部分
        var node1 = document.createElement("span");
        node1.className = "Icc-l-btn-left Icc-l-btn-icon-top";
        node.appendChild(node1);
        //文本部门
        var node2 = document.createElement("span");
        node2.className = "Icc-l-btn-text";
        var Text = eval(IccButtonText[_this].replace("@langType@", P.options.langType));
        IccText[_this] = Text;
        node2.appendChild(document.createTextNode(Text));
        node1.appendChild(node2);
        //图标部分
        var node3 = document.createElement("span");
        node3.className = _undef(IccIcon[_this], "");
        node3.innerHTML = "&nbsp;";
        node1.appendChild(node3);
        if (_this == "keepCall") {
            createLink('keepRegain', 0);
        }
        else if (_this == "keepQuiet") {
            createLink('keepRegain', 1);
        }
    }

    function createInput(_this) {
        //按钮主部分
        var node = document.createElement("input");
        if (_undef(IccId[_this], "") != "") {
            node.id = IccId[_this];
            node.name = IccId[_this];
        }
        node.type = "text";
        node.value = "";
        container.appendChild(node);
    }

    //创建分机状态功能
    function createRightInfo() {
        //主容器的加载
        var node = document.createElement("ul");
        node.className = "regularly-item-list ThreeDul";
        container.appendChild(node);
        var node1 = document.createElement("li");
        node1.className = "first viewport-flip";
        node.appendChild(node1);

        //第一页的排版
        var node2 = document.createElement("div");
        node2.className = "flip-box flip";
        node1.appendChild(node2);
        //第一个版面的第一个div内容
        var node2_1 = document.createElement("div");
        node2_1.appendChild(document.createTextNode(eval("_language[\"" + P.options.langType + "\"].core.seatNo") + "："));
        node2.appendChild(node2_1);
        var node2_1_1 = document.createElement("span");
        node2_1_1.style = "color: #07a4a9";
        node2_1_1.appendChild(document.createTextNode(P.options.seatExt));
        node2_1.appendChild(node2_1_1);
        //第一个版面的第二个div内容
        var node2_2 = document.createElement("div");
        node2_2.appendChild(document.createTextNode(eval("_language[\"" + P.options.langType + "\"].core.lineUp") + "："));
        node2.appendChild(node2_2);
        var node2_2_1 = document.createElement("span");
        node2_2_1.id = "IcclineUp";
        node2_2_1.appendChild(document.createTextNode("0"));
        node2_2.appendChild(node2_2_1);

        //第二页的排版
        var node3 = document.createElement("div");
        node3.className = "flip-box flip";
        node1.appendChild(node3);
        //第二页的版面第一个div
        var node3_1 = document.createElement("div");
        node3_1.appendChild(document.createTextNode(eval("_language[\"" + P.options.langType + "\"].core.seatExtSate") + "："));
        node3.appendChild(node3_1);
        var node3_1_1 = document.createElement("span");
        node3_1_1.id = "Iccstate";
        node3_1_1.appendChild(document.createTextNode("离线"));
        node3_1.appendChild(node3_1_1);

        var node3_2 = document.createElement("div");
        node3_2.appendChild(document.createTextNode(eval("_language[\"" + P.options.langType + "\"].core.callLongTime") + "："));
        node3.appendChild(node3_2);
        var node3_2_1 = document.createElement("span");
        node3_2_1.id = "IcctimerSpan";
        node3_2_1.appendChild(document.createTextNode("00:00:00"));
        node3_2.appendChild(node3_2_1);
    }

    //方法属性都绑定
    P.create = _create;
    P.plugin = _plugins;
    P.lang = _lang;
    P.IccText = IccText;
    //Js
})(window);

function callPhone(phoneNumber){
    $('#Icctxttel').val(phoneNumber);
    callTel();
}