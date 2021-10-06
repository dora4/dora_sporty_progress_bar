# DoraSportyProgressBar

描述：一个周期性刷新的进度条控件

复杂度：★★☆☆☆

分组：【Dora大控件组】

关系：暂无

技术要点：基本绘图、ValueAnimator、TypeEvaluator

### 照片

![avatar](https://github.com/dora4/dora_sporty_progress_bar/blob/main/art/dora_sporty_progress_bar.jpg)

### 动图

![avatar](https://github.com/dora4/dora_sporty_progress_bar/blob/main/art/dora_sporty_progress_bar.gif)

### 软件包

https://github.com/dora4/dora_sporty_progress_bar/blob/main/art/dora_sporty_progress_bar.apk

### 用法

```kotlin
val progressBar = findViewById<DoraSportyProgressBar>(R.id.progressBar)
        progressBar.setPercentRate(0.5f)
```

| 自定义属性              | 描述                                                         |
| ----------------------- | ------------------------------------------------------------ |
| dora_progressType       | 进度条类型，flat水平进度条，semicircleTop上半圆的进度条，semicircleBottom下半圆的进度条 |
| dora_progressWidth      | 进度条的宽度，如果为flat类型，为控件的高度，反之，则需要手动设置 |
| dora_progressBgColor    | 进度条的背景色                                               |
| dora_progressHoverColor | 进度条有效区域的颜色                                         |
| dora_animationTime      | 刷新间隔时间，需要手动调用setPercentRate进行刷新             |
