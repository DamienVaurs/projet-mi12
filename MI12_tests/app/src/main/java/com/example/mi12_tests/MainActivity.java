package com.example.mi12_tests;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    private List<com.example.mi12_tests.Measurement> measurements = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.print("Clicked MEASURE RSSI Button");
                // Ask the user to input the point coordinates
                EditText xInput = findViewById(R.id.x_input);
                EditText yInput = findViewById(R.id.y_input);
                double x = Double.parseDouble(xInput.getText().toString());
                double y = Double.parseDouble(yInput.getText().toString());
                System.out.print("Points : " + x + " " + y);
                Point point = new Point(x, y);
                // Ask the user to input the device address
                EditText addressInput = findViewById(R.id.address_input);
                String address = addressInput.getText().toString();
                // Create a measurement with the point and device address
                Measurement measurement = new Measurement(point, address);
                // Add the measurement to the list
                measurements.add(measurement);
            }
        });

    }
}