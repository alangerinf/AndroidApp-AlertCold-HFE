package ibao.alanger.alertcoldhfe;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import ibao.alanger.alertcoldhfe.model.SharedPreferencesManager;


public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FragmentListBatch.OnFragmentInteractionListener{

    private Fragment myFragment = null;
    public static final String TAG = ActivityMain.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myFragment = new FragmentListBatch();


        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, myFragment).commit();

        //verificar y estan actualizar de inmediato

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        Menu menu = navigationView.getMenu();
        ((TextView) headerView.findViewById(R.id.tViewUserName)).setText("Hola "+SharedPreferencesManager.getUser(this).getName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id == R.id.version) {
            try {
                Toast.makeText(getBaseContext(),"Versión "+BuildConfig.VERSION_NAME+" code."+BuildConfig.VERSION_CODE,Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getBaseContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        }
        if (id == R.id.logout) {

            showPopupRecomendacion("¿Desea Cerrar Sesión?");



            //return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void showPopupRecomendacion(String mensaje){
        Dialog dialogClose;
        dialogClose = new Dialog(this);
        dialogClose.setContentView(R.layout.dialog_recomendaciones);
        Button btnDialogClose = (Button) dialogClose.findViewById(R.id.buton_close_dialog);
        ImageView iViewDialogClose = (ImageView) dialogClose.findViewById(R.id.iViewDialogClose);
        TextView tViewRecomendacion = dialogClose.findViewById(R.id.tViewRecomendacion);
        tViewRecomendacion.setText(mensaje);
        iViewDialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogClose.dismiss();
            }
        });
        btnDialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getBaseContext(), "Cerrando Sesión...", Toast.LENGTH_LONG).show();
                //new LoginDataDAO(getBaseContext()).borrarTable();
                SharedPreferencesManager.deleteUser(getBaseContext());
                Intent intent = new Intent(getBaseContext(), ActivityPreloader.class);
                startActivity(intent);
                dialogClose.dismiss();
            }
        });

        dialogClose.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogClose.show();
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        boolean isFrame = false;
        Intent i = null;

        if (id == R.id.iDrawer_listaBatchs) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, myFragment).commit();
            //comprobar internet
        }
        if (id == R.id.iDrawer_MonitoreoTemp) {
            //comprobar internet
            i  = new Intent(this,WebActivity.class);
            startActivity(i);
        }
        if (id == R.id.iDrawer_AddBatch) {
            //comprobar internet
            i  = new Intent(this, EditBasicBash.class);
            startActivity(i);
        }
        if (id == R.id.iDrawer_SearchPallet) {
            //comprobar internet
            i  = new Intent(this, SearchPalletActivity.class);
            startActivity(i);

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
