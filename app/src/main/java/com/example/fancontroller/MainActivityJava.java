package com.example.fancontroller;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivityJava extends AppCompatActivity {

    //activity_main.xml'deki View'ları değiken olarak tanımlama işlemi
    EditText minuteTens;    //Dakikanın onlar basamağının bulunduğu değişken
    EditText minuteOnes;    //Dakikanın birler basamağının bulunduğu değişken
    EditText hourTens;    //Saatin onlar basamağının bulunduğu değişken
    EditText hourOnes;    //Saatin birler basamağının bulunduğu değişken
    Button startButton;     //Sayımı başlatma butonu
    Button stopButton;      //Vantilatörü anında durdurma butonu
    TextView timer3;    // ":" işareti
    ImageView recep;    //Recep resmi

    //EditText View'lerinden sayıları almaya yarayan fonksiyon
    private long getNumberFromEditText(EditText et){
        String text = et.getText().toString();
        if(text.isEmpty()){
            return 0;
        }
        return Long.parseLong(text);
    }

    CountDownTimer countDownTimer; //Public geri sayım nesnesi
    long mTens, mOnes, hTens, hOnes=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Butonları Bağlama İşlemi
        hourTens = findViewById(R.id.hourTens);
        hourOnes = findViewById(R.id.hourOnes);
        minuteTens = findViewById(R.id.minuteTens);
        minuteOnes = findViewById(R.id.minuteOnes);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        timer3 = findViewById(R.id.Timer3);
        recep = findViewById(R.id.recep);

        //Listener'ler
        //startButton On Click Listener:
        startButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        if(startButton.getText().toString().equals("Başlat")){
                            //Dakika ve saniye timer'lerinden süre alma:
                            mTens = getNumberFromEditText(minuteTens);
                            mOnes = getNumberFromEditText(minuteOnes);
                            hTens = getNumberFromEditText(hourTens);
                            hOnes = getNumberFromEditText(hourOnes);
                            long totalTime = (mTens*10+mOnes)*60+(hTens*10+hOnes)*3600; //Sürenin tamamının saniyeye çevirmiş hali.


                            //Başlat tuşunu iptal et yapma
                            startButton.setText("İptal Et");
                            startButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E2B6B7")));

                            //EditText'leri dokunulmaz yapma
                            minuteTens.setEnabled(false);
                            minuteOnes.setEnabled(false);
                            hourTens.setEnabled(false);
                            hourOnes.setEnabled(false);

                            //Geri Sayım
                            long totalMillis = totalTime*1000; //Milisaniye cinsinden
                            countDownTimer = new CountDownTimer(totalMillis, 1000){
                                public void onTick(long millisUntilFinished){ //millisUntilFinished: kalan milisaniye
                                    int secondsLeft = (int)(millisUntilFinished/1000); //Kalan toplam saniye
                                    int hours = secondsLeft/3600;   //Kalan saat
                                    int minutes = (secondsLeft%3600)/60;   //Kalan dakika

                                    //Kalanı EditText'lere Yazdırma:
                                    minuteTens.setText(String.valueOf(minutes/10));
                                    minuteOnes.setText(String.valueOf(minutes%10));
                                    hourTens.setText(String.valueOf(hours/10));
                                    hourOnes.setText(String.valueOf(hours%10));
                                }

                                public void onFinish() {    //Bitince uyarı mesajı yazıyor.
                                    Toast.makeText(MainActivityJava.this, "Süre Bitti\nVantilatör Kapatılıyor", Toast.LENGTH_SHORT).show();
                                    countDownTimer.cancel();

                                    //Sayacı tekrar dokunulabilir yapma
                                    minuteTens.setEnabled(true);
                                    minuteOnes.setEnabled(true);
                                    hourTens.setEnabled(true);
                                    hourOnes.setEnabled(true);

                                    //Başlat tuşunu düzeltme
                                    if(startButton.getText().toString().equals("İptal Et")){
                                        startButton.setText("Başlat");
                                        startButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B6D0E2")));
                                    }
                                }
                            }.start();
                        }else{
                            //İsmi düzletme
                            startButton.setText("Başlat");
                            //Rengi düzeltme
                            startButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B6D0E2")));

                            //Sayacı durdurma
                            countDownTimer.cancel();

                            //Sayacı başa alma
                            minuteTens.setText(String.valueOf(mTens));
                            minuteOnes.setText(String.valueOf(mOnes));
                            hourTens.setText(String.valueOf(hTens));
                            hourOnes.setText(String.valueOf(hOnes));

                            //Sayacı tekrar dokunulabilir yapma
                            minuteTens.setEnabled(true);
                            minuteOnes.setEnabled(true);
                            hourTens.setEnabled(true);
                            hourOnes.setEnabled(true);

                        }

                        //SÜRE SONUNDA VANTİLATÖRÜ KAPATMA SİNYALİ GÖNDERME ÖZELLİĞİ EKLENECEK
                    }
                }
        );

        //stopButton On Click Listener:
        stopButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Başlamış bir gerisayım varsa durdurur.
                        if(countDownTimer != null){
                            countDownTimer.cancel();
                        }

                        //Sayacı sıfırlama
                        minuteTens.setText("0");
                        minuteOnes.setText("0");
                        hourTens.setText("0");
                        hourOnes.setText("0");

                        //Durduruluyor Pop-Up'ı
                        Toast.makeText(MainActivityJava.this, "Vantilatör Kapatılıyor", Toast.LENGTH_SHORT).show();

                        //Sayacı tekrar dokunulabilir yapma
                        minuteTens.setEnabled(true);
                        minuteOnes.setEnabled(true);
                        hourTens.setEnabled(true);
                        hourOnes.setEnabled(true);

                        hourTens.clearFocus();
                        hourOnes.clearFocus();
                        minuteTens.clearFocus();
                        minuteOnes.clearFocus();

                        //Başlat tuşunu düzeltme
                        if(startButton.getText().toString().equals("İptal Et")){
                            startButton.setText("Başlat");
                            startButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B6D0E2")));
                        }

                        //VANTİLATÖRÜ KAPATMA SİNYALİ GÖNDERME KODLARI EKLENECEK
                    }
                }
        );

        //Sayaç Listener'leri
        hourTens.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus && hourTens.getText().toString().equals("0")){
                hourTens.setText("");
            } else if (hourTens.getText().toString().equals("")) {
                hourTens.setText("0");
            }
        });
        hourOnes.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus && hourOnes.getText().toString().equals("0")){
                hourOnes.setText("");
            }else if (hourOnes.getText().toString().equals("")) {
                hourOnes.setText("0");
            }
        });
        minuteTens.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus && minuteTens.getText().toString().equals("0")){
                minuteTens.setText("");
            }else if (minuteTens.getText().toString().equals("")) {
                minuteTens.setText("0");
            }
        });
        minuteOnes.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus && minuteOnes.getText().toString().equals("0")){
                minuteOnes.setText("");
            }else if (minuteOnes.getText().toString().equals("")) {
                minuteOnes.setText("0");
            }
        });

        //Sayaç basamak sınırı ve otomatik geçiş koyma
        hourTens.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==1){
                    hourTens.clearFocus();
                    hourOnes.requestFocus();
                    hourOnes.setSelection(hourOnes.getText().length());
                }
            }
        });
        hourOnes.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==1){
                    hourOnes.clearFocus();
                    minuteTens.requestFocus();
                    minuteTens.setSelection(minuteTens.getText().length());
                }
            }
        });
        minuteTens.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==1){
                    minuteTens.clearFocus();
                    minuteOnes.requestFocus();
                    minuteOnes.setSelection(minuteOnes.getText().length());
                }
            }
        });
        minuteOnes.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==1){
                    minuteOnes.clearFocus();
                    // Klavyeyi kapat
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(minuteOnes.getWindowToken(), 0);
                }
            }
        });

        //RİCEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEB
        final int[] recepCount = {0};
        timer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recepCount[0]++;
                if(recepCount[0]==18){
                    recep.setVisibility(View.VISIBLE);
                    recep.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            recep.setVisibility(View.GONE);
                        }
                    });
                    recepCount[0]=0;
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}