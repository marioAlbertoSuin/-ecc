package com.example.cripto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

import android.graphics.Color;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class graficas extends AppCompatActivity {
    private static final String FILE_NAME = "texto.csv";

    LineChartView lineChartView;
   // String[] axisData = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept",
   //         "Oct", "Nov", "Dec"};
   // int[] yAxisData = {50, 20, 15, 30, 20, 60, 15, 40, 45, 10, 90, 18};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficas);

        lineChartView = findViewById(R.id.chart);
        ArrayList<ArrayList<String>> datos =readFile();
        String [] axisData = new String[datos.size()];
        String [] yAxisData = new String[datos.size()];


        for(int i=0;i<datos.size();i++){
            for (int j=0;j<datos.get(i).size();j++){
                axisData[i]=datos.get(i).get(2);
                yAxisData[i]=datos.get(i).get(4);
            }
        }



        

        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();


        Line line = new Line(yAxisValues).setColor(Color.parseColor("#9C27B0"));

        for (int i = 0; i < axisData.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
        }

        for (int i = 0; i < yAxisData.length; i++) {
            yAxisValues.add(new PointValue(i,Float.parseFloat(yAxisData[i])));
        }

        List lines = new ArrayList();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axis = new Axis();
        axis.setValues(axisValues);
        axis.setTextSize(16);
        axis.setTextColor(Color.parseColor("#03A9F4"));
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        yAxis.setName("Tiempo de encryptacion");
        yAxis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextSize(16);
        data.setAxisYLeft(yAxis);

        lineChartView.setLineChartData(data);
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top = 110;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);

    }


    public ArrayList<ArrayList<String>> readFile(){

        ArrayList<ArrayList<String >> datos = new ArrayList<ArrayList<String >>();
        FileInputStream fileInputStream = null;
        try{
            fileInputStream = openFileInput(FILE_NAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String lineaTexto;
            //StringBuilder stringBuilder = new StringBuilder();
            while((lineaTexto = bufferedReader.readLine())!=null){
                String[] datosLinea = lineaTexto.split(",");
                ArrayList<String> datosTemporal = new ArrayList<String>();
                for(String dato : datosLinea){
                    datosTemporal.add(dato);
                }
                datos.add(datosTemporal);
                //stringBuilder.append(lineaTexto).append("\n");
            }
            //resultadoRrip.setText(stringBuilder);
        }catch (Exception e){

        }finally {
            if(fileInputStream !=null){
                try {
                    fileInputStream.close();
                }catch (Exception e){

                }
            }
            System.out.println(datos.get(0).size());
            return datos;

        }
    }



}