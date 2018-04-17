package com.niko.moneycounter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import com.niko.moneycounter.TimeManager.OnTimeManagerListener;

public class MainActivity extends AppCompatActivity {

  private Button btnStart;
  private Button btnPause;
  private Button btnStop;
  private TextView horlyRateView;
  private TextView timeTextView;
  private TextView moneyTextView;

  private TimeManager timerManager;

  private float moneyDelta;
  private String moneyRate;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initMoneySettings();

    initViews();

    initTimeManager();

    handleListeners();
  }

  private void initMoneySettings() {
    SharedPreferences sharedPreferences = getSharedPreferences(Const.SHARED_NAME, MODE_PRIVATE);
    moneyDelta = sharedPreferences.getFloat(Const.MONEY_DELTA, 8.6f);
    moneyRate = sharedPreferences.getString(Const.MONEY_RATE, "$");
  }

  private void initViews() {

    Toolbar toolbar = findViewById(R.id.my_toolbar);
    setSupportActionBar(toolbar);

    btnStart = findViewById(R.id.btnStart);

    btnPause = findViewById(R.id.btnPause);

    btnStop = findViewById(R.id.btnStop);

    horlyRateView = findViewById(R.id.hourlyRate);

    timeTextView = findViewById(R.id.time);

    moneyTextView = findViewById(R.id.money);

    horlyRateView.setText(Html.fromHtml(String.format(getString(R.string.hourly_rate_layout), moneyDelta, moneyRate)));

  }

  private void initTimeManager() {
    timerManager = new TimeManager(this);

    timerManager.setOnTimerListener(new OnTimeManagerListener() {
      @Override
      public void onHandleRunningStatus(String runningStatus) {
        if (runningStatus.equals(Const.STATUS_START)) {
          btnStart.setEnabled(false);
          btnPause.setEnabled(true);
          btnStop.setEnabled(true);
        }

        if (runningStatus.equals(Const.STATUS_PAUSE)) {
          btnStart.setEnabled(true);
          btnPause.setEnabled(false);
          btnStop.setEnabled(true);
        }

        if (runningStatus.equals(Const.STATUS_STOP)) {
          btnStart.setEnabled(true);
          btnPause.setEnabled(false);
          btnStop.setEnabled(false);
        }
      }

      @Override
      public void onTick(long time) {
        moneyTextView.setText(TimeUtils.formatMoney(moneyDelta, time));
        checkSizeMoney(TimeUtils.formatMoney(moneyDelta, time));
        timeTextView.setText(TimeUtils.formatTime(time));
      }
    });
  }

  private void checkSizeMoney(String money) {
    Log.d("TAG", "money size: " + money.length());
    if (money.length() <= 6) {
      moneyTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
          getResources().getDimensionPixelSize(R.dimen.money_text_size_large_two));
    } else if (money.length() <= 7) {
      moneyTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
          getResources().getDimensionPixelSize(R.dimen.money_text_size_large));
    } else if (money.length() <= 9) {
      moneyTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
          getResources().getDimensionPixelSize(R.dimen.money_text_size_biggest));
    } else if (money.length() <= 11) {
      moneyTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
          getResources().getDimensionPixelSize(R.dimen.money_text_size_big));
    } else if (money.length() > 12) {
      moneyTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
          getResources().getDimensionPixelSize(R.dimen.money_text_size_small));
    }
  }

  private void handleListeners() {
    btnStart.setOnClickListener(v -> timerManager.setRunningStatus(Const.STATUS_START));

    btnPause.setOnClickListener(v -> timerManager.setRunningStatus(Const.STATUS_PAUSE));

    btnStop.setOnClickListener(v -> timerManager.setRunningStatus(Const.STATUS_STOP));
  }

  @Override
  protected void onStart() {
    timerManager.registerManager();
    super.onStart();
  }

  @Override
  protected void onStop() {
    timerManager.unregisterManager();
    super.onStop();
  }

  // Menu icons are inflated just as they were with actionbar
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    // getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }


}
