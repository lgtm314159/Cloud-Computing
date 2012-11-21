function setupRadioPlayer1() {
  var myPlayer = $("#jquery_jplayer_1"),

    myPlayerData,

    fixFlash_mp4, // Flag: The m4a and m4v Flash player gives some old currentTime values when changed.

    fixFlash_mp4_id, // Timeout ID used with fixFlash_mp4

    ignore_timeupdate, // Flag used with fixFlash_mp4

    options = {

      ready: function (event) {

        // Hide the volume slider on mobile browsers. ie., They have no effect.

        if (event.jPlayer.status.noVolume) {

          // Add a class and then CSS rules deal with it.

          $(".jp-gui").addClass("jp-no-volume");

        }

        // Determine if Flash is being used and the mp4 media type is supplied. BTW, Supplying both mp3 and mp4 is pointless.

        fixFlash_mp4 = event.jPlayer.flash.used && /m4a|m4v/.test(event.jPlayer.options.supplied);

        // Setup the player with media.

        $(this).jPlayer("setMedia", {

           mp3: "http://www.jplayer.org/audio/mp3/Miaow-07-Bubble.mp3",

          //m4a: "http://www.jplayer.org/audio/m4a/Miaow-07-Bubble.m4a",

          //oga: "http://www.jplayer.org/audio/ogg/Miaow-07-Bubble.ogg"

        });

      },

      timeupdate: function(event) {

        if (!ignore_timeupdate) {

          myControl.progress.slider("value", event.jPlayer.status.currentPercentAbsolute);

        }

      },

      volumechange: function(event) {

        if (event.jPlayer.options.muted) {

          myControl.volume.slider("value", 0);

        } else {

          myControl.volume.slider("value", event.jPlayer.options.volume);

        }

      },

      repeat: function(event) {
        if(event.jPlayer.options.loop) {
          $(this).unbind(".jPlayerRepeat").bind($.jPlayer.event.ended + ".jPlayer.jPlayerRepeat", function() {
            $(this).jPlayer("play");
          });
        } else {
          $(this).unbind(".jPlayerRepeat");
        }
      },

      swfPath: "js/jPlayer/js",

      supplied: "mp3, m4a, oga",

      cssSelectorAncestor: "#jp_container_1",

      wmode: "window"

    },

    myControl = {

      progress: $(options.cssSelectorAncestor + " .jp-progress-slider"),

      volume: $(options.cssSelectorAncestor + " .jp-volume-slider")

    };



  // Instance jPlayer

  myPlayer.jPlayer(options);



  // A pointer to the jPlayer data object

  myPlayerData = myPlayer.data("jPlayer");



  // Define hover states of the buttons

  $('.jp-gui ul li').hover(

    function() { $(this).addClass('ui-state-hover'); },

    function() { $(this).removeClass('ui-state-hover'); }

  );



  // Create the progress slider control

  myControl.progress.slider({

    animate: "fast",

    max: 100,

    range: "min",

    step: 0.1,

    value : 0,

    slide: function(event, ui) {

      var sp = myPlayerData.status.seekPercent;

      if (sp > 0) {

        // Apply a fix to mp4 formats when the Flash is used.

        if (fixFlash_mp4) {

          ignore_timeupdate = true;

          clearTimeout(fixFlash_mp4_id);

          fixFlash_mp4_id = setTimeout(function() {

            ignore_timeupdate = false;

          },1000);

        }

        // Move the play-head to the value and factor in the seek percent.

        myPlayer.jPlayer("playHead", ui.value * (100 / sp));

      } else {

        // Create a timeout to reset this slider to zero.

        setTimeout(function() {

          myControl.progress.slider("value", 0);

        }, 0);

      }

    }

  });



  // Create the volume slider control

  myControl.volume.slider({

    animate: "fast",

    max: 1,

    range: "min",

    step: 0.01,

    value : $.jPlayer.prototype.options.volume,

    slide: function(event, ui) {

      myPlayer.jPlayer("option", "muted", false);

      myPlayer.jPlayer("option", "volume", ui.value);

    }

  });
}

function setupRadioPlayer2() {
  var myPlayer = $("#jquery_jplayer_2"),

    myPlayerData,

    fixFlash_mp4, // Flag: The m4a and m4v Flash player gives some old currentTime values when changed.

    fixFlash_mp4_id, // Timeout ID used with fixFlash_mp4

    ignore_timeupdate, // Flag used with fixFlash_mp4

    options = {

      ready: function (event) {

        // Hide the volume slider on mobile browsers. ie., They have no effect.

        if (event.jPlayer.status.noVolume) {

          // Add a class and then CSS rules deal with it.

          $(".jp-gui").addClass("jp-no-volume");

        }

        // Determine if Flash is being used and the mp4 media type is supplied. BTW, Supplying both mp3 and mp4 is pointless.

        fixFlash_mp4 = event.jPlayer.flash.used && /m4a|m4v/.test(event.jPlayer.options.supplied);

        // Setup the player with media.

        $(this).jPlayer("setMedia", {

          mp3: "http://www.jplayer.org/audio/mp3/Miaow-07-Bubble.mp3",
          //mp3: "audio/gangnam_style.mp3",

          // m4a: "http://www.jplayer.org/audio/m4a/Miaow-07-Bubble.m4a",

          //oga: "http://www.jplayer.org/audio/ogg/Miaow-07-Bubble.ogg"

        });

      },

      timeupdate: function(event) {

        if (!ignore_timeupdate) {

          myControl.progress.slider("value", event.jPlayer.status.currentPercentAbsolute);

        }

      },

      volumechange: function(event) {

        if (event.jPlayer.options.muted) {

          myControl.volume.slider("value", 0);

        } else {

          myControl.volume.slider("value", event.jPlayer.options.volume);

        }

      },

      repeat: function(event) {
        if(event.jPlayer.options.loop) {
          $(this).unbind(".jPlayerRepeat").bind($.jPlayer.event.ended + ".jPlayer.jPlayerRepeat", function() {
            $(this).jPlayer("play");
          });
        } else {
          $(this).unbind(".jPlayerRepeat");
        }
      },

      swfPath: "js/jPlayer/js",

      supplied: "mp3, oga",

      cssSelectorAncestor: "#jp_container_2",

      wmode: "window"

    },

    myControl = {

      progress: $(options.cssSelectorAncestor + " .jp-progress-slider"),

      volume: $(options.cssSelectorAncestor + " .jp-volume-slider")

    };



  // Instance jPlayer

  myPlayer.jPlayer(options);



  // A pointer to the jPlayer data object

  myPlayerData = myPlayer.data("jPlayer");



  // Define hover states of the buttons

  $('.jp-gui ul li').hover(

    function() { $(this).addClass('ui-state-hover'); },

    function() { $(this).removeClass('ui-state-hover'); }

  );



  // Create the progress slider control

  myControl.progress.slider({

    animate: "fast",

    max: 100,

    range: "min",

    step: 0.1,

    value : 0,

    slide: function(event, ui) {

      var sp = myPlayerData.status.seekPercent;

      if (sp > 0) {

        // Apply a fix to mp4 formats when the Flash is used.

        if (fixFlash_mp4) {

          ignore_timeupdate = true;

          clearTimeout(fixFlash_mp4_id);

          fixFlash_mp4_id = setTimeout(function() {

            ignore_timeupdate = false;

          },1000);

        }

        // Move the play-head to the value and factor in the seek percent.

        myPlayer.jPlayer("playHead", ui.value * (100 / sp));

      } else {

        // Create a timeout to reset this slider to zero.

        setTimeout(function() {

          myControl.progress.slider("value", 0);

        }, 0);

      }

    }

  });



  // Create the volume slider control

  myControl.volume.slider({

    animate: "fast",

    max: 1,

    range: "min",

    step: 0.01,

    value : $.jPlayer.prototype.options.volume,

    slide: function(event, ui) {

      myPlayer.jPlayer("option", "muted", false);

      myPlayer.jPlayer("option", "volume", ui.value);

    }

  });
}
function setupRadioPlayer3() {
  var myPlayer = $("#jquery_jplayer_3"),

    myPlayerData,

    fixFlash_mp4, // Flag: The m4a and m4v Flash player gives some old currentTime values when changed.

    fixFlash_mp4_id, // Timeout ID used with fixFlash_mp4

    ignore_timeupdate, // Flag used with fixFlash_mp4

    options = {

      ready: function (event) {

        // Hide the volume slider on mobile browsers. ie., They have no effect.

        if (event.jPlayer.status.noVolume) {

          // Add a class and then CSS rules deal with it.

          $(".jp-gui").addClass("jp-no-volume");

        }

        // Determine if Flash is being used and the mp4 media type is supplied. BTW, Supplying both mp3 and mp4 is pointless.

        fixFlash_mp4 = event.jPlayer.flash.used && /m4a|m4v/.test(event.jPlayer.options.supplied);

        // Setup the player with media.

        $(this).jPlayer("setMedia", {

          mp3: "http://www.jplayer.org/audio/mp3/Miaow-07-Bubble.mp3",

          //m4a: "http://www.jplayer.org/audio/m4a/Miaow-07-Bubble.m4a",

          //oga: "http://www.jplayer.org/audio/ogg/Miaow-07-Bubble.ogg"

        });

      },

      timeupdate: function(event) {

        if (!ignore_timeupdate) {

          myControl.progress.slider("value", event.jPlayer.status.currentPercentAbsolute);

        }

      },

      volumechange: function(event) {

        if (event.jPlayer.options.muted) {

          myControl.volume.slider("value", 0);

        } else {

          myControl.volume.slider("value", event.jPlayer.options.volume);

        }

      },

      repeat: function(event) {
        if(event.jPlayer.options.loop) {
          $(this).unbind(".jPlayerRepeat").bind($.jPlayer.event.ended + ".jPlayer.jPlayerRepeat", function() {
            $(this).jPlayer("play");
          });
        } else {
          $(this).unbind(".jPlayerRepeat");
        }
      },

      swfPath: "js/jPlayer/js",

      supplied: "mp3",

      cssSelectorAncestor: "#jp_container_3",

      wmode: "window"

    },

    myControl = {

      progress: $(options.cssSelectorAncestor + " .jp-progress-slider"),

      volume: $(options.cssSelectorAncestor + " .jp-volume-slider")

    };



  // Instance jPlayer

  myPlayer.jPlayer(options);



  // A pointer to the jPlayer data object

  myPlayerData = myPlayer.data("jPlayer");



  // Define hover states of the buttons

  $('.jp-gui ul li').hover(

    function() { $(this).addClass('ui-state-hover'); },

    function() { $(this).removeClass('ui-state-hover'); }

  );



  // Create the progress slider control

  myControl.progress.slider({

    animate: "fast",

    max: 100,

    range: "min",

    step: 0.1,

    value : 0,

    slide: function(event, ui) {

      var sp = myPlayerData.status.seekPercent;

      if (sp > 0) {

        // Apply a fix to mp4 formats when the Flash is used.

        if (fixFlash_mp4) {

          ignore_timeupdate = true;

          clearTimeout(fixFlash_mp4_id);

          fixFlash_mp4_id = setTimeout(function() {

            ignore_timeupdate = false;

          },1000);

        }

        // Move the play-head to the value and factor in the seek percent.

        myPlayer.jPlayer("playHead", ui.value * (100 / sp));

      } else {

        // Create a timeout to reset this slider to zero.

        setTimeout(function() {

          myControl.progress.slider("value", 0);

        }, 0);

      }

    }

  });



  // Create the volume slider control

  myControl.volume.slider({

    animate: "fast",

    max: 1,

    range: "min",

    step: 0.01,

    value : $.jPlayer.prototype.options.volume,

    slide: function(event, ui) {

      myPlayer.jPlayer("option", "muted", false);

      myPlayer.jPlayer("option", "volume", ui.value);

    }

  });
}
function setupRadioPlayer4() {
  var myPlayer = $("#jquery_jplayer_4"),

    myPlayerData,

    fixFlash_mp4, // Flag: The m4a and m4v Flash player gives some old currentTime values when changed.

    fixFlash_mp4_id, // Timeout ID used with fixFlash_mp4

    ignore_timeupdate, // Flag used with fixFlash_mp4

    options = {

      ready: function (event) {

        // Hide the volume slider on mobile browsers. ie., They have no effect.

        if (event.jPlayer.status.noVolume) {

          // Add a class and then CSS rules deal with it.

          $(".jp-gui").addClass("jp-no-volume");

        }

        // Determine if Flash is being used and the mp4 media type is supplied. BTW, Supplying both mp3 and mp4 is pointless.

        fixFlash_mp4 = event.jPlayer.flash.used && /m4a|m4v/.test(event.jPlayer.options.supplied);

        // Setup the player with media.

        $(this).jPlayer("setMedia", {

          mp3: "http://www.jplayer.org/audio/mp3/Miaow-07-Bubble.mp3",

          //m4a: "http://www.jplayer.org/audio/m4a/Miaow-07-Bubble.m4a",

          //oga: "http://www.jplayer.org/audio/ogg/Miaow-07-Bubble.ogg"

        });

      },

      timeupdate: function(event) {

        if (!ignore_timeupdate) {

          myControl.progress.slider("value", event.jPlayer.status.currentPercentAbsolute);

        }

      },

      volumechange: function(event) {

        if (event.jPlayer.options.muted) {

          myControl.volume.slider("value", 0);

        } else {

          myControl.volume.slider("value", event.jPlayer.options.volume);

        }

      },

      repeat: function(event) {
        if(event.jPlayer.options.loop) {
          $(this).unbind(".jPlayerRepeat").bind($.jPlayer.event.ended + ".jPlayer.jPlayerRepeat", function() {
            $(this).jPlayer("play");
          });
        } else {
          $(this).unbind(".jPlayerRepeat");
        }
      },

      swfPath: "js/jPlayer/js",

      supplied: "mp3, m4a, oga",

      cssSelectorAncestor: "#jp_container_4",

      wmode: "window"

    },

    myControl = {

      progress: $(options.cssSelectorAncestor + " .jp-progress-slider"),

      volume: $(options.cssSelectorAncestor + " .jp-volume-slider")

    };



  // Instance jPlayer

  myPlayer.jPlayer(options);



  // A pointer to the jPlayer data object

  myPlayerData = myPlayer.data("jPlayer");



  // Define hover states of the buttons

  $('.jp-gui ul li').hover(

    function() { $(this).addClass('ui-state-hover'); },

    function() { $(this).removeClass('ui-state-hover'); }

  );



  // Create the progress slider control

  myControl.progress.slider({

    animate: "fast",

    max: 100,

    range: "min",

    step: 0.1,

    value : 0,

    slide: function(event, ui) {

      var sp = myPlayerData.status.seekPercent;

      if (sp > 0) {

        // Apply a fix to mp4 formats when the Flash is used.

        if (fixFlash_mp4) {

          ignore_timeupdate = true;

          clearTimeout(fixFlash_mp4_id);

          fixFlash_mp4_id = setTimeout(function() {

            ignore_timeupdate = false;

          },1000);

        }

        // Move the play-head to the value and factor in the seek percent.

        myPlayer.jPlayer("playHead", ui.value * (100 / sp));

      } else {

        // Create a timeout to reset this slider to zero.

        setTimeout(function() {

          myControl.progress.slider("value", 0);

        }, 0);

      }

    }

  });



  // Create the volume slider control

  myControl.volume.slider({

    animate: "fast",

    max: 1,

    range: "min",

    step: 0.01,

    value : $.jPlayer.prototype.options.volume,

    slide: function(event, ui) {

      myPlayer.jPlayer("option", "muted", false);

      myPlayer.jPlayer("option", "volume", ui.value);

    }

  });
}
function setupRadioPlayer5() {
  var myPlayer = $("#jquery_jplayer_5"),

    myPlayerData,

    fixFlash_mp4, // Flag: The m4a and m4v Flash player gives some old currentTime values when changed.

    fixFlash_mp4_id, // Timeout ID used with fixFlash_mp4

    ignore_timeupdate, // Flag used with fixFlash_mp4

    options = {

      ready: function (event) {

        // Hide the volume slider on mobile browsers. ie., They have no effect.

        if (event.jPlayer.status.noVolume) {

          // Add a class and then CSS rules deal with it.

          $(".jp-gui").addClass("jp-no-volume");

        }

        // Determine if Flash is being used and the mp4 media type is supplied. BTW, Supplying both mp3 and mp4 is pointless.

        fixFlash_mp4 = event.jPlayer.flash.used && /m4a|m4v/.test(event.jPlayer.options.supplied);

        // Setup the player with media.

        $(this).jPlayer("setMedia", {

          mp3: "http://www.jplayer.org/audio/mp3/Miaow-07-Bubble.mp3",

          //m4a: "http://www.jplayer.org/audio/m4a/Miaow-07-Bubble.m4a",

          //oga: "http://www.jplayer.org/audio/ogg/Miaow-07-Bubble.ogg"

        });

      },

      timeupdate: function(event) {

        if (!ignore_timeupdate) {

          myControl.progress.slider("value", event.jPlayer.status.currentPercentAbsolute);

        }

      },

      volumechange: function(event) {

        if (event.jPlayer.options.muted) {

          myControl.volume.slider("value", 0);

        } else {

          myControl.volume.slider("value", event.jPlayer.options.volume);

        }

      },

      repeat: function(event) {
        if(event.jPlayer.options.loop) {
          $(this).unbind(".jPlayerRepeat").bind($.jPlayer.event.ended + ".jPlayer.jPlayerRepeat", function() {
            $(this).jPlayer("play");
          });
        } else {
          $(this).unbind(".jPlayerRepeat");
        }
      },

      swfPath: "js/jPlayer/js",

      supplied: "mp3, m4a, oga",

      cssSelectorAncestor: "#jp_container_5",

      wmode: "window"

    },

    myControl = {

      progress: $(options.cssSelectorAncestor + " .jp-progress-slider"),

      volume: $(options.cssSelectorAncestor + " .jp-volume-slider")

    };



  // Instance jPlayer

  myPlayer.jPlayer(options);



  // A pointer to the jPlayer data object

  myPlayerData = myPlayer.data("jPlayer");



  // Define hover states of the buttons

  $('.jp-gui ul li').hover(

    function() { $(this).addClass('ui-state-hover'); },

    function() { $(this).removeClass('ui-state-hover'); }

  );



  // Create the progress slider control

  myControl.progress.slider({

    animate: "fast",

    max: 100,

    range: "min",

    step: 0.1,

    value : 0,

    slide: function(event, ui) {

      var sp = myPlayerData.status.seekPercent;

      if (sp > 0) {

        // Apply a fix to mp4 formats when the Flash is used.

        if (fixFlash_mp4) {

          ignore_timeupdate = true;

          clearTimeout(fixFlash_mp4_id);

          fixFlash_mp4_id = setTimeout(function() {

            ignore_timeupdate = false;

          },1000);

        }

        // Move the play-head to the value and factor in the seek percent.

        myPlayer.jPlayer("playHead", ui.value * (100 / sp));

      } else {

        // Create a timeout to reset this slider to zero.

        setTimeout(function() {

          myControl.progress.slider("value", 0);

        }, 0);

      }

    }

  });



  // Create the volume slider control

  myControl.volume.slider({

    animate: "fast",

    max: 1,

    range: "min",

    step: 0.01,

    value : $.jPlayer.prototype.options.volume,

    slide: function(event, ui) {

      myPlayer.jPlayer("option", "muted", false);

      myPlayer.jPlayer("option", "volume", ui.value);

    }

  });
}
