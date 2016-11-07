package cgt.SQLite.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cgt.SQLite.model.Contacto;


public class ContactoOperators {
    public static final String LOGTAG = "EMP_MNGMNT_SYS";

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    private static final String[] allColumns = {
            ContactoDBHandler.COLUMN_ID,
            ContactoDBHandler.COLUMN_NAME,
            ContactoDBHandler.COLUMN_URL,
            ContactoDBHandler.COLUMN_PHONE,
            ContactoDBHandler.COLUMN_EMAIL,
            ContactoDBHandler.COLUMN_PAS,
            ContactoDBHandler.COLUMN_DEPT

    };

    public ContactoOperators(Context context){
        dbhandler = new ContactoDBHandler(context);
    }

    public void open(){
        Log.i(LOGTAG,"Database Opened");
        database = dbhandler.getWritableDatabase();


    }
    public void close(){
        Log.i(LOGTAG, "Database Closed");
        dbhandler.close();

    }
    public Contacto addContacto(Contacto Contacto){
        ContentValues values  = new ContentValues();
        values.put(ContactoDBHandler.COLUMN_NAME, Contacto.getNombre());
        values.put(ContactoDBHandler.COLUMN_URL, Contacto.getUrl());
        values.put(ContactoDBHandler.COLUMN_PHONE, Contacto.getTelefono());
        values.put(ContactoDBHandler.COLUMN_EMAIL, Contacto.getEmail());
        values.put(ContactoDBHandler.COLUMN_PAS, Contacto.getProductosYServicios());
        values.put(ContactoDBHandler.COLUMN_DEPT, Contacto.getDepartamento());
        long insertid = database.insert(ContactoDBHandler.TABLE_EMPLOYEES,null,values);
        Contacto.setId(insertid);
        return Contacto;

    }

    // Getting single Contacto
    public Contacto getContacto(long id) {
        Log.v("EL ID ES ", String.valueOf(id));
        Cursor cursor = database.query(ContactoDBHandler.TABLE_EMPLOYEES,allColumns,ContactoDBHandler.COLUMN_ID + "=?",new String[]{String.valueOf(id)},null,null, null, null);
        Log.v("EL ID ES PARTE 2 ", String.valueOf(id));
        if (cursor != null)
            cursor.moveToFirst();

        Contacto e = new Contacto(Long.parseLong(cursor.getString(0)),cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)), cursor.getString(4), cursor.getString(5), cursor.getString(6));
        // return Contacto
        return e;
    }

    public List<Contacto> getAllContactos() {

        Cursor cursor = database.query(ContactoDBHandler.TABLE_EMPLOYEES,allColumns,null,null,null, null, null);

        List<Contacto> empresas = new ArrayList<>();
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                Contacto Contacto = new Contacto();
                Contacto.setId(cursor.getLong(cursor.getColumnIndex(ContactoDBHandler.COLUMN_ID)));
                Contacto.setNombre(cursor.getString(cursor.getColumnIndex(ContactoDBHandler.COLUMN_NAME)));
                Contacto.setUrl(cursor.getString(cursor.getColumnIndex(ContactoDBHandler.COLUMN_URL)));
                Contacto.setTelefono(cursor.getInt(cursor.getColumnIndex(ContactoDBHandler.COLUMN_PHONE)));
                Contacto.setEmail(cursor.getString(cursor.getColumnIndex(ContactoDBHandler.COLUMN_EMAIL)));
                Contacto.setProductosYServicios(cursor.getString(cursor.getColumnIndex(ContactoDBHandler.COLUMN_PAS)));
                Contacto.setDepartamento(cursor.getString(cursor.getColumnIndex(ContactoDBHandler.COLUMN_DEPT)));
                empresas.add(Contacto);
            }
        }
        // return All empresas
        return empresas;
    }




    // Updating Contacto
    public int updateContacto(Contacto Contacto) {

        ContentValues values = new ContentValues();
        values.put(ContactoDBHandler.COLUMN_NAME, Contacto.getNombre());
        values.put(ContactoDBHandler.COLUMN_URL, Contacto.getUrl());
        values.put(ContactoDBHandler.COLUMN_PHONE, Contacto.getTelefono());
        values.put(ContactoDBHandler.COLUMN_EMAIL, Contacto.getEmail());
        values.put(ContactoDBHandler.COLUMN_PAS, Contacto.getProductosYServicios());
        values.put(ContactoDBHandler.COLUMN_DEPT, Contacto.getDepartamento());

        // updating row
        return database.update(ContactoDBHandler.TABLE_EMPLOYEES, values,
                ContactoDBHandler.COLUMN_ID + "=?",new String[] { String.valueOf(Contacto.getId())});
    }

    // Deleting Contacto
    public void removeContacto(Contacto Contacto) {
        Log.d("SI ENTRO", "ENTRO");
        database.delete(ContactoDBHandler.TABLE_EMPLOYEES, ContactoDBHandler.COLUMN_ID + "=" + Contacto.getId(), null);
    }

}
