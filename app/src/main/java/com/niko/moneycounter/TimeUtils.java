package com.niko.moneycounter;

import java.util.Locale;

public class TimeUtils {

  public static String formatTime(long time) {
    long hours = (time / 60 / 60 / 1000);
    long minutes = (time / 60 / 1000) % 60;
    long seconds = (time / 1000) % 60;
    return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
  }

  public static String formatMoney(float moneyDelta, long time) {
    float result = (time / 1000f) * moneyDelta / 3600f;
    return String.format(Locale.getDefault(), "%,.2f", result) + "$";
  }

}
