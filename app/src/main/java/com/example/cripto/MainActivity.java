package com.example.cripto;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    private EditText texto;
    private TextView resultadoRrip;
    ImageView imagen;

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
        texto=findViewById(R.id.ID_texto);
        resultadoRrip=findViewById(R.id.IDtextView);
        String data = texto.getText().toString();
        resultadoRrip.setText(stringFromJNI(data));
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI(String cadena);
}