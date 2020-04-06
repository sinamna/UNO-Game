import java.util.*;

public class Player {
    ArrayList<Card> cards;
    CardStorage cardStorage;
    private int playerId;
    private boolean playTurn;
    public Player(int playerId,CardStorage cardStorage){
        this.playerId=playerId;
        this.cardStorage=cardStorage;
        cards=new ArrayList<>();
        this.setInitialCards();
        playTurn=false;
    }
    public void playTurn(Integer playerTurnIndex,ArrayList<Player> players){



        // dont remember to change playtrun false at the end;
    }
    // a method for choosing a card from the list
    public int chooseCard(){
        Scanner input=new Scanner(System.in);
        System.out.println("Which card do you choose ?");
        int cardChosen;
        while(true){
            try{
                cardChosen=input.nextInt();
                if(cardChosen-1>=0&&cardChosen-1<cards.size())
                    break;
                else
                    System.out.println("Please enter card");
            }catch (Exception e){
                System.out.println("Please enter a number");
            }
        }
        return cardChosen;
    }
    // a method for printing players cards
    private void printCards(){
        //for
    }
    // a method for picking seven random card from storage
    private void setInitialCards(){
        for(int i=1;i<=7;i++){
            cards.add(cardStorage.randomPicking());
        }
    }
    public void takeCard(){
        cards.add(cardStorage.randomPicking());
    }
    public boolean getPlayTurn(){
        return playTurn;
    }
    public void setPlayTurn(boolean playTurn){
        this.playTurn=playTurn;
    }
    public int getCardNumber() {
        return cards.size();
    }
    public int getScore(){
        int playerScore=0;
        for(Card card:cards){
            playerScore+=card.getScore();
        }
        return playerScore;
    }
    @Override
    public String toString() {
        return String.format("Player %d -> %d score",playerId,getScore());
    }
}
