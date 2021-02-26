package com.presidev.maos.catatanku.target.view;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.presidev.maos.R;
import com.presidev.maos.catatanku.target.model.Target;
import com.presidev.maos.databinding.ItemTargetBinding;

import java.util.ArrayList;

import static com.presidev.maos.utils.Constants.EXTRA_TARGET;

class TargetAdapter extends RecyclerView.Adapter<TargetAdapter.ViewHolder> {
    private final ArrayList<Target> targetList = new ArrayList<>();

    public TargetAdapter(){}

    public void setData(ArrayList<Target> targetList){
        this.targetList.clear();
        this.targetList.addAll(targetList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTargetBinding binding = ItemTargetBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Target target = targetList.get(position);
        holder.bind(target);
    }

    @Override
    public int getItemCount() {
        return targetList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTargetBinding binding;

        public ViewHolder(@NonNull ItemTargetBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Target target) {
            binding.tvBookTitle.setText(target.getBookTitle());
            binding.tvDailyPages.setText(target.getDailyPages() + " halaman per hari");

            double progress = target.getProgress();
            if (progress == 100){
                binding.tvProgress.setText("selesai");
                binding.tvProgress.setTextColor(binding.getRoot().getResources().getColor(R.color.green));
            } else {
                binding.tvProgress.setText("progres: " + (int) progress + "%" + " (" + target.getPagesRead() + "/" + target.getTotalPages() + ")");
                binding.tvProgress.setTextColor(binding.getRoot().getResources().getColor(R.color.red));
            }

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), AddUpdateTargetActivity.class);
                intent.putExtra(EXTRA_TARGET, target);
                view.getContext().startActivity(intent);
            });
        }
    }
}
