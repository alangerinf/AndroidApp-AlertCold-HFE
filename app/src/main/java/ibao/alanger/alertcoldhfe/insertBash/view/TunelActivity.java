package ibao.alanger.alertcoldhfe.insertBash.view;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ibao.alanger.alertcoldhfe.ActivityMain;
import ibao.alanger.alertcoldhfe.ConectionConfig;
import ibao.alanger.alertcoldhfe.EditBasicBash;
import ibao.alanger.alertcoldhfe.R;
import ibao.alanger.alertcoldhfe.adapters.RViewAdapterPalletToBatch;
import ibao.alanger.alertcoldhfe.app.AppController;
import ibao.alanger.alertcoldhfe.editPallet.editPalletActivity;
import ibao.alanger.alertcoldhfe.model.Batch;
import ibao.alanger.alertcoldhfe.model.Pallet;
import ibao.alanger.alertcoldhfe.model.Sensor;
import ibao.alanger.alertcoldhfe.model.SharedPreferencesManager;
import ibao.alanger.alertcoldhfe.model.User;

public class TunelActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    private  static List<Sensor> sensorListAll;
    private static List<Sensor> sensorListAcutal;

    Context ctx;
    private static RViewAdapterPalletToBatch adapter;

    private static RecyclerView rViewPallets;

    private static int REQUESTCODE_EDITPALLET=1001;

    static LottieAnimationView gear;

    FloatingActionButton fAButtonUpload;

    Batch BATCH;

    String TAG = TunelActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tunel);
        setTitle("Registro Batch");

        define();
        getExtras();
        addPallets();
        events();
        startAnimation();
        User user = SharedPreferencesManager.getUser(ctx);
        progressDialog.show();
        consultarSensores(user.getToken());
    }

    private void startAnimation() {
        final Animation anim_rightFadeIn2 =
                android.view.animation.AnimationUtils.loadAnimation(getBaseContext(),R.anim.bot_fade_in);
        Handler handler2 = new Handler();
        handler2.post(
                () -> {
                    rViewPallets.startAnimation(anim_rightFadeIn2);
                    rViewPallets.setVisibility(View.VISIBLE);
                }
        );
    }
    Handler handler = new Handler();
    private void events() {

        gear.setOnClickListener(v -> {
            startActivity(new Intent(this, EditBasicBash.class));
        });
        fAButtonUpload.setOnClickListener(v ->{
            handler.post(()->{
                adapter.verificar();
                User user = SharedPreferencesManager.getUser(ctx);
                uploadBATCH(user.getToken());
                Log.d(TAG,BATCH.toString());
                // adapter.setModeVerify(true);
                // adapter.notifyDataSetChanged();
            });
        });
    }

    private void getExtras() {
        Bundle b = getIntent().getExtras();
        BATCH = (Batch) b.getSerializable("BATCH");
    }

    private void addPallets() {

        Log.d(TAG,"BATCH="+BATCH.toString());
        for(int i=0;i<BATCH.getNumPallets();i++){
            Pallet pallet = new Pallet(i+1);
            BATCH.getPalletList().add(pallet);
        }
        adapter = new RViewAdapterPalletToBatch(BATCH.getPalletList());
        adapter.setOnClicListener(v -> {
            obtenerSensoresRestantes();
            int pos = rViewPallets.getChildAdapterPosition(v);
            Pallet item = BATCH.getPalletList().get(pos);
            Intent i = new Intent(this, editPalletActivity.class);
            View viewTemp = v;
            TextView tViewNOrden = v.findViewById(R.id.tViewNOrden);
            ActivityOptions options = (ActivityOptions) ActivityOptions.makeSceneTransitionAnimation
                    (this,
                            Pair.create(viewTemp, viewTemp.getTransitionName()),
                            Pair.create((View)tViewNOrden, tViewNOrden.getTransitionName())
                    );
            Bundle bundleExtra = new Bundle();
            bundleExtra.putInt("position",pos);
            bundleExtra.putSerializable("pallet",item);
            bundleExtra.putSerializable("sensorList", (Serializable) sensorListAcutal);
            i.putExtras(bundleExtra);
            startActivityForResult(i,REQUESTCODE_EDITPALLET, options.toBundle());
            /*handler.postDelayed(()->{
                adapter.setModeVerify(false);
                adapter.notifyDataSetChanged();
            },500);*/
        });
        rViewPallets.setAdapter(adapter);
    }


    private void uploadBATCH(String token){
        progressDialog.setTitle("Subiendo BATCH");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.d(TAG,"entro en consulta");

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(BATCH.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                ConectionConfig.POST_BATCH_UPLOAD, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        progressDialog.dismiss();
                        try {
                            int codigoRespuesta = response.getInt("codigoRespuesta");
                            if(codigoRespuesta==ConectionConfig.HTTP_OK) {

                                    Intent i  = new Intent(ctx, ActivityMain.class);
                                    startActivity(i);
                                    Toast.makeText(ctx,"Subido con Exito",Toast.LENGTH_LONG).show();
                                    finish();


                            }else {
                                    Toast.makeText(ctx,response.getString("mensajeRespuesta"),Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(ctx,"json"+e.toString(),Toast.LENGTH_LONG).show();
                            Log.d(TAG,e.toString());
                            e.printStackTrace();
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx,error.toString(),Toast.LENGTH_LONG).show();
                Log.d(TAG,error.toString());
                progressDialog.dismiss();
                finish();
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



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        showPopupRecomendacion("Perderá el Progreso al Retroceder\n¿Desea Continuar?");
    }

    private void showPopupRecomendacion(String mensaje){
        Dialog dialogClose;
        dialogClose = new Dialog(this);
        dialogClose.setContentView(R.layout.dialog_recomendaciones);
        Button btnDialogClose = (Button) dialogClose.findViewById(R.id.buton_close_dialog);
        ImageView iViewDialogClose = (ImageView) dialogClose.findViewById(R.id.iViewDialogClose);
        TextView tViewRecomendacion = dialogClose.findViewById(R.id.tViewRecomendacion);
        tViewRecomendacion.setText(mensaje);
        iViewDialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogClose.dismiss();
            }
        });
        btnDialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogClose.dismiss();
                finish();
            }
        });

        dialogClose.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogClose.show();
    }

    private void consultarSensores(String token){
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Buscando Sensores");
        progressDialog.show();
        Log.d(TAG,"entro en consulta");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("zona", BATCH.getArea().getId());
            jsonObject.put("subZona", BATCH.getZone().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                ConectionConfig.POST_GETSENSOR, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            int codigoRespuesta = response.getInt("codigoRespuesta");
                            if(codigoRespuesta==ConectionConfig.HTTP_OK) {

                                JSONObject datos = new JSONObject(String.valueOf(response.getJSONObject("datos")));
                                JSONArray sensores = datos.getJSONArray("sensores");
                                for(int i=0;i<sensores.length();i++){
                                    Sensor sensor = new Sensor();
                                    JSONObject productTemp = sensores.getJSONObject(i);
                                    sensor.setId(productTemp.getString("ID"));
                                    sensor.setName(productTemp.getString("NOMBRE"));
                                    sensorListAll.add(sensor);
                                    sensorListAcutal.add(sensor);
                                }

                                progressDialog.dismiss();
                                events();
                            }else {
                                if(codigoRespuesta==ConectionConfig.HTTP_ERROR){
                                    Toast.makeText(ctx,"Área sin Sensores",Toast.LENGTH_LONG).show();
                                    onBackPressed();
                                    progressDialog.dismiss();
                                }
                            }

                        } catch (JSONException e) {
                            Toast.makeText(ctx,"json"+e.toString(),Toast.LENGTH_LONG).show();

                            Log.d(TAG,e.toString());
                            e.printStackTrace();
                            progressDialog.dismiss();
                            onBackPressed();
                        }
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ctx,error.toString(),Toast.LENGTH_LONG).show();
                    Log.d(TAG,error.toString());
                    error.printStackTrace();
                    progressDialog.dismiss();
                    onBackPressed();
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

    private void define() {
        ctx = this;
        progressDialog = new ProgressDialog(ctx);

        fAButtonUpload = findViewById(R.id.fAButtonUpload);
        gear = findViewById(R.id.gear);
        rViewPallets = findViewById(R.id.rViewPallets);
        sensorListAll = new ArrayList<>();
        sensorListAcutal= new ArrayList<>();

    }


    private void obtenerSensoresRestantes(){

        List<Sensor> sensorListTemp = new ArrayList<>();//sensores en todos los pallets
        sensorListAcutal= new ArrayList<>();
        sensorListAcutal.addAll(sensorListAll);
        for(int i=0;i<BATCH.getPalletList().size();i++){// entrando a cada pallet
            Pallet palletTemp = BATCH.getPalletList().get(i);
            sensorListTemp.addAll(palletTemp.getSensorList());//entregando todos los sensores
            Log.d(TAG,"se añadio "+palletTemp.getSensorList().size()+" sensores i="+ i);
            Log.d(TAG,"total "+sensorListTemp.size()+" sensores i="+ palletTemp.getSensorList().size());

        }
        Log.d(TAG,"lista temporal sensorListTemp "+sensorListTemp.size()+" sensores");
        Log.d(TAG,"añadido a sensorListAcutal "+sensorListAcutal.size()+" sensores");

        for(Sensor s: sensorListAcutal){
            Log.d(TAG,"-"+s.getId());
        }
        for(Sensor s: sensorListTemp){
            Log.d(TAG,"+ "+s.getId());
        }



        for(int i=0;i<sensorListAcutal.size();i++){
            for(int j=0;j<sensorListTemp.size();j++){
                Log.d(TAG,"=======================================");
                Log.d(TAG,"comparando A:"+sensorListAcutal.get(i).getId());
                Log.d(TAG,"comparando B:"+sensorListTemp.get(j).getId());

                if(sensorListAcutal.get(i).getId().equals(sensorListTemp.get(j).getId())){//si tienen el mismo sensor
                    Sensor sensorRemovido = sensorListAcutal.remove(i);
                    Log.d(TAG,"removiendo: "+i+" "+sensorRemovido.getId());

                    i--;
                    break;
                }
                Log.d(TAG,"=======================================");
            }
        }//al terminar queda la lista de  sensores restados los de los q estas ya instanciados y se almacenan en <sensorListActual>
        Log.d(TAG,"quedaron sensorListAcutal "+sensorListAcutal.size()+" sensores");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = data.getExtras();
        assert bundle != null;
        Pallet palletTemp = (Pallet) bundle.getSerializable("pallet");
        Log.d(TAG,"data para remplazar"+palletTemp.toString());

        Log.d(TAG,"se recibio: "+palletTemp.toString());
        if (requestCode == REQUESTCODE_EDITPALLET) {
            Pallet pallet;
            pallet = BATCH.getPalletList().get(palletTemp.getPosition()-1);
            Log.d(TAG,"data por remplazar"+pallet.toString());
            pallet.setCod(palletTemp.getCod());
            pallet.setSensorList(palletTemp.getSensorList());
            /*
            //removiando los  sensores agregados
            Log.d(TAG,"tamaño previo sensores "+ sensorListAll.size());
            for(int i = 0; i< sensorListAll.size(); i++ ){
                for(int j=0;j<pallet.getSensorList().size();j++){
                    Log.d(TAG,"*************");
                    Log.d(TAG,"Comparando 1:"+ sensorListAll.get(i).getId());
                    Log.d(TAG,"Comparando 2:"+pallet.getSensorList().get(j).getId());
                    if(pallet.getSensorList().get(j).getId().equals(sensorListAll.get(i).getId())){
                        Log.d(TAG,"tam antes "+ sensorListAll.size()+" ");
                        Log.d(TAG,"intentando descontar pos "+i+" ");
                        sensorListAll.remove(i);//al quitanle  baja la cantidad porq sino va ha saltar el siugiente elemento porq se elimino el q tienen match y vana  correer los elementos
                        i--;
                        Log.d(TAG,"tam pos "+ sensorListAll.size()+" ");
                        break;
                    }
                    Log.d(TAG,"*************");

                }
            }
            //sensorListAll.removeAll(pallet.getSensorList() );
            Log.d(TAG,"tamaño posterior de  sensores restantes"+ sensorListAll.size());
            Log.d(TAG,"se intentaron descontar sensores "+palletTemp.getSensorList().size());
*/

            //adapter.setModeVerify(false);
            adapter = new RViewAdapterPalletToBatch(BATCH.getPalletList());
            adapter.setOnClicListener(v -> {
                obtenerSensoresRestantes();
                int pos = rViewPallets.getChildAdapterPosition(v);
                Pallet item = BATCH.getPalletList().get(pos);
                Intent i = new Intent(this, editPalletActivity.class);
                View viewTemp = v;
                TextView tViewNOrden = v.findViewById(R.id.tViewNOrden);
                ActivityOptions options = (ActivityOptions) ActivityOptions.makeSceneTransitionAnimation
                        (this,
                                Pair.create(viewTemp, viewTemp.getTransitionName()),
                                Pair.create((View)tViewNOrden, tViewNOrden.getTransitionName())
                        );
                Bundle bundleExtra = new Bundle();
                bundleExtra.putInt("position",pos);
                bundleExtra.putSerializable("pallet",item);
                bundleExtra.putSerializable("sensorList", (Serializable) sensorListAcutal);
                i.putExtras(bundleExtra);
                startActivityForResult(i,REQUESTCODE_EDITPALLET, options.toBundle());
            /*handler.postDelayed(()->{
                adapter.setModeVerify(false);
                adapter.notifyDataSetChanged();
            },500);*/
            });
            rViewPallets.setAdapter(adapter);
        }

    }
}
