package com.freakydevs.kolkatalocal.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.freakydevs.kolkatalocal.R;
import com.freakydevs.kolkatalocal.adapter.MyPagerAdapter;
import com.freakydevs.kolkatalocal.connection.DownloadManager;
import com.freakydevs.kolkatalocal.connection.Updater;
import com.freakydevs.kolkatalocal.customview.MySnackBar;
import com.freakydevs.kolkatalocal.customview.MyToast;
import com.freakydevs.kolkatalocal.enums.UpdateType;
import com.freakydevs.kolkatalocal.fragment.PnrStatusFragment;
import com.freakydevs.kolkatalocal.fragment.SearchFragment;
import com.freakydevs.kolkatalocal.fragment.TrainRouteFragment;
import com.freakydevs.kolkatalocal.interfaces.MainActivityInterface;
import com.freakydevs.kolkatalocal.resources.CustomAdListener;
import com.freakydevs.kolkatalocal.resources.MyDataBaseHandler;
import com.freakydevs.kolkatalocal.utils.Constants;
import com.freakydevs.kolkatalocal.utils.Internet;
import com.freakydevs.kolkatalocal.utils.SharedPreferenceManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.kaopiz.kprogresshud.KProgressHUD;

import net.khirr.android.privacypolicy.PrivacyPolicyDialog;

import java.io.File;
import java.util.Arrays;


/**
 * Created by PURUSHOTAM on 10/30/2017.
 */

public class MainActivity extends AppCompatActivity implements MainActivityInterface {

