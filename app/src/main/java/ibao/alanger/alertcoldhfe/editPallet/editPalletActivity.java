package ibao.alanger.alertcoldhfe.editPallet;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.integration.android.IntentIntegrator;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import ibao.alanger.alertcoldhfe.CustomScannerActivity;

import ibao.alanger.alertcoldhfe.R;
import ibao.alanger.alertcoldhfe.adapters.RViewAdapterSensorToPallet;
import ibao.alanger.alertcoldhfe.model.Formato;
import ibao.alanger.alertcoldhfe.model.Pallet;
import ibao.alanger.alertcoldhfe.model.Sensor;
import ibao.alanger.alertcoldhfe.model.Variedad;

public class editPalletActivity extends Activity {

    String TAG  = editPalletActivity.class.getSimpleName();
    Pallet PALLET;
    TextView tViewNOrden;
    EditText eTextNumPallets;
    AppCompatButton btnQR;
    List<Sensor> SENSORLISTALL;

    Spinner spnVariedad;
    Spinner spnFormato;


    RViewAdapterSensorToPallet adapter;

    RecyclerView rView;

    AlertDialog dialog;

    List<Formato> formatoList;

    List<Variedad> variedadList;

    TextInputEditText tietCantCajas;

    Context ctx;


    int REQUEST_QR_NPALLET=2134;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pallet);


        ctx = this;

        Bundle b = getIntent().getExtras();
        assert b != null;
        PALLET = (Pallet) b.getSerializable("pallet");
        assert PALLET != null;
        SENSORLISTALL = (List<Sensor>) b.getSerializable("sensorList");

        formatoList = (List<Formato>) b.getSerializable("formatoList") ;

        Log.d(TAG,"tam ->"+formatoList.size());
        variedadList = (List<Variedad>) b.getSerializable("variedadList") ;


        Log.d(TAG,PALLET.toString());
        Log.d(TAG,"listando Sensores ***************");
        for(int i = 0; i< SENSORLISTALL.size(); i++){
            Log.d(TAG,""+(i+1)+": "+ SENSORLISTALL.get(i).getName());
        }
        Log.d(TAG,"listando Fin del listado de sensores ***************");

        define();
        defineAtribs();

        events();

        loadformato();
        loadVariedades();


        try {
            for(int i=0;i<formatoList.size();i++){
                if(PALLET.getFormato().getId().equals(formatoList.get(i).getId())){
                    spnFormato.setSelection(i);
                    break;
                }
            }
        }catch (Exception e){
            Log.d(TAG,"formato"+PALLET.getFormato());

        }


        try {
            for(int i=0;i<variedadList.size();i++){
                if(PALLET.getVariedad().getId()==variedadList.get(i).getId()){
                    spnVariedad.setSelection(i);
                }
            }
        }catch (Exception e){
            Log.d(TAG,"formato"+PALLET.getFormato());

        }


        if(PALLET.getCantCajas()>0){
            tietCantCajas.setText(""+PALLET.getCantCajas());
        }

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
    }

    @SuppressLint("SetTextI18n")
    private void defineAtribs() {
        tViewNOrden.setText(""+PALLET.getPosition());
        eTextNumPallets.setText(PALLET.getCod());
    }

    private void define() {
        tietCantCajas = findViewById(R.id.tietCantCajas);
        spnVariedad = findViewById(R.id.spnVariedad);
        spnFormato = findViewById(R.id.spnFormato);
        rView = findViewById(R.id.recyclerView_Sensors);
        rView.setLayoutManager(new LinearLayoutManager(this));
        tViewNOrden = findViewById(R.id.tViewNOrden);
        eTextNumPallets = findViewById(R.id.eTextSearchPallet);
        btnQR = findViewById(R.id.btnQR);
        adapter = new RViewAdapterSensorToPallet(PALLET.getSensorList(),SENSORLISTALL);
        rView.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        if(PALLET.getSensorList().size()>0){
            if(eTextNumPallets.getText().toString().equals("")){
                Toast.makeText(this, "Ingrese un # Pallet válido",Toast.LENGTH_SHORT).show();
            }else {

                PALLET.setCod(eTextNumPallets.getText().toString());
                Log.d(TAG,"posFormato "+spnFormato.getSelectedItemPosition());
                PALLET.setFormato(formatoList.get(spnFormato.getSelectedItemPosition()));

                Log.d(TAG,"posVariedad "+spnVariedad.getSelectedItemPosition());
                PALLET.setVariedad(variedadList.get(spnVariedad.getSelectedItemPosition()));
                try{
                    PALLET.setCantCajas(Integer.valueOf(tietCantCajas.getText().toString()));
                }catch (Exception e){
                    Log.d(TAG, e.toString());
                }

                Intent i = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("pallet",PALLET);
                i.putExtras(bundle);
                setResult(Activity.RESULT_OK,i);
                super.onBackPressed();
            }

        }else {
            Toast.makeText(this, "No se seleccionó sensor",Toast.LENGTH_SHORT).show();
            PALLET.setCod("");
            Intent i = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("pallet",PALLET);
            i.putExtras(bundle);
            setResult(Activity.RESULT_OK,i);
            super.onBackPressed();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_QR_NPALLET) {

            try {
                Bundle recibidos = (data.getExtras());
                if (recibidos != null) {
                    String qr = recibidos.getString("qr");
                    eTextNumPallets.setText(qr);
                }
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
        }
    }

    public void showDialog(View view){
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
        List<Sensor> sensorListTemp = SENSORLISTALL;
        sensorListTemp.removeAll(PALLET.getSensorList());
        List<String> sensorListTemp_string = new ArrayList<>();
        for(int i =0;i<sensorListTemp.size();i++){
            sensorListTemp_string.add(sensorListTemp.get(i).getName());
        }

        CharSequence[] sensorListTemp_cs = sensorListTemp_string.toArray(new CharSequence[sensorListTemp_string.size()]);

        myBuilder.setTitle("Elija un Sensor").setItems(sensorListTemp_cs, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PALLET.getSensorList().add(sensorListTemp.get(i));
                SENSORLISTALL.remove(sensorListTemp.get(i));
                Collator collator = Collator.getInstance(Locale.getDefault());

                if (SENSORLISTALL.size() > 0) {
                    Collections.sort(SENSORLISTALL, new Comparator<Sensor>() {
                        @Override
                        public int compare(Sensor sensor, Sensor t1) {
                            return collator.compare(sensor.getName(), t1.getName());
                        }
                    });
                }
                adapter.notifyDataSetChanged();
            }
        });
        dialog= myBuilder.create();
        dialog.show();
    }


    private void loadVariedades() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ctx,R.layout.spinner_item,getArrayVariedad(variedadList));
        spnVariedad.setAdapter(adapter);
    }

    private List<String> getArrayVariedad(List<Variedad> temp) {
        List<String> list = new ArrayList<>();
        for(Variedad a: temp){
            list.add(a.getName());
        }
        return list;
    }


    private void loadformato() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ctx,R.layout.spinner_item,getArrayFormato(formatoList));
        spnFormato.setAdapter(adapter);
    }

    private List<String> getArrayFormato(List<Formato> temp) {
        List<String> list = new ArrayList<>();
        for(Formato a: temp){
            list.add(a.getName());
        }
        return list;
    }
}
