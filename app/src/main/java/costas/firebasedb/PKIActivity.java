package costas.firebasedb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PKIActivity extends AppCompatActivity {

    String pki;

    EditText pkiInput;

    Button pkiSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pki);

        pkiInput = (EditText) findViewById(R.id.pkiInput);

        pkiSubmit = (Button) findViewById(R.id.pkiSubmit);
        pkiSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pki = pkiInput.getText().toString();

                showToast(pki);
            }
        });
    }

    private void showToast(String text) {
        Toast.makeText(PKIActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}
