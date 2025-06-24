package com.example.d308vacationplanner.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308vacationplanner.R;
import com.example.d308vacationplanner.entities.Excursion;
import com.example.d308vacationplanner.entities.Vacation;
import com.example.d308vacationplanner.viewModel.DetailViewModel;
import com.example.d308vacationplanner.viewModel.VacationViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class VacationDetails extends AppCompatActivity {
    private DetailViewModel mDetailViewModel;
    private ExcursionAdapter excursionAdapter;

    private int vacationId;
    private Vacation currentVacation;


    // Variables to hold all vacation data
    private TextInputEditText editName;
    private TextInputEditText editHotel;
    private TextInputEditText editStartDate;
    private TextInputEditText editEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);

        mDetailViewModel = new ViewModelProvider(this).get(DetailViewModel.class);

        // Get all data from the intent once at the top
        vacationId = getIntent().getIntExtra("id", -1);
        String name = getIntent().getStringExtra("name");
        String hotel = getIntent().getStringExtra("hotel");
        String startDate = getIntent().getStringExtra("startDate");
        String endDate = getIntent().getStringExtra("endDate");

        // --- Setup Views ---
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(name);

        editName = findViewById(R.id.title_text);
        editHotel = findViewById(R.id.hotel_text);
        editStartDate = findViewById(R.id.edit_text_start_date);
        editEndDate = findViewById(R.id.edit_text_end_date);

        mDetailViewModel.getVacationById(vacationId).observe(this, vacation -> {
            if (vacation != null) {
                // When the LiveData delivers the vacation, update the UI
                currentVacation = vacation; // Keep a reference to the current vacation
                editName.setText(vacation.getVacationName());
                editHotel.setText(vacation.getHotel());
                editStartDate.setText(vacation.getStartDate());
                editEndDate.setText(vacation.getEndDate());
                getSupportActionBar().setTitle(vacation.getVacationName());
            }
        });


        editStartDate.setOnClickListener(v -> showDatePicker(editStartDate, "Select Start Date"));
        editEndDate.setOnClickListener(v -> showDatePicker(editEndDate, "Select End Date"));

        // --- Setup RecyclerView ---
        RecyclerView recyclerView = findViewById(R.id.excursionRecyclerView);
        excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Observe the excursion list from the ViewModel
        mDetailViewModel.getAssociatedExcursions(vacationId).observe(this, excursions -> {
            excursionAdapter.setExcursions(excursions);
        });

        // --- Setup Floating Action Button ---
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(v -> {
            // This button on this screen is for adding a NEW excursion
            Intent intent = new Intent(VacationDetails.this, AddEditExcursionActivity.class);
            intent.putExtra("vacationId", vacationId);
            startActivity(intent);
        });
    }
    private void showDatePicker(final TextInputEditText dateEditText, String title) {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText(title);
        final MaterialDatePicker<Long> datePicker = builder.build();
        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");

        datePicker.addOnPositiveButtonClickListener(selection -> {
            TimeZone timeZone = TimeZone.getTimeZone("UTC");
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
            sdf.setTimeZone(timeZone);
            String formattedDate = sdf.format(new Date(selection));
            dateEditText.setText(formattedDate);
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_save_vacation) {
            saveChanges();
            return true;
        }

        if (itemId == R.id.action_set_alerts) {


        }

        if (itemId == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveChanges() {
        // ✅ FIX: Read the text from all the updated fields
        String updatedName = editName.getText().toString().trim();
        String updatedHotel = editHotel.getText().toString().trim();
        String updatedStartDate = editStartDate.getText().toString().trim();
        String updatedEndDate = editEndDate.getText().toString().trim();


        if (updatedName.isEmpty() || updatedHotel.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
        Date startDate = null;
        Date endDate = null;

        try {
            startDate = sdf.parse(updatedStartDate);
            endDate = sdf.parse(updatedEndDate);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Invalid date format. Please use MM/DD/YY.", Toast.LENGTH_SHORT).show();
            return; // Stop the save if dates are invalid
        }

        if (endDate.before(startDate)) {
            Toast.makeText(this, "End date cannot be before the start date.", Toast.LENGTH_SHORT).show();
            return; // Stop the save if the end date is before the start date
        }

        // Create the updated Vacation object with all the new data
        Vacation updatedVacation = new Vacation(vacationId, updatedName, updatedHotel, updatedStartDate, updatedEndDate);
        mDetailViewModel.update(updatedVacation);

        Toast.makeText(this, "Vacation details saved.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
