"use strict";

document.addEventListener("DOMContentLoaded", init);

function init(){
    console.log(localStorage.getItem("username"));
    showCorrectUsername();

}

let showCorrectUsername = function () {
    let doc = document.getElementById("nameP1");
    doc.innerHTML = localStorage.getItem("username");
    console.log("username succesfully added");
};



