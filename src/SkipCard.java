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
}
