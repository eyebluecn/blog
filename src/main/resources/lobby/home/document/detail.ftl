<@layout.extends name="/common/base.ftl">
    <@layout.put block="title" type="replace">
        ${document.title}
    </@layout.put>
    <@layout.put block="head" type="append">

    </@layout.put>

    <@layout.put block="content" type="replace">
        <#import "widget/detail/IndexFrame.ftl" as IndexFrame>

    <div class="page-document-detail row">
        <div>
            <div class="title">
                ${document.title}
            </div>
            <div class="digest">
                ${document.digest}
            </div>
            <div class="author">
                <div>
                    <img src="${document.user.avatarUrl}"/>
                </div>
                <div>
                    <router-link to="/user/detail">
                        ${document.user.nickname}
                    </router-link>
                </div>
            </div>
        </div>

        <@IndexFrame.IndexFrame document=document/>
    </div>

    </@layout.put>

</@layout.extends>