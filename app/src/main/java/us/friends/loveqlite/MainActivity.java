package us.friends.loveqlite;

import android.app.Notification;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.joda.time.DateTime;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import us.friends.loveqlite.bean.Datum;
import us.friends.loveqlite.bean.ProgramList;
import us.friends.loveqlite.util.CommonUtils;
import us.friends.loveqlite.util.FileUtils;
import us.friends.loveqlite.view.PickerView;
import us.friends.loveqlite.view.SpaceItemDecoration;

import static us.friends.loveqlite.Config.CANTONESE;
import static us.friends.loveqlite.Config.FORM_URLENCODED;
import static us.friends.loveqlite.Config.LOVEQ_URL;
import static us.friends.loveqlite.Config.MANDARIN;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.btn_cantonese)
    Button cantonese;
    @BindView(R.id.btn_mandarin)
    Button mandarin;
    @BindView(R.id.year)
    TextView yearText;
    @BindView(R.id.monthView)
    PickerView monthView;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private ProgramAdapter programAdapter;

    private List<Datum> datas = new ArrayList<>();
    private List<String> months = new ArrayList<>();

    private AsyncHttpClient client = new AsyncHttpClient(true, 0, 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity_layout);
        ButterKnife.bind(this);
        initViews();

        setMonths();
        getDatas(Integer.valueOf(yearText.getText().toString()));

        programAdapter = new ProgramAdapter(this, getMonthDatas(DateTime.now().getMonthOfYear()));
        recyclerView.setAdapter(programAdapter);
        monthView.setData(months);
        monthView.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                // 更新当月周末
                programAdapter.setDate(getMonthDatas(CommonUtils.convertMonthToNum(text)));
                programAdapter.notifyDataSetChanged();
            }
        });

        monthView.setSelected(0);
    }

    private void initViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.addItemDecoration(new SpaceItemDecoration(60));
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setMonths() {
        Collections.addAll(months, getResources().getStringArray(R.array.months));
    }

    /**
     * 获取当年数据
     *
     * @param year 年份
     */
    public void getDatas(int year) {
        try {
            NameValuePair pair = new BasicNameValuePair("json", getString(R.string.request_para1) + year + getString(R.string.request_para2));
            List<NameValuePair> list = new ArrayList<>(Collections.singletonList(pair));
            UrlEncodedFormEntity se = new UrlEncodedFormEntity(list);

            client.post(MainActivity.this, LOVEQ_URL, se, FORM_URLENCODED, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    ProgramList programList = JSON.parseObject(responseBody, ProgramList.class);
                    List<Datum> datum = programList.getData();

                    //再次处理 将数据按月份分组
                    for (Datum item : datum) {
                        String[] strings = item.getFile_name().split("\\.");
                        item.setYear(Integer.valueOf(strings[0]))
                                .setMonth(Integer.valueOf(strings[1]))
                                .setDay(strings[2]);
                    }

                    setYearData(datum);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.e("fuck ", statusCode + "");
                    Toast.makeText(MainActivity.this, R.string.network_error, Toast.LENGTH_LONG).show();
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void setYearData(List<Datum> datas) {
        this.datas = datas;
        //更新当月周末
        programAdapter.setDate(getMonthDatas(CommonUtils.convertMonthToNum(monthView.getCurrentValue())));
        programAdapter.notifyDataSetChanged();
    }

    //获得当月数据
    private List<Datum> getMonthDatas(int month) {
        List<Datum> monthDatas = new ArrayList<>();
        for (Datum data : datas) {
            if (data.getMonth() == (month)) {
                String day = data.getDay().split("-")[0].trim();
                data.setDayOfWeek(new DateTime(data.getYear(), data.getMonth(), Integer.valueOf(day), 0, 0).dayOfWeek().get());
                monthDatas.add(data);
            }
        }
        return monthDatas;
    }

    @OnClick(R.id.year)
    public void showYearSelecteDialog() {
        YearDialogFragment yearDialogFragment = new YearDialogFragment();
        yearDialogFragment.show(getSupportFragmentManager(), null);
        yearDialogFragment.setOnYearClickListerner(new YearDialogFragment.OnYearClickListener() {
            @Override
            public void onClick(String year) {
                yearText.setText(year);
                //获取当年数据
                getDatas(Integer.valueOf(year));
            }
        });
    }

    @OnClick(R.id.btn_mandarin)
    public void downloadMandarin() {
        download(MANDARIN);
    }

    @OnClick(R.id.btn_cantonese)
    public void downloadCantonese() {
        download(CANTONESE);
    }

    private void download(int language) {
        final Datum datum = programAdapter.getDate();
        final String fileName = datum.getFile_name().trim() + "-" + language + ".mp3";
        datum.setFile_name(fileName);

        //判断是否已经下载
        if (FileUtils.isFileExist(fileName)) {
            Toast.makeText(this, datum.getFile_name() + getString(R.string.already_downloaded), Toast.LENGTH_SHORT).show();
            return;
        }

        String url = datum.getDown_url();
        if (language == MANDARIN) {
            //通过粤语url 拼装出国语版url
            url = url.replace("-1.", "-2.");
        }

        Toast.makeText(MainActivity.this, getString(R.string.downloading) + fileName, Toast.LENGTH_LONG).show();
        final Notification.Builder notification = NotificationUtil.publicNotification(this, datum);

        client.get(this, url, null, new AsyncHttpResponseHandler() {
            long lastTimestamp = 0;
            long interval = 0;

            @Override
            public void onSuccess(int returnCode, Header[] headers, byte[] binaryData) {
                if (returnCode == HttpStatus.SC_OK) {

                    FileUtils.write2SDFromInput(fileName, new ByteArrayInputStream(binaryData));

                    Toast.makeText(MainActivity.this, fileName + getString(R.string.download_finish), Toast.LENGTH_LONG).show();
                    NotificationUtil.completeNotification(MainActivity.this, notification, datum);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(MainActivity.this, fileName + getString(R.string.download_fail), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                int progress = (int) ((bytesWritten * 1.0 / totalSize) * 100);
                if (interval > 500) {
                    NotificationUtil.updateNotification(MainActivity.this, notification, datum, progress);
//                    Log.d(datum.getFile_name() + "下载 Progress>>>>>", bytesWritten + " / " + totalSize + " " + progress + "%");
                    interval = 0;
                }
                long now = System.currentTimeMillis();
                interval += now - lastTimestamp;
                lastTimestamp = now;
            }
        });
    }
}
