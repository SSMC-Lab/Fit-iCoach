package fruitbasket.com.bodyfit.DietRecommendations;

/**
 * Created by lkeye on 2018/9/2
 */
public class FoodScore {

    private String name;
    private int imageId;
    private int score;

    public FoodScore(String name, int imageId, int score){
        this.name=name;
        this.imageId=imageId;
        this.score=score;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
