package dojo.supermarket.model.loyalty;

public class LoyaltyCard {
    private int points;
    private final String owner;

    public LoyaltyCard(String owner, int initialPoints) {
        this.owner = owner;
        this.points = initialPoints;
    }

    public void addPoints(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("points must be >= 0");
        }
        this.points += points;
    }

    public void usePoints(int points) {
        if (this.points < points) {
            throw new IllegalArgumentException("Insufficient points");
        }
        this.points -= points;
    }

    public int getPoints() {
        return points;
    }

    public String getOwner() {
        return owner;
    }
}
