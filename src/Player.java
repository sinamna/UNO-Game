
import java.util.*;

public class Player {
    private ArrayList<Card> cards;
    private CardStorage cardStorage;
    private int playerId;
    private boolean playTurn;
    private PlayTable playTable;

    public Player(int playerId, CardStorage cardStorage, PlayTable playTable) {
        this.playerId = playerId;
        this.cardStorage = cardStorage;
        this.playTable = playTable;
        cards = new ArrayList<>();
        this.setInitialCards();
        playTurn = false;
    }

    public int getPlayerId() {
        return playerId;
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
                //add sth here to determine he can choose again
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
    private void cardAction(ArrayList<Player>players,Integer playerTurnIndex,Card chosenCard){
//        printCards();
//        Card chosenCard = this.chooseCard();
        if (chosenCard instanceof ColoredCard) {
            //the action of coloredCards(except action ones) is to set next players turn boolean
            //variable as true
            ((ColoredCard) chosenCard).action(playerTurnIndex, players);
        } else if (chosenCard instanceof WildCard) {
            ((WildCard) chosenCard).action(playerTurnIndex, players);
        }
        //removes chosen card from list and puts it on table
        playTable.putCardOnTable(chosenCard);
        cards.remove(chosenCard);
    }
    // a method for choosing a card from the list
    public Card chooseCard() {
        Scanner input = new Scanner(System.in);
        System.out.println("Which card do you choose ?");
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
                    if (wildcard.checkPlacingCondition(cards, playTable.getCardOnTable()))
                        return wildcard;
                } else if (chosenCard instanceof ColoredCard) {
                    ColoredCard coloredCard = (ColoredCard) chosenCard;
                    if (coloredCard.checkPlacingCondition(playTable.getCardOnTable()))
                        return coloredCard;
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
