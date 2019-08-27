package ibao.alanger.alertcoldhfe.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import ibao.alanger.alertcoldhfe.R;
import ibao.alanger.alertcoldhfe.model.Sensor;

public class RViewAdapterSensorToPallet
        extends RecyclerView.Adapter<RViewAdapterSensorToPallet.ViewHolder>{

    List<Sensor> list;
    List<Sensor> sensorListAll;

    public RViewAdapterSensorToPallet(List<Sensor> list,List<Sensor> sensorListAll) {
        this.list = list;
        this.sensorListAll = sensorListAll;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_pallet_sensor,null,false);

        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sensor sensor = list.get(position);
        holder.ips_tViewName.setText(sensor.getName());
        holder.ips_btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sensorListAll.add(list.remove(position));
                notifyDataSetChanged();
                Collator collator = Collator.getInstance(Locale.getDefault());

                if (sensorListAll.size() > 0) {
                    Collections.sort(sensorListAll, new Comparator<Sensor>() {
                        @Override
                        public int compare(Sensor sensor, Sensor t1) {
                            return collator.compare(sensor.getName(), t1.getName());
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView ips_tViewName;
        AppCompatButton ips_btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ips_tViewName = itemView.findViewById(R.id.ips_tViewName);
            ips_btnDelete = itemView.findViewById(R.id.ips_btnDelete);
        }
    }
}
