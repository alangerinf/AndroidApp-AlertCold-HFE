package ibao.alanger.alertcoldhfe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ibao.alanger.alertcoldhfe.adapters.RViewAdapterSensorToSearchPallet;
import ibao.alanger.alertcoldhfe.app.AppController;
import ibao.alanger.alertcoldhfe.model.SensorIntoPallet;
import ibao.alanger.alertcoldhfe.model.SharedPreferencesManager;

public class SearchPalletActivity extends AppCompatActivity {

    String TAG = SearchPalletActivity.class.getSimpleName();
    Context ctx = this;


    int REQUEST_QR_NPALLET = 123;
    List<SensorIntoPallet> sensorAll = new ArrayList<>();
    EditText editText;
    AppCompatButton btnQR;
    RViewAdapterSensorToSearchPallet adapter;
    RecyclerView rView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_pallet);
        setTitle("Consulta Pallet");

        rView = findViewById(R.id.rView);
        btnQR = findViewById(R.id.btnQR);
        editText = findViewById(R.id.eTextSearchPallet);
        events();


    }




    private void consultarSensores(String token,String txt){

        Log.d(TAG,"entro en consulta");

        String url = ConectionConfig.POST_GETSENSOR+"/"+txt;
        Log.d(TAG,url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            int codigoRespuesta = response.getInt("codigoRespuesta");
                            sensorAll.clear();
                            adapter = new RViewAdapterSensorToSearchPallet(sensorAll);
                            rView.setAdapter(adapter);
                            if(codigoRespuesta==ConectionConfig.HTTP_OK) {

                                JSONObject datos = new JSONObject(String.valueOf(response.getJSONObject("datos")));
                                JSONArray sensores = datos.getJSONArray("sensores");


                                for (int i = 0; i < sensores.length(); i++) {
                                    //convertir cada Batch  por GSON
                                    Gson gson = new Gson();
                                    Log.d(TAG, sensores.getJSONObject(i).toString());
                                    SensorIntoPallet temp = gson.fromJson(sensores.getJSONObject(i).toString(), SensorIntoPallet.class);
                                    sensorAll.add(temp);
                                }
                            /*
                                tViewSinBatch.setVisibility(View.VISIBLE);
                                if(sensorAll.size()>0){
                                    tViewSinBatch.setVisibility(View.INVISIBLE);
                                }
*/
                                adapter = new RViewAdapterSensorToSearchPallet(sensorAll);
                                rView.setAdapter(adapter);


                            }else {
                                if(codigoRespuesta==ConectionConfig.HTTP_ERROR){
                                  //  tViewSinBatch.setVisibility(View.VISIBLE);
                                   // Toast.makeText(ctx,"Área sin Sensores",Toast.LENGTH_LONG).show();
                                    // onBackPressed();

                                }
                            }

                        } catch (JSONException e) {
                          //  Toast.makeText(ctx,"json"+e.toString(),Toast.LENGTH_LONG).show();
                            //tViewSinBatch.setVisibility(View.VISIBLE);
                            Log.d(TAG,e.toString());
                            e.printStackTrace();

                            // onBackPressed();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  tViewSinBatch.setVisibility(View.VISIBLE);
              //  Toast.makeText(ctx,error.toString(),Toast.LENGTH_LONG).show();
                Log.d(TAG,error.toString());
                error.printStackTrace();

                //     onBackPressed();
            }

        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String, String>();
                headers.put("Authorization",token);
                return headers;
            }

        };
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void events() {
        btnQR.setOnClickListener(v->{
            IntentIntegrator intentIntegrator =new IntentIntegrator(this);
            intentIntegrator
                    .setOrientationLocked(false)
                    .setCaptureActivity(CustomScannerActivity.class)
                    .setRequestCode(REQUEST_QR_NPALLET)
                    .initiateScan();
        });
        editText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if(!s.equals("") ) {
                    consultarSensores(SharedPreferencesManager.getUser(ctx).getToken(),editText.getText().toString());
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_QR_NPALLET) {

            try {
                Bundle recibidos = (data.getExtras());
                if (recibidos != null) {
                    String qr = recibidos.getString("qr");
                    editText.setText(qr);
                    consultarSensores(SharedPreferencesManager.getUser(ctx).getToken(),qr);
                }
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
        }
    }
}
