public class Numerical extends ColoredCard {
    private int cardNumber;

    /**
     * constructs a numerical card with specified color and number
     * @param color the color of the card
     * @param cardNumber the number of the card
     */
    public Numerical(String color, int cardNumber) {
        super(color, cardNumber);
        this.cardNumber = cardNumber;
    }

    /**
     * gets the card number
     * @return the card number
     */
    public int getCardNumber() {
        return cardNumber;
    }

    @Override
    public void print() {
        System.out.println(this.getColor()+" "+this.getCardNumber());
    }

    /**
     * checks if numerical card can be placed on table or not
     * @param card the card which is on table
     * @return returns true if numerical card could be placed on table,and false if it couldn't
     */
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
