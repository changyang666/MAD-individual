package my.edu.utar.splitbill;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class CustomPercentage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_percentage);

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
                if (totalAmount.getText().toString().isEmpty()){
                    // do nothing
                    Toast.makeText(CustomPercentage.this, "Amount cannot be empty", Toast.LENGTH_LONG).show();
                }
                else if ((totalAmount.getText().toString().indexOf(".") < totalAmount.getText().length() - 3) & totalAmount.getText().toString().indexOf(".") != -1){
                    totalAmount.setText("");
                    Toast.makeText(CustomPercentage.this, "Decimal number cannot exceed 2", Toast.LENGTH_LONG).show();
                }
                else {
                    float amount = Float.parseFloat(totalAmount.getText().toString());
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
                //do nothing
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

        // to store the percentage used by each view
        ArrayList<String> percentArray = new ArrayList<>();

        okPersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // result display
                resultSection.setVisibility(View.VISIBLE);
                int num_of_person = Integer.parseInt(numberOfPerson.getText().toString());
                LayoutInflater inflater = LayoutInflater.from(CustomPercentage.this);

                resultLayout.removeAllViews();

                for (int i = 0; i < Integer.parseInt(numberOfPerson.getText().toString()); i++){
                    View res_view = inflater.inflate(R.layout.result, resultLayout, false);

                    int view_id = i;
                    res_view.setTag(view_id);
                    percentArray.add("0");

                    // result XML
                    EditText personName = res_view.findViewById(R.id.personName);
                    EditText value = res_view.findViewById(R.id.Value);
                    EditText amount = res_view.findViewById(R.id.Amount);
                    TextView title = res_view.findViewById(R.id.titleTextView);
                    Button valueConfirm = res_view.findViewById(R.id.valueConfirm);

                    Button amountConfirm = res_view.findViewById(R.id.amountOKButton);

                    title.setText("Person " + Integer.toString(i + 1));
                    value.setHint("Enter Percentage" );
                    personName.setHint("Person " + Integer.toString(i + 1));
                    amountConfirm.setVisibility(View.GONE);
                    amount.setFocusable(false);
                    amount.setFocusableInTouchMode(false);

                    float totalAmountValue = Float.parseFloat(totalAmount.getText().toString());

                    //user enter percentage
                    value.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            // do nothing
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            amount.setText("");
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            // do nothing
                        }
                    });

                    // percentage confirm Button
                    valueConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (!percentArray.isEmpty()){
                                percentArray.set(Integer.parseInt(res_view.getTag().toString()), "0");
                            }
                            // verify the percentage key in is valid
                            if (value.getText().toString().isEmpty()){
                                Toast.makeText(CustomPercentage.this, "Percentage cannot be empty", Toast.LENGTH_SHORT).show();
                            }
                            else if (value.getText().toString().contains(".")){
                                // invalid, should be only integers
                                value.setText("");
                                Toast.makeText(CustomPercentage.this, "Only integers are allowed", Toast.LENGTH_SHORT).show();
                            }
                            else if (Integer.parseInt(value.getText().toString()) > 100){
                                value.setText("");
                                Toast.makeText(CustomPercentage.this, "Invalid percentage (> 100)", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                percentArray.set(Integer.parseInt(res_view.getTag().toString()), value.getText().toString());
                                int sum = 0;
                                for(String i : percentArray){
                                    sum += Integer.parseInt(i);
                                }
                                if (sum > 100){
                                    value.setText("");
                                    percentArray.set(Integer.parseInt(res_view.getTag().toString()), "0");
                                    Toast.makeText(CustomPercentage.this, "Invalid percentage (sum > 100)", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(CustomPercentage.this, "Percentage Recorded " + Integer.toString(sum) + "%", Toast.LENGTH_SHORT).show();
                                    float percentage_in_decimal = Float.parseFloat(percentArray.get(Integer.parseInt(res_view.getTag().toString()))) / 100;
                                    String formatted = String.format("%.2f", totalAmountValue * percentage_in_decimal);
                                    amount.setText(formatted);
                                }

                                if (sum == 100){
                                    Toast.makeText(CustomPercentage.this, "Full 100%", Toast.LENGTH_SHORT).show();
                                    remainBar.setText("Remaining: " + Integer.toString(100 - sum) + "%");
                                }
                                else{
                                    amount.setText(String.format("%.2f", Float.parseFloat(amount.getText().toString())));
                                    remainBar.setText("Remaining: " + Integer.toString(100 - sum) + "%");
                                }
                            }
                        }
                    });

                    inflateView.add(res_view);
                    resultLayout.addView(res_view);
                }
            }
        });

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

                File imageFile = new File(getExternalFilesDir(null), formattedDateTime + "+CustomPercentage"+ " .png");
                try {
                    FileOutputStream fos = new FileOutputStream(imageFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();

                    Toast.makeText(CustomPercentage.this, "Image saved at " + getExternalFilesDir(null).toString(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}