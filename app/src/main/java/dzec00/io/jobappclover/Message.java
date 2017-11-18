package dzec00.io.jobappclover;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Message extends AppCompatActivity {

    TextView tvMessage;
    Button send;
    Map<String,String> postData = new HashMap<>();
    public String accessToken = "";
    HttpPostAsyncTask task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);

        tvMessage = (TextView) findViewById(R.id.etMessage);
        send = (Button) findViewById(R.id.btnSend);
        accessToken = getIntent().getExtras().getString("accessToken");



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postData.put("targetType", "3");
                postData.put("messageType","1");
                postData.put("target","5a05ccd4829e64fd1dcd7732");
                postData.put("message", tvMessage.getText().toString());

                task = new HttpPostAsyncTask(postData);
                task.execute("https://spika.chat/api/v3/messages");

                Toast.makeText(getApplicationContext(),"Message sent.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class HttpPostAsyncTask extends AsyncTask<String, Void, Void>{

        JSONObject postData;
        int statusCode;


        public HttpPostAsyncTask(Map<String, String> postData) {
            if(postData!=null){
                this.postData = new JSONObject(postData);

            }

        }

        @Override
        protected Void doInBackground(String... params) {


            try {
                URL url = new URL(params[0]);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-type","application/json; charset=utf-8");
                urlConnection.setRequestProperty("apikey","GMUwQIHielm7b1ZQNNJYMAfCC508Giof");
                urlConnection.setRequestProperty("access-token", accessToken);
                urlConnection.setRequestMethod("POST");

                if (this.postData != null) {
                    OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                    writer.write(postData.toString());
                    writer.flush();
                }

                statusCode = urlConnection.getResponseCode();
                Log.d("statuscode2", "statuscode: " + statusCode);

            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Toast.makeText(getApplicationContext(),"Done. Status code: " + statusCode,Toast.LENGTH_LONG).show();


        }
    }
}
