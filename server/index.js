var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);

// ===============================================
// The code in this block is for object prototypes
function Room(theHostID, theName, thePass){
    // stuff around here is like the constructor
    this.status = "LOBBY"; // possible statuses are LOBBY, PLAYING
    this.roomName = theName;
    this.roomPass = thePass;
    this.hostID = theHostID;
    this.playerArray = new Array();
    // when you make a new room, immediately add the host player
    playerArray[theHostID] = new PlayerRecord(id);

    // stuff down here is like methods
    // use this as a shortcut to send messages
    this.sendMessage = function(type, payload){
        for(var p in playerArray){
            p.socket.emit(type, payload);
        }
    }

    this.addPlayerRecord(newPlayerRecord){
        playerArray[newPlayerRecord.socket.id] = newPlayerRecord;
    }

    this.start = function(){
        this.status = "PLAYING";
        // configure the sockets with the new play event listeners
        for(var p in playerArray){
            p.socket.on('playerDataUpdate', playerData){
                sendMessage('playerDataUpdate', playerData);
            };
            // todo add many more events, arrows etc
        }
        // tell players its starting time
        sendMessage("starting", "null?");
    }
}

// this object contains all the info about a single player
// including a reference to the smaller playerdata object
// meant to be used for repeated in-game data transfer.
function PlayerRecord(theUsername, theSocket){
    this.socket = theSocket;
    this.username = username;
}
// ===============================================

var rooms = [];

server.listen(8080, function(){
    console.log("Server is now running. . .");
});

// set up the event that starts on a connection
// pass the socket so it's sub events can use it
io.on('connection', function(socket){
    console.log("Player Connected");
    // send the new player their own id
    socket.emit('socketID', { id: socket.id});

    // this event waits on the player trying to join a room
    // make sure when the client calls this event they send
    // the properly formatted room joining data
    socket.on('joinRoomAttempt', function(joinRoomData){
        var username = joinRoomData.username;
        var roomToJoinHostID = joinRoomData.hostID;
        var newPlayerRecord = new PlayerRecord(username, socket);
        // let the player object keep a reference to its socket
        rooms[roomToJoinHostID].addPlayerRecord(newPlayerRecord);
    });

    // simultaneously make a room and join it
    socket.on("makeRoomAttempt", function(makeRoomData){
        var name = makeRoomData.name;
        var pass = makeRoomData.password;
        rooms.push(new Room(socket.id, name, pass));
        var username = makeRoomData.username;
        var newPlayerRecord = new PlayerRecord(username, socket);
        rooms[socket.id].addPlayerRecord(newPlayerRecord);
    });

    socket.('refreshRoomList', function(){
        socket.emit('roomsRefreshed', rooms);
        // TODO does this even work lmao
    });

    socket.on('disconnect', function(){
        console.log("Player Disconnected!");
    });
});

/* TODO
 plans for tomorrow
  - work out full login path

*/