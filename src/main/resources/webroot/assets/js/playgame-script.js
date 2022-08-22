"use strict";

/********************/
/* Global Variables */
/********************/

let eb;
let timer;
let usernames;

/**************************/
/* Data Processing Module */
/**************************/

const DATA = (function () {
    let playingFields = {P1: null, P2: null};

    let json2FieldObject = function (field) {
        return {
            grid: JSON.parse(field.grid),
            tetromino: JSON.parse(field.tetromino),
            position: JSON.parse(field.position),
            score: JSON.parse(field.score),
            event: JSON.parse(field.event),
            hero: field.hero,
            alive: field.alive
        };
    };

    let determineWinner = function () {
        function updateWins(winner) {
            eb.send("tetris-27.socket.wins", {username: winner}, (err, mes) => {
            });
        }

        let winner = "Nobody";
        if (!playingFields.P2.alive) {
            winner = usernames.P1;
        }
        else if (!playingFields.P1.alive) {
            winner = usernames.P2;
        }
        if (winner !== "Nobody") updateWins(winner);

        console.log(usernames);
        console.log(winner);

        VIEW.showPopup(winner);
    };

    return {
        jsonHandler: function (error, message) {
            function gameOver(game) {
                return !(JSON.parse(JSON.parse(game.P1).alive) && JSON.parse(JSON.parse(game.P2).alive));
            }

            function gameOverHandler() {
                clearTimeout(timer);
                VIEW.cleanAllBoards();
                determineWinner();
            }

            function setUsernames(game) {
                usernames.P1 = localStorage.getItem("username");
                usernames.P2 = JSON.parse(game.P2).user;
                VIEW.showUsernames();
            }

            if (error) throw new Error("Can't get game info from the server");
            let game = JSON.parse(message.body);
            if (usernames.P1 === "" || usernames.P2 === "") setUsernames(game);
            playingFields.P1 = json2FieldObject(JSON.parse(game.P1));
            playingFields.P2 = json2FieldObject(JSON.parse(game.P2));
            if (gameOver(game)) gameOverHandler();
            else VIEW.viewHandler(playingFields);
        }
    };
})();


/*************************/
/* Canvas Drawing Module */
/*************************/

const VIEW = (function () {

    let cleanCanvas = function (canvas) {
        let ctx = canvas.getContext("2d");
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.strokeStyle = 'black';
        ctx.lineWidth = 2;

    };

    let drawField = function (keyPlayer, playerInfo) {

        function isFilledBlock(block) {
            return block !== 0;
        }

        function generateColor(tetromino) {
            //fire, water, earth, air, neutral, wood
            let tempColorPallet = ["#ce3939", "#4bc4f4", "#c1883e", "#edfaff", "#a37fa1", "#3a281b"];
            return tempColorPallet[tetromino - 1];
        }

        function drawBlock(keyPlayer, x, y, color) {
            let c = document.getElementById(keyPlayer);
            let ctx = c.getContext("2d");

            ctx.lineWidth = 2;
            ctx.strokeStyle = "black";
            ctx.fillStyle = color;
            ctx.stroke();

            ctx.fillRect(x * 30, y * 15, 30, 15);
        }

        cleanCanvas(document.getElementById(keyPlayer));

        playerInfo.grid.forEach((row, y) => {
            row.forEach((cell, x) => {
                if (isFilledBlock(cell)) drawBlock(keyPlayer, x, y, generateColor(cell))
            })
        });


        for (let i = 0; i < playerInfo.tetromino.length; i++) {
            for (let j = 0; j < playerInfo.tetromino[i].length; j++) {
                if (playerInfo.tetromino[i][j] !== 0) drawBlock(keyPlayer, j + playerInfo.position[0], i + playerInfo.position[1], generateColor(playerInfo.tetromino[i][j]))
            }
        }
    };

    let checkEvent = function (keyPlayer, eventCode) {
        function showEventName(keyPlayer, event) {
            let doc = document.getElementById("activePower" + keyPlayer);
            doc.innerHTML = event;
        }

        function removeEventChanges(keyPlayer) {
            let doc = document.getElementById(keyPlayer);
            doc.classList.remove("blur");
        }

        function steamEvent(keyPlayer) {
            let doc = document.getElementById(keyPlayer);
            doc.classList.add("blur");
        }

        switch (eventCode) {
            case 0:
                removeEventChanges(keyPlayer);
                showEventName(keyPlayer, "No event");
                break;
            case 11:
                steamEvent(keyPlayer);
                showEventName(keyPlayer, "Steam");
                break;
            case 21:
                showEventName(keyPlayer, "Growth");
                break;
        }
    };

    let displayHero = function (keyPlayer, hero) {
        let doc = document.getElementById("hero" + keyPlayer);
        doc.innerHTML = hero;
    };

    let showCurrentScore = function (gameInfo) {
        let scoreP1Element = qs("#playerOneScore");
        let scoreP2Element = qs("#playerTwoScore");
        if (gameInfo.P1.score === "0" || gameInfo.P2.score === "0") {
            scoreP1Element.innerHTML = "0";
            scoreP2Element.innerHTML = "0";
        } else {
            scoreP1Element.innerHTML = gameInfo.P1.score;
            scoreP2Element.innerHTML = gameInfo.P2.score;
        }
    };

    let fieldViewHandler = function (keyPlayer, field) {
        checkEvent(keyPlayer, field.event);
        drawField(keyPlayer, field);
        displayHero(keyPlayer, field.hero);
    };

    return {
        viewHandler: function (playingFields) {
            fieldViewHandler("P1", playingFields.P1);
            fieldViewHandler("P2", playingFields.P2);
            showCurrentScore(playingFields);
        },
        showUsernames: function () {
            qs("#nameP1").innerHTML = usernames.P1;
            qs("#nameP2").innerHTML = usernames.P2;
        },
        showPopup: function (winner) {
            qs("#gameOverPopup").removeAttribute("class");
            let doc = document.getElementById("winner");
            doc.innerHTML = winner;
        },
        cleanAllBoards: function () {
            qsa("canvas").forEach((canvas) => cleanCanvas(canvas));
        }
    }

})();

/*****************/
/* Event Handler */
/*****************/

const PROGRAM = (function () {

    const VERTX_CONSTANTS = {
        LOCALHOST: "http://localhost:8027/tetris-27/socket",
        DROP_NATURAL: "tetris-27.socket.game",
        DROP: "tetris-27.socket.game.drop",
        MOVE: "tetris-27.socket.game.move",
        ROTATE: "tetris-27.socket.game.rotate",
        START: "tetris-27.socket.game.start",
        POWER: "tetris-27.socket.game.power",
    };

    let userInputHandler = function (ev) {

        function dropTetromino(keyPlayer) {
            eb.send(VERTX_CONSTANTS.DROP, {keyplayer: keyPlayer}, (err, message) => DATA.jsonHandler(err, message))
        }

        function moveTetromino(keyPlayer, direction) {
            eb.send(VERTX_CONSTANTS.MOVE, {
                keyplayer: keyPlayer,
                direction: direction
            }, (err, message) => DATA.jsonHandler(err, message))
        }

        function rotateTetromino(keyPlayer) {
            eb.send(VERTX_CONSTANTS.ROTATE, {keyplayer: keyPlayer}, (err, message) => DATA.jsonHandler(err, message))
        }

        function activatePower(arr) {
            eb.send(VERTX_CONSTANTS.POWER, {
                keyplayer: arr.subject,
                power: arr.power
            }, (err, message) => DATA.jsonHandler(err, message))
        }

        function checkPlayer(keycode) {
            return keycode > 41 ? "P1" : "P2";
        }

        function returnOppositePlayer(keycode) {
            if (keycode === 69) {
                console.log("E");
                return keycode < 70 ? {subject: "P2", power: "Standard"} : {subject: "P2", power: "Eruption"};
            } else if (keycode === 16) {
                console.log("SHIFT");
                return keycode < 30 ? {subject: "P1", power: "Standard"} : {subject: "P1", power: "Solidify"};
            }

        }

        function checkAction(keyCode, arrayOfKeyCodes) {
            for (let i = 0; i < arrayOfKeyCodes.length; i++)
                if (arrayOfKeyCodes[i] === keyCode) return true;
            return false;
        }

        let keycode = ev ? (ev.which ? ev.which : ev.keycode) : ev.keycode;
        //ROTATE
        if (checkAction(keycode, [90, 38])) rotateTetromino(checkPlayer(keycode));
        //DOWN
        if (checkAction(keycode, [83, 40])) dropTetromino(checkPlayer(keycode));
        //LEFT
        if (checkAction(keycode, [81, 37])) moveTetromino(checkPlayer(keycode), -1);
        //RIGHT
        if (checkAction(keycode, [68, 39])) moveTetromino(checkPlayer(keycode), +1);
        //POWER
        if (checkAction(keycode, [69, 16])) activatePower(returnOppositePlayer(keycode));
    };

    return {
        init: function () {
            function dropTetrominoNaturally() {
                eb.send(VERTX_CONSTANTS.DROP_NATURAL, {}, (err, message) => DATA.jsonHandler(err, message));
                timer = setTimeout(dropTetrominoNaturally, 1000);
            }

            function setTetromino() {
                eb.send(VERTX_CONSTANTS.START, {}, (err, message) => DATA.jsonHandler(err, message));
            }

            usernames = {P1: "", P2: ""};
            VIEW.cleanAllBoards();
            document.addEventListener("keyup", userInputHandler);
            eb = new EventBus(VERTX_CONSTANTS.LOCALHOST);
            eb.onopen = function () {
                setTetromino();
                dropTetrominoNaturally();
            };
        }
    }

})();

document.addEventListener("DOMContentLoaded", PROGRAM.init);