package com.example.cripto;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


import java.io.BufferedReader;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity<Public> extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    private static volatile Instrumentation globalInstrumentation;
    private EditText texto,clave;
    private TextView resultadoRrip;
    ImageView imagen;
    Bitmap bm;
    String base64;

    private static final String FILE_NAME = "texto.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
       // TextView tv = findViewById(R.id.sample_text);
       // tv.setText(stringFromJNI());
        imagen = (ImageView)findViewById(R.id.imagenid);


    }

    public void imagen(View view) {
        cargarImagen();
    }

    private void cargarImagen() {

        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicacion"),10);


    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Uri path= data.getData();

            try {
                InputStream iStream =   getContentResolver().openInputStream(path);
                byte[] inputData = getBytes(iStream);
                base64 = Base64.encodeToString(inputData,Base64.DEFAULT);

                AlertDialog.Builder myBuild = new AlertDialog.Builder(this);
                myBuild.setMessage("Archivo Cargado");
                myBuild.setTitle("Aviso");
                myBuild.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = myBuild.create();
                dialog.show();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }



            imagen.setImageURI(path);

           // bm = BitmapFactory.decodeFile(data.getData().toString());
        }else{


            resultadoRrip=findViewById(R.id.IDtextView);
            resultadoRrip.setText("Seleccione un archvio");
        }
    }

    public void archivo(View view){cargarArchivo();}


    public void cargarArchivo(){

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent.createChooser(intent, null),10);


    }




    public void Criptar(View Vista){

        long tinicio = System.currentTimeMillis() ;
        //texto=findViewById(R.id.ID_texto);
        clave=findViewById(R.id.IDclave);
        resultadoRrip=findViewById(R.id.IDtextView);
        //


        //

//        String data = texto.getText().toString();
        String psw = clave.getText().toString();

        //String cadena = cambio(data);
        //String cla = cambio(psw);

        String resultado = stringFromJNI(base64 ,psw);
        String []datos = resultado.split("-");
        resultadoRrip.setText("\n Texto encriptado:\n"+datos[2]+"\n Clicks en ram: \n"+datos[0]+"\n Segundos: \n"+datos[1]);

        long tfinal = System.currentTimeMillis();
        long tDiferencia = tfinal - tinicio;
        saveFile(String.valueOf(datos[0]+","+datos[1]+","+datos[2])+","+String.valueOf(tDiferencia)+","+base64.length()+","+psw.length());
    }



      private void saveFile(String tiempo){
       // String textoASalvar = etFile.getText().toString();
        String datos = leer()+tiempo;
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fileOutputStream.write(datos.getBytes());
            Log.d("TAG1", "Fichero Salvado en: " + getFilesDir() + "/" + FILE_NAME);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(fileOutputStream != null){
                try{
                    fileOutputStream.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private String leer(){

        resultadoRrip=findViewById(R.id.IDtextView);
        FileInputStream fileInputStream = null;
        try{
            fileInputStream = openFileInput(FILE_NAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String lineaTexto;
            StringBuilder stringBuilder = new StringBuilder();
            while((lineaTexto = bufferedReader.readLine())!=null){
                stringBuilder.append(lineaTexto).append("\n");
            }
            return stringBuilder.toString();
        }catch (Exception e){
            return "";
        }finally {
            if(fileInputStream !=null){
                try {
                    fileInputStream.close();
                }catch (Exception e){

                }
            }
        }
    }

    public void  CargarGraficas(View view){

        Intent acty2 =new Intent(this,graficas.class);
        startActivity(acty2);

    }




    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI(String cadena,String clave);
}