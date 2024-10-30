/*
 * Copyright (C) 2024 Jesus del Buey Jiménez
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * SPDX-License-Identifier: GPL-3.0
 */
package main;

import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Jesus.delBuey
 */
public class Config {

    // Ruta de la ayuda de la aplicación, con la barra normal, porque sera una url
    public static String getRutaAyuda() {
        String carpetaUsuario = "file://".concat(System.getProperty("user.dir"));
        // Si el sistema es Linux, añado el nombre del programa, para que encuentre la ayuda
        if (System.getProperty("os.name").contains("Linux")) {
            carpetaUsuario = carpetaUsuario.concat("/correctA");
        }
        return (carpetaUsuario.concat("/ayuda/").concat(getIdiomaActual())).replace("\\", "/");
    }

    public static String getRutaAyudaInicio() {
        return getRutaAyuda().concat("/pgInicio.html");
    }

    public static String getRutaAyudaConfiguracion() {
        return getRutaAyuda().concat("/pgConfiguracion.html");
    }
    
    public static String getRutaAyudaPruebas() {
        return getRutaAyuda().concat("/pgPruebas.html");
    }

    public static String getRutaAyudaHojaRespuestas() {
        return getRutaAyuda().concat("/pgHojaRespuestas.html");
    }

    public static String getRutaAyudaEvaluaciones() {
        return getRutaAyuda().concat("/pgEvaluaciones.html");
    }
    
    public static String getRutaAyudaTiposyEquivalencias() {
        return getRutaAyuda().concat("/pgTiposyEquivalencias.html");
    }

    public static String getRutaAyudaEstadisticas() {
        return getRutaAyuda().concat("/pgEstadisticas.html");
    }
    
    //Logo de la Aplicación
    protected static ImageIcon iconoAplic = new ImageIcon(Toolkit.getDefaultToolkit().getImage(Config.class.getResource("icono_aplicacion.png")));

    public static ImageIcon getIconoAplic() {
        return iconoAplic;
    }

    // Log general de la aplicación
    protected static Loguero log = Procesador.getLog();

    // Base de datos de la aplicación.
    // Como debe estar fuera del archivo .jar para poderlo sobreescribirla la creo si no existe, en la carperta raiz de ejecución
    // En el ide si funciona, en la carpeta de recursos "src/main/resources/main/correctA.db". pero en un .jar, NO.
    // Genero un path válido multiplataforma
    private static final String FICHERO_BASE_DATOS = Paths.get("correctA.db").toString();

    /**
     * Get the value of nombreBaseDatos
     *
     * @return the value of nombreBaseDatos
     */
    public static String getFicheroBaseDatos() {
        return FICHERO_BASE_DATOS;
    }

    // Fichero de configuración. Igual que el de base de datos, debe estar fuera del archivo .jar para poderlo sobreescribir lo creo si no existe,
    // en la carperta raiz de ejecución.
    //
    // Genero un path válido multiplataforma
    private static final String FICHERO_CONFIGURACION = Paths.get("configuracion.cfg").toString();

    public static String getFichConfiguracion() {
        return FICHERO_CONFIGURACION;
    }

    private static String IdiomaActual = Locale.getDefault().getLanguage(); // valor por defecto

    /**
     * Get the value of IdiomaActual
     *
     * @return the value of IdiomaActual
     */
    public static String getIdiomaActual() {
        return IdiomaActual;
    }

    /**
     * Set the value of IdiomaActual
     *
     * @param IdiomaActual new value of IdiomaActual
     */
    public static void setIdiomaActual(String IdiomaActual) {
        Config.IdiomaActual = IdiomaActual;
    }

    // Valor inicial para la carpeta de archivos donde están los test
    private static String carpetaArchivosTests = "";

    public static String getCarpetaArchivosTests() {
        return carpetaArchivosTests;
    }

    public static void setCarpetaArchivosTests(String valor) {
        Config.carpetaArchivosTests = valor;
    }

    //
    // Fichero con el modelo de Test, archivo png, en uso. Se tiene en cuenta el idioma _xx
    private static final String FICHERO_MODELO_TEST = "Modelo100_General";

    /**
     * Get the value of FICHERO_MODELO_TEST
     *
     * @return the value of FICHERO_MODELO_TEST
     */
    public static String getFICHERO_MODELO_TEST() {
        // Agrego el idioma _xx y la extensión png
        return FICHERO_MODELO_TEST.concat("_").concat(getIdiomaActual()).concat(".png");
    }

    //
    // Fichero que contiene las casillas del modelo de test, sus coordenadas
    public static String ficheroCasillasTest = "main/casillasTest100.csv";

    /**
     * Get the value of ficheroCasillasTest
     *
     * @return the value of ficheroCasillasTest
     */
    public static String getFicheroCasillasTest() {
        return ficheroCasillasTest;
    }

    /**
     * Set the value of ficheroCasillasTest
     *
     * @param ficheroCasillasTest new value of ficheroCasillasTest
     */
    public static void setFicheroCasillasTest(String ficheroCasillasTest) {
        Config.ficheroCasillasTest = ficheroCasillasTest;
    }

    public static Casilla[][] esquinasZona = new Casilla[4][2]; // Cuadrados de búsqueda de las esquinas

    private static int ubralDeteccionEsquina = 210; // Valor por defecto

    /**
     * @return the ubralDeteccionEsquina
     */
    public static int getUmbralDeteccionEsquina() {
        return ubralDeteccionEsquina;
    }

    public static void setUmbralDeteccionEsquina(int limite) {
        ubralDeteccionEsquina = limite;
    }

    //    
    private static int umbralDeteccionMarca = 245; // Valor por defecto

    /**
     * @return the umbralDeteccionMarca
     */
    public static int getUmbralDeteccionMarca() {
        return umbralDeteccionMarca;
    }

    public static void setUmbralDeteccionMarca(int limite) {
        umbralDeteccionMarca = limite;
    }

    //
    private static int rangoBusquedaEsquinas = 70; // Valor por defecto

    /**
     * @return the rangoBusquedaEsquinas
     */
    public static int getRangoBusquedaEsquinas() {
        return rangoBusquedaEsquinas;
    }

    /**
     * @param limite the rangoBusquedaEsquinas to set
     */
    public static void setRangoBusquedaEsquinas(int limite) {
        rangoBusquedaEsquinas = limite;
    }

    /**
     * Grosor de los círculos de las marcas reconocidas Por defecto, 4
     */
    private static float grosorCirculoMarca = 4;

    /**
     * Get the value of grosorCirculoMarca
     *
     * @return the value of grosorCirculoMarca
     */
    public static float getGrosorCirculoMarca() {
        return grosorCirculoMarca;
    }

    /**
     * Set the value of grosorCirculoMarca
     *
     * @param valor Diametro del circulo de marcas
     */
    public static void setGrosorCirculoMarca(float valor) {
        grosorCirculoMarca = valor;
    }

    private static final int LIMITE_NEGRO_MARGEN = 150; // Valor por defecto

    /**
     * Get the value of LIMITE_NEGRO_MARGEN
     *
     * @return the value of LIMITE_NEGRO_MARGEN
     */
    public static int getLimiteNegroMargen() {
        return LIMITE_NEGRO_MARGEN;
    }

    //
    public static String SEPARADOR_CSV = ";";
    public static String DELIMITADOR_TEXTO_CSV = "\"";
    public static String CARACTER_NUEVA_LINEA = "\r\n";
    public static final int ANCHO_PUNTO_ESQUINA = 12;
    public static int MARGEN_HOJA_RECORTAR = 30;
    public static final int ANCHO_CAJA = 16;

    // Ancho de detección de las marcas de respuestas
    private static int anchoMarcasRespuesta = 10; // de 5 a 10

    public static int getAnchoMarcasRespuesta() {
        return anchoMarcasRespuesta;
    }

    public static void setAnchoMarcasRespuesta(int valor) {
        anchoMarcasRespuesta = valor;
    }

    // Margen superior a recortar
    private static int margenSuperiorHojaRecortar = 0;

    public static int getMargenSuperiorHojaRecortar() {
        return margenSuperiorHojaRecortar;
    }

    public static void setMargenSuperiorHojaRecortar(int valor) {
        margenSuperiorHojaRecortar = valor;
    }

    // Margen izquierdo a recortar
    private static int margenIzquierdoHojaRecortar = 0;

    public static int getMargenIzquierdoHojaRecortar() {
        return margenIzquierdoHojaRecortar;
    }

    public static void setMargenIzquierdoHojaRecortar(int valor) {
        margenIzquierdoHojaRecortar = valor;
    }

    // Margen derecho a recortar
    private static int margenDerechoHojaRecortar = 0;

    public static int getMargenDerechoHojaRecortar() {
        return margenDerechoHojaRecortar;
    }

    public static void setMargenDerechoHojaRecortar(int valor) {
        margenDerechoHojaRecortar = valor;
    }

    private static String rutaUltimaImagen = "";
    private static String nombreArchivoUltimaImagen = "";

    public static final int MARGEN_HOJA_NO_BUSCAR = 5;
    // Tamaño de 1000 x 1415, que es el del modelo, para reescalar
    static final int ANCHO_MODELO = 1000;
    static final int ALTO_MODELO = 1415;

// Tamaño de 205 x 40, que es el del modelo, para reescalar
    static final int ANCHO_LOGO = 256;
    static final int ALTO_LOGO = 50;

// Posicion del logo en el modelo
    static final int LOGO_X = 653;
    static final int LOGO_Y = 23;

// Estructura de la tabla que muestra un test corregido
    static final int PREGUNTAS_POR_COLUMNA = 20;
    static final int COLUMNAS_POR_HOJA = 5;

//  Establezco un número máximo de tests
    public static final int MAX_NUM_TEST = 1000;

// Tipo de fichero CSV a importar
    public static final int CSV_TIPOS_EQUIVALENCIAS = 0;
    public static final int CSV_TEST_CORREGIDOS = 1;

// Datos para la corrección
    public static final int MAX_NUM_PREGUNTAS = 100;
    public static final int MAX_NUM_TIPOS = 6;

    private static int numPreguntas = 10;

    /**
     * Get the value of numPreguntas
     *
     * @return the value of numPreguntas
     */
    public static int getNumPreguntas() {
        return numPreguntas;
    }

    /**
     * Set the value of numPreguntas
     *
     * @param valor Cantidad de preguntas
     */
    public static void setNumPreguntas(int valor) {
        numPreguntas = valor;
    }

//    
    private static int numPreguntasValidas = 100;

    /**
     * Get the value of numPreguntasValidas
     *
     * @return the value of numPreguntasValidas
     */
    public static int getNumPreguntasValidas() {
        return numPreguntasValidas;
    }

    /**
     * Set the value of numPreguntasValidas
     *
     * @param valor Cantidad de preguntasValidas
     */
    public static void setNumPreguntasValidas(int valor) {
        numPreguntasValidas = valor;
    }

    private static int numTipos = 6;

    /**
     * Get the value of numTipos
     *
     * @return the value of numTipos
     */
    public static int getNumTipos() {
        return numTipos;
    }

    /**
     * Set the value of numTipos
     *
     * @param valor Cantidad de tipos
     */
    public static void setNumTipos(int valor) {
        numTipos = valor;
    }

    private static float puntosAcierto = 1;

    /**
     * Get the value of puntosAcierto
     *
     * @return the value of puntosAcierto
     */
    public static float getPuntosAcierto() {
        return puntosAcierto;
    }

    /**
     *
     * @param pun Set the value of puntosAcierto
     */
    public static void setPuntosAcierto(float pun) {
        puntosAcierto = pun;
    }

    private static float puntosFallo = (float) 0.33;

    /**
     * Get the value of puntosFallo
     *
     * @return the value of puntosFallo
     */
    public static float getPuntosFallo() {
        return puntosFallo;
    }

    /**
     *
     * @param pun Set the value of puntosFallo
     */
    public static void setPuntosFallo(float pun) {
        puntosFallo = pun;
    }

    private static float puntosBlanco = 0;

    /**
     * Get the value of puntosBlanco
     *
     * @return the value of puntosBlanco
     */
    public static float getPuntosBlanco() {
        return puntosBlanco;
    }

    /**
     *
     * @param pun Set the value of puntosBlanco
     */
    public static void setPuntosBlanco(float pun) {
        puntosBlanco = pun;
    }

    private static float puntosDoble = 0;

    /**
     * Get the value of puntosDoble
     *
     * @return the value of puntosDoble
     */
    public static float getPuntosDoble() {
        return puntosDoble;
    }

    /**
     *
     * @param pun Set the value of puntosDoble
     */
    public static void setPuntosDoble(float pun) {
        puntosDoble = pun;
    }

    private static float escalaCalificacion = 10;

    /**
     * Get the value of escalaCalificacion
     *
     * @return the value of escalaCalificacion
     */
    public static float getEscalaCalificacion() {
        return escalaCalificacion;
    }

    /**
     *
     * @param pun Set the value of escalaCalificacion
     */
    public static void setEscalaCalificacion(float pun) {
        escalaCalificacion = pun;
    }

    private static float puntAprobado = 5;

    /**
     * Get the value of puntAprobado
     *
     * @return the value of puntAprobado
     */
    public static float getPuntAprobado() {
        return puntAprobado;
    }

    /**
     *
     * @param pun Set the value of puntAprobado
     */
    public static void setPuntAprobado(float pun) {
        puntAprobado = pun;
    }

    // Fuentos de uso en la aplicación
    // Fuente normal
    public static final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);

    // Fuente Titulo
    public static final Font FUENTE_TITULO = new Font("Segoe UI", Font.PLAIN, 18);

    // Arrays de Strings con las opciones para JOptionPane.OPtionDialog
    //
    /**
     * Arrays de Strings con las opciones para JOptionPane.OPtionDialog. Sólo
     * Aceptar
     */
    public static final String[] OPCION_ACEPTAR = {Procesador.idioma.getString("Aceptar.text")};
    /**
     * Arrays de Strings con las opciones para JOptionPane.OPtionDialog. Aceptar
     * y Cancelar
     */
    public static final String[] OPCIONES_ACEPTAR_CANCELAR = {Procesador.idioma.getString("Aceptar.text"), Procesador.idioma.getString("Cancelar.text")};
    //

// Métodos para guardar y cargar la configuración en el fichero
    public static void guardarConfiguracion() {
        File f = new File(FICHERO_CONFIGURACION);
        guardarConfiguracion(f);
    }

//
    public static void guardarConfiguracion(File f) {
        Properties conf = new Properties();

        try {
            conf.setProperty("LimiteDeteccionEsquina", Integer.toString(getUmbralDeteccionEsquina()));
            conf.setProperty("LimiteDeteccionMarca", Integer.toString(getUmbralDeteccionMarca()));
            conf.setProperty("RangoBusquedaEsquinas", Integer.toString(getRangoBusquedaEsquinas()));
            conf.setProperty("IdiomaActual", getIdiomaActual());
            conf.setProperty("RecorteSuperior", Integer.toString(getMargenSuperiorHojaRecortar()));
            conf.setProperty("RecorteIzquierdo", Integer.toString(getMargenIzquierdoHojaRecortar()));
            conf.setProperty("RecorteDerecho", Integer.toString(getMargenDerechoHojaRecortar()));
            conf.setProperty("AnchoMarcas", Integer.toString(getAnchoMarcasRespuesta()));
            conf.setProperty("NumeroPreguntas", Integer.toString(getNumPreguntas()));
            conf.setProperty("NumeroPreguntasValidas", Integer.toString(getNumPreguntasValidas()));
            conf.setProperty("NumeroTipos", Integer.toString(getNumTipos()));
            conf.setProperty("PuntosAcierto", Float.toString(getPuntosAcierto()));
            conf.setProperty("PuntosFallo", Float.toString(getPuntosFallo()));
            conf.setProperty("PuntosBlanco", Float.toString(getPuntosBlanco()));
            conf.setProperty("PuntosDoble", Float.toString(getPuntosDoble()));
            conf.setProperty("EscalaCalificacion", Float.toString(getEscalaCalificacion()));
            conf.setProperty("PuntAprobado", Float.toString(getPuntAprobado()));
            // Guardo el archivo de configuración en directorio donde se está ejecutando la aplicación
            //conf.store(new FileOutputStream(getFichConfiguracion()), "Ultima Actualización");
            FileOutputStream fos = new FileOutputStream(f);
            // Utilizo UTF-8 por compatibilidad con Linux
            conf.store(new OutputStreamWriter(fos, "UTF-8"), "Ultima Actualización");
        } catch (FileNotFoundException ex) {
            String error = Procesador.idioma.getString("Configuracion.Error.Fichero.noesta") + "\n" + ex.getMessage() + "\n";
            log.error(error);
            JOptionPane.showOptionDialog(null, error, Procesador.idioma.getString("Error.text"),
                    JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
            System.exit(0);
            //Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            String error = Procesador.idioma.getString("Configuracion.Error.Fichero.formato") + "\n" + ex.getMessage();
            log.error(error);
            JOptionPane.showOptionDialog(null, error, Procesador.idioma.getString("Error.text"),
                    JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
            //Exceptions.printStackTrace(ex);
            System.exit(0);
        }
    }

    public static boolean cargarConfiguracion() {
        Properties conf = new Properties();
        // uso un formato de separador decimal, independiente del locale. Float.parseFloat sólo funciona con el punto inglés.
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat formato = new DecimalFormat("0.#");
        formato.setDecimalFormatSymbols(symbols);
        try {
            // Compruebo si existe el fichero de configuración, si no lo creo nuevo
            File f = new File(FICHERO_CONFIGURACION);
            if (!(f.exists())) {
                // Si no existe el fichero de configuración, lo creo. En el directorio de ejecución de la aplicación
                // Con los valores por defecto.
                guardarConfiguracion(f);
            }
            FileInputStream fis = new FileInputStream(f);
            // Utilizo UTF-8 por compatibilidad con Linux
            conf.load(new InputStreamReader(fis, "UTF-8"));
            // Cargo las propiedades
            setUmbralDeteccionEsquina(Integer.parseInt(conf.getProperty("LimiteDeteccionEsquina")));
            setUmbralDeteccionMarca(Integer.parseInt(conf.getProperty("LimiteDeteccionMarca")));
            setRangoBusquedaEsquinas(Integer.parseInt(conf.getProperty("RangoBusquedaEsquinas")));
            setIdiomaActual(conf.getProperty("IdiomaActual"));
            setMargenSuperiorHojaRecortar(Integer.parseInt(conf.getProperty("RecorteSuperior")));
            setMargenIzquierdoHojaRecortar(Integer.parseInt(conf.getProperty("RecorteIzquierdo")));
            setMargenDerechoHojaRecortar(Integer.parseInt(conf.getProperty("RecorteDerecho")));
            setAnchoMarcasRespuesta(Integer.parseInt(conf.getProperty("AnchoMarcas")));
            setNumPreguntas(Integer.parseInt(conf.getProperty("NumeroPreguntas")));
            setNumPreguntasValidas(Integer.parseInt(conf.getProperty("NumeroPreguntasValidas")));
            setNumTipos(Integer.parseInt(conf.getProperty("NumeroTipos")));
            setPuntosAcierto(formato.parse(conf.getProperty("PuntosAcierto")).floatValue());
            setPuntosFallo(formato.parse(conf.getProperty("PuntosFallo")).floatValue());
            setPuntosBlanco(formato.parse(conf.getProperty("PuntosBlanco")).floatValue());
            setPuntosDoble(formato.parse(conf.getProperty("PuntosDoble")).floatValue());
            setEscalaCalificacion(formato.parse(conf.getProperty("EscalaCalificacion")).floatValue());
            setPuntAprobado(formato.parse(conf.getProperty("PuntAprobado")).floatValue());

            // Inicializo cosas. Zonas de búsqueda de Esquinas
            // Las cuatro esquinas, SI, II, SD, ID.
            esquinasZona[0][0] = new Casilla(MARGEN_HOJA_NO_BUSCAR, MARGEN_HOJA_NO_BUSCAR); // Rango de búsqueda Superior Izquierdo, esquinasImagen superior Izquierda del rango
            esquinasZona[0][1] = new Casilla(MARGEN_HOJA_NO_BUSCAR + getRangoBusquedaEsquinas() - ANCHO_PUNTO_ESQUINA, MARGEN_HOJA_NO_BUSCAR + getRangoBusquedaEsquinas() - ANCHO_PUNTO_ESQUINA); // esquinasImagen inferior derecha del rango
            esquinasZona[1][0] = new Casilla(MARGEN_HOJA_NO_BUSCAR, ALTO_MODELO - MARGEN_HOJA_NO_BUSCAR - getRangoBusquedaEsquinas()); // Rango de búsqueda Inferior Izquierdo, esquinasImagen superior Izquierda del rango
            esquinasZona[1][1] = new Casilla(MARGEN_HOJA_NO_BUSCAR + getRangoBusquedaEsquinas() - ANCHO_PUNTO_ESQUINA, ALTO_MODELO - MARGEN_HOJA_NO_BUSCAR - ANCHO_PUNTO_ESQUINA); // esquinasImagen inferior derecha del rango
            esquinasZona[2][0] = new Casilla(ANCHO_MODELO - MARGEN_HOJA_NO_BUSCAR - getRangoBusquedaEsquinas(), MARGEN_HOJA_NO_BUSCAR); // Rango de búsqueda Superior Derecho, esquinasImagen superior Izquierda del rango
            esquinasZona[2][1] = new Casilla(ANCHO_MODELO - MARGEN_HOJA_NO_BUSCAR - ANCHO_PUNTO_ESQUINA, MARGEN_HOJA_NO_BUSCAR + getRangoBusquedaEsquinas() - ANCHO_PUNTO_ESQUINA); // esquinasImagen inferior derecha del rango
            esquinasZona[3][0] = new Casilla(ANCHO_MODELO - MARGEN_HOJA_NO_BUSCAR - getRangoBusquedaEsquinas(), ALTO_MODELO - MARGEN_HOJA_NO_BUSCAR - getRangoBusquedaEsquinas()); // Rango de búsqueda Inferior Derecho, esquinasImagen superior Izquierda del rango
            esquinasZona[3][1] = new Casilla(ANCHO_MODELO - MARGEN_HOJA_NO_BUSCAR - ANCHO_PUNTO_ESQUINA, ALTO_MODELO - MARGEN_HOJA_NO_BUSCAR - ANCHO_PUNTO_ESQUINA); // esquinasImagen inferior derecha del rango

            // Cargo las casillas del test en activo
            Procesador.setCasillasTest(Procesador.cargaCasillas());

        } catch (FileNotFoundException ex) {
            String error = Procesador.idioma.getString("Configuracion.Error.Fichero.noesta") + "\n" + ex.getMessage() + "\n";
            log.error(error);
            JOptionPane.showOptionDialog(null, error, Procesador.idioma.getString("Error.text"),
                    JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
            return false;
        } catch (NumberFormatException | IOException | ParseException ex) {
            String error = Procesador.idioma.getString("Configuracion.Error.Fichero.formato") + "\n" + ex.getMessage() + "\n";
            log.error(error);
            JOptionPane.showOptionDialog(null, error, Procesador.idioma.getString("Error.text"),
                    JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
            return false;
        }
        return true;
    }

    /**
     * @return the nombreArchivoUltimaImagen
     */
    public static String getNombreArchivoUltimaImagen() {
        return nombreArchivoUltimaImagen;
    }

    /**
     * @param aNombreArchivoUltimaImagen the nombreArchivoUltimaImagen to set
     */
    public static void setNombreArchivoUltimaImagen(String aNombreArchivoUltimaImagen) {
        nombreArchivoUltimaImagen = aNombreArchivoUltimaImagen;
    }

    /**
     * @return the rutaUltimaImagen
     */
    public static String getRutaUltimaImagen() {
        return rutaUltimaImagen;
    }

    /**
     * @param aRutaUltimaImagen the rutaUltimaImagen to set
     */
    public static void setRutaUltimaImagen(String aRutaUltimaImagen) {
        rutaUltimaImagen = aRutaUltimaImagen;
    }

}
