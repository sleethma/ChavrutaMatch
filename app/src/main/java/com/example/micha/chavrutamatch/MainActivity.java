package com.example.micha.chavrutamatch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.micha.chavrutamatch.AcctLogin.LoginActivity;
import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.DI.Components.ApplicationComponent;
import com.example.micha.chavrutamatch.DI.Components.DaggerMAComponent;
import com.example.micha.chavrutamatch.DI.Components.MAComponent;
import com.example.micha.chavrutamatch.DI.Modules.MAModule;
import com.example.micha.chavrutamatch.Data.AvatarImgs;
import com.example.micha.chavrutamatch.Data.HostSessionData;
import com.example.micha.chavrutamatch.Data.Http.APIModels.MyChavrutas;
import com.example.micha.chavrutamatch.Data.Http.APIModels.ServerResponse;
import com.example.micha.chavrutamatch.Data.Http.MyChavrutaAPI;
import com.example.micha.chavrutamatch.Data.ServerConnect;
import com.example.micha.chavrutamatch.MVPConstructs.MAContractMVP;
import com.example.micha.chavrutamatch.Utils.ConnCheckUtil;
import com.example.micha.chavrutamatch.Utils.GlideApp;
import com.example.micha.chavrutamatch.Utils.RecyclerViewListDecor;
import com.facebook.accountkit.AccountKit;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.micha.chavrutamatch.AcctLogin.UserDetails.getUserCustomAvatarBase64ByteArray;

public class MainActivity extends AppCompatActivity implements OpenChavrutaAdapter.ParentView, MAContractMVP.View {
    @BindView(R.id.iv_no_match_add_match)
    ImageView noMatchView;
    @BindView(R.id.lv_my_chavruta)
    RecyclerView myChavrutaListView;
    @BindView(R.id.iv_user_avatar)
    ImageView userAvatar;
    @BindView(R.id.tv_my_chavruta_label)
    TextView myChavrutaLabel;
    @BindView(R.id.v_underline_toolbar)
    View underlineToolbar;

    @Inject
    UserDetails userDetailsInstance;

    @Inject
    MAContractMVP.Presenter presenter;

    OpenChavrutaAdapter mAdapter;

    static ArrayList<HostSessionData> myChavrutasArrayList;
    public static Context mContext;
    private static String jsonString;

    private final int VERTICAL_LIST_ITEM_SPACE = 40;
    // indicates user custom avatar used
    private final String CUSTOM_AVATAR = "999";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;

//        ApplicationComponent appComponent = ((ChavrutaMatch.get(this)).getApplicationComponent());

        (ChavrutaMatch.get(this)).getMAComponent().inject(this);


        if (ConnCheckUtil.isConnected(mContext)) {
            presenter.activateAccountKit();
            //check if already logged in
            if (presenter.isCurrentUserLoggedInToAccountKit()) {
                presenter.getJsonChavrutaString();
            } else {
                AccountKit.logOut();
                launchLoginActivity();
            }

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(view -> loadOnSelectActivity(view));

            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                // COMPLETED (4) Override onMove and simply return false inside
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    //do nothing, we only swipe needed
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                    // Inside, get the viewHolder's itemView's tag and store in a long variable id
                    //get the id of the item being swiped
                    int id = (int) viewHolder.itemView.getTag();
                    int currentItemViewType = viewHolder.getItemViewType();
                    notifyUserBeforeDelete(id, currentItemViewType);

                }
            }).attachToRecyclerView(myChavrutaListView);
        } else {
            alertUserToCheckConn();
        }
        userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadProfile();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setMAView(this);
        presenter.setupToolbar();
    }

    @Override
    public void setOptionsMenu() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh_list) {
            refreshMainActivity();
            return true;
        }
        //My Profile
        if (id == R.id.my_profile) {
            // Access addBio for profile edit
            Intent intent = new Intent(this, AddBio.class);
            Boolean updateBio = true;
            intent.putExtra("update_bio", updateBio);
            startActivity(intent);
            return true;
        }
        //logout
        if (id == R.id.logout) {
            // logout of Account Kit
            AccountKit.logOut();
            launchLoginActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void loadProfile() {
        Intent intent = new Intent(this, AddBio.class);
        Boolean updateBio = true;
        intent.putExtra("update_bio", updateBio);
        startActivity(intent);
    }

    //verify on exiting app
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit App?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    System.exit(0);
                }).create().show();
    }

    public void notifyUserBeforeDelete(final int indexToDelete, final int viewTypeToDelete) {
        String title;
        String message;
        if (viewTypeToDelete == 0) {
            title = "Unregister For Class?";
            message = "Let Class Host Know You Cannot Attend?";
        } else {
            title = "Delete Class?";
            message = "Are you sure you want to delete class?";
        }

        new AlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        refreshMainActivity();
                    }
                })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        mAdapter.deleteMyChavrutaArrayItemOnSwipe(indexToDelete, viewTypeToDelete);
