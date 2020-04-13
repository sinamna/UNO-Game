import java.util.ArrayList;

public class DrawCard extends ActionCard {
    public DrawCard(String color){
        super(color);
    }
    //checking placing condition

    /**
     * checks if draw card can be placed on table based on the tableCard
     * @param card the card which is on table
     * @return returns true if draw card could be placed on table
     */
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

    @Override
    public void print() {
        System.out.println(this.getColor()+" draw card");
    }

    /**
     * does the action of draw card
     * @param playerIndex the index of player holding this card in players list
     * @param players the list of players of the game
     */
    @Override
    public void action(Integer playerIndex,ArrayList<Player> players){
            int nextPlayerIndex=(playerIndex+1)%players.size();
            System.out.printf("Player %d lost its turn \n",(playerIndex+1)%players.size()+1);
            for(int i=1;i<3;i++)players.get(nextPlayerIndex).takeCard();
            players.get((playerIndex+2)%players.size()).setPlayTurn(true);

    }

    /**
     * does the action of draw card if it hits the first player of the game1
     * @param playerIndex the index of player holding this card in players list
     * @param players the list of players of the game
     */
    @Override
    public void firstAct(Integer playerIndex, ArrayList<Player> players) {
        players.get(playerIndex).setPlayTurn(false);
        System.out.printf("Player %d lost its turn \n",players.get(playerIndex).getPlayerId());
        for(int i=1;i<=2;i++)players.get(playerIndex).takeCard();
        players.get((playerIndex+1)%players.size()).setPlayTurn(true);
    }
}
