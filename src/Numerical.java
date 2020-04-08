public class Numerical extends ColoredCard {
    private int cardNumber;

    public Numerical(String color, int cardNumber) {
        super(color, cardNumber);
        this.cardNumber = cardNumber;
    }
    //check placing condition

    public int getCardNumber() {
        return cardNumber;
    }
    @Override
    public void print() {
        System.out.println(this.getColor()+" "+this.getCardNumber());
    }
    @Override
    public boolean checkPlacingCondition(Card card) {
        if (card instanceof Numerical) {
            Numerical numCard = (Numerical) card;
            return cardNumber == numCard.getCardNumber() || numCard.getColor().equals(this.getColor());
        } else if (card instanceof WildCard) {
            WildCard wildCard = (WildCard) card;
            return this.getColor().equals(wildCard.getNextCardColor());
        } else if (card instanceof ActionCard) {
            ActionCard actionCard = (ActionCard) card;
            return this.getColor().equals(actionCard.getColor());
        }
        return false;
    }
}
