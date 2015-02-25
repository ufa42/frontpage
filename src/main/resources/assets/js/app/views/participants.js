
"use strict";

APP.ParticipantRowView = Backbone.View.extend({
  initialize: function (options) {
    this.participant = options.participant;
  },
  render: function () {
    this.$el.html(_.template($('#participantTemplate').html(), this.participant));
    return this;
  }
});

APP.ParticipantsView = Backbone.View.extend({
  initialize: function (options) {
    this.participants = options.participants;
  },

  render: function () {
    this.$el.html($('#participantsTemplate').html());
    this.addAll();
    return this;
  },

  addAll: function () {
    this.$el.find('#participantList').children().remove();
    _.each(this.participants, $.proxy(this, 'addParticipant'));
  },

  addParticipant: function (participant) {
    var view = new APP.ParticipantRowView({
      participant: participant
    });
    this.$el.find("ul").append(view.render().$el.find("li"));
  }
});

