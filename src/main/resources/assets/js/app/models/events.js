
"use strict";
APP.EventModel = Backbone.Model.extend({
  defaults: {
  },

  url: function() {
    var base = 'api/events';
    return base + (base.charAt(base.length - 1) == '/' ? '' : '/') + this.id;
  }
});

APP.EventCollection = Backbone.Collection.extend({
  model: APP.EventModel,
  url: 'api/events',
  parse : function(resp) {
    return _.map(resp.events, function(event) {
      event.talks = new APP.TalkCollection(event.talks);
      event.lightningTalks = new APP.TalkCollection(event.lightningTalks);
      return event;
    })
  }
});
