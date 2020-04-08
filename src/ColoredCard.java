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
    public void action(Integer playerIndex, ArrayList<Player> players){
        players.get((playerIndex+1)%players.size()).setPlayTurn(true);
    }
    public abstract boolean checkPlacingCondition(Card card);

    @Override
    public void print() {

    }

}
