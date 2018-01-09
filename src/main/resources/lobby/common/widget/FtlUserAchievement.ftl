<#-- 标签样式 -->
<#macro FtlUserAchievement user>
<div>
    <div class="text-center">
        <div>
        <#if user.avatarUrl?? && user.avatarUrl!="">
            <a href="${user.avatarUrl}" target="_blank">
                <img class="img-circle w100 h100" src="${user.avatarUrl}"/>
            </a>
        <#else>
            <a href="/static/img/avatar.png" target="_blank">
                <img class="img-circle w100 h100" src="/static/img/avatar.png"/>
            </a>
        </#if>

        </div>
        <div class="f18 bold mv15">
            ${user.username}
        </div>

    </div>

    <div>

        <div class="achievement-cell">
            <i class="fa fa-pencil-square"></i>
            <span>撰写了 ${user.articleNum} 篇文章</span>
        </div>

        <div class="achievement-cell">
            <i class="fa fa-font"></i>
            <span>总共有 ${user.articleWords} 字</span>
        </div>

        <div class="achievement-cell">
            <i class="fa fa-book"></i>
            <span>被阅读 ${user.articleHit} 次</span>
        </div>

        <div class="achievement-cell">
            <i class="fa fa-thumbs-o-up"></i>
            <span>获得了 ${user.articleAgreeNum} 次赞</span>
        </div>

        <div class="achievement-cell">
            <i class="fa fa-commenting-o"></i>
            <span>共收到 ${user.commentNum} 条评论</span>
        </div>


    </div>
</div>
</#macro>