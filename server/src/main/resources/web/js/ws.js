window.onload = function () {
    let conn;

    let connected = document.getElementById("connected");
    window.connected = false;
    if (window["WebSocket"]) {
        conn = new WebSocket("ws://" + document.location.host + "/ws");
        conn.onclose = function (event) {
            window.connected = false;
            connected.style.color = "#CC0000";
            connected.innerText = "Disconnected";
        };
        conn.onmessage = function (event) {
            const messages = event.data.split('\n');
            for (let i = 0; i < messages.length; i++) {
                let output = undefined;
                try {
                    output = JSON.parse(messages[i]);
                } catch (error) {
                    console.error(messages[i]);
                    console.error(error);
                    return;
                }
                console.log(output);
            }
        };
        conn.onopen = function (event) {
            window.connected = true;
            connected.style.color = "green";
            connected.innerText = "Connected";
        }
    } else {
        console.debug("Your browser does not support WebSockets.");
    }
};
