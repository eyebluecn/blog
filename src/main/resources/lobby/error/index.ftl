<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>错误页面</title>
</head>
<body>
<#if statusCode = 404>
<div>您访问的页面不存在！404 NOT FOUND!</div>
<#else>
<div>${statusCode} 您访问的页面出错了! ERROR!</div>
</#if>

</body>
</html>
