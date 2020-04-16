
import java.awt.*;
import java.net.BindException;
import java.util.*;
import java.lang.reflect.*;

public class Player {
    private ArrayList<Card> cards;
    private CardStorage cardStorage;
    private int playerId;
    private boolean playTurn;
    private PlayTable playTable;

    /**
     * constructs player with specified id and given storage and play table
     * @param playerId the id of the player
     * @param cardStorage the card storage play take card from
     * @param playTable the table player play on it
     */
    public Player(int playerId, CardStorage cardStorage, PlayTable playTable) {
        this.playerId = playerId;
        this.cardStorage = cardStorage;
        this.playTable = playTable;
        cards = new ArrayList<>();
        //gives player first 7 cards
        this.setInitialCards();
        playTurn = false;
    }

    /**
     * players action is done
     * @param playerTurnIndex the index of player who has to play in the list
     */
    public void playTurn(Integer playerTurnIndex) {
        System.out.println("_____________________________________________________________________________________________________");
        this.printPlayers();
        playTable.printTable(getPlayerId());
        ArrayList<Player> players = playTable.getPlayers();
        printCards();
        Card chosenCard = this.chooseCard();
        //if chosenCard is null it means a new card is added to the list
        if (chosenCard == null) {
            /*
            if newly added card can be placed on table ,player gets to choose again
            if not , turn goes to next player
             */
            if (!mustAddCard()) {
                printCards();
                // gets a card again
                Card secondChosenCard = this.chooseCard();
                this.cardAction(playTable.getPlayers(), playerTurnIndex, secondChosenCard);
            } else {
                //allow next player to play
                players.get((playerTurnIndex + 1) % players.size()).setPlayTurn(true);
            }
            // the chosen card isn't null and should act its action
        } else {
            this.cardAction(playTable.getPlayers(), playerTurnIndex, chosenCard);
        }
        // this players turn is over
        this.setPlayTurn(false);
    }


    /**
     * performs the action of the card which player had chosen
     * @param players the list of player of the game
     * @param playerTurnIndex the index of current player in the list
     * @param chosenCard the card player picked
     */
    protected void cardAction(ArrayList<Player> players, Integer playerTurnIndex, Card chosenCard) {
        /*
        for draw and wild draw cards ->first checks if nextPlayer has same card ,in that case increase
        a the count of draws or wildDraws in a row in order to use them to give extra cards in the turn of player
        who dont have one of those cards
        if next player has no similar cards -> checks if there was a row of cards in previous players ,gives this
        player extra card and takes its turn ,if not perform the action of cards
        ------------------------
        for other cards simply performs its action
         */
        Scanner input=new Scanner(System.in);
        Card cardOnTable=playTable.getCardOnTable();
        Player nextPlayer = players.get((playerTurnIndex + 1) % players.size());
        if (chosenCard instanceof ColoredCard) {
            if (chosenCard instanceof DrawCard) {
                DrawCard drawCard = (DrawCard) chosenCard;
                if (nextDrawFound(nextPlayer.getCards()) && drawCard.checkPlacingCondition(cardOnTable)) {
                    System.out.println("\u001B[96m"+"enter 'y' if u wish to add another draw on this :"+"\u001B[0m");
                    String choice=null;
                    if(this instanceof Ai){
                        choice="n";
                        System.out.println("n");
                    }else
                        choice=input.next();
                    if(choice.charAt(0)=='y'){
                        playTable.increaseDrawNum();
                        nextPlayer.setPlayTurn(true);
                    }else
                        drawCard.action(playerTurnIndex, players);
                } else {
                    if (playTable.getDrawInRow() > 0) {
                        for (int i = 1; i <= playTable.getDrawInRow(); i++)
                            for (int j = 1; j <= 2; j++)
                                nextPlayer.takeCard();
                    }
                    drawCard.action(playerTurnIndex, players);
                    playTable.resetDrawNum();
                }
            } else {
                ((ColoredCard) chosenCard).action(playerTurnIndex, players);
                //reverses the play order of the game
                if (chosenCard instanceof ReverseCard)
                    playTable.reversePlayOrder();

            }
            if (playTable.getNextColor() != null)
                playTable.resetColor();

        } else if (chosenCard instanceof WildCard) {
            WildCard wildCard=(WildCard)chosenCard;
            Player thisPlayer = players.get(playerTurnIndex);
            boolean canPlaceCard = wildCard.checkPlacingCondition(thisPlayer.getCards(), cardOnTable);
            if (nextWildDrawFound(nextPlayer.getCards()) && canPlaceCard && wildCard.getType().equals("drawFour")) {
                System.out.println("\u001B[96m"+"enter 'y' if u wish to add another Wild draw4 on this :"+"\u001B[0m");
                String choice=null;
                if(this instanceof Ai){
                    choice="n";
                    System.out.println("n");
                }else
                    choice=input.next();
                if(choice.charAt(0)=='y'){
                    playTable.increaseWildNum();
                    nextPlayer.setPlayTurn(true);
                }else{
                    wildCard.action(playerTurnIndex, players,this.getClass().getName());
                    playTable.setNextColor((wildCard.getNextCardColor()));
                }
            } else {
                if (playTable.getWildDrawInRow() > 0) {
                    for (int i = 1; i <= playTable.getWildDrawInRow(); i++)
                        for (int j = 1; j <= 4; i++)
                            nextPlayer.takeCard();
                }
                wildCard.action(playerTurnIndex, players,this.getClass().getName());
                playTable.setNextColor((wildCard.getNextCardColor()));
                playTable.resetWildNum();
            }
        }
        //removes chosen card from list and puts it on table
        playTable.putCardOnTable(chosenCard);
        cards.remove(chosenCard);
    }
    /**
     * allow player to choose a card from its list
     * @return returns the chosen card
     */
    public Card chooseCard() {
        /*
        if there are some draws or wildDraws which previous players had placed in a row , and current player has the same
        card , it controls that player only choose that specific card
        if not , player can choose any card and if card had the condition to place on table it returns the card
            and if not it prints proper error and takes another card
         */
        Scanner input = new Scanner(System.in);
        Card cardOnTable=playTable.getCardOnTable();
        if (playTable.getWildDrawInRow() > 0 || playTable.getDrawInRow() > 0) {
            System.out.printf("You must choose %s \n", playTable.getDrawInRow() > 0 ? "Draw card" : "WildCard draw4");
        } else {
            System.out.println("Which card do you choose ?");
        }
        //the loop and try-catch block handles that the card be chosen correctly
        //and prints error when its not
        while (true) {
            try {
                int cardIndex = input.nextInt();
                input.nextLine();
                // it condition which there is no proper card to be placed , player gets to choose max number to
                // add card from storage to its list
                //null value returned is handled in playTrun method
                if (cardIndex == cards.size() + 1 && this.mustAddCard()) {
                    cards.add(cardStorage.randomPicking());
                    return null;
                }
                Card chosenCard = cards.get(cardIndex - 1);
                if (chosenCard instanceof WildCard) {
                    WildCard wildcard = (WildCard) chosenCard;
                    //handles that player choose only wildDraw card in specific condition
                    //if previous player placed drawFour this player can place card even if it doesn't fit
                    // the placing condition
                    if (wildcard.getType().equals("drawFour") && playTable.getWildDrawInRow() > 0)
                        return wildcard;
                    else if (wildcard.checkPlacingCondition(cards, cardOnTable))
                        return wildcard;
                } else if (chosenCard instanceof ColoredCard) {
                    ColoredCard coloredCard = (ColoredCard) chosenCard;
                    // handles that player choose only draw card in specific condition
                    if (playTable.getDrawInRow() > 0) {
                        if (coloredCard instanceof DrawCard)
                            return coloredCard;
                    } else {
                        if (coloredCard.checkPlacingCondition(cardOnTable))
                            return coloredCard;
                    }
                }
                System.out.println("This Card can not be placed on table");

            } catch (Exception e) {
                System.out.println("Please enter the correct number");
            }
        }
    }

    /**
     * goes throw all cards and checks if there is no proper card to choose and must add a new card
     * @return returns true if there is no card to place in table , and false if there is at least one
     *      proper card
     */
    protected boolean mustAddCard() {
        Card tableCard = playTable.getCardOnTable();
        boolean mustAdd = true;
        for (Card card : cards) {
            if (card instanceof Numerical) {
                Numerical numCard = (Numerical) card;
                if (numCard.checkPlacingCondition(tableCard))
                    mustAdd = false;
            } else if (card instanceof ActionCard) {
                ActionCard actionCard = (ActionCard) card;
                if (actionCard.checkPlacingCondition(tableCard))
                    mustAdd = false;
            } else if (card instanceof WildCard) {
                WildCard wildCard = (WildCard) card;
                if (wildCard.checkPlacingCondition(cards, tableCard))
                    mustAdd = false;
            }
        }
        return mustAdd;
    }

    /**
     * checks if next player has a draw card or not
     * @param nextPlayerCards the card list of next player
     * @return returns true if next player has a draw card and false if it doesn't
     */
    protected boolean nextDrawFound(ArrayList<Card> nextPlayerCards) {
        boolean found = false;
        for (Card card : nextPlayerCards) {
            if (card instanceof DrawCard) {
                found = true;
                break;
            }
        }
        return found;
    }

    /**
     * checks if next player has a wild draw card or not
     * @param nextPlayerCards the card list of next player
     * @return returns true if next player has a wild draw card and false if it doesn't
     */
    protected boolean nextWildDrawFound(ArrayList<Card> nextPlayerCards) {
        boolean found = false;
        for (Card card : nextPlayerCards) {
            if (card instanceof WildCard && ((WildCard) card).getType().equals("drawFour")) {
                found = true;
                break;
            }
        }
        return found;
    }

    /**
     * calculates player score based on cards score
     * @return the score of player
     */
    public int getScore() {
        int playerScore = 0;
        for (Card card : cards) {
            playerScore += card.getScore();
        }
        return playerScore;
    }

    /**
     * gives player details
     * @return the string representing player and its score
     */
    @Override
    public String toString() {
        return String.format("Player %d -> %d score", playerId, getScore());
    }

    /**
     * prints the player in graphic
     */
    public void printPlayers() {
        //12 *5 characters
        ArrayList<Player> players = playTable.getPlayers();
        for (int j = 1; j <= 5; j++) {
            int index = 0;
            for (int k = 0; k < playTable.getPlayers().size() - 1; k++) {
                //moves to counter to next player to avoid printing current player details
                if (players.get(index).getPlayerId() == this.playerId)
                    index = (index + 1) % players.size();
                System.out.print("    ");//4 spaces
                //top and bottom of box
                if (j == 1)
                    System.out.print("┍━━━━━━━━━━┑");
                else if (j == 5) {
                    System.out.print("┕━━━━━━━━━━┙");
                } else {
                    if (j == 2)
                        System.out.printf("| Player%2d |", players.get(index).getPlayerId());
                    else if (j == 4) System.out.printf("| cards %2d |", players.get(index).getCards().size());
                    else System.out.print("|          |");
                }
                index++;
            }
            System.out.println();
        }
    }

    /**
     * prints the list of cards
     */
    protected void printCards() {
        System.out.println("    Players Cards :");
        String resetColor = "\u001B[0m";
        for (int j = 1; j <= 7; j++) {
            for (int i = 0; i < cards.size(); i++) {
                /*
                String [0] the type of the card
                String [1] character representing the card
                String [2] the extra info about card
                String [3] the color of the card
                 */
                String[] cardDetails = Player.cardDetails(cards.get(i));
                String cardColor = cardDetails[3];
                System.out.print("    ");
                if (j == 1)
                    System.out.print(cardColor + "┍━━━━━━━━━━┑" + resetColor);
                else if (j == 6)
                    System.out.print(cardColor + "┕━━━━━━━━━━┙" + resetColor);
                else if (j == 2)
                    System.out.printf("%s| %c        |%s", cardColor, cardDetails[1].charAt(0), resetColor);
                else if (j == 3) System.out.printf("%s| %6s   |%s", cardColor, cardDetails[0], resetColor);
                else if (j == 5) System.out.printf("%s| %5s    |%s", cardColor, cardDetails[2], resetColor);
                else if (j == 7) System.out.printf("%s ---  %d --- %s", cardColor, i + 1, resetColor);
                else if (j == 4) System.out.print(cardColor + "|          |" + resetColor);
            }
            // adds a new card-like box at the end of the list to show player the option to choose to add card
            if (mustAddCard()) {
                String textColor = "\u001B[96m";
                System.out.print("    ");
                if (j == 1)
                    System.out.print(textColor + "┍━━━━━━━━━━┑" + resetColor);
                else if (j == 6)
                    System.out.print(textColor + "┕━━━━━━━━━━┙" + resetColor);
                else if (j != 7 && j != 8)
                    System.out.printf("%s|  %4s    |%s", textColor, j == 3 ? "Take" : j == 4 ? "Card" : " ", resetColor);
                if (j == 7)
                    System.out.printf("%s ---  %d --- %s", textColor, cards.size() + 1, resetColor);
            }
            System.out.println();
        }
    }

    /**
     * creates a customized list to use for printing students details
     * @param card the card which details want to be made
     * @return returns an array of String representing card details
     */
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
                cardSymbol = '♣';
                str.append("\u001B[34m");
            } else if (((ColoredCard) card).getColor().equals("Red")) {
                cardSymbol = '♥';
                str.append("\u001B[31m");
            } else if (((ColoredCard) card).getColor().equals("Yellow")) {
                cardSymbol = '♦';
                str.append("\u001B[33m");
            } else if (((ColoredCard) card).getColor().equals("Green")) {
                cardSymbol = '♠';
                str.append("\u001B[92m");
            }
        } else {
            cardSymbol = 'W';
            str.append("\u001B[37m");
        }
        /*
                String [0] the type of the card
                String [1] character representing the card
                String [2] the extra info about card
                String [3] the color of the card
                 */
        return str.toString().replace('c', cardSymbol).split("/");
    }

    /**
     *
     * @return the id of the player
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     *
     * @return the list of cards of player
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * adding first 7 cards in first state
     */
    private void setInitialCards() {
        for (int i = 1; i <= 7; i++) {
            cards.add(cardStorage.randomPicking());
        }
    }

    /**
     * takes card from storage
     */
    public void takeCard() {
        cards.add(cardStorage.randomPicking());
    }

    /**
     *
     * @return the playTurn of the player
     */
    public boolean getPlayTurn() {
        return playTurn;
    }

    /**
     * changing the playTrun of player
     * @param playTurn the playTurn of player
     */
    public void setPlayTurn(boolean playTurn) {
        this.playTurn = playTurn;
    }

    /**
     * @return the number of cards
     */
    public int getCardNumber() {
        return cards.size();
    }

    /**
     *  gives the subClasse the storage
     * @return the storage of cards
     */
    protected CardStorage getCardStorage() {
        return cardStorage;
    }

    /**
     * gives the subClasse the playTable
     * @return the playTable
     */
    protected PlayTable getPlayTable() {
        return playTable;
    }
}
