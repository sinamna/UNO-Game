import java.util.ArrayList;
import java.util.Scanner;

public class WildCard extends Card {
    //it can be normal normal card or DrawFour
    private String type;
    private String nextCardColor;

    public WildCard(String type) {
        super(50);
        this.type = type;
        nextCardColor = null;
    }

    // a method to take user input for choosing nextCardColor

    /**
     * Takes the color of next card from user
     */
    public void takeNextCardColor() {
        Scanner input = new Scanner(System.in);
        String resetColor="\u001B[0m";
        String[] colors = {"Green", "Blue", "Yellow", "Red"};
        System.out.println("Which one you Choose? ");
        int index = 1;
        for (String color : colors) {
            String textColor = color.equals("Green") ? "\u001B[92m" : color.equals("Yellow") ?
                    "\u001B[33m" : color.equals("Red") ? "\u001B[31m" : "\u001B[34m";
            System.out.printf("%s%d - %s%s\n",textColor, index, color,resetColor);
            index++;
        }
        nextCardColor = colors[input.nextInt() - 1];

    }

    /**
     *
     * @return returns the color of next Card that should be on table
     */
    public String getNextCardColor() {
        return nextCardColor;
    }

    /**
     *
     * @return  the type of WildCard (Normal or fourDraw)
     */
    public String getType() {
        return type;
    }

//    @Override
//    public void print() {
//        System.out.println("WildCard - " + this.type);
//    }

    // action -> type comes handy
    public void action(Integer playerIndex, ArrayList<Player> players) {
        if (type.equals("drawFour")) {
            this.takeNextCardColor();
            players.get((playerIndex + 2) % players.size()).setPlayTurn(true);
            for (int i = 1; i <= 4; i++) players.get((playerIndex + 1) % players.size()).takeCard();
        } else {
            this.takeNextCardColor();
            players.get((playerIndex + 1) % players.size()).setPlayTurn(true);
        }
    }

    /**
     * checks if card can be placed based on the card on the table
     * @param playerCards the list of player's cards
     * @param tableCard the card which is placed on table
     * @return true if it is possible to place the card, false if it is not
     */
    public boolean checkPlacingCondition(ArrayList<Card> playerCards, Card tableCard) {
        /*
            goes throw the cards and allows to put if no other cards could be placed on table
         */
        if (this.type.equals("normal")) return true;
        boolean canPlace = true;

        for (Card card : playerCards) {
            if (card instanceof Numerical) {
                Numerical numCard = (Numerical) card;
                if (numCard.checkPlacingCondition(tableCard))
                    canPlace = false;
            } else if (card instanceof ActionCard) {
                ActionCard actionCard = (ActionCard) card;
                if (actionCard.checkPlacingCondition(tableCard))
                    canPlace = false;
            }
        }
        return type.equals("normal") || canPlace;
    }
}
