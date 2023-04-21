package com.imss.sivimss.arquetipo.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtil {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LogUtil.class);
    public static void crearArchivoLog(String tipoLog, String origen, String clasePath , String mensaje) throws IOException {
        DateFormat formatoFechaLog = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
        DateFormat formatoFecha = new SimpleDateFormat("ddMMyyyy");
        String fechaArchivo = formatoFecha.format(new Date());
        File archivo = new File("C:/Users/eoflores/Documents/logs/" + fechaArchivo + ".log" );
        FileWriter escribirArchivo = new FileWriter(archivo,true);
        if(archivo.exists()){
            escribirArchivo.write("" + formatoFechaLog.format(new Date()) + " --- [" + tipoLog +"] " +  origen + " " +clasePath + " : " + mensaje );
            escribirArchivo.write("\r\n");
            escribirArchivo.close();
        }else{
            archivo.createNewFile();
            escribirArchivo.write("" + formatoFechaLog.format(new Date()) + " --- [" + tipoLog +"] " +  origen + " " +clasePath + " : " + mensaje );
            escribirArchivo.write("\r\n");
            escribirArchivo.close();
        }
    }
}
