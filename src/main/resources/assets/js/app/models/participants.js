
"use strict";
APP.ParticipantModel = Backbone.Model.extend({
  defaults: {
//    user: {
//    }
  },

});

APP.ParticipantCollection = Backbone.Collection.extend({
  model: APP.ParticipantModel,
  url: 'api/participants',
  parse: function(resp) {
    return resp.participants;
  }
});
