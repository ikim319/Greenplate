package com.example.greenplate.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.example.greenplate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class CaloriesVsTime extends AppCompatActivity {
    private FirebaseManager manager = FirebaseManager.getInstance();
    private DatabaseReference rootDatabref;
    private DatabaseReference userDatabref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_intake);

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        Cartesian cartesian = AnyChart.line();
        cartesian.animation(true);
        cartesian.padding(10d, 20d, 5d, 20d);
        cartesian.crosshair().enabled(true);
        cartesian.crosshair().yLabel(true);
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();
        int totalDataPoints = 8; // Number of days to fetch data for
        AtomicInteger dataPointsFetched = new AtomicInteger(0);
        // Counter to track fetched data points

        // Fetch data for each day
        for (int i = -7; i <= 0; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, i); // Subtract 7 days
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1; // Months are zero-based
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String formattedDate = String.format(Locale.US, "%04d-%02d-%02d", year, month, day);

            fetchMealsForDay(formattedDate, calories -> {
                seriesData.add(new ValueDataEntry(formattedDate, calories));
                int fetchedCount = dataPointsFetched.incrementAndGet();
                if (fetchedCount == totalDataPoints) {
                    // All data points fetched, populate the chart
                    populateChart(cartesian, anyChartView, seriesData);
                }
            });
        }
    }

    private void populateChart(Cartesian cartesian, AnyChartView anyChartView,
                               List<DataEntry> seriesData) {
        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Line series1 = cartesian.line(series1Mapping);
        series1.name("Calories");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);
        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);
    }

    private void fetchMealsForDay(String formattedDate, final OnCaloriesFetchedListener listener) {
        String userId = manager.getAuth().getCurrentUser().getUid();
        DatabaseReference userMealRef = FirebaseManager.getInstance().getRef()
                .child("Users")
                .child(userId)
                .child("Meal_Log");
        userMealRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int todayCalories = 0;
                // Iterate through all meals
                for (DataSnapshot mealSnapshot : dataSnapshot.getChildren()) {
                    // Get the meal data
                    String mealDate = mealSnapshot.child("date").getValue(String.class);
                    String calories = mealSnapshot.child("calories").getValue(String.class);
                    // Check if the meal date matches today's date
                    if (mealDate != null && mealDate.equals(formattedDate)) {
                        // Add the calories to the total for today
                        if (calories != null && !calories.isEmpty()) {
                            todayCalories += Integer.parseInt(calories);
                        }
                    }
                }
                // Call the callback with the total calories for today
                listener.onCaloriesFetched(todayCalories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    // Define an interface for the callback
    interface OnCaloriesFetchedListener {
        void onCaloriesFetched(int calories);
    }


}