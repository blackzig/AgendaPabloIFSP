package br.edu.ifspsaocarlos.agenda.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.edu.ifspsaocarlos.agenda.R;
import br.edu.ifspsaocarlos.agenda.model.ContatoV2;
import br.edu.ifspsaocarlos.agenda.model.ContatoV3;


public class ContatoAdapterV3 extends RecyclerView.Adapter<ContatoAdapterV3.ContatoViewHolder> {

    private List<ContatoV3> contatos;
    private Context context;

    private static ItemClickListener clickListener;


    public ContatoAdapterV3(List<ContatoV3> contatos, Context context) {
        this.contatos = contatos;
        this.context = context;
    }

    @Override
    public ContatoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contato_celula, parent, false);
        return new ContatoViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ContatoViewHolder holder, int position) {
        ContatoV3 contato  = contatos.get(position) ;
        holder.nome.setText(contato.getNome());
        int isFavorite = contato.getFavorite();
        if(isFavorite==0){
            holder.favorite.setImageResource(R.drawable.icone_favorito_off);
        }else{
            holder.favorite.setImageResource(R.drawable.icone_favorito_on);
        }
    }

    @Override
    public int getItemCount() {
        return contatos.size();
    }


    public void setClickListener(ItemClickListener itemClickListener) {
        clickListener = itemClickListener;
    }


    public  class ContatoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView nome;
        final ImageView favorite;

        ContatoViewHolder(View view) {
            super(view);
            nome = (TextView)view.findViewById(R.id.nome);
            favorite = view.findViewById(R.id.contact_favorite);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (clickListener != null)
                clickListener.onItemClick(getAdapterPosition());
        }
    }


    public interface ItemClickListener {
        void onItemClick(int position);
    }

}


