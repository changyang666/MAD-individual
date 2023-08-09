package my.edu.utar.splitbill;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
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
import java.util.ArrayList;
import java.util.Calendar;

public class EqualSplit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equal_split);

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

        Button saveButton = findViewById(R.id.equalSaveButton);

        personLayout.setVisibility(View.GONE);
        resultSection.setVisibility(View.GONE);

        // make sure the amount key in is correct
        amountConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalAmount.getText().toString().isEmpty()){
                    // do nothing
                    Toast.makeText(EqualSplit.this, "Amount cannot be empty", Toast.LENGTH_LONG).show();
                }
                else if ((totalAmount.getText().toString().indexOf(".") < totalAmount.getText().length() - 3) & totalAmount.getText().toString().indexOf(".") != -1){
                    totalAmount.setText("");
                    Toast.makeText(EqualSplit.this, "Decimal number cannot exceed 2", Toast.LENGTH_LONG).show();
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

        okPersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // result display
                resultSection.setVisibility(View.VISIBLE);
                int num_of_person = Integer.parseInt(numberOfPerson.getText().toString());
                LayoutInflater inflater = LayoutInflater.from(EqualSplit.this);

                resultLayout.removeAllViews();

                //result.xml
                EditText personName;
                EditText value;
                EditText amount;
                TextView title;
                Button valueConfirm;

                View res_view = inflater.inflate(R.layout.result, resultLayout, false);

                // result XML
                personName = res_view.findViewById(R.id.personName);
                value = res_view.findViewById(R.id.Value);
                amount = res_view.findViewById(R.id.Amount);
                title = res_view.findViewById(R.id.titleTextView);
                valueConfirm = res_view.findViewById(R.id.valueConfirm);

                personName.setVisibility(View.GONE);
                value.setVisibility(View.GONE);
                valueConfirm.setVisibility(View.GONE);
                title.setText("Each person pay: ");

                // result calculation
                float totalAmountValue = Float.parseFloat(totalAmount.getText().toString());
                float numberOfPersonValue = Float.parseFloat(numberOfPerson.getText().toString());

                float divisionResult = totalAmountValue / numberOfPersonValue;

                String formattedResult = String.format("%.2f", divisionResult);

                amount.setText(formattedResult);
                resultLayout.addView(res_view);
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

                File imageFile = new File(getExternalFilesDir(null), formattedDateTime + "+EqualSplit" + " .png");
                try {
                    FileOutputStream fos = new FileOutputStream(imageFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(EqualSplit.this, "Image saved at " + getExternalFilesDir(null).toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}