
"use strict";
APP.TweetModel = Backbone.Model.extend({
  // you can set any defaults you would like here
  defaults: {
//    title: "No title",
//    distance: 0,
//    points: [],
    user: {
      name: "@anonymous"
    }
  },

});

APP.TweetCollection = Backbone.Collection.extend({
  // Reference to this collection's model.
  model: APP.TweetModel,
  url: 'api/tweets',
  parse: function(resp) {
    return resp.tweets;
  }
});
