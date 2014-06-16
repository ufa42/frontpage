
"use strict";

APP.TweetRowView = Backbone.View.extend({
  initialize: function (options) {
    this.tweet = options.tweet;
  },
  render: function () {
    this.$el.html(_.template($('#tweetTemplate').html(), this.tweet.toJSON()));
    return this;
  }
});

APP.MainView = Backbone.View.extend({
  tweetsLimit: 3,

  initialize: function (options) {
    this.tweets = options.tweets;
    this.event = options.event;
    this.tweets.bind('add', this.addTweet, this);
  },

  render: function () {
    this.$el.html(_.template($('#mainTemplate').html(), this.event.toJSON()));
//    this.$el.html($('#mainTemplate').html(), this.event.toJSON());
    this.addAll();
    return this;
  },

  addAll: function () {
    this.$el.find('#tweets').find('ul').children().remove();
    _.each(this.tweets.models.slice(0, this.tweetsLimit), $.proxy(this, 'addTweet'));
  },

  addTweet: function (tweet) {
    var view = new APP.TweetRowView({
      tweet: tweet
    });
    var list = this.$el.find('#tweets').find('ul');
    if (list.children().length > this.tweetsLimit)
      list.children().slice(this.tweetsLimit).remove();
    $(view.render().el).children(":first").hide().prependTo(list).slideDown("slow");
  }
});

