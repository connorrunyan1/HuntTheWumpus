var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);

var players = [];

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
        // put this player in our array for others
  players.push(player);

  // when this socket changes it's position, let the others know
  socket.on('positionUpdate', function(positionUpdateData){
    socket.broadcast.emit('positionUpdated', positionUpdateData);
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

function player(id, x, y){
    this.id = id;
    this.x = x;
    this.y = y;
}
