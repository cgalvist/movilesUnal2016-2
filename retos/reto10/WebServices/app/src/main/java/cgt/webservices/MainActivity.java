package cgt.webservices;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText cuadranteText;
    EditText numContactoText;
    EditText deptText;
    EditText estText;
    TextView responseView;
    ProgressBar progressBar;
    //static final String API_KEY = "USE_YOUR_OWN_API_KEY";
    static final String API_URL = "https://www.datos.gov.co/resource/bc9h-88z3.json?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cuadranteText = (EditText) findViewById(R.id.cuadranteText);
        numContactoText = (EditText) findViewById(R.id.numContactoText);
        deptText = (EditText) findViewById(R.id.deptText);
        estText = (EditText) findViewById(R.id.estText);

        Button queryButton = (Button) findViewById(R.id.queryButton);
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RetrieveFeedTask().execute();
            }
        });
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;
        String cuadrante, numeroContacto, departamento, estacion;

        protected void onPreExecute() {
            setContentView(R.layout.activity_resultados);
            responseView = (TextView) findViewById(R.id.responseView);
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            responseView.setText("");
            cuadrante = cuadranteText.getText().toString();
            numeroContacto = numContactoText.getText().toString();
            departamento = deptText.getText().toString();
            estacion = estText.getText().toString();
        }

        protected String doInBackground(Void... urls) {
            // Do some validation here

            try {
                String parametros[] = {"","","",""};

                if(cuadrante.length() > 0) parametros[0] = "cuadrante=" + cuadrante + "&";
                if(numeroContacto.length() > 0) parametros[0] = "numerodecontacto=" + numeroContacto + "&";
                if(estacion.length() > 0) parametros[0] = "estacion=" + estacion + "&";
                if(departamento.length() > 0) parametros[0] = "departamento=" + departamento + "&";

                //URL url = new URL(API_URL + "cuadrante=" + cuadrante + "&apiKey=" + API_KEY);
                URL url = new URL(API_URL + parametros[0] + parametros[1] + parametros[2] + parametros[3]);
                System.out.println(API_URL + parametros[0] + parametros[1] + parametros[2] + parametros[3]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);
            responseView.setText(response);
            // TODO: check this.exception
            // TODO: do something with the feed

//            try {
//                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
//                String requestID = object.getString("requestId");
//                int likelihood = object.getInt("likelihood");
//                JSONArray photos = object.getJSONArray("photos");
//                .
//                .
//                .
//                .
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }
    }
}