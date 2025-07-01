package com.example.d308vacationplanner.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308vacationplanner.R;
import com.example.d308vacationplanner.entities.Excursion;

import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {

    private List<Excursion> mExcursion;
    private final Context context;
    private final LayoutInflater mInflater;

    class ExcursionViewHolder extends RecyclerView.ViewHolder {

        private final TextView excursionItemView;
        private final TextView excursionItemView2;


        private ExcursionViewHolder(View itemView) {
            super(itemView);
            excursionItemView = itemView.findViewById(R.id.textView_excursion_name);
            excursionItemView2 = itemView.findViewById(R.id.textView_excursion_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();


                    if (mExcursion != null && position != RecyclerView.NO_POSITION) {
                        final Excursion current = mExcursion.get(position);
                        Intent intent = new Intent(context, ExcursionDetails.class);
                        intent.putExtra("id", current.getExcursionID());
                        intent.putExtra("name", current.getExcursionName());
                        intent.putExtra("prodId", current.getVacationID());
                        intent.putExtra("date", current.getExcursionDate());

                        context.startActivity(intent);
                    }

                }
            });

        }
    }

    public ExcursionAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }
    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.excursion_list_item, parent, false);
        return new ExcursionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        if (mExcursion != null) {
            Excursion current = mExcursion.get(position);
            String name = current.getExcursionName();
            String date = current.getExcursionDate();


            holder.excursionItemView.setText(name);
            holder.excursionItemView2.setText(date);




        } else {
            holder.excursionItemView.setText("No Excursion Name");

        }
    }


    public void setExcursions(List<Excursion> excursions){
        mExcursion = excursions;
        notifyDataSetChanged();

    }

    public int getItemCount() {
        if (mExcursion != null){
        return mExcursion.size();
    } else {
            return 0;
        }
    }
}
