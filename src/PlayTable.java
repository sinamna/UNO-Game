import java.util.ArrayList;

public class PlayTable {
    ArrayList<Player> players;
    CardStorage cardStorage;
    Card cardOnTable;
    public PlayTable(CardStorage cardStorage) {
        this.cardStorage = cardStorage;
        players = new ArrayList<>();
        setInitialState();
    }

    public void addPlayer(Player player){
        players.add(player);
    }
    // a method for putting card in the middle

    // a method for printing the whole table

    // a method for placing a initial card on the table
    private void setInitialState() {
        Card cardOnTable;
        while (true) {
            cardOnTable = cardStorage.randomPicking();
            if (cardOnTable instanceof ColoredCard)
                break;
            else
                cardStorage.addToStorage(cardOnTable);
        }
        this.cardOnTable = cardOnTable;
    }
    // ending condition

    //scoreBoard

    // play game method
}
