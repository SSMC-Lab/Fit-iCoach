package fruitbasket.com.bodyfit.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


import fruitbasket.com.bodyfit.R;
import pl.droidsonroids.gif.GifImageView;

public class ActionIntroductionActivity extends AppCompatActivity {
    //    private GifImageView gif ;
    private ImageView actionImage;
    private TextView nameTextView;
    private TextView preparation;
    private TextView points;
    private TextView explanationText;
    private ImageView explanationImage;
    private TextView benefitsText;
    private ImageView benefitsImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_action_introduction);
        actionImage = (ImageView) findViewById(R.id.action_image);
//        gif=(GifImageView)findViewById(R.id.action_gif);
        nameTextView = (TextView) findViewById(R.id.action_name);
        preparation = (TextView) findViewById(R.id.prepartion_textview);
        points = (TextView) findViewById(R.id.points_textview);
        explanationText = (TextView) findViewById(R.id.explanation_textview);
//        explanationImage=(ImageView)findViewById(R.id.explanation_imageview);
        benefitsText = (TextView) findViewById(R.id.benefits_textview);
        benefitsImage = (ImageView) findViewById(R.id.benefits_imageview);
        String name = getIntent().getStringExtra("action");
        inits(name);
    }

    private void inits(String name) {
        if (name.equals("坐姿哑铃弯举")) {
            actionImage.setImageResource(R.drawable.action1);
            nameTextView.setText("坐姿哑铃弯举");
            preparation.setText("      · 抓住哑铃,坐在平板凳上。双脚分开,身体前倾,把哑铃放在大腿内侧。");
            points.setText("      · 提高哑铃向肩膀。保持手臂稍微弯曲。重复。");
            explanationText.setText("      · 动作过程中身体不要摆动。");
            benefitsText.setText("      · 目标肌群: 肱肌\n      · 相关肌群: 肱二头肌|肱桡肌\n      · 稳定肌群: 斜方肌上束|斜方肌下束|竖脊肌");
            benefitsImage.setImageResource(R.drawable.benefits1);
        } else if (name.equals("锤式弯举")) {
            actionImage.setImageResource(R.drawable.action2);
            nameTextView.setText("锤式弯举");
            preparation.setText("      · 抓住哑铃,膝盖稍微弯曲，放在身体两侧，掌心向相。");
            points.setText("      · 向上提高哑铃向肩膀。保持肘部的位置。保持手臂稍微弯曲。重复。");
            explanationText.setText("      · 有伙伴在旁边保护着。");
            benefitsText.setText("      · 目标肌群: 肱桡肌\n      · 相关肌群: 肱肌|肱二头肌\n      · 稳定肌群: 三角肌前束|斜方肌上束|斜方肌中束");
            benefitsImage.setImageResource(R.drawable.benefits2);
        } else if (name.equals("哑铃侧卧外旋")) {
            actionImage.setImageResource(R.drawable.action3);
            nameTextView.setText("哑铃侧卧外旋");
            preparation.setText("      · 单手持一直哑铃，侧卧在平板凳上，上臂紧贴身体，屈肘，使前臂与上臂呈90度角，掌心向脚部方向。另一只手掐腰。");
            points.setText("      · 上臂不动，保持手臂的90度弯曲，向身体外侧旋转前臂，同时呼气。直至旋转到极限，保持顶峰收缩一秒。然后慢慢回到起始位置，同时吸气。");
            explanationText.setText("      · 不要使用太大的重量，不然会造成肩袖损伤。。");
            benefitsText.setText("      · 目标肌群: \n      · 相关肌群: \n      · 稳定肌群: ");
            benefitsImage.setImageResource(R.drawable.benefits3);
        } else if (name.equals("坐姿器械推胸")) {
            actionImage.setImageResource(R.drawable.action4);
            nameTextView.setText("坐姿器械推胸");
            preparation.setText("      · 坐在推胸机器上，调整座位让手柄平胸部。使前臂与躯干垂直。");
            points.setText("      · 直到手臂几乎是伸展。返回时到肘部和肩膀是一致的。重复。");
            explanationText.setText("      · 在运动保持手腕伸直,让肩膀休息，背紧贴靠垫。");
            benefitsText.setText("      · 目标肌群: 大胸肌\n      · 相关肌群: 大胸肌|三角肌前束|肱三头肌\n      · 稳定肌群: 肱二头肌");
            benefitsImage.setImageResource(R.drawable.benefits4);
        } else if (name.equals("正握下拉")) {
            actionImage.setImageResource(R.drawable.action5);
            nameTextView.setText("正握下拉");
            preparation.setText("      · 坐姿、挺胸抬头，肩下沉,腹内收，双手握实横握柄，小臂靠住护板，上肢依靠坐椅靠垫，双脚分开平踏于地面。");
            points.setText("      · 向上推举器械，直到手臂几乎伸直完毕。返回后，直到肘部略低于肩膀。");
            explanationText.setText("      · 注意肩关节的充分热身，训练时注意肘关节保持与地面垂直，不要耸肩。");
            benefitsText.setText("      · 目标肌群: 三角肌前束\n      · 相关肌群: 胸大肌|肱三头肌|斜方肌\n      · 稳定肌群: 肱二头肌");
            benefitsImage.setImageResource(R.drawable.benefits5);
        } else if (name.equals("弹力绳高位面拉")) {
            actionImage.setImageResource(R.drawable.action6);
            nameTextView.setText("弹力绳高位面拉");
            preparation.setText("      · 弹力绳系宇高点。双脚并立，腰背挺直。");
            points.setText("      · 拳心向下抓住手柄，后拉至面部，大臂与躯干相平，稍作停留后还原。");
            explanationText.setText("      · 还原吸气，后拉呼气。");
            benefitsText.setText("      · 目标肌群: \n      · 相关肌群:肱肌|肱桡肌\n      · 稳定肌群: ");
            benefitsImage.setImageResource(R.drawable.benefits6);
        } else if (name.equals("站姿飞鸟")) {
            actionImage.setImageResource(R.drawable.action7);
            nameTextView.setText("站姿飞鸟");
            preparation.setText("      · 两脚稍微分开站立，背部挺直，双臂垂直于身体两侧，双手抓握哑铃。");
            points.setText("      · 向侧上方平举哑铃至双肩水平，肘部微屈，返回起始位置。");
            explanationText.setText("      · 保持身体直立，不要晃动。.保持沉肩，不要耸肩，上臂尽量往后靠。.举起来后肘关节要稍高于手腕。");
            benefitsText.setText("      · 目标肌群: 三角肌中束\n      · 相关肌群: 三角肌前束|冈上肌|斜方肌中束\n      · 稳定肌群: 斜方肌上束|腕伸肌");
            benefitsImage.setImageResource(R.drawable.benefits7);
        } else if (name.equals("坐姿推肩")) {
            actionImage.setImageResource(R.drawable.action8);
            nameTextView.setText("坐姿推肩");
            preparation.setText("      · 背部挺直，坐于长凳上，正手抓握哑铃举至双肩连侧，掌心向前。");
            points.setText("      · 吸气，垂直向上推举哑铃，缓慢下落动作完成时呼气。");
            explanationText.setText("      · 可采用站姿做这一练习，也可左右两臂交替推举。为防止脊柱拉伸，常常用坐姿做推举练习。");
            benefitsText.setText("      · 目标肌群: 三角肌前束\n      · 相关肌群: 三角肌|冈上肌|斜方肌中束\n      · 稳定肌群: 斜方肌下束|肱二头肌");
            benefitsImage.setImageResource(R.drawable.benefits8);
        } else if (name.equals("阿诺德推举")) {
            actionImage.setImageResource(R.drawable.action9);
            nameTextView.setText("阿诺德推举");
            preparation.setText("      · 双手持一个哑铃，把哑铃举到肩膀高度，肘部在身体两侧，手掌朝向自己。");
            points.setText("      · 缓慢将哑铃举过头顶，不要完全锁定，与此同时转动你的手，大拇指向内转，" +
                    "使得手掌在动作的顶点朝向前方。在这个最终位置坚持一段时间然后进行反向动作，将哑铃慢慢放低，同时转动你的手，使其恢复至动作的初始状态。");
            explanationText.setText("      · 不要借力，保证哑铃完全在你的控制中，这个动作是由半个平举动作和半个推举动作组成的。");
            benefitsText.setText("      · 目标肌群: 三角肌前束\n      · 相关肌群: 肱三头肌|冈上肌|斜方肌中束\n      · 稳定肌群: 斜方肌上束");
            benefitsImage.setImageResource(R.drawable.benefits9);
        } else if (name.equals("单臂哑铃侧曲")) {
            actionImage.setImageResource(R.drawable.action10);
            nameTextView.setText("单臂哑铃侧曲");
            preparation.setText("      · 用一只手抓住哑铃并直立。保持手臂伸直,哑铃垂直到大腿根部。");
            points.setText("      · 身体向哑铃一侧弯曲，大概弯30度角左右，重复。");
            explanationText.setText("      · 运动的范围应限于大约30度。保持臀部在中心的位置,要两边摇摆。");
            benefitsText.setText("      · 目标肌群: 腹斜肌\n      · 相关肌群: 大胸肌|三角肌前束|肱三头肌\n      · 稳定肌群: 斜方肌上部|斜方肌中间|股大肌");
            benefitsImage.setImageResource(R.drawable.benefits10);
        } else if (name.equals("杠铃划船")) {
            actionImage.setImageResource(R.drawable.action11);
            nameTextView.setText("杠铃划船");
            preparation.setText("      · 把杠铃放在地板上面，站在它的前面。膝盖微微弯曲。臀部向前倾斜，直到双手在握到斑鞘杠铃。上半身应该平行略低于平行于地板上。掌握与反手握杠铃，伸直背部然后向前看。");
            points.setText("      · 把杠铃向低然后移动你的手臂和胸部以及上背部向上，注意肩胛骨肌肉，缓缓把杠铃放到地上，重复以上动作。");
            explanationText.setText("      · 保持好身体水平位置，杠铃的位置应该触到胸部,避免任何矫直的臀部。");
            benefitsText.setText("      · 目标肌群: 背部肌肉\n      · 相关肌群: 斜方肌中束|斜方肌下束|胸大肌|菱形肌\n      · 稳定肌群: 肱二头肌|竖脊肌|臀大肌|腘绳肌|髋内收肌");
            benefitsImage.setImageResource(R.drawable.benefits11);
        } else if (name.equals("杠铃卧推")) {
            actionImage.setImageResource(R.drawable.action12);
            nameTextView.setText("杠铃卧推");
            preparation.setText("      · 双手反握杠铃,躺在垫子或地板上。持杠铃到胸部以上,将上臂放在地板上,垂直于躯干。");
            points.setText("      · 杠铃位于胸部以上。保持手臂微微弯曲。返回到上臂略触摸地板，重复。");
            explanationText.setText("      · 有伙伴在旁边保护着，这个动作有一点的难度。");
            benefitsText.setText("      · 目标肌群: 肱三头肌\n      · 相关肌群: 胸大肌|三角肌前束\n      · 稳定肌群: 肱二头肌");
            benefitsImage.setImageResource(R.drawable.benefits12);
        } else if (name.equals("蝴蝶机夹胸")) {
            actionImage.setImageResource(R.drawable.action13);
            nameTextView.setText("蝴蝶机夹胸");
            preparation.setText("      · 坐在蝴蝶机上,杠杆调为垂直位置。上臂与地面平行，背后靠垫,小臂向上面向前方。");
            points.setText("      · 双臂一起推动杠杆。返回到肘部和肩膀平行，重复。");
            explanationText.setText("      · 不要让杠杆接触。");
            benefitsText.setText("      · 目标肌群: 胸肌\n      · 相关肌群: 胸大肌|三角肌前束\n      · 稳定肌群: ");
            benefitsImage.setImageResource(R.drawable.benefits13);
        } else if (name.equals("上斜哑铃飞鸟")) {
            actionImage.setImageResource(R.drawable.action14);
            nameTextView.setText("上斜哑铃飞鸟");
            preparation.setText("      · 抓住两个哑铃、仰卧在斜板。持哑铃在胸，保持手臂微屈，肘指向两侧，掌心朝内。");
            points.setText("      · 小臂向两侧做画圆动作，像抱大树那样的感觉，返回重复。");
            explanationText.setText("      · 斜坡板凳斜面台阶高于45度，让胸部充分得到锻炼。");
            benefitsText.setText("      · 目标肌群: 胸肌\n      · 相关肌群:胸大肌|三角肌前束\n      · 稳定肌群: 肱二头肌|肱肌|肱三头肌|屈肌");
            benefitsImage.setImageResource(R.drawable.benefits14);
        } else if (name.equals("背后拉力器弯举")) {
            actionImage.setImageResource(R.drawable.action15);
            nameTextView.setText("背后拉力器弯举");
            preparation.setText("      · 背身单手抓住拉力器手柄，双腿呈小弓步站牢。");
            points.setText("      · 向肩部弯举手臂。保持肘部位置固定，或允许稍微往前前移。返回时，保持手臂稍微弯曲。");
            explanationText.setText("      ");
            benefitsText.setText("      · 目标肌群: 肱二头肌\n      · 相关肌群:肱肌|肱桡肌\n      · 稳定肌群: 三角肌前束|斜方肌上束|斜方肌中束");
            benefitsImage.setImageResource(R.drawable.benefits15);
        }
    }
}
