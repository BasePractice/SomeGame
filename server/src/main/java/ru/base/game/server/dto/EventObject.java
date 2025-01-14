package ru.base.game.server.dto;

import com.google.gson.annotations.SerializedName;

public record EventObject<E>(@SerializedName("type") String type, @SerializedName("data") E data) {
}
