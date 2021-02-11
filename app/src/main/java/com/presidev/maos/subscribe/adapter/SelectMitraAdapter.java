package com.presidev.maos.subscribe.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.presidev.maos.R;
import com.presidev.maos.profile.mitra.Mitra;
import com.presidev.maos.subscribe.view.SelectMitraFragment;

import java.util.ArrayList;

import static com.presidev.maos.utils.AppUtils.loadImageFromUrl;
import static com.presidev.maos.utils.AppUtils.setFullAddress;

public class SelectMitraAdapter extends RecyclerView.Adapter<SelectMitraAdapter.ViewHolder> {
    private final BottomSheetDialogFragment dialogFragment;
    private final SelectMitraFragment.SelectMitraListener listener;

    private final ArrayList<Mitra> mitraList = new ArrayList<>();

    public SelectMitraAdapter(BottomSheetDialogFragment dialogFragment, SelectMitraFragment.SelectMitraListener listener){
        this.dialogFragment = dialogFragment;
        this.listener = listener;
    }

    public void setData(ArrayList<Mitra> mitraList){
        this.mitraList.clear();
        this.mitraList.addAll(mitraList);
        notifyDataSetChanged();
    }

    public ArrayList<Mitra> getData(){
        return mitraList;
    }

    @NonNull
    @Override
    public SelectMitraAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mitra_dashboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectMitraAdapter.ViewHolder holder, int position) {
        Mitra mitra = mitraList.get(position);
        holder.bind(mitra);
    }

    @Override
    public int getItemCount() {
        return mitraList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgLogo;
        private final TextView tvName, tvAddress;

        public ViewHolder(@NonNull View view) {
            super(view);
            imgLogo = view.findViewById(R.id.img_item_book_dashboard);
            tvName = view.findViewById(R.id.txt_title_book);
            tvAddress = view.findViewById(R.id.txt_address_book);
        }

        public void bind(Mitra mitra) {
            loadImageFromUrl(imgLogo, mitra.getLogo());
            tvName.setText(mitra.getName());
            tvAddress.setText(setFullAddress(null, mitra.getProvince(), mitra.getRegency(), mitra.getDistrict()));

            itemView.setOnClickListener(view -> {
                listener.receiveData(mitra);
                dialogFragment.dismiss();
            });
        }
    }
}