    private ViewPager viewPager;
    private AdView mAdView;
    private DrawerLayout drawerLayout;
    private Context context;
    private KProgressHUD hud;
    private ConstraintLayout parentLayout;
    private InterstitialAd mInterstitialAd;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initView();
        initViewPager();
        showPrivacyPolicyDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAdView();
    }

    private void runAppAndDatabaseCheck() {
        if (SharedPreferenceManager.isFirstTime(getApplicationContext())) {
            checkDatabase();
        } else if (SharedPreferenceManager.isupdateTime(getApplicationContext())) {
            new Updater(context, false, UpdateType.BOTH);
        }
    }

    private void initView() {
        viewPager = findViewById(R.id.pager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        setNavigationListener(navigationView);
        drawerLayout = findViewById(R.id.drawer);
        setDrawerListener(toolbar);
        mAdView = findViewById(R.id.adView);
        parentLayout = findViewById(R.id.parent_layout);

        viewPager.setOffscreenPageLimit(3);

        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.6f);

        Menu menu = navigationView.getMenu();
        MenuItem target = menu.findItem(R.id.remove_ad);
        target.setVisible(SharedPreferenceManager.isRemoveAd(getApplicationContext()));
    }

    private void setNavigationListener(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                switch (item.getItemId()) {

                    case R.id.nav_home:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.nav_train_route:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.nav_pnr:
                        viewPager.setCurrentItem(2);
                        return true;
                    case R.id.nav_share:
                        Intent sharing = new Intent(Intent.ACTION_SEND);
                        sharing.setType("text/plain");
                        sharing.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                        sharing.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text));
                        startActivity(Intent.createChooser(sharing, getString(R.string.share_with)));
                        return true;
                    case R.id.nav_rateus:
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(getString(R.string.app_url)));
                        startActivity(i);
                        return true;
                    case R.id.nav_contactus:
                        startActivity(new Intent(getApplicationContext(), ContactusActivity.class));
                        return true;
                    case R.id.nav_aboutus:
                        startActivity(new Intent(getApplicationContext(), AboutusActivity.class));
                        return true;
                    case R.id.nav_appupdate:
                        if (Internet.isConnected(context)) {
                            new Updater(context, true, UpdateType.APP);
                        }
                        return true;
                    case R.id.nav_databaseupdate:
                        if (Internet.isConnected(context)) {
                            new Updater(context, true, UpdateType.DATABASE);
                        }
                        return true;
                    case R.id.buy_me_cofee:
                        startActivity(new Intent(getApplicationContext(), BuymeCofee.class));
                        return true;
                    case R.id.remove_ad:
                        startActivity(new Intent(getApplicationContext(), RemoveAd.class));
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    private void setDrawerListener(Toolbar toolbar) {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void initViewPager() {
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        myPagerAdapter.addFragment(new SearchFragment(), "SearchFragment");
        myPagerAdapter.addFragment(new TrainRouteFragment(), "Train");
        myPagerAdapter.addFragment(new PnrStatusFragment(), "PNR Status");
        viewPager.setAdapter(myPagerAdapter);

    }

    private void initAdView() {
        if (SharedPreferenceManager.isShowAd(getApplicationContext())) {
            AdRequest adRequest = new AdRequest.Builder().addTestDevice("6BB916C8AE03F4AF01DC11FDAB3DA729").build();
            mAdView.setAdListener(new CustomAdListener(this, mAdView));
            mAdView.loadAd(adRequest);
            mAdView.setVisibility(View.VISIBLE);
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
            mInterstitialAd.setAdListener(new CustomAdListener(this, true));
            mInterstitialAd.loadAd(adRequest);
        } else {
            mAdView.setVisibility(View.GONE);
        }
    }

    @Override
    public void notifyDatabaseDownload() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder
                .setMessage(getString(R.string.database_download_message))
                .setCancelable(false);
        alertDialogBuilder.setPositiveButton(getString(R.string.download), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Internet.isConnected(context)) {
                    new DownloadManager(context);
                }
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void noTrainDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder
                .setMessage(getString(R.string.no_direct_train))
                .setCancelable(false);

        alertDialogBuilder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    @Override
    public void showLoader() {
        hud.setLabel(getString(R.string.loading));
        hud.show();
    }

    @Override
    public void hideLoader() {
        if (hud.isShowing()) {
            hud.dismiss();
        }
    }

    @Override
    public void showDatabaseUpdate() {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder
                .setMessage(getString(R.string.database_update_available))
                .setCancelable(false);
        alertDialogBuilder.setPositiveButton(getString(R.string.download), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Internet.isConnected(context)) {
                    new DownloadManager(context);
                }
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void showAppUpdate() {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder
                .setMessage(getString(R.string.app_update_available))
                .setCancelable(false);
        alertDialogBuilder.setPositiveButton(getString(R.string.download), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Internet.isConnected(context)) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(getString(R.string.app_url)));
                    startActivity(i);
                }
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void showUpdateLoader() {
        hud.setLabel(getString(R.string.checking));
        hud.show();
    }

    @Override
    public void hideUpdateLoader() {
        hud.dismiss();
    }

    @Override
    public void noAppUpdate() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder
                .setMessage(getString(R.string.latest_version_of_app))
                .setCancelable(false);

        alertDialogBuilder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    @Override
    public void noDbUpdate() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder
                .setMessage(getString(R.string.latest_version_of_database))
                .setCancelable(false);

        alertDialogBuilder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    @Override
    public void invalidPnr() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder
                .setMessage(getString(R.string.enter_valid_pnr))
                .setCancelable(false);

        alertDialogBuilder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    private void checkDatabase() {
        MyDataBaseHandler myDataBaseHandler = new MyDataBaseHandler(context);
        if (!myDataBaseHandler.checkDataBase()) {
            notifyDatabaseFirstTime();
        }
    }

    private void notifyDatabaseFirstTime() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder
                .setMessage(getString(R.string.download_database_first_time))
                .setCancelable(false);
        alertDialogBuilder.setPositiveButton(getString(R.string.download), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Internet.isConnected(context)) {
                    new DownloadManager(context);
                }
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            openQuitDialog();
        }
    }

    private void openQuitDialog() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();

            if (SharedPreferenceManager.isShowAd(getApplicationContext()) && mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                deleteCache(context);
            }
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        MySnackBar.show(getApplicationContext(), parentLayout, getString(R.string.press_back_again));

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else
            return dir != null && dir.isFile() && dir.delete();
    }

    private void showPrivacyPolicyDialog() {
        final PrivacyPolicyDialog privacyPolicyDialog = new PrivacyPolicyDialog(this,
                Constants.TERMS_CONDITION_URL,
                Constants.PRIVACY_POLICY_URL);

        addPoliciesToDialog(privacyPolicyDialog);
        privacyPolicyDialog.setOnClickListener(new PrivacyPolicyDialog.OnClickListener() {
            @Override
            public void onAccept(boolean isFirstTime) {
                runAppAndDatabaseCheck();
            }

            @Override
            public void onCancel() {
                MyToast.showToast(MainActivity.this, getString(R.string.accept_terms_condition));
                privacyPolicyDialog.show();
            }
        });
        privacyPolicyDialog.show();
    }

    private void addPoliciesToDialog(PrivacyPolicyDialog privacyPolicyDialog) {
        for (int policyId : Arrays.asList(
                R.string.policy_1,
                R.string.policy_2,
                R.string.policy_3,
                R.string.policy_4)) {
            privacyPolicyDialog.addPoliceLine(getString(policyId));
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hud != null && hud.isShowing())
            hud.dismiss();
    }

}
