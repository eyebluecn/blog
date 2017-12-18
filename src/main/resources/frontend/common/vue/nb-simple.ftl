<#-- 分页 -->
<#macro NbSimple>

<script type="text/x-template" id="nb-simple">
    <div>
        <h1>我只是一个非常简单的</h1>
        <h2>{{name}}</h2>
    </div>
</script>

<script type="text/javascript">
    // 注册
    Vue.component('nb-simple', {
        template: '#nb-simple',
        data: function () {
            return {

                name: "你好啊！"
            }
        },
        props: {},
        computed: {},
        watch: {},
        methods: {},
        mounted: function () {

            console.log("到底有没有消息啊？")

        }
    })

</script>

</#macro>