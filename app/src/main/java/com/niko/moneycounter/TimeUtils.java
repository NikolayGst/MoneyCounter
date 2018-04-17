package com.niko.moneycounter;

import java.util.Locale;

public class TimeUtils {

  public static String formatTime(long time) {
    long hours = (time / 60 / 60 / 1000);
    long minutes = (time / 60 / 1000) % 60;
    long seconds = (time / 1000) % 60;
    return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
  }

  public static String formatMoney(double moneyDelta, long time) {
    double result = (time / 1000.0) * moneyDelta / 3600.0;
    return String.format(Locale.getDefault(), "%,.2f", result) + "$";
  }

}
