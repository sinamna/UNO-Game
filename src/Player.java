
import java.awt.*;
import java.net.BindException;
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
    public ArrayList<Card> getCards() {
        return cards;
    }
    public void playTurn(Integer playerTurnIndex) {
        this.printPlayers();
        playTable.printTable();
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
                playTable.setNextColor(((WildCard) chosenCard).getNextCardColor());
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
    public void printPlayers() {
        //12 *5 characters
        ArrayList<Player> players = playTable.getPlayers();
        for (int j = 1; j <= 5; j++) {
            int index = 0;
            for (int k = 0; k < playTable.getPlayers().size() - 1; k++) {
                if (players.get(index).getPlayerId() == this.playerId)
                    index = (index + 1) % players.size();
                System.out.print("    ");//4 spaces
                if (j == 1 || j == 5) {
                    System.out.print("------------");
                } else {
                    if (j == 2)
                        System.out.printf("| Player %d |", players.get(index).getPlayerId());
                    else if (j == 4) System.out.printf("| cards %2d |", players.get(index).getCards().size());
                    else System.out.print("|          |");
                }
                index++;
            }
            System.out.println();
        }
    }
    public static String[] cardDetails(Card card) {
        StringBuilder str = new StringBuilder();
        char cardSymbol = 0;
        if (card instanceof Numerical) {
            str.append(String.format("Number/c/%d/", ((Numerical) card).getCardNumber()));
        } else if (card instanceof SkipCard) {
            str.append("Skip/c/ /");
        } else if (card instanceof DrawCard) {
            str.append("Draw/c/ /");
        } else if (card instanceof ReverseCard) {
            str.append("Rev/c/ /");
        } else if (card instanceof WildCard) {
            if (((WildCard) card).getType().equals("normal"))
                str.append("Wild/c/ /");
            else
                str.append("Wild/c/Dfour/");
        }
        if (card instanceof ColoredCard) {
            if (((ColoredCard) card).getColor().equals("Blue")) {
                cardSymbol = 'B';
                str.append("\u001B[34m");
            } else if (((ColoredCard) card).getColor().equals("Red")) {
                cardSymbol = 'R';
                str.append("\u001B[31m");
            } else if (((ColoredCard) card).getColor().equals("Yellow")) {
                cardSymbol = 'Y';
                str.append("\u001B[33m");
            } else if (((ColoredCard) card).getColor().equals("Green")) {
                cardSymbol = 'G';
                str.append("\u001B[92m");
            }
        } else {
            cardSymbol = 'W';
            str.append("\u001B[37m");
        }
        return str.toString().replace('c', cardSymbol).split("/");
    }
}
