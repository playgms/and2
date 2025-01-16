package com.example.asyctask;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView bannerTextView;
    private Button startButton, stopButton;
    private BannerTask bannerTask;
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bannerTextView = findViewById(R.id.bannerTextView);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);

        startButton.setOnClickListener(v -> startBannerTask());
        stopButton.setOnClickListener(v -> stopBannerTask());
    }

    private void startBannerTask() {
        if (bannerTask == null || bannerTask.getStatus() == AsyncTask.Status.FINISHED) {
            isRunning = true;
            bannerTask = new BannerTask();
            bannerTask.execute("Demonstration of Asynchronous Task");
        }
    }

    private void stopBannerTask() {
        isRunning = false;
        if (bannerTask != null) {
            bannerTask.cancel(true);
        }
    }

    private class BannerTask extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String message = strings[0];
            StringBuilder banner = new StringBuilder(message);

            while (isRunning && !isCancelled()) {
                try {
                    Thread.sleep(300); // Adjust scrolling speed here
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                banner.append(banner.charAt(0));
                banner.deleteCharAt(0);
                publishProgress(banner.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            bannerTextView.setText(values[0]);
        }

        @Override
        protected void onCancelled() {
            bannerTextView.setText(""); // Clear the banner on cancel
        }
    }
}
