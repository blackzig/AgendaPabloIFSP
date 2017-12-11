package br.edu.ifspsaocarlos.agenda.data;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import br.edu.ifspsaocarlos.agenda.message.Message;
import br.edu.ifspsaocarlos.agenda.model.Contato;
import br.edu.ifspsaocarlos.agenda.model.ContatoV2;

import java.util.ArrayList;
import java.util.List;


public class ContatoDAO {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    private Context appContext;

    List<Contato> contatos = new ArrayList();
    List<ContatoV2> contatosV2 = new ArrayList();

    public ContatoDAO(Context context) {
        this.dbHelper=new SQLiteHelper(context);
        this.appContext = context;
    }

    public <T> List<T> buscaTodosContatos()
    {
        SharedPreferences prefs = appContext.getSharedPreferences("Versoes", 0);
        String versao = prefs.getString("versao", null);
        int version = Integer.parseInt(versao);

        database=dbHelper.getReadableDatabase();

        switch (version){
            case 1:
                return (List<T>) (contatos = searchAllContactsVersion1());
            case 2:
                Log.i("Buscar V2", "Funcionou?");
                return (List<T>) (contatosV2 = searchAllContactsVersion2());
            default:
                Message m = new Message();
                m.LongMessage(appContext, "Você está utilizando a versão " + version);
        }

        database.close();
        Log.i("return ", "hora de retornar a lista de contatos");
        return null;
    }

    private List<Contato> searchAllContactsVersion1(){
        contatos.clear();
        Cursor cursor;

        String[] cols=new String[] {SQLiteHelper.KEY_ID,SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_FONE, SQLiteHelper.KEY_EMAIL};

        cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, null , null,
                null, null, SQLiteHelper.KEY_NAME);

        while (cursor.moveToNext())
        {
            Contato contato = new Contato();
            contato.setId(cursor.getInt(0));
            contato.setNome(cursor.getString(1));
            contato.setFone(cursor.getString(2));
            contato.setEmail(cursor.getString(3));
            contatos.add(contato);
        }
        cursor.close();
        return contatos;
    }

    private List<ContatoV2> searchAllContactsVersion2(){
        contatosV2.clear();
        Cursor cursor;

        String[] cols=new String[] {SQLiteHelper.KEY_ID,SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_FONE, SQLiteHelper.KEY_EMAIL, SQLiteHelper.KEY_FAVORITE};

        cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, null , null,
                null, null, SQLiteHelper.KEY_NAME);

        while (cursor.moveToNext())
        {
            ContatoV2 contato = new ContatoV2();
            contato.setId(cursor.getInt(0));
            contato.setNome(cursor.getString(1));
            contato.setFone(cursor.getString(2));
            contato.setEmail(cursor.getString(3));
            contato.setFavorite(cursor.getInt(4));
            contatosV2.add(contato);
        }
        cursor.close();
        Log.i("size contatosV2 ", String.valueOf(contatosV2.size()));
        return contatosV2;
    }

    public <T>  List<T> buscaContato(String nome)
    {
        database=dbHelper.getReadableDatabase();
        List<Contato> contatos = new ArrayList<>();

        Cursor cursor;

        String[] cols=new String[] {SQLiteHelper.KEY_ID,SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_FONE, SQLiteHelper.KEY_EMAIL};
        String where=SQLiteHelper.KEY_NAME + " like ?";
        String[] argWhere=new String[]{nome + "%"};


        cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, where , argWhere,
                null, null, SQLiteHelper.KEY_NAME);


        while (cursor.moveToNext())
        {
            Contato contato = new Contato();
            contato.setId(cursor.getInt(0));
            contato.setNome(cursor.getString(1));
            contato.setFone(cursor.getString(2));
            contato.setEmail(cursor.getString(3));
            contatos.add(contato);
        }
        cursor.close();

        database.close();
        return (List<T>) contatos;
    }

    public void salvaContato(Contato c) {
        database=dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.KEY_NAME, c.getNome());
        values.put(SQLiteHelper.KEY_FONE, c.getFone());
        values.put(SQLiteHelper.KEY_EMAIL, c.getEmail());

       if (c.getId()>0)
          database.update(SQLiteHelper.DATABASE_TABLE, values, SQLiteHelper.KEY_ID + "="
                + c.getId(), null);
        else
           database.insert(SQLiteHelper.DATABASE_TABLE, null, values);

        database.close();
    }

    public void salvaContatoV2(ContatoV2 c) {
        Log.i(">>>>>>>>>>>>>>>>>>>>>>", "Salvando contato na versão 2");
        Log.i("ID>>>>>>>>>>>>>>>>>>>>", String.valueOf(c.getId()));
        Log.i("Favorite>>>>>>>>>>>>>>>", String.valueOf(c.getFavorite()));
        database=dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.KEY_NAME, c.getNome());
        values.put(SQLiteHelper.KEY_FONE, c.getFone());
        values.put(SQLiteHelper.KEY_EMAIL, c.getEmail());
        values.put(SQLiteHelper.KEY_FAVORITE, c.getFavorite());

        if (c.getId()>0)
            database.update(SQLiteHelper.DATABASE_TABLE, values, SQLiteHelper.KEY_ID + "="
                    + c.getId(), null);
        else
            database.insert(SQLiteHelper.DATABASE_TABLE, null, values);

        database.close();
    }

    public void apagaContato(Contato c)
    {
        database=dbHelper.getWritableDatabase();
        database.delete(SQLiteHelper.DATABASE_TABLE, SQLiteHelper.KEY_ID + "="
                + c.getId(), null);
        database.close();
    }

    public void apagaContato(ContatoV2 c)
    {
        Log.i("remover v2>>>>>", String.valueOf(c.getId()));
        database=dbHelper.getWritableDatabase();
        database.delete(SQLiteHelper.DATABASE_TABLE, SQLiteHelper.KEY_ID + "="
                + c.getId(), null);
        database.close();
    }
}
