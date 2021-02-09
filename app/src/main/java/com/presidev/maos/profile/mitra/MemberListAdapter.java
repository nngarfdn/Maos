package com.presidev.maos.profile.mitra;

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
import com.presidev.maos.profile.user.User;
import com.presidev.maos.profile.user.UserViewModel;
import com.presidev.maos.subscribe.model.MemberCard;

import java.util.ArrayList;

import static com.presidev.maos.utils.AppUtils.loadProfilePicFromUrl;
import static com.presidev.maos.utils.AppUtils.showMemberCardId;
import static com.presidev.maos.utils.DateUtils.getFullDate;

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.ViewHolder> {
    private final ArrayList<MemberCard> memberCardList = new ArrayList<>();
    private final UserViewModel userViewModel;

    public MemberListAdapter(ViewModelStoreOwner viewModelStoreOwner){
        userViewModel = new ViewModelProvider(viewModelStoreOwner).get(UserViewModel.class);
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
    public MemberListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberListAdapter.ViewHolder holder, int position) {
        MemberCard memberCard = memberCardList.get(position);
        holder.bind(memberCard);
    }

    @Override
    public int getItemCount() {
        return memberCardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private User user;

        private final ImageView imgPhoto;
        private final TextView tvName, tvEmail, tvAddress, tvMemberCardId, tvStartDate, tvExpDate;

        public ViewHolder(@NonNull View view) {
            super(view);
            imgPhoto = view.findViewById(R.id.img_photo_member);
            tvName = view.findViewById(R.id.tv_name_member);
            tvEmail = view.findViewById(R.id.tv_email_member);
            tvAddress = view.findViewById(R.id.tv_address_member);
            tvMemberCardId = view.findViewById(R.id.tv_member_card_id_member);
            tvStartDate = view.findViewById(R.id.tv_start_date_member);
            tvExpDate = view.findViewById(R.id.tv_exp_date_member);
        }

        public void bind(MemberCard memberCard) {
            userViewModel.getReference().document(memberCard.getUserId()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    user = task.getResult().toObject(User.class);
                    Log.d(getClass().getSimpleName(), user.getEmail());
                    loadProfilePicFromUrl(imgPhoto, user.getPhoto());
                    tvName.setText(user.getName());
                    tvEmail.setText(user.getEmail());
                    tvAddress.setText(user.getAddress());
                }
            });

            tvMemberCardId.setText(showMemberCardId(memberCard.getId()));
            tvStartDate.setText(getFullDate(memberCard.getStartDate(), true));
            tvExpDate.setText(getFullDate(memberCard.getExpDate(), true));
        }
    }
}
