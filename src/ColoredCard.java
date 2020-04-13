import java.util.ArrayList;

public abstract class ColoredCard extends Card {
    private String color;
    public ColoredCard(String color,int score){
        super(score);
        this.color=color;
    }
    public String getColor() {
        return color;
    }

    /**
     * allows next player to play its turn
     * @param playerIndex the index of player in the players list of game table
     * @param players the list of players of game table
     */
    public void action(Integer playerIndex, ArrayList<Player> players){
        players.get((playerIndex+1)%players.size()).setPlayTurn(true);
    }

    /**
     * checks if card can be placed on table or not
     * @param card the card which is on table
     * @return returns true if it is possible to place card on table
     */
    public abstract boolean checkPlacingCondition(Card card);

    /**
     * prints the card
     */
    @Override
    public abstract void print();

}
