package br.edu.ifspsaocarlos.agenda.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.agenda.R;
import br.edu.ifspsaocarlos.agenda.activityV2.DetalheV2Activity;
import br.edu.ifspsaocarlos.agenda.activityV3.DetalheV3Activity;
import br.edu.ifspsaocarlos.agenda.adapter.ContatoAdapter;
import br.edu.ifspsaocarlos.agenda.adapter.ContatoAdapterV2;
import br.edu.ifspsaocarlos.agenda.adapter.ContatoAdapterV3;
import br.edu.ifspsaocarlos.agenda.data.ContatoDAO;
import br.edu.ifspsaocarlos.agenda.model.Contato;
import br.edu.ifspsaocarlos.agenda.model.ContatoV2;
import br.edu.ifspsaocarlos.agenda.model.ContatoV3;


public class MainActivity extends AppCompatActivity{

    private ContatoDAO cDAO ;
    private RecyclerView recyclerView;

    private List<Contato> contatos = new ArrayList<>();
    private List<ContatoV2> contatosV2 = new ArrayList<>();
    private List<ContatoV3> contatosV3 = new ArrayList<>();

    private TextView empty;

    private ContatoAdapter adapter;
    private ContatoAdapterV2 adapterV2;
    private ContatoAdapterV3 adapterV3;

    private SearchView searchView;

    private FloatingActionButton fab;

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.onActionViewCollapsed();
            updateUI(null);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchView.clearFocus();
            updateUI(query);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        handleIntent(intent);

        cDAO= new ContatoDAO(this);

        empty= (TextView) findViewById(R.id.empty_view);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("Versoes", 0);
                String versao = prefs.getString("versao", null);
                Intent i = null;
                if(versao.equals("1")){
                    i = new Intent(getApplicationContext(), DetalheActivity.class);
                }else if(versao.equals("2")){
                    i = new Intent(getApplicationContext(), DetalheV2Activity.class);
                }else if(versao.equals("3")){
                    i = new Intent(getApplicationContext(), DetalheV3Activity.class);
                }
                startActivityForResult(i, 1);
            }
        });

        updateUI(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SharedPreferences prefs = getSharedPreferences("Versoes", 0);
        String versao = prefs.getString("versao", null);

        getMenuInflater().inflate(R.menu.menu_main, menu);
        int version = Integer.parseInt(versao);

        MenuItem jf = menu.findItem(R.id.just_favorites);
        MenuItem ac = menu.findItem(R.id.all_contacts);

        if(version==1){
            jf.setVisible(false);
            ac.setVisible(false);
        }else{
            jf.setVisible(true);
            ac.setVisible(true);
        }

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.pesqContato).getActionView();

        ImageView closeButton = (ImageView)searchView.findViewById(R.id.search_close_btn);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText)findViewById(R.id.search_src_text);
                if (et.getText().toString().isEmpty())
                    searchView.onActionViewCollapsed();

                searchView.setQuery("", false);
                updateUI(null);
            }
        });

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setIconifiedByDefault(true);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        SharedPreferences prefs = getSharedPreferences("Versoes", 0);
        String versao = prefs.getString("versao", null);

        switch (item.getItemId()){
            case R.id.versoes:
                startActivity(new Intent(this, VersoesActivity.class));
                break;
            case R.id.just_favorites:
                if(versao.equals("2")){
                    contatosV2.clear();
                    adapterV2 = new ContatoAdapterV2(contatosV2, this);
                    recyclerView.setAdapter(adapterV2);
                    setupRecyclerViewV2();

                    contatosV2.addAll(cDAO.returnJustFavoritesContacts());
                    empty.setText(getResources().getString(R.string.contato_nao_encontrado));
                    fab.setVisibility(View.VISIBLE);
                }else if(versao.equals("3")){
                    contatosV3.clear();
                    adapterV3 = new ContatoAdapterV3(contatosV3, this);
                    recyclerView.setAdapter(adapterV3);
                    setupRecyclerViewV3();

                    contatosV3.addAll(cDAO.returnJustFavoritesContactsV3());
                    empty.setText(getResources().getString(R.string.contato_nao_encontrado));
                    fab.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.all_contacts:
                searchAllContacts(null, versao);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1)
            if (resultCode == RESULT_OK) {
                showSnackBar(getResources().getString(R.string.contato_adicionado));
                updateUI(null);
            }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK)
                showSnackBar(getResources().getString(R.string.contato_alterado));
            if (resultCode == 3)
                showSnackBar(getResources().getString(R.string.contato_apagado));

            updateUI(null);
        }
    }

    private void showSnackBar(String msg) {
        CoordinatorLayout coordinatorlayout= (CoordinatorLayout)findViewById(R.id.coordlayout);
        Snackbar.make(coordinatorlayout, msg,
                Snackbar.LENGTH_LONG)
                .show();
    }

    private void updateUI(String nomeContato){
        //Na primeira vez que o sistema é instalado deve ter o SharedPreferences aqui, senão gera uma exceção
        SharedPreferences prefs = getSharedPreferences("Versoes", 0);
        String versao = prefs.getString("versao", null);

        searchAllContacts(nomeContato, versao);

        recyclerView.getAdapter().notifyDataSetChanged();

        if (recyclerView.getAdapter().getItemCount()==0)
            empty.setVisibility(View.VISIBLE);
        else
            empty.setVisibility(View.GONE);

    }

    private void searchAllContacts(String nomeContato, String versao){
        if(versao.equals("1")){
            contatos.clear();
            adapter = new ContatoAdapter(contatos, this);
            recyclerView.setAdapter(adapter);
            setupRecyclerView();

            if (nomeContato==null) {
                contatos.addAll(cDAO.<Contato>buscaTodosContatos());
                empty.setText(getResources().getString(R.string.lista_vazia));
                fab.setVisibility(View.VISIBLE);
            }
            else {
                contatos.addAll(cDAO.<Contato>buscaContato(nomeContato));
                empty.setText(getResources().getString(R.string.contato_nao_encontrado));
                fab.setVisibility(View.GONE);
            }
        }else if(versao.equals("2")){
            contatosV2.clear();
            adapterV2 = new ContatoAdapterV2(contatosV2, this);
            recyclerView.setAdapter(adapterV2);
            setupRecyclerViewV2();

            if (nomeContato==null) {
                contatosV2.addAll(cDAO.<ContatoV2>buscaTodosContatos());
                empty.setText(getResources().getString(R.string.lista_vazia));
                fab.setVisibility(View.VISIBLE);
            }
            else {
                contatosV2.addAll(cDAO.<ContatoV2>buscaContato(nomeContato));
                empty.setText(getResources().getString(R.string.contato_nao_encontrado));
                fab.setVisibility(View.GONE);
            }
        }else if(versao.equals("3")){
            contatosV3.clear();
            adapterV3 = new ContatoAdapterV3(contatosV3, this);
            recyclerView.setAdapter(adapterV3);
            setupRecyclerViewV3();

            if (nomeContato==null) {
                contatosV3.addAll(cDAO.<ContatoV3>buscaTodosContatos());
                empty.setText(getResources().getString(R.string.lista_vazia));
                fab.setVisibility(View.VISIBLE);
            }
            else {
                contatosV3.addAll(cDAO.<ContatoV3>buscaContato(nomeContato));
                empty.setText(getResources().getString(R.string.contato_nao_encontrado));
                fab.setVisibility(View.GONE);
            }
        }
    }

    private void setupRecyclerView() {

        adapter.setClickListener(new ContatoAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                final Contato contato = contatos.get(position);
                Intent i = new Intent(getApplicationContext(), DetalheActivity.class);
                i.putExtra("contato", contato);
                startActivityForResult(i, 2);
            }
        });


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                if (swipeDir == ItemTouchHelper.RIGHT) {
                    Contato contato = contatos.get(viewHolder.getAdapterPosition());
                    cDAO.apagaContato(contato);
                    contatos.remove(viewHolder.getAdapterPosition());
                    recyclerView.getAdapter().notifyDataSetChanged();
                    showSnackBar(getResources().getString(R.string.contato_apagado));
                    updateUI(null);
                }
            }


            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                Paint p = new Paint();
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(ContextCompat.getColor(getBaseContext(), R.color.colorDelete));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_remove);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setupRecyclerViewV2() {

        adapterV2.setClickListener(new ContatoAdapterV2.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                final ContatoV2 contato = contatosV2.get(position);
                Intent i = new Intent(getApplicationContext(), DetalheV2Activity.class);
                i.putExtra("contato", contato);
                startActivityForResult(i, 2);
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                if (swipeDir == ItemTouchHelper.RIGHT) {
                    ContatoV2 contato = contatosV2.get(viewHolder.getAdapterPosition());
                    cDAO.apagaContato(contato);
                    contatos.remove(viewHolder.getAdapterPosition());
                    recyclerView.getAdapter().notifyDataSetChanged();
                    showSnackBar(getResources().getString(R.string.contato_apagado));
                    updateUI(null);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                Paint p = new Paint();
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(ContextCompat.getColor(getBaseContext(), R.color.colorDelete));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_remove);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setupRecyclerViewV3() {

        adapterV3.setClickListener(new ContatoAdapterV3.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                final ContatoV3 contato = contatosV3.get(position);
                Intent i = new Intent(getApplicationContext(), DetalheV3Activity.class);
                i.putExtra("contato", contato);
                startActivityForResult(i, 2);
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                if (swipeDir == ItemTouchHelper.RIGHT) {
                    ContatoV3 contato = contatosV3.get(viewHolder.getAdapterPosition());
                    cDAO.apagaContato(contato);
                    contatos.remove(viewHolder.getAdapterPosition());
                    recyclerView.getAdapter().notifyDataSetChanged();
                    showSnackBar(getResources().getString(R.string.contato_apagado));
                    updateUI(null);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                Paint p = new Paint();
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(ContextCompat.getColor(getBaseContext(), R.color.colorDelete));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_remove);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
