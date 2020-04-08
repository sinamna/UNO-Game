public abstract class Card {
    private int score;
    public Card (int score){
        this.score=score;
    }

    public abstract boolean checkPlacingCondition(Card card);
    public abstract void print();
    public int getScore() {
        return score;
    }
}
