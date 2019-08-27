package ibao.alanger.alertcoldhfe;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ibao.alanger.alertcoldhfe.adapters.RViewAdapterMainBatch;
import ibao.alanger.alertcoldhfe.app.AppController;
import ibao.alanger.alertcoldhfe.model.Batch;
import ibao.alanger.alertcoldhfe.model.SharedPreferencesManager;
import ibao.alanger.alertcoldhfe.model.User;

public class FragmentListBatch extends Fragment {


    private OnFragmentInteractionListener mListener;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    RecyclerView rViewBatch;
    RViewAdapterMainBatch adapter;
    Context ctx;
    List<Batch> BATCHLIST;
    AppCompatTextView tViewDateFilter;
    AppCompatTextView tViewSinBatch;

    ConstraintLayout clEditStart;

    String TAG = FragmentListBatch.class.getSimpleName();


    // TODO: Rename and change types and number of parameters
    public static FragmentListBatch newInstance(String param1, String param2) {
        FragmentListBatch fragment = new FragmentListBatch();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        define();
        events();


        Date dateFin = new Date();
        Date dateIni;
        //obtengo hora del sistema:
        Calendar c = Calendar.getInstance();
        c.setTime(dateFin);
        dateIni = c.getTime();
        c.add(Calendar.DATE, 1);
        dateFin = c.getTime();
        //

        DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd");

        String tIni = hourFormat.format(dateIni);
        String tFin= hourFormat.format(dateFin);

        tViewDateFilter.setText(tIni);

        User user = SharedPreferencesManager.getUser(ctx);

        consultarSensores(user.getToken(),""+tIni,""+tFin);

    }

    private void startAdapter() {
        adapter = new RViewAdapterMainBatch(BATCHLIST);
        rViewBatch.setAdapter(adapter);
    }


    public void openDialogFecha(final AppCompatTextView tViewFecha) {
        Dialog dialog;
        dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.datepicker);

        final DatePicker datePicker = (DatePicker)dialog.findViewById(R.id.datePicker);

        String strFecha= tViewFecha.getText().toString();
        final String fecha = strFecha;
        int year=0;
        int month=0;
        int day=0;
        try{
            String[] parts = fecha.split("-");
            year = Integer.parseInt(parts[0]);
            month = Integer.parseInt(parts[1])-1;
            day = Integer.parseInt(parts[2]);
        }catch (Exception e){
            Log.d(TAG,e.toString());
        }


        if(year != 0 && month != 0 && day != 0) {
            datePicker.updateDate(year, month, day);
        }

        final Button btn_aceptar = (Button)dialog.findViewById(R.id.btn_aceptar);
        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth()+1;
                int year = datePicker.getYear();

                String dia = String.valueOf(day);
                String dia2= String.valueOf((day+1));
                String mes = String.valueOf(month);
                String anio = String.valueOf(year);

                if(dia.length()==1){
                    dia = "0"+dia;
                }

                if(dia2.length()==1){
                    dia2 = "0"+dia2;
                }

                if(mes.length()==1){
                    mes = "0"+mes;
                }


                Date date = new Date();
                date.setYear(Integer.parseInt(anio));
                date.setMonth(Integer.parseInt(mes+1));
                date.setDate(Integer.parseInt(dia));

                String fechaI = anio+"-"+mes+"-"+dia;
                String fechaF = anio+"-"+mes+"-"+dia;

                tViewFecha.setText(fechaI);
                User user = SharedPreferencesManager.getUser(ctx);

                dialog.dismiss();

                consultarSensores(user.getToken(),""+fechaI,""+fechaF);

                //BuscarServicios();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }

    private void events() {

        clEditStart.setOnClickListener(
                v -> {
                    openDialogFecha(tViewDateFilter);
                }
        );

        /*
        tViewClose.setOnClickListener(
                v->{
                   closeSession();
                }
        );
        iViewClose.setOnClickListener(
                v->{
                   closeSession();
                }
        );
*/

    }
    /*
    void closeSession(){

        SharedPreferencesManager.deleteUser(ctx);
        startActivity(new Intent(ctx, ActivityPreloader.class));
        finish();
    }

    @Override
    public void onBackPressed() {

        getActivity().moveTaskToBack(true);
    }
*/
    private void define() {
        ctx = getActivity();
        tViewSinBatch = getView().findViewById(R.id.tViewSinBatch);
        rViewBatch = getView().findViewById(R.id.rViewBatch);
        tViewDateFilter = getView().findViewById(R.id.tViewDateFilter);
        clEditStart = getView().findViewById(R.id.clEditStart);
        BATCHLIST  = new ArrayList<>();


    }

    private void consultarSensores(String token,String fechaIni,String fechaFin){
        ProgressDialog progressDialog = new ProgressDialog(ctx);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Buscando Sensores");
        progressDialog.show();
        Log.d(TAG,"entro en consulta");

        String url = ConectionConfig.POST_GETPERIODOS+"/"+fechaIni+"%2000:00:00/"+fechaFin+"%2023:59:59";
        Log.d(TAG,url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            int codigoRespuesta = response.getInt("codigoRespuesta");
                            if(codigoRespuesta==ConectionConfig.HTTP_OK) {

                                JSONObject datos = new JSONObject(String.valueOf(response.getJSONObject("datos")));
                                JSONArray periodos = datos.getJSONArray("periodos");
                                BATCHLIST.clear();

                                for(int i=0;i<periodos.length();i++){
                                    //convertir cada Batch  por GSON
                                    Gson gson = new Gson();
                                    Log.d(TAG,periodos.getJSONObject(i).toString());
                                    Batch batchTemp = gson.fromJson(periodos.getJSONObject(i).toString(), Batch.class);
                                    BATCHLIST.add(batchTemp);
                                }

                                tViewSinBatch.setVisibility(View.VISIBLE);
                                if(BATCHLIST.size()>0){
                                    tViewSinBatch.setVisibility(View.INVISIBLE);
                                }

                                adapter = new RViewAdapterMainBatch(BATCHLIST);
                                rViewBatch.setAdapter(adapter);
                                progressDialog.dismiss();
                                events();
                            }else {
                                if(codigoRespuesta==ConectionConfig.HTTP_ERROR){
                                    tViewSinBatch.setVisibility(View.VISIBLE);
                                    Toast.makeText(ctx,"Área sin Sensores",Toast.LENGTH_LONG).show();
                                   // onBackPressed();
                                    progressDialog.dismiss();
                                }
                            }

                        } catch (JSONException e) {
                            Toast.makeText(ctx,"json"+e.toString(),Toast.LENGTH_LONG).show();
                            tViewSinBatch.setVisibility(View.VISIBLE);
                            Log.d(TAG,e.toString());
                            e.printStackTrace();
                            progressDialog.dismiss();
                           // onBackPressed();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tViewSinBatch.setVisibility(View.VISIBLE);
                Toast.makeText(ctx,error.toString(),Toast.LENGTH_LONG).show();
                Log.d(TAG,error.toString());
                error.printStackTrace();
                progressDialog.dismiss();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView = inflater.inflate(R.layout.fragment_list_batch, container, false);


        return RootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: ActivityUpdate argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
