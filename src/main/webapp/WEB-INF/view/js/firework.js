var canvas;
var ctx;
var bigbooms = [];
var lastTime;
var ishow = false;
var hide = function () {
    $('#canvas').remove();
    ishow = false;
};
var show = function () {
    ishow = true;
    $('body').append('<canvas id="canvas"></canvas>');
    $('#canvas').attr({'width': $(window).width(), 'height': $(window).height()});
    canvas = document.getElementById("canvas");
    ctx = canvas.getContext("2d");
    bigbooms = [];
    initAnimate();
};

var initAnimate = function () {
    lastTime = new Date();
    if (ishow) {
        animate();
    }
}
var animate = function () {
    ctx.save();
    ctx.fillStyle = "rgba(0,0,0,0.5)";
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    ctx.restore();
    var newTime = new Date();
    if (newTime - lastTime > 300) {
        var x = getRandom(canvas.width / 5, canvas.width * 4 / 5);
        var y = getRandom(50, 200);
        var bigboom = new Boom(getRandom(canvas.width / 3, canvas.width * 2 / 3), 2, "#FFF", {
            x: x,
            y: y
        });
        bigbooms.push(bigboom)
        lastTime = newTime;
    }
    bigbooms.foreach(function (index) {
        var that = this;
        if (!this.dead) {
            this._move();
            this._drawLight();
        }
        else {
            this.booms.foreach(function (index) {
                if (!this.dead) {
                    this.moveTo(index);
                }
                else if (index === that.booms.length - 1) {
                    bigbooms[bigbooms.indexOf(that)] = null;
                }
            })
        }
    });
    if (ishow) {
        raf(animate);
    }
}

Array.prototype.foreach = function (callback) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] !== null) callback.apply(this[i], [i])
    }
}

var raf = window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.oRequestAnimationFrame || window.msRequestAnimationFrame || function (callback) {
        window.setTimeout(callback, 1);
    };

var Boom = function (x, r, c, boomArea, shape) {
    this.booms = [];
    this.x = x;
    this.y = (canvas.height + r);
    this.r = r;
    this.c = c;
    this.shape = shape || false;
    this.boomArea = boomArea;
    this.theta = 0;
    this.dead = false;
    this.ba = parseInt(getRandom(80, 200));
}
Boom.prototype = {
    _paint: function () {
        ctx.save();
        ctx.beginPath();
        ctx.arc(this.x, this.y, this.r, 0, 2 * Math.PI);
        ctx.fillStyle = this.c;
        ctx.fill();
        ctx.restore();
    },
    _move: function () {
        var dx = this.boomArea.x - this.x, dy = this.boomArea.y - this.y;
        this.x = this.x + dx * 0.09;
        this.y = this.y + dy * 0.09;

        if (Math.abs(dx) <= this.ba && Math.abs(dy) <= this.ba) {
            if (this.shape) {
                this._shapBoom();
            }
            else this._boom();
            this.dead = true;
        }
        else {
            this._paint();
        }
    },
    _drawLight: function () {
        ctx.save();
        ctx.fillStyle = "rgba(255,228,150,0.3)";
        ctx.beginPath();
        ctx.arc(this.x, this.y, this.r + 3 * Math.random() + 1, 0, 2 * Math.PI);
        ctx.fill();
        ctx.restore();
    },
    _boom: function () {
        var fragNum = getRandom(30, 200);
        var style = getRandom(0, 10) >= 5 ? 1 : 2;
        var color;
        if (style === 1) {
            color = {
                a: parseInt(getRandom(128, 255)),
                b: parseInt(getRandom(128, 255)),
                c: parseInt(getRandom(128, 255))
            }
        }

        var fanwei = parseInt(getRandom(300, 800));
        for (var i = 0; i < fragNum; i++) {
            if (style === 2) {
                color = {
                    a: parseInt(getRandom(128, 255)),
                    b: parseInt(getRandom(128, 255)),
                    c: parseInt(getRandom(128, 255))
                }
            }
            var a = getRandom(-Math.PI, Math.PI);
            var x = getRandom(0, fanwei) * Math.cos(a) + this.x;
            var y = getRandom(0, fanwei) * Math.sin(a) + this.y;
            var radius = getRandom(0, 5)
            var frag = new Frag(this.x, this.y, radius, color, x, y);
            this.booms.push(frag);
        }
    }
}


var getRandom = function (a, b) {
    return Math.random() * (b - a) + a;
}

var Frag = function (centerX, centerY, radius, color, tx, ty) {
    this.tx = tx;
    this.ty = ty;
    this.x = centerX;
    this.y = centerY;
    this.dead = false;
    this.centerX = centerX;
    this.centerY = centerY;
    this.radius = radius;
    this.color = color;
}

Frag.prototype = {
    paint: function () {
        ctx.save();
        ctx.beginPath();
        ctx.arc(this.x, this.y, this.radius, 0, 2 * Math.PI);
        ctx.fillStyle = "rgba(" + this.color.a + "," + this.color.b + "," + this.color.c + ",1)";
        ctx.fill();
        ctx.restore();
    },
    moveTo: function (index) {
        this.ty = this.ty + 0.3;
        var dx = this.tx - this.x, dy = this.ty - this.y;
        this.x = Math.abs(dx) < 0.1 ? this.tx : (this.x + dx * 0.1);
        this.y = Math.abs(dy) < 0.1 ? this.ty : (this.y + dy * 0.1);
        if (dx === 0 && Math.abs(dy) <= 80) {
            this.dead = true;
        }
        this.paint();
    }
}
