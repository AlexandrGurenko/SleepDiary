package com.alexandr.gurenko.sleepdiary;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static final boolean START_SLEEP = true;
    static final boolean END_SLEEP = false;

    TextView lastAction;
    Button action, btnTime;
    DBHelper dbHelper;
    String[] goodNight, wakeUp;

    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lastAction = findViewById(R.id.lastAction);
        action = findViewById(R.id.action);
        action.setOnClickListener(this);

        btnTime = findViewById(R.id.changeTime);
        btnTime.setOnClickListener(this);

        calendar = Calendar.getInstance();

        goodNight = new String[]{"Сладких снов, наш маленький карапуз!:)",
                "Какой хороший мальчик, когда спит... Правда?:)",
                "Никому не двигаться - ребенок спит!!!",
                "А не хочешь ли ты тоже вздремнуть?",
                "Прятных сновидений, малыш:)"};

        wakeUp = new String[]{"А кто это у нас такой соня?:)",
                "Какой сейчас год?",
                "Ну вот я проснулся... Где мои игрушки?"
        };

        dbHelper = new DBHelper(this);
        if (dbHelper.getState() == 0) {
            action.setText(R.string.wake_up);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.action) {
            changeStateButton();
        } else if (v.getId() == R.id.changeTime) {
            setTime();
        }
    }

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            changeTime(calendar);
        }
    };

    public void setTime() {
        new TimePickerDialog(MainActivity.this, t,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true).show();
    }

    private void changeStateButton() {
        Random random = new Random();
        long time = System.currentTimeMillis();
        if (action.getText() == getResources().getString(R.string.sleep)) {
            dbHelper.action(time, START_SLEEP);
            action.setText(R.string.wake_up);
            lastAction.setText(goodNight[random.nextInt(goodNight.length)]);
        } else {
            long duration = dbHelper.action(time, END_SLEEP);
            action.setText(R.string.sleep);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(duration);
            toastShort(getHoursAndMin(calendar));
            lastAction.setText(wakeUp[random.nextInt(wakeUp.length)]);
        }
    }

    private void changeTime(Calendar calendar) {
        if (action.getText() == getResources().getString(R.string.wake_up)) {
            dbHelper.updateTime(calendar.getTimeInMillis(), START_SLEEP);
        } else {
            dbHelper.updateTime(calendar.getTimeInMillis(), END_SLEEP);
            toastShort(getHoursAndMin(calendar));
        }
    }

    private String getHoursAndMin(Calendar calendar) {
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        int h = calendar.get(Calendar.HOUR);
        int m = calendar.get(Calendar.MINUTE);
        return "Сон длился " + h + "ч. " + m + "м.";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.history) {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    public void toastShort(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
