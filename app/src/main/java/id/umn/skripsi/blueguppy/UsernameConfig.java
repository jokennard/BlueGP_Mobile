package id.umn.skripsi.blueguppy;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UsernameConfig extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username_config);

        final SharedPreferences pref = getSharedPreferences("SKRIPSI", 0);
        final EditText usernameET = (EditText) findViewById(R.id.editText_username);
        Button usernameSaveBtn = (Button) findViewById(R.id.button_usernameSave);
        TextView usernameCTV = (TextView) findViewById(R.id.textView_usernameCensored);

        usernameCTV.setText(String.format("%s●●●●●", pref.getString("USERNAME", "dummy").substring(0, 1)));

        usernameSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("USERNAME", usernameET.getText().toString());
                editor.apply();
                finish();
            }
        });

    }
}
