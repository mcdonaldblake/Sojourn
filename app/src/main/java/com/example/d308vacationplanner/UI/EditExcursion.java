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

import com.example.d308vacationplanner.R;
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

public class EditExcursion extends AppCompatActivity {

    private DetailViewModel mDetailViewModel;

    private int excursionId;
    private int vacationId;


    private TextInputEditText editExcursionTitle;
    private TextInputEditText editExcursionDate;

    private String vacationStartDateStr;
    private String vacationEndDateStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_edit);

        mDetailViewModel = new ViewModelProvider(this).get(DetailViewModel.class);


        excursionId = getIntent().getIntExtra("id", -1);
        String excursionName = getIntent().getStringExtra("name");
        String excursionDate = getIntent().getStringExtra("date");
        vacationId = getIntent().getIntExtra("vacationId", -1);
        vacationStartDateStr = getIntent().getStringExtra("vacationStartDate");
        vacationEndDateStr = getIntent().getStringExtra("vacationEndDate");



        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(excursionName);
        }


        editExcursionTitle = findViewById(R.id.edit_text_excursion_title);
        editExcursionDate = findViewById(R.id.edit_text_excursion_date);
        editExcursionTitle.setText(excursionName);
        editExcursionDate.setText(excursionDate);


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


        if (title.isEmpty() || dateStr.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
        try {
            Date excursionDate = sdf.parse(dateStr);
            Date vacationStartDate = sdf.parse(vacationStartDateStr);
            Date vacationEndDate = sdf.parse(vacationEndDateStr);

            if (excursionDate == null) {
                Toast.makeText(this, "Invalid date input.", Toast.LENGTH_LONG).show();
                return;
            }

            if (excursionDate.before(vacationStartDate) || excursionDate.after(vacationEndDate)) {
                Toast.makeText(this, "Excursion date must be within the vacation's start and end dates.", Toast.LENGTH_LONG).show();
                return;
            }

        } catch (ParseException e) {
            Toast.makeText(this, "Invalid date format. Please use MM/dd/yy.", Toast.LENGTH_LONG).show();
            return;
        }

        // Update or insert the excursion
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


    private void setToastAlert() {
        String title = editExcursionTitle.getText().toString();
        String dateStr = editExcursionDate.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);

        if (dateStr.isEmpty()) {
            Toast.makeText(this, "Please select a date to set an alert.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Date excursionDate = sdf.parse(dateStr);
            Date vacationStartDate = sdf.parse(vacationStartDateStr);
            Date vacationEndDate = sdf.parse(vacationEndDateStr);

            if (excursionDate.before(vacationStartDate) || excursionDate.after(vacationEndDate)) {
                Toast.makeText(this, "Excursion date must be within the vacation's start and end dates.", Toast.LENGTH_LONG).show();
                return;
            }


            // Normalize today
            Calendar todayCal = Calendar.getInstance();
            todayCal.set(Calendar.HOUR_OF_DAY, 0);
            todayCal.set(Calendar.MINUTE, 0);
            todayCal.set(Calendar.SECOND, 0);
            todayCal.set(Calendar.MILLISECOND, 0);
            Date today = todayCal.getTime();

            Toast.makeText(this, "Alerts have been set.", Toast.LENGTH_SHORT).show();

            if (isSameDay(excursionDate, today)) {

                Toast.makeText(this, "Your excursion '" + title + "' is today!", Toast.LENGTH_LONG).show();
            } else if (excursionDate.after(today)) {

                long delayMillis = excursionDate.getTime() - today.getTime();
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    Toast.makeText(this, "Reminder: Your excursion '" + title + "' is today!", Toast.LENGTH_LONG).show();
                }, delayMillis);
                Toast.makeText(this, "Alert set for excursion on " + dateStr, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "The excursion date is in the past. No alert set.", Toast.LENGTH_SHORT).show();
            }

        } catch (ParseException e) {
            Toast.makeText(this, "Invalid date format. Please use MM/dd/yy.", Toast.LENGTH_SHORT).show();
        }
    }
}