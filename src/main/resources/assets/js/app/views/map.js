
"use strict";
APP.MapView = Backbone.View.extend({
  initialize: function (options) {
    this.event = options.event;
//    this.events.bind('reset', this.updateMap, this);
//    var domElement = this.$('#canvas-map');
    var colors = {
        red :          "#F16665",
        redDkr :       "#E25758",
        redLtr :       "#F6ACAC",
        redText :      "#D64546",

        // BLUES
        blue :         "#21ABCD",
        blueDkr :      "#42b8d7",
        blueLtr :      "#c3eef8",
        blueText :     "#19aacf",
        blueTs :       "#7BDCF5",
        blueTeal :     "#187086",

        // navys
        navy :         "#103a51",

        // slates
        slate :        "#364550",
        slateDkr :     "#2a363e",
        slateLtr :     "#3e515e",
        slateText :    "#28353e",


        // GREYS
        grey :         "#899CA9",
        greyDkr :      "#6F7F89",
        greyLtr :      "#8BA1B0",
        greyText :     "#778a99",

        // GREENS
        green :        "#3B7A57",

        // whites
        white :        "#ffffff",
        whiteLtr :     "#f0f3f6",
        whiteDkr :     "#c2d2dc"
    }

    var mapStyle = [
      {
        "stylers": [
          { "color": colors.grey }
        ]
      },{
        "featureType": "poi",
        "stylers": [
          { "visibility": "on" }
        ]
      },{
          "featureType": "poi.park",
          "stylers": [
            { "color": colors.green }
          ]
      },{
        "featureType": "transit",
        "stylers": [
          { "visibility": "off" }
        ]
      },{
        "featureType": "road",
        "elementType": "labels.icon",
        "stylers": [
          { "visibility": "off" }
        ]
      },{
        "elementType": "labels.text.fill",
        "stylers": [
          { "visibility": "off" }
        ]
      },{
        "elementType": "labels.text.stroke",
        "stylers": [
          { "visibility": "off" }
        ]
      },{
        "featureType": "administrative",
        "elementType": "labels.text.fill",
        "stylers": [
          { "visibility": "on" },
          { "color": colors.white }
        ]
      },{
        "featureType": "administrative",
        "elementType": "labels.text.stroke",
        "stylers": [
          { "visibility": "on" },
          { "color": colors.slate }
        ]
      },{
        "featureType": "landscape.man_made",
        "stylers": [
          { "visibility": "on" },
          { "color": colors.greyDkr }
        ]
      },{
        "featureType": "road",
        "elementType": "geometry.fill",
        "stylers": [
          { "color": colors.whiteDkr }
        ]
      },{
        "featureType": "water",
        "stylers": [
          { "color": colors.blue }
        ]
      },{
        "featureType": "water",
        "elementType": "labels.text.stroke",
        "stylers": [
          { "visibility": "on" },
          { "color": colors.slate }
        ]
      },{
        "featureType": "road",
        "elementType": "labels.text.fill",
        "stylers": [
          { "visibility": "on" },
          { "color": colors.slate }
        ]
      },{
        "featureType": "road",
        "elementType": "labels.text.stroke",
        "stylers": [
          { "visibility": "on" },
          { "color": colors.whiteDkr }
        ]
      }
    ];

    this.map = new google.maps.Map(document.getElementById('canvas-map'),
      {
        zoom: 16,
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        scrollwheel: false,
        panControl: false,
        mapTypeControl: false,
        streetViewControl: false,
        styles: mapStyle,
      }
    );
    this.updateMap();
  },

  render: function () {
    this.$el.html(_.template($('#mapPanelTemplate').html(), this.event.toJSON()));
    return this;
  },

  updateMap: function () {
    var talksPlace = this.event.get('talksPlace');
    var beersPlace = this.event.get('beersPlace');
    var talksLoc = new google.maps.LatLng(talksPlace.lat, talksPlace.lon);
    var beersLoc = new google.maps.LatLng(beersPlace.lat, beersPlace.lon);
    var markerTalks = new google.maps.Marker({
    	position: talksLoc,
    	map: this.map,
    	title: talksPlace.title,
      icon: 'assets/img/place/talks.png'
    });
    var markerBeers = new google.maps.Marker({
      position: beersLoc,
      map: this.map,
      title: beersPlace.title,
      icon: 'assets/img/place/beers.png'
    });
    this.center = new google.maps.LatLng((talksPlace.lat + beersPlace.lat) / 2.0, Math.min(talksPlace.lon, beersPlace.lon) - 0.007);
    this.map.setCenter(this.center);
  },

  onResize: function() {
    var center = this.map.getCenter();
    google.maps.event.trigger(this.map, "resize");
    this.map.setCenter(center);
  }
});
