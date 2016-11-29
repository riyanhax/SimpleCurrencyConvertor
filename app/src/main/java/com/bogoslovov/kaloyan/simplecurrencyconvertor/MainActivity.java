package com.bogoslovov.kaloyan.simplecurrencyconvertor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.bogoslovov.kaloyan.simplecurrencyconvertor.data.ECBData;

import static com.bogoslovov.kaloyan.simplecurrencyconvertor.Calculations.bottomSpinner;
import static com.bogoslovov.kaloyan.simplecurrencyconvertor.Calculations.topSpinner;
import static com.bogoslovov.kaloyan.simplecurrencyconvertor.data.ECBData.sharedPreferences;

public class MainActivity extends AppCompatActivity {

    Calculations calculations = new Calculations(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkIfSharedPreferenceExists();
        initSpinners();
        initSwapButton();
        initEditTextFields();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setElevation(0f);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkForConnection();
        System.out.println("The application started: onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("The application started: onResume");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();

        if(id==R.id.action_about){
            Intent intent = new Intent (this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void checkIfSharedPreferenceExists() {
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        if (!sharedPreferences.contains("EUR")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("EUR", "1");
            editor.putString("JPY", "116.95");
            editor.putString("BGN", "1.9558");
            editor.putString("CZK", "27.035");
            editor.putString("DKK", "7.4399");
            editor.putString("GBP", "0.86218");
            editor.putString("HUF", "309.49");
            editor.putString("USD", "1.0629");
            editor.putString("PLN", "4.4429");
            editor.putString("RON", "4.5150");
            editor.putString("SEK", "9.8243");
            editor.putString("CHF", "1.0711");
            editor.putString("NOK", "9.1038");
            editor.putString("HRK", "7.5320");
            editor.putString("RUB", "68.7941");
            editor.putString("TRY", "3.5798");
            editor.putString("AUD", "1.4376");
            editor.putString("BRL", "3.6049");
            editor.putString("CAD", "1.4365");
            editor.putString("CNY", "7.3156");
            editor.putString("HKD", "8.2450");
            editor.putString("IDR", "14272.09");
            editor.putString("ILS", "4.1163");
            editor.putString("INR", "72.2170");
            editor.putString("KRW", "1250.14");
            editor.putString("MXN", "21.6968");
            editor.putString("MYR", "4.6810");
            editor.putString("NZD", "1.5073");
            editor.putString("PHP", "52.687");
            editor.putString("SGD", "1.5107");
            editor.putString("THB", "37.744");
            editor.putString("ZAR", "15.2790");
            editor.putString("date","2016/11/18");
            editor.commit();
        }
    }

    private void initSwapButton(){
        Button swapButton = (Button) findViewById(R.id.swap_button);
        swapButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Spinner spinnerBottom = (Spinner) findViewById(R.id.spinner_bottom);
                Spinner spinnerTop = (Spinner) findViewById(R.id.spinner_top);
                int position = spinnerBottom.getSelectedItemPosition();
                spinnerBottom.setSelection(spinnerTop.getSelectedItemPosition());
                spinnerTop.setSelection(position);
            }
        });
    }

    private void initEditTextFields() {
        final EditText editTextTop = (EditText) findViewById(R.id.edit_text_top);
        editTextTop.setText("1");
        editTextTop.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            public void afterTextChanged(Editable editable) {
                if (editTextTop.isFocused()) {
                    if (!editTextTop.getText().toString().equals("")) {
                        calculations.calculate("top",topSpinner,bottomSpinner);
                    }
                    System.out.println("bottom listener activated");
                }
            }
        });

        final EditText editTextBottom = (EditText) findViewById(R.id.edit_text_bottom);
        editTextBottom.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            public void afterTextChanged(Editable editable) {
                if (editTextBottom.isFocused()) {
                    if (!editTextBottom.getText().toString().equals("")) {
                        calculations.calculate("bottom",topSpinner,bottomSpinner);
                    }
                    System.out.println("bottom listener activated");
                }
            }
        });
    }

    private void checkForConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() &&networkInfo.isAvailable()) {
            new ECBData(this).execute();
        }else{
            initLastUpdateTextField();
        }

    }

    private void initSpinners(){
        String [] currencies={"EUR Euro", "USD US dollar", "JPY Japanese yen", "BGN Bulgarian lev", "CZK Czech koruna",
                "DKK Danish krone", "GBP Pound sterling", "HUF Hungarian forint", "PLN Polish zloty", "RON Romanian leu",
                "SEK Swedish krona", "CHF Swiss franc", "NOK Norwegian krone", "HRK Croatian kuna", "RUB Russian rouble",
                "TRY Turkish lira", "AUD Australian dollar", "BRL Brazilian real", "CAD Canadian dollar", "CNY Chinese yuan",
                "HKD Hong Kong dollar", "IDR Indonesian rupiah", "ILS Israeli shekel", "INR Indian rupee", "KRW South Korean won",
                "MXN Mexican peso", "MYR Malaysian ringgit","NZD New Zealand dollar", "PHP Philippine peso",
                "SGD Singapore dollar", "THB Thai baht", "ZAR South African rand"};
        Integer [] images = {R.drawable.eur,R.drawable.usa,R.drawable.jpy,R.drawable.bgn,R.drawable.czk,
                R.drawable.dkk,R.drawable.gbp,R.drawable.huf,R.drawable.pln,R.drawable.ron,
                R.drawable.sek,R.drawable.chf,R.drawable.nok,R.drawable.hrk,R.drawable.rub,
                R.drawable.tryy,R.drawable.aud,R.drawable.brl,R.drawable.cad,R.drawable.cny,
                R.drawable.hkd,R.drawable.idr,R.drawable.ils,R.drawable.inr,R.drawable.krw,
                R.drawable.mxn,R.drawable.myr,R.drawable.nzd,R.drawable.php,
                R.drawable.sgd,R.drawable.thb,R.drawable.zar};

        final SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, R.layout.spinner_row,currencies,images);
        Spinner spinnerBottom = (Spinner) findViewById(R.id.spinner_bottom);
        Spinner spinnerTop = (Spinner) findViewById(R.id.spinner_top);
        spinnerTop.setAdapter(spinnerAdapter);
        spinnerBottom.setAdapter(spinnerAdapter);
        spinnerBottom.setSelection(1);
        spinnerTop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                topSpinner = spinnerAdapter.getItem(position);
                calculations.calculate("top",topSpinner,bottomSpinner);
            }
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        spinnerBottom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                bottomSpinner = spinnerAdapter.getItem(position);
                calculations.calculate("top",topSpinner,bottomSpinner);
            }
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }


    private void initLastUpdateTextField(){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        TextView lastUpdate = (TextView) findViewById(R.id.last_update_text_view);
        String date = "Last update: "+sharedPref.getString("date","");
        lastUpdate.setText(date);
    }

}
