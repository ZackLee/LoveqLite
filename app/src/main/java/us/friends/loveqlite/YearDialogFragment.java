package us.friends.loveqlite;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lino on 16/3/28.
 * 选择“年份”对话框
 */
public class YearDialogFragment extends DialogFragment {

    @BindView(R.id.yearList)
    ListView listView;

    private List<String> yearList;
    private OnYearClickListener listerner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        yearList = getYears();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.year, container);
        ButterKnife.bind(this, view);
        listView.setAdapter(new YearAdapter(getActivity(), yearList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listerner != null) {
                    listerner.onClick(String.valueOf(yearList.get(position)));
                }
                dismiss();
            }
        });
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    class YearAdapter extends BaseAdapter {
        Context context;
        List<String> datas;

        public YearAdapter(Context context, List<String> datas) {
            this.context = context;
            this.datas = datas;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView != null) {
                viewHolder = (ViewHolder) convertView.getTag();
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.year_dialog_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.textView = (TextView) convertView.findViewById(R.id.text);
                convertView.setTag(viewHolder);
            }
            viewHolder.textView.setText(String.valueOf(datas.get(position)));
            return convertView;
        }

        class ViewHolder {
            TextView textView;
        }
    }

    /**
     * 获取03年开播到现在所有的年份
     * FIXME 接口暂时只提供2010年后的数据
     */
    public ArrayList getYears() {
        int currentYear = DateTime.now().year().get();
        ArrayList years = new ArrayList();
        for (int startAt = 2010; startAt <= currentYear; startAt++) {
            years.add(startAt);
        }
        return years;
    }

    public void setOnYearClickListerner(OnYearClickListener listerner) {
        this.listerner = listerner;
    }

    interface OnYearClickListener {
        void onClick(String year);
    }
}
