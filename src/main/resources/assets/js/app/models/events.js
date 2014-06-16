
"use strict";
APP.EventModel = Backbone.Model.extend({
  // you can set any defaults you would like here
  defaults: {
//    title: "No title",
//    distance: 0,
//    points: [],
//    owner: {
//      name: "anonymous"
//    }
  },

//  validate: function (attrs) {
//    var errors = {};
//    if (!attrs.title) errors.title = "Hey! Give this thing a title.";
//    if (!_.isEmpty(errors)) {
//      return errors;
//    }
//  },

  url: function() {
    var base = 'api/events';
//    var base = 'api/talks';
    //if (this.isNew()) return base;
    return base + (base.charAt(base.length - 1) == '/' ? '' : '/') + this.id;
  }
});

APP.EventCollection = Backbone.Collection.extend({
  // Reference to this collection's model.
  model: APP.EventModel,
  url: 'api/events',
  parse : function(resp) {
//    return resp.events
    return _.map(resp.events, function(event) {
      event.talks = new APP.TalkCollection(event.talks);
      event.lightningTalks = new APP.TalkCollection(event.lightningTalks);
      return event;
    })
  }
});
