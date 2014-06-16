
"use strict";
APP.TalkModel = Backbone.Model.extend({
  defaults: {
  },

});

APP.TalkCollection = Backbone.Collection.extend({
  model: APP.TalkModel
});
