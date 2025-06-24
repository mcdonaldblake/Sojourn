package com.example.d308vacationplanner.UI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.d308vacationplanner.R;
import com.example.d308vacationplanner.entities.Vacation;
import com.example.d308vacationplanner.viewModel.VacationViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AddEditVacationActivity extends AppCompatActivity {

    private VacationViewModel mVacationViewModel;

    private EditText mEditTitle;
    private EditText mEditHotel;
    private EditText mEditStartDate;
    private EditText mEditEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_vacation);

        mVacationViewModel = new ViewModelProvider(this).get(VacationViewModel.class);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add New Vacation");

        mEditTitle = findViewById(R.id.edit_text_title);
        mEditHotel = findViewById(R.id.edit_text_hotel);
        mEditStartDate = findViewById(R.id.edit_text_start_date);
        mEditEndDate = findViewById(R.id.edit_text_end_date);

        mEditStartDate.setOnClickListener(v -> showDatePicker(mEditStartDate, "Select Start Date"));
        mEditEndDate.setOnClickListener(view -> showDatePicker(mEditEndDate, "Select End Date"));
    }

    private void showDatePicker(final EditText dateEditText, String title) {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText(title);
        final MaterialDatePicker<Long> datePicker = builder.build();

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");

        datePicker.addOnPositiveButtonClickListener(selection -> {
            TimeZone timeZone = TimeZone.getTimeZone("UTC");
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy", Locale.US);
            sdf.setTimeZone(timeZone);

            String formattedDate = sdf.format(new Date(selection));
            dateEditText.setText(formattedDate);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_vacation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save_vacation) {
            // When the user clicks "Save"
            saveVacation();
            return true;
        }

        if (id == android.R.id.home) {
            // When the user clicks the "Up" arrow
            finish(); // Close the activity and go back
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveVacation() {
        // Read the text from each EditText field
        String title = mEditTitle.getText().toString().trim();
        String hotel = mEditHotel.getText().toString().trim();
        String startDate = mEditStartDate.getText().toString().trim();
        String endDate = mEditEndDate.getText().toString().trim();

        // Basic validation to make sure fields are not empty
        if (title.isEmpty() || hotel.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new Vacation object with an ID of 0.
        // Room will auto-generate the real ID.
        Vacation newVacation = new Vacation(0, title, hotel, startDate, endDate); // Using 0.0 for price temporarily
        mVacationViewModel.insert(newVacation);

        Toast.makeText(this, "Vacation saved!", Toast.LENGTH_SHORT).show();
        finish(); // Close the activity and return to the list
    }
}

