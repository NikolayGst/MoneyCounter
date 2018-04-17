package com.niko.moneycounter.manager;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import com.niko.moneycounter.utils.Const;

public class TimeManager extends ContextWrapper {

  public static final int ZERO = 0;
  public static final String TAG = TimeManager.class.getName();

  public static final int DELAY_MILLIS = 1000;

  //Слушатель для счетчика времени
  public interface OnTimeManagerListener {

    void onHandleRunningStatus(String runningStatus);

    void onTick(long time);
  }

  private String runningStatus;
  //Стартовое время
  private long startTime;
  //Пройденное время
  private long elapsedTime;

  private SharedPreferences sPref;

  private Handler handler;
  private Runnable timerTask;

  private OnTimeManagerListener onTimerListener;


  public void setOnTimerListener(OnTimeManagerListener onTimerListener) {
    this.onTimerListener = onTimerListener;
  }

  public OnTimeManagerListener getOnTimerListener() {
    return onTimerListener;
  }

  public TimeManager(Context base) {
    super(base);
    sPref = getSharedPreferences(Const.SHARED_NAME, MODE_PRIVATE);

    handler = new Handler();

    timerTask = () -> {

      elapsedTime = (System.currentTimeMillis() - startTime);

      if (getOnTimerListener() != null) {
        getOnTimerListener().onTick(elapsedTime);
      }

      handler.postDelayed(timerTask, DELAY_MILLIS);

    };
  }

  /*
   * Метод запускает счетчик времени
   * */
  private void startTimer() {
    Log.d(TAG, "startTimer");
    handler.postDelayed(timerTask, DELAY_MILLIS);
  }

  /*
   * Метод останавливает счетчик времени
   * */
  private void stopTimer() {
    Log.d(TAG, "stopTimer");
    handler.removeCallbacks(timerTask);
  }

  /*
   * Метод вызывается когда приложение открывается
   * Внутри него получаем данные, если они имеются и обрабатываем
   * */
  public void registerManager() {
    Log.d(TAG, "registerManager");

    runningStatus = sPref.getString(Const.RUNNING_STATUS, Const.STATUS_STOP);

    //Если статус == старт, то получаем сохраненное стартовое время, далее мы вычитаем текущее со стартовым
    //и получаем пройденное время, выводим пользователю время
    if (runningStatus.equals(Const.STATUS_START)) {
      startTime = sPref.getLong(Const.START_TIME, System.currentTimeMillis());
      elapsedTime = System.currentTimeMillis() - startTime;
      startTimer();
      //Если статус == пауза, то получаем сохраненное стартовое время и получаем сохраненное пройденное время, выводим пользователю время
    } else if (runningStatus.equals(Const.STATUS_PAUSE)) {
      startTime = sPref.getLong(Const.START_TIME, System.currentTimeMillis());
      elapsedTime = sPref.getLong(Const.ELAPSED_TIME, 0);
    }

    if (onTimerListener != null) {
      onTimerListener.onHandleRunningStatus(runningStatus);
      onTimerListener.onTick(elapsedTime);
    }

  }

  /*
   * Метод, который получает сохраненное стартовое время, далее мы вычитаем текущее со стартовым
   * и получаем пройденное время, выводим пользователю время
   * */
  private void getSavedTimeAndShow() {
    startTime = sPref.getLong(Const.START_TIME, System.currentTimeMillis());
    elapsedTime = System.currentTimeMillis() - startTime;
    if (onTimerListener != null) {
      onTimerListener.onTick(elapsedTime);
    }
  }

  /*
   * Метод вызывается когда приложение закрывается, останавливая таймер и сохраняя текущие данные
   * */
  public void unregisterManager() {
    Log.d(TAG, "unregisterManager");
    stopTimer();
    saveTimeAndAction();
  }

  public void setRunningStatus(String runningStatus) {
    this.runningStatus = runningStatus;
    checkRunningStatus();
  }

  /*
   * Метод для обработки статуса
   * */
  private void checkRunningStatus() {
    if (onTimerListener != null) {
      onTimerListener.onHandleRunningStatus(runningStatus);
    }

    Log.d(TAG, "setRunningStatus: " + runningStatus);

    switch (getRunningStatus()) {
      case Const.STATUS_START:
        startTime = System.currentTimeMillis() - getElapsedTime();
        startTimer();
        break;
      case Const.STATUS_PAUSE:
        stopTimer();
        saveTimeAndAction();
        break;
      case Const.STATUS_STOP:
        stopTimer();
        setStartTime(ZERO);
        setElapsedTime(ZERO);
        saveTimeAndAction();
        if (onTimerListener != null) {
          onTimerListener.onTick(ZERO);
        }
        break;
    }
  }

  /*
   * Метод сохоняет текущее состояние таймера, время старта и пройденное время
   * */
  private void saveTimeAndAction() {
    Log.d(TAG, "saveTimeAndAction: " + startTime);
    sPref.edit()
        .putString(Const.RUNNING_STATUS, getRunningStatus())
        .putLong(Const.START_TIME, getStartTime())
        .putLong(Const.ELAPSED_TIME, getElapsedTime())
        .apply();
  }
  public String getRunningStatus() {
    return runningStatus;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  public long getStartTime() {
    return startTime;
  }

  public void setElapsedTime(long elapsedTime) {
    this.elapsedTime = elapsedTime;
  }

  public long getElapsedTime() {
    return elapsedTime;
  }
}
