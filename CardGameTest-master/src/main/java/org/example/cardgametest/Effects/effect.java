package org.example.cardgametest.Effects;

public abstract class effect {
    private String name;

    public effect(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public abstract void cardeffect();
}
