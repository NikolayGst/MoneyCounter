package com.niko.moneycounter;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

  private AlertDialog alertDialog;
  private String[] currencies;
  private EditText currency;
  private EditText hourlyRate;
  private Button btnSave;

  private SharedPreferences sPref;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    initDialogSelectRate();

    initViews();

    initMoneySettings();
  }

  private void initMoneySettings() {
    sPref = getSharedPreferences(Const.SHARED_NAME, MODE_PRIVATE);
    currency.setText(sPref.getString(Const.MONEY_CURRENCY, null));
    hourlyRate.setText(sPref.getFloat(Const.MONEY_DELTA, 0) == 0 ? null : String.valueOf(sPref.getFloat(Const.MONEY_DELTA, 0)));
  }

  private void initViews() {
    Toolbar toolbar = findViewById(R.id.settingsToolbar);
    toolbar.setTitle(R.string.settings);

    currency = findViewById(R.id.currency);

    hourlyRate = findViewById(R.id.hourlyRate);

    btnSave = findViewById(R.id.btnSave);

    currency.getBackground().mutate()
        .setColorFilter(getResources().getColor(R.color.cyan_100), PorterDuff.Mode.SRC_ATOP);

    hourlyRate.getBackground().mutate()
        .setColorFilter(getResources().getColor(R.color.cyan_100), PorterDuff.Mode.SRC_ATOP);

    currency.setOnClickListener(v -> alertDialog.show());

    btnSave.setOnClickListener(v -> {
      if (currency.getText().length() > 0 && hourlyRate.getText().length() > 0) {
        sPref.edit()
            .putString(Const.MONEY_CURRENCY, currency.getText().toString())
            .putFloat(Const.MONEY_DELTA, Float.parseFloat(hourlyRate.getText().toString()))
            .apply();
        finish();
      } else {
        Toast.makeText(this, R.string.toast_error_empty, Toast.LENGTH_SHORT).show();
      }
    });
  }

  private void initDialogSelectRate() {
    currencies = getResources().getStringArray(R.array.currencies);
    Builder alertDialogBuilder = new Builder(this);
    alertDialogBuilder.setTitle(R.string.select_currency_title)
        .setSingleChoiceItems(currencies, -1, (dialog, item) -> {
          currency.setText(currencies[item]);
          dialog.dismiss();
        });
    alertDialog = alertDialogBuilder.create();
  }
}
