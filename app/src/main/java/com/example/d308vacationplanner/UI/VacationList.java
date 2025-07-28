package com.example.d308vacationplanner.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.d308vacationplanner.R;
import com.example.d308vacationplanner.viewModel.VacationViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Objects;


public class VacationList extends AppCompatActivity {

    private VacationAdapter vacationAdapter;
    private LinearLayout emptyStateLayout;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sojourn Tracker");

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        emptyStateLayout = findViewById(R.id.empty_state_layout);
        Button addFirstVacation = findViewById(R.id.add_vacation_button_empty);
        fab = findViewById(R.id.add_button);
        vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        VacationViewModel mVacationViewModel = new ViewModelProvider(this).get(VacationViewModel.class);

        mVacationViewModel.getVacationsWithTotals().observe(this, vacationWithTotals -> {
            vacationAdapter.setVacations(vacationWithTotals);

            if (vacationWithTotals == null || vacationWithTotals.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                emptyStateLayout.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                fab.setVisibility(View.VISIBLE);
                emptyStateLayout.setVisibility(View.GONE);
            }
        });

        View.OnClickListener addVacationListener = view -> {
            Intent intent = new Intent(VacationList.this, AddVacationActivity.class);
            startActivity(intent);
        };

        addFirstVacation.setOnClickListener(addVacationListener);
        fab.setOnClickListener(addVacationListener);
    }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            if (item.getItemId() == R.id.action_save_vacation) {
                Toast.makeText(this, "Sample data added!", Toast.LENGTH_SHORT).show();
                return true;
            }

            if (item.getItemId() == android.R.id.home) {
                this.finish();
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    }