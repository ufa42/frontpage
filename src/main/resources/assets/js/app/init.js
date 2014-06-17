function declOfNum(number, titles) {
  var cases = [2, 0, 1, 1, 1, 2];
  number = Math.round(number);
  return titles[(number % 100 > 4 && number % 100 < 20) ? 2 : cases[(number % 10 < 5) ? number % 10 : 5]];
}

(function(){
  window.app = {el : {}, fn : {}};
  app.el['window'] = $(window);
  app.el['document'] = $(document);
  app.el['back-to-top'] = $('.back-to-top');
  app.el['html-body'] = $('html,body');
  app.el['loader'] = $('#loader');
  app.el['mask'] = $('#mask');

  var router = new APP.AppRouter({
    eventList: new APP.EventCollection(),
    tweets: new APP.TweetCollection()
  });
  router.eventList.fetch({reset: true, success: function() {
    //Backbone.history.start();
    router.index();
  }})
  setInterval(function() { router.eventList.fetch({reset: true}); }, 5000);
  setInterval(function() { if (app.el['window'].width() > 1024) router.tweets.fetch({ add: true}); }, 2000);

  $(function() {
    app.el['window'].resize(function() {
      router.onResize();
    });

    $(window).scroll(function () {
      if ($(this).scrollTop() > 500) {
        app.el['back-to-top'].fadeIn();
      } else {
        app.el['back-to-top'].fadeOut();
      }
    });
    app.el['back-to-top'].click(function () {
      app.el['html-body'].animate({
        scrollTop: 0
      }, 1000);
      return false;
    });

    $('#mobileheader').html($('#header').html());

    function heroInit() {
      jQuery('#hero').css({ height: app.el['window'].height() + "px" });
    };

    jQuery(window).on("resize", heroInit);
    jQuery(document).on("ready", heroInit);

    $('.navigation-bar').onePageNav({
      currentClass: 'active',
      changeHash: true,
      scrollSpeed: 1500,
      scrollThreshold: 0.5,
      easing: 'swing'
    });

    $('#header').waypoint('sticky', {
      wrapper: '<div class="sticky-wrapper" />',
      stuckClass: 'sticky'
    });

//    $('.fancybox').fancybox();

  });
	
})();