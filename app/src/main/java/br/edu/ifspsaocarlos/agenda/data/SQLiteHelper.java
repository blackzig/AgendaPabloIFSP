package br.edu.ifspsaocarlos.agenda.data;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Switch;

import br.edu.ifspsaocarlos.agenda.message.Message;

class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "agenda.db";
    static final String DATABASE_TABLE = "contatos";
    static final String KEY_ID = "id";
    static final String KEY_NAME = "nome";
    static final String KEY_FONE = "fone";
    static final String KEY_EMAIL = "email";
    static final String KEY_FAVORITE = "favorite";
    private static int DATABASE_VERSION = 1;
    private static String DATABASE_CREATE = "";
   /* private static final String DATABASE_CREATE = "CREATE TABLE "+ DATABASE_TABLE +" (" +
            KEY_ID  +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_NAME + " TEXT NOT NULL, " +
            KEY_FONE + " TEXT, "  +
            KEY_EMAIL + " TEXT);";*/

    private Context appContext;

    String versao = "";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i("onCreate","onCreate");
        this.appContext = context;
        SharedPreferences prefs = appContext.getSharedPreferences("Versoes", 0);
        versao = prefs.getString("versao", null);
        changeVersion();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.i("Michel","Michel");
       /* if(versao=="" || versao==null){
            justFirstTime();
        }*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int    newVersion) {
        Log.i("onUpgrade - oldVersion>", String.valueOf(oldVersion));
        Log.i("onUpgrade - newVersion>", String.valueOf(newVersion));
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       // super.onDowngrade(db, oldVersion, newVersion);
        Log.i("onDown - oldVersion>", String.valueOf(oldVersion));
        Log.i("onDown - newVersion>", String.valueOf(newVersion));
    }

    private void justFirstTime(){
        SQLiteDatabase db = getWritableDatabase();
        DATABASE_CREATE = "CREATE TABLE "+ DATABASE_TABLE +" (" +
                KEY_ID  +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_NAME + " TEXT NOT NULL, " +
                KEY_FONE + " TEXT, "  +
                KEY_EMAIL + " TEXT);";
        db.execSQL(DATABASE_CREATE);

        SharedPreferences prefs = appContext.getSharedPreferences("Versoes",0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("versao", String.valueOf(DATABASE_VERSION));
        editor.apply();
    }

    private void changeVersion(){
        try{
            //Na primeira vez que executar o sistema cair na execeção e chamar o método justFirstTime()
        DATABASE_VERSION =Integer.parseInt(versao);
        Log.i("DATABASE_VERSION>>>>>>",String.valueOf(DATABASE_VERSION));
        }catch(Exception e){
            Log.i("changeVersion","funciona?");
            justFirstTime();
        }
        /*
        try e catch para evitar o fechamento da aplicação, porque ele tenta por exemplo adicionar
        toda a vez a column favorite se eu estiver utilizando a versão 2.
         */
        try{
            switch (DATABASE_VERSION){
                case 1:
                   SQLiteDatabase db = getWritableDatabase();
                    String sql = "";
                  //  onDowngrade(db, 1, 1);
                /*    sql = "DROP TABLE IF EXISTS "+DATABASE_TABLE;
                    db.execSQL(sql);
                    onCreate(db);*/
                    break;
                case 2:
                    Log.i("UPDATE>>>","Vá para a versão 2");
                    db = getWritableDatabase();
                    sql = "ALTER TABLE "+DATABASE_TABLE+" ADD COLUMN favorite INTEGER DEFAULT 0";
                    db.execSQL(sql); // indo para versao 2
                    break;
                default:
                    Message m = new Message();
                    m.LongMessage(appContext, "Você está utilizando a versão "+ DATABASE_VERSION);
            }
        }catch(Exception e){

        }
    }
}

