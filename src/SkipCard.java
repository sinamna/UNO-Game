import java.util.ArrayList;

public class SkipCard extends ActionCard {
    /**
     * constructs a skip card with specified color
     * @param color the color of the card
     */
    public SkipCard(String color){
        super(color);
    }

    /**
     * checks if skip card can be placed on table based on the tableCard
     * @param card the card which is on table
     * @return returns true if skip card could be placed on table
     */
    @Override
    public boolean checkPlacingCondition(Card card) {
        if(card instanceof Numerical){
            return this.getColor().equals(((Numerical)card).getColor());
        }else if(card instanceof ActionCard){
            if(card instanceof SkipCard)return true;
            else{
                ActionCard actionCard=(ActionCard) card;
                return actionCard.getColor().equals(this.getColor());
            }
        }else if(card instanceof WildCard){
            WildCard wildCard=(WildCard) card;
            return wildCard.getNextCardColor()==null?true:wildCard.getNextCardColor().equals(this.getColor());
        }
        return false;
    }
    /**
     * prints the
     */
//    @Override
//    public void print() {
//        System.out.println(this.getColor()+" Skip card");
//    }

    /**
     * does the action of skip card
     * @param playerIndex the index of player holding this card in players list
     * @param players the list of players of the game
     */
    public void action(Integer playerIndex, ArrayList<Player> players){
        String resetColor = "\u001B[0m";
        String textColor="\u001B[96m";
        System.out.printf("%sPlayer %d skipped%s \n",textColor,players.get((playerIndex+1)%players.size()).getPlayerId(),resetColor);
        players.get((playerIndex+2)%players.size()).setPlayTurn(true);
    }
    /**
     * does the action of skip card if it hits the first player starts the game
     * @param playerIndex the index of player holding this card in players list
     * @param players the list of players of the game
     */
    @Override
    public void firstAct(Integer playerIndex, ArrayList<Player> players) {
        players.get(playerIndex).setPlayTurn(false);
        players.get((playerIndex+1)%players.size()).setPlayTurn(true);
    }
}
