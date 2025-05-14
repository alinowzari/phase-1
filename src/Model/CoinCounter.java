package Model;

public class CoinCounter {
    private int coin;
    public CoinCounter(int coin) {
        this.coin = coin;
    }
    public int getCoin() {
        return coin;
    }
    public void removeCoinAmount(int amount) {
        coin -= amount;
    }
    public void addCoinAmount(int amount) {
        coin += amount;
    }
}
