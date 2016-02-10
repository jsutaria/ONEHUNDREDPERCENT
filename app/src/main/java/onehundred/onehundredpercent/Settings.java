package onehundred.onehundredpercent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

public class Settings extends AppCompatActivity {
    TextView numberView;
    NumberPicker numberPicker;
    NumberPicker numberPicker1;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.sharedpreferences), Context.MODE_PRIVATE);

        Intent intent = getIntent();

        int dailyVal = intent.getIntExtra("dailyRequirement", 22);
        int weeklyVal = intent.getIntExtra("weeklyRequirement", 83);

        Log.v("Value", Integer.toString(dailyVal));
        Log.v("Value", Integer.toString(weeklyVal));


        numberPicker = (NumberPicker) findViewById(R.id.numberpickerDaily);
        numberPicker.setMaxValue(30);
        numberPicker.setMinValue(1);
        numberPicker.setValue(dailyVal);
        numberPicker.setWrapSelectorWheel(false);

        numberPicker1 = (NumberPicker) findViewById(R.id.numberpickerTotal);
        numberPicker1.setMinValue(numberPicker.getValue()*7);
        numberPicker1.setMaxValue(numberPicker.getMaxValue() * 7);
        numberPicker1.setValue(weeklyVal);
        numberPicker1.setWrapSelectorWheel(false);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                numberPicker1.setMinValue(newVal * 7);
                numberPicker1.setMaxValue(numberPicker.getMaxValue() * 7);
                numberPicker1.setValue(newVal * 7);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("dailyRequirement", numberPicker.getValue());
                editor.apply();
            }
        });

        numberPicker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("weeklyRequirement", numberPicker1.getValue());
                editor.apply();
            }
        });
    }

    public void ReturnToMain(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
