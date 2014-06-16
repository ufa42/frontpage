"use strict";
window.APP = window.APP || {};
APP.AppRouter = Backbone.Router.extend({
//  routes: {
//    "talks/new": "create",
//    "talks/index": "index",
//    "talks/:id/view": "show"
//  },

  initialize: function (options) {
    this.eventList = options.eventList;
    this.tweets = options.tweets;
  },

  index: function () {
    this.mainView = new APP.MainView({
      tweets: this.tweets,
      event: this.eventList.models[0]
    });
    this.talksView = new APP.TalksView({
      event: this.eventList.models[0]
    });
    this.mapView = new APP.MapView({
      event: this.eventList.models[0]
    });
    this.statsView = new APP.StatsView({
      eventList: this.eventList
    });
    $('#panelMain').html(this.mainView.render().el);
    $('#panelTimeline').html(this.talksView.render().el);
    $('#panelMap').html(this.mapView.render().el);
    $('#panelStats').html(this.statsView.render().el);
  },

  onResize: function () {
    this.mapView.onResize();
  }
});
