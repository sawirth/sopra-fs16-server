package ch.uzh.ifi.seal.soprafs16.model;

import java.util.List;

/**
 * Created by Ada on 28.03.2016.
 */
public class Level {
    private List<Treasure> moneybags;
    private int diamonds;
    private int cashboxes;
    private List<Character> characters;

    public Level(List<Treasure> moneybags,int diamonds, int cashboxes){

        this.diamonds = diamonds;
        this.cashboxes = cashboxes;
    }

    public Level(){}

    public List<Treasure> getMoneybags() {
        return moneybags;
    }

    public void setMoneybags(List<Treasure> moneybags) {
        this.moneybags = moneybags;
    }

    public int getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(int diamonds) {
        this.diamonds = diamonds;
    }

    public int getCashboxes() {
        return cashboxes;
    }

    public void setCashboxes(int cashboxes) {
        this.cashboxes = cashboxes;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }


}
