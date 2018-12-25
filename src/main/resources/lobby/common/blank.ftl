<!DOCTYPE html>
<html>
<head>
    <title>

    <@layout.block name="title">
        ${preference.name}
    </@layout.block>

    </title>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="renderer" content="webkit">
    <meta name="viewport" content="user-scalable=no,width=device-width,initial-scale=1,maximum-scale=1">
    <meta name="msapplication-tap-highlight" content="no">
    <meta name="apple-mobile-web-app-capable" content="yes">

    <#if preference.faviconUrl?? && preference.faviconUrl!="">
        <link rel="shortcut icon" type="image/x-icon" href="${preference.faviconUrl}">
    <#else>
        <link rel="shortcut icon" type="image/x-icon" href="/static/img/favicon.ico">
    </#if>

    <link href="/static/fork/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link href="/static/fork/toastr/build/toastr.min.css" rel="stylesheet" type="text/css">
    <link href="/static/css/main.css" rel="stylesheet" type="text/css">

    <script src="/static/fork/jquery/dist/jquery.min.js"></script>
    <script src="/static/fork/bootstrap/dist/js/bootstrap.min.js"></script>

    <script src="/static/fork/velocity-animate/velocity.js"></script>
    <script src="/static/fork/toastr/build/toastr.min.js"></script>

    <script src="/static/js/common/widget/FtlPager.js"></script>
    <script src="/static/js/common/widget/FtlTab.js"></script>
    <script src="/static/js/common/widget/FtlExpanding.js"></script>

    <script src="/static/js/init.js"></script>

<@layout.block name="head"></@layout.block>

</head>
<body>

<@layout.block name="content"></@layout.block>
</body>
</html>
