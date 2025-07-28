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
import com.example.d308vacationplanner.entities.Vacation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.example.d308vacationplanner.entities.Excursion;
import com.example.d308vacationplanner.database.Repository;
import com.example.d308vacationplanner.entities.VacationWithTotals;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {

    private final Context context;

    private List<VacationWithTotals> mVacationsWithTotals = new ArrayList<>();


    private final LayoutInflater mInflater;

    public VacationAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setVacationsWithTotals(List<VacationWithTotals> vacations) {
        mVacationsWithTotals = vacations;
        notifyDataSetChanged();
    }

    public class VacationViewHolder extends RecyclerView.ViewHolder {

        private final TextView vacationItemView;
        private final TextView vacationCostView;


        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            vacationItemView = itemView.findViewById(R.id.textView2);
            vacationCostView = itemView.findViewById(R.id.textView_vacation_cost);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Vacation current = mVacationsWithTotals.get(position).vacation;
                    Intent intent = new Intent(context, EditVacation.class);
                    intent.putExtra("id", current.getVacationID());
                    intent.putExtra("name", current.getVacationName());
                    intent.putExtra("vacationStartDate", current.getStartDate());
                    intent.putExtra("vacationEndDate", current.getEndDate());
                    intent.putExtra("hotel", current.getHotel());
                    intent.putExtra("vacationCost", String.valueOf(current.getCost()));
                    context.startActivity(intent);



                }
            });
        }
    }

    @NonNull
    @Override
    public VacationAdapter.VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.vacation_list_item,parent, false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationAdapter.VacationViewHolder holder, int position) {
        if (mVacationsWithTotals != null && position < mVacationsWithTotals .size()) {
            VacationWithTotals current = mVacationsWithTotals.get(position);
            String name = current.vacation.getVacationName();
            double total = current.getTotalCost();

            holder.vacationItemView.setText(name);
            holder.vacationCostView.setText(String.format(Locale.US, "$%.2f", total));
        } else {
            holder.vacationItemView.setText("No Vacation Name");
        }
    }

    @Override
    public int getItemCount() {
        if(mVacationsWithTotals!=null){
            return mVacationsWithTotals.size();
        }
        else return 0;
    }
    public void setVacations(List<VacationWithTotals> vacations) {
        mVacationsWithTotals = vacations;
        notifyDataSetChanged();
    }
}
