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
    public void takeNextCardColor() {
        Scanner input = new Scanner(System.in);
        String[] colors = {"Green", "Blue", "Yellow", "Red"};
        System.out.println("Which one you Choose? ");
        int index = 1;
        for (String color : colors) {
            System.out.printf("%d - %s\n", index, color);
            index++;
        }
        nextCardColor = colors[input.nextInt() - 1];
    }

    public String getNextCardColor() {
        return nextCardColor;
    }

    @Override
    public void print() {
        System.out.println("WildCard - " + this.type);
    }

    // action -> type comes handy
    public void action(Integer playerIndex, ArrayList<Player> players) {
        if (type.equals("drawFour")) {
            this.takeNextCardColor();
            players.get((playerIndex + 2) % players.size()).setPlayTurn(true);
            for (int i = 1; i <= 4; i++) players.get((playerIndex + 2) % players.size()).takeCard();
        } else {
            this.takeNextCardColor();
            players.get((playerIndex + 1) % players.size()).setPlayTurn(true);
        }
    }

    public boolean checkPlacingCondition(ArrayList<Card> playerCards, Card tableCard) {
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
        return canPlace;
    }
}
