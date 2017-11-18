package dzec00.io.jobappclover;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
public class MainActivity extends AppCompatActivity {

    EditText user, pass;
    Button signIn;
    Map<String, String> postData = new HashMap<>();
    HttpPostAsyncTask task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = (EditText) findViewById(R.id.etUsername);
        pass = (EditText) findViewById(R.id.etPassword);
        signIn = (Button) findViewById(R.id.btnSignIn);



        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postData.put("organization","clover");
                postData.put("username", user.getText().toString());
                postData.put("password", pass.getText().toString());
                task = new HttpPostAsyncTask(postData);
                task.execute("https://spika.chat/api/v3/signin");
                Toast.makeText(getApplicationContext(),"Sign in data sent.",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private class HttpPostAsyncTask extends AsyncTask<String, Void, Void> {

        JSONObject postData;
        String response,inputString,accessToken;
        int statusCode;

        public HttpPostAsyncTask(Map<String, String> postData) {
            Log.d("postData2",postData.toString());
            if (postData != null) {
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
                urlConnection.setRequestMethod("POST");

                if (this.postData != null) {
                    OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                    Log.d("JSONDATA", postData.toString());
                    writer.write(postData.toString());
                    writer.flush();
                }

                statusCode = urlConnection.getResponseCode();
                Log.d("STATUSCODE: ", "status code is " + statusCode);
                if (statusCode == 200) {

                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    response = inputStream.toString();

                    StringWriter writer = new StringWriter();
                    IOUtils.copy(inputStream,writer,"UTF-8");
                    inputString = writer.toString();


                    try {
                        JSONObject reader = new JSONObject(inputString);
                        accessToken  = reader.getString("access-token");
                        Log.d("AccessToken: ", accessToken);
                    } catch (JSONException e) {
                        Log.d("JSONParsing", "JSONParsing failed");
                        e.printStackTrace();
                    }


                } else {

                }

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

            Intent intent1 = new Intent(getApplicationContext(), Message.class);
            intent1.putExtra("accessToken", accessToken);
            startActivity(intent1);
        }

    }
}


