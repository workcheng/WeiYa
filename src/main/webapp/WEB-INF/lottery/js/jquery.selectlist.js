/*!
 * select v0.0.1
 * by Liuyuchao
 * Copyright 2015.2
 * Date: Tue Feb 25 2015
 */

 ;(function($,window,document){

    var pluginName = 'selectlist',
        defaults = {

            //默认属性配置
            enable: true,   //选择列表是否可用
            zIndex: null,  //选择列表z-index值，如需兼容IE6/7,必须加此属性
            width: null,   //选择列表宽度
            height: null,  //选择列表高度
            border: null,  //选择列表边框
            borderRadius: null,  //选择列表圆角
            showMaxHeight: null,  //选择列表显示最大高度
            optionHeight: 34,   //选择列表单项高度
            triangleSize: 6,   //右侧小三角大小
            triangleColor: '#333',  //右侧小三角颜色
            
            topPosition: false,  //选择列表项在列表框上部显示,默认在下边显示
            speed: 100,   //选择列表框显示动画速度（毫秒）
            onChange: function(){}  //自定义模拟选择列表项change事件
        };

    function SelectList(element,options){
        this.element = element;
        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this.init();        
    }
    
    SelectList.prototype={
        
        init: function(){
            var that = this,
                element = this.element;
                
            that.replaceProtoypeSelect(element);
            that.setSelectEvent(element);
            that.setSelectStyle(element);
        },
        
        //获取原生选择列表id值
        getSelectID: function(element){
            var $this = $(element),
                selectID = $this.attr('id');
                
            if(typeof(selectID) !== 'undefined'){
                return selectID;
            }else{
                selectID='';
                return selectID;
            }
        },
        
        //获取原生选择列表name值
        getSelectName: function(element){
            var that = this,
                $this = $(element),
                selectName = $this.attr('name');
                
            if(typeof(selectName) !== 'undefined'){
                return selectName;
            }else{
                return that.getSelectID($this);
            }
        },
        
        //获取原生选择列表class名
        getSelectClassName: function(element){
            var $this = $(element),
                tempClassNameArray = [],
                classNameArray = [],
                className = $this.attr('class');
                
            if(typeof(className) !== 'undefined'){
                classNameArray = className.split(' ');
                classNameArray.sort();
                tempClassNameArray =[classNameArray[0]];
                for(var i = 1; i < classNameArray.length; i++){
                    if( classNameArray[i] !== tempClassNameArray[tempClassNameArray.length-1]){
                        tempClassNameArray.push(classNameArray[i]);
                    }
                }
                return tempClassNameArray.join(' ');
            }else{
                className='';
                return className;
            }
        },

        //获取原生选择列表选中索引值
        getSelectedIndex: function(element){
            var $this = $(element),
                selectedIndex = $this.get(0).selectedIndex || 0;
                
            return selectedIndex;
        },
        
        //获取原生选择列表option的数量
        getOptionCount: function(element){
            var $this = $(element);

            return  $this.children().length;
        },
        
        //获取原生选择列表option的内容
        getOptionText: function(element){
            var that = this,
                $this = $(element),
                $option = $this.children(),
                optionText = [],
                selectLength = that.getOptionCount($this);
            
            for(var i=0;i<selectLength;i++){
                optionText[i] = $.trim($option.eq(i).text());
            }
            return optionText;
        },
        
        //获取原生选择列表选中值
        getSelectedOptionText: function(element){
            var that = this,
                $this = $(element),
                selectedIndex = that.getSelectedIndex($this),
                optionText = that.getOptionText($this);
                
            return optionText[selectedIndex];
            
        },
        
        //获取原生选择列表选中option的value值
        getSelectedOptionValue: function(element){
            var that = this,
                $this = $(element),
                selectedIndex = that.getSelectedIndex($this),
                optionValue = that.getOptionValue($this);
            
            return optionValue[selectedIndex];
        },
        
        //获取原生选择列表所有option的value,返回数组
        getOptionValue: function(element){
            var that = this,
                $this = $(element),
                $option = $this.children(),
                optionValue = [],
                selectLength = that.getOptionCount($this);
                
            for(var i = 0; i < selectLength; i++ ){
                optionValue[i] = $option.eq(i).val();
            }
            return optionValue;
        },
        
        //生成模拟选择列表
        renderSelect: function(element){
            var that = this,
                $this = $(element),
                selectID = that.getSelectID($this),
                selectName = that.getSelectName($this),
                selectClassName = that.getSelectClassName($this),
                selectOptionText = that.getOptionText($this),
                selectedOptionText = that.getSelectedOptionText($this),
                selectOptionValue = that.getOptionValue($this),
                selectedIndex = that.getSelectedIndex($this),
                selectedValue = that.getSelectedOptionValue($this),
                selectLength = that.getOptionCount($this),
                selectHTML = '<div id="' + selectID + '" class="select-wrapper ' + selectClassName + '"><input type="hidden" name="' + selectName + '" value="' + selectedValue + '" /><i class="icon select-down"></i><input type="button" class="select-button" value="' + selectedOptionText + '" /><div class="select-list"><ul>',
                selectListHTML = '';
                
            for(var i=0; i<selectLength; i++){
                if(i !== selectedIndex){
                    selectListHTML = selectListHTML + '<li data-value="' + selectOptionValue[i] + '">' + selectOptionText[i] + '</li>';
                }else{
                    selectListHTML = selectListHTML + '<li data-value="' + selectOptionValue[i] + '" class="selected">' + selectOptionText[i] + '</li>';
                }
                
            }
            selectHTML = selectHTML + selectListHTML + '</ul></div></div>';
                
            return selectHTML;                                          
        },
        
        //替换原生选择列表
        replaceProtoypeSelect: function(element){
            var that = this,
                $this = $(element),
                selectHTML = that.renderSelect($this);

            $this.replaceWith(selectHTML);
        },
        
        //设置模拟选择列表不可用
        setSelectDisabled: function(element){
            var that = this,
                $this = $(element),
                selectID = '#' + that.getSelectID($this);
                
            $(selectID).children('i').addClass('disabled').end()
                .children('.select-button').attr('disabled','disabled');
        },
        
        //设置模拟选择列表可用
        setSelectEnabled: function(element){
            var that = this,
                $this = $(element),
                selectID = '#' + that.getSelectID($this);
                
            $(selectID).children('i').removeClass('disabled').end()
                .children('.select-button').removeAttr('disabled');
        },
        
        //设置模拟选择列表样式
        setSelectStyle: function(element){
            var that = this,
                $this = $(element),
                selectID = '#' + that.getSelectID($this),
                selectLength = that.getOptionCount($this);
                
            //设置模拟选择列表外层样式
            $(selectID).css({
                'z-index': that.setStyleProperty(that.settings.zIndex),
                width: that.setStyleProperty(that.settings.width) - 2 + 'px',
                height: that.setStyleProperty(that.settings.height) + 'px'
            });
            
            //设置模拟选择列表向下箭头样式
            $(selectID).children('.select-down').css({
                top: that.setStyleProperty((that.settings.height - that.settings.triangleSize)/2) + 'px',
                'border-width': that.setStyleProperty(that.settings.triangleSize) + 'px',
                'border-color': that.setStyleProperty(that.settings.triangleColor) + ' transparent transparent transparent'
            });
            
            //设置模拟选择列表按钮样式
            $(selectID).children('.select-button').css({
                width: function(){
                    if(!that.settings.width){
                        return;
                    }else{
                        return that.settings.width - 2 + 'px';
                    }
                },
                height: that.setStyleProperty(that.settings.height) + 'px',
                border: that.setStyleProperty(that.settings.border),
                'border-radius': that.setStyleProperty(that.settings.borderRadius) + 'px'
            });
            
            //设置模拟选择列表下拉外层样式
            $(selectID).children('.select-list').css({
                width:  function(){
                    if(!that.settings.width){
                        return;
                    }else{
                        return that.settings.width - 2 + 'px';
                    }
                },
                'top': function(index,value){
                    if(!that.settings.height){
                        return;
                    }else{
                        if(!that.settings.topPosition){
                            return that.settings.height + 1 + 'px';
                        }else{
                            if(!that.settings.optionHeight){
                                //计算下拉列表高度
                            }else{
                                return -that.settings.optionHeight*selectLength - 3 + 'px';
                            }
                        }
                    }   
                },
                'max-height': that.setStyleProperty(that.settings.showMaxHeight) + 'px'
            }); 
            
            //设置设置模拟选择列表选项外层样式
            $(selectID + ' ul').css({
                'max-height': that.setStyleProperty(that.settings.showMaxHeight) + 'px',
                'line-height': that.setStyleProperty(that.settings.optionHeight) + 'px'
            });
            
            //设置模拟选择列表选项样式
            $(selectID + ' li').css({
                height: that.setStyleProperty(that.settings.optionHeight) + 'px'
            }); 
            
        },
        
        //检测是否设置某个样式
        setStyleProperty: function(styleProperty){
            if(!styleProperty){
                return;
            }else{
                return styleProperty;
            }
        },
            
        //绑定模拟选择列表一系列事件
        setSelectEvent: function(element){
            var that = this,
                $this = $(element),
                showSpeed = that.settings.speed,
                border = that.settings.border,
                selectID = '#' + that.getSelectID($this),
                selectName = that.getSelectName($this),
                selectedIndex = that.getSelectedIndex($this),
                selectLength = that.getOptionCount($this),
                selectBtn = $(selectID + ' input[type="button"]'),
                selectItem = $(selectID + ' li');
            
            if(that.settings.enable){
                $(selectID)
                    .click(function(event){
                        event.stopPropagation();
                        $(this).children('.select-list').slideToggle(showSpeed);

                        if(that.settings.border){
                            $(this).css({border:border});
                        }else{
                            $(this).addClass('focus');
                        }

                        $(this).find('li').each(function(){
                            if($(this).text() === selectBtn.val()){
                                $(this).addClass('selected').siblings().removeClass('selected');
                            }
                        })

                    })
                    .on('focusin','input[type="button"]',function(){
                        $('.select-wrapper').children('.select-list').slideUp(showSpeed);
                        if($('.select-wrapper').hasClass('focus')){
                            $('.select-wrapper').removeClass('focus');
                        }
                    })
                    .on('keyup','input[type="button"]',function(event){
                        //缓存第一个被选中的值
                        var $selectedItem = $(this).siblings('.select-list').children().children('li.selected');
                        
                        switch(event.keyCode){
                            //Enter
                            case 13:
                                $(this)
                                    .val($selectedItem.text())
                                    .prev().prev().val($selectedItem.attr('data-value'));
                                if ($.isFunction(that.settings.onChange)) {
                                    that.settings.onChange.call(that);
                                }
                                break;
                            //Esc
                            case 27:
                                $(this).siblings('.select-list').slideUp(showSpeed);
                                break;
                            //Up
                            case 38:
                                event.preventDefault();
                                if(selectedIndex !== 0){
                                    $selectedItem.removeClass('selected').prev().addClass('selected');
                                    selectedIndex =  selectedIndex - 1;
                                }else{
                                    $selectItem.last().addClass('selected').siblings().removeClass('selected');
                                    selectedIndex = selectLength - 1;
                                }
                                $selectedItem =  $(this).siblings('.select-list').children().children('li.selected');
                                $(this)
                                    .val($selectedItem.text())
                                    .prev().prev().val($selectedItem.attr('data-value'));
                                break;
                            //Down
                            case 40:
                                event.preventDefault();
                                if(selectedIndex < selectLength - 1 ){
                                    $selectedItem.removeClass('selected').next().addClass('selected');
                                    selectedIndex =  selectedIndex + 1; 
                                }else{
                                    $selectItem.first().addClass('selected').siblings().removeClass('selected');
                                    selectedIndex = 0;
                                }
                                $selectedItem =  $(this).siblings('.select-list').children().children('li.selected');
                                $(this)
                                    .val($selectedItem.text())
                                    .prev().prev().val($selectedItem.attr('data-value'));
                                break;
                        }
                        
                    })
                    .children('i').removeClass('disabled').end()
                    .children('.select-button').removeAttr('disabled');
                    
                //绑定单击选项事件
                selectItem.on('click',function(event){
                    event.stopPropagation();                    
                    $(this)
                        .addClass('selected').siblings().removeClass('selected')
                        .parent().parent().slideUp(showSpeed)
                        .prev().val($(this).text())
                        .siblings('input[type="hidden"]').val($(this).attr('data-value'));

                    if($('.select-wrapper').hasClass('focus')){
                        $('.select-wrapper').removeClass('focus');
                    }
                
                    if($.isFunction(that.settings.onChange)){
                        that.settings.onChange.call(that);
                    }

                    return false;
                }).hover(function(){
                    $(this).addClass('selected').siblings().removeClass('selected');
                }).mouseenter(function(event){
                    var target = event.target,
                        realWidth =  target.offsetWidth,
                        wrapperWidth = target.scrollWidth,
                        text = $(target).text();
                    if(realWidth < wrapperWidth){
                         $(target).attr( "title", text);
                    }
                })

                $(document).on('click',function(){
                    $(this).find('.select-list').slideUp(showSpeed);
                    if($('.select-wrapper').hasClass('focus')){
                        $('.select-wrapper').removeClass('focus');
                    }
                })
                               
            }else{
                $(selectID)
                    .children('i').addClass('disabled').end()
                    .children('.select-button').attr('disabled','disabled');
                return;
            }
        }
        
    };

    $.fn[pluginName] = function(options) {
        this.each(function() {
            if (!$.data(this, "plugin_" + pluginName)) {
                $.data(this, "plugin_" + pluginName, new SelectList(this, options));
                if(!options.topPosition){
                    options.zIndex--;
                }else{
                    options.zIndex++;
                };              
            }
        });
        return this;
    };
    
 })(jQuery,window,document);