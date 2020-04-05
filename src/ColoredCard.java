public class ColoredCard extends Card {
    private String color;
    public ColoredCard(String color,int score){
        super(score);
        this.color=color;
    }
    public String getColor() {
        return color;
    }
}
