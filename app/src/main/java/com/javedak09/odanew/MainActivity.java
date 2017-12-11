package com.javedak09.odanew;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends Activity {

    static String TAG = "";

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    String DirectoryName;

    @BindView(R.id.username)
    EditText username;

    @BindView(R.id.passwd)
    EditText passwd;

    DatabaseContext db;
    UserEntity tbl;

    List<UserEntity> usr;

    int i;
    int a;
    String[] arr1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        i = 0;

        db = db.getAppDatabase(this);

        dbBackup();
    }

    @OnClick(R.id.btn_Continue)
    void SaveData() {

        if (ValidateForm()) {
            //db.daoAccess().insertRecord(tbl);
        }
    }


    private boolean ValidateForm() {

        int rdoCount = 0;
        int chkCount = 0;
        boolean isexists = false;
        View v = null;

        ArrayList<Boolean> arrChk = null;

        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.luvmain);

        for (int i = 0, count = viewGroup.getChildCount(); i < count; ++i) {
            View view = viewGroup.getChildAt(i);

            isexists = false;

            if (view instanceof EditText) {
                if (((EditText) view).getText().toString().isEmpty() && ((EditText) view).getVisibility() == View.VISIBLE) {

                    Toast.makeText(this, ((EditText) view).getResources().getResourceEntryName(view.getId()) + " cannot be left blank", Toast.LENGTH_LONG).show();
                    ((EditText) view).requestFocus();

                    return false;
                }
            } else if (view instanceof RadioGroup) {

                rdoCount = ((RadioGroup) view).getChildCount();

                for (int a = 0; a < rdoCount; a++) {

                    View o = ((RadioGroup) view).getChildAt(a);
                    int result = ((RadioGroup) view).getCheckedRadioButtonId();

                    if (o instanceof RadioButton && o.getVisibility() == View.VISIBLE) {
                        //if (!((RadioButton) o).isChecked()) {

                        if (result == -1) {
                            Toast.makeText(this, ((RadioGroup) view).getResources().getResourceEntryName(view.getId()) + " cannot be left blank", Toast.LENGTH_LONG).show();
                            ((RadioButton) o).setError("This data is required");
                            ((RadioButton) o).setFocusable(true);
                            ((RadioButton) o).setFocusableInTouchMode(true);
                            ((RadioButton) o).requestFocus();
                            Log.i(TAG, ((RadioGroup) view).getResources().getResourceEntryName(view.getId()) + ": This Data is Required!");
                            return false;
                        } else {
                            ((RadioButton) o).setError(null);
                        }

                        //}
                    }
                }

            } else if (view instanceof ViewGroup) {

                chkCount = ((ViewGroup) view).getChildCount();

                for (int b = 0; b < chkCount; b++) {

                    v = ((ViewGroup) view).getChildAt(b);

                    if (v instanceof CheckBox) {
                        if (((CheckBox) v).isChecked()) {
                            isexists = true;
                            break;
                        }
                    }
                }

                if (v instanceof CheckBox) {
                    if (!isexists)
                        Toast.makeText(this, ((CheckBox) v).getResources().getResourceEntryName(view.getId()) + " cannot be left blank", Toast.LENGTH_LONG).show();
                }


                //ArrayList<RadioButton> listOfRadioButtons = new ArrayList<RadioButton>();

                /*for (int a = 0; a < rdoCount; a++) {

                    View o = ((RadioGroup) view).getChildAt(a);
                    int result = ((RadioGroup) view).getCheckedRadioButtonId();

                    if (o instanceof CheckBox) {
                        //if (!((RadioButton) o).isChecked()) {

                        if (result == -1) {
                            Toast.makeText(this, ((RadioGroup) view).getResources().getResourceName(view.getId()) + " cannot be left blank", Toast.LENGTH_LONG).show();
                            ((RadioButton) o).setError("This data is required");
                            ((RadioButton) o).setFocusable(true);
                            ((RadioButton) o).setFocusableInTouchMode(true);
                            ((RadioButton) o).requestFocus();
                            Log.i(TAG, ((RadioGroup) view).getResources().getResourceName(view.getId()) + ": This Data is Required!");
                            return false;
                        } else {
                            ((RadioButton) o).setError(null);
                        }

                        //}
                    }
                }*/

            }

        }

        return true;
    }


    @OnClick(R.id.btnDB)
    void ShowData() {
        Intent intent = new Intent(getApplicationContext(), AndroidDatabaseManager.class);
        startActivity(intent);
    }

    @OnClick(R.id.btngetData)
    void getData() {
        usr = db.daoAccess().fetchAllUsers();
        a = usr.size();
        i = 0;
    }


    @OnClick(R.id.btnPrevious)
    void movePrevious() {

        if (usr.size() <= 0 || i >= usr.size()) {
            Toast.makeText(this, "You are at 1st / last record", Toast.LENGTH_LONG).show();
            a = usr.size();
            i = 0;
        } else {
            username.setText(usr.get(i).getUserName());
            passwd.setText(usr.get(i).getPasswd());
            i++;
            a--;
        }
    }

    @OnClick(R.id.btnDBBackup)
    void DBBackup() {

        editor.putBoolean("flag", true);
        editor.commit();

        dbBackup();
    }

    private void dbBackup() {

        sharedPref = getSharedPreferences("dss01", MODE_PRIVATE);
        editor = sharedPref.edit();


        if (sharedPref.getBoolean("flag", false)) {

            String dt = sharedPref.getString("dt", new SimpleDateFormat("dd-MM-yy").format(new Date()).toString());

            if (dt != new SimpleDateFormat("dd-MM-yy").format(new Date()).toString()) {
                editor.putString("dt", new SimpleDateFormat("dd-MM-yy").format(new Date()).toString());

                editor.commit();
            }

            File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "DMU-PISHIN");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            if (success) {

                DirectoryName = folder.getPath() + sharedPref.getString("dt", "");
                folder = new File(DirectoryName);
                if (!folder.exists()) {
                    success = folder.mkdirs();
                }
                if (success) {

                    try {
                        File dbFile = new File(this.getDatabasePath(DatabaseHelper.DATABASE_NAME).getPath());
                        FileInputStream fis = new FileInputStream(dbFile);

                        String outFileName = DirectoryName + File.separator +
                                DatabaseHelper.DATABASE_NAME;

                        // Open the empty db as the output stream
                        OutputStream output = new FileOutputStream(outFileName);

                        // Transfer bytes from the inputfile to the outputfile
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fis.read(buffer)) > 0) {
                            output.write(buffer, 0, length);
                        }
                        // Close the streams
                        output.flush();
                        output.close();
                        fis.close();
                    } catch (IOException e) {
                        Log.e("dbBackup:", e.getMessage());
                    }

                }

            } else {
                Toast.makeText(this, "Not create folder", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @OnClick(R.id.btnNext)
    void moveNext() {

        if (usr.size() <= 0 || usr.size() > i) {
            Toast.makeText(this, "You are at 1st / last record", Toast.LENGTH_LONG).show();
        } else {
            username.setText(usr.get(i).getUserName());
            passwd.setText(usr.get(i).getPasswd());
            i++;
        }
    }
}