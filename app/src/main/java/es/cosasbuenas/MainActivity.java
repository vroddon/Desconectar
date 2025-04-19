package es.cosasbuenas;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView screenTimeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!ScreenTimeCalculator.hasUsageStatsPermission(this)) {
            ScreenTimeCalculator.requestUsageStatsPermission(this);
        }
        screenTimeText = findViewById(R.id.screenTimeText);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check permission and update UI
        if (!ScreenTimeCalculator.hasUsageStatsPermission(this)) {
            ScreenTimeCalculator.requestUsageStatsPermission(this);
        } else {
            long millis = ScreenTimeCalculator.getScreenTimeToday(this);
            long minutes = millis / 1000 / 60;
            screenTimeText.setText("Screen today: " + minutes + " min");
        }
    }

}