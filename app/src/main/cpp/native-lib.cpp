#include <jni.h>
#include <omp.h>
#include <ctype.h>
#include <stdlib.h>
#include <android/log.h>
#include <iostream>
#include <string>
#include <cmath>

#include <stdio.h>
#include <time.h>
#include <math.h>


#define  LOG_TAG    "your-log-tag"

#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_

//////////////////////////////////////////////////////////////////////////////
using namespace std;

class point
{
public:
    int x;
    int y;
};
point P[100];
int num = 0;

// Modulo
int my_mod(int a, int p)
{
    // Tenga en cuenta que en el caso de números negativos, agregue una p
    int i;
    i = a / p;
    int re = a - i * p;
    if (re >= 0)
    {
        return re;
    }
    else
    {
        return re + p;
    }
}

// Operación eléctrica, incluida la operación modular, para evitar desbordes
int my_pow(int a, int m, int p)
{
    int result = 1;
    for (int i = 0; i < m; i++)
    {
        result = (result*a) % p;
    }
    return result;
}

// Se usa para encontrar y y determinar si la raíz cuadrada es un número entero
int my_sqrt(int s)
{
    int t;
    t = (int)sqrt(s);
    if (t*t == s)
    {
        return t;
    }
    else {
        return -1;
    }
}

void all_points(int a,int b,int p)
{
    for (int i = 0; i < p; i++)
    {
        int s = i * i * i + a * i + b;
        while (s < 0)
        {
            s += p;
        }
        s = my_mod(s, p);
        // Determinar si es un superávit cuadrado
        // p es 23, que es un número primo impar
        // Criterio de Euler
        int re = my_pow(s, (p - 1) / 2, p);
        if (re == 1)
        {
            // Buscando y
            int n = 1, y;
            int f = my_sqrt(s);
            if (f != -1)
            {
                y = f;
            }
            else
            {
                for (; n <= p - 1;)
                {
                    s = s + n * p;
                    f = my_sqrt(s);
                    if (f != -1)
                    {
                        y = f;
                        break;
                    }
                    n++;
                }
            }
            y = my_mod(y, p);
            P[num].x = i;
            P[num].y = y;
            num++;
            if (y != 0)
            {
                P[num].x = i;
                P[num].y = (p - y) % p;
                num++;
            }
        }
    }
}

void show()
{
    for (int i = 0; i < num; i++)
    {
        cout << P[i].x << " " << P[i].y << endl;
    }
}


// Método euclidiano extendido, método recursivo
int extend(int a, int b, int&x, int&y)
{
    if (b == 0)
    {
        x = 1;
        y = 0;
        return a;
    }
    int r = extend(b, a % b, x, y);
    int t = x;
    x = y;
    y = t - a / b * y;
    return r;
}

// Extiende la inversión euclidiana con la ayuda de la recursividad
int inv(int a, int b)
{
    int x, y;
    int r = extend(a, b, x, y);
    if (r != 1)
    {
        return 0;
    }
    x = x % b;
    if (x < 0)
    {
        x = x + b;
    }
    return x;
}


// Suma de dos puntos
point add(point p1, point p2, int a, int p)
{
    long t;int flag = 0;
    int x1 = p1.x;int y1 = p1.y;
    int x2 = p2.x;int y2 = p2.y;
    int tx, ty;int x3, y3;

    if ((x2 == x1) && (y2 == y1))
    {
        // Mismo punto
        if (y1 == 0)
        {
            flag = 1;
        }
        else
        {
            t = (3 * x1*x1 + a)*inv(2 * y1, p) % p;
        }
    }
    else
    {
        // Agrega las diferencias
        ty = y2 - y1;
        tx = x2 - x1;
        while (tx<0)
        {
            tx = tx + p;
        }
        while (ty<0)
        {
            ty = ty + p;
        }

        if (tx == 0 && ty != 0)
        {
            flag = 1;
        }
        else
        {
            // Los puntos no son iguales
            t = ty * inv(tx, p) % p;
        }
    }

    if (flag == 1)
    {
        // Punto infinito
        p2.x = -1;
        p2.y = -1;
    }
    else
    {
        x3 = (t*t - x1 - x2) % p;
        y3 = (t*(x1 - x3) - y1) % p;
        while (x3<0)
        {
            x3 += p;
        }
        while (y3<0)
        {
            y3 += p;
        }
        p2.x = x3;
        p2.y = y3;
    }
    return p2;
}

