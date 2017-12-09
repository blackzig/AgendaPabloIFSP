package br.edu.ifspsaocarlos.agenda.message;

import android.content.Context;
import android.widget.Toast;

import br.edu.ifspsaocarlos.agenda.activity.VersoesActivity;

/**
 * Created by zigui on 06/12/2017.
 */

public class Message {

    public void ShortMessage(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void LongMessage(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
