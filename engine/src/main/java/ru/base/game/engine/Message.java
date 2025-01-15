package ru.base.game.engine;

import com.google.gson.annotations.SerializedName;

public record Message<S extends Message.Source>(@SerializedName("type") Type type, @SerializedName("source") S source) {
    public enum Type {
        LEVEL
    }

    @FunctionalInterface
    public interface Listener {
        <S extends Source> void emit(Message<S> message);
    }

    public interface Source {

    }
}
