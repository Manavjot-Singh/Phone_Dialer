package nz.massey.phone;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.Manifest;
import android.content.pm.PackageManager;



public class MainActivity extends AppCompatActivity {
    private EditText editTextNumber;
    private Button[] numbers = new Button[12];
    private ImageButton callButton, deleteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Declaring textfield
        editTextNumber = findViewById(R.id.editTextNumber);

        // Declaring buttons
        numbers[0] = findViewById(R.id.button11); // Number 1
        numbers[1] = findViewById(R.id.button10); // Number 2
        numbers[2] = findViewById(R.id.button12); // Number 3
        numbers[3] = findViewById(R.id.button13); // Number 4
        numbers[4] = findViewById(R.id.button14); // Number 5
        numbers[5] = findViewById(R.id.button15); // Number 6
        numbers[6] = findViewById(R.id.button7); // Number 7
        numbers[7] = findViewById(R.id.button8); // Number 8
        numbers[8] = findViewById(R.id.button9); // Number 9
        numbers[9] = findViewById(R.id.button17); // Number 0
        numbers[10] = findViewById(R.id.button16); // Number *
        numbers[11] = findViewById(R.id.button18); // Number #

        // Declaring call and delete buttons
        callButton = findViewById(R.id.imageButton); // call Button
        deleteButton = findViewById(R.id.imageButton3); // delete Button

        // Handle ACTION_DIAL intent
        Intent intent = getIntent();
        if (Intent.ACTION_DIAL.equals(intent.getAction())) {
            Uri data = intent.getData();
            if (data != null) {
                String phoneNumber = data.getSchemeSpecificPart();
                editTextNumber.setText(phoneNumber);
            }
        }

        // Calling the listener method
        setupButtonListeners();
    }


    // method for all action listeners
    private void setupButtonListeners() {
        for (int i = 0; i < numbers.length; i++) {
            int finalI = i;
            numbers[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String currentText = editTextNumber.getText().toString();
                    String buttonText = (finalI == 9) ? "0" :
                                        (finalI == 10) ? "*" :
                                        (finalI == 11) ? "#" :
                                        String.valueOf(finalI + 1); // map all numbers to array indexes
                    editTextNumber.setText(currentText + buttonText); // show number
                }
            });
        }
        // action listener for delete button
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentText = editTextNumber.getText().toString();
                if (!currentText.isEmpty()) {
                    editTextNumber.setText(currentText.substring(0,currentText.length() -1));
                }
            }
        });
        //action listener for call button
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = editTextNumber.getText().toString();
                if(!phoneNumber.isEmpty()) {
                    makePhoneCall(phoneNumber);
                }
                else {
                    Toast.makeText(MainActivity.this, "Enter a Phone Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // Dial the number
    private void makePhoneCall(String phoneNumber) {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else {
            String dial = "tel:" + phoneNumber;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }
    // check for permisiion
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String phoneNumber = editTextNumber.getText().toString();
                makePhoneCall(phoneNumber);
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}