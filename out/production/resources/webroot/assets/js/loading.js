"use strict";

document.addEventListener("DOMContentLoaded", init);

function init(){
    redirectAfterInterval();

}

let redirectAfterInterval = function () {
    var count = 8;
    setInterval(function(){
        count--;
        if (count === 0) {
            window.location = '../game/startgame.html';
        }
    },1000);
};



