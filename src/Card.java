public abstract class Card {
    private int score;
    public Card (int score){
        this.score=score;
    }
    public abstract void print();
    public int getScore() {
        return score;
    }
}
