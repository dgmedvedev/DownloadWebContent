package demo.com;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private String mailRu = "https://mail.ru/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Hello", mailRu);

        DownloadTask task = new DownloadTask();
        // task.execute(mailRu); - метод, который запускает задачу в другом потоке
        // get() - получение данных, которые возвращает метод execute(). Может вызвать Exception.
        try {
            String result = task.execute(mailRu).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.i("URL", strings[0]);
            return null;
        }
    }
}