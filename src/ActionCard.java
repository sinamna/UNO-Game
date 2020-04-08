import java.util.ArrayList;

public abstract class ActionCard extends ColoredCard{
    public ActionCard(String color){
        super(color,20);
    }
    //draw action
    public abstract boolean checkPlacingCondition(Card card);
    public abstract void action(Integer playerIndex, ArrayList<Player> players);
    public abstract void firstAct(Integer playerIndex,ArrayList<Player>players);
}
