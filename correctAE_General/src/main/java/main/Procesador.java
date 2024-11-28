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

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.RasterFormatException;
import java.awt.image.RescaleOp;
import java.awt.image.ShortLookupTable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Jesus.delBuey
 */
public class Procesador {

    // Mensajes, de AVISO, INFORMCION y de ERROR
    /**
     * Array de tres strings, para transmitir los mensajes de error a la clase
     * llamante. 0 = texto del mensaje, 1 = título del dialogo, 2 = tipo de
     * diálogo/mensaje
     */
    public static String[] mensajesResultado = {"", "", ""};

    // Idioma
    /**
     * idioma. Bundle con las traducciones
     */
    protected static ResourceBundle idioma = ResourceBundle.getBundle("propiedades/Idioma");
    //
    //protected static ResourceBundle configuracion = ResourceBundle.getBundle("propiedades/Configuracion");

    /**
     * Log de la aplicación
     */
    static protected Loguero log = new Loguero();

    /**
     *
     * @return log común para toda la aplicación
     */
    public static Loguero getLog() {
        return log;
    }

    /**
     * Inicializa todo lo necesario para el funcionamiento de la aplicación
     *
     * @return Inicializacon correcta o no.
     */
    public static boolean InicializarAplicacion() {
        // Cargo la configuración, si hay error salgo de la aplicación
        if (!Config.cargarConfiguracion()) {
            return false;
        }

        // Inicializo lo modelo de la tablas de la aplicación
        InicializarModelosAplicacion();

        // Inicializo la base de datos. Alli se comprueba si ya lo está
        BaseDatos.InicializarBaseDatos();
        return true;
    }

    /**
     *
     * @param boton Boton de ayuda al que asociar el evento de teclado F1
     * @param rutaArchivoAyuda Ruta del archivo de ayuda
     */
    public static void asociaAyudaF1(JButton boton, String rutaArchivoAyuda) {
        // Asocio un evento de teclado F1 para lanzar la ayuda
        // Definir la acción que se ejecutará cuando se presione el botón o el atajo de teclado
        Action accionF1 = new AbstractAction("Ayuda") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String error = mostrarAyuda(rutaArchivoAyuda);
                if (!"".equals(error)) {
                    log.error(error);
                    JOptionPane.showOptionDialog(null, error, idioma.getString("Error.text"),
                            JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{idioma.getString("Aceptar.text")}, idioma.getString("Aceptar.text"));
                }
            }
        };
        KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);
        // Asocio la tecla F1 al boton de ayuda
        boton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, "Ayuda");
        boton.getActionMap().put("Ayuda", accionF1);
    }

    /**
     * Nombre de la prueba, examen
     */
    protected static String nombreExamen = "";

    /**
     * Descripción de la prueba, examen
     */
    protected static String descrExamen = "";

    // Lista con los testLeídos
    /**
     * Lista con los test leídos
     */
    protected static List<ModeloTest100> listaTestsLeidos = new ArrayList<>();

    //
    private static BufferedImage imagenTest = null; // Se inicializa al cargar la imagen

    /**
     *
     * @param imagen Imagen que contiene el test actual
     */
    public static void setImagenTest(BufferedImage imagen) {
        imagenTest = imagen;
    }

    /**
     *
     * @return imagenTest
     */
    public static BufferedImage getImagenTest() {
        return imagenTest;
    }

    //
    private static ModeloTest100 testActual = new ModeloTest100("Inicio");

    /**
     *
     * @param test Objeto que tiene el test actual
     */
    public static void setTestActual(ModeloTest100 test) {
        testActual = test;
    }

    /**
     *
     * @return testActual
     */
    public static ModeloTest100 getTestActual() {
        return testActual;
    }

    //
    private static List<Casilla> casillasTest;

    /**
     *
     * @return casillasTest
     */
    public static List<Casilla> getCasillasTest() {
        return casillasTest;
    }

    /**
     *
     * @param casillas Lista de objetos Casilla del modelo de test
     */
    public static void setCasillasTest(List<Casilla> casillas) {
        casillasTest = casillas;
    }

    // Estadísticas de las preguntas de un test
    // Para ir guardando las estadísticas de las preguntas 
    // en la posición 0, aciertos
    // en la 1 fallos, en la 2 blancos, y en la 3 dobles
    // Se crea nueva cada vez que se corrige, en el método CorregiryPuntuar, estadPregs = new int[Config.getNumPreguntas()][4];
    static int[][] estadPregs = new int[Config.getNumPreguntas()][4];

    public static int[][] getEstadPregs() {
        return estadPregs;
    }

    // Estadísticas de las calificaciones de un test
    // Para ir guardando las estadísticas de las frecuencias de las calificaciones 
    // en la posición 0, aciertos
    // en la 1 fallos, en la 2 blancos, y en la 3 dobles
    // Se crea nueva cada vez que se corrige, en el método CorregiryPuntuar, estadPregs = new int[Config.getNumPreguntas()][4];
    static int[] estadNotas;//  = new int[(int)Config.getEscalaCalificacion() + 1];

    public static int[] getEstadNotas() {
        return estadNotas;
    }

    // Estadísticas de aprobados y suspensos
    // Para ir guardando aprobados y suspensos
    // en la posición 0, aprobados
    // en la 1 suspensos
    // Se crea nueva cada vez que se corrige, en el método CorregiryPuntuar
    static int[] estadAprobados = new int[2];

    public static int[] getEstadAprobados() {
        return estadAprobados;
    }

    private static final Dimension pantallaDimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

    /**
     *
     * @return pantallaDimension
     */
    static public Dimension getPantallaDimension() {
        return pantallaDimension;
    }

    private static final double ANCHO_PANTALLA = pantallaDimension.getWidth();

    /**
     *
     * @return ANCHO_PANTALLA
     */
    static public int getAnchoPantalla() {
        return (int) ANCHO_PANTALLA;
    }

    private static final double ALTO_PANTALLA = pantallaDimension.getHeight();

    /**
     *
     * @return ALTO_PANTALLA
     */
    static public int getAltoPantalla() {
        return (int) ALTO_PANTALLA;
    }

    // Métodos
    // Centrar un JFrame
    /**
     *
     * @param cualo JFrame que hay que centrar en la pantalla
     */
    protected static void Centrame(JFrame cualo) {
        cualo.setLocation(getAnchoPantalla() / 2 - (cualo.getWidth() / 2), getAltoPantalla() / 2 - (cualo.getHeight() / 2));
    }

    // Centrar un JDialog
    /**
     *
     * @param cualo JDialog que hay que centrar en la pantalla
     */
    protected static void Centrame(JDialog cualo) {
        cualo.setLocation(getAnchoPantalla() / 2 - (cualo.getWidth() / 2), getAltoPantalla() / 2 - (cualo.getHeight() / 2));
    }

    // Recargo las claves del idioma seleccionado
    /**
     * Recarga el ResourcedBundle para que se actualize el idioma
     */
    protected static void ReCargarIdioma() {
        ResourceBundle.clearCache();
        idioma = ResourceBundle.getBundle("propiedades/Idioma");
        Config.setIdiomaActual(Locale.getDefault().getLanguage());
    }

    // Modelo de la tabla para los tests leídos
    // Campos del test, archivo tiene la ruta completa
    // {"DNI", "Tipo", "Grupo", "Respuestas", "Archivo"};
    /**
     *
     */
    protected static MiModeloTabla modeloTablaTestsLeidos = new MiModeloTabla();

    // Cargo los test leidos en la tabla de tests
    /**
     * Inicializa un modelo nuevo para la tabla de test leídos
     */
    static public void InicializaModeloTablaTestsLeidos() {
        for (int i = 0; i < 6; i++) {
            modeloTablaTestsLeidos.addColumn("col" + i);
        }
        CabecerasModeloTablaTestsLeidos();
    }

    /**
     * Inicializa las cabeceras del modelo de la tabla de test leídos
     */
    static public void CabecerasModeloTablaTestsLeidos() {
        modeloTablaTestsLeidos.setColumnIdentifiers(new String[]{
            idioma.getString("VENTANAINICIO.TABLA_TEST.COLUMNA0"),
            idioma.getString("VENTANAINICIO.TABLA_TEST.COLUMNA1"),
            idioma.getString("VENTANAINICIO.TABLA_TEST.COLUMNA2"),
            idioma.getString("VENTANAINICIO.TABLA_TEST.COLUMNA3"),
            idioma.getString("VENTANAINICIO.TABLA_TEST.COLUMNA4"),
            idioma.getString("VENTANAINICIO.TABLA_TEST.COLUMNA5")});
    }

    // Modelo de la tabla de test de un examen, o prueba
    // Campos del test de un examen
    // {"DNI", "Tipo", "Grupo", "Aciertos", "Fallos", "Blancos", "Dobles", "Nota", "Respuestas"};
    protected static MiModeloTabla modeloTablaTestsCorregidos;

    /**
     * Inicializo los modelos de las tablas de la aplicación
     */
    static public void InicializarModelosAplicacion() {
        // Inicializo el modelo de la tabla de tests a leer
        InicializaModeloTablaTestsLeidos();

        // Inicializo el modelo de la tabla de test corregidos
        InicializaModeloTablaTestCorregidos();

        // Inicializo el modelo de la tabla de tipos y equivalencias, y agrego líneas de ejemplo
        inicializaModeloRespTipos();
        AgregaFilasModeloRespTipos(Config.MAX_NUM_PREGUNTAS);
    }

    // Cargo los test leidos en la tabla de tests
    static public void InicializaModeloTablaTestCorregidos() {
        //CabecerasTablaTestsExamen();
        modeloTablaTestsCorregidos = new MiModeloTabla(new String[]{
            idioma.getString("DialogoEvaluar.tabla.columna0"),
            idioma.getString("DialogoEvaluar.tabla.columna1"),
            idioma.getString("DialogoEvaluar.tabla.columna2"),
            idioma.getString("DialogoEvaluar.tabla.columna3"),
            idioma.getString("DialogoEvaluar.tabla.columna4"),
            idioma.getString("DialogoEvaluar.tabla.columna5"),
            idioma.getString("DialogoEvaluar.tabla.columna6"),
            idioma.getString("DialogoEvaluar.tabla.columna7"),
            idioma.getString("DialogoEvaluar.tabla.columna8"),
            idioma.getString("DialogoEvaluar.tabla.columna9")});
    }

    static public void AgregarTestLeidos() {
        // Añado las filas de la lectura de tests
        // Pongo el id a partir del último de la tabla de test de examen 
        // en lugar del id original de la tabla de test leídos
        int ultId = modeloTablaTestsCorregidos.getRowCount() + 1;
        for (int i = 0; i < modeloTablaTestsLeidos.getRowCount(); i++) {
            Object[] fila = {
                ultId,
                modeloTablaTestsLeidos.getValueAt(i, 1),
                modeloTablaTestsLeidos.getValueAt(i, 2),
                modeloTablaTestsLeidos.getValueAt(i, 3),
                "---", "---", "---", "---", "---",
                modeloTablaTestsLeidos.getValueAt(i, 4)
            };
            modeloTablaTestsCorregidos.addRow(fila);
            ultId++;
        }
    }

    // Modelo de la tabla de tipos y respuestas
    // Campos 
    // {"Pregunta", "Respuesta", "Tipo 1" ... a "Tipo 6" variable};
    private static DefaultTableModel modeloRespTipos;

    /**
     * @return the modeloRespTipos
     */
    public static DefaultTableModel getModeloRespTipos() {
        return modeloRespTipos;
    }

    /**
     * @param aModeloRespTipos the modeloRespTipos to set
     */
    public static void setModeloRespTipos(DefaultTableModel aModeloRespTipos) {
        modeloRespTipos = aModeloRespTipos;
    }

    // Inicializo la tabla al principio
    public static void inicializaModeloRespTipos() {
        // Agregar filas indica si las agrego o no. Al inicio debe hacerse
        // Al cargar un examen, no, ya que agregan al cargar los registros de la BD
        //modeloRespTipos = new DefaultTableModel(cabecerasModeloRespTipos(Config.getNumTipos()), 0);
        modeloRespTipos = nuevoModeloRespTipos(Config.getNumTipos());
    }

    public static DefaultTableModel nuevoModeloRespTipos(int numTipos) {

        // Cabeceras
        String[] cabeceras = new String[numTipos + 2];
        cabeceras[0] = idioma.getString("DialogoTipos.Pregunta.text");
        cabeceras[1] = idioma.getString("DialogoTipos.Respuesta.text");
        // Columnas de los tipos
        for (int i = 1; i <= numTipos; i++) {
            cabeceras[i + 1] = idioma.getString("DialogoTipos.Tipo.text") + " " + i;
        }

        // Tipos de datos en columna
        Class[] tipos = new Class[numTipos + 2];
        for (int i = 0; i < tipos.length; i++) {
            tipos[i] = java.lang.Object.class;
        }

        // Si la columna es editable
        boolean[] editable = new boolean[numTipos + 2];
        editable[0] = false;
        for (int i = 1; i < editable.length; i++) {
            editable[i] = true;
        }

        DefaultTableModel nuevoModelo = new javax.swing.table.DefaultTableModel(
                new Object[][]{}, cabeceras) {

            @Override
            public Class getColumnClass(int columnIndex) {
                return tipos[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return editable[columnIndex];
            }
        };
        return nuevoModelo;
    }

    static public void ajustaTipos(int numTipos) {
        DefaultTableModel modelTemp = nuevoModeloRespTipos(numTipos);
        int ind;

        // Relleno con los datos del modelo actual, hasta donde se pueda
        // El resto columnas nuevas con valor "A"
        for (int fil = 0; fil < modeloRespTipos.getRowCount(); fil++) {
            // Nueva fila
            Object[] nuevaFila = new Object[numTipos + 2];
            nuevaFila[0] = fil + 1;
            nuevaFila[1] = modeloRespTipos.getValueAt(fil, 1);
            for (int tip = 0; tip < numTipos; tip++) {
                ind = tip + 2;
                if (ind < modeloRespTipos.getColumnCount()) {
                    nuevaFila[ind] = modeloRespTipos.getValueAt(fil, ind);
                } else {
                    // Resto de columnas si numTipos es mayor que los que había.
                    nuevaFila[ind] = fil + 1;
                }
            }
            modelTemp.addRow(nuevaFila);
        }
        modeloRespTipos = modelTemp;
    }

    static public void AgregaFilasModeloRespTipos(int numPreguntas) {
        // Agrego las filas.
        Object[] fila = new Object[Config.getNumTipos() + 2];
        for (int i = 1; i <= numPreguntas; i++) {
            fila[0] = modeloRespTipos.getRowCount() + 1;
            fila[1] = "A";
            for (int tipo = 1; tipo <= Config.getNumTipos(); tipo++) {
                fila[tipo + 1] = modeloRespTipos.getRowCount() + 1;
            }
            modeloRespTipos.addRow(fila);
        }
    }

    static public void BorraFilasModeloRespTipos(int preguntas) {
        for (int i = 0; i < preguntas; i++) {
            modeloRespTipos.removeRow(modeloRespTipos.getRowCount() - 1);
        }
    }

    /**
     *
     * @param filaBorrar Fila del modelo que hay que borrar
     */
    static public void BorraFilaModeloTestLeidos(int filaBorrar) {

        modeloTablaTestsLeidos.removeRow(filaBorrar);
        listaTestsLeidos.remove(filaBorrar);
        // Renumero las filas de la tabla
        for (int i = 1; i <= modeloTablaTestsLeidos.getRowCount(); i++) {
            modeloTablaTestsLeidos.setValueAt(i, i - 1, 0);
        }
    }

    // Cargar la ayuda de la página suministrada
    /**
     *
     * @param ruta Ruta del archivo html de ayuda
     * @return error si lo hay
     */
    static public String mostrarAyuda(String ruta) {
        Desktop dt;
        String error = "";

        try {
            if (System.getProperty("os.name").contains("Linux")) {
                // En Linux no funciona getDesktop().browse(lauri), hay que hacerlo
                // así, al menos en Ubuntu y familia
                Runtime.getRuntime().exec(new String[]{"xdg-open", ruta});
            } else {
                // Compruebo que está soportado
                if (Desktop.isDesktopSupported()) {
                    dt = Desktop.getDesktop();
                    // Lanzo el navegador con la ruta del fichero de ayuda
                    if (dt.isSupported(Desktop.Action.BROWSE)) {
                        URI lauri = new URI(ruta);
                        EventQueue.invokeLater(() -> {
                            try {
                                Desktop.getDesktop().browse(lauri);
                            } catch (IOException ex) {
                                log.error(ex.getLocalizedMessage());
                            }
                        });
                    } else {
                        error = idioma.getString("VentanaInicio.errorNavegador.text");
                    }
                }
            }
        } catch (URISyntaxException | IOException ex) {
            log.error(ex.getLocalizedMessage());
            error = ex.getLocalizedMessage();
        }

        return error;
    }

    // Inicializo la tabla al principio
    static public void CargaEquivalenciasyTipos(int idExamen) {
        // Pongo false porque al cargar un examen no deben ponerse
        // ya que se agregan al cargar los registros de la BD
        inicializaModeloRespTipos();
        // Elimino las filas que hubira, antes de cargar las grabadas en la BD
        modeloRespTipos.setRowCount(0);
        BaseDatos.CargaEquivalencias(modeloRespTipos, idExamen);
    }

    static public MiModeloTabla CargaTestsExamen(int idExamen) {
        return BaseDatos.CargaTestsExamen(modeloTablaTestsCorregidos, idExamen);
    }

    /**
     *
     * @param fichero Fichero a cargar
     * @param modelo modelo a rellenar
     * @param topeCampos Máximo número de campos por linea del fichero
     * @param topeFilas Máximo número de test admitidos
     * @param tipoImportacion Tipo de archivo CSV a importar, tipos y
     * equivalencias ó tests corregidos
     * @return Mensajes de error o lo que sea
     */
    static public String[] importarCSV(File fichero, DefaultTableModel modelo, int topeCampos, int topeFilas, int tipoImportacion) {
        int lineaNum;
        DefaultTableModel modeloTmp;

        // Si es un tipo admitido sigo, ya se han formateado las cabeceras del modelo correctamente
        mensajesResultado = new String[]{"", "", ""};
        // Cargo las respuestas correctas y las equivalencias desde un csv
        try (Scanner lector = new Scanner(new FileInputStream(fichero))) {
            // .replace(Config.DELIMITADOR_TEXTO_CSV, "") (Config.getNumTipos() + 2)
            String[] cabeceraFich = lector.nextLine().replace(Config.DELIMITADOR_TEXTO_CSV, "").split(Config.SEPARADOR_CSV, topeCampos);
            // Compruebo la cabecera y si es correcta me la salto y continúo
            // Debe venir un string con el primer nombre de la columna del modelo, luego el resto
            // Modelo temporal

            switch (tipoImportacion) {
                case Config.CSV_TIPOS_EQUIVALENCIAS:
                    // 6 tipos máximo mas la columna respuesta
                    if (cabeceraFich.length > 1 && cabeceraFich.length < 8) // Formateto el modelo con el número de tipos que tenga la cabecera
                    {
                        modeloTmp = nuevoModeloRespTipos(cabeceraFich.length - 1);
                        modeloRespTipos = modeloTmp;
                    } else {
                        log.error(idioma.getString("Procesador.error.tipo_csv_no_admitido.text"));
                        mensajesResultado[0] = "Procesador.error.tipo_csv_no_admitido.text";
                        mensajesResultado[1] = "Error.text";
                        mensajesResultado[2] = String.valueOf(JOptionPane.ERROR_MESSAGE);
                        return mensajesResultado;
                    }
                    break;

                case Config.CSV_TEST_CORREGIDOS:
                    // Cabecera examenes tests corregidos ya está creada. borro las filas que tenga
                    modeloTmp = modeloTablaTestsCorregidos;
                    modeloTmp.setRowCount(0);
                    break;

                default:
                    // No es un tipo de importación admitido
                    log.error(idioma.getString("Procesador.error.tipo_csv_no_admitido.text"));
                    mensajesResultado[0] = "Procesador.error.tipo_csv_no_admitido.text";
                    mensajesResultado[1] = "Error.text";
                    mensajesResultado[2] = String.valueOf(JOptionPane.ERROR_MESSAGE);
                    return mensajesResultado;
            }

            // Compruebo que el nombre deL primer campo del CSV al menos coincida con el primero del modelo, columna 1
            if (cabeceraFich[0].contains(modelo.getColumnName(1)) && cabeceraFich.length <= topeCampos) {
                // leo el resto de lineas
                lineaNum = 1;
                while (lector.hasNextLine()) {
                    // Separo los campos delimitados por el separador y quito las comillas dobles
                    String[] linea = lector.nextLine().replace(Config.DELIMITADOR_TEXTO_CSV, "").split(Config.SEPARADOR_CSV, topeCampos);
                    // Si está en los límites se agrega, si no error
                    // y la respuesta tiene un valor válido
                    if (linea.length > 1) {
                        //Agrego el número de línea
                        String[] fila = new String[linea.length + 1];
                        fila[0] = String.valueOf(lineaNum);
                        System.arraycopy(linea, 0, fila, 1, linea.length);
                        modeloTmp.addRow(fila);
                        if (lineaNum <= topeFilas) {
                            lineaNum++;
                        } else {
                            // Se alcanzó el límite de preguntas, NO leo más
                            log.error(idioma.getString("DialogoTipos.error.limite_preguntas_alcanzado.text"));
                            mensajesResultado[0] = "DialogoTipos.error.limite_preguntas_alcanzado.text";
                            mensajesResultado[1] = "Atencion.text";
                            mensajesResultado[2] = String.valueOf(JOptionPane.WARNING_MESSAGE);
                            return mensajesResultado;
                        }
                    } else {
                        log.error(idioma.getString("DialogoTipos.error.leer_csv.text") + (lineaNum + 1));
                        mensajesResultado[0] = "DialogoTipos.error.error.leer_csv.text";
                        mensajesResultado[1] = "Atencion.text";
                        mensajesResultado[2] = String.valueOf(JOptionPane.WARNING_MESSAGE);
                        return mensajesResultado;
                    }
                }
                // Ha terminado la importación correctamente
                mensajesResultado[0] = "DialogoTipos.teminado.leer_csv.text";
                mensajesResultado[1] = "Atencion.text";
                mensajesResultado[2] = String.valueOf(JOptionPane.INFORMATION_MESSAGE);
                return mensajesResultado;

            } else {
                // Falta la cabecera o está incompleta
                log.error(idioma.getString("DialogoTipos.error.leer_csv.text"));
                mensajesResultado[0] = "DialogoTipos.error.leer_csv.text";
                mensajesResultado[1] = "Error.text";
                mensajesResultado[2] = String.valueOf(JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            log.error("Error de formato en el fichero csv - " + ex.getMessage());
            mensajesResultado[0] = "DialogoTipos.error.leer_csv.text";
            mensajesResultado[1] = "Error.text";
            mensajesResultado[2] = String.valueOf(JOptionPane.ERROR_MESSAGE);
        } catch (FileNotFoundException ex) {
            log.error("No existe el fichero csv - " + ex.getMessage());
            mensajesResultado[0] = "DialogoTipos.error.no_existe_csv.text";
            mensajesResultado[1] = "Error.text";
            mensajesResultado[2] = String.valueOf(JOptionPane.ERROR_MESSAGE);
        }
        return mensajesResultado;
    }

    /**
     *
     * @param fichero Fichero donde guardar los datos
     * @param modelo modelo donde están los datos
     * @return Array de strings con mensaje, título y código de tipo de mensaje
     */
    static public String[] exportarCSV(File fichero, DefaultTableModel modelo) {
        String linea;
        Object valor;

        mensajesResultado = new String[]{"", "", ""};
        // false para borre el anterior, no añada. Siempre fiechero nuevo
        try (FileWriter escri = new FileWriter(fichero, false)) {
            // Escribo la cabecera, con todos los nombres de las columnas del modelo, menos la primera, el número de línea no se guarda
            linea = "";
            for (int numCol = 1; numCol < modelo.getColumnCount() - 1; numCol++) {
                linea = linea.concat("\"").concat(modelo.getColumnName(numCol)).concat("\"");
                // si no es el fin de linea añado un separador
                linea = linea.concat(Config.SEPARADOR_CSV);
            }
            // Añado la cabecera del último tipo, sin el ;
            linea = linea.concat("\"").concat(modelo.getColumnName(modelo.getColumnCount() - 1)).concat("\"").concat(Config.CARACTER_NUEVA_LINEA);
            escri.write(linea);
            // Escribo las líneas
            for (int numFila = 0; numFila < modelo.getRowCount(); numFila++) {
                linea = "";
                for (int numCol = 1; numCol < modelo.getColumnCount() - 1; numCol++) {
                    valor = modelo.getValueAt(numFila, numCol);
                    valor = valor == null ? "" : valor;
                    linea = linea.concat("\"").concat(valor.toString()).concat("\"");
                    linea = linea.concat(Config.SEPARADOR_CSV);
                }
                valor = modelo.getValueAt(numFila, modelo.getColumnCount() - 1);
                valor = valor == null ? "" : valor;
                linea = linea.concat("\"").concat(valor.toString()).concat("\"").concat(Config.CARACTER_NUEVA_LINEA);
                escri.write(linea);
            }
            mensajesResultado[0] = "DialogoTipos.teminado.escribir_csv.text";
            mensajesResultado[1] = "Atencion.text";
            mensajesResultado[2] = String.valueOf(JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            log.error(ex.getMessage());
            mensajesResultado[0] = "DialogoTipos.error.escribir_csv.text";
            mensajesResultado[1] = "Error.text";
            mensajesResultado[2] = String.valueOf(JOptionPane.ERROR_MESSAGE);
        }
        return mensajesResultado;
    }

    static public boolean leerTest(File fichero) {

        cargarImagen(fichero);
        // Corrijo la imagen
        if (getImagenTest() != null) {
            try {
                setImagenTest(corrigeImagen(getImagenTest()));
            } catch (RasterFormatException ex) {
                // Error Buscando esquinas, u otro. Pegunto si continuar. El último parámetro es el botón npor defecto
                return false;
            }
            getTestActual().setCasillasMarcadas(analizar(getImagenTest(), getTestActual()));
            if (getTestActual().getCasillasMarcadas() != null) {
                // Analizo los campos y almaceno el resultado en cada uno de ellos
                setTestActual(extraeResultadosCampos(getTestActual(), getCasillasTest()));
                // Actualizo el modelo de la tabla de test leidos, usado en VentanaInicio
                actualizaTablaTest(listaTestsLeidos, getTestActual());
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return Cantidad de preguntas anuladas
     */
    static public int numPreguntasNulas() {
        int canceladas = 0;

        for (int i = 0; i < Procesador.getModeloRespTipos().getRowCount(); i++) {
            String valor = Procesador.getModeloRespTipos().getValueAt(i, 1).toString();
            // El valor de ser A..E, si la logitud del valor es mayor que 1 anulo la pregunta, Pondrá anular
            if (valor.length() > 1) {
                canceladas++;
            }
        }
        return canceladas;
    }

    /**
     *
     * @return Mensajes de error, si los hay
     */
    static public String[] CorregiryPuntuar() {
        String respuestaDelTest;
        int tipo, equiv, aciertos, fallos, blancos, dobles, indice;
        float nota;

// Actualizo los valores de Tipos y preguntas
        Config.setNumPreguntas(Procesador.getModeloRespTipos().getRowCount());
        Config.setNumPreguntasValidas(Config.getNumPreguntas() - numPreguntasNulas());

// Vacío los mensajes, por si no hay error
        mensajesResultado = new String[]{"", "", ""};
        // Para ir guardando las estadísticas de las preguntas en la 
        // pos 0, aciertos, 1 fallos, en la 2 blancos, y en la 3 dobles
        // de la 4 a la 10, cantidad de alumnos que responden la A..E
        estadPregs = new int[Config.getNumPreguntas()][9];

        // Paso a porcentaje el valor que venga
        estadNotas = new int[(int) Math.ceil(Config.getEscalaCalificacion()) + 1];

        estadAprobados = new int[2];
        int posicionAE;
        char responde, respCorrecta_char;
        String respuestaCorrecta;
        if (modeloRespTipos.getRowCount() > 0) {
            for (int fila = 0; fila < modeloTablaTestsCorregidos.getRowCount(); fila++) {
                aciertos = 0;
                fallos = 0;
                blancos = 0;
                dobles = 0;
                respuestaDelTest = modeloTablaTestsCorregidos.getValueAt(fila, modeloTablaTestsCorregidos.getColumnCount() - 1).toString();
                try {
                    // Extraigo el tipo, si es un número del 1 al máximo configurado corrijo si no, NO.
                    tipo = Integer.parseInt(modeloTablaTestsCorregidos.getValueAt(fila, 2).toString());
                    if (tipo > 0 && tipo <= Config.getNumTipos()) {
                        for (int preg = 0; preg < Config.getNumPreguntas(); preg++) {
                            // Extraigo la Equivalencia
                            // La equivalencia está en la columna 2 para el tipo uno y así sucesivamente,
                            // por tanto tipo + 1
                            equiv = Integer.parseInt(modeloRespTipos.getValueAt(preg, tipo + 1).toString());
                            // Si la equivalencia está en el rango de preguntas, corrijo, si no NO. Guiones
                            if (equiv > 0 && equiv <= Config.getNumPreguntas()) {
                                // La primera fila en la tabla es la 0, que es la pregunta 1
                                equiv--;
                                // Respuesta correcta segun la equivalencia respecto al tipo Maestro
                                respuestaCorrecta = modeloRespTipos.getValueAt(equiv, 1).toString();
                                // Si la pregunta NO está anulada sigo, en caso contrario no apunto nada
                                if (!idioma.getString("DialogoTipos.Anular.text").equals(respuestaCorrecta)) {
                                    responde = respuestaDelTest.charAt(preg);
                                    if (responde == '?') {
                                        dobles++;
                                        estadPregs[equiv][3]++;
                                    } else {
                                        if (responde == ' ') {
                                            blancos++;
                                            estadPregs[equiv][2]++;
                                        } else {
                                            // Tomo el carácter inicial, que es la respuesta correcta
                                            respCorrecta_char = respuestaCorrecta.charAt(0);
                                            // Apunto que respuesta ha puesto
                                            posicionAE = "ABCDE".indexOf(responde) + 4; // 4 es la posicion en el array donde estan la respuestas a "A"
                                            estadPregs[equiv][posicionAE]++;
                                            if (responde == respCorrecta_char) {
                                                aciertos++;
                                                estadPregs[equiv][0]++;
                                            } else {
                                                fallos++;
                                                estadPregs[equiv][1]++;
                                            }
                                        }
                                    }
                                }
                            } else {
                                // La equivalencia no es válida, pongo guiones y paso al test siguiente
                                tipoEquivNoValidos(fila);
                                break;
                            }
                        }
                        // Pongo los resultados en la tabla
                        modeloTablaTestsCorregidos.setValueAt(aciertos, fila, 4);
                        modeloTablaTestsCorregidos.setValueAt(fallos, fila, 5);
                        modeloTablaTestsCorregidos.setValueAt(blancos, fila, 6);
                        modeloTablaTestsCorregidos.setValueAt(dobles, fila, 7);
                        // Calculo y pongo la nota
                        nota = (aciertos * Config.getPuntosAcierto()
                                - ((fallos) * Config.getPuntosFallo())
                                - ((blancos) * Config.getPuntosBlanco())
                                - ((dobles) * Config.getPuntosDoble()))
                                / (float) Config.getNumPreguntasValidas()
                                * Config.getEscalaCalificacion();
                        //
                        modeloTablaTestsCorregidos.setValueAt(String.format("%.2f", nota), fila, 8);
                        // Apunto la estadística de aprobados
                        if (nota >= Config.getPuntAprobado()) {
                            estadAprobados[0]++;
                        } else {
                            estadAprobados[1]++;
                        }
                        // Sólo hay 10 rangos. Si la nota es negativa, pongo 0
                        indice = nota < 0 ? 0 : (int) nota;
                        estadNotas[indice]++;
                    } else {
                        // El tipo no es válido y Apunto guiones en la tabla
                        tipoEquivNoValidos(fila);
                    }
                } catch (NumberFormatException ex) {
                    // Si da error al extraer el tipo, o la equivalencia, una interrogación por doble marca, pongo guiones en la tabla y NO evalúo
                    tipoEquivNoValidos(fila);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    log.error(ex.getLocalizedMessage());
                }

            }
        } else {
            mensajesResultado[0] = "Procesador.plantilla.error.tiposYequivalencias.text";
            mensajesResultado[1] = "Error.text";
            mensajesResultado[2] = String.valueOf(JOptionPane.ERROR_MESSAGE);
        }
        return mensajesResultado;
    }

    // Método que cuando el tipo no es válido, apunta guiones en la tabla
    static private void tipoEquivNoValidos(int enFila) {
        // Si da error en el tipo pongo guiones en la tabla y NO evalúo
        modeloTablaTestsCorregidos.setValueAt("---", enFila, 4);
        modeloTablaTestsCorregidos.setValueAt("---", enFila, 5);
        modeloTablaTestsCorregidos.setValueAt("---", enFila, 6);
        modeloTablaTestsCorregidos.setValueAt("---", enFila, 7);
        modeloTablaTestsCorregidos.setValueAt("---", enFila, 8);
    }

    /**
     *
     * @param img Imagen que hay que ajustaImagen, enderazar
     * @return La imagen corregida
     */
    static public BufferedImage corrigeImagen(BufferedImage img) throws RasterFormatException {
        Casilla[] esquinasImagen;

        if (img != null) {
            //img = (recorteMargenesConfigurados(img));

            esquinasImagen = buscaEsquinas(img, Config.esquinasZona); // Busco las esquinas de la imagen por los puntos de referencia. NO pinto cuadrados
            img = (ajustaImagen(img, esquinasImagen));  // Enderezo la imagen, contrasto más.
            esquinasImagen = buscaEsquinas(img, Config.esquinasZona); // Busco las esquinas de la imagen YA enderezada
            img = (blanqueaMargenes(img));
            // Imagen recortada. Toma desde la cordenada SI y ancho y alto hasta la cordenada ID
            img = recortarXpunEsquinas(img, esquinasImagen);
            // Reescalo de nuevo, tras el recorte, a un tamaño de 1000 x 1415, está definido en Principal
            // En el escalado prograsivo sólo uso el lado largo como referencia
            img = cambiaTamano(img, Config.ALTO_MODELO);
            // Devuelvo la imagen corregida
            return img;
        }
        return null;
    }

    /**
     *
     * @param img
     * @param esquinasImagen
     * @return
     */
    static public BufferedImage ajustaImagen(BufferedImage img, Casilla[] esquinasImagen) {
        double anguloDesvio;

        anguloDesvio = anguloGriro(esquinasImagen);
        if (img != null && anguloDesvio != 0) {
            AffineTransform transform = AffineTransform.getRotateInstance(Math.toRadians(anguloDesvio),
                    esquinasImagen[0].getCoordX(), esquinasImagen[0].getCoordY());
            // Roto desde la esquina Superior Izquierda, en lugar de desde el centro de la imagen
            AffineTransformOp op = new AffineTransformOp(transform,
                    AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            // De menos a más calidad (y tiempo de CPU)
            // nearest neighbor, bilinear interpolation, bicubic interpolation
            img = filter(op, img);
            return img;
        }
        return img;
    }
        
    /**
     *
     * @param img
     * @param esquinasZona
     * @return
     * @throws RasterFormatException
     */
    static public Casilla[] buscaEsquinas(BufferedImage img, Casilla[][] esquinasZona) throws RasterFormatException {
        // Busco las esquinas, enderezo y aplico algo de contraste, y recorto
        Casilla[] esquinas = new Casilla[4]; // Coordenadas de las esquinas de la imagen
        Casilla coorPunto;

        for (int fila = 0; fila < 4; fila++) {
            //System.out.printf("%s  %s\n", "Buscando esquina ", nomEsquina[fila]);
            //log.info("Buscando esquina " + nomEsquina[fila]);
            coorPunto = buscaUnaEsquina(img, esquinasZona[fila][0], esquinasZona[fila][1], Config.ANCHO_PUNTO_ESQUINA, Config.getUmbralDeteccionEsquina());
            if (coorPunto != null) {
                // Guardo las coordenadas donde encontró el punto de esquina
                esquinas[fila] = new Casilla(coorPunto.getCoordX(), coorPunto.getCoordY());
            } else {
                // No encontró el punto
                esquinas[fila] = new Casilla(0, 0);
            }
        }
        return esquinas;
    }

    /**
     *
     * @param img
     * @param esquinaDesde
     * @param esquinaHasta
     * @param anchoBusqueda
     * @param limite
     * @return
     * @throws RasterFormatException
     */
    static public Casilla buscaUnaEsquina(BufferedImage img, Casilla esquinaDesde, Casilla esquinaHasta, int anchoBusqueda, int limite) throws RasterFormatException {
        Casilla aqui;
        int brilloZona;

        for (int x = esquinaDesde.getCoordX(); x < esquinaHasta.getCoordX(); x++) {
            for (int y = esquinaDesde.getCoordY(); y < esquinaHasta.getCoordY(); y++) {
                BufferedImage resp = img.getSubimage(x, y, anchoBusqueda, anchoBusqueda);
                brilloZona = brilloRespuesta(resp);
                if (brilloZona < limite) {
                    aqui = new Casilla(x, y);
                    return aqui;
                }
            }
        }
        return null;
    }

    /**
     *
     * @param esquinasImagen
     * @return
     */
    static public double anguloGriro(Casilla[] esquinasImagen) {
        // Las coordenadas vienen SI, II, SD y ID

        if (esquinasImagen != null) {
            Casilla[] esquinas = esquinasImagen;
            int catetoLargo = esquinas[0].getCoordY() - esquinas[1].getCoordY();
            int catetoCorto = esquinas[0].getCoordX() - esquinas[1].getCoordX();
            double angulo = Math.atan(catetoCorto / (double) catetoLargo);
            return Math.toDegrees(angulo);
        }
        return 0;
    }

    /**
     *
     * @param img imagen original
     * @return imagen recortada según la configuración
     * @throws RasterFormatException
     */
    static public BufferedImage recorteMargenesConfigurados(BufferedImage img) throws RasterFormatException {
        // Devuelvo la imagen recortada entre los puntos de coordenada
        return img.getSubimage(Config.getMargenIzquierdoHojaRecortar(), Config.getMargenSuperiorHojaRecortar(),
                img.getWidth() - Config.getMargenDerechoHojaRecortar() - Config.getMargenIzquierdoHojaRecortar(),
                img.getHeight() - Config.getMargenSuperiorHojaRecortar());
    }

    /**
     *
     * @param img
     * @param esquinasImagen
     * @return
     * @throws RasterFormatException
     */
    static public BufferedImage recortarXpunEsquinas(BufferedImage img, Casilla[] esquinasImagen) throws RasterFormatException {
        // Devuelvo la imagen recortada entre los puntos de coordenada
        return img.getSubimage(esquinasImagen[0].getCoordX() + Config.getMargenIzquierdoHojaRecortar(), esquinasImagen[0].getCoordY() - Config.getMargenSuperiorHojaRecortar(),
                esquinasImagen[2].getCoordX() - esquinasImagen[0].getCoordX() + Config.ANCHO_PUNTO_ESQUINA + Config.MARGEN_HOJA_NO_BUSCAR - Config.getMargenDerechoHojaRecortar(),
                esquinasImagen[3].getCoordY() - esquinasImagen[2].getCoordY() - Config.ANCHO_PUNTO_ESQUINA);
    }

// Métodos del programa origen, para probar...
    /**
     *
     * @param x
     * @param y
     */
    static public void apuntaCoords(int x, int y) {
        String caminoCasillas = Config.getFicheroCasillasTest();
        // Si existe el fichero no lo crea

        try (FileWriter fich = new FileWriter(caminoCasillas, true)) {
            fich.append(String.valueOf(x) + Config.SEPARADOR_CSV + String.valueOf(y) + "\r\n");
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    /**
     *
     * @param img
     * @return
     */
    static public BufferedImage blur(BufferedImage img) {
        float weight = 1.0f / 9.0f;
        float[] elements = new float[9];
        for (int i = 0; i < 9; i++) {
            elements[i] = weight;
        }
        return convolve(elements, img);
    }

    /**
     *
     * @param img
     * @return
     */
    static public BufferedImage sharpen(BufferedImage img) {
        float[] elements = {0.0f, -1.0f, 0.0f, -1.0f, 5.f, -1.0f, 0.0f, -1.0f, 0.0f};
        return convolve(elements, img);
    }

    /**
     *
     * @param img
     * @return
     */
    static public BufferedImage brighten(BufferedImage img) {
        //float a = 1.1f; Abrillanta
        // Oscurece
        float a = 0.9f;
        // float b = 20.0f;
        float b = 0;
        RescaleOp op = new RescaleOp(a, b, null);
        return filter(op, img);
    }

    /**
     *
     * @param img
     * @return
     */
    static public BufferedImage edgeDetect(BufferedImage img) {
        float[] elements = {0.0f, -1.0f, 0.0f, -1.0f, 4.f, -1.0f, 0.0f, -1.0f, 0.0f};
        return convolve(elements, img);
    }

    /**
     *
     * @param img
     * @return
     */
    static public BufferedImage negative(BufferedImage img) {
        short[] negative = new short[256 * 1];
        for (int i = 0; i < 256; i++) {
            negative[i] = (short) (255 - i);
        }
        ShortLookupTable table = new ShortLookupTable(0, negative);
        LookupOp op = new LookupOp(table, null);
        return filter(op, img);
    }

    /**
     *
     * @param img
     * @return
     */
    static public BufferedImage rotate(BufferedImage img) {
        if (img == null) {
            return null;
        }
        AffineTransform transform = AffineTransform.getRotateInstance(Math.toRadians(5),
                img.getWidth() / 2, img.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(transform,
                AffineTransformOp.TYPE_BICUBIC);
        return filter(op, img);
    }

    /**
     * Apply a filter
     *
     * @param op the image operation to apply
     */
    static private BufferedImage filter(BufferedImageOp op, BufferedImage img) {
        if (img == null) {
            return null;
        }
        return op.filter(img, null);
    }

    /**
     * Apply a convolution
     *
     * @param elements the convolution kernel (an array of 9 matrix elements)
     */
    static private BufferedImage convolve(float[] elements, BufferedImage img) {
        Kernel kernel = new Kernel(3, 3, elements);
        ConvolveOp op = new ConvolveOp(kernel);
        return filter(op, img);
    }

    /**
     *
     * @param img
     * @return Imagen con los márgenes blanqueados
     */
    static public BufferedImage blanqueaMargenes(BufferedImage img) {
        BufferedImage imag = img;
        Color color;
        Color blanco = new Color(255, 255, 255); // Color Blanco
        int rgbBlanco = blanco.getRGB();
        int inicioMargen = imag.getWidth() - Config.MARGEN_HOJA_RECORTAR;

        for (int x = 0; x < Config.MARGEN_HOJA_RECORTAR; x++) {
            for (int y = 0; y < imag.getHeight(); y++) {
                // Margen Izquierdo
                color = new Color(imag.getRGB(x, y));
                if (color.getBlue() < Config.getLimiteNegroMargen()) {
                    imag.setRGB(x, y, rgbBlanco);
                }
                // Margen Derecho
                color = new Color(imag.getRGB(x + inicioMargen, y));
                if (color.getBlue() < Config.getLimiteNegroMargen()) {
                    imag.setRGB(x + inicioMargen, y, rgbBlanco);
                }
            }
        }
        // Margen Inferior
        for (int y = imag.getHeight() - Config.MARGEN_HOJA_RECORTAR; y < imag.getHeight(); y++) {
            for (int x = 0; x < imag.getWidth(); x++) {
                color = new Color(imag.getRGB(x, y));
                if (color.getBlue() < Config.getLimiteNegroMargen()) {
                    imag.setRGB(x, y, rgbBlanco);
                }
            }
        }
        return imag;
    }

    // Hago un cambio de escala progresivo, que da más calidad
    //
    /**
     *
     * @param imagenOriginal
     * @param ladoLargo
     * @return Imagen resultado
     */
    public static BufferedImage cambiaTamano(BufferedImage imagenOriginal, Integer ladoLargo) {
        if (imagenOriginal != null) {
            Integer w = imagenOriginal.getWidth();
            Integer h = imagenOriginal.getHeight();

            double ratio = h > w ? ladoLargo.doubleValue() / h : ladoLargo.doubleValue() / w;

            // Cambio de tamaño basado en la técnica descrita por Chris Campbell’s en blog The Perils of Image.getScaledInstance(). 
            // Comentario original: As Chris mentions, when downscaling to something
            // less than factor 0.5, you get the best result by doing multiple downscaling
            // with a minimum factor of 0.5 (in other words: each scaling operation should scale to maximum half the size).
            while (ratio < 0.5) {
                imagenOriginal = scaleProg(imagenOriginal, 0.5);
                //imagenOriginal = tmp;
                w = imagenOriginal.getWidth();
                h = imagenOriginal.getHeight();
                ratio = h > w ? ladoLargo.floatValue() / h : ladoLargo.floatValue() / w;
            }
            return scaleProg(imagenOriginal, ratio);
        }
        return null;
    }

    private static BufferedImage scaleProg(BufferedImage imageToScale, Double ratio) {
        Integer dWidth = ((Double) (imageToScale.getWidth() * ratio)).intValue();
        Integer dHeight = ((Double) (imageToScale.getHeight() * ratio)).intValue();
        BufferedImage scaledImage = new BufferedImage(dWidth, dHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(imageToScale, 0, 0, dWidth, dHeight, null);
        graphics2D.dispose();
        return scaledImage;
    }
    //  

    /**
     *
     * @param img
     * @param esquinas
     * @param anchoPunto
     * @return Imagen con las esquinas pintadas en rojo
     */
    static public BufferedImage pintaEsquinas(BufferedImage img, Casilla[] esquinas, int anchoPunto) {
        // Busco las esquinas, enderezo y aplico algo de contraste, y recorto
        BufferedImage imag = img;

        if (imag != null) {
            Graphics g = imag.getGraphics();
            g.setColor(Color.RED);
            for (int fila = 0; fila < 4; fila++) {
                g.drawRect(esquinas[fila].getCoordX(), esquinas[fila].getCoordY(), anchoPunto, anchoPunto);
            }
            return imag;
        } else {
            return null;
        }
    }

    static public int brilloRespuesta(BufferedImage img) {
        int nivelGris;
        Color queColor;

        nivelGris = 0;
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                queColor = new Color(img.getRGB(x, y));
                // Como es en escala de grises, los tres colores tienen el mismo valor
                nivelGris += queColor.getRed();
            }
        }
        nivelGris = nivelGris / (img.getWidth() * img.getHeight());
        return nivelGris;
    }

    /**
     * Analiza todas las casillas de un test dado
     *
     * @param img Imagen a analizar
     * @param elTest el test y su estructura
     * @return Una lista con las casillas marcadas
     */
    static public List<Casilla> analizar(BufferedImage img, ModeloTest100 elTest) {
        /* Analiza el brillo alrededor de la coordenada (ésta es el centro), en 
         * un área cuadrada del ancho definido en Principal.anchoMarcasRespuesta
         */
        List<Casilla> marcas = new ArrayList<>();

        int brilloResp;
        int mitadAncho = Config.getAnchoMarcasRespuesta() / 2;
        if (img != null) {
            for (Casilla pun : getCasillasTest()) {
                try {
                    BufferedImage resp = img.getSubimage(pun.getCoordX() - mitadAncho, pun.getCoordY()
                            - mitadAncho, Config.getAnchoMarcasRespuesta(), Config.getAnchoMarcasRespuesta());
                    brilloResp = brilloRespuesta(resp);
                    // Guardo la coordenada original, el centro del cuadrado
                    if (brilloResp < Config.getUmbralDeteccionMarca()) {
                        pun.setMarcada(true);
                        marcas.add(pun);
                    } else {
                        pun.setMarcada(false);
                    }
                } catch (RasterFormatException ex) {
                    log.error("Error en Procesador.analizar\n" + ex.getMessage());
                }
            }
        }
        return marcas;
    }

    /**
     * @param losTests Lista con los test del modelo en vigor
     * @param elTest Test, cuyos datos se añadiran a la tabla de resultados
     */
    static public void actualizaTablaTest(List<ModeloTest100> losTests, ModeloTest100 elTest) {
        // Añado los resultados en el modelo de datos de la tabla de ventanaInicio
        String[] linea = new String[elTest.getCamposTest().size() + 2];
        // Ruta y nombre del archivo, último campo
        linea[linea.length - 1] = elTest.getNombreArchivo();
        // el id es el primero, luego Respuestas, NIFNumero, NIFLetra, Tipo y Grupo
        linea[0] = String.valueOf(modeloTablaTestsLeidos.getRowCount() + 1);
        // NIF Completo, número y letra
        linea[1] = elTest.getCamposTest().get(1).getResultado() + "-" + elTest.getCamposTest().get(2).getResultado();
        // Preguntas. Las pongo al final
        linea[4] = elTest.getCamposTest().get(0).getResultado();
        // Tipo
        linea[2] = elTest.getCamposTest().get(3).getResultado();
        // Grupo
        linea[3] = elTest.getCamposTest().get(4).getResultado();
        // Nombre Archivo
        linea[5] = elTest.getNombreArchivo();
        //
        modeloTablaTestsLeidos.addRow(linea);
        // Agrego el test a la lista de idem
        losTests.add(elTest);
    }

    static public String[] restauraCasillasTestInicial() {
        // Copia todas las líneas del archivo original sobre el que está en vigor
        mensajesResultado = new String[]{"", "", ""};
        
        try (FileInputStream inputStream = new FileInputStream(Procesador.class
                .getClassLoader().getResource(Config.getFicheroCasillasTestInicial()).getFile());
                FileOutputStream outputStream = new FileOutputStream(Procesador.class
                .getClassLoader().getResource(Config.getFicheroCasillasTest()).getFile())) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            log.aviso(idioma.getString("Procesador.ficheroCasillasRestarurado.text"));
        } catch (IOException e) {
            log.aviso(idioma.getString("Procesador.ficheroCasillasError.text"));
            mensajesResultado[0] = "Procesador.ficheroCasillasError.text";
            mensajesResultado[1] = "Error.text";
            mensajesResultado[2] = String.valueOf(JOptionPane.ERROR_MESSAGE);
        }
        return mensajesResultado;
    }

    static public List<Casilla> cargaCasillas() {
        int x, y;
        List<Casilla> lasCasillasTest = new ArrayList<>();
        InputStream casillas = Procesador.class
                .getClassLoader().getResourceAsStream(Config.getFicheroCasillasTest());

        // Cargo todas las casillas (puntos con dos coordenadas) del Test 
        try (Scanner lector = new Scanner(casillas)) {
            // necesito un string
            while (lector.hasNextLine()) {
                String[] coordXY = lector.nextLine().split(Config.SEPARADOR_CSV, 2);
                x = Integer.parseInt(coordXY[0]);
                y = Integer.parseInt(coordXY[1]);
                lasCasillasTest.add(new Casilla(x, y));
            }
            return lasCasillasTest;
        } catch (NumberFormatException ex) {
            log.error(idioma.getString("Procesador.plantilla.error.leyendo.text") + " - " + ex.getMessage());
            mensajesResultado[0] = "Procesador.plantilla.error.leyendo.text";
            mensajesResultado[1] = "Error.text";
            mensajesResultado[2] = String.valueOf(JOptionPane.ERROR_MESSAGE);
        }
        return null;

    }

    static String[] guardaCasillas(ArrayList<JCheckBox> lasCajas) {
        // Guardo las CAJAS del Test 
        File fich = new File(Procesador.class
                .getClassLoader().getResource(Config.getFicheroCasillasTest()).getFile());
        String linea;
        int ajuste = 8;

        if (!lasCajas.isEmpty()) {
            ajuste = lasCajas.get(0).getWidth() / 2 + 2;
        }
        mensajesResultado = new String[]{"", "", ""};
        if (!lasCajas.isEmpty()) {
            // false para borre el anterior, no añada
            try (FileWriter escri = new FileWriter(fich, false)) {
                for (JCheckBox caja : lasCajas) {
                    // Al pintar las cajas le sumé 2 para que salgan centradas, como las casillas. Ahora se lo quito al guardarlas  
                    //System.out.println("Ancho caja = " + caja.getWidth());
                    linea = String.valueOf((int) caja.getX() + ajuste) + Config.SEPARADOR_CSV + String.valueOf((int) caja.getY() + ajuste) + "\n";
                    escri.write(linea);
                }
                // Recargo las casillas, para que se actualicen
                List<Casilla> tales = cargaCasillas();
                if (tales != null) {
                    setCasillasTest(tales);
                }
                // Si no error
            } catch (IOException ex) {
                log.error(ex.getMessage());
                mensajesResultado[0] = "Procesador.plantilla.error.escribiendo.text";
                mensajesResultado[1] = "Error.text";
                mensajesResultado[2] = String.valueOf(JOptionPane.ERROR_MESSAGE);
            }
        }
        return mensajesResultado;
    }

    /**
     * @param elTest Test del modelo en vigor
     * @param marcas Lista de casillas marcadas
     * @return elTest Test ya corregido del modelo en vigor
     */
    static public ModeloTest100 extraeResultadosCampos(ModeloTest100 elTest, List<Casilla> marcas) {
        String resultado;
        Casilla unaCasilla;
        boolean yaHayMarca;

        try {
            if (marcas != null) {
                for (Campo campoVoy : elTest.getCamposTest()) {
                    char[] campo = new char[campoVoy.getDigitos()];
                    resultado = "";
                    // Bucle, para dígito del campo
                    for (int digito = 0; digito < campoVoy.getDigitos(); digito++) {
                        yaHayMarca = false;
                        // Para cada Casilla del dígito del campo
                        for (int casillaDigito = 0; casillaDigito < campoVoy.getLargoDigitos(); casillaDigito++) {
                            unaCasilla = marcas.get(campoVoy.getPosIncicioPlantilla() + (digito * campoVoy.getLargoDigitos()) + casillaDigito);
                            // Guardo la coordenada original, el centro del cuadrado
                            if (unaCasilla.isMarcada()) {
                                if (!yaHayMarca) {
                                    // Marca encontrada
                                    campo[digito] = campoVoy.getValoresDigito().charAt(casillaDigito);
                                    yaHayMarca = true;
                                } else {
                                    // Doble Marca, ya había una antes
                                    campo[digito] = '?';
                                }
                            }
                        }
                        if (!yaHayMarca) {
                            // No se encontró marca en todo el digito, entonces caráter blanco
                            campo[digito] = ' ';
                        }
                        resultado += campo[digito];
                    }
                    campoVoy.setResultado(resultado);
                }
            }
        } catch (IndexOutOfBoundsException ex) {
            elTest.setNombreArchivo(elTest.getNombreArchivo() + " - ERROR");
        }
        return elTest;
    }

    /**
     *
     * @return Imagen del test corregida
     */
    public static BufferedImage imagenCorregida() {
        // Corrijo la imagen y actualizo el testActual
        if (imagenTest != null) {
            imagenTest = corrigeImagen(imagenTest);
            testActual.setCasillasMarcadas(analizar(imagenTest, testActual));
            if (testActual.getCasillasMarcadas() != null) {
                // Analizo los campos y almaceno el resultado en cada uno de ellos
                testActual = extraeResultadosCampos(testActual, getCasillasTest());
            }
            // Devuelvo la imagen corregida
            return imagenTest;
        }
        return null;
    }

    // Carga la imagen que se va a analizar
    /**
     *
     * @param ficheroImagen Fichero que contiene el archivo de imagen
     */
    public static void cargarImagen(File ficheroImagen) {
        BufferedImage imgCargo;
        try {
            //BufferedImage img = ImageIO.read(ficheroImagen);
            imgCargo = ImageIO.read(ficheroImagen);
            BufferedImage imgNueva = new BufferedImage(imgCargo.getWidth(), imgCargo.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D grImgNueva = imgNueva.createGraphics();
            grImgNueva.drawImage(imgCargo, 0, 0, Color.white, null);
            grImgNueva.dispose();
            Config.setRutaUltimaImagen(ficheroImagen.getAbsolutePath());
            Config.setNombreArchivoUltimaImagen(ficheroImagen.getName());
            // En el escalado prograsivo sólo uso el lado largo como referencia
            setImagenTest(cambiaTamano(imgNueva, Config.ALTO_MODELO));
            // Si la imagen no tiene el tamaño estandarizado, la cambio
            if (getImagenTest().getWidth() != Config.ANCHO_MODELO || getImagenTest().getHeight() != Config.ALTO_MODELO) {
                setImagenTest(normalizaTamano(getImagenTest()));
            }
            // Creo un nuevo test y le pongo el nombre. Guardo también la ruta completa del archivo, con nombre
            setTestActual(new ModeloTest100(ficheroImagen.getCanonicalPath()));
        } catch (IOException e) {
            log.aviso(e.getMessage());
            setImagenTest(null);
        }
    }

    /**
     *
     * @param imag
     * @return imagen normalizada
     */
    public static BufferedImage normalizaTamano(BufferedImage imag) {
        // Cambio el tamaño de la imagen al estándar
        BufferedImage cambiadaImg = new BufferedImage(Config.ANCHO_MODELO, Config.ALTO_MODELO, imag.getType());
        Graphics2D g = cambiadaImg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(imag, 0, 0, Config.ANCHO_MODELO, Config.ALTO_MODELO, null);
        g.dispose();
        return cambiadaImg;
    }

    // Carga el logo que se va a analizar
    /**
     *
     * @param ficheroImagen Fichero con la imagen del nuevo logo
     * @param imgModelo Objeto ModeloImprimible con la imagen del modelo
     * @return Modelo con el nuevo logo
     */
    public static BufferedImage cargarLogoenModelo(File ficheroImagen, ModeloImprimible imgModelo) {
        BufferedImage imgLogoCargo, modeloNuevoLogo;

        try {
            // Cargo el logo nuevo
            imgLogoCargo = ImageIO.read(ficheroImagen);
            modeloNuevoLogo = new BufferedImage(imgModelo.getWidth(), imgModelo.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D grModeloNuevoLogo = modeloNuevoLogo.createGraphics();
            // Pinto el modelo
            grModeloNuevoLogo.drawImage(imgModelo.getImagenModelo(), 0, 0, imgModelo.getImagenModelo().getWidth(), imgModelo.getImagenModelo().getHeight(), Color.white, null);
            // Pinto el logo nuevo
            grModeloNuevoLogo.drawImage(imgLogoCargo, Config.LOGO_X, Config.LOGO_Y, Config.ANCHO_LOGO, Config.ALTO_LOGO, Color.white, null);
            grModeloNuevoLogo.dispose();
            return modeloNuevoLogo;
        } catch (IOException e) {
            log.aviso(e.getMessage());
            return null;
        }
    }

    public static String mostrarVariablesEntorno() {
        // Método auxiliar que devuelve las variables de entorno en un String. Quitar
        StringBuilder resultado = new StringBuilder();

        Map<String, String> env = System.getenv();
        env.forEach((key, value) -> resultado.append(key).append(" = ").append(value).append(System.lineSeparator()));
        return resultado.toString();
    }

    public static String mostrarPropiedadesSistema() {
        // Método auxiliar que devuelve las propiedades del sistema en un String. Quitar
        StringBuilder resultado = new StringBuilder();
        Properties pros = System.getProperties();
        //pros.elements().asIterator().forEachRemaining((var element) -> resultado.append(element).append(System.lineSeparator()));
        pros.forEach((key, value) -> {
            //if (key.toString().contains("dir")) {
            resultado.append(key).append(" = ").append(value).append(System.lineSeparator());
            //}
        });
        return resultado.toString();
        //pros.list(System.out);
    }
}
