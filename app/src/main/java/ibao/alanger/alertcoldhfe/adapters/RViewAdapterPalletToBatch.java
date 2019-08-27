package ibao.alanger.alertcoldhfe.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ibao.alanger.alertcoldhfe.R;
import ibao.alanger.alertcoldhfe.model.Pallet;

public class RViewAdapterPalletToBatch
        extends RecyclerView.Adapter<RViewAdapterPalletToBatch.ViewHolder>
        implements View.OnClickListener{

    private View.OnClickListener onClickListener;

    private boolean modeVerify;
    List<Pallet> list;

    public RViewAdapterPalletToBatch(List<Pallet> list) {
        this.list = list;
        this.modeVerify=false;
    }

    public void setModeVerify(boolean flag){
        this.modeVerify=flag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_batch_pallet,null,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tViewNOrden.setText(String.valueOf(position+1));
        Pallet pallet = list.get(position);
        if((!pallet.getCod().isEmpty())){
            holder.tViewNumPallet.setText(pallet.getCod());
            holder.tView_NumPallet.setError(null);
        }else {
            if(modeVerify){
                holder.tView_NumPallet.setError("Ingrese un código válido");
            }else {
                holder.tView_NumPallet.setError(null);
            }
        }
        if(pallet.getSensorList().isEmpty()){
            holder.tViewNumSensors.setVisibility(View.INVISIBLE);
        }else {
            holder.tViewNumSensors.setVisibility(View.VISIBLE);

            if(pallet.getSensorList().size()>1){
                holder.tViewNumSensors.setText(""+pallet.getSensorList().size()+" Sensores");
            }else {
                holder.tViewNumSensors.setText(""+pallet.getSensorList().size()+" Sensor");
            }

        }
    }

    public void setOnClicListener(View.OnClickListener listener){
        this.onClickListener=listener;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    @Override
    public void onClick(View v) {
        if(onClickListener!=null){
            onClickListener.onClick(v);
        }
    }

    public void verificar() {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tView_NumPallet;
        AppCompatTextView tViewNumPallet;
        AppCompatTextView tViewNumSensors;
        AppCompatTextView tViewNOrden;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tView_NumPallet = itemView.findViewById(R.id.tView_NumPallet);
            tViewNumPallet = itemView.findViewById(R.id.tViewNumPallet);
            tViewNumSensors = itemView.findViewById(R.id.tViewNumSensors);
            tViewNOrden = itemView.findViewById(R.id.tViewNOrden);
        }
    }
}
