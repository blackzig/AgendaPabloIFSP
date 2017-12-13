package br.edu.ifspsaocarlos.agenda.activityV3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import br.edu.ifspsaocarlos.agenda.R;
import br.edu.ifspsaocarlos.agenda.data.ContatoDAO;
import br.edu.ifspsaocarlos.agenda.model.ContatoV2;
import br.edu.ifspsaocarlos.agenda.model.ContatoV3;

public class DetalheV3Activity extends AppCompatActivity {

    private ContatoV3 cv3;
    private ContatoDAO cDAO;
    private CheckBox checkBox;

    String name, fone, email, fone2;
    int favorite=0;
    long idContact = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_v3);

        loadDataContact();
    }

    private void loadDataContact(){
        if (getIntent().hasExtra("contato"))
        {//UPDATE
            this.cv3 = (ContatoV3) getIntent().getSerializableExtra("contato");
            idContact = cv3.getId();
            EditText nameText = (EditText)findViewById(R.id.editTextNomeV2);
            nameText.setText(cv3.getNome());

            EditText foneText = (EditText)findViewById(R.id.editTextFoneV2);
            foneText.setText(cv3.getFone());
            EditText foneTextV3 = (EditText)findViewById(R.id.editTextFoneV3);
            foneTextV3.setText(cv3.getFone2());

            EditText emailText = (EditText)findViewById(R.id.editTextEmailV2);
            emailText.setText(cv3.getEmail());
            int pos =cv3.getNome().indexOf(" ");
            if (pos==-1)
                pos=cv3.getNome().length();
            setTitle(cv3.getNome().substring(0,pos));
            checkBox = findViewById(R.id.cb_favorito);
            favorite = cv3.getFavorite();

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
        cDAO.apagaContato(cv3);
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
        checkBox = findViewById(R.id.cb_favorito);

        if(checkBox.isChecked()){
            favorite=1;
        }else{
            favorite=0;
        }

        cv3 = new ContatoV3();
        if(idContact>0){
            cv3.setId(idContact);
        }
        cv3.setNome(name);
        cv3.setFone(fone);
        cv3.setFone2(fone2);
        cv3.setEmail(email);
        cv3.setFavorite(favorite);
        cDAO.salvaContatoV3(cv3);

        Intent resultIntent = new Intent();
        setResult(RESULT_OK,resultIntent);
        finish();
    }

}
