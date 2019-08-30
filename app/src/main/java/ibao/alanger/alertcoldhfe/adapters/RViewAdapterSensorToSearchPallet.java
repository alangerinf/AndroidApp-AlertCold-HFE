package ibao.alanger.alertcoldhfe.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ibao.alanger.alertcoldhfe.R;
import ibao.alanger.alertcoldhfe.model.SensorIntoPallet;

public class RViewAdapterSensorToSearchPallet
        extends RecyclerView.Adapter<RViewAdapterSensorToSearchPallet.ViewHolder>{

    List<SensorIntoPallet> sensorIntoPalletList;

    public RViewAdapterSensorToSearchPallet(List<SensorIntoPallet> sensorIntoPalletList) {
        this.sensorIntoPalletList = sensorIntoPalletList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_pallet_search,null,false);

        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SensorIntoPallet sensor = sensorIntoPalletList.get(position);

        holder.spa_tViewNameBatch.setText(sensor.getPERIODO());
        holder.spa_tViewTempMax.setText(sensor.getTEMP_MAX()+" C°");
        holder.spa_tViewTempMin.setText(sensor.getTEMP_MIN()+" C°");
        holder.spa_tViewArea.setText(sensor.getZONA());
        holder.spa_tViewZona.setText(sensor.getSUBZONA());
        holder.spa_tViewSensor.setText(sensor.getSENSOR());
        holder.spa_tViewTempStart.setText(sensor.getTEMP_INICIO()+" C°");
        holder.spa_tViewTempEnd.setText(sensor.getTEMP_FIN()+" C°");
        holder.spa_tViewTimeStart.setText(sensor.getFECHA_INICIO());
        holder.spa_tViewTimeEnd.setText(sensor.getFECHA_FIN());

        holder.spa_tViewFormato.setText(sensor.getFORMATO());
        holder.spa_tViewVariedad.setText(sensor.getVARIEDAD());
        holder.spa_tViewCantCajas.setText(sensor.getCANT_CAJAS());


    }



    @Override
    public int getItemCount() {
        return sensorIntoPalletList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout spa_index;
        AppCompatTextView spa_tViewNameBatch;
        AppCompatTextView spa_tViewTempMax;
        AppCompatTextView spa_tViewTempMin;
        AppCompatTextView spa_tViewArea;
        AppCompatTextView spa_tViewZona;
        AppCompatTextView spa_tViewSensor;

        AppCompatTextView spa_tViewTempStart;
        AppCompatTextView spa_tViewTempEnd;
        AppCompatTextView spa_tViewTimeStart;
        AppCompatTextView spa_tViewTimeEnd;


        AppCompatTextView spa_tViewFormato;
        AppCompatTextView spa_tViewVariedad;
        AppCompatTextView spa_tViewCantCajas;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            spa_index = itemView.findViewById(R.id.spa_index);
            spa_tViewNameBatch = itemView.findViewById(R.id.spa_tViewNameBatch);
            spa_tViewTempMax = itemView.findViewById(R.id.spa_tViewTempMax);
            spa_tViewTempMin = itemView.findViewById(R.id.spa_tViewTempMin);
            spa_tViewArea = itemView.findViewById(R.id.spa_tViewArea);
            spa_tViewZona = itemView.findViewById(R.id.spa_tViewZona);
            spa_tViewSensor = itemView.findViewById(R.id.spa_tViewSensor);

            spa_tViewTempStart = itemView.findViewById(R.id.spa_tViewTempStart);
            spa_tViewTempEnd= itemView.findViewById(R.id.spa_tViewTempEnd);
            spa_tViewTimeStart= itemView.findViewById(R.id.spa_tViewTimeStart);
            spa_tViewTimeEnd= itemView.findViewById(R.id.spa_tViewTimeEnd);

            spa_tViewFormato = itemView.findViewById(R.id.spa_tViewFormato);
            spa_tViewVariedad = itemView.findViewById(R.id.spa_tViewVariedad);
            spa_tViewCantCajas = itemView.findViewById(R.id.spa_tViewCantCajas);


        }
    }
}
