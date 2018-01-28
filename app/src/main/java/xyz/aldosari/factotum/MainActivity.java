package xyz.aldosari.factotum;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zendesk.sdk.feedback.ui.ContactZendeskActivity;
import com.zendesk.sdk.model.access.AnonymousIdentity;
import com.zendesk.sdk.model.access.Identity;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.sdk.requests.RequestActivity;

public class MainActivity extends AppCompatActivity {

    private StringBuilder welcomeMessage;

    @Override
    protected void onResume() {
        super.onResume();
        loadAccountData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        if (id == R.id.menu_user_profile) {
            startActivity(new Intent(this, EmailPasswordActivity.class));
//            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openTicket(View view) {
        if (welcomeMessage.toString().equals(getString(R.string.please_login)) || welcomeMessage.toString().isEmpty()) {
            Toast toast = Toast.makeText(this, R.string.please_register, Toast.LENGTH_LONG);
            toast.show();
            startActivity(new Intent(this, EmailPasswordActivity.class));

        } else {
            startActivity(new Intent(MainActivity.this, ContactZendeskActivity.class));
        }
    }

    public void checkTicket(View view) {
        if (welcomeMessage.toString().equals(getString(R.string.please_login)) || welcomeMessage.toString().isEmpty()) {
            Toast toast = Toast.makeText(this, R.string.please_register, Toast.LENGTH_LONG);
            toast.show();
            startActivity(new Intent(this, EmailPasswordActivity.class));

        } else {
            startActivity(new Intent(MainActivity.this, RequestActivity.class));
        }
    }

    private void loadAccountData() {
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName() + Constants.PREF_FILE_NAME, Context.MODE_PRIVATE);
        String name = sharedPreferences.getString(Constants.KEY_NAME, "");
        welcomeMessage = new StringBuilder("");

        if (name.equals(getText(R.string.signed_out)) || name.isEmpty())
        {
            welcomeMessage.append(getString(R.string.please_login));

        } else {
            welcomeMessage.append(getString(R.string.hello)).append(name);
            connectZen(name);

        }
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(welcomeMessage);
    }

    private void connectZen(String userEmail) {
        ZendeskConfig.INSTANCE.init(this, getString(R.string.zendeskUrl), getString(R.string.applicationId), getString(R.string.oauthClientId));
        Identity anonymousIdentity = new AnonymousIdentity.Builder()
                .withEmailIdentifier(userEmail)
                .build();
        ZendeskConfig.INSTANCE.setIdentity(anonymousIdentity);
    }

}
