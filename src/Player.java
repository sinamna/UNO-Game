
import java.util.*;

public class Player {
    private ArrayList<Card> cards;
    private CardStorage cardStorage;
    private int playerId;
    private boolean playTurn;
    private PlayTable playTable;
    private int drawsInRow;
    public Player(int playerId, CardStorage cardStorage, PlayTable playTable) {
        this.playerId = playerId;
        this.cardStorage = cardStorage;
        this.playTable = playTable;
        cards = new ArrayList<>();
        this.setInitialCards();
        playTurn = false;
        drawsInRow=0;
    }
    public int getPlayerId() {
        return playerId;
    }
    public ArrayList<Card> getCards() {
        return cards;
    }
    public void playTurn(Integer playerTurnIndex) {
        playTable.getCardOnTable().print();
        System.out.println("Player " + this.getPlayerId());
        ArrayList<Player> players = playTable.getPlayers();
        printCards();
        Card chosenCard = this.chooseCard();
        //if chosenCard is null it means a new card is added to the list
        if(chosenCard==null) {
            if(!mustAddCard()){
                printCards();
                Card secondChosenCard = this.chooseCard();
                this.cardAction(playTable.getPlayers(),playerTurnIndex,secondChosenCard);
            }else{
                players.get((playerTurnIndex+1)%players.size()).setPlayTurn(true);
            }
        }else{
            this.cardAction(playTable.getPlayers(),playerTurnIndex,chosenCard);
        }
        this.setPlayTurn(false);
    }
    private boolean nextDrawFound(ArrayList<Card>nextPlayerCards){
        boolean found=false;
        for(Card card :nextPlayerCards){
            if(card instanceof DrawCard){
                found=true;
                break;
            }
        }
        return found;
    }
    private boolean nextWildDrawFound(ArrayList<Card>nextPlayerCard){
        boolean found=false;
        for(Card card:nextPlayerCard){
            if(card instanceof WildCard && ((WildCard) card).getType().equals("drawFour")) {
                found = true;
                break;
            }
        }
        return found;
    }
    private void cardAction(ArrayList<Player>players,Integer playerTurnIndex,Card chosenCard){
        Player nextPlayer = players.get((playerTurnIndex + 1) % players.size());
        if (chosenCard instanceof ColoredCard) {
            //the action of coloredCards(except action ones) is to set next players turn boolean
            //variable as true
            if(chosenCard instanceof DrawCard){
                if(nextDrawFound(nextPlayer.getCards())&&((DrawCard) chosenCard).checkPlacingCondition(playTable.getCardOnTable())){
                    playTable.increaseDrawNum();
                    //remember to check input when draw num is >0
                    nextPlayer.setPlayTurn(true);
                }else{
                    if(playTable.getDrawInRow()>0) {
                        for (int i = 1; i <= playTable.getDrawInRow(); i++)
                            for (int j = 1; j <= 2; j++)
                                nextPlayer.takeCard();
                    }
                       DrawCard drawCard=(DrawCard) chosenCard;
                       drawCard.action(playerTurnIndex,players);
                       playTable.resetDrawNum();
                }
            }else{
                ((ColoredCard) chosenCard).action(playerTurnIndex, players);
            }
        } else if (chosenCard instanceof WildCard) {
            Player thisPlayer=players.get(playerTurnIndex);
            boolean canPlaceCard=((WildCard) chosenCard).checkPlacingCondition(thisPlayer.getCards(),playTable.getCardOnTable());
            if(nextWildDrawFound(nextPlayer.getCards())&&canPlaceCard){
                playTable.increaseWildNum();
                nextPlayer.setPlayTurn(true);
            }else{
                if(playTable.getWildDrawInRow()>0){
                    for(int i=1;i<=playTable.getWildDrawInRow();i++)
                        for(int j=1;j<=4;i++)
                            nextPlayer.takeCard();
                }
                ((WildCard) chosenCard).action(playerTurnIndex, players);
                playTable.resetWildNum();
            }
        }
        //removes chosen card from list and puts it on table
        playTable.putCardOnTable(chosenCard);
        cards.remove(chosenCard);
    }
    // a method for choosing a card from the list
    public Card chooseCard() {
        Scanner input = new Scanner(System.in);
        if(playTable.getWildDrawInRow()>0 || playTable.getDrawInRow()>0){
            System.out.printf("You must choose %s \n",playTable.getDrawInRow()>0?"Draw card":"WildCard draw4");
        }else{
            System.out.println("Which card do you choose ?");
        }
        while (true) {
            try {
                int cardIndex = input.nextInt();
                if(cardIndex==cards.size()+1&&this.mustAddCard()){
                    cards.add(cardStorage.randomPicking());
                    return null;
                }
                Card chosenCard = cards.get(cardIndex - 1);
                if (chosenCard instanceof WildCard) {
                    WildCard wildcard = (WildCard) chosenCard;
                    if(wildcard.getType().equals("drawFour")&&playTable.getWildDrawInRow()>0)
                        return wildcard;
                    else if (wildcard.checkPlacingCondition(cards, playTable.getCardOnTable()))
                        return wildcard;
                } else if (chosenCard instanceof ColoredCard) {
                    ColoredCard coloredCard = (ColoredCard) chosenCard;
                    if(playTable.getDrawInRow()>0){
                        if(chosenCard instanceof DrawCard)
                            return ((DrawCard)chosenCard);
                    }else{
                        if (coloredCard.checkPlacingCondition(playTable.getCardOnTable()))
                            return coloredCard;
                    }
                }
                System.out.println("This Card can not be placed on table");

            } catch (Exception e) {
                System.out.println("Please enter the correct number");
            }
        }
    }
    // a method for printing players cards
    private void printCards() {
        int index = 1;
        for (Card card : cards) {
            System.out.printf("%d - ", index);
            if (card instanceof ActionCard) {
                ActionCard actionCard = (ActionCard) card;
                actionCard.print();
            } else if (card instanceof Numerical) {
                Numerical numCard = (Numerical) card;
                numCard.print();
            } else if (card instanceof WildCard) {
                WildCard wildCard = (WildCard) card;
                wildCard.print();
            }
            index++;
        }
        if(mustAddCard())
            System.out.printf("%d - add card\n",index);
    }
    private boolean mustAddCard(){
        Card tableCard=playTable.getCardOnTable();
        boolean mustAdd=true;
        for(Card card :cards){
            if(card instanceof Numerical){
                Numerical numCard=(Numerical)card;
                if(numCard.checkPlacingCondition(tableCard))
                    mustAdd=false;
            }else if(card instanceof ActionCard){
                ActionCard actionCard=(ActionCard)card;
                if(actionCard.checkPlacingCondition(tableCard))
                    mustAdd=false;
            }else if(card instanceof WildCard){
                WildCard wildCard=(WildCard) card;
                if(wildCard.checkPlacingCondition(cards,tableCard))
                    mustAdd=false;
            }
        }
        return mustAdd;
    }
    // a method for picking seven random card from storage
    private void setInitialCards() {
        for (int i = 1; i <= 7; i++) {
            cards.add(cardStorage.randomPicking());
        }
    }

    public void takeCard() {
        cards.add(cardStorage.randomPicking());
    }

    public boolean getPlayTurn() {
        return playTurn;
    }

    public void setPlayTurn(boolean playTurn) {
        this.playTurn = playTurn;
    }

    public int getCardNumber() {
        return cards.size();
    }

    public int getScore() {
        int playerScore = 0;
        for (Card card : cards) {
            playerScore += card.getScore();
        }
        return playerScore;
    }

    @Override
    public String toString() {
        return String.format("Player %d -> %d score", playerId, getScore());
    }
}
