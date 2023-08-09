package my.edu.utar.splitbill;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class CustomAmount extends AppCompatActivity {

    float amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_amount);

        // equal_split XML
        EditText totalAmount = findViewById(R.id.totalAmount);
        Button amountConfirmButton = findViewById(R.id.amountConfirmButton);

        LinearLayout personLayout = findViewById(R.id.personLayout);
        EditText numberOfPerson = findViewById(R.id.numberOfPerson);
        Button okPersonButton = findViewById(R.id.okPersonButton);
        Button addPersonButton = findViewById(R.id.addPersonButton);
        Button deductPersonButton = findViewById(R.id.deductPersonButton);

        LinearLayout resultSection = findViewById(R.id.ResultSection);
        LinearLayout resultLayout = findViewById(R.id.resultLayout);
        TextView remainBar = findViewById(R.id.remainBar);

        Button saveButton = findViewById(R.id.equalSaveButton);

        personLayout.setVisibility(View.GONE);
        resultSection.setVisibility(View.GONE);

        ArrayList<View> inflateView = new ArrayList<>();

        // make sure the amount key in is correct
        amountConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if amount is empty (invalid)
                if (totalAmount.getText().toString().isEmpty()){
                    // do nothing
                    Toast.makeText(CustomAmount.this, "Amount cannot be empty", Toast.LENGTH_LONG).show();
                }
                // check for decimal place more than 2 (invalid)
                else if ((totalAmount.getText().toString().indexOf(".") < totalAmount.getText().length() - 3) & totalAmount.getText().toString().indexOf(".") != -1){
                    totalAmount.setText("");
                    Toast.makeText(CustomAmount.this, "Decimal number cannot exceed 2", Toast.LENGTH_LONG).show();
                }
                // valid amount
                else {
                    amount = Float.parseFloat(totalAmount.getText().toString());
                    personLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        // make person layout gone when user changing value of amount
        totalAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                personLayout.setVisibility(View.GONE);
                resultSection.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // do nothing
            }
        });

        // set the function for the +, -, OK button
        addPersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultSection.setVisibility(View.GONE);
                numberOfPerson.setText(Integer.toString(Integer.parseInt(numberOfPerson.getText().toString()) + 1));
            }
        });

        deductPersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultSection.setVisibility(View.GONE);
                if (Integer.parseInt(numberOfPerson.getText().toString()) > 2){
                    numberOfPerson.setText(Integer.toString(Integer.parseInt(numberOfPerson.getText().toString()) - 1));
                }
            }
        });

        // to store the amount used by each view
        ArrayList<String> amountArray = new ArrayList<>();

        okPersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // result display
                resultSection.setVisibility(View.VISIBLE);
                int num_of_person = Integer.parseInt(numberOfPerson.getText().toString());
                LayoutInflater inflater = LayoutInflater.from(CustomAmount.this);

                resultLayout.removeAllViews();

                // create views based on the person count
                for (int i = 0; i < num_of_person; i++){
                    View res_view = inflater.inflate(R.layout.result, resultLayout, false);

                    // set tag for further identification
                    res_view.setTag(i);
                    amountArray.add(Integer.parseInt(res_view.getTag().toString()), "0");

                    // result XML
                    EditText personName = res_view.findViewById(R.id.personName);
                    EditText value = res_view.findViewById(R.id.Value);
                    EditText amount = res_view.findViewById(R.id.Amount);
                    TextView title = res_view.findViewById(R.id.titleTextView);
                    Button valueConfirm = res_view.findViewById(R.id.valueConfirm);
                    Button amountOKButton = res_view.findViewById(R.id.amountOKButton);

                    LinearLayout valueLayout = res_view.findViewById(R.id.valueLayout);

                    title.setText("Person " + Integer.toString(i + 1));
                    amount.setHint("Enter Amount ");
                    personName.setHint("Person " + Integer.toString(i + 1));

                    valueLayout.setVisibility(View.GONE);

                    // amount confirm Button
                    amountOKButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (!amountArray.isEmpty()){
                                amountArray.set(Integer.parseInt(res_view.getTag().toString()), "0");
                            }

                            // verify the percentage key in is valid

                            // amount is empty (invalid)
                            if (amount.getText().toString().isEmpty()){
                                Toast.makeText(CustomAmount.this, "Amount cannot be empty", Toast.LENGTH_SHORT).show();
                            }

                            // if 2 decimal or more exist / double decimal point (invalid)
                            else if ((amount.getText().toString().indexOf(".") < amount.getText().length() - 3) & amount.getText().toString().indexOf(".") != -1){
                                // invalid, should be only integers
                                amount.setText("");
                                Toast.makeText(CustomAmount.this, "More than 2 decimals / 2 dots", Toast.LENGTH_SHORT).show();
                            }

                            // input value are more than the total amount
                            else if (Float.parseFloat(amount.getText().toString()) > Float.parseFloat(totalAmount.getText().toString())){
                                amount.setText("");
                                Toast.makeText(CustomAmount.this, "Invalid amount (> " + totalAmount.getText().toString() + ")", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                // add to array (for checking validity)
                                amountArray.set(Integer.parseInt(res_view.getTag().toString()), amount.getText().toString());
                                double sum = 0.0;
                                for(String i : amountArray){
                                    sum += Double.parseDouble(i);
                                }

                                // if sum of all is > total amount (invalid)
                                if (sum > Float.parseFloat(totalAmount.getText().toString())){
                                    amount.setText("");
                                    amountArray.set(Integer.parseInt(res_view.getTag().toString()), "0");
                                    Toast.makeText(CustomAmount.this, "Invalid amount (sum > " + String.format("%.2f", Float.parseFloat(totalAmount.getText().toString())) + ")", Toast.LENGTH_SHORT).show();
                                }
                                // sum is < total amount
                                else{
                                    Toast.makeText(CustomAmount.this, "Amount Recorded RM " + String.format("%.2f", sum), Toast.LENGTH_SHORT).show();
                                }

                                if (sum == Float.parseFloat(totalAmount.getText().toString())){
                                    Toast.makeText(CustomAmount.this, "All amount assigned", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    amount.setText(String.format("%.2f", Float.parseFloat(amount.getText().toString())));
                                    Toast.makeText(CustomAmount.this, "total RM" + Double.toString(sum), Toast.LENGTH_SHORT).show();
                                    remainBar.setText("Remaining: RM" + String.format("%.2f", (Float.parseFloat(totalAmount.getText().toString()) - sum)));

                                }

                            }
                        }
                    });
                    inflateView.add(res_view);
                    resultLayout.addView(res_view);
                }
            }
        });

        // save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = Bitmap.createBitmap(resultLayout.getWidth(), resultLayout.getHeight(), Bitmap.Config.ARGB_8888);

                Canvas canvas = new Canvas(bitmap);
                resultLayout.draw(canvas);

                // Get the current date (as name of image)
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH); // Note: Month is zero-based (0 - January, 11 - December)
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);

                String formattedDateTime = String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month + 1, day, hour, minute, second);

                File imageFile = new File(getExternalFilesDir(null), formattedDateTime + "+CustomAmount" + " .png");
                try {
                    FileOutputStream fos = new FileOutputStream(imageFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();

                    Toast.makeText(CustomAmount.this, "Image saved at " + getExternalFilesDir(null).toString(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}