javascript:(function () {
    fetch('https://play.games.dmm.com/game/kancolle')
        .catch(error => {
            console.error(error);
        })
        .then(response => {
            return response.text();
        })
        .then(data => {
            let v1 = data.length;
            console.log(data);
            console.log(v1);
            let v2 = v1.getReader().read().;
            console.log(v2);
            let v3 = v2.getAttribute("src");
        })
//            window.open(url, "kancolle", "popup=1");
})();


// popup,innerWidth=920,innerHeight=630,status=false,scrollbars=false

javascript:(function () {fetch('https://play.games.dmm.com/game/kancolle').then(response => {let url = response.html.getElementById("game_frame").getAttribute("src");window.open(url, "kancolle", "popup=1");}).catch(error => {console.error(error);})})();
// https://developer.mozilla.org/ja/docs/Web/API/Window/open
