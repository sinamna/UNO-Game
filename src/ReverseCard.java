import java.util.ArrayList;
import java.util.Collections;

public class ReverseCard extends ActionCard {
    public ReverseCard(String color){
        super(color);
    }
    //check placing condition move
    @Override
    public boolean checkPlacingCondition(Card card){
        if(card instanceof Numerical){
            Numerical numCard=(Numerical)card;
            return this.getColor().equals(numCard.getColor());
        }else if(card instanceof ActionCard){
            if(card instanceof ReverseCard) return true;
            else{
                ActionCard actionCard=(ActionCard) card;
                return actionCard.getColor().equals(this.getColor());
            }

        }else if(card instanceof WildCard){
            WildCard wildCard=(WildCard) card;
            return wildCard.getNextCardColor().equals(this.getColor());
        }
        return false;
    }
    //reverse action
    public void action(Integer playerIndex, ArrayList<Player> players){
        Player tempPlayer=players.get(playerIndex);
        Collections.reverse(players);
        playerIndex=players.indexOf(tempPlayer);
    }
}
