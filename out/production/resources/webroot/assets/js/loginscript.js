"use strict";

document.addEventListener("DOMContentLoaded", init);

function init(){
    document.querySelector("form").addEventListener('submit', processLogin);
}

let eb = new EventBus("http://localhost:8027/tetris-27/socket");

let getDataFromForm = function(){
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;
    return createObj(username, password);
};

let createObj = function(username, password){
    let obj = {
        username: username,
        password: password
    };

    console.log(obj);
    return obj;

};

let processLogin = function (e) {
    e.preventDefault();
    let data = getDataFromForm();
    console.log(data);
    sendLogin(data.username, data.password);


};

let sendLogin = (username, pass) => {
    eb.send("tetris-27.socket.login", {username: username, pass: pass}, (err, rep) => {
        console.log(rep.body);
        checkLogin(rep.body, username);
    })
};

let checkLogin = function(msg, username){
    if(msg === true){
        window.location.href = "../user/welcome.html";
        localStorage.setItem("username", username);
    } else {
        showFalseLoginMessage();
    }
};

let showFalseLoginMessage = function () {
    let message = "Sorry, no login could be found for the entered details.";
    let doc = document.getElementById("falseLogin");
    doc.innerHTML = message;
};


