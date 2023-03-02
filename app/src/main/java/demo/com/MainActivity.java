package demo.com;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

// Тестовое приложение, которое выводит html код страницы, в окне Logcat
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String mailRu = "https://mail.ru/";
        // 1. Всегда, когда нужно читать данные из интернета, у приложения должен быть
        // разрешен доступ в интернет. Разрешение нужно добавить в AndroidManifest
        DownloadTask task = new DownloadTask();
        // task.execute(mailRu); - метод, который запускает задачу в другом потоке. Т.е. метод doInBackground()
        // get() - получение данных, которые возвращает метод execute(). Может вызвать Exception.
        try {
            String result = task.execute(mailRu).get();
            // выводим информационное сообщение в окно Logcat
            Log.i("URL", result);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder result = new StringBuilder();
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                // передаем адрес для создания url
                url = new URL(strings[0]);
                // открываем соединение
                urlConnection = (HttpURLConnection) url.openConnection();
                // читаем данные из интернета
                InputStream in = urlConnection.getInputStream(); // поток ввода для чтения данных
                InputStreamReader reader = new InputStreamReader(in); // создали, чтобы начать процесс чтения данных
                // reader читает данные по одному символу, чтобы читать строками, создаем BufferdReader
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line = bufferedReader.readLine(); // получили первую строку с сайта
                while (line != null) {
                    result.append(line);
                    line = bufferedReader.readLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return result.toString();
        }
    }
}