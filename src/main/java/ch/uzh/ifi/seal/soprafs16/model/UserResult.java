package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.constant.CharacterType;
import ch.uzh.ifi.seal.soprafs16.constant.TreasureType;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;

@Entity
public class UserResult implements Serializable, Comparable{

    @Id
    @GeneratedValue
    @JsonView(Views.Extended.class)
    private Long id;

    @Column
    @JsonView(Views.Extended.class)
    private String username;

    @Column
    @JsonView(Views.Extended.class)
    private CharacterType characterType;

    @Column
    @JsonView(Views.Extended.class)
    private Boolean isGunslinger;

    @Column(length = 100000)
    @JsonView(Views.Extended.class)
    private HashMap<TreasureType,Integer> treasures = new HashMap();

    @Column
    @JsonView(Views.Extended.class)
    private int totalMoney;

    public UserResult (User user, boolean isGunslinger){
        username = user.getUsername();
        characterType = user.getCharacterType();

        treasures.put(TreasureType.MONEYBAG, user.getMoneybagsValue());
        treasures.put(TreasureType.DIAMOND, user.getDiamondsValue());
        treasures.put(TreasureType.CASHBOX, user.getCashboxesValue());
        this.isGunslinger = isGunslinger;

        if(isGunslinger){
            totalMoney = user.getTotalValueOfTreasures() + 1000;
        } else {
            totalMoney = user.getTotalValueOfTreasures();
        }
    }

    public UserResult(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public CharacterType getCharacterType() {
        return characterType;
    }

    public void setCharacterType(CharacterType characterType) {
        this.characterType = characterType;
    }

    public Boolean getGunslinger() {
        return isGunslinger;
    }

    public void setGunslinger(Boolean gunslinger) {
        isGunslinger = gunslinger;
    }

    public HashMap<TreasureType, Integer> getTreasures() {
        return treasures;
    }

    public void setTreasures(HashMap<TreasureType, Integer> treasures) {
        this.treasures = treasures;
    }

    public int getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(int totalMoney) {
        this.totalMoney = totalMoney;
    }

    @Override
    public int compareTo(Object o) {
        int compareTotal = ((UserResult) o).getTotalMoney();

        return compareTotal - this.totalMoney;
    }
}
