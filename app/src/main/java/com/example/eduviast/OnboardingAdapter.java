package com.example.eduviast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {

    private final List<OnboardItem> items;
    private final Runnable onFinish;

    public OnboardingAdapter(List<OnboardItem> items, Runnable onFinish) {
        this.items = items;
        this.onFinish = onFinish;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnboardingViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_onboarding_screen, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        OnboardItem item = items.get(position);
        holder.image.setImageResource(item.image);
        holder.title.setText(item.title);
        holder.description.setText(item.description);
        holder.subText.setText(item.subtext);
        holder.btnNext.setText(item.buttonText);

        if (position == items.size() - 1) {
            holder.btnNext.setOnClickListener(v -> onFinish.run());
        } else {
            holder.btnNext.setOnClickListener(v -> holder.itemView.post(() -> {
                RecyclerView recyclerView = (RecyclerView) holder.itemView.getParent();
                recyclerView.smoothScrollToPosition(position + 1);
            }));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class OnboardingViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, description,subText;
        Button btnNext;

        OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            subText = itemView.findViewById(R.id.subText);
            btnNext = itemView.findViewById(R.id.btnNext);
        }
    }
}