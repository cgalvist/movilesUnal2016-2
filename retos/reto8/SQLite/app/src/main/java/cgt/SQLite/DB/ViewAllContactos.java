package cgt.SQLite.DB;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import cgt.SQLite.R;
import cgt.SQLite.model.Contacto;

import java.util.List;

public class ViewAllContactos extends ListActivity{
    private ContactoOperators employeeOps;
    List<Contacto> employees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_employee);
        employeeOps = new ContactoOperators(this);
        employeeOps.open();
        employees = employeeOps.getAllContactos();
        employeeOps.close();
        ArrayAdapter<Contacto> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, employees);
        setListAdapter(adapter);
    }
}
