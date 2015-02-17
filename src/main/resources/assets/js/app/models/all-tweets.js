
"use strict";
APP.TweetModel = Backbone.Model.extend({
  defaults: {
    user: {
    }
  },

});

APP.TweetCollection = Backbone.Collection.extend({
  model: APP.TweetModel,
  url: 'api/all-tweets',
  parse: function(resp) {
    return resp.tweets;
  }
});
