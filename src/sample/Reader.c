/*
 * test.c
 * 
 * Copyright 2018  <ConsoftInformaticaSRL>
 * 
 * Opzioni di compilazione: 
 * gcc -o test.o test.c $(pkg-config --cflags --libs gtk+-3.0) -lnfc
 * 
 * Passaggi logici:
 * 	1) read_nfc
 * 	2) print_hex
 * 	3) socket_to_java
 * 	4) open_dialog
 * 
 */


//NFC libreria per la lettura del modulo nfc
#include <nfc/nfc.h>

//TCP librerie per la connessione al servet via socket TCP
#include <stdio.h>
#include <string.h>
#include <time.h>
#include "sample_NFC.h"


#define TIMEOUT 1*CLOCKS_PER_SEC

nfc_device *pnd;
nfc_target nt;
nfc_context *context;
int errorNFC=1;

char data[64] = "ESC";


void print_hex(const uint8_t *pbtData, const size_t szBytes);
char* strnconcat(char *dest, const uint8_t *src, size_t n);
/**
 * Metodo per la lettura del tag NFC.
 * @return intero per la verifica di eventuali errori
 * */
JNIEXPORT jintArray JNICALL Java_sample_NFC_readNfc (JNIEnv *env, jobject javobj) {
	
        
    do{
        /*nfc_init(&context);

        if (context == NULL) {
            printf("Unable to init libnfc (malloc)\n");

            return -1;
        }

        pnd = nfc_open(context, NULL);

        if (pnd == NULL) {
            printf("ERROR: %s\n", "Unable to open NFC device.");

            return -2;
        }
        if (nfc_initiator_init(pnd) < 0) {
            nfc_perror(pnd, "nfc_initiator_init");

            return -3;
        }*/
        nfc_init(&context);

        if (context != NULL) {
            pnd = nfc_open(context, NULL);

            if (pnd != NULL) {
                if (nfc_initiator_init(pnd) >= 0) {
                    errorNFC=0;
                }
                else{
                    nfc_perror(pnd, "nfc_initiator_init");
                    nfc_close(pnd);
                    nfc_exit(context);
                }

            }
            else{
                printf("ERROR: %s\n", "Unable to open NFC device.");
                nfc_close(pnd);
                nfc_exit(context);
            }

        }
        else{
            printf("Unable to init libnfc (malloc)\n");

            nfc_exit(context);
        }

    }while(errorNFC==1);
	printf("NFC reader: %s opened\n", nfc_device_get_name(pnd));

	const nfc_modulation nmMifare = {.nmt = NMT_ISO14443A, .nbr = NBR_106};
	jintArray result;
        
	if (nfc_initiator_select_passive_target(pnd, nmMifare, NULL, 0, &nt) > 0) {
		printf("The following (NFC) ISO14443A tag was found:\n");
		result = (*env)->NewIntArray(env, nt.nti.nai.szUidLen);
                jint fill[nt.nti.nai.szUidLen];
		printf("       Hhyusdw");
		printf("       UID (NFCID%c): ", (nt.nti.nai.abtUid[0] == 0x08 ? '3' : '1'));
		//print_hex(nt.nti.nai.abtUid, nt.nti.nai.szUidLen);
		for(int i=0;i<nt.nti.nai.szUidLen;i++) {
		    fill[i]=(jint) ((int) nt.nti.nai.abtUid[i]);
		    printf("Elemento nti.Uid %d: %d\n", i, (int) nt.nti.nai.abtUid[i]);
		}
		printf("Lunghezza array: %d", nt.nti.nai.szUidLen);
		(*env)->SetIntArrayRegion(env, result, 0, nt.nti.nai.szUidLen, fill);
        
	}
	nfc_close(pnd);
	nfc_exit(context);
	
	
	printf("Funziona");
	//return (*env) -> NewIntArray(env,stringa);

        

        /*jintArray result;
        result = (*env)->NewIntArray(env, 4);

        if (result == NULL) {
            return NULL;
        }

        int i;

        jint fill[4];
        for (i=0; i<4; i++) {
            fill[i] = 0;
        }*/

        return result;

}


/**
 * Metodo per la stampa in console dei valori letti dal passaggio del tag NFC
 * */
void print_hex(const uint8_t *pbtData, const size_t szBytes) {
	size_t  szPos;
        //int length = (int) szBytes;
        

	int i;
	strnconcat(data, pbtData, szBytes);
	/*
	for ( i = 0; i < szBytes; i++){
		data[3+i] = hexadecimalToDecimal((char*)("%02x", pbtData[i]));
		//printf("%u", pbtData[szPos]);
	}
	*/

	for (szPos = 0; szPos < szBytes; szPos++) {
		printf("%02x  ", pbtData[szPos]);
                
	}
	printf("\n");
        
        
        
}

char* strnconcat(char *dest, const uint8_t *src, size_t n) {
	size_t dest_len = strlen(dest);
	size_t i;

	for (i = 0 ; i < n && src[i] != '\0' ; i++){
		dest[dest_len + i] = ("%c",src[i]);
	}
	
	dest[dest_len + i] = '\0';

	return dest;
}







