package my.edu.utar.splitbill;

import static my.edu.utar.splitbill.R.layout.result;

import android.animation.FloatArrayEvaluator;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button equal;
    Button custom;
    Button history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        equal = findViewById(R.id.equalSplitButton);
        custom = findViewById(R.id.customSplitButton);
        history = findViewById(R.id.historyButton);

        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EqualSplit.class);
                startActivity(intent);
            }
        });

        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Custom Split Selection");
                builder.setItems(new CharSequence[]{"By Percentage", "By Custom Amount"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        // Handle the selected option
                        if (i == 0) {
                            // Launch activity for percentage split
                            Intent intent = new Intent(MainActivity.this, CustomPercentage.class);
                            startActivity(intent);
                        } else if (i == 1) {
                            // Launch activity for custom amount split
                            Intent intent = new Intent(MainActivity.this, CustomAmount.class);
                            startActivity(intent);
                        }
                    }
                });
                builder.show();
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, history.class);
                startActivity(intent);
            }
        });
    }
}
