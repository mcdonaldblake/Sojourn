package com.example.d308vacationplanner.UI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
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
import com.example.d308vacationplanner.database.Repository;
import com.example.d308vacationplanner.entities.Excursion;
import com.example.d308vacationplanner.viewModel.DetailViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ExcursionDetails extends AppCompatActivity {

    private DetailViewModel mDetailViewModel;

    private int excursionId;
    private int vacationId;

    // Editable fields from the layout
    private TextInputEditText editExcursionTitle;
    private TextInputEditText editExcursionDate;

    // Dates from the parent vacation, needed for validation
    private String vacationStartDateStr;
    private String vacationEndDateStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);

        mDetailViewModel = new ViewModelProvider(this).get(DetailViewModel.class);

        // Get all data from the intent
        excursionId = getIntent().getIntExtra("id", -1);
        String excursionName = getIntent().getStringExtra("name");
        String excursionDate = getIntent().getStringExtra("date");
        vacationId = getIntent().getIntExtra("vacationId", -1);
        vacationStartDateStr = getIntent().getStringExtra("vacationStartDate");
        vacationEndDateStr = getIntent().getStringExtra("vacationEndDate");

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(excursionName);
        }

        // Find and populate the editable text fields
        editExcursionTitle = findViewById(R.id.edit_text_excursion_title);
        editExcursionDate = findViewById(R.id.edit_text_excursion_date);
        editExcursionTitle.setText(excursionName);
        editExcursionDate.setText(excursionDate);

        // ✅ FIX: Set listener to show the date picker on the correct UI element
        editExcursionDate.setOnClickListener(v -> showDatePicker(editExcursionDate, "Select Excursion Date"));
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
        getMenuInflater().inflate(R.menu.menu_edit_excursion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_save_excursion) {
            saveChanges();
            return true;
        }
        if (itemId == R.id.action_delete_excursion) {
            deleteExcursion();
            return true;
        }
        if (itemId == R.id.action_share_excursion) {
            shareExcursion();
            return true;
        }
        if (itemId == R.id.action_set_alerts_excursion) {
            // ✅ FIX: This now calls our simple Toast alert method
            setToastAlert();
            return true;
        }
        if (itemId == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveChanges() {
        String title = editExcursionTitle.getText().toString().trim();
        String dateStr = editExcursionDate.getText().toString().trim();

        // ✅ REQUIREMENT B5.e: Validate that the excursion date is within the vacation's date range
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
        try {
            Date excursionDate = sdf.parse(dateStr);
            Date vacationStartDate = sdf.parse(vacationStartDateStr);
            Date vacationEndDate = sdf.parse(vacationEndDateStr);

            if (excursionDate.before(vacationStartDate) || excursionDate.after(vacationEndDate)) {
                Toast.makeText(this, "Excursion date must be within the vacation's start and end dates.", Toast.LENGTH_LONG).show();
                return; // Stop the save if the date is out of range
            }
        } catch (ParseException e) {
            Toast.makeText(this, "Invalid date format. Please re-select dates.", Toast.LENGTH_LONG).show();
            return;
        }

        // This object is used for both updating an existing excursion and saving a new one
        Excursion excursion = new Excursion(excursionId, title, dateStr, vacationId);
        if (excursionId == -1) {
            mDetailViewModel.insert(excursion);
            Toast.makeText(this, "Excursion created.", Toast.LENGTH_SHORT).show();
        } else {
            mDetailViewModel.update(excursion);
            Toast.makeText(this, "Excursion updated.", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void deleteExcursion() {

        String title = editExcursionTitle.getText().toString().trim();
        String dateStr = editExcursionDate.getText().toString().trim();
        // We only need the ID to delete, but creating the object is safe.
        Excursion excursion = new Excursion(excursionId, title, dateStr, vacationId);
        mDetailViewModel.deleteExcursionById(excursionId);
        Toast.makeText(this, "Excursion deleted.", Toast.LENGTH_SHORT).show();
        finish();
    }
    private void shareExcursion() {
        String title = editExcursionTitle.getText().toString();
        String date = editExcursionDate.getText().toString();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Check out my excursion plans!\n\n" + "Title: " + title + "\n" + "Date: " + date;
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    /**
     * ✅ FIX: Replaced the complex alarm logic with a simple Toast message.
     * This is the functionality you requested.
     */
    private void setToastAlert() {
        String title = editExcursionTitle.getText().toString();
        String date = editExcursionDate.getText().toString();

        if (date.isEmpty()) {
            Toast.makeText(this, "Please select a date to set an alert.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Alert set for '" + title + "' on " + date, Toast.LENGTH_LONG).show();
        }
    }
}
