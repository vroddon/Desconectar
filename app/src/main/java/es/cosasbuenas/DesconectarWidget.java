package es.cosasbuenas;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DesconectarWidget extends AppWidgetProvider {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    @Override
    public void onUpdate(Context context, AppWidgetManager manager, int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_hello);
            System.out.println("This is a debug message");
            views.setTextViewText(R.id.text_screen_on, "Screen On: 0 min");
            views.setTextViewText(R.id.text_message, "Message: Hola 👋");
            setMotto(context, manager, widgetId, views);

            // Inicio del tap
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_container, pendingIntent);
            // Fin del tap

            manager.updateAppWidget(widgetId, views);


        }
    }
    private void setMotto(Context context, AppWidgetManager manager, int widgetId, RemoteViews views) {
        executor.execute(() -> {
            String message = "No message";

            try {
                URL url = new URL("https://cosasbuenas.es/api/motto");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                message = response.toString();
            } catch (Exception e) {
                message = "Error al conectar";
            }
            String message2="00";
            try {
                URL url = new URL("https://cosasbuenas.es/api/computertoday");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                message2 = response.toString();
            } catch (Exception e) {
                message2 = "00";
            }

            long millis = ScreenTimeCalculator.getScreenTimeToday(context);
            long minutes = millis / 1000 / 60;
            String display = "Móvil: " + minutes + " min. PC: " + message2 +" min.";
            views.setTextViewText(R.id.text_screen_on, display);

            Preservation.saveScreenTimeToFile(context, (int)minutes);


            String finalMessage = message;
            views.setTextViewText(R.id.text_message, "Motto: " + finalMessage);
            manager.updateAppWidget(widgetId, views);
        });
    }

}