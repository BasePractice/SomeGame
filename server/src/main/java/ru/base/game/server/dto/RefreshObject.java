package ru.base.game.server.dto;

import com.google.gson.annotations.SerializedName;
import ru.base.game.engine.Map;
import ru.base.game.engine.Player;

import java.io.Serializable;

public final class RefreshObject implements Serializable {
    @SerializedName("player")
    private final Player player;
    @SerializedName("width")
    private final int width;
    @SerializedName("height")
    private final int height;
    @SerializedName("layout")
    private final int[][] blocks;
    @SerializedName("items")
    private final int[][] items;
    @SerializedName("assets")
    private final Asset[] assets;

    public RefreshObject(Player player, Map.Matrix matrix) {
        this.player = player;
        this.blocks = new int[matrix.height()][matrix.width()];
        this.items = new int[matrix.height()][matrix.width()];
        this.width = matrix.width();
        this.height = matrix.height();
        for (int y = 0; y < matrix.height(); y++) {
            for (int x = 0; x < matrix.width(); x++) {
                Map.MapElement element = matrix.at(x, y);
                this.blocks[y][x] = ((Map.BlockType) Map.Layer.BLOCKS.at(element)).ordinal();
                this.items[y][x] = 0;
            }
        }
        this.assets = new Asset[]{
            new Asset("grass-short", false, 0),
            new Asset("grass-tall", false, 1),
            new Asset("bush", true, 0),
            new Asset("wave", true, 1),
        };
    }

    record Asset(@SerializedName("file_name") String name,
                 @SerializedName("collision") boolean collision,
                 @SerializedName("frames") int frames) {

    }
}
