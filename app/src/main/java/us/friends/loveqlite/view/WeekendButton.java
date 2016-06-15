package us.friends.loveqlite.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import us.friends.loveqlite.R;

/**
 * Created by lino on 16/2/18.
 */
public class WeekendButton extends LinearLayout {

    public RadioButton button;
    private TextView text;
    private int position;


    public WeekendButton(Context context) {
        this(context, null);
    }

    public WeekendButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekendButton(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.weekendview_layout, this, true);
        button = (RadioButton) findViewById(R.id.button);
        text = (TextView) findViewById(R.id.text);
    }

    /**
     * 设置图片资源
     */
    public void setButtonText(String text) {
        button.setText(text);
    }

    /**
     * 设置显示的文字
     */
    public void setTextViewText(String day, String weekDay) {
        button.setText(day);
        text.setText(weekDay);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return this.position;
    }

    public void switchRadioButton(boolean flag) {
        this.button.setChecked(flag);
    }

    public void setOnRadioButtonListener(CompoundButton.OnCheckedChangeListener onRadioButtonListener) {
        button.setOnCheckedChangeListener(onRadioButtonListener);
    }

}
