package ro.pub.cs.systems.eim.practicaltest01;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Date;
import java.util.Random;

class ProcessingThread extends Thread{
    private Context context;
    private double aritm;
    private double geom;
    private boolean isRunning = true;
    Random random = new Random();

    public ProcessingThread(Context context, Integer nr1, Integer nr2) {
        this.context = context;
        this.aritm = (nr1 + nr2)/ 2;
        this.geom = Math.sqrt(nr1 * nr2);
    }

    @Override
    public void run() {
        Log.d("[ProcessingThread]", "Thread has started!");
        while (isRunning) {
            sendMessage();
            sleep();
        }
        Log.d("[ProcessingThread]", "Thread has stopped!");
    }

    private void sendMessage() {
        Intent intent = new Intent();
        intent.setAction(Constants.actionTypes[random.nextInt(Constants.actionTypes.length)]);
        intent.putExtra("message", new Date(System.currentTimeMillis()) + " " + aritm + geom);
        context.sendBroadcast(intent);
    }

    private void sleep() {
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopThread() {
        isRunning = false;
    }
}

public class PracticalTest01Service extends Service {
    private Integer nr1 = null;
    private Integer nr2 = null;
    private ProcessingThread processingThread = null;

    public PracticalTest01Service() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            nr1 = Integer.parseInt(intent.getExtras().getString(Constants.NR_1));
            nr2 = Integer.parseInt(intent.getExtras().getString(Constants.NR_2));
        }
        processingThread = new ProcessingThread(this, nr1, nr2);
        processingThread.start();
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        processingThread.stopThread();
    }
}
