import java.util.ArrayList;
import java.util.Random;

public class PlayTable {
    private ArrayList<Player> players;
    private CardStorage cardStorage;
    private Card cardOnTable;

    public PlayTable(CardStorage cardStorage) {
        this.cardStorage = cardStorage;
        players = new ArrayList<>();
        putFirstCard();
    }
    public void addPlayer(Player player){
        players.add(player);
    }
    // a method for putting card in the middle
    public void putCardOnTable(Card cardToBePut){
        cardStorage.addToStorage(cardOnTable);
        cardOnTable=cardToBePut;
    }
    // a method for printing the whole table        KINDA GREAT IMPORTANT

    // a method for placing a initial card on the table
    private void putFirstCard() {
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
    private boolean endingCondition(){
        for(Player player:players){
            if(player.getCardNumber()==0){
                return true;
            }
        }
        return false;
    }
    //scoreBoard
    public void printScoreBoard(){
        if(players.size()!=1){
            Player minScore=players.get(0);
            for(Player player:players){
                if(player.getScore()<=minScore.getScore())
                    minScore=player;
            }
            System.out.println(minScore);
            players.remove(minScore);
            printScoreBoard();
        }
    }
    // play game method
    public void playGame(){
        Random randomGen=new Random();
        // creating random first player
        Integer turnCounter=new Integer (randomGen.nextInt(players.size()));
        players.get(turnCounter).setPlayTurn(true);
        while(!endingCondition()){
            if(players.get(turnCounter).getPlayTurn()) {
                players.get(turnCounter).playTurn(turnCounter);
            }
            turnCounter = (turnCounter + 1) % players.size();
        }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Card getCardOnTable() {
        return cardOnTable;
    }
}
