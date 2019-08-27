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
import ibao.alanger.alertcoldhfe.model.Batch;

public class RViewAdapterMainBatch
        extends RecyclerView.Adapter<RViewAdapterMainBatch.ViewHolder>{

    List<Batch> batchList;

    public RViewAdapterMainBatch(List<Batch> batchList) {
        this.batchList = batchList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_main_batch,null,false);

        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Batch batch = batchList.get(position);

        holder.bch_tViewNameBatch.setText(""+batch.getNameBatch());
        holder.bch_tViewFInicio.setText(""+batch.getDateStart()+" "+batch.getTimeStart());
        holder.bch_tViewFFin.setText(""+batch.getDateEnd()+" "+batch.getTimeEnd());
        holder.bch_tViewArea.setText(""+batch.getArea().getName());
        holder.bch_tViewZona.setText(""+batch.getZone().getName());
        holder.bch_tViewProducto.setText(""+batch.getProduct().getName());

    }



    @Override
    public int getItemCount() {
        return batchList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {



        ConstraintLayout bch_index;
        AppCompatTextView bch_tViewNameBatch;
        AppCompatTextView bch_tViewFInicio;
        AppCompatTextView bch_tViewFFin;
        AppCompatTextView bch_tViewArea;
        AppCompatTextView bch_tViewZona;
        AppCompatTextView bch_tViewProducto;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bch_index = itemView.findViewById(R.id.bch_index);
            bch_tViewNameBatch = itemView.findViewById(R.id.bch_tViewNameBatch);
            bch_tViewFInicio = itemView.findViewById(R.id.bch_tViewFInicio);
            bch_tViewFFin = itemView.findViewById(R.id.bch_tViewFFin);
            bch_tViewArea = itemView.findViewById(R.id.bch_tViewArea);
            bch_tViewZona = itemView.findViewById(R.id.bch_tViewZona);
            bch_tViewProducto = itemView.findViewById(R.id.bch_tViewProducto);
        }
    }
}