// Seleccione aleatoriamente un generador y calcule el orden
int jie(point &pp, int a, int p)
{
    int ii = rand() % num;
    point P0 = P[ii];
    point p1, p2;
    int number = 1;
    p1.x = P0.x; p2.x = P0.x;
    p1.y = P0.y; p2.y = P0.y;
    while (true)
    {
        p2 = add(p1, p2, a, p);
        if (p2.x == -1 && p2.y == -1)
        {
            break;
        }
        number++;
        if (p2.x == p1.x)
        {
            break;
        }
    }
    pp.x = p1.x;
    pp.y = p1.y;
    int n = ++number;
    return n;
}

// Juicio principal
bool judge(int num)
{
    bool ret = true;
    int ubound = sqrt(num) + 1;
    for (int i = 2; i < ubound; i++)
    {
        if (num % i == 0)
        {
            ret = false;
            break;
        }
    }
    return ret;
}

// Calcular kG
point cal(point G, int k, int a, int p)
{
    point temp = G;
    for (int i = 0; i < k - 1; i++)
    {
        temp = add(temp, G, a, p);
    }
    return temp;
}

/////////////////////////////////////////////////////////////////////////////

std::string ConvertJString(JNIEnv *pEnv, jstring pJstring);
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_cripto_MainActivity_stringFromJNI(JNIEnv* env, jobject,jstring clave, jstring cadena ) {


    //std::string cad = ConvertJString(env,cadena);
    clock_t ti;
    ti = clock();
    char buf [128];

    const char *mensaje = (*env).GetStringUTFChars(cadena,0);
    const char *cla = (*env).GetStringUTFChars(clave,0);
    //std::string hello = mensaje;
    const char *SE=mensaje;
    const char *CR=mensaje;


    //printf("(12 bits) mensaje plano = ");
    //scanf("%12s",&m);
    // const char *m=mensaje;
    // A 64 bit key
   // string key= "1010101010111011000010010001100000100111001101101100110011011101";
   // LOGD(" el pepe %s",m);
    // A block of plain text of 64 bits
  //  pt= m;
    // Calling the function to generate 16 keys
   // generate_keys(key);
    //cout<<"Plain text: "<<pt<<endl;
    // Applying the algo
   // string ct= DES();

   // cout<<"Ciphertext: "<<ct<<endl;
    srand(time(NULL));
    int a= 1, b=1, p=23;
    point generator; int n;
    //char SE=sms;
    //char CR[10];




    // Calcular todos los puntos
    all_points(a, b, p);
    // Seleccionar generadores hasta que el pedido sea primo
    do
    {
        n = jie(generator, a, p);
    } while (judge(n) == false);
    cout << endl << "Seleccione el generador (" << generator.x << "," << generator.y << "), el orden es:" << n << endl;
    // Seleccionar clave privada
    int ka = int (CR [0])% (n-1) + 1; // Seleccione la clave utilizada
    point pa = cal (generator, ka, a, p); // Calcula la clave pública
    cout << "Clave privada:" << ka << endl;
    cout << "Clave pública: (" << pa.x << "," << pa.y << ")" << endl;

    // Cifrado

    int k = 0; // número aleatorio k
    k = rand() % (n - 2) + 1;
    point C1 = cal (generator, k, a, p); // Calcular C1

    // m incrustado en la curva elíptica
    int t = rand ()% num; // Seleccionar punto de mapeo
    point Pt = P[t];
    point P2 = cal(pa, k, a, p);
    point Pm = add(Pt, P2, a, p);
    cout << endl << "El texto cifrado a enviar:" << endl;
    cout << "kG=(" << C1.x << "," << C1.y << "),pt+kPa=(" << Pm.x << "," << Pm.y << ")";
    int C[100];
    char *val;
    cout<<",C = { ";
    for (int i = 0; i<strlen(SE); i++)
    {
        C [i] = int (SE [i]) * Pt.x + Pt.y; // Seleccione el texto sin formato a cifrar
        cout<< C[i] <<" ";
        val= reinterpret_cast<char *>(C[i]);
    }
    cout << "}" << endl;


    // Descifrar
    point temp, temp1;
    int m;
    string xx;
    temp = cal(C1, ka, a, p);
    temp.y = 0 - temp.y;
    temp1 = add (Pm, temp, a, p); // Resuelve para Pt
    printf ("\n Resultado encriptado: \n");
    for (int i = 0; i<strlen(SE); i++)
    {
        m = (C[i] - temp1.y) / temp1.x;
        printf ("% c", char (m)); // texto cifrado de salida

        //xx=xx+char(C[i]);

    }
    printf("\n");



    ti = clock() - ti;
    std::string click = std::to_string(ti);
    std::string crypt = ",".join(map(str,C));
    std::string tiempo = std::to_string(((float)ti)/CLOCKS_PER_SEC);
    std::string hello = "ramm en click"+click+"\n tiempo en segundos "+tiempo+"\n cyptado "+;


    return env->NewStringUTF(hello.c_str());
}


