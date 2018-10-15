package fruitbasket.com.bodyfit.DietRecommendations;

/**
 * Created by lkeye on 2018/9/8
 */
public class Food {
    private String name;
    private String gram;
    private int imageId;

    public Food(String name, String gram, int imageId) {
        this.name = name;
        this.gram = gram;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public String getGram() {
        return gram;
    }

    public int getImageId() {
        return imageId;
    }
}
