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

import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.util.ResourceBundle;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Jesus.delBuey
 */
public class DialogoTipos extends javax.swing.JDialog {

    // Para controlar si hay cambios en el número de preguntas.
    private boolean numPreguntasCambiadas = false;

    /**
     * Get the value of numPreguntasCambiadas
     *
     * @return the value of numPreguntasCambiadas
     */
    public boolean isNumPreguntasCambiadas() {
        return numPreguntasCambiadas;
    }

    /**
     * Set the value of numPreguntasCambiadas
     *
     * @param valor new value of numPreguntasCambiadas
     */
    public void setNumPreguntasCambiadas(boolean valor) {
        this.numPreguntasCambiadas = valor;
    }

    ResourceBundle idioma = Procesador.idioma;
    protected static Loguero log = Procesador.getLog();

    JTableHeader cabecera;
    DefaultTableCellRenderer ren = new DefaultTableCellRenderer();
    TableColumnModel modelCol;
    TableColumn col;

    /**
     * Creates new form DialogoTipos
     *
     * @param parent Padre del diálogo
     * @param modal Si es modal o no
     */
    public DialogoTipos(javax.swing.JDialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
        //
        InicializarFormulario();
        this.setIconImage(Config.getIconoAplic().getImage());
        // Formateo la cabecera
        cabecera = tablaTipos.getTableHeader();
        cabecera.setDefaultRenderer(new main.estilos.RenderCabeceraTablas_AlineaCentro(tablaTipos));
        cabecera.setFont(tablaTipos.getTableHeader().getFont().deriveFont(Font.BOLD));
    }

    private void InicializarFormulario() {
        // Inicializo y formateo las columnas de datos de nuevo. El modelo se ha inicializado en el padre, DialogoEvaluar
        // Copio la estructura de columnas y los datos del modelo que esté definido en Procesador,
        // a la espera de que si guarda los cambios o NO
        // 

        // Pongo a false el cambio de preguntas
        setNumPreguntasCambiadas(false);
        // Inicio los valores de cantidad de preguntas y tipos
        int nTipos = Procesador.getModeloRespTipos().getColumnCount() - 3;
        if (nTipos >= 0) {
            comboTipos.setSelectedIndex(nTipos);
        }
        //comboTipos.setSelectedIndex(Config.getNumTipos());
        valorPreguntasTotal.setText(String.valueOf(Procesador.getModeloRespTipos().getRowCount()));
        // Actualizo el contador de preguntas y preguntas válidas
        tablaTipos.setModel(Procesador.getModeloRespTipos());
        // Actualizdo los valores de Tipos y preguntas
        Config.setNumPreguntas(Procesador.getModeloRespTipos().getRowCount());
        Config.setNumPreguntasValidas(Config.getNumPreguntas() - Procesador.numPreguntasNulas());
        //
        modelCol = tablaTipos.getColumnModel();
        // Cabecera de filas, es la pregunta
        col = modelCol.getColumn(0);
        col.setCellRenderer(tablaTipos.getTableHeader().getDefaultRenderer());
        col.setMinWidth(80);
        col.setMaxWidth(85);
        col.setCellEditor(new main.estilos.EditorCeldaNoEditable());

        // Columnas centradas
        ren.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 1; i < tablaTipos.getColumnCount(); i++) {
            col = modelCol.getColumn(i);
            col.setCellRenderer(ren);
            col.setPreferredWidth(60);
        }

        // Columna de respuesta correcta, son JComboBox. Aplico un editor de ComboBox, y renderizado negrita
        col = modelCol.getColumn(1);
        col.setCellRenderer(new main.estilos.RenderCabeceraFilasTabla_VideoInverso());
        JComboBox elCombo = new JComboBox(new String[]{"A", "B", "C", "D", "E", idioma.getString("DialogoTipos.Anular.text")});

        // Asocio al comboBox un Listener para dectectar cambios en el valor y contar las preguntas anuladas
        elCombo.addPropertyChangeListener((evt) -> {
            // Actualizo el las preguntas válidas
            Config.setNumPreguntasValidas(Config.getNumPreguntas() - Procesador.numPreguntasNulas());
            etqPreguntasValidas.setText("(".concat(idioma.getString("DialogoTipos.etqPreguntasValidas.text").concat(" "))
                    .concat(String.valueOf(Config.getNumPreguntasValidas())).concat(")"));
        });
        col.setCellEditor(new DefaultCellEditor(elCombo));
        col.setMinWidth(80);

        // Ajuste de altura del formulario, en base al número de filas, suponiendo 16 de alto por fila
        int alto = 190 + (tablaTipos.getRowHeight() * tablaTipos.getRowCount());
        int ancho = Config.getNumTipos() * 130 + 370;
        // si es más de 800, pongo 800
        alto = (alto > Procesador.getAltoPantalla() - 50) ? Procesador.getAltoPantalla() - 50 : alto;
        ancho = (ancho > Procesador.getAnchoPantalla()) ? Procesador.getAnchoPantalla() : ancho;
        this.setPreferredSize(new Dimension(ancho, alto));
        // Aplico las nuevas dimensiones
        pack();
        // Coloco el formulario en el centro de la pantalla
        Procesador.Centrame(this);
        this.setLocation(this.getLocation().x, 5);
        // Asocio un evento de teclado F1 para lanzar la ayuda
        Procesador.asociaAyudaF1(btnAyuda, Config.getRutaAyudaTiposyEquivalencias());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelPreguntas = new javax.swing.JPanel();
        etqNumTipos = new javax.swing.JLabel();
        comboTipos = new javax.swing.JComboBox<>();
        etqPreguntas = new javax.swing.JLabel();
        valorPreguntasTotal = new javax.swing.JTextField();
        etqPreguntasValidas = new javax.swing.JLabel();
        panelScroll = new javax.swing.JScrollPane();
        tablaTipos = new javax.swing.JTable();
        btnVolver = new javax.swing.JButton();
        btnAyuda = new javax.swing.JButton();
        btnCargarCSV = new javax.swing.JButton();
        btnGuardarCSV = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("propiedades/Idioma"); // NOI18N
        setTitle(bundle.getString("DialogoTipos.Titulo.Text")); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        panelPreguntas.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        etqNumTipos.setFont(Config.FUENTE_NORMAL);
        etqNumTipos.setText(bundle.getString("DialogoTipos.etqNumTipos.text")); // NOI18N

        comboTipos.setFont(Config.FUENTE_NORMAL);
        comboTipos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6" }));
        comboTipos.setPreferredSize(new java.awt.Dimension(40, 22));
        comboTipos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboTiposActionPerformed(evt);
            }
        });

        etqPreguntas.setFont(Config.FUENTE_NORMAL);
        etqPreguntas.setText(bundle.getString("DialogoTipos.etqPreguntas.text")); // NOI18N

        valorPreguntasTotal.setFont(Config.FUENTE_NORMAL);
        valorPreguntasTotal.setText(bundle.getString("DialogoTipos.valorPreguntasTotal.text")); // NOI18N
        valorPreguntasTotal.setPreferredSize(new java.awt.Dimension(40, 25));
        valorPreguntasTotal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                valorPreguntasTotalFocusLost(evt);
            }
        });
        valorPreguntasTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                valorPreguntasTotalActionPerformed(evt);
            }
        });

        etqPreguntasValidas.setFont(Config.FUENTE_NORMAL.deriveFont(Font.ITALIC)
        );
        etqPreguntasValidas.setText(bundle.getString("DialogoTipos.etqPreguntasValidas.text")); // NOI18N

        javax.swing.GroupLayout panelPreguntasLayout = new javax.swing.GroupLayout(panelPreguntas);
        panelPreguntas.setLayout(panelPreguntasLayout);
        panelPreguntasLayout.setHorizontalGroup(
            panelPreguntasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPreguntasLayout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(etqNumTipos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboTipos, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(etqPreguntas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(valorPreguntasTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(etqPreguntasValidas)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        panelPreguntasLayout.setVerticalGroup(
            panelPreguntasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPreguntasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPreguntasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(etqPreguntas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(valorPreguntasTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(etqPreguntasValidas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboTipos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(etqNumTipos))
                .addContainerGap())
        );

        tablaTipos.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tablaTipos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tablaTipos.setShowGrid(true);
        tablaTipos.getTableHeader().setReorderingAllowed(false);
        panelScroll.setViewportView(tablaTipos);

        btnVolver.setText(bundle.getString("DialogoTipos.btnVolver.text")); // NOI18N
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });

        btnAyuda.setBackground(new java.awt.Color(61, 117, 105));
        btnAyuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/main/Azul_ayuda_chic.png"))); // NOI18N
        btnAyuda.setToolTipText(bundle.getString("DialogoTipos.btnAyuda.toolTipText")); // NOI18N
        btnAyuda.setBorder(null);
        btnAyuda.setBorderPainted(false);
        btnAyuda.setContentAreaFilled(false);
        btnAyuda.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/main/Azul_brillo_ayuda_chic.png"))); // NOI18N
        btnAyuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAyudaActionPerformed(evt);
            }
        });

        btnCargarCSV.setFont(Config.FUENTE_NORMAL);
        btnCargarCSV.setText(bundle.getString("DialogoTipos.btnCargarCSV.text")); // NOI18N
        btnCargarCSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarCSVActionPerformed(evt);
            }
        });

        btnGuardarCSV.setFont(Config.FUENTE_NORMAL);
        btnGuardarCSV.setText(bundle.getString("DialogoTipos.btnGuardarCSV.text")); // NOI18N
        btnGuardarCSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarCSVActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnCargarCSV)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnGuardarCSV))
                            .addComponent(panelScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(20, 20, 20))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelPreguntas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnVolver)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAyuda)
                        .addGap(20, 20, 20))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelPreguntas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAyuda)
                            .addComponent(btnVolver))
                        .addGap(15, 15, 15)))
                .addComponent(panelScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCargarCSV)
                    .addComponent(btnGuardarCSV))
                .addGap(10, 10, 10))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarCSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarCSVActionPerformed
        // Guardo en un archivo csv
        // Guardo primero los cambios en la estructura, si los hubo. Si es correcto guardo el fichero

        if (actualizarCambios()) {
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
                // Exporto al archivo
                String[] mensajes = Procesador.exportarCSV(SelectorFichero.getSelectedFile(), Procesador.getModeloRespTipos());
                // Si hay algún mensaje, lo muestro
                if (!"".equals(mensajes[0])) {
                    // Hubo un error u otro mensaje
                    JOptionPane.showOptionDialog(this.getContentPane(), idioma.getString(mensajes[0]), idioma.getString(mensajes[1]),
                            JOptionPane.NO_OPTION, Integer.parseInt(mensajes[2]), null, Config.OPCION_ACEPTAR, null);
                }
            }
        } else {
            JOptionPane.showOptionDialog(this.getContentPane(), idioma.getString("DialogoTipos.errorGuardarCSV.text"), idioma.getString("Error.text"),
                    JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR,
                    null);
        }
    }//GEN-LAST:event_btnGuardarCSVActionPerformed

    private void btnCargarCSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarCSVActionPerformed
        // Cargo la tabla desde un archivo csv
        // Borro todos los test de la tabla. Uso un JOptionPane personalizado que cambia el idioma, en en array de opciones de botón. Por defecto, paso el botón cancelar

        int loqueDice = JOptionPane.showOptionDialog(this, idioma.getString("DialogoTipos.aviso.borrar.aviso.text"), idioma.getString("Atencion.text"),
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, Config.OPCIONES_ACEPTAR_CANCELAR,
                Config.OPCIONES_ACEPTAR_CANCELAR[Config.OPCIONES_ACEPTAR_CANCELAR.length - 1]);
        // El primer botón, el 0 es aceptar
        if (loqueDice == 0) {
            //
            DialogoCarpertaFichero SelectorFichero = new DialogoCarpertaFichero();  // false indica que quiero seleccionar ficheros
            SelectorFichero.setDialogType(JFileChooser.OPEN_DIALOG);
            SelectorFichero.setFileSelectionMode(JFileChooser.FILES_ONLY); // Para que elija carpeta en lugar de ficheros individuales
            SelectorFichero.setDialogTitle(idioma.getString("FileChooser.Fichero.title"));
            SelectorFichero.setCurrentDirectory(new File(Config.getCarpetaArchivosTests()));

            String[] extensions = {"csv", "txt"};
            SelectorFichero.setFileFilter(new FileNameExtensionFilter("CSV FILES", extensions));
            SelectorFichero.setSelectedFile(new File(Config.getRutaUltimaImagen()));
            // Compruevo y ajusto la escala si es necesario
            int resultado = SelectorFichero.showOpenDialog(this);
            if (resultado == DialogoCarpertaFichero.APPROVE_OPTION) {
                // Actualizo en Config la última ruta
                Config.setCarpetaArchivosTests(SelectorFichero.getSelectedFile().getAbsolutePath());
                // Cargo el CSV
                String[] mensajes = Procesador.importarCSV(SelectorFichero.getSelectedFile(), Procesador.getModeloRespTipos(),
                        Config.MAX_NUM_TIPOS + 1, Config.MAX_NUM_PREGUNTAS, Config.CSV_TIPOS_EQUIVALENCIAS);

                // Si el mensaje es que ha terminado correctamente
                if ("DialogoTipos.teminado.leer_csv.text".equals(mensajes[0])) {
                    // Actualizo la configuración
                    Config.setNumPreguntas(Procesador.getModeloRespTipos().getRowCount() - 1);
                    Config.setNumTipos(Procesador.getModeloRespTipos().getColumnCount() - 2);
                    Config.guardarConfiguracion();
                } else {
                    // Si hubo un error u otro mensaje
                    if (!"".equals(mensajes[0])) {
                        JOptionPane.showOptionDialog(this.getContentPane(), idioma.getString(mensajes[0]), idioma.getString(mensajes[1]),
                                JOptionPane.NO_OPTION, Integer.parseInt(mensajes[2]), null, Config.OPCION_ACEPTAR, null);
                    }
                }
                // Actualizo los campos del formulario
                InicializarFormulario();
            }
        }
    }//GEN-LAST:event_btnCargarCSVActionPerformed

    private void comboTiposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboTiposActionPerformed
        // Detecto cambios del valor del comboBox
        if (evt.getActionCommand().equals("comboBoxChanged")
                && !comboTipos.getSelectedItem().toString().equals(String.valueOf(Config.getNumTipos()))) {
            // Si ha cambiado la estructura, num. preguntas o tipos
            if (actualizarCambios()) {
                //this.dispose();
                this.repaint();
            }
        }
    }//GEN-LAST:event_comboTiposActionPerformed

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        // Cerrar el formulario
        this.dispose();
    }//GEN-LAST:event_btnVolverActionPerformed

    private void valorPreguntasTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_valorPreguntasTotalActionPerformed
        // Ha pulsado INTRO en el campo. Compruebo si el número de preguntas es correcto
        System.out.println("Intro : " + evt.getActionCommand());
        if (!valorPreguntasTotal.getText().equals(String.valueOf(Config.getNumPreguntas()))) {
            if (actualizarCambios()) {
                this.repaint();
            }
        }
    }//GEN-LAST:event_valorPreguntasTotalActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // Al cerrar la ventana compruebo si ha cambiado elnúmero de preguntas
        if (isNumPreguntasCambiadas()) {
            if (actualizarCambios()) {
                this.repaint();
                // Vuelvo a presentar la ventana
                this.setVisible(true);
            }
        }
    }//GEN-LAST:event_formWindowClosed

    private void valorPreguntasTotalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_valorPreguntasTotalFocusLost
        // Si abandono este campo y he cambiado el número de preguntas
        if (!valorPreguntasTotal.getText().equals(String.valueOf(Config.getNumPreguntas()))) {
            setNumPreguntasCambiadas(true);
        } else {
            setNumPreguntasCambiadas(false);
        }
    }//GEN-LAST:event_valorPreguntasTotalFocusLost

    private void btnAyudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAyudaActionPerformed
        String error = Procesador.mostrarAyuda(Config.getRutaAyudaTiposyEquivalencias());
        
        if (!"".equals(error)) {
            log.error(error);
            JOptionPane.showOptionDialog(rootPane, error, idioma.getString("Error.text"),
                JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{idioma.getString("Aceptar.text")}, idioma.getString("Aceptar.text"));
        }

    }//GEN-LAST:event_btnAyudaActionPerformed

    // Métodos propios
    private boolean actualizarCambios() {
        // Actualizo la tabla con los valores de tipos y preguntas.
        // Si ha cambiado la estructura, num. preguntas o tipos
        // Aquí cambio las preguntas, los tipos se cambian en el evento del comboBox
        // Pongo a false el control de número de preguntas cambiado
        setNumPreguntasCambiadas(false);
        if (comboTipos.getSelectedIndex() != Config.getNumTipos() - 1) {
            return actualizarTipos();
        }
        try {
            if (Integer.parseInt(valorPreguntasTotal.getText()) != Config.getNumPreguntas()) {
                return actualizarPreguntas();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showOptionDialog(rootPane, idioma.getString("DialogoTipos.valorNumPreguntas.error.text"), idioma.getString("Error.text"),
                    JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
            // Restauro el valor de numero de preguntas
            valorPreguntasTotal.setText(String.valueOf(Config.getNumPreguntas()));
        }
        // Si no hay cambios Guardo los cambios de valores en la tabla y salgo
        Procesador.setModeloRespTipos((DefaultTableModel) tablaTipos.getModel());
        return true;
    }

    private boolean actualizarTipos() {
        int tipos = comboTipos.getSelectedIndex() + 1;
        if (tipos != Config.getNumTipos()) {
            Procesador.ajustaTipos(tipos);
            Config.setNumTipos(tipos);
            comboTipos.setSelectedIndex(tipos - 1);
            Config.guardarConfiguracion();
            InicializarFormulario();
            return true;
        }
        return false;
    }

    private boolean actualizarPreguntas() {
        int preguntas;
        // Compruebo que el valor de preguntas esté entre los límites
        try {
            preguntas = Integer.parseInt(valorPreguntasTotal.getText());
            if (preguntas > 0 && preguntas <= Config.MAX_NUM_PREGUNTAS) {
                // Si hay cambio en el numero de preguntas
                if (preguntas != Config.getNumPreguntas()) {
                    // Compruebo que el valor esté entre los límites
                    if (preguntas > Config.getNumPreguntas()) {
                        Procesador.AgregaFilasModeloRespTipos(preguntas - Config.getNumPreguntas());
                    } else {
                        // el número es menor que 0
                        Procesador.BorraFilasModeloRespTipos(Config.getNumPreguntas() - preguntas);
                    }
                    // Si todo es correcto agrego o elimino filas y actualizo la configuración
                    Config.setNumPreguntas(preguntas);
                    valorPreguntasTotal.setText(String.valueOf(preguntas));
                    Config.setNumPreguntasValidas(preguntas - Procesador.numPreguntasNulas());
                    etqPreguntasValidas.setText("(".concat(idioma.getString("DialogoTipos.etqPreguntasValidas.text").concat(" "))
                            .concat(String.valueOf(Config.getNumPreguntasValidas())).concat(")"));
                    Config.guardarConfiguracion();
                    InicializarFormulario();
                    return true;
                }
            } else {
                JOptionPane.showOptionDialog(rootPane, idioma.getString("DialogoTipos.valorNumPreguntas.error.text"), idioma.getString("Error.text"),
                        JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showOptionDialog(rootPane, idioma.getString("DialogoTipos.valorNumPreguntas.error.text"), idioma.getString("Error.text"),
                    JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
        }
        // El valor de preguntas NO es correcto
        valorPreguntasTotal.setText(String.valueOf(Config.getNumPreguntas()));
        comboTipos.setSelectedIndex(Config.getNumTipos() - 1);
        Config.setNumPreguntasValidas(Procesador.getModeloRespTipos().getRowCount() - Procesador.numPreguntasNulas());
        etqPreguntasValidas.setText("(".concat(idioma.getString("DialogoTipos.etqPreguntasValidas.text").concat(" "))
                .concat(String.valueOf(Config.getNumPreguntasValidas())).concat(")"));
        return false;
    }

    /**
     *
     * /
     *
     **
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DialogoTipos.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogoTipos.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogoTipos.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogoTipos.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        }
        //</editor-fold>
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAyuda;
    private javax.swing.JButton btnCargarCSV;
    private javax.swing.JButton btnGuardarCSV;
    private javax.swing.JButton btnVolver;
    private javax.swing.JComboBox<String> comboTipos;
    private javax.swing.JLabel etqNumTipos;
    private javax.swing.JLabel etqPreguntas;
    private javax.swing.JLabel etqPreguntasValidas;
    private javax.swing.JPanel panelPreguntas;
    private javax.swing.JScrollPane panelScroll;
    private javax.swing.JTable tablaTipos;
    private javax.swing.JTextField valorPreguntasTotal;
    // End of variables declaration//GEN-END:variables
}
