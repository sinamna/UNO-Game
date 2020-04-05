public class ActionCard extends ColoredCard{
    public ActionCard(String color){
        super(color,20);
    }

    /*@Override
    public boolean checkPlacingCondition(Card card) {
        if(card instanceof Numerical){
            Numerical numCard=(Numerical)card;
            return this.getColor().equals(numCard.getColor());
        }else if(card instanceof ActionCard){
            ActionCard actionCard=(ActionCard) card;
            return actionCard.getColor().equals(this.getColor());
        }else if(card instanceof WildCard){
            WildCard wildCard=(WildCard) card;
            return wildCard.getNextCardColor().equals(this.getColor());
        }
        return false;
    }*/
}
