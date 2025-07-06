package com.example.d308vacationplanner.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308vacationplanner.R;
import com.example.d308vacationplanner.entities.Vacation;
import com.example.d308vacationplanner.viewModel.DetailViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class EditVacation extends AppCompatActivity {
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
        setContentView(R.layout.activity_vacation_edit);

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


                // --- Setup RecyclerView ---
                RecyclerView recyclerView = findViewById(R.id.excursionRecyclerView);
                excursionAdapter = new ExcursionAdapter(this, currentVacation);
                recyclerView.setAdapter(excursionAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));

                // Observe the excursion list from the ViewModel
                mDetailViewModel.getAssociatedExcursions(vacationId).observe(this, excursions -> {
                    excursionAdapter.setExcursions(excursions);
        });
                }


        editStartDate.setOnClickListener(v -> showDatePicker(editStartDate, "Select Start Date"));
        editEndDate.setOnClickListener(v -> showDatePicker(editEndDate, "Select End Date"));

        });

        // --- Setup Floating Action Button ---
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(v -> {
            // This button on this screen is for adding a NEW excursion
            Intent intent = new Intent(EditVacation.this, AddExcursionActivity.class);
            intent.putExtra("vacationId", vacationId);
            intent.putExtra("vacationStartDate", currentVacation.getStartDate());
            intent.putExtra("vacationEndDate", currentVacation.getEndDate());
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
            scheduleToastAlerts();
            return true;
        }
        if (itemId == R.id.action_delete_vacation) {
            deleteVacation();
            return true;

        }

        if (itemId == R.id.action_share_vacation) {
            shareVacationDetails();
            return true;
        }

        if (itemId == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveChanges() {

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
            Toast.makeText(this, "Invalid date format. Please use MM-DD-YY.", Toast.LENGTH_SHORT).show();
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

    private void scheduleToastAlerts() {
        String vacationName = editName.getText().toString();
        String startDateStr = editStartDate.getText().toString();
        String endDateStr = editEndDate.getText().toString();
        String format = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);

        if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
            Toast.makeText(this, "Please select both a start and end date.", Toast.LENGTH_LONG).show();
            return;
        }

        Date startDate;
        Date endDate;
        Date today;

        try {
            startDate = sdf.parse(startDateStr);
            endDate = sdf.parse(endDateStr);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            today = cal.getTime();
        } catch (ParseException e) {
            Toast.makeText(this, "Invalid date format.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (endDate.before(startDate)) {
            Toast.makeText(this, "End date cannot be before the start date.", Toast.LENGTH_LONG).show();
            return; // Stop the method if validation fails
        }

        Toast.makeText(this, "Alerts have been set.", Toast.LENGTH_SHORT).show();

        if (isSameDay(startDate, today)) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Toast.makeText(EditVacation.this, "Your vacation, '" + vacationName + "', begins today!", Toast.LENGTH_LONG).show();
            }, 2000);
        }

        if (isSameDay(endDate, today)) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Toast.makeText(EditVacation.this, "Your vacation, '" + vacationName + "', ends today!", Toast.LENGTH_LONG).show();
            }, 4000);
        }
    }

    private boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    private void shareVacationDetails() {
        if (currentVacation == null) {
            Toast.makeText(this, "Cannot share, vacation details not loaded yet.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Create the message content by combining all the vacation details
        String shareBody = "Check out my vacation plans!\n\n" +
                "Title: " + currentVacation.getVacationName() + "\n" +
                "Hotel: " + currentVacation.getHotel() + "\n" +
                "Start Date: " + currentVacation.getStartDate() + "\n" +
                "End Date: " + currentVacation.getEndDate();

        // 2. Create a generic "share" Intent
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        // 3. Add the content to the intent
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Vacation Details: " + currentVacation.getVacationName());
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

        // 4. Launch the Android share dialog
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void deleteVacation() {
        mDetailViewModel.getExcursionCountForVacation(vacationId).observe(this, excursions -> {
            if (excursions == null || excursions.isEmpty()) {
                mDetailViewModel.delete(currentVacation);
                Toast.makeText(this, "Vacation deleted.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Cannot delete vacation with attached excursions.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
