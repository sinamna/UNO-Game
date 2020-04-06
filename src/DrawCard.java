import java.util.ArrayList;

public class DrawCard extends ActionCard {
    public DrawCard(String color){
        super(color);
    }
    //checking placing condition
    @Override
    public boolean checkPlacingCondition(Card card) {
       if(card instanceof Numerical){
           Numerical numCard=(Numerical)card;
           return this.getColor().equals(numCard.getColor());
       }else if(card instanceof ActionCard){
           if(card instanceof DrawCard)return true;
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

    //draw action
    public void action(int playerIndex,ArrayList<Player> players){
        int nextPlayerIndex=(playerIndex+1)%players.size();
        for(int i=1;i<3;i++)players.get(nextPlayerIndex).takeCard();
        players.get((playerIndex+2)%players.size()).setPlayTurn(true);
    }
}
