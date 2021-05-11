#include <jni.h>
#include <string>
#include <omp.h>
#include <ctype.h>
#include <stdlib.h>

#include <android/log.h>

#define  LOG_TAG    "your-log-tag"

#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

char m[13]; // 12 caracteres + null= 13 caracteres
char mi[13];
char mj[13];
char li[7];
char ri[7];
char l0[7];
char r0[7];
char l1[7];
char r1[7];
char l2[7];
char r2[7];
char l3[7];
char r3[7];
char l4[7];
char r4[7];
char l5[7];
char r5[7];
char l6[7];
char r6[7];
char l7[7];
char r7[7];
char l8[7];
char r8[7];
char l9[7];
char r9[7];



char er[9];

char k[10];
char kk[19];

char k1[9];
char k2[9];
char k3[9];
char k4[9];
char k5[9];
char k6[9];
char k7[9];
char k8[9];
char k9[9];

char str[7];

char x8[9];
char x6[7];

char b1[7];
char b2[7];


char row;
char col[4];


char c1[4];
char c2[4];

char c[7];




// s-boxes S1 y S2: entrada 4 bits salida 3 bits
void sbox_1(char row, char *col) {
    int icol;
    icol=atoi(col);
    if (row=='0')
        switch (icol) {
            case 0: strcpy(c1,"101");break;
            case 1: strcpy(c1,"010");break;
            case 10: strcpy(c1,"001");break;
            case 11: strcpy(c1,"110");break;
            case 100: strcpy(c1,"011");break;
            case 101: strcpy(c1,"100");break;
            case 110: strcpy(c1,"111");break;
            default: strcpy(c1,"000");break;}

    else
        switch (icol)  {
            case 0: strcpy(c1,"001");break;
            case 1: strcpy(c1,"100");break;
            case 10: strcpy(c1,"110");break;
            case 11: strcpy(c1,"010");break;
            case 100: strcpy(c1,"000");break;
            case 101: strcpy(c1,"111");break;
            case 110: strcpy(c1,"101");break;
            default: strcpy(c1,"011");break; }}
void sbox_2(char row, char *col) {
    int icol;
    icol=atoi(col);
    if (row=='0')
        switch (icol)   {
            case 0: strcpy(c2, "100");break;
            case 1: strcpy(c2, "000");break;
            case 10: strcpy(c2, "110");break;
            case 11: strcpy(c2, "101");break;
            case 100: strcpy(c2, "111");break;
            case 101: strcpy(c2, "001");break;
            case 110: strcpy(c2, "011");break;
            default: strcpy(c2,"010");break;}

    else
        switch (icol) {
            case 0: strcpy(c2,"101");break;
            case 1: strcpy(c2,"011");break;
            case 10: strcpy(c2,"000");break;
            case 11: strcpy(c2,"111");break;
            case 100: strcpy(c2,"110");break;
            case 101: strcpy(c2,"010");break;
            case 110: strcpy(c2,"001");break;
            default:strcpy(c2,"100");break; }}


// divide el mensaje plano en dos de 6 bits

void split_m(){
    strncpy(l0,m,7);// 6 caracteres + null = 7 caracteres
    l0[6]='\0';
    strncpy(r0,m+6,7);
    r0[6]='\0';

    strncpy(l9,m,7);// 6 caracteres + null = 7 caracteres
    l9[6]='\0';
    strncpy(r9,m+6,7);
    r9[6]='\0';

}

// divide E(Ri-1) xor ki en dos mitades de 4 bits
void split_x(){
    strncpy(b1,x8,5);//4 caracteres + null = 5 caracteres
    b1[4]='\0';
    strncpy(b2,x8+4,5);
    b2[4]='\0';}

// or exclusiva de 8 bits
void xor8(char *a,char *b){
    int i;
    for (i=0; i<strlen(a);i++){
        if ( a[i]!=b[i]) x8[i]='1';
        else x8[i]='0';} }

// or exclusiva de 6 bits
void xor6(char *a,char *b){
    int i;
    for (i=0; i<strlen(a);i++){
        if ( a[i]!=b[i]) x6[i]='1';
        else x6[i]='0';}
    strcpy(ri,x6); }

// genera las subclaves de 8 bits a partir de la clave de 9
void subkeys(){
    strcpy(kk,k);
    strcat(kk,k);

    strncpy(k1,kk,9);
    k1[8]='\0';
    strncpy(k2,kk+1,9);
    k2[8]='\0';
    strncpy(k3,kk+2,9);
    k3[8]='\0';
    strncpy(k4,kk+3,9);
    k4[8]='\0';
    strncpy(k5,kk+4,9);
    k5[8]='\0';
    strncpy(k6,kk+5,9);
    k6[8]='\0';
    strncpy(k7,kk+6,9);
    k7[8]='\0';
    strncpy(k8,kk+7,9);
    k8[8]='\0';
    strncpy(k9,kk+8,9);
    k9[8]='\0'; }

// expande Ri-1 de 6 a 8 bits
void expand_r(char *str){
    er[0]=str[0];
    er[1]=str[1];
    er[2]=str[3];
    er[3]=str[2];
    er[4]=str[3];
    er[5]=str[2];
    er[6]=str[4];
    er[7]=str[5];
    er[8]='\0';   }


// realiza Feistel
void feistel(char *l,char *r,char *k){
    char row1;
    char col1[4];

    char row2;
    char col2[4];



    expand_r(r);
    xor8(er,k);
    split_x();

    row1=b1[0];
    strncpy(col1,b1+1,4);
    col1[3]='\0';

    row2=b2[0];
    strncpy(col2,b2+1,4);
    col2[3]='\0';

    sbox_1(row1, col1);
    sbox_2(row2, col2);

    strcpy(c,c1);
    strcat(c,c2);

    strcpy(li,r);
    xor6(l,c);

    strcpy(mi,li);
    strcat(mi,ri);

    strcpy(mj,ri);
    strcat(mj,li);

}

char * encrypt(){

    feistel(l0,r0,k1);
    strcpy(l1,li);
    strcpy(r1,ri);

    feistel(l1,r1,k2);
    strcpy(l2,li);
    strcpy(r2,ri);

    feistel(l2,r2,k3);
    strcpy(l3,li);
    strcpy(r3,ri);

    feistel(l3,r3,k4);
    strcpy(l4,li);
    strcpy(r4,ri);

    feistel(l4,r4,k5);
    strcpy(l5,li);
    strcpy(r5,ri);

    feistel(l5,r5,k6);
    strcpy(l6,li);
    strcpy(r6,ri);

    feistel(l6,r6,k7);
    strcpy(l7,li);
    strcpy(r7,ri);

    feistel(l7,r7,k8);
    strcpy(l8,li);
    strcpy(r8,ri);

    feistel(l8,r8,k9);

    //printf("--------------------------\n");
    LOGD("mensaje cifrado = %s\n",mi);
    return mi;
}

void decrypt() {

    feistel(r9, l9, k9);
    strcpy(l8, ri);
    strcpy(r8, li);

    feistel(r8, l8, k8);
    strcpy(l7, ri);
    strcpy(r7, li);

    feistel(r7, l7, k7);
    strcpy(l6, ri);
    strcpy(r6, li);

    feistel(r6, l6, k6);
    strcpy(l5, ri);
    strcpy(r5, li);

    feistel(r5, l5, k5);
    strcpy(l4, ri);
    strcpy(r4, li);

    feistel(r4, l4, k4);
    strcpy(l3, ri);
    strcpy(r3, li);

    feistel(r3, l3, k3);
    strcpy(l2, ri);
    strcpy(r2, li);

    feistel(r2, l2, k2);
    strcpy(l1, ri);
    strcpy(r1, li);

    feistel(r1, l1, k1);
    strcpy(l0, ri);
    strcpy(r0, li);


    printf("--------------------------\n");
    LOGD("mensaje descifrado = %s\n", mj);

}




std::string ConvertJString(JNIEnv *pEnv, jstring pJstring);
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_cripto_MainActivity_stringFromJNI(JNIEnv* env, jobject, jstring cadena) {
    //std::string cad = ConvertJString(env,cadena);
    char buf [128];

    const char *mensaje = (*env).GetStringUTFChars(cadena,0);
    //std::string hello = mensaje;
    const char *m=mensaje;
    char k[8]= { 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11 };
    split_m();
    LOGD(" el pepe %s",m);
    subkeys();
    encrypt();
    decrypt();

    //printf("(12 bits) mensaje plano = ");
    //scanf("%12s",&m);
    // const char *m=mensaje;



    std::string hello = mensaje;


    return env->NewStringUTF(hello.c_str());
}


