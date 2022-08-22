"use strict";

document.addEventListener("DOMContentLoaded", init);

function init(){
    document.querySelector("form").addEventListener('submit', processRegister);
}

let eb = new EventBus("http://localhost:8027/tetris-27/socket");

let getDataFromForm = function(){
    let username = document.getElementById("regUsername").value;
    let password = document.getElementById("pass").value;
    let confirmPass = document.getElementById("confirmPass").value;
    let email = document.getElementById("email").value;
    return createObj(username, password, confirmPass, email);
};

let createObj = function(username, password, confirmPass, email, wins){
    let obj = {
        username: username,
        password: password,
        confirmPass: confirmPass,
        email: email
    };
    console.log(obj);
    return obj;
};

let processRegister = function (e) {
    e.preventDefault();
    let data = getDataFromForm();
    console.log(data);
    sendRegister(data.username, data.password, data.confirmPass, data.email, data.wins);

};
let sendRegister = (username, pass, confirmPass, email) => {

    eb.send("tetris-27.socket.register", {username: username, pass: pass, cpass: confirmPass, email: email}, (err, rep) => {
        console.log(rep.body);
        checkRegister(rep.body, username);
    })
};

let checkRegister = function (msg, username) {
    if(msg === true){
        /*Redirect naar welcome*/
        window.location.href = "../user/welcome.html";
        showUsername(username);
    } else {
        /*Error message*/
        showFalseRegisterMessage();
    }
};

let showUsername = function(username){
    localStorage.setItem('username', username);
};

let showFalseRegisterMessage = function (e) {
    e.preventDefault();
    let message = "Something went wrong, please try again later";
    let doc = document.getElementById("falseRegister");
    doc.innerHTML = message;
};

