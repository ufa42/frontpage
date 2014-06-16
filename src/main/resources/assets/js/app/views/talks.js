
"use strict";

APP.TalkRowView = Backbone.View.extend({
  initialize: function (options) {
    this.talk = options.talk;
  },
  render: function () {
    this.$el.html(_.template($('#talkTemplate').html(), this.talk.toJSON()));
    return this;
  }
});

APP.LightningTalkRowView = Backbone.View.extend({
  initialize: function (options) {
    this.talk = options.talk;
  },
  render: function () {
    this.$el.html(_.template($('#lightningTalkTemplate').html(), this.talk.toJSON()));
    return this;
  },
});

APP.TalksView = Backbone.View.extend({
  // the constructor
  initialize: function (options) {
    // model is passed through
    this.talks = options.event.get('talks');
    this.lightningTalks = options.event.get('lightningTalks');
//    this.talks.bind('reset', this.addAll, this);
//    this.talks.bind('add', this.addOne, this);
  },

  // populate the html to the dom
  render: function () {
    this.$el.html($('#timelineTemplate').html());
    this.addAll();
    return this;
  },

  addAll: function () {
    // clear out the container each time you render index
    this.$el.find('#talks').children().remove();
    this.$el.find('#lightningTalks').children().remove();
    _.each(this.talks.models, $.proxy(this, 'addTalk'));
    _.each(this.lightningTalks.models, $.proxy(this, 'addLightningTalk'));
  },

  addTalk: function (talk) {
    var view = new APP.TalkRowView({
      talk: talk
    });
    this.$el.find("#talks").append(view.render().el);
  },

  addLightningTalk: function (talk) {
    var view = new APP.LightningTalkRowView({
      talk: talk
    });
    this.$el.find("#lightningTalks").append(view.render().el);
  }
});

