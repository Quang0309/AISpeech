package com.example.admin.ai_speech;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.HandlerThread;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;


public class MainActivity extends AppCompatActivity {


    TextView text; // Text
    TextView response; // Language code
    private EditText your_message; // Input text
    private EditText your_response; // Input LC(Language Code)
    private String message;
    //private SimsimiAPI simsimiAPI; // a class represents for requesting
    // information to Server.(AsyncTask)
    private String result = "Fail";
    private BufferedReader bufferReader = null;
    private InputStreamReader inputStreamReader = null;
    private String buffer = null;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private boolean isDone = false;
    private boolean isDone2 = false;
    private TextToSpeech t1;
    List<chatmodel> list_chat = new ArrayList<>();
    ListView listView;
    ImageView imageView;
    String character;
    String array[] = {"boo","boy","agnes","tangled","boy1"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        character = intent.getStringExtra("name");

        init();

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isNetworkAvailable())
                    promptSpeechInput();
                else
                    Toast.makeText(getApplicationContext(),"Please check your connection and try again",Toast.LENGTH_LONG).show();

               // new SimsimiAPI().execute(your_response);
            }
        });
    }

    private void init() {
        text = (TextView) findViewById(R.id.text);
        imageView = (ImageView) findViewById(R.id.image);
        btnSpeak = (ImageButton) findViewById(R.id.request_button);
        your_message = (EditText) findViewById(R.id.your_message);
        your_response = (EditText) findViewById(R.id.you_response);
        listView = (ListView)findViewById(R.id.list_of_message);

        your_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isDone)
                    return;
                isDone =true;
                String text = your_message.getText().toString();
                chatmodel model = new chatmodel(text, true);
                list_chat.add(model);
                //CustomAdapter adapter = new CustomAdapter(list_chat,getApplicationContext());
                //listView.setAdapter(adapter);
                new SimsimiAPI().execute(your_response);

                isDone = false;
            }
        });
        your_response.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isDone2)
                    return;
                isDone2 = true;

                t1.speak(your_response.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
                isDone2 = false;
            }
        });
        //simsimiAPI = new SimsimiAPI();

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR) {
                    if(!isPackageInstalled(getPackageManager(), "com.google.android.tts"))
                    {
                        Intent installVoice = new Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                        startActivity(installVoice);
                    }
                    else
                        t1.setEngineByPackageName("com.google.android.tts");
                    if (character.equals(array[0])) {
                        t1.setLanguage(Locale.US);
                        imageView.setImageResource(R.drawable.boo);
                        t1.setPitch(1.4f);
                    } else if (character.equals(array[3])) {
                        t1.setLanguage(Locale.US);
                        imageView.setImageResource(R.drawable.tangled);
                        t1.setSpeechRate(0.9f);
                        t1.setPitch(1.5f);
                    } else if (character.equals(array[4])) {
                        t1.setLanguage(Locale.UK);
                        imageView.setImageResource(R.drawable.boy1);

                        t1.setPitch(1.2f);
                    } else if (character.equals(array[1])) {
                        t1.setLanguage(Locale.UK);
                        imageView.setImageResource(R.drawable.boy);
                        t1.setPitch(1.4f);
                    } else if (character.equals(array[2])) {
                        t1.setLanguage(Locale.US);
                        imageView.setImageResource(R.drawable.agnes);
                        t1.setPitch(2.0f);
                        t1.setSpeechRate(1.2f);
                    }
                }
            }
        });


    }
    public static boolean isPackageInstalled(PackageManager pm, String packageName) {
        try {
            pm.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }
    /*public void OnGetClick(View v) {

        new SimsimiAPI().execute(your_response);

    }*/

    /**
     * SimsimiAPI extends AsyncTask because we can process background work
     * easily.
     *
     */
    public class SimsimiAPI extends AsyncTask<TextView, Void, String> {

        private TextView response;
        //private boolean isDone = false;


        // process background work
        protected String doInBackground(TextView... params) {

            /*try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

                this.response = params[0];
                return makeHttpRequest();

        }



        // request information to Simsimi Server
        final String makeHttpRequest()
        {

                String key = "48554a76-4b54-45df-a47d-dc14fd5666ea";
                String lc = "en";
                double ft = 0.0;

                try {

                    String text = URLEncoder.encode(your_message.getText().toString(), "UTF-8");
                    String url = "http://sandbox.api.simsimi.com/request.p?key="
                            + key + "&lc=" + lc + "&ft=" + ft + "&text=" + text;
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpRequest = new HttpGet(url);

                    HttpResponse response = httpClient.execute(httpRequest);

                    inputStreamReader = new InputStreamReader(response.getEntity()
                            .getContent());
                    bufferReader = new BufferedReader(inputStreamReader);

                    while ((buffer = bufferReader.readLine()) != null) {
                        if (buffer.length() > 1) {
                            result = buffer;
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    System.out
                            .println("UnsupportedEncodingException is generated.");
                } catch (IOException e) {

                    System.out.println("IOException is generated.");

                } finally {

                    // InputStreamReader is closed.
                    if (inputStreamReader != null)
                        try {
                            inputStreamReader.close();
                        } catch (IOException e) {
                            System.out.println("InputStreamReader is not closed.");
                        }

                    // BufferedReader is closed.
                    if (bufferReader != null)
                        try {
                            bufferReader.close();
                        } catch (IOException e) {
                            System.out.println("BufferedReader is not closed.");
                        }
                }

                return result; // return Server's response information which
                // consists of JSON Format.

        } // end of makeHttpRequest method

        /**
         * After background works finisheds, This method is called. Result of
         * doInBackground method is transmitted to onPostExecute's parameter
         */
        protected void onPostExecute(String page)
        {
            Gson gson = new Gson();
            simsimi sim = gson.fromJson(page,simsimi.class);
            if (sim == null)
            {
                Toast.makeText(getApplicationContext(),"NULL",Toast.LENGTH_LONG).show();
            }
            else {
                chatmodel model = new chatmodel(sim.getResponse(),false);
                list_chat.add(model);
                CustomAdapter adapter = new CustomAdapter(list_chat,getApplicationContext());
                listView.setAdapter(adapter);
                response.setText(sim.getResponse());
                adapter.notifyDataSetChanged();
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Let's speak");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry you device is not supported",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    your_message.setText(result.get(0));
                    message = your_message.getText().toString();
                }
                break;
            }

        }


    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
