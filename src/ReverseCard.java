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
    @Override
    public void print() {
        System.out.println(this.getColor()+" Reverse card");
    }
    //reverse action
    @Override
    public void action(Integer playerIndex, ArrayList<Player> players){
        Player tempPlayer=players.get(playerIndex);
        Collections.reverse(players);
        playerIndex=players.indexOf(tempPlayer);
        players.get((playerIndex+1)%players.size()).setPlayTurn(true);
    }

    @Override
    public void firstAct(Integer playerIndex, ArrayList<Player> players) {
        players.get(playerIndex).setPlayTurn(false);
        Player tempPlayer=players.get(playerIndex);
        Collections.reverse(players);
        playerIndex=players.indexOf(tempPlayer);
        players.get((playerIndex+1)%players.size()).setPlayTurn(true);
    }
}
