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


    <link href="/static/node_modules/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link href="/static/node_modules/toastr/build/toastr.min.css" rel="stylesheet" type="text/css">
    <link href="/static/css/main.css" rel="stylesheet" type="text/css">

    <script src="/static/node_modules/jquery/dist/jquery.min.js"></script>
    <script src="/static/node_modules/bootstrap/dist/js/bootstrap.min.js"></script>

    <script src="/static/node_modules/swiper/dist/js/swiper.jquery.min.js"></script>
    <script src="/static/node_modules/velocity-animate/velocity.js"></script>
    <script src="/static/node_modules/toastr/build/toastr.min.js"></script>

    <script src="/static/js/common/widget/FtlPager.js"></script>
    <script src="/static/js/common/widget/FtlTab.js"></script>
    <script src="/static/js/common/widget/FtlExpanding.js"></script>


    <script src="/static/js/init.js"></script>

<@layout.block name="head"></@layout.block>

</head>
<body>

<div class="nb-app">

    <div class="section-navigation">
        <div class="container">
            <div class="row">
                <div class="col-lg-10 col-lg-offset-1 col-md-10 col-md-offset-1">
                    <a href="/">
                        <img src="/static/img/logo.png" class="logo"/>
                    </a>
                    <span class="menus">
                        <a href="/">首页</a>
                        <a href="#">关于</a>
                    </span>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="row">
            <div class="col-lg-10 col-lg-offset-1 col-md-10 col-md-offset-1">
                <@layout.block name="content"></@layout.block>
            </div>
        </div>
    </div>

</div>

</body>
</html>
