
"use strict";
APP.StatsView = Backbone.View.extend({
  initialize: function (options) {
    this.eventList = options.eventList;
    this.eventList.bind('reset', this.update, this);
    this.update();
  },

  update: function() {
    var event = this.eventList.models[0];
    if (typeof event == 'undefined')
      return;
    var stats = event.get('stats');
    var talksCount = event.get('talks').length;
    var lightningTalksCount = event.get('lightningTalks').length;
    var timeHrs = (60 + lightningTalksCount * 7 + 20) / 60;
    this.stats = {
      officialPartDuration: Math.round(timeHrs * 2) / 2.0,
      talksCount: talksCount,
      lightningTalksCount: lightningTalksCount,
      participantsCount: event.get('participants').length,
    };
    this.render();
  },

  render: function () {
    this.$el.html(_.template($('#statsTemplate').html(), this.stats));
    return this;
  }
});

