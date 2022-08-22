"use strict";

document.addEventListener("DOMContentLoaded", init);

function init(){
    getLeaderboard();
}

let getLeaderboard = function(){
    let eb = new EventBus("http://localhost:8027/tetris-27/socket");
    eb.onopen = function(){
        eb.send("tetris-27.socket.leaderboard", {}, (err, mes) => {
            fillTableWithData(mes.body);
        });
    };
};

let fillTableWithData = function (data) {
    console.log(data);
    let rank = 0;
    let doc = document.getElementById("leaderboard");
    Object.keys(data).forEach(key => {
          let val = data[key];
          rank += 1;
          doc.innerHTML += "<tr><td>" + rank + "</td><td>" + val.username + "</td><td>" + val.wins + "</td>";
            })
        };