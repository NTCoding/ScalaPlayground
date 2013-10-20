  $(function() {
            "use strict";
            var socket = atmosphere;
            var subsocket;

            var connectionUrl = window.location.origin + '/search'
            console.log("About to connect to: " + connectionUrl)

            var request = {
                url: connectionUrl,
                logLevel: "debug",
                trackMessageSize: true,
                transport : "websocket",
                fallbackTransport: 'long-polling'
            };

            request.onOpen = function(response) {
                console.log("Atmosphere connected with: " + response.transport)
            };

            request.onMessage = function (response) {
            	alert("Received message" + response.responseBody);
            };


            subsocket = socket.subscribe(request);

            $('#startSearch').click(function() {
                var searchRequest = $('#query').val();
                
                console.log("About to push search request data to server:" + searchRequest);
                subsocket.push(searchRequest);
                console.log("Message sent");
            });
        });
  
                  
                