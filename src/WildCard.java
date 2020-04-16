import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class WildCard extends Card {
    //it can be normal normal card or DrawFour
    private String type;
    private String nextCardColor;

    /**
     * constructs a wildCard with specified type
     * @param type the type of wildCard which is either normal or drawFour
     */
    public WildCard(String type) {
        super(50);
        this.type = type;
        nextCardColor = null;
    }
    /**
     * chooses the next Color for cards
     */
    private void pickNextCardColor(String className) {
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
        // if ai is playing the game it will choose a random color but if an actual player is playing
        // the game it will take the color from the user
        if(className.equals("Player"))takeNextCardColor();
        else randomNextColor();
    }

    /**
     * randomly chooses the next color for AI players
     */
    private void randomNextColor(){
        String[] colors = {"Green", "Blue", "Yellow", "Red"};
        String resetColor="\u001B[0m";

        Random ranGen=new Random();
        int randomIndex=ranGen.nextInt(4);
        nextCardColor=colors[randomIndex];
        String textColor = nextCardColor.equals("Green") ? "\u001B[92m" : nextCardColor.equals("Yellow") ?
                "\u001B[33m" : nextCardColor.equals("Red") ? "\u001B[31m" : "\u001B[34m";
        System.out.println(textColor+nextCardColor+resetColor+" is chosen.");
    }

    /**
     * takes the color from user
     */
    private void takeNextCardColor(){
        Scanner input=new Scanner (System.in);
        String[] colors = {"Green", "Blue", "Yellow", "Red"};
        while (true){
            try{
                nextCardColor = colors[input.nextInt() - 1];
                break;
            }catch(Exception e){
                System.out.println("Please enter correct number : ");
            }
        }
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
     * @return the type of WildCard (Normal or fourDraw)
     */
    public String getType() {
        return type;
    }

    /**
     * does the action of wild card
     * @param playerIndex the index of player in the list
     * @param players the list of players
     * @param className the type of user who puts this card , Player or AI
     */
    public void action(Integer playerIndex, ArrayList<Player> players,String className) {
    /*
    draw four card plus setting next color gives 4 cards to the next Player and takes it turns
     */
        if (type.equals("drawFour")) {
            this.pickNextCardColor(className);
            players.get((playerIndex + 2) % players.size()).setPlayTurn(true);
            for (int i = 1; i <= 4; i++) players.get((playerIndex + 1) % players.size()).takeCard();
        } else {
            this.pickNextCardColor(className);
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
