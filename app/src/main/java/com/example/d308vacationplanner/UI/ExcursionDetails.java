package com.example.d308vacationplanner.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308vacationplanner.R;
import com.example.d308vacationplanner.database.Repository;

public class ExcursionDetails extends AppCompatActivity {
    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion_details);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back button
        getSupportActionBar().setTitle("Excursion Details");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        // 1. Get the Intent that started this activity
        Intent intent = getIntent();

        // 2. Extract the data passed from the adapter
        String excursionName = intent.getStringExtra("name");
        // Get other extras like id and prodId if you need them

        // 3. Find the TextViews from your new layout
        TextView nameTextView = findViewById(R.id.excursionRecyclerView);
        TextView priceTextView = findViewById(R.id.excursionRecyclerView);

        // 4. Set the data into the TextViews
        nameTextView.setText(excursionName);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Check if the up arrow (home button) was pressed
        if (item.getItemId() == android.R.id.home) {
            // This line tells the current activity to close itself and go back.
            // It's the same behavior as the system's "Back" button.
            this.finish();
            return true;
        }

        // ... handle your other menu items like "Save", "Delete", etc. ...
        if (item.getItemId() == R.id.action_save_vacation) {
            // ... your save logic ...
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}