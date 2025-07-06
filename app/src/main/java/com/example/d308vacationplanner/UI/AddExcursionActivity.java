package com.example.d308vacationplanner.UI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.d308vacationplanner.R;
import com.example.d308vacationplanner.entities.Excursion;
import com.example.d308vacationplanner.viewModel.DetailViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AddExcursionActivity extends AppCompatActivity {

    private DetailViewModel mDetailViewModel;

    private EditText mEditExcursionTitle;
    private EditText mEditExcursionDate;
    private int vacationId;
    private String vacationStartDateStr;
    private String vacationEndDateStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_excursion);

        // Initialize the ViewModel
        mDetailViewModel = new ViewModelProvider(this).get(DetailViewModel.class);

        // Get the vacationId passed from the VacationDetails screen
        vacationId = getIntent().getIntExtra("vacationId", -1);
        vacationStartDateStr = getIntent().getStringExtra("vacationStartDate");
        vacationEndDateStr = getIntent().getStringExtra("vacationEndDate");

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Excursion");

        // Find views
        mEditExcursionTitle = findViewById(R.id.edit_text_excursion_title);
        mEditExcursionDate = findViewById(R.id.edit_text_excursion_date);

        // Set listener to show date picker
        mEditExcursionDate.setOnClickListener(v -> showDatePicker(mEditExcursionDate, "Select Excursion Date"));
    }

    private void showDatePicker(final EditText dateEditText, String title) {
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
        // Use the same save menu as the AddEditVacationActivity
        getMenuInflater().inflate(R.menu.menu_add_vacation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save_vacation) {
            saveExcursion();
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveExcursion() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
        String title = mEditExcursionTitle.getText().toString().trim();
        String dateStr = mEditExcursionDate.getText().toString().trim();

        if (title.isEmpty() || dateStr.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {

            Date excursionDate = sdf.parse(dateStr);
            Date vacationStartDate = sdf.parse(vacationStartDateStr);
            Date vacationEndDate = sdf.parse(vacationEndDateStr);

            if (excursionDate.before(vacationStartDate) || excursionDate.after(vacationEndDate)) {
                Toast.makeText(this, "Excursion Date must be within Vacation start and end data", Toast.LENGTH_LONG).show();
                return;
            }

            if (vacationId == -1) {
                Toast.makeText(this, "Error: Could not associate excursion with a vacation.", Toast.LENGTH_SHORT).show();
                return;

            }
            Excursion newExcursion = new Excursion(0, title, dateStr, vacationId);
            mDetailViewModel.insert(newExcursion);
            Toast.makeText(this, "Excursion saved!", Toast.LENGTH_SHORT).show();

            finish(); // Return to previous screen
        } catch (ParseException e) {
            Toast.makeText(this, "Invalid Date Format. Use MM/DD/YY", Toast.LENGTH_LONG).show();
        }
    }
}