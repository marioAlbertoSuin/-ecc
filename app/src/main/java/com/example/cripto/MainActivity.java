package com.example.cripto;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import java.io.FileOutputStream;


import java.io.BufferedReader;
import java.io.FileInputStream;

import java.io.InputStreamReader;

public class MainActivity<Public> extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    private static volatile Instrumentation globalInstrumentation;
    private EditText texto;
    private TextView resultadoRrip;
    ImageView imagen;
    private static final String FILE_NAME = "texto.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
       // tv.setText(stringFromJNI());
        imagen = (ImageView)findViewById(R.id.imageId);


    }

    public void imagen(View view) {
        cargarImagen();
    }

    private void cargarImagen() {

        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicacion"),10);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Uri path= data.getData();

            imagen.setImageURI(path);

        }else{

            texto=findViewById(R.id.ID_texto);
            resultadoRrip=findViewById(R.id.IDtextView);
            resultadoRrip.setText("no funciona");
        }
    }

    public void Criptar(View Vista){
        long tinicio = System.currentTimeMillis() ;
        texto=findViewById(R.id.ID_texto);
        resultadoRrip=findViewById(R.id.IDtextView);
        String data = texto.getText().toString();
        String cadena = cambio(data);

        resultadoRrip.setText(stringFromJNI(cadena));
        long tfinal = System.currentTimeMillis();
        long tDiferencia = tfinal - tinicio;
        saveFile(String.valueOf(tDiferencia+"\t"+data.length()));
    }


    public void CriptarIMG(View Vista){
        long tinicio = System.currentTimeMillis() ;
        resultadoRrip=findViewById(R.id.IDtextView);
        String image = cambio(imagen.getImageMatrix().toString());
        resultadoRrip.setText(stringFromJNI(image));
        long tfinal = System.currentTimeMillis();
        long tDiferencia = tfinal - tinicio;
        saveFile(String.valueOf(tDiferencia+"\t"+imagen.getImageMatrix().toString().length()));

    }


    public String cambio(String texto){
        byte []byteArray = new byte[texto.length()];
        for(int i=0;i<byteArray.length;i++){
            byteArray[i] = (byte) texto.charAt(i);
        }
        return String.valueOf(byteArray);
    }



    private void saveFile(String tiempo){
       // String textoASalvar = etFile.getText().toString();
        String datos = leer()+"\n"+tiempo;
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

    public void readFile(View vista){
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
            resultadoRrip.setText(stringBuilder);
        }catch (Exception e){

        }finally {
            if(fileInputStream !=null){
                try {
                    fileInputStream.close();
                }catch (Exception e){

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




    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI(String cadena);
}