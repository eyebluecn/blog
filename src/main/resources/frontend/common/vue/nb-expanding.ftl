<#-- 收缩滑动 -->
<#macro NbExpanding>

<script type="text/x-template" id="nb-expanding">
    <transition
            v-on:before-enter="beforeEnter"
            v-on:enter="enter"
            v-on:leave="leave"
            v-bind:css="false">
        <slot></slot>
    </transition>
</script>
<script type="text/javascript">
    // 注册
    Vue.component('nb-expanding', {
        template: '#nb-expanding',
        data: function () {
            return {}
        },
        props: {},
        computed: {},
        watch: {},
        methods: {
            beforeEnter: function (el) {

            },
            enter: function (el, done) {

                $(el).hide();
                $(el).slideDown({duration: 377});


            },
            leave: function (el, done) {
                $(el).slideUp({duration: 377});

            }
        },
        mounted: function () {

        }
    })
</script>

</#macro>