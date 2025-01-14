function OnWebSocket() {
    let conn;

    let connected = document.getElementById("connected");
    window.connected = false;
    if (window["WebSocket"]) {
        conn = new WebSocket("ws://" + document.location.host + "/ws");
        conn.onclose = function (event) {
            window.connected = false;
            connected.classList.replace("text-connected", "text-disconnected");
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
                if (output.type === "refresh") {
                    map.refresh(output.data);
                    player.pos.x = output.data.player.x;
                    player.pos.y = output.data.player.y;
                }
                console.log(output);
            }
        };
        conn.onopen = function (event) {
            window.connected = true;
            connected.classList.replace("text-disconnected", "text-connected");
            connected.innerText = "Connected";
        }
    } else {
        console.debug("Your browser does not support WebSockets.");
    }
};
