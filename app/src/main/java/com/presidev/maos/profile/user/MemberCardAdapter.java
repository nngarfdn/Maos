package com.presidev.maos.profile.user;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.presidev.maos.R;
import com.presidev.maos.mitrabookcatalog.view.MitraBookCatalogActivity;
import com.presidev.maos.profile.mitra.Mitra;
import com.presidev.maos.profile.mitra.MitraViewModel;
import com.presidev.maos.subscribe.model.MemberCard;

import java.util.ArrayList;

import static com.presidev.maos.utils.AppUtils.loadBlurImageFromUrl;
import static com.presidev.maos.utils.AppUtils.loadProfilePicFromUrl;
import static com.presidev.maos.utils.AppUtils.showMemberCardId;
import static com.presidev.maos.utils.Constants.EXTRA_MITRA;
import static com.presidev.maos.utils.DateUtils.getFullDate;

public class MemberCardAdapter extends RecyclerView.Adapter<MemberCardAdapter.ViewHolder> {
    private final ArrayList<MemberCard> memberCardList = new ArrayList<>();
    private final MitraViewModel mitraViewModel;

    public MemberCardAdapter(ViewModelStoreOwner viewModelStoreOwner){
        mitraViewModel = new ViewModelProvider(viewModelStoreOwner).get(MitraViewModel.class);
    }

    public void setData(ArrayList<MemberCard> memberCardList){
        this.memberCardList.clear();
        this.memberCardList.addAll(memberCardList);
        notifyDataSetChanged();
    }

    public ArrayList<MemberCard> getData(){
        return memberCardList;
    }

    @NonNull
    @Override
    public MemberCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberCardAdapter.ViewHolder holder, int position) {
        MemberCard memberCard = memberCardList.get(position);
        holder.bind(memberCard);
    }

    @Override
    public int getItemCount() {
        return memberCardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Mitra mitra;

        private final ImageView imgBanner, imgMitra;
        private final TextView tvId, tvMitra, tvExpDate;

        public ViewHolder(@NonNull View view) {
            super(view);
            imgBanner = view.findViewById(R.id.img_banner_mc);
            imgMitra = view.findViewById(R.id.img_mitra_mc);
            tvId = view.findViewById(R.id.tv_id_mc);
            tvMitra = view.findViewById(R.id.tv_mitra_mc);
            tvExpDate = view.findViewById(R.id.tv_exp_date_mc);
        }

        public void bind(MemberCard memberCard) {
            mitraViewModel.getReference().document(memberCard.getMitraId()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    mitra = task.getResult().toObject(Mitra.class);
                    Log.d(getClass().getSimpleName(), mitra.getEmail());
                    loadBlurImageFromUrl(itemView.getContext(), imgBanner, mitra.getBanner());
                    loadProfilePicFromUrl(imgMitra, mitra.getLogo());
                    tvMitra.setText(mitra.getName());
                }
            });

            tvId.setText(showMemberCardId(memberCard.getId()));
            tvExpDate.setText(getFullDate(memberCard.getExpDate(), true));

            itemView.setOnClickListener(view -> {
                if (mitra == null) return;
                Intent intent = new Intent(view.getContext(), MitraBookCatalogActivity.class);
                intent.putExtra(EXTRA_MITRA, mitra);
                view.getContext().startActivity(intent);
            });
        }
    }
}
