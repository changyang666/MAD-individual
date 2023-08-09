package my.edu.utar.splitbill;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.util.zip.Inflater;

public class history extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        LinearLayout historyLayout = findViewById(R.id.historyLayout);

        File directory = new File(getExternalFilesDir(null).toString());
        Toast.makeText(history.this, "Image saved at " + getExternalFilesDir(null).toString(), Toast.LENGTH_LONG).show();

        File[] files = directory.listFiles();
        LayoutInflater inflater = LayoutInflater.from(history.this);

        for (File file : files) {

            String filename = file.getName().toString();

            int indexDot = filename.indexOf('.');
            int indexPlus = filename.indexOf('+');
            String date_type = filename.substring(0, indexDot);

            String date = date_type.substring(0, indexPlus);
            String type = date_type.substring(indexPlus + 1, indexDot);

            View his_view = inflater.inflate(R.layout.history, historyLayout, false);

            his_view.setTag(filename);

            TextView historyType = his_view.findViewById(R.id.historyType);

            historyType.setText("Type: " + type + "\nDate: " + date);

            his_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Inflate the popup layout
                    View popupView = getLayoutInflater().inflate(R.layout.pop_out_image, null);

                    // Create a PopupWindow and set its properties
                    PopupWindow popupWindow = new PopupWindow(
                            popupView,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            true
                    );

                    // Get the ImageView from the popup layout
                    ImageView popupImageView = popupView.findViewById(R.id.popupImageView);

                    // Load the desired image into the ImageView
                    Bitmap bitmap = BitmapFactory.decodeFile(getExternalFilesDir(null).toString() + "/" + his_view.getTag().toString());
                    popupImageView.setImageBitmap(bitmap); // Replace with your image resource

                    // Show the popup window
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                    popupView.setBackgroundColor(Color.BLACK);

                    popupView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });
                }
            });

            historyLayout.addView(his_view);
        }


    }
}