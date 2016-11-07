package cgt.SQLite.DB;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import cgt.SQLite.DatePickerFragment;
import cgt.SQLite.MainActivity;
import cgt.SQLite.R;
import cgt.SQLite.model.Contacto;

import java.text.SimpleDateFormat;
import java.util.Date;
public class AddUpdateContacto extends AppCompatActivity implements DatePickerFragment.DateDialogListener{

    private static final String EXTRA_EMP_ID = "com.androidtutorialpoint.empId";
    private static final String EXTRA_ADD_UPDATE = "com.androidtutorialpoint.add_update";
    private static final String DIALOG_DATE = "DialogDate";
    //private ImageView calendarImage;
    private RadioGroup radioGroup;
    private RadioButton maleRadioButton,femaleRadioButton;
    private EditText nameEditText;
    private EditText urlEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText pasEditText;
    private EditText deptEditText;
    //private EditText hireDateEditText;
    private Button addUpdateButton;
    private Contacto newContacto;
    private Contacto oldContacto;
    private String mode;
    private long empId;
    private ContactoOperators ContactoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_employee);
        newContacto = new Contacto();
        oldContacto = new Contacto();
        nameEditText = (EditText)findViewById(R.id.edit_text_name);
        urlEditText = (EditText)findViewById(R.id.edit_text_url);
        phoneEditText = (EditText)findViewById(R.id.edit_text_phone);
        emailEditText = (EditText)findViewById(R.id.edit_text_email);
        pasEditText = (EditText)findViewById(R.id.edit_text_pas);        
        deptEditText = (EditText)findViewById(R.id.edit_text_dept);
        addUpdateButton = (Button)findViewById(R.id.button_add_update_employee);
        ContactoData = new ContactoOperators(this);
        ContactoData.open();


        mode = getIntent().getStringExtra(EXTRA_ADD_UPDATE);
        if(mode.equals("Update")){

            addUpdateButton.setText("Actualizar Contacto");
            empId = getIntent().getLongExtra(EXTRA_EMP_ID,0);
            Log.d("EL ID ES", String.valueOf(empId));
            initializeContacto(empId);

        }


        addUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mode.equals("Add")) {
                    newContacto.setNombre(nameEditText.getText().toString());
                    newContacto.setUrl(urlEditText.getText().toString());
                    newContacto.setTelefono(Integer.parseInt(phoneEditText.getText().toString()));
                    newContacto.setEmail(emailEditText.getText().toString());
                    newContacto.setProductosYServicios(pasEditText.getText().toString());
                    newContacto.setDepartamento(deptEditText.getText().toString());
                    ContactoData.addContacto(newContacto);
                    Toast t = Toast.makeText(AddUpdateContacto.this, "El contacto "+ newContacto.getNombre() + " ha sido agregado exitosamente", Toast.LENGTH_SHORT);
                    t.show();
                    Intent i = new Intent(AddUpdateContacto.this,MainActivity.class);
                    startActivity(i);
                }else {
                    oldContacto.setNombre(nameEditText.getText().toString());
                    oldContacto.setUrl(urlEditText.getText().toString());
                    oldContacto.setTelefono(Integer.parseInt(phoneEditText.getText().toString()));
                    oldContacto.setEmail(emailEditText.getText().toString());
                    oldContacto.setProductosYServicios(pasEditText.getText().toString());
                    oldContacto.setDepartamento(deptEditText.getText().toString());
                    ContactoData.updateContacto(oldContacto);
                    Toast t = Toast.makeText(AddUpdateContacto.this, "El contacto "+ oldContacto.getNombre() + " ha sido actualizado exitosamente", Toast.LENGTH_SHORT);
                    t.show();
                    Intent i = new Intent(AddUpdateContacto.this,MainActivity.class);
                    startActivity(i);

                }


            }
        });


    }

    private void initializeContacto(long empId) {
        oldContacto = ContactoData.getContacto(empId);
        nameEditText.setText(oldContacto.getNombre());
        urlEditText.setText(oldContacto.getUrl());
        phoneEditText.setText(Integer.toString(oldContacto.getTelefono()));
        emailEditText.setText(oldContacto.getEmail());
        pasEditText.setText(oldContacto.getProductosYServicios());
        deptEditText.setText(oldContacto.getDepartamento());
    }


    @Override
    public void onFinishDialog(Date date) {
        //hireDateEditText.setText(formatDate(date));

    }

    public String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String hireDate = sdf.format(date);
        return hireDate;
    }
}
