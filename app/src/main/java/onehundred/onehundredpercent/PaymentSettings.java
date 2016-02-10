package onehundred.onehundredpercent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

public class PaymentSettings extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    NumberPicker paymentSelectorWeekly;
    NumberPicker paymentSelectorDaily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_settings);

        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.sharedpreferences), Context.MODE_PRIVATE);

        paymentSelectorWeekly = (NumberPicker) findViewById(R.id.paymentWeekly);
        paymentSelectorWeekly.setMinValue(5);
        paymentSelectorWeekly.setMaxValue(250);
        paymentSelectorWeekly.setValue(sharedPreferences.getInt("weeklyPayment", 5));
        paymentSelectorWeekly.setWrapSelectorWheel(false);

        paymentSelectorDaily = (NumberPicker) findViewById(R.id.paymentDaily);
        paymentSelectorDaily.setMinValue(5);
        paymentSelectorDaily.setMaxValue(250);
        paymentSelectorDaily.setValue(sharedPreferences.getInt("dailyPayment", 5));
        paymentSelectorDaily.setWrapSelectorWheel(false);
    }

    public void ReturnToMain(View view)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("dailyPayment", paymentSelectorDaily.getValue());
        editor.putInt("weeklyPayment", paymentSelectorWeekly.getValue());

        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
