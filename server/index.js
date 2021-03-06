var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);

var players = [];
var arrows = [];

server.listen(8080, function(){
  console.log("Server is now running. . .");
});

// set up the event that starts on a connection
// pass the socket so it's sub events can use it
io.on('connection', function(socket){
  // send the new player their own id
  socket.emit('socketID', { id: socket.id});
  var player = new player("random name", socket.id, "red", 0, 0);
  console.log("Player Connected with ID: " + socket.id);
  // let the other players know this player connected
  socket.broadcast.emit('playerConnected', player);
        // give this new socket the current other players info
        socket.emit('getPlayers', players);
        socket.emit('getArrows', arrows);
        // put this player in our array for others
        players.push(player);
  socket.on('finishConnection', function(data){
  });


  // when this socket changes it's position, let the others know
  socket.on('positionUpdate', function(positionUpdateData){
    socket.broadcast.emit('positionUpdated', positionUpdateData);
  });

  // when this socket fires an arrow, let the others know
  socket.on('firingArrow', function(arrowFiredData){
    socket.broadcast.emit('arrowFired', arrowFiredData);
    // also put arrow into the array
    var x = arrowFiredData.x;
    var y = arrowFiredData.y;
    var direction = arrowFiredData.direction;
    var velocity = arrowFiredData.velocity;
    arrows.push(new arrow(x, y, direction, velocity));
  });

  // when this socket disconnects
  socket.on('disconnect', function(){
    console.log("Player Disconnected!");
    // let the other players know this player disconnected
    socket.broadcast.emit('playerDisconnected', {id: socket.id});
    // remove this player from array
      for(var i = 0; i < players.length; i++){
        if(players[i].id == socket.id){
          players.splice(i, 1);
        }
      }
  });
});

function player(username, socket, skin, x, y){
    this.socket = socket;
    this.x = x;
    this.y = y;
    this.username = username;
    this.skin = skin;
}

function arrow(x, y, direction, velocity){
    this.x = x;
    this.y = y;
    this.direction = direction;
    this.velocity = velocity;
}