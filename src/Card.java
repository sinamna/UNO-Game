public abstract class Card {
    private int score;

    /**
     * constructs a card with specified score
     * @param score
     */
    public Card (int score){
        this.score=score;
    }

    /**
     * prints the card
     */
    public abstract void print();

    /**
     * gets the score of the card
     * @return the score of the card
     */
    public int getScore() {
        return score;
    }
}
