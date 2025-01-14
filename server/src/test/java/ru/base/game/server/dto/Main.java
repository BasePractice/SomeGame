package ru.base.game.server.dto;

import com.google.gson.GsonBuilder;
import ru.base.game.engine.Map;
import ru.base.game.engine.Player;
import ru.base.game.engine.map.StandardMapGenerator;

public abstract class Main {
    public static void main(String[] args) {
        Map generated = new StandardMapGenerator().generate(1, 23, 23, Map.Generator.Kind.D2D);
        Map.Coordinated<Map.Matrix> matrix = generated.matrix(5, 5, 3);
        RefreshObject source = new RefreshObject(new Player(), matrix.source());
        EventObject<RefreshObject> event = new EventObject<>("map", source);
        String json = new GsonBuilder().create().toJson(event);
        System.out.println(json);
    }
}
