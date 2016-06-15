package us.friends.loveqlite;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.List;

import us.friends.loveqlite.bean.Datum;
import us.friends.loveqlite.view.WeekendButton;

/**
 * Created by joker on 15/8/5.
 */
public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ProgramViewHolder> {

    private Context context;
    private List<Datum> data;
    private int checkedPositon = -1;
    private Handler handler;

    public ProgramAdapter(Context context, List<Datum> dates) {
        this.context = context;
        this.data = dates;
    }

    @Override
    public ProgramViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.program_item_layout, parent, false);
        return new ProgramViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProgramViewHolder holder, int position) {
        Datum date = data.get(position);
        holder.weekendButton.setTextViewText(String.valueOf(date.getDay()), date.getDayOfWeek() == 6 ? "六" : "日");
        holder.weekendButton.setPosition(position);
        if (position != checkedPositon) {
            holder.weekendButton.button.setChecked(false);
            holder.weekendButton.button.setTextColor(Color.BLACK);
        } else {
            holder.weekendButton.button.setChecked(true);
            holder.weekendButton.button.setTextColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ProgramViewHolder extends RecyclerView.ViewHolder {
        WeekendButton weekendButton;

        ProgramViewHolder(View itemView) {
            super(itemView);
            this.weekendButton = (WeekendButton) itemView.findViewById(R.id.weekend);
            this.weekendButton.setOnRadioButtonListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && checkedPositon != weekendButton.getPosition()) {
                        checkedPositon = weekendButton.getPosition();

                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                            }
                        });
                    }
                }
            });
        }
    }

    public int getCheckedPositon() {
        return checkedPositon;
    }

    public Datum getDate() {
        return data.get(checkedPositon);
    }

    public void setDate(List<Datum> date) {
        this.data = date;
    }
}
