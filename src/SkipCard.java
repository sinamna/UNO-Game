import java.util.ArrayList;

public class SkipCard extends ActionCard {
    public SkipCard(String color){
        super(color);
    }

    // some casting can be avoided? in times of comparing colors!       CHECK THIS PART LATER
    @Override
    public boolean checkPlacingCondition(Card card) {
        if(card instanceof Numerical){
            Numerical numCard=(Numerical)card;
            return this.getColor().equals(numCard.getColor());
        }else if(card instanceof ActionCard){
            if(card instanceof SkipCard)return true;
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
        System.out.println(this.getColor()+" Skip card");
    }
    public void action(Integer playerIndex, ArrayList<Player> players){
        System.out.printf("Player %d skipped \n",(playerIndex+1)%players.size()+1);
        players.get((playerIndex+2)%players.size()).setPlayTurn(true);
    }

    @Override
    public void firstAct(Integer playerIndex, ArrayList<Player> players) {
        players.get(playerIndex).setPlayTurn(false);
        players.get((playerIndex+1)%players.size()).setPlayTurn(true);
    }
}
