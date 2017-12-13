package br.edu.ifspsaocarlos.agenda.activityV4;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import br.edu.ifspsaocarlos.agenda.R;
import br.edu.ifspsaocarlos.agenda.data.ContatoDAO;
import br.edu.ifspsaocarlos.agenda.model.ContatoV4;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class DetalheV4Activity extends AppCompatActivity {

    private ContatoV4 cv4;
    private ContatoDAO cDAO;
    private CheckBox checkBox;

    String name, fone, email, fone2, birth;
    int favorite=0;
    long idContact = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_v4);

        loadDataContact();
      ///  receiveData();
    }

    private void loadDataContact(){
        if (getIntent().hasExtra("contato"))
        {//UPDATE
            this.cv4 = (ContatoV4) getIntent().getSerializableExtra("contato");
            idContact = cv4.getId();
            EditText nameText = (EditText)findViewById(R.id.editTextNomeV2);
            nameText.setText(cv4.getNome());

            EditText foneText = (EditText)findViewById(R.id.editTextFoneV2);
            foneText.setText(cv4.getFone());
            EditText foneTextV3 = (EditText)findViewById(R.id.editTextFoneV3);
            foneTextV3.setText(cv4.getFone2());

            EditText emailText = (EditText)findViewById(R.id.editTextEmailV2);
            emailText.setText(cv4.getEmail());
            int pos =cv4.getNome().indexOf(" ");
            if (pos==-1)
                pos=cv4.getNome().length();
            setTitle(cv4.getNome().substring(0,pos));

            EditText birth = findViewById(R.id.edit_birth);
            birth.setText(cv4.getBirth());

            checkBox = findViewById(R.id.cb_favorito);
            favorite = cv4.getFavorite();

            if(favorite==1){
                checkBox.setChecked(true);
            }else{
                checkBox.setChecked(false);
            }
        }
        cDAO = new ContatoDAO(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalhe, menu);
        if (!getIntent().hasExtra("contato"))
        {
            MenuItem item = menu.findItem(R.id.delContato);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.salvarContato:
                salvar();
                return true;
            case R.id.delContato:
                apagar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void apagar()
    {
        cDAO.apagaContato(cv4);
        Intent resultIntent = new Intent();
        setResult(3,resultIntent);
        finish();
    }

    private void salvar()
    {
        name = ((EditText) findViewById(R.id.editTextNomeV2)).getText().toString();
        fone = ((EditText) findViewById(R.id.editTextFoneV2)).getText().toString();
        fone2 = ((EditText) findViewById(R.id.editTextFoneV3)).getText().toString();
        email = ((EditText) findViewById(R.id.editTextEmailV2)).getText().toString();
        birth = ((EditText) findViewById(R.id.edit_birth)).getText().toString();
        checkBox = findViewById(R.id.cb_favorito);

        if(checkBox.isChecked()){
            favorite=1;
        }else{
            favorite=0;
        }

        cv4 = new ContatoV4();
        if(idContact>0){
            cv4.setId(idContact);
        }
        cv4.setNome(name);
        cv4.setFone(fone);
        cv4.setFone2(fone2);
        cv4.setEmail(email);
        cv4.setFavorite(favorite);
        cv4.setBirth(birth);
        cDAO.salvaContatoV4(cv4);

        Intent resultIntent = new Intent();
        setResult(RESULT_OK,resultIntent);
        finish();
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

}
