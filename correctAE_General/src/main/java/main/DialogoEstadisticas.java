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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.io.File;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import main.estilos.CeldasTablaEstad;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.util.Rotation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 *
 * @author Jesus.delBuey
 */
public class DialogoEstadisticas extends javax.swing.JDialog {
// Objetos de los graficos

    ResourceBundle idioma = Procesador.idioma;
    private final Loguero log = Procesador.getLog();

    String[] columnas = {idioma.getString("DialogoEstadisticas.numPregunta.text"),
        idioma.getString("DialogoEvaluar.tabla.columna4"),
        idioma.getString("DialogoEvaluar.tabla.columna5"),
        idioma.getString("DialogoEvaluar.tabla.columna6"),
        idioma.getString("DialogoEvaluar.tabla.columna7"),
        "A", "B", "C", "D", "E"};
    DefaultTableModel modeloEstadisPreg = new DefaultTableModel(columnas, 0);

    // Estadísticas de aprobados y suspensos en int[] aprobySuspen
    // Para ir guardando aprobados y suspensos
    // en la posición 0, aprobados
    // en la 1 suspensos
    // Estadísticas de preguntas en int[][] estadisticasPreg
    // Totales de aciertos, fallos, blancos y dobles
    /**
     * Creates new form DialogoEstadisticas
     *
     * @param parent Padre del diálogo
     * @param modal Si es modal o no
     * @param totalTests Número total de test corregidos
     * @param aprobySuspen Array enteros que tiene en el elemento 0 los
     * aprobados y en el 1 los suspensos
     * @param estadisticasNotas Array con la frecuencia de notas
     * @param estadisticasPreg Array bidimensional con las estadisticas de las
     * preguntas
     */
    public DialogoEstadisticas(javax.swing.JDialog parent, boolean modal, int totalTests, int[] aprobySuspen, int[] estadisticasNotas, int[][] estadisticasPreg) {
        super(parent, modal);
        initComponents();

        ponGraficos(totalTests, aprobySuspen, estadisticasNotas);
        InicializarFormulario(estadisticasPreg);
    }

    private void InicializarFormulario(int[][] estad) {
        JTableHeader cabecera;
        float porciento;
        String valorTxt, respCorrecta;
        Set<Point> celdasNegritas = new HashSet<>();
        int indiceRespCorrec;
        
        // Establezco los máximos de ancho y alto
        if (this.getWidth() > Procesador.getAnchoPantalla()) {
            this.setSize(new Dimension(Procesador.getAnchoPantalla() - 25, this.getHeight()));
        }
        if (this.getHeight() > Procesador.getAltoPantalla()) {
            this.setSize(new Dimension(this.getWidth(), Procesador.getAltoPantalla() - 50));
        }
        // Coloco el formulario en el centro de la pantalla
        Procesador.Centrame(this);
        this.setLocation(this.getLocation().x, 5);
        //
        tablaEstadisPreg.setModel(modeloEstadisPreg);
        //
        // Formateo la cabecera
        cabecera = tablaEstadisPreg.getTableHeader();
        cabecera.setDefaultRenderer(new main.estilos.RenderCabeceraTablas_AlineaCentro(tablaEstadisPreg));
        cabecera.setFont(tablaEstadisPreg.getTableHeader().getFont().deriveFont(Font.BOLD));
        TableColumnModel modelCol = tablaEstadisPreg.getColumnModel();
        // Pongo los anchos preferidos y centrado el contenido
        modelCol.getColumn(0).setPreferredWidth(80);
        modelCol.getColumn(0).setCellRenderer(tablaEstadisPreg.getTableHeader().getDefaultRenderer());
        for (int i = 1; i < modelCol.getColumnCount(); i++) {
            if (i < 5) {
                modelCol.getColumn(i).setPreferredWidth(60);
            } else {
                modelCol.getColumn(i).setPreferredWidth(20);
            }
        }
        for (int fil = 0; fil < Config.getNumPreguntas(); fil++) {
            // Num pregunta, aciertos, fallos, blancos, dobles, A, B, C, D y E
            Object[] fila;
            // Respuesta correcta segun la equivalencia respecto al tipo Maestro
            respCorrecta = Procesador.getModeloRespTipos().getValueAt(fil, 1).toString();
            // Si está anulada no pongo nada
            if (respCorrecta.contains(idioma.getString("DialogoTipos.Anular.text"))) {
                fila = new Object[]{fil + 1,
                    idioma.getString("DialogoTipos.Anular.text"), idioma.getString("DialogoTipos.Anular.text"),
                    idioma.getString("DialogoTipos.Anular.text"), idioma.getString("DialogoTipos.Anular.text"),
                    idioma.getString("DialogoTipos.Anular.text"), idioma.getString("DialogoTipos.Anular.text"),
                    idioma.getString("DialogoTipos.Anular.text"), idioma.getString("DialogoTipos.Anular.text"),
                    idioma.getString("DialogoTipos.Anular.text")};
                // Guardo las posiciones para poner las celdas en negrita
                for (int i = 1; i < modelCol.getColumnCount(); i++) {
                    celdasNegritas.add(new Point(fil, i));
                }
            } else {
                fila = new Object[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                indiceRespCorrec = "ABCDE".indexOf(respCorrecta) + 5;
                // En la columna 0 esta el número de pregunta
                fila[0] = fil + 1;
                // Pongo los valores absolutos y el porcentaje respecto al todal de alumnos, tests. Si es 0 no pongo 0 en lugar de 0%
                for (int col = 1; col < fila.length; col++) {
                    porciento = ((float) estad[fil][col - 1] / Procesador.modeloTablaTestsCorregidos.getRowCount()) * 100;
                    valorTxt = estad[fil][col - 1] == 0 ? "0" : String.format("%.1f%%", porciento);
                    fila[col] = valorTxt;
                    if(col == indiceRespCorrec) {
                        // Si es la respuesta correcta, pongo en Negrita los datos
                        celdasNegritas.add(new Point(fil, col));
                    }
                }
            }
            modeloEstadisPreg.addRow(fila);
            // Asocio un evento de teclado F1 para lanzar la ayuda
            Procesador.asociaAyudaF1(btnAyuda, Config.getRutaAyudaEstadisticas());
        }
        tablaEstadisPreg.setDefaultRenderer(Object.class, new CeldasTablaEstad(Config.FUENTE_NORMAL, celdasNegritas));
    }

    private void ponGraficos(int totalTests, int[] aprSus, int[] estadNotas) {
// Grafico de tarta con aprovados y suspensos
        PieDataset tartaDataset = createTartaDataset(totalTests, aprSus);
        JFreeChart tartaChart = createTartaChart(tartaDataset, idioma.getString("DialogoEstadisticas.tarta.titulo.text"));
        ChartPanel tartaPanel = new ChartPanel(tartaChart);

// Grafico de barras con distribución de notas
        DefaultCategoryDataset barrasSet = new DefaultCategoryDataset();

        for (int ind = 0; ind < estadNotas.length; ind++) {
            barrasSet.addValue(estadNotas[ind], idioma.getString("DialogoEstadisticas.barras.etqCalificacion.text"), String.valueOf(ind));
        }
        JFreeChart barrasChart = ChartFactory.createBarChart(idioma.getString("DialogoEstadisticas.barras.titulo.text"),
                idioma.getString("DialogoEstadisticas.barras.categoria.text"),
                idioma.getString("DialogoEstadisticas.barras.valor.text"),
                barrasSet,
                PlotOrientation.VERTICAL,
                true,
                true,
                true);

        // CAmbio el color de las barras
        CategoryPlot plot = barrasChart.getCategoryPlot();
        BarRenderer renderBarras = (BarRenderer) plot.getRenderer();

        // Color (r,g,b) or (r,g,b,a)
        Color miColor = new Color(48, 100, 125);
        renderBarras.setSeriesPaint(0, miColor);

        ChartPanel barrasPanel = new ChartPanel(barrasChart);
        panelGraficos.add(tartaPanel);
        panelGraficos.add(barrasPanel);
        //panelGraficos.setBackground(Color.lightGray);
        // default size
        tartaPanel.setPreferredSize(new Dimension(300, 300));
        //tartaPanel.setMaximumSize(new Dimension(300, 300));
        barrasPanel.setPreferredSize(new Dimension(estadNotas.length * 60, 300));
    }

    /**
     * Crea un tartaDataset
     */
    private PieDataset createTartaDataset(int totalTests, int[] estadAproySus) {
        DefaultPieDataset grafTartaDataSet = new DefaultPieDataset();
        float tantoApro, tantoSus;
        String strTantoApro, strTantoSus;

        if (totalTests > 0) {
            tantoApro = estadAproySus[0] / (float) totalTests * 100;
            strTantoApro = String.format("%.2f", tantoApro);
            tantoSus = estadAproySus[1] / (float) totalTests * 100;
            strTantoSus = String.format("%.2f", tantoSus);
        } else {
            strTantoApro = "0";
            strTantoSus = "0";
        }

        grafTartaDataSet.setValue(idioma.getString("DialogoEstadisticas.tarta.suspensos.text").concat(" ").concat(String.valueOf(estadAproySus[1])).
                concat(" - ").concat(strTantoSus).concat("%"), estadAproySus[1]);

        grafTartaDataSet.setValue(idioma.getString("DialogoEstadisticas.tarta.aprobados.text").concat(" ").concat(String.valueOf(estadAproySus[0])).
                concat(" - ").concat(strTantoApro).concat("%"), estadAproySus[0]);
        //result.setValue("No presentados", 51);
        return grafTartaDataSet;
    }

    /**
     * Crea un tartaChart
     */
    private JFreeChart createTartaChart(PieDataset dataset, String title) {

        JFreeChart chart = ChartFactory.createPieChart3D(
                title, // tartaChart title
                dataset, // data
                true, // include legend
                true,
                false
        );

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        return chart;
    }

    private void exportarTablaEstadisticas() {
        // Guardo en un archivo csv
        // Guardo primero los cambios en la estructura, si los hubo. Si es correcto guardo el fichero

        if (tablaEstadisPreg.getRowCount() > 0) {
            DialogoCarpertaFichero SelectorFichero = new DialogoCarpertaFichero();  // false indica que quiero seleccionar ficheros
            SelectorFichero.setDialogType(JFileChooser.SAVE_DIALOG);
            SelectorFichero.setFileSelectionMode(JFileChooser.FILES_ONLY); // Para que elija carpeta en lugar de ficheros individuales
            SelectorFichero.setDialogTitle(idioma.getString("FileChooser.Fichero.title"));
            SelectorFichero.setCurrentDirectory(new File(Config.getCarpetaArchivosTests()));

            String[] extensions = {"csv", "txt"};
            SelectorFichero.setFileFilter(new FileNameExtensionFilter("CSV FILES", extensions));
            SelectorFichero.setSelectedFile(new File(Config.getRutaUltimaImagen()));
            // Compruevo y ajusto la escala si es necesario
            int resultado = SelectorFichero.showSaveDialog(this);
            if (resultado == DialogoCarpertaFichero.APPROVE_OPTION) {
                // Actualizo en Config la última ruta
                Config.setCarpetaArchivosTests(SelectorFichero.getSelectedFile().getAbsolutePath());
                String[] mensajes = Procesador.exportarCSV(SelectorFichero.getSelectedFile(), (DefaultTableModel) tablaEstadisPreg.getModel());
                // Si hay algún mensaje, lo muestro
                if (!"".equals(mensajes[0])) {
                    // Hubo un error u otro mensaje
                    JOptionPane.showOptionDialog(this.getContentPane(), idioma.getString(mensajes[0]), idioma.getString(mensajes[1]),
                            JOptionPane.NO_OPTION, Integer.parseInt(mensajes[2]), null, Config.OPCION_ACEPTAR, null);
                }
            }
        } else {
            JOptionPane.showOptionDialog(this.getContentPane(), idioma.getString("DialogoEvaluar.tablaVacia.text"), idioma.getString("Atencion.text"),
                    JOptionPane.NO_OPTION, JOptionPane.WARNING_MESSAGE, null, Config.OPCION_ACEPTAR,
                    null);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        panelGraficos = new javax.swing.JPanel();
        panelEstadisPreg = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaEstadisPreg = new javax.swing.JTable();
        panelAyuda = new javax.swing.JPanel();
        btnExportarEstadisticas = new javax.swing.JButton();
        btnAyuda = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("propiedades/Idioma"); // NOI18N
        setTitle(bundle.getString("DialogoEstadisticas.Titulo.text")); // NOI18N

        panelGraficos.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelGraficos.setMinimumSize(new java.awt.Dimension(600, 600));
        panelGraficos.setLayout(new javax.swing.BoxLayout(panelGraficos, javax.swing.BoxLayout.LINE_AXIS));
        jScrollPane2.setViewportView(panelGraficos);

        panelEstadisPreg.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tablaEstadisPreg.setFont(Config.FUENTE_NORMAL);
        tablaEstadisPreg.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tablaEstadisPreg);

        javax.swing.GroupLayout panelEstadisPregLayout = new javax.swing.GroupLayout(panelEstadisPreg);
        panelEstadisPreg.setLayout(panelEstadisPregLayout);
        panelEstadisPregLayout.setHorizontalGroup(
            panelEstadisPregLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEstadisPregLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1416, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );
        panelEstadisPregLayout.setVerticalGroup(
            panelEstadisPregLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEstadisPregLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnExportarEstadisticas.setFont(Config.FUENTE_NORMAL);
        btnExportarEstadisticas.setText(bundle.getString("DialogoEstadisticas.btnExportarEstadisticas.text")); // NOI18N
        btnExportarEstadisticas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarEstadisticasActionPerformed(evt);
            }
        });

        btnAyuda.setBackground(new java.awt.Color(61, 117, 105));
        btnAyuda.setFont(Config.FUENTE_NORMAL);
        btnAyuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/main/Azul_ayuda_chic.png"))); // NOI18N
        btnAyuda.setToolTipText(bundle.getString("DialogoEstadisticas.btnAyuda.toolTipText")); // NOI18N
        btnAyuda.setBorder(null);
        btnAyuda.setBorderPainted(false);
        btnAyuda.setContentAreaFilled(false);
        btnAyuda.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/main/Azul_brillo_ayuda_chic.png"))); // NOI18N
        btnAyuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAyudaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAyudaLayout = new javax.swing.GroupLayout(panelAyuda);
        panelAyuda.setLayout(panelAyudaLayout);
        panelAyudaLayout.setHorizontalGroup(
            panelAyudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAyudaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnExportarEstadisticas)
                .addGap(18, 18, 18)
                .addComponent(btnAyuda)
                .addContainerGap())
        );
        panelAyudaLayout.setVerticalGroup(
            panelAyudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAyudaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAyudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAyuda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnExportarEstadisticas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panelAyuda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(panelEstadisPreg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelAyuda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelEstadisPreg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAyudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAyudaActionPerformed
        String error = Procesador.mostrarAyuda(Config.getRutaAyudaEstadisticas());

        if (!"".equals(error)) {
            log.error(error);
            JOptionPane.showOptionDialog(rootPane, error, idioma.getString("Error.text"),
                    JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{idioma.getString("Aceptar.text")}, idioma.getString("Aceptar.text"));
        }
    }//GEN-LAST:event_btnAyudaActionPerformed

    private void btnExportarEstadisticasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarEstadisticasActionPerformed
        // Exporto la tabla de estadística a un fichero csv
        exportarTablaEstadisticas();
    }//GEN-LAST:event_btnExportarEstadisticasActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAyuda;
    private javax.swing.JButton btnExportarEstadisticas;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel panelAyuda;
    private javax.swing.JPanel panelEstadisPreg;
    private javax.swing.JPanel panelGraficos;
    private javax.swing.JTable tablaEstadisPreg;
    // End of variables declaration//GEN-END:variables
}
