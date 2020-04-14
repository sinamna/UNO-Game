import java.util.ArrayList;
import java.util.Random;

public class PlayTable {
    private ArrayList<Player> players;
    private CardStorage cardStorage;
    private Card cardOnTable;
    private int drawInRow;
    private int wildDrawInRow;
    private String nextColor;
    private int playOrder;
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
        playOrder=1;
    }

    /**
     * sets the next color which should be placed on table
     * @param color the color to be set
     */
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
     * gets the number of wild draw in a row
     * @return the wildDrawInRow
     */
    public int getWildDrawInRow() {
        return wildDrawInRow;
    }

    /**
     * sets the number of draws in row to 0
     */
    public void resetDrawNum(){
        drawInRow=0;
    }

    /**
     * gets the number of wild draws which came in a row
     * @return
     */
    public int getDrawInRow() {
        return drawInRow;
    }

    /**
     * reverses the play order in the table which is
     * used while printing table details
     */
    public void reversePlayOrder() {
        if(playOrder==1)
            playOrder=2;
        else
            playOrder=1;
    }

    /**
     * adds player to the game
     * @param player
     */
    public void addPlayer(Player player){
        players.add(player);
    }

    /**
     * place a card on the table
     * @param cardToBePut the card to be placed on table
     */
    public void putCardOnTable(Card cardToBePut){
         /*
            move the previous card back to storage and place new one on top
          */
        cardStorage.addToStorage(cardOnTable);
        cardOnTable=cardToBePut;
    }

    /**
     *
     * @return next color should be placed on table
     */
    public String getNextColor() {
        return nextColor;
    }
    public void resetColor(){
        nextColor=null;
    }
    /**
     * place the first card on table when game starts
     */
    private void putFirstCard() {
        Card cardOnTable;
        /*
        handles that the card isnt wildCard
         */
        while (true) {
            cardOnTable = cardStorage.randomPicking();
            if (cardOnTable instanceof ColoredCard)
                break;
            else
                cardStorage.addToStorage(cardOnTable);
        }
        this.cardOnTable = cardOnTable;

    }

    /**
     * checks the ending condition
     * @return returns true when a player who played all its cards is found
     */
    private boolean endingCondition(){
        for(Player player:players){
            if(player.getCardNumber()==0){
                return true;
            }
        }
        return false;
    }

    /**
     * prints the players and scores in descending order
     */
    public void printScoreBoard(){
        /*
        first finds the minimum score and prints it ,then remove the player and use recursive
        way to print the newly modified player list
         */
        if(players.size()!=0){
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

    /**
     * start playing game
     */
    public void playGame(){
        Random randomGen=new Random();
        /*
        first , a player is randomly chosen to start the game
        then if the card on top is instance of action cards it will does its action
        at last the players continue playing until one of them run out of cards
         */
        Integer playerIndex=new Integer (randomGen.nextInt(players.size()));
        players.get(playerIndex).setPlayTurn(true);
        System.out.printf("%sPlayer %d starts the game %s\n","\u001B[96m",players.get(playerIndex).getPlayerId()
                ,"\u001B[0m");
        if(cardOnTable instanceof ActionCard){
            ActionCard actionCard=(ActionCard) cardOnTable;
            actionCard.firstAct(playerIndex,players);
            if(actionCard instanceof ReverseCard)this.reversePlayOrder();
        }
        while(!endingCondition()){
            if(players.get(playerIndex).getPlayTurn()) {
                players.get(playerIndex).playTurn(playerIndex);
            }
            playerIndex = (playerIndex + 1) % players.size();
        }
        System.out.println("\u001B[96m"+"GAME FINISHED ! :)))))."+"\u001B[0m");
    }

    /**
     * gets the list of players
     * @return the list of players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     *
     * @return the card placed on table
     */
    public Card getCardOnTable() {
        return cardOnTable;
    }

    /**
     * prints the table
     * @param playerId the id of the player who is currently playing
     */
    public void printTable(int playerId){
        //12*6 characters
        /*
        table has three area , first the current player status, second the card on table
        and third is the the status of the game (including remaining cards and play order and
        next color) which is horribly organized by printing each line its individual characyers
         */
        String[] cardDetails= Player.cardDetails(cardOnTable);
        String cardColor=cardDetails[3];
        String resetColor="\u001B[0m";
        for(int j=1;j<=6;j++){
            //--------------------------------------------------------------------------------------
            // printing current player status
            if(j!=3)
            {
                if(j==5){
                    System.out.print("\u001B[96m"+"     playing        "+resetColor);
                }else if(j==4){
                    System.out.print("\u001B[96m"+"       is           "+resetColor);
                }else
                    System.out.print("                    ");
            }
            else System.out.printf("%s     Player%2d       %s","\u001B[96m",playerId,resetColor);
            //--------------------------------------------------------------------------------------
            if(j==1)
                System.out.print(cardColor+"┍━━━━━━━━━━┑"+resetColor);
            else if(j==6)
                System.out.print(cardColor+"┕━━━━━━━━━━┙"+resetColor);
            else if(j==2) {
                System.out.printf("%s| %c        |%s",cardColor,cardDetails[1].charAt(0),resetColor);
                //third part
                if(this.nextColor!=null) {
                    System.out.print("    next color: " );
                }
            }
            else if(j==3) {
                //the type of card
                System.out.printf("%s| %6s   |%s",cardColor,cardDetails[0],resetColor);
                //third part
                if(nextColor!=null) {
                    String textColor = nextColor.equals("Green") ? "\u001B[92m" : nextColor.equals("Yellow") ?
                            "\u001B[33m" : nextColor.equals("Red") ? "\u001B[31m" : "\u001B[34m";
                    System.out.print(textColor + "          " + nextColor + resetColor);
                    //nextColor = null;
                }
            }
            else if(j==5) {
                //prints the extra detail of the card
                System.out.printf("%s|%5s     |%s",cardColor,cardDetails[2],resetColor);
                // third part -  prints the play order of game
                System.out.printf("    %s%s%s","\u001B[96m",playOrder==1?"Clockwise ↻":"Anti-Clockwise ↺",resetColor);
            }
            else if(j==4) {
                //body of the card
                System.out.print(cardColor + "|          |" + resetColor);
                //third part - prints the storage status
                System.out.printf("%s    Storage : %d cards %s","\u001B[96m",cardStorage.getSize(),resetColor);
            }
            //body of the card
            else System.out.print(cardColor+"|          |"+resetColor);

            System.out.println();
        }
    }
}
