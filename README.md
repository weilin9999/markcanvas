# markcanvas
安卓Canvas自定义化功能，Json数据转换成Canvas实体，小方便（timetip）独立开源项目， Android Project For Canvas , Json to Canvas Entity

## 耐心使用，教程没那么快做，如果可以，可以提交你自己写的教程给我，^_^ 有点懒了~

文件格式：
```json
/**
    使用建议：建议使用百分比布局
    element内部的组件集合的顺序是自上而下一层一层叠加的，越往后组件的层数就越高显示的位置就越靠前
**/
{
    "template_name": "app.weilin.timetip.mark_1", // 模板风格ID，开头必须以"app.weilin.timetip."开头
    "element":[ // 组件集合
        {
            "element_id": "element_1", // 数据类型：String，组件ID，必填项且必须是整个组件合集唯一的ID
            "type_id": "image", // 数据类型：String，组件的类型 （image:图片、text：文本）
            "file_pre" : "", // 数据类型：String， 如果不是APP自带资源则为"",否则为文件目录
            "file_name" : "", // 数据类型：String， 文件名称
            "is_file": false, // 数据类型：Boolean， 是否为外部文件
            "width_percent": 0, // percent优先级大于指定宽度，与width参数相加
            "width": 0, // 数据类型：Int， 宽度 -1 为屏幕宽度
            "height_percent": 0, // 数据类型：Float， 百分比布局 按 1~100 范围 可以超过100 与height参数相加
            "height": 0, //  数据类型：Int，高度 -1 为画布最高高度
            "left_percent": 0,// 数据类型：Float，百分比布局 与 left参数相叠加，当为0时指定左边相对位置为准 按 1~100 范围 可以超过100
            "left": 0, // 数据类型：Float， 左边相对位置-1 为居中
            "top_percent": 0, // 数据类型：Float， 与top参数相叠加，当为0时指定顶部相对位置为准
            "top": 0,  // 数据类型：Float， 顶部相对位置 -1 为居中
            "round": 0, // 数据类型：Float， 圆角度
            "rotate": 0, // 数据类型：Float， 旋转角度
            "offset_top_percent": 0, // 数据类型：Float， 百分比偏移量，此项不为空时offset_top参数无效
            "offset_top": 0, // 数据类型：Float， Top偏移量 可以为负数 非百分比
            "offset_left_percent": 0, // 数据类型：Float， 百分比偏移量，此项不为空时offset_left参数无效
            "offset_left": 0, // 数据类型：Float， Left偏移量 可以为负数 非百分比
            "align_left_mode": "center", // left_follow 参数无效，与根据指定组件合集left对齐方式，有:left，right，bottom，默认left
            "align_left": [], // 数据类型：Array， 单个组件填写方法["element_1"],多个写法["element_1","element_2"],有多个组件时只left和right对齐时只取最前端的组件对齐
            "align_top_mode": "center", // left_top 参数无效，与根据指定组件合集top对齐方式，有:center，top，bottom，默认top
            "align_top": [], // 数据类型：Array， 单个组件填写方法["element_1"],多个写法["element_1","element_2"]
            "display": "block", // 数据类型: String， 组件是否可视，参数: block显示、none隐藏，默认: block
            "margin_left_item": "", // 数据类型: String， 根据对象的宽度偏移位置 self为自己
            "margin_left_percent": 0, // 数据类型：Float， 根据对象的宽度的百分比定宽度 正数向右移动，负数向左移动
            "margin_top_item": "", // 数据类型: String， 根据对象的高度偏移位置 self为自己
            "margin_top_percent": 0, // 数据类型：Float， 根据对象的高度的百分比定宽度 正数向上移动，负数向下移动
            "is_blur": false, // 数据类型：Boolean，是否模糊图片
            "blur_percent": 0, // 数据类型：Int，模糊图片的百分比，0~100之间
            "alpha": 255, // 数据类型：Int，透明度，默认255，0~255区间
        },
        {
            "element_id": "element_2", // 数据类型：String，组件ID，必填项且必须是整个组件合集唯一的ID
            "type_id": "text", // 数据类型：String，组件的类型 （image:图片、text：文本）
            "context_id": "", // 数据类型：String，绑定组件数据，如果此项不为空则text的选项无效，
            /** 可绑定的组件数据为：
            app.context.text.title(顶部标题)、 app.context.text.content(顶部内容)、 app.context.text.mark(顶部标记)、
            app.context.text.daysdate(倒数的时间)、 app.context.text.date(底部目标日期)、
            **/
            "width_percent": 0, // percent优先级大于指定宽度，与width参数相加
            "width": 0, // 数据类型：Int， 宽度 -1 为屏幕宽度 0 为不限制 (文本会根据宽度自动换行) -2 为根据文本宽度自适应
            "max_width": 0, // 数据类型：Float， 限制文本长度，超过文本长度可以换行也可以省略
            "max_width_percent": 0, // 数据类型：Float， 设置字体大小
            "line_height": 0, // 数据类型：Int，文本最大高度，如果不为0则无限制，如果大于0则如果文本超过高度则显示...的省略字符
            "text": "", // 数据类型：String，指定文本，可以为自定义文本
            "font_color": "000000", // 数据类型：String，文本字体颜色
            "font_style_mode": 0, // 数据类型：Int， 字体样式模式 0 为本地 1 为资源库
            "font_style": "", // 数据类型：String，字体样式名称 空为系统默认 此参数为空则font_style_mode无效
            "font_size": 0, // 数据类型：Float， 设置字体大小
            "font_align": "center", // 数据类型：String，文本对齐方式，对齐方式有：center（居中）、left（左对齐）、right（右对齐）
            "font_stroke": false, // 数据类型：Boolean，是否为空心仅描边
            "stroke_width": 0, // 数据类型：Int，描边宽度，仅"font_stroke":true时生效
            "font_bold": false, // 数据类型：Boolean，是否为粗体
            "font_scale": 0, // 数据类型：Float，字体拉伸仅向X轴拉伸
            "skew_x": 0, // 数据类型：Float，字体倾斜，负数为右倾斜，正数为左倾斜
            "shadow_width": 0, // 数据类型：Float，字体阴影半径
            "shadow_x": 0, // 数据类型：Float，字体阴影X轴坐标偏移量
            "shadow_y": 0, // 数据类型：Float，字体阴影Y轴坐标偏移量
            "shadow_color": "eeeeee", // 数据类型：String，阴影颜色 16进制颜色代码
            "left_percent": 0, // 数据类型：Float，百分比布局 与 left参数相叠加，当为0时指定左边相对位置为准 按 1~100 范围 可以超过100
            "left": 0, // 数据类型：Float， 左边相对位置-1 为居中
            "top_percent": 0,// 数据类型：Float，与top参数相叠加，当为0时指定顶部相对位置为准
            "top": 0, // 数据类型：Float， 顶部相对位置 -1 为居中
            "rotate": 0, // 数据类型：Float， 旋转角度
            "offset_top_percent": 0, // 数据类型：Float， 百分比偏移量，此项不为空时offset_top参数无效
            "offset_top": 0, // 数据类型：Float， Top偏移量 可以为负数 非百分比
            "offset_left_percent": 0, // 数据类型：Float， 百分比偏移量，此项不为空时offset_left参数无效
            "offset_left": 0, // 数据类型：Float， Left偏移量 可以为负数 非百分比
            "align_left_mode": "center", // left_follow 参数无效，与根据指定组件合集left对齐方式，有:left，right，bottom，默认left
            "align_left": [], // 数据类型：Array， 单个组件填写方法["element_1"],多个写法["element_1","element_2"],有多个组件时只left和right对齐时只取最前端的组件对齐
            "align_top_mode": "center", // left_top 参数无效，与根据指定组件合集top对齐方式，有:center，top，bottom，默认top
            "align_top": [], // 数据类型：Array， 单个组件填写方法["element_1"],多个写法["element_1","element_2"]
            "display": "block", // 数据类型: String， 组件是否可视，参数: block显示、none隐藏，默认: block
            "margin_left_item": "", // 数据类型: String， 根据对象的宽度偏移位置 self为自己
            "margin_left_percent": 0, // 数据类型：Float， 根据对象的宽度的百分比定宽度 正数向右移动，负数向左移动
            "margin_top_item": "", // 数据类型: String， 根据对象的高度偏移位置 self为自己
            "margin_top_percent": 0, // 数据类型：Float， 根据对象的高度的百分比定宽度 正数向上移动，负数向下移动
            "alpha": 255, // 数据类型：Int，透明度，默认255，0~255区间
        },
        {
            "element_id": "element_3", // 数据类型：String，组件ID，必填项且必须是整个组件合集唯一的ID
            "type_id": "circle", // 数据类型：String，组件的类型 圆形
            "color": "", // 数据类型：String，填充的颜色
            "stroke": false, // 数据类型：Boolean，是否为空心只有描边
            "stroke_width": 0, // 数据类型：Float，描边宽度，仅stroke参数为true时生效
            "size": 0, // 数据类型：Float，圆形大小，是圆的半径
            "size_percent": 0, // 数据类型：Float， 百分比大小，是圆的半径
            "display": "block", // 数据类型: String， 组件是否可视，参数: block显示、none隐藏，默认: block
            "left_percent": 0, // 数据类型：Float，百分比布局 与 left参数相叠加，当为0时指定左边相对位置为准 按 1~100 范围 可以超过100
            "left": 0, // 数据类型：Float， 左边相对位置-1 为居中
            "top_percent": 0, // 数据类型：Float，与top参数相叠加，当为0时指定顶部相对位置为准
            "top": 0, // 数据类型：Float， 顶部相对位置 -1 为居中
            "offset_top_percent": 0, // 数据类型：Float， 百分比偏移量，此项不为空时offset_top参数无效
            "offset_top": 0, // 数据类型：Float， Top偏移量 可以为负数 非百分比
            "offset_left_percent": 0, // 数据类型：Float， 百分比偏移量，此项不为空时offset_left参数无效
            "offset_left": 0, // 数据类型：Float， Left偏移量 可以为负数 非百分比
            "align_left_mode": "center", // left_follow 参数无效，与根据指定组件合集left对齐方式，有:left，right，bottom，默认left
            "align_left": [], // 数据类型：Array， 单个组件填写方法["element_1"],多个写法["element_1","element_2"],有多个组件时只left和right对齐时只取最前端的组件对齐
            "align_top_mode": "center", // left_top 参数无效，与根据指定组件合集top对齐方式，有:center，top，bottom，默认top
            "align_top": [], // 数据类型：Array， 单个组件填写方法["element_1"],多个写法["element_1","element_2"]
            "margin_left_item": "", // 数据类型: String， 根据对象的宽度偏移位置 self为自己
            "margin_left_percent": 0, // 数据类型：Float， 根据对象的宽度的百分比定宽度 正数向右移动，负数向左移动
            "margin_top_item": "", // 数据类型: String， 根据对象的高度偏移位置 self为自己
            "margin_top_percent": 0, // 数据类型：Float， 根据对象的高度的百分比定宽度 正数向上移动，负数向下移动
            "mask_item": "mask_1", // 数据类型: String，蒙版ID
            "mask_mode": "SRC_OUT", // 数据类型: String，蒙版的模式，默认 SRC_OUT
            "alpha": 255, // 数据类型：Int，透明度，默认255，0~255区间
            /* 
                SRC_OVER：在目标图像上层绘制源图像
                DST_OVER：与SRC_OVER相反，此模式是目标图像被绘制在源图像的上方
                SRC_IN：在两者相交的地方绘制源图像，并且绘制的效果会受到目标图像对应地方透明度的影响
                DST_IN：可以和SRC_IN 进行类比，在两者相交的地方绘制目标图像，并且绘制的效果会受到源图像对应地方透明度的影响
                SRC_OUT：从字面上可以理解为在不相交的地方绘制源图像
                DST_OUT：可以类比SRC_OUT , 在不相交的地方绘制目标图像，相交处根据源图像alpha进行过滤，完全不透明处则完全过滤，完全透明则不过滤
                SRC_ATOP：源图像和目标图像相交处绘制源图像，不相交的地方绘制目标图像，并且相交处的效果会受到源图像和目标图像alpha的影响
                DST_ATOP：源图像和目标图像相交处绘制目标图像，不相交的地方绘制源图像，并且相交处的效果会受到源图像和目标图像alpha的影响
                XOR：在不相交的地方按原样绘制源图像和目标图像，相交的地方受到对应alpha和颜色值影响，按公式进行计算，如果都完全不透明则相交处完全不绘制
                DARKEN：该模式处理过后，会感觉效果变暗，即进行对应像素的比较，取较暗值，如果色值相同则进行混合
                LIGHTEN：可以和 DARKEN 对比起来看，DARKEN 的目的是变暗，LIGHTEN 的目的则是变亮，如果在均完全不透明的情况下，色值取源色值和目标色值中的较大值，否则按上面算法进行计算
                MULTIPLY：正片叠底，即查看每个通道中的颜色信息，并将基色与混合色复合。结果色总是较暗的颜色，任何颜色与黑色复合产生黑色，任何颜色与白色复合保持不变，当用黑色或白色以外的颜色绘画时，绘画工具绘制的连续描边产生逐渐变暗的颜色
                SCREEN：滤色，滤色模式与我们所用的显示屏原理相同，所以也有版本把它翻译成屏幕；简单的说就是保留两个图层中较白的部分，较暗的部分被遮盖
                ADD：饱和度叠加
                OVERLAY：像素是进行 Multiply （正片叠底）混合还是 Screen （屏幕）混合，取决于底层颜色，但底层颜色的高光与阴影部分的亮度细节会被保留
                （摘录：https://www.jianshu.com/p/d11892bbe055）
            */
        },
        {
            "element_id": "element_4", // 数据类型：String，组件ID，必填项且必须是整个组件合集唯一的ID
            "type_id": "rect", // 数据类型：String，组件的类型 矩形
            "color": "", // 数据类型：String，填充的颜色
            "stroke": false, // 数据类型：Boolean，是否为空心只有描边
            "stroke_width": 0, // 数据类型：Float，描边宽度，仅stroke参数为true时生效
            "width": 0, // 数据类型：Float，矩形宽度大小
            "width_percent": 0, // 数据类型：Float， 百分比大小，宽度
            "height": 0, // 数据类型：Float，矩形高度度大小
            "height_percent": 0, // 数据类型：Float， 百分比大小，高度
            "auto_size_item": "element_1",
            "display": "block", // 数据类型: String， 组件是否可视，参数: block显示、none隐藏，默认: block
            "left_percent": 0, // 数据类型：Float，百分比布局 与 left参数相叠加，当为0时指定左边相对位置为准 按 1~100 范围 可以超过100
            "left": 0, // 数据类型：Float， 左边相对位置-1 为居中
            "top_percent": 0, // 数据类型：Float，与top参数相叠加，当为0时指定顶部相对位置为准
            "top": 0, // 数据类型：Float， 顶部相对位置 -1 为居中
            "offset_top_percent": 0, // 数据类型：Float， 百分比偏移量，此项不为空时offset_top参数无效
            "offset_top": 0, // 数据类型：Float， Top偏移量 可以为负数 非百分比
            "offset_left_percent": 0, // 数据类型：Float， 百分比偏移量，此项不为空时offset_left参数无效
            "offset_left": 0, // 数据类型：Float， Left偏移量 可以为负数 非百分比
            "align_left_mode": "center", // left_follow 参数无效，与根据指定组件合集left对齐方式，有:left，right，bottom，默认left
            "align_left": [], // 数据类型：Array， 单个组件填写方法["element_1"],多个写法["element_1","element_2"],有多个组件时只left和right对齐时只取最前端的组件对齐
            "align_top_mode": "center", // left_top 参数无效，与根据指定组件合集top对齐方式，有:center，top，bottom，默认top
            "align_top": [], // 数据类型：Array， 单个组件填写方法["element_1"],多个写法["element_1","element_2"]
            "margin_left_item": "", // 数据类型: String， 根据对象的宽度偏移位置 self为自己
            "margin_left_percent": 0, // 数据类型：Float， 根据对象的宽度的百分比定宽度 正数向右移动，负数向左移动
            "margin_top_item": "", // 数据类型: String， 根据对象的高度偏移位置 self为自己
            "margin_top_percent": 0, // 数据类型：Float， 根据对象的高度的百分比定宽度 正数向上移动，负数向下移动
            "mask_item": "mask_1", // 数据类型: String，蒙版ID
            "mask_mode": "SRC_OUT", // 数据类型: String，蒙版的模式，默认 SRC_OUT
            "round": 0, // 数据类型：Float， 圆角度
            "rotate": 0, // 数据类型：Float， 旋转角度
            "alpha": 255, // 数据类型：Int，透明度，默认255，0~255区间
        }
    ],
    "mode_only_show_title":[ // 标题样式适配：仅显示标题，会覆盖element集合内部的组件属性
        {
            "element_id": "element_2",
            "display" : "block"
        }
    ],
    "mode_only_show_content":[ // 标题样式适配：仅显示内容，会覆盖element集合内部的组件属性
        {
            "element_id": "element_2",
            "display" : "block"
        }
    ],
    "mode_show_detail_days":[ // 倒数日样式适配：显示年月日日期，会覆盖element集合内部的组件属性
        {
            "element_id": "element_2",
            "display" : "block"
        }
    ],
    "mask_element":[ // 蒙版对象，用于蒙版
        {
            "mask_id": "mask_1", // 数据类型：String，蒙版组件ID，必填项且必须是整个组件合集唯一的ID
            // 其余跟element相同，只有element_id变为了mask_id
            "type_id": "text", // 数据类型：String，组件的类型 （image:图片、text：文本）
            "context_id": "", // 数据类型：String，绑定组件数据，如果此项不为空则text的选项无效，
            /** 可绑定的组件数据为：
            app.context.text.title(顶部标题)、 app.context.text.content(顶部内容)、 app.context.text.mark(顶部标记)、
            app.context.text.daysdate(倒数的时间)、 app.context.text.date(底部目标日期)、
            **/
            "width_percent": 0, // percent优先级大于指定宽度，与width参数相加
            "width": 0, // 数据类型：Int， 宽度 -1 为屏幕宽度 0 为不限制 (文本会根据宽度自动换行) -2 为根据文本宽度自适应
            "max_width": 0, // 数据类型：Float， 限制文本长度，超过文本长度可以换行也可以省略
            "max_width_percent": 0, // 数据类型：Float， 设置字体大小
            "line_height": 0, // 数据类型：Int，文本最大高度，如果不为0则无限制，如果大于0则如果文本超过高度则显示...的省略字符
            "text": "", // 数据类型：String，指定文本，可以为自定义文本
            "font_color": "000000", // 数据类型：String，文本字体颜色
            "font_style_mode": 0, // 数据类型：Int， 字体样式模式 0 为本地 1 为资源库
            "font_style": "", // 数据类型：String，字体样式名称 空为系统默认 此参数为空则font_style_mode无效
            "font_size": 0, // 数据类型：Float， 设置字体大小
            "font_align": "center", // 数据类型：String，文本对齐方式，对齐方式有：center（居中）、left（左对齐）、right（右对齐）
            "font_stroke": false, // 数据类型：Boolean，是否为空心仅描边
            "stroke_width": 0, // 数据类型：Int，描边宽度，仅"font_stroke":true时生效
            "font_bold": false, // 数据类型：Boolean，是否为粗体
            "font_scale": 0, // 数据类型：Float，字体拉伸仅向X轴拉伸
            "skew_x": 0, // 数据类型：Float，字体倾斜，负数为右倾斜，正数为左倾斜
            "shadow_width": 0, // 数据类型：Float，字体阴影半径
            "shadow_x": 0, // 数据类型：Float，字体阴影X轴坐标偏移量
            "shadow_y": 0, // 数据类型：Float，字体阴影Y轴坐标偏移量
            "shadow_color": "eeeeee", // 数据类型：String，阴影颜色 16进制颜色代码
            "left_percent": 0, // 数据类型：Float，百分比布局 与 left参数相叠加，当为0时指定左边相对位置为准 按 1~100 范围 可以超过100
            "left": 0, // 数据类型：Float， 左边相对位置-1 为居中
            "top_percent": 0,// 数据类型：Float，与top参数相叠加，当为0时指定顶部相对位置为准
            "top": 0, // 数据类型：Float， 顶部相对位置 -1 为居中
            "rotate": 0, // 数据类型：Float， 旋转角度
            "offset_top_percent": 0, // 数据类型：Float， 百分比偏移量，此项不为空时offset_top参数无效
            "offset_top": 0, // 数据类型：Float， Top偏移量 可以为负数 非百分比
            "offset_left_percent": 0, // 数据类型：Float， 百分比偏移量，此项不为空时offset_left参数无效
            "offset_left": 0, // 数据类型：Float， Left偏移量 可以为负数 非百分比
            "align_left_mode": "center", // left_follow 参数无效，与根据指定组件合集left对齐方式，有:left，right，bottom，默认left
            "align_left": [], // 数据类型：Array， 单个组件填写方法["element_1"],多个写法["element_1","element_2"],有多个组件时只left和right对齐时只取最前端的组件对齐
            "align_top_mode": "center", // left_top 参数无效，与根据指定组件合集top对齐方式，有:center，top，bottom，默认top
            "align_top": [], // 数据类型：Array， 单个组件填写方法["element_1"],多个写法["element_1","element_2"]
            "display": "block", // 数据类型: String， 组件是否可视，参数: block显示、none隐藏，默认: block
            "margin_left_item": "", // 数据类型: String， 根据对象的宽度偏移位置 self为自己
            "margin_left_percent": 0, // 数据类型：Float， 根据对象的宽度的百分比定宽度 正数向右移动，负数向左移动
            "margin_top_item": "", // 数据类型: String， 根据对象的高度偏移位置 self为自己
            "margin_top_percent": 0, // 数据类型：Float， 根据对象的高度的百分比定宽度 正数向上移动，负数向下移动
        }
    ]
}
```
