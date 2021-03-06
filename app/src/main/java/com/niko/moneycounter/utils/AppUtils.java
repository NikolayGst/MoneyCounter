package com.niko.moneycounter.utils;

import java.util.Locale;

public class AppUtils {

  public static String formatTime(long time) {
    long hours = (time / 60 / 60 / 1000);
    long minutes = (time / 60 / 1000) % 60;
    long seconds = (time / 1000) % 60;
    return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
  }

  public static String formatMoney(float moneyDelta,String currency, long time) {
    float result = (time / 1000f) * moneyDelta / 3600f;
    return String.format(Locale.ENGLISH, "%,.2f", result) + currency;
  }

  public static String formatMoney(float moneyDelta) {
    return String.format(Locale.ENGLISH, "%.2f", moneyDelta);
  }

}
