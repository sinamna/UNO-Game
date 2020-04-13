import java.util.ArrayList;
import java.util.Random;

public class PlayTable {
    private ArrayList<Player> players;
    private CardStorage cardStorage;
    private Card cardOnTable;
    private int drawInRow;
    private int wildDrawInRow;
    private String nextColor;
    /**
     * constructs a play table with specified card storage in it
     * @param cardStorage
     */
    public PlayTable(CardStorage cardStorage) {
        this.cardStorage = cardStorage;
        players = new ArrayList<>();
        putFirstCard();
        drawInRow=0;
        wildDrawInRow=0;
        nextColor=null;
    }
    public void setNextColor(String color){
        nextColor=color;
    }
    /**
     * increases the number of draws that came one after another
     */
    public void increaseDrawNum(){
        drawInRow++;
    }

    /**
     * increases the number of wild draws that came one after another
     */
    public void increaseWildNum(){
        wildDrawInRow++;
    }

    /**
     * set wildNum to zero
     */
    public void resetWildNum(){
        wildDrawInRow=0;
    }

    /**
     * gets the wild draw number in a row
     * @return the wildDrawInRow
     */
    public int getWildDrawInRow() {
        return wildDrawInRow;
    }

    public void resetDrawNum(){
        drawInRow=0;
    }

    public int getDrawInRow() {
        return drawInRow;
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
        if(cardOnTable instanceof ActionCard){
            ActionCard actionCard=(ActionCard) cardOnTable;
            actionCard.firstAct(turnCounter,players);
        }
        while(!endingCondition()){
            if(players.get(turnCounter).getPlayTurn()) {
                //the print method
                //players.get(turnCounter).printPlayers();
                //print desk
                //print cards
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
    public void printTable(){
        String[] cardDetails= Player.cardDetails(cardOnTable);
        String cardColor=cardDetails[3];
        String resetColor="\u001B[0m";
        for(int j=1;j<=6;j++){
            System.out.print("                    ");
            if(j==1||j==6)
                System.out.print(cardColor+"------------"+resetColor);
            else if(j==2) System.out.printf("%s| %c        |%s",cardColor,cardDetails[1].charAt(0),resetColor);
            else if(j==3) {
                System.out.printf("%s| %6s   |%s",cardColor,cardDetails[0],resetColor);
                if(this.nextColor!=null)
                    System.out.print("    next color: "+nextColor);
            }
            else if(j==5) System.out.printf("%s|%5s     |%s",cardColor,cardDetails[2],resetColor);
            else System.out.print(cardColor+"|          |"+resetColor);

            System.out.println();
        }





    }
}
