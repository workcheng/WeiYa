/*!
 *@作者：  赵玉
 *@邮箱：  sailiy@126.com
 *@公司：  彩虹世纪文化传媒有限公司
 *@项目：  jquery.barrager.js
 * <!--https://github.com/Sailiy/JBarrager-->
 */
(function ($) {
    function Barrager(dom) {
        this.canvas = dom.get(0);
        this.ctx = this.canvas.getContext("2d");
        this.msgs = new Array(300);
        this.image = new Image();
        this.width = 1280;
        this.height = 720;
        this.canvas.width = this.width;
        this.canvas.height = this.height;
        this.font = "30px 黑体";
        this.ctx.font = this.font;
        this.colorArr = [{"font": "#F0FFF0", "bg": "#000099"}, {"font": "#F0FFF0", "bg": "#990066"},
            {"font": "#EE0000", "bg": "#F0FFF0"}, {"font": "#68228B", "bg": "#00CD00"},
            {"font": "#0000FF", "bg": "#CD3700"}, {"font": "#3A5FCD", "bg": "#CDCD00"},
            {"font": "#000000", "bg": "#FFFFFF"},
            {"font": "#FFFFFF", "bg": "#CC3333"},
            {"font": "#FFFFFF", "bg": "#FF6600"},
            {"font": "#FFFFFF", "bg": "#CC0033"}
        ];
        this.interval = "";
        this.draw = function () {
            if (this.interval != "")return;
            var _this = this;
            this.interval = setInterval(function () {
                _this.ctx.clearRect(0, 0, _this.width, _this.height);
                _this.ctx.save();
                for (var i = 0; i < _this.msgs.length; i++) {
                    if (!(_this.msgs[i] == null || _this.msgs[i] == "" || typeof(_this.msgs[i]) == "undefined")) {
                        _this.image.src = _this.msgs[i].img;
                        if (_this.msgs[i].L == null || typeof(_this.msgs[i].L) == "undefined") {
                            _this.msgs[i].L = _this.width;
                            _this.msgs[i].T = parseInt(Math.random() * 700);
                            if (_this.msgs[i].T < 40) {
                                _this.msgs[i].T = _this.msgs[i].T + 100;
                            }
                            _this.msgs[i].S = parseInt(Math.random() * (10 - 4) + 4);
                            _this.msgs[i].C = _this.colorArr[Math.floor(Math.random() * _this.colorArr.length)];
                        } else {
                            if (_this.msgs[i].L < -200) {
                                _this.msgs[i] = null;
                            } else {
                                _this.msgs[i].L = parseInt(_this.msgs[i].L - _this.msgs[i].S);
                                _this.drawRoundedRect(_this.msgs[i].C.font, _this.msgs[i].C.bg, _this.msgs[i].L - 20, _this.msgs[i].T - 25, _this.ctx.measureText(_this.msgs[i].msg).width + 25, 40, 10);
                                _this.ctx.fillStyle = _this.msgs[i].C.font;//文字颜色
                                _this.ctx.fillText(_this.msgs[i].msg, _this.msgs[i].L, _this.msgs[i].T + 6);

                                _this.ctx.drawImage(_this.image, _this.msgs[i].L - 60, _this.msgs[i].T - 35, 60, 60);
                                _this.ctx.arc(_this.image.width / 2, _this.image.width / 2, Math.min(_this.image.width, _this.image.height) / 2, 0, 2 * Math.PI);
                                _this.ctx.restore();
                            }
                        }
                    }

                }
            }, 35);
        };
        this.putMsg = function (datas) {
            for (var j = 0; j < datas.length; j++) {
                for (var i = 0; i < this.msgs.length; i++) {
                    if (this.msgs[i] == null || this.msgs[i] == "" || typeof(this.msgs[i]) == "undefined") {
                        this.msgs[i] = datas[j];
                        break;
                    }
                }
            }
            this.draw();
        };
        this.clear = function () {
            clearInterval(this.interval);
            this.interval = "";
            this.ctx.clearRect(0, 0, this.width, this.height);
            this.ctx.save();
            for (var i = 0; i < this.msgs.length; i++) {
                this.msgs[i] = null;
            }
        };
        this.drawRoundedRect = function (strokeStyle, fillStyle, cornerX, cornerY, width, height, cornerRadius) {
            this.ctx.beginPath();
            this.ctx.globalAlpha = '1';
            this.roundedRect(cornerX, cornerY, width, height, cornerRadius);
            this.ctx.strokeStyle = strokeStyle;
            this.ctx.fillStyle = fillStyle;
            this.ctx.stroke();
            this.ctx.fill();
        }
        this.roundedRect = function (cornerX, cornerY, width, height, cornerRadius) {
            if (width > 0) this.ctx.moveTo(cornerX + cornerRadius, cornerY);
            else  this.ctx.moveTo(cornerX - cornerRadius, cornerY);
            this.ctx.arcTo(cornerX + width, cornerY, cornerX + width, cornerY + height, cornerRadius);
            this.ctx.arcTo(cornerX + width, cornerY + height, cornerX, cornerY + height, cornerRadius);
            this.ctx.arcTo(cornerX, cornerY + height, cornerX, cornerY, cornerRadius);
            if (width > 0) {
                this.ctx.arcTo(cornerX, cornerY, cornerX + cornerRadius, cornerY, cornerRadius);
            }
            else {
                this.ctx.arcTo(cornerX, cornerY, cornerX - cornerRadius, cornerY, cornerRadius);
            }
        }
    }

    $.fn.barrager = function (para) {
        if (typeof(para) == "string") {
            try {
                var api = $(this).data('barrager_api');
                api[para].apply(api);
            } catch (e) {
            }
        } else if (typeof para == 'object' || !para) {
            $this = $(this);
            if ($this.data('barrager_api') != null && $this.data('barrager_api') != '') {
                var api = $this.data('barrager_api');
                api.putMsg(para);
            } else {
                var api = new Barrager($this);
                $this.data('barrager_api', api);
                api.putMsg(para);
            }
        } else {
            $.error('Method ' + method + ' does not exist on jQuery.barrager');
        }
        return this;
    }
})(jQuery);