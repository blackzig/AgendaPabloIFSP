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
import br.edu.ifspsaocarlos.agenda.model.ContatoV3;
import br.edu.ifspsaocarlos.agenda.model.ContatoV4;

import java.util.ArrayList;
import java.util.List;

public class ContatoDAO {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private Context appContext;

    List<Contato> contatos = new ArrayList();
    List<ContatoV2> contatosV2 = new ArrayList();
    List<ContatoV3> contatosV3 = new ArrayList();
    List<ContatoV4> contatosV4 = new ArrayList();

    String versao = "";

    public ContatoDAO(Context context) {
        this.dbHelper=new SQLiteHelper(context);
        this.appContext = context;

        SharedPreferences prefs = appContext.getSharedPreferences("Versoes", 0);
        versao = prefs.getString("versao", null);
    }

    public <T> List<T> buscaTodosContatos()
    {
        int version = Integer.parseInt(versao);

        database=dbHelper.getReadableDatabase();

        switch (version){
            case 1:
                return (List<T>) (contatos = searchAllContactsVersion1());
            case 2:
                return (List<T>) (contatosV2 = searchAllContactsVersion2());
            case 3:
                return (List<T>) (contatosV3 = searchAllContactsVersion3());
            case 4:
                return (List<T>) (contatosV4 = searchAllContactsVersion4());
            default:
                Message m = new Message();
                m.LongMessage(appContext, "Você está utilizando a versão " + version);
        }

        database.close();
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
        return contatosV2;
    }

    private List<ContatoV3> searchAllContactsVersion3(){
        contatosV3.clear();
        Cursor cursor;

        String[] cols=new String[] {SQLiteHelper.KEY_ID,SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_FONE, SQLiteHelper.KEY_EMAIL, SQLiteHelper.KEY_FAVORITE, SQLiteHelper.KEY_FONE2};

        cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, null , null,
                null, null, SQLiteHelper.KEY_NAME);

        while (cursor.moveToNext())
        {
            ContatoV3 contato = new ContatoV3();
            contato.setId(cursor.getInt(0));
            contato.setNome(cursor.getString(1));
            contato.setFone(cursor.getString(2));
            contato.setEmail(cursor.getString(3));
            contato.setFavorite(cursor.getInt(4));
            contato.setFone2(cursor.getString(5));
            contatosV3.add(contato);
        }
        cursor.close();
        return contatosV3;
    }

    private List<ContatoV4> searchAllContactsVersion4(){
        contatosV4.clear();
        Cursor cursor;

        String[] cols=new String[] {SQLiteHelper.KEY_ID,SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_FONE, SQLiteHelper.KEY_EMAIL, SQLiteHelper.KEY_FAVORITE, SQLiteHelper.KEY_FONE2, SQLiteHelper.KEY_BIRTH};

        cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, null , null,
                null, null, SQLiteHelper.KEY_NAME);

        while (cursor.moveToNext())
        {
            ContatoV4 contato = new ContatoV4();
            contato.setId(cursor.getInt(0));
            contato.setNome(cursor.getString(1));
            contato.setFone(cursor.getString(2));
            contato.setEmail(cursor.getString(3));
            contato.setFavorite(cursor.getInt(4));
            contato.setFone2(cursor.getString(5));
            contato.setBirth(cursor.getString(6));
            contatosV4.add(contato);
        }
        cursor.close();
        return contatosV4;
    }

    public List<ContatoV2> returnJustFavoritesContacts(){
        contatosV2.clear();
        Cursor cursor;

        String[] cols=new String[] {SQLiteHelper.KEY_ID,SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_FONE, SQLiteHelper.KEY_EMAIL, SQLiteHelper.KEY_FAVORITE};
        String where=SQLiteHelper.KEY_FAVORITE + " = ?";
        String[] argWhere=new String[]{"1"};

        cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, where , argWhere,
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
        return contatosV2;
    }

    public List<ContatoV3> returnJustFavoritesContactsV3(){
        contatosV3.clear();
        Cursor cursor;

        String[] cols=new String[] {SQLiteHelper.KEY_ID,SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_FONE, SQLiteHelper.KEY_EMAIL, SQLiteHelper.KEY_FAVORITE, SQLiteHelper.KEY_FONE2};
        String where=SQLiteHelper.KEY_FAVORITE + " = ?";
        String[] argWhere=new String[]{"1"};

        cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, where , argWhere,
                null, null, SQLiteHelper.KEY_NAME);

        while (cursor.moveToNext())
        {
            ContatoV3 contato = new ContatoV3();
            contato.setId(cursor.getInt(0));
            contato.setNome(cursor.getString(1));
            contato.setFone(cursor.getString(2));
            contato.setEmail(cursor.getString(3));
            contato.setFavorite(cursor.getInt(4));
            contato.setFone2(cursor.getString(5));
            contatosV3.add(contato);
        }
        cursor.close();
        return contatosV3;
    }

    public List<ContatoV4> returnJustFavoritesContactsV4(){
        contatosV4.clear();
        Cursor cursor;

        String[] cols=new String[] {SQLiteHelper.KEY_ID,SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_FONE, SQLiteHelper.KEY_EMAIL, SQLiteHelper.KEY_FAVORITE, SQLiteHelper.KEY_FONE2, SQLiteHelper.KEY_BIRTH};
        String where=SQLiteHelper.KEY_FAVORITE + " = ?";
        String[] argWhere=new String[]{"1"};

        cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, where , argWhere,
                null, null, SQLiteHelper.KEY_NAME);

        while (cursor.moveToNext())
        {
            ContatoV4 contato = new ContatoV4();
            contato.setId(cursor.getInt(0));
            contato.setNome(cursor.getString(1));
            contato.setFone(cursor.getString(2));
            contato.setEmail(cursor.getString(3));
            contato.setFavorite(cursor.getInt(4));
            contato.setFone2(cursor.getString(5));
            contato.setBirth(cursor.getString(6));
            contatosV4.add(contato);
        }
        cursor.close();
        return contatosV4;
    }

    public <T> List<T> buscaContato(String nome)
    {
        database=dbHelper.getReadableDatabase();
        List <T> contatos = new ArrayList<>();
        Cursor cursor = null;

        if(versao.equals("1")){
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
                contatos.add((T) contato);
            }
        }else if(versao.equals("2")){
            String[] cols=new String[] {SQLiteHelper.KEY_ID,SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_FONE, SQLiteHelper.KEY_EMAIL, SQLiteHelper.KEY_FAVORITE};
            String where=SQLiteHelper.KEY_NAME + " like ?";
            String[] argWhere=new String[]{nome + "%"};

            cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, where , argWhere,
                    null, null, SQLiteHelper.KEY_NAME);

            while (cursor.moveToNext())
            {
                ContatoV2 contato = new ContatoV2();
                contato.setId(cursor.getInt(0));
                contato.setNome(cursor.getString(1));
                contato.setFone(cursor.getString(2));
                contato.setEmail(cursor.getString(3));
                contato.setFavorite(cursor.getInt(4));
                contatos.add((T) contato);
            }
        }else if(versao.equals("3")){
            String[] cols=new String[] {SQLiteHelper.KEY_ID,SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_FONE, SQLiteHelper.KEY_EMAIL, SQLiteHelper.KEY_FAVORITE, SQLiteHelper.KEY_FONE2};
            String where=SQLiteHelper.KEY_NAME + " like ?";
            String[] argWhere=new String[]{nome + "%"};

            cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, where , argWhere,
                    null, null, SQLiteHelper.KEY_NAME);

            while (cursor.moveToNext())
            {
                ContatoV3 contato = new ContatoV3();
                contato.setId(cursor.getInt(0));
                contato.setNome(cursor.getString(1));
                contato.setFone(cursor.getString(2));
                contato.setEmail(cursor.getString(3));
                contato.setFavorite(cursor.getInt(4));
                contato.setFone2(cursor.getString(5));
                contatos.add((T) contato);
            }
        }else if(versao.equals("4")){
            String[] cols=new String[] {SQLiteHelper.KEY_ID,SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_FONE, SQLiteHelper.KEY_EMAIL, SQLiteHelper.KEY_FAVORITE, SQLiteHelper.KEY_FONE2, SQLiteHelper.KEY_BIRTH};
            String where=SQLiteHelper.KEY_NAME + " like ? or " +SQLiteHelper.KEY_EMAIL+" like ?";
            String[] argWhere=new String[]{nome, nome + "%"};

            cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, where , argWhere,
                    null, null, SQLiteHelper.KEY_NAME);

            while (cursor.moveToNext())
            {
                ContatoV4 contato = new ContatoV4();
                contato.setId(cursor.getInt(0));
                contato.setNome(cursor.getString(1));
                contato.setFone(cursor.getString(2));
                contato.setEmail(cursor.getString(3));
                contato.setFavorite(cursor.getInt(4));
                contato.setFone2(cursor.getString(5));
                contato.setBirth(cursor.getString(6));
                contatos.add((T) contato);
            }
        }
        cursor.close();
        database.close();
        return contatos;
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

    public void salvaContatoV3(ContatoV3 c) {
        database=dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.KEY_NAME, c.getNome());
        values.put(SQLiteHelper.KEY_FONE, c.getFone());
        values.put(SQLiteHelper.KEY_FONE2, c.getFone2());
        values.put(SQLiteHelper.KEY_EMAIL, c.getEmail());
        values.put(SQLiteHelper.KEY_FAVORITE, c.getFavorite());

        if (c.getId()>0)
            database.update(SQLiteHelper.DATABASE_TABLE, values, SQLiteHelper.KEY_ID + "="
                    + c.getId(), null);
        else
            database.insert(SQLiteHelper.DATABASE_TABLE, null, values);

        database.close();
    }

    public void salvaContatoV4(ContatoV4 c) {
        database=dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.KEY_NAME, c.getNome());
        values.put(SQLiteHelper.KEY_FONE, c.getFone());
        values.put(SQLiteHelper.KEY_FONE2, c.getFone2());
        values.put(SQLiteHelper.KEY_EMAIL, c.getEmail());
        values.put(SQLiteHelper.KEY_FAVORITE, c.getFavorite());
        values.put(SQLiteHelper.KEY_BIRTH, c.getBirth());

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
        database=dbHelper.getWritableDatabase();
        database.delete(SQLiteHelper.DATABASE_TABLE, SQLiteHelper.KEY_ID + "="
                + c.getId(), null);
        database.close();
    }

    public void apagaContato(ContatoV3 c)
    {
        database=dbHelper.getWritableDatabase();
        database.delete(SQLiteHelper.DATABASE_TABLE, SQLiteHelper.KEY_ID + "="
                + c.getId(), null);
        database.close();
    }

    public void apagaContato(ContatoV4 c)
    {
        database=dbHelper.getWritableDatabase();
        database.delete(SQLiteHelper.DATABASE_TABLE, SQLiteHelper.KEY_ID + "="
                + c.getId(), null);
        database.close();
    }
}
