package com.freakydevs.kolkatalocal.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.freakydevs.kolkatalocal.R;
import com.freakydevs.kolkatalocal.connection.MailSenderAsync;
import com.freakydevs.kolkatalocal.customview.MySnackBar;
import com.freakydevs.kolkatalocal.interfaces.ContactusInterface;
import com.freakydevs.kolkatalocal.utils.Internet;
import com.kaopiz.kprogresshud.KProgressHUD;

public class ContactusActivity extends AppCompatActivity implements View.OnClickListener, ContactusInterface {
    private EditText editName, editEmail, editMessage;
    private Button sendButton;
    private Context context;
    private ConstraintLayout parentLayout;
    private KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);
        context = this;
        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.text_get_in_touch));
        editName = findViewById(R.id.edit_name);
        editEmail = findViewById(R.id.edit_email);
        editMessage = findViewById(R.id.edit_message);
        sendButton = findViewById(R.id.send);
        parentLayout = findViewById(R.id.parent_layout);
        sendButton.setOnClickListener(this);
        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.text_sending))
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.6f);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (Internet.isConnected(context) && performValidation())
            new MailSenderAsync(context).execute(editName.getText().toString(), editEmail.getText().toString(), editMessage.getText().toString());
    }

    private boolean performValidation() {
        boolean isNameEmpty, isEmailEmpty, isMessageEmpty;

        isNameEmpty = editName.getText().toString().isEmpty();
        isEmailEmpty = editEmail.getText().toString().isEmpty();
        isMessageEmpty = editMessage.getText().toString().isEmpty();

        if (isNameEmpty && isEmailEmpty && isMessageEmpty) {
            MySnackBar.show(context, parentLayout, getString(R.string.text_fill_all_details));
            return false;
        } else if (isNameEmpty) {
            MySnackBar.show(context, parentLayout, getString(R.string.text_fill_name));
            return false;
        } else if (isEmailEmpty) {
            MySnackBar.show(context, parentLayout, getString(R.string.text_fill_email));
            return false;
        } else if (isMessageEmpty) {
            MySnackBar.show(context, parentLayout, getString(R.string.text_fill_message));
            return false;
        }
        return true;
    }

    @Override
    public void beforeSend() {
        hud.show();
        sendButton.setClickable(false);
    }

    @Override
    public void afterSend() {
        if (hud != null && hud.isShowing())
            hud.dismiss();
        MySnackBar.show(context, parentLayout, getString(R.string.text_message_sent));
        sendButton.setClickable(true);
        editMessage.setText("");
        editName.setText("");
        editEmail.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hud != null && hud.isShowing())
            hud.dismiss();
    }
}
