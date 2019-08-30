package ibao.alanger.alertcoldhfe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ibao.alanger.alertcoldhfe.app.AppController;
import ibao.alanger.alertcoldhfe.insertBash.view.TunelActivity;
import ibao.alanger.alertcoldhfe.model.Area;
import ibao.alanger.alertcoldhfe.model.Batch;
import ibao.alanger.alertcoldhfe.model.Product;
import ibao.alanger.alertcoldhfe.model.SharedPreferencesManager;
import ibao.alanger.alertcoldhfe.model.User;
import ibao.alanger.alertcoldhfe.model.Zone;

public class EditBasicBash extends AppCompatActivity {

    String TAG = EditBasicBash.class.getSimpleName();

    private static Batch BATCH;

    Context ctx;
    Dialog dialog;
    //botones de  editar
        AppCompatButton btnEditFechaInicio;
        AppCompatButton btnEditHoraInicio;
        AppCompatButton btnEditFechaFin;
        AppCompatButton btnEditHoraFin;
    //boton de ok
        AppCompatButton btnOk;
    //spinners
        AppCompatSpinner spnArea;
        AppCompatSpinner spnZona;
        AppCompatSpinner spnProducto;
    //textViews
        AppCompatTextView tViewFechaInicio;
        AppCompatTextView tViewHoraInicio;
        AppCompatTextView tViewFechaFin;
        AppCompatTextView tViewHoraFin;
    //editText
        AppCompatEditText eTextNumPallets;
        AppCompatEditText eTextBatchName;


        List<Product> productListAll;
        List<Area> areaListAll;
        List<Zone> zoneListAll;



    List<Product> productList;
        List<Area> areaList;
        List<Zone> zoneList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTitle("Registro Batch");
        setContentView(R.layout.activity_edit_basic_bash);

        define();
       // events(); //comentado por error antes de la carga los envtos son cargados al consultar la data sino no cargan
    }


    private void define(){
        BATCH = new Batch();
        ctx = this;
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Consultando Áreas");
        //listas filtradas
        productList = new ArrayList<>();
        areaList    = new ArrayList<>();
        zoneList    = new ArrayList<>();
        //listas sin filtrar
        productListAll = new ArrayList<>();
        areaListAll    = new ArrayList<>();
        zoneListAll    = new ArrayList<>();

        //botones de  editar
            btnEditFechaInicio  = findViewById(R.id.editBatch_btnEditFechaInicio);
            btnEditHoraInicio   = findViewById(R.id.editBatch_btnEditHoraInicio);
            btnEditFechaFin     = findViewById(R.id.editBatch_btnEditFechaFin);
            btnEditHoraFin      = findViewById(R.id.editBatch_btnEditHoraFin);
        //botones de  aceptar
            btnOk   = findViewById(R.id.editBatch_btnOk);
        //spinner
            spnArea     = findViewById(R.id.editBatch_spnArea);
            spnZona     = findViewById(R.id.editBatch_spnZona);
            spnProducto = findViewById(R.id.editBatch_spnProducto);
        //eTexts
            tViewFechaInicio    = findViewById(R.id.editBatch_tViewFechaInicio);
            tViewHoraInicio     = findViewById(R.id.editBatch_tViewHoraInicio);
            tViewFechaFin       = findViewById(R.id.editBatch_tViewFechaFin);
            tViewHoraFin        = findViewById(R.id.editBatch_tViewHoraFin);
        //editText
            eTextNumPallets     = findViewById(R.id.editBatch_eTextNumPallets);
            eTextBatchName      = findViewById(R.id.editBatch_eTextBatchName);

        /***
         * Contectarse a la db para obtener todas als listas  de  un porrazo
         * luego cargar areas
         */
        loadAll();

    }


    private void consultBasics(String token){
        Log.d(TAG,"entro en consulta");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                ConectionConfig.GET_GETBASICS, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            int codigoRespuesta = response.getInt("codigoRespuesta");
                            String mensajeRespuesta = response.getString("mensajeRespuesta");
                            if(codigoRespuesta==ConectionConfig.HTTP_OK) {

                                JSONObject datos = new JSONObject(String.valueOf(response.getJSONObject("datos")));
                                Log.d(TAG,"f1");
                                    JSONArray areas = datos.getJSONArray("zonas");
                                    JSONArray productos = datos.getJSONArray("productos");
                                Log.d(TAG,"f2");
                                    for(int i=0;i<productos.length();i++){
                                        Product product = new Product();
                                        JSONObject productTemp = productos.getJSONObject(i);
                                            product.setId(productTemp.getString("ID"));
                                            product.setName(productTemp.getString("NOMBRE"));
                                        productListAll.add(product);
                                    }
                                Log.d(TAG,"f3");
                                    for(int i=0;i<areas.length();i++){
                                        Area area = new Area();
                                        JSONObject areaTemp = areas.getJSONObject(i);
                                            area.setId(areaTemp.getString("ID"));
                                            area.setName(areaTemp.getString("NOMBRE"));
                                        JSONArray zonasListTemp = areaTemp.getJSONArray("SUB_ZONAS");
                                        for(int j=0;j<zonasListTemp.length();j++){
                                            Zone zone = new Zone();
                                            JSONObject zonaTemp = zonasListTemp.getJSONObject(j);
                                                zone.setId(zonaTemp.getString("ID"));
                                                zone.setName(zonaTemp.getString("ID"));
                                                zone.setIdArea(area.getId());
                                            zoneListAll.add(zone);
                                        }
                                        if(zonasListTemp.length()>0){
                                            areaListAll.add(area);
                                        }

                                    }
                                Log.d(TAG,"f4");
                                    progressDialog.dismiss();


                                String mensaje ="";

                                if (productListAll.size() == 0 ) {
                                    Toast.makeText(ctx, "Sin Productos", Toast.LENGTH_LONG).show();

                                    mensaje = "Sin Productos";

                                }
                                if (zoneListAll.size() == 0 ) {
                                    if(!mensaje.equals("")){
                                        mensaje=mensaje+"\n";
                                    }

                                    mensaje=mensaje+"Sin Zonas";

                                }
                                if (areaListAll.size() == 0 ) {
                                    if(!mensaje.equals("")){
                                        mensaje=mensaje+"\n";
                                    }
                                    mensaje=mensaje+"Sin Areas";


                                }


                                if(productListAll.size()>0 && zoneListAll.size()>0 && areaListAll.size()>0){
                                    loadArea();
                                    loadProduct();
                                    events();

                                }else {
                                    Toast.makeText(ctx,mensaje,Toast.LENGTH_LONG).show();

                                    onBackPressed();
                                }






                            }else {
                                if(codigoRespuesta==ConectionConfig.HTTP_ERROR){
                                    Toast.makeText(ctx,mensajeRespuesta,Toast.LENGTH_LONG).show();
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

    ProgressDialog progressDialog;

    private void loadAll() {
            User user = SharedPreferencesManager.getUser(ctx);

            progressDialog.show();
            consultBasics(user.getToken());

    }

    private void loadArea() {
        areaList = new ArrayList<>();
        areaList.addAll(areaListAll);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ctx,R.layout.spinner_item,getArrayArea(areaList));
        spnArea.setAdapter(adapter);
        loadZona();
    }

    private void loadZona() {
        //conectar con la api antes para obtener zonas segun idArea;
        zoneList = new ArrayList<>();
        int pos = spnArea.getSelectedItemPosition();
        String idArea = areaList.get(pos).getId();
        for(Zone z : zoneListAll){
            if(z.getIdArea()==idArea){
                zoneList.add(z);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ctx,R.layout.spinner_item,getArrayZone(zoneList));
        spnZona.setAdapter(adapter);
       // loadProduct();
    }

    private void loadProduct() {
        productList = new ArrayList<>();
        productList.addAll(productListAll);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ctx,R.layout.spinner_item,getArrayProduct(productList));
        spnProducto.setAdapter(adapter);
    }

    private List<String> getArrayProduct(List<Product> productListTemp) {
        List<String> list = new ArrayList<>();
        for(Product a: productListTemp){
            list.add(a.getName());
        }
        return list;
    }

    private List<String> getArrayArea(List<Area> areaListTemp) {
        List<String> list = new ArrayList<>();
        for(Area a: areaListTemp){
            list.add(a.getName());
        }
        return list;
    }

    private List<String> getArrayZone(List<Zone> zoneListTemp) {
        List<String> list = new ArrayList<>();
        for(Zone a: zoneListTemp){
            list.add(a.getName());
        }
        return list;
    }
    private void events() {

        JSONObject json = new JSONObject();


        spnArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadZona();
                spnZona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        eTextNumPallets.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if(!eTextNumPallets.getText().toString().equals("")){
                    BATCH.setNumPallets(Integer.parseInt(eTextNumPallets.getText().toString()));
                }else {
                    BATCH.setNumPallets(0);
                }
                try {
                    if(Integer.valueOf(eTextNumPallets.getText().toString())==0){
                        eTextNumPallets.setText("");
                        BATCH.setNumPallets(0);
                    }
                }catch (Exception e){

                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });

        eTextBatchName.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if(eTextBatchName.getText().equals("")){
                    BATCH.setNameBatch("");
                }else {
                    BATCH.setNameBatch(eTextBatchName.getText().toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });
        btnOk.setOnClickListener(v->{
            if(checkTunel()){

                JSONObject temp = new JSONObject();
                try {
                    temp.put("grupo_id",areaList.get(spnArea.getSelectedItemPosition()).getName());
                    temp.put("subgrupo_id",zoneList.get(spnZona.getSelectedItemPosition()).getName());
                    temp.put("tipo_id",productList.get(spnProducto.getSelectedItemPosition()).getName());
                    temp.put("periodo",BATCH.getNameBatch());
                    temp.put("fecha_inicio",!BATCH.getTimeStart().equals("")?BATCH.getDateStart()+" "+BATCH.getTimeStart()+":00":"");
                    temp.put("fecha_fin",!BATCH.getTimeEnd().equals("")?BATCH.getDateEnd()+" "+BATCH.getTimeEnd()+":00":"");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    String dateIni =  temp.getString("fecha_inicio");
                    String dateFin =  temp.getString("fecha_fin");

                    SimpleDateFormat sdf = new  SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );
                    // El método parse devuelve null si no se ha podido parsear el string en  según el formato indicado. Este método lanza una excepción NullPointer  exception si alguno de sus parámetros es null
                    Date fecha1 = sdf.parse(dateIni , new ParsePosition(0));

                    // El método parse devuelve null si no se ha podido parsear el string en  según el formato indicado. Este método lanza una excepción NullPointer  exception si alguno de sus parámetros es null
                    Date fecha2 = sdf.parse(dateFin , new ParsePosition(0));
                    Log.d(TAG,"compara Ini"+fecha1);
                    Log.d(TAG,"compara Fin"+fecha2);

                    if(fecha2!=null){
                        if ( fecha1.before(fecha2) ) { //Create

                            Log.d(TAG,"verificando" + temp.toString());
                            validarBatch(SharedPreferencesManager.getUser(ctx).getToken(),temp);
                        }
                        else{
                            System.out.println(" Fecha1 NO es anterior a fecha2.");
                            Toast.makeText(ctx,"La fecha fin no debe ser menor/igual que fecha inicio",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        validarBatch(SharedPreferencesManager.getUser(ctx).getToken(),temp);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });
        btnEditFechaFin.setOnClickListener(v->{
            openEditFechaFin();
        });
        btnEditFechaInicio.setOnClickListener(v->{
            openEditFechaInicio();
        });
        btnEditHoraFin.setOnClickListener(v->{
            openEditHoraFin();
        });
        btnEditHoraInicio.setOnClickListener(v->{
            openEditHoraInicio();
        });
    }




    private void validarBatch(String token, JSONObject json){

        Log.d(TAG,"entro en consulta");

        String url = ConectionConfig.POST_GETPERIODOS+"/validar";
        Log.d(TAG,url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            int codigoRespuesta = response.getInt("codigoRespuesta");

                            if(codigoRespuesta==ConectionConfig.HTTP_OK) {

                                int valido = response.getInt("valido");

                                if (valido==1){
                                    goToTunel();
                                }else {
                                    Toast.makeText(ctx,response.getString("mensajeRespuesta"),Toast.LENGTH_SHORT).show();
                                }


                            }else {
                                if(codigoRespuesta==ConectionConfig.HTTP_ERROR){
                                    //goToTunel();
                                    Toast.makeText(ctx,response.getString("mensajeRespuesta"),Toast.LENGTH_SHORT).show();
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

    private boolean checkTunel(){
        boolean response = true;

        if(eTextBatchName.getText().toString().isEmpty()){
            eTextBatchName.setError("Ingrese Nombre del Batch");
            response= false;
        }
        if(
                (
                    (BATCH.getDateEnd()==null||BATCH.getDateEnd().isEmpty())
                    &&
                    (BATCH.getTimeEnd()==null||BATCH.getTimeEnd().isEmpty())
                )
                ||
                (
                !(BATCH.getDateEnd()==null||BATCH.getDateEnd().isEmpty())//lleno
                &&
                !(BATCH.getTimeEnd()==null||BATCH.getTimeEnd().isEmpty())//llenov
                )
        ){

            //is ok
        }else {
            if(BATCH.getDateEnd()==null||BATCH.getDateEnd().isEmpty()){
                tViewFechaFin.setError("Elija Fecha de Fin");
                response= false;
            }
            if(BATCH.getTimeEnd()!=null||!BATCH.getTimeEnd().isEmpty()){
                tViewHoraFin.setError("Elija Fecha de Fin");
                response= false;
            }
        }
        if(BATCH.getDateStart()==null||BATCH.getDateStart().isEmpty()){
            tViewFechaInicio.setError("Elija Fecha de Inicio");
            response= false;
        }

        if(BATCH.getTimeStart()==null||BATCH.getTimeStart().isEmpty()){
            tViewHoraInicio.setError("Elija Hora de Inicio");
            response= false;
        }
        if(eTextNumPallets.getText().toString().isEmpty()){
            eTextNumPallets.setError("Ingrese un Número de Pallets válido");
            response= false;
        }


        return response;
    }

    private void goToTunel(){
        Log.d(TAG,""+BATCH.toString());
        BATCH.setArea(areaList.get(spnArea.getSelectedItemPosition()));
        BATCH.setZone(zoneList.get(spnZona.getSelectedItemPosition()));
        BATCH.setProduct(productList.get(spnProducto.getSelectedItemPosition()));

        Bundle bundle = new Bundle();
        bundle.putSerializable("BATCH",BATCH);
        Intent i = new Intent(this, TunelActivity.class);
        i.putExtras(bundle);
        startActivity(i);
    }

    private void openEditFechaFin(){
        openDialogFecha(tViewFechaFin);
    }

    private void openEditHoraFin(){
        openDialogHora(tViewHoraFin);
    }
    private void openEditHoraInicio(){
        openDialogHora(tViewHoraInicio);
    }

    private void openEditFechaInicio(){
        openDialogFecha(tViewFechaInicio);
    }
    public void openDialogFecha(final TextView tViewFecha) {
        tViewFecha.setError(null);
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.datepicker);

        final DatePicker datePicker = dialog.findViewById(R.id.datePicker);

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

        final Button btn_aceptar = dialog.findViewById(R.id.btn_aceptar);
        btn_aceptar.setOnClickListener(view -> {
            int day1 = datePicker.getDayOfMonth();
            int month1 = datePicker.getMonth()+1;
            int year1 = datePicker.getYear();

            String dia = String.valueOf(day1);
            String mes = String.valueOf(month1);
            String anio = String.valueOf(year1);

            if(dia.length()==1){
                dia = "0"+dia;
            }

            if(mes.length()==1){
                mes = "0"+mes;
            }

            Date date = new Date();
            date.setYear(Integer.parseInt(anio));
            date.setMonth(Integer.parseInt(mes+1));
            date.setDate(Integer.parseInt(dia));

            String fechaF = anio+"-"+mes+"-"+dia;


            tViewFecha.setText(fechaF);
            if(tViewFecha==tViewFechaInicio){
                BATCH.setDateStart(fechaF);
            }else if(tViewFecha == tViewFechaFin) {
                BATCH.setDateEnd(fechaF);
            }
            dialog.dismiss();

            //BuscarServicios();
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }


    public void openDialogHora(final TextView tViewHora) {
        tViewHora.setError(null);
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.hourpicker);

        final TimePicker timePicker = dialog.findViewById(R.id.hour_datePicker1);
        timePicker.setIs24HourView(true);
        String strFecha= tViewHora.getText().toString();
        final String fecha = strFecha;
        int hour=0;
        int minute=0;

        try{
            String[] parts = fecha.split(":");
            hour = Integer.parseInt(parts[0]);
            minute = Integer.parseInt(parts[1]);

        }catch (Exception e){
            Log.d(TAG,e.toString());
        }


        if(hour != 0 && minute != 0 ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                timePicker.setHour(hour);
                timePicker.setMinute(minute);
            }
        }

        final Button btn_aceptar = dialog.findViewById(R.id.hour_date_btn_aceptar);
        btn_aceptar.setOnClickListener(view -> {

            int hourTemp = 0;
            int minuteTemp =0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                hourTemp = timePicker.getHour();
                minuteTemp = timePicker.getMinute();
            }


            String hour_ = String.valueOf(hourTemp);
            String minute_ = String.valueOf(minuteTemp);


            if(hour_.length()==1){
                hour_ = "0"+hour_;
            }

            if(minute_.length()==1){
                minute_ = "0"+minute_;
            }

            String fechaF = hour_+":"+minute_;


            tViewHora.setText(fechaF);
            if(tViewHora==tViewHoraInicio){
                BATCH.setTimeStart(fechaF);
            }else if(tViewHora == tViewHoraFin) {
                BATCH.setTimeEnd(fechaF);
            }
            dialog.dismiss();

            //BuscarServicios();
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }


    void consultarZonas(){

    }
}
