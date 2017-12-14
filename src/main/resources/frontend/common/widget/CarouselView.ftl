<div class="index-carousel-swiper">

    <div class="carousel-swiper">
        <div class="carousel-swiper-container swiper-container">
            <div class="swiper-wrapper">

            <#list officialCarousels as carousel>

                <div class="swiper-slide">

                    <div class="carousel-content" style="background-image: url('${carousel.imageUrl}');">
                        <div class="title">
                        ${carousel.title}
                        </div>
                        <div class="subtitle">
                        ${carousel.subtitle}
                        </div>
                        <div class="more">
                            <button class="btn btn-warning" data-href-blank="${carousel.url!""}">
                                了解详情
                            </button>
                        </div>

                    </div>
                </div>

            </#list>


            </div>
            <!--分页小圆点-->
            <div class="swiper-pagination"></div>
            <!-- 翻页箭头 -->
            <div class="swiper-button-prev swiper-button"></div>
            <div class="swiper-button-next swiper-button"></div>

        </div>
    </div>

</div>


<script type="text/javascript">

    $(function () {

        var swiper = new Swiper('.carousel-swiper-container', {
            //自动播放，周期6s
            autoplay: 6000,
            //速度1000
            speed: 1000,
            //水平滚动
            direction: 'horizontal',
            //循环播放
            loop: true,

            //分页小圆点
            pagination: '.swiper-pagination',
            paginationClickable: true,
            //翻页指示器的左右箭头
            nextButton: '.swiper-button-next',
            prevButton: '.swiper-button-prev',
            onSlideChangeStart: function (swiper) {
            },
            onSlideChangeEnd: function (swiper) {
            }
        });

    })


</script>