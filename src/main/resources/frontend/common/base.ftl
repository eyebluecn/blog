<!DOCTYPE html>
<html>
<head>
    <title>

    <@layout.block name="title">
        个人博客
    </@layout.block>

    </title>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="renderer" content="webkit">
    <meta name="viewport" content="user-scalable=no,width=device-width,initial-scale=1,maximum-scale=1">
    <meta name="msapplication-tap-highlight" content="no">
    <meta name="apple-mobile-web-app-capable" content="yes">

    <link rel="shortcut icon" type="image/x-icon" href="/static/favicon.ico">

    <link href="/static/css/app.css" rel="stylesheet" type="text/css">


    <script src="/static/node_modules/jquery/dist/jquery.min.js"></script>
    <script src="/static/node_modules/bootstrap/dist/js/bootstrap.min.js"></script>

    <script src="/static/node_modules/swiper/dist/js/swiper.jquery.min.js"></script>
    <script src="/static/node_modules/velocity-animate/velocity.js"></script>
    <script src="/static/node_modules/toastr/build/toastr.min.js"></script>


    <script src="/static/node_modules/vue/dist/vue.min.js"></script>

    <script src="/static/js/common/widget/NbPager.js"></script>
    <script src="/static/js/common/widget/NbTab.js"></script>
    <script src="/static/js/common/widget/NbExpanding.js"></script>


    <script src="/static/js/init.js"></script>

<@layout.block name="head"></@layout.block>

</head>
<body>

<div class="nb-app">

    <div>
        这里是菜单
    </div>
        <@layout.block name="content"></@layout.block>
</div>

</body>
</html>
