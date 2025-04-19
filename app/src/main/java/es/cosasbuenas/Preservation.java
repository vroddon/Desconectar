package es.cosasbuenas;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import java.util.Date;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Preservation {
    public static void saveScreenTimeToFile(Context context, int totalMinutes) {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String fileName = today + ".txt";
        String content = String.valueOf(totalMinutes);

        File folder = new File(Environment.getExternalStorageDirectory(), "vroddon/data/skyneta");
        if (!folder.exists()) {
            folder.mkdirs(); // create folder if not exists
        }

        File outFile = new File(folder, fileName);

        try (FileOutputStream fos = new FileOutputStream(outFile)) {
            fos.write(content.getBytes());
            fos.flush();
            Log.d("ScreenTime", "Saved screen time to file: " + outFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e("ScreenTime", "Failed to write file", e);
        }
    }
}
