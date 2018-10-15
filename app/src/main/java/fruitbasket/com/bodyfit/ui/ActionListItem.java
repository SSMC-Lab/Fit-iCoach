package fruitbasket.com.bodyfit.ui;

import java.io.Serializable;

/**
 * Created by lkeye on 2018/1/24.
 */

public class ActionListItem implements Serializable {
    private String name;
    private String times;
    private int imageId;

    public ActionListItem(String name, String times, int imageId) {
        this.name = name;
        this.times = times;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public String getTimes() {
        return times;
    }

    public int getImageId() {
        return imageId;
    }
}
