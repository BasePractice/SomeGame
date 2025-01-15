package ru.base.game.engine.messages;

import com.google.gson.annotations.SerializedName;
import ru.base.game.engine.Message;

public record SourceLevel(@SerializedName("width") int width,
                          @SerializedName("height") int height,
                          @SerializedName("layout") int[][] layers,
                          @SerializedName("x") int x,
                          @SerializedName("y") int y,
                          @SerializedName("viewport") int viewport,
                          @SerializedName("assets") Asset[] assets) implements Message.Source {
    public record Asset(@SerializedName("file_name") String name,
                 @SerializedName("collision") boolean collision,
                 @SerializedName("frames") int frames) {

    }
}
