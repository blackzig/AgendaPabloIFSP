package br.edu.ifspsaocarlos.agenda.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import br.edu.ifspsaocarlos.agenda.R;

public class VersoesActivity extends AppCompatActivity {
    Spinner spinner_versions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_versoes);

        spinner_versions = findViewById(R.id.spinner_versions);

        comboboxVersions();
    }

    public void comboboxVersions(){
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.which_version, android.R.layout.simple_spinner_item);
        spinner_versions.setAdapter(arrayAdapter);

        AdapterView.OnItemSelectedListener choiced = new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String versao = spinner_versions.getSelectedItem().toString();
                Toast.makeText(VersoesActivity.this, "Versão " + versao +" escolhida.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
        spinner_versions.setOnItemSelectedListener(choiced);
    }


}