//                        myChavrutasArrayList.remove(indexToDelete);
                        refreshMainActivity();
                    }
                }).create().show();
    }


    private void alertUserToCheckConn() {
        new AlertDialog.Builder(mContext)
                .setTitle("Please check internet connection")
                .setMessage("Retry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        refreshMainActivity();
                        //internet conn not connected
                        ProgressDialog pDialog;
                        pDialog = new ProgressDialog(mContext);
                        pDialog.setMessage("Checking Connection. Please wait...");
                        pDialog.setCancelable(false);
                        pDialog.show();
                    }
                }).create().show();
    }

    public void refreshMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void loadOnSelectActivity(View view) {
        Intent intent = new Intent(MainActivity.this, AddSelect.class);
        startActivity(intent);
    }


    @Override
    public void getParentView() {
        Snackbar.make(myChavrutaListView, "Swipe To Unregister Class", Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void sendToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setToolbarUnderline() {
        //sets up UI specific to SDK
        if (Build.VERSION.SDK_INT >= 21) underlineToolbar.setVisibility(View.GONE);
    }

    @Override
    public void setUserAvatar() {
        //sets user avatar. @UserAvatarNumberString = "999" indicates avatar is user photo
        if (userDetailsInstance.getmUserAvatarNumberString() != null &&
                !userDetailsInstance.getmUserAvatarNumberString().equals("999")) {
            userAvatar.setImageResource(AvatarImgs.getAvatarNumberResId(
                    Integer.parseInt(userDetailsInstance.getmUserAvatarNumberString())));
        } else {
            try {
                if (UserDetails.getHostAvatarUri() != null) {
                    GlideApp
                            .with(mContext)
                            .load(UserDetails.getHostAvatarUri())
                            .placeholder(R.drawable.ic_unknown_user)
                            .circleCrop()
                            .into(userAvatar);
                } else if (UserDetails.getUserCustomAvatarBase64ByteArray() != null) {
                    GlideApp
                            .with(mContext)
                            .load(UserDetails.getUserCustomAvatarBase64ByteArray())
                            .placeholder(R.drawable.ic_unknown_user)
                            .into(userAvatar);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setMyChavrutaAdapter(ArrayList<HostSessionData> myChavrutasArrayList) {
        myChavrutaListView.requestLayout();
        //attaches data source to adapter and displays list
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        myChavrutaListView.setLayoutManager(linearLayoutManager);

        //add ItemDecoration
        myChavrutaListView.addItemDecoration(new RecyclerViewListDecor(VERTICAL_LIST_ITEM_SPACE));
        myChavrutaListView.setHasFixedSize(true);
        mAdapter = new OpenChavrutaAdapter(mContext, myChavrutasArrayList, presenter, userDetailsInstance);
        myChavrutaListView.setAdapter(mAdapter);
    }

    @Override
    public void displayRecyclerView() {
        myChavrutaListView.setVisibility(View.VISIBLE);
        noMatchView.setVisibility(View.GONE);
    }

    @Override
    public void sendHostsConfirmationtoDb(String chavrutaId, String requesterId) {
        String confirmedChavrutaKey = "confirmChavrutaRequest";
        ServerConnect confirmInDb = new ServerConnect(mContext, userDetailsInstance);
        confirmInDb.execute(confirmedChavrutaKey, chavrutaId, requesterId);
    }
}
