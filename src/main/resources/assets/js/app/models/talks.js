
"use strict";
APP.TalkModel = Backbone.Model.extend({
  // you can set any defaults you would like here
  defaults: {
//    title: "No title",
//    distance: 0,
//    points: [],
//    owner: {
//      name: "anonymous"
//    }
  },

});

APP.TalkCollection = Backbone.Collection.extend({
  // Reference to this collection's model.
  model: APP.TalkModel
});
