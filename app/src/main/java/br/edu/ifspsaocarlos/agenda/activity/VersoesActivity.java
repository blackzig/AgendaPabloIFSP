package br.edu.ifspsaocarlos.agenda.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import br.edu.ifspsaocarlos.agenda.R;
import br.edu.ifspsaocarlos.agenda.message.Message;

public class VersoesActivity extends AppCompatActivity {
    Spinner spinner_versions;
    Button button_save_version;
    TextView show_version, description;

    Message m = new Message();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_versoes);

        spinner_versions = findViewById(R.id.spinner_versions);
        button_save_version = findViewById(R.id.button_save_version);
        description = findViewById(R.id.description);

        button_save_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("Versoes",0);
                SharedPreferences.Editor editor = prefs.edit();
                String versao = spinner_versions.getSelectedItem().toString();
                editor.putString("versao", versao);
                editor.apply();
                m.ShortMessage(VersoesActivity.this, "Configurações salvas.");
                finish();
                System.exit(0);
            }
        });

        comboboxVersions();
        loadSettings();
    }

    private void loadSettings(){
        show_version = findViewById(R.id.show_version);
        SharedPreferences prefs = getSharedPreferences("Versoes", 0);
        String versao = prefs.getString("versao", null);
        show_version.setText(versao);
    }

    public void comboboxVersions(){
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.which_version, android.R.layout.simple_spinner_item);
        spinner_versions.setAdapter(arrayAdapter);

        AdapterView.OnItemSelectedListener choiced = new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String versao = spinner_versions.getSelectedItem().toString();
                    m.ShortMessage(VersoesActivity.this, "Versão " + versao +" escolhida.");

                    if(versao.equals("1")){
                        description.setText("Na versão um, somente é possível cadastrar um contato com nome, " +
                                "telefone e email.");
                    }else if(versao.equals("2")){
                        description.setText("Na versão dois é possível favoritar contatos. " +
                                "Possibilidade de mostrar só os contatos como favoritos.");
                    }else if(versao.equals("3")){
                        description.setText("Na versão três é possível adicionar mais de um número de " +
                                "telefone. ");
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
        spinner_versions.setOnItemSelectedListener(choiced);
    }
}
