package br.edu.ifspsaocarlos.agenda.activityV2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import br.edu.ifspsaocarlos.agenda.R;
import br.edu.ifspsaocarlos.agenda.data.ContatoDAO;
import br.edu.ifspsaocarlos.agenda.model.Contato;
import br.edu.ifspsaocarlos.agenda.model.ContatoV2;

public class DetalheV2Activity extends AppCompatActivity {

    private ContatoV2 cv2;
    private ContatoDAO cDAO;
    private CheckBox checkBox;

    String name, fone, email;
    int favorite=0;
    long idContact = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_v2);

        loadDataContact();
    }

    private void loadDataContact(){
        if (getIntent().hasExtra("contato"))
        {//UPDATE
            Log.i("loadDataContact", "Yes or No");
            this.cv2 = (ContatoV2) getIntent().getSerializableExtra("contato");
            idContact = cv2.getId();
            EditText nameText = (EditText)findViewById(R.id.editTextNomeV2);
            nameText.setText(cv2.getNome());
            EditText foneText = (EditText)findViewById(R.id.editTextFoneV2);
            foneText.setText(cv2.getFone());
            EditText emailText = (EditText)findViewById(R.id.editTextEmailV2);
            emailText.setText(cv2.getEmail());
            int pos =cv2.getNome().indexOf(" ");
            if (pos==-1)
                pos=cv2.getNome().length();
            setTitle(cv2.getNome().substring(0,pos));
            checkBox = findViewById(R.id.cb_favorito);
            favorite = cv2.getFavorite();

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
        cDAO.apagaContato(cv2);
        Intent resultIntent = new Intent();
        setResult(3,resultIntent);
        finish();
    }

    private void salvar()
    {
        name = ((EditText) findViewById(R.id.editTextNomeV2)).getText().toString();
        fone = ((EditText) findViewById(R.id.editTextFoneV2)).getText().toString();
        email = ((EditText) findViewById(R.id.editTextEmailV2)).getText().toString();
        checkBox = findViewById(R.id.cb_favorito);

        if(checkBox.isChecked()){
            favorite=1;
        }else{
            favorite=0;
        }

        cv2 = new ContatoV2();
        if(idContact>0){
            cv2.setId(idContact);
        }
        cv2.setNome(name);
        cv2.setFone(fone);
        cv2.setEmail(email);
        cv2.setFavorite(favorite);
        cDAO.salvaContatoV2(cv2);

        Intent resultIntent = new Intent();
        setResult(RESULT_OK,resultIntent);
        finish();
    }

}
