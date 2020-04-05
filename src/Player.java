import java.util.*;

public class Player {
    ArrayList<Card> cards;
    CardStorage cardStorage;
    private int playerId;
    public Player(int playerId,CardStorage cardStorage){
        this.playerId=playerId;
        this.cardStorage=cardStorage;
        cards=new ArrayList<>();
        this.setInitialState();
    }
    // a method for choosing a card from the list

    // a method for printing players cards

    // a method for picking seven random card from storage
    private void setInitialState(){
        for(int i=1;i<=7;i++){
            cards.add(cardStorage.randomPicking());
        }
    }
}
