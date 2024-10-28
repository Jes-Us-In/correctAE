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
import java.awt.event.MouseAdapter;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import main.estilos.RenderAlineadoFuenteActual;

/**
 *
 * @author Jesus.delBuey
 */
public class DialogoEvaluar extends javax.swing.JDialog {

    ResourceBundle idioma = Procesador.idioma;
    private final Loguero log = Procesador.getLog();

    // Si no se ha corregido no muestro las estadísticas
    private boolean heCorregido = false;
    JDialog padre;

    //MiModeloTabla modeloTabla;
    /**
     * Creates new form DialogoEvaluar
     *
     * @param parent Padre del diálogo
     * @param modal Si es modal o no
     */
    public DialogoEvaluar(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        padre = this;
        initComponents();
        InicializarFormulario();
        // Incializo el modelo de respuestas y tipos.
    }

    private void InicializarFormulario() {
        Map<String, String> testCorregido = new HashMap<>();

        // Coloco el formulario en el centro de la pantalla
        Procesador.Centrame(this);

        // Si no se ha corregido no muestro las estadísticas
        heCorregido = false;
        // Asigno el modelo de tests de examen
        tablaTestsCorregidos.setModel(Procesador.modeloTablaTestsCorregidos);
        // Formateo las columnas de la tabla de tests
        formateaColumnas(tablaTestsCorregidos);
        // Añado un listener a la tabla para el doble click sobre una fila
        tablaTestsCorregidos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    // Cargo el archivo de imagen del test seleccionado
                    try {
                        JTable tabla = (JTable) e.getComponent();
                        testCorregido.put("num", tabla.getModel().getValueAt(tabla.getSelectedRow(), 0).toString());
                        testCorregido.put("nif", tabla.getModel().getValueAt(tabla.getSelectedRow(), 1).toString());
                        testCorregido.put("tipo", tabla.getModel().getValueAt(tabla.getSelectedRow(), 2).toString());
                        testCorregido.put("grupo", tabla.getModel().getValueAt(tabla.getSelectedRow(), 3).toString());
                        testCorregido.put("aciertos", tabla.getModel().getValueAt(tabla.getSelectedRow(), 4).toString());
                        testCorregido.put("fallos", tabla.getModel().getValueAt(tabla.getSelectedRow(), 5).toString());
                        testCorregido.put("blancos", tabla.getModel().getValueAt(tabla.getSelectedRow(), 6).toString());
                        testCorregido.put("dobles", tabla.getModel().getValueAt(tabla.getSelectedRow(), 7).toString());
                        testCorregido.put("nota", tabla.getModel().getValueAt(tabla.getSelectedRow(), 8).toString());
                        testCorregido.put("respuestas", tabla.getModel().getValueAt(tabla.getSelectedRow(), 9).toString());
                        DialogoVerTestCorregido ventaTestCorr = new DialogoVerTestCorregido(padre, true, testCorregido); // permito abrir varios tests
                        ventaTestCorr.setVisible(true);
                        // Si hay cambios actualizo el test
                        // El campo num, no lo cambio, ni la corrección, sólo nif, tipo, grupo y respuestas.
                        if (ventaTestCorr.isHayCambios()) {
                            tabla.getModel().setValueAt(testCorregido.get("nif"), tabla.getSelectedRow(), 1);
                            tabla.getModel().setValueAt(testCorregido.get("tipo"), tabla.getSelectedRow(), 2);
                            tabla.getModel().setValueAt(testCorregido.get("grupo"), tabla.getSelectedRow(), 3);
                            tabla.getModel().setValueAt(testCorregido.get("respuestas"), tabla.getSelectedRow(), 9);
                        }
                    } catch (IndexOutOfBoundsException ex) {
                        log.info(idioma.getString("DialogoVerTest.error.leyendo.archivo.text") + "\n" + ex.getMessage());
                        JOptionPane.showOptionDialog(rootPane, idioma.getString("DialogoVerTest.error.leyendo.archivo.text"), idioma.getString("Error.text"),
                                JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
                    }
                }
            }
        });
        // Asocio un evento de teclado F1 para lanzar la ayuda
        Procesador.asociaAyudaF1(btnAyuda, Config.getRutaAyudaEvaluaciones());
    }

    private void formateaColumnas(JTable tabla) {
        // Formateo las columnas de la tabla de tests
        // {"NIF", "Tipo", "Grupo", "Aciertos", "Fallos", "Blancos", "Dobles", "Nota", "Respuestas"};
        TableColumnModel columnModel = tabla.getColumnModel();
        // Centrar las cabeceras
        DefaultTableCellRenderer renCab = (DefaultTableCellRenderer) tabla.getTableHeader().getDefaultRenderer();
        renCab.setHorizontalAlignment(JLabel.CENTER);
        JTableHeader cab = tabla.getTableHeader();
        cab.setFont(Config.FUENTE_NORMAL);

        // Para centrar el contenido de las siete primeras columnas, todas menos las respuestas
        RenderAlineadoFuenteActual ren = new RenderAlineadoFuenteActual(SwingConstants.CENTER);
        for (int i = 0; i < columnModel.getColumnCount() - 1; i++) {
            columnModel.getColumn(i).setCellRenderer(ren);
        }
        // Ajuste a la izquierda para la columna de respuestas y con la fuente actual, escalada
        RenderAlineadoFuenteActual renRespuestas = new RenderAlineadoFuenteActual(SwingConstants.LEFT);
        columnModel.getColumn(columnModel.getColumnCount() - 1).setCellRenderer(renRespuestas);

        // Numero de la fila, test
        columnModel.getColumn(0).setMaxWidth(70);
        columnModel.getColumn(0).setMinWidth(40);
        // NIF
        columnModel.getColumn(1).setMaxWidth(90);
        columnModel.getColumn(1).setMinWidth(90);
        //Tipo
        columnModel.getColumn(2).setMaxWidth(50);
        columnModel.getColumn(2).setMinWidth(50);
        // Grupo
        columnModel.getColumn(3).setMaxWidth(55);
        columnModel.getColumn(3).setMinWidth(55);
        // Aciertos
        columnModel.getColumn(4).setMaxWidth(70);
        columnModel.getColumn(4).setMinWidth(70);
        // Fallos
        columnModel.getColumn(5).setMaxWidth(60);
        columnModel.getColumn(5).setMinWidth(60);
        // Blancos
        columnModel.getColumn(6).setMaxWidth(65);
        columnModel.getColumn(6).setMinWidth(65);
        // Dobles
        columnModel.getColumn(7).setMaxWidth(60);
        columnModel.getColumn(7).setMinWidth(60);
        // Nota
        columnModel.getColumn(8).setMaxWidth(70);
        columnModel.getColumn(8).setMinWidth(70);
        // Respuestas        
        columnModel.getColumn(9).setMinWidth(200);
        columnModel.getColumn(9).setMinWidth(200);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        etqNombreExamen = new javax.swing.JLabel();
        etqInfoDobleClick = new javax.swing.JLabel();
        btnAyuda = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaTestsCorregidos = new javax.swing.JTable();
        panelBotones = new javax.swing.JPanel();
        btnEvaluar = new javax.swing.JButton();
        btnEstadisticas = new javax.swing.JButton();
        btnTiposEquiv = new javax.swing.JButton();
        menuPrinciaplBarra = new javax.swing.JMenuBar();
        menuArchivo = new javax.swing.JMenu();
        cargarExamen = new javax.swing.JMenuItem();
        guardarExamen = new javax.swing.JMenuItem();
        separador = new javax.swing.JPopupMenu.Separator();
        importarExamen = new javax.swing.JMenuItem();
        exportarExamen = new javax.swing.JMenuItem();
        menuOpciones = new javax.swing.JMenu();
        borrarTests = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("propiedades/Idioma"); // NOI18N
        setTitle(bundle.getString("DialogoEvaluar.title.text")); // NOI18N

        etqNombreExamen.setFont(Config.FUENTE_NORMAL);
        etqNombreExamen.setText(bundle.getString("DialogoGuardarExamen.etqNombreExamen.text")); // NOI18N

        etqInfoDobleClick.setFont(Config.FUENTE_NORMAL.deriveFont(Font.BOLD));
        etqInfoDobleClick.setText(bundle.getString("DialogoEvaluar.etqInfoDobleClick.text")); // NOI18N

        btnAyuda.setBackground(new java.awt.Color(61, 117, 105));
        btnAyuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/main/Azul_ayuda_chic.png"))); // NOI18N
        btnAyuda.setToolTipText(bundle.getString("btnAyudaToolTip.text")); // NOI18N
        btnAyuda.setBorder(null);
        btnAyuda.setBorderPainted(false);
        btnAyuda.setContentAreaFilled(false);
        btnAyuda.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/main/Azul_brillo_ayuda_chic.png"))); // NOI18N
        btnAyuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAyudaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(etqNombreExamen)
                    .addComponent(etqInfoDobleClick))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(btnAyuda)
                .addGap(0, 0, 0))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(etqNombreExamen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(etqInfoDobleClick))
                    .addComponent(btnAyuda))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jScrollPane1.setMaximumSize(new java.awt.Dimension(2400, 300));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(300, 300));

        tablaTestsCorregidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tablaTestsCorregidos);

        panelBotones.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        btnEvaluar.setFont(Config.FUENTE_TITULO);
        btnEvaluar.setText(bundle.getString("DialogoEvaluar.btnEvaluar.text")); // NOI18N
        btnEvaluar.setMaximumSize(new java.awt.Dimension(100, 23));
        btnEvaluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEvaluarActionPerformed(evt);
            }
        });

        btnEstadisticas.setFont(Config.FUENTE_TITULO);
        btnEstadisticas.setText(bundle.getString("DialogoEvaluar.btnEstadisticas.text")); // NOI18N
        btnEstadisticas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEstadisticasActionPerformed(evt);
            }
        });

        btnTiposEquiv.setFont(Config.FUENTE_TITULO);
        btnTiposEquiv.setText(bundle.getString("DialogoEvaluar.btnTiposEquiv.text")); // NOI18N
        btnTiposEquiv.setActionCommand(bundle.getString("DialogoEvaluar.btnTiposEquiv.actionCommand")); // NOI18N
        btnTiposEquiv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTiposEquivActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBotonesLayout = new javax.swing.GroupLayout(panelBotones);
        panelBotones.setLayout(panelBotonesLayout);
        panelBotonesLayout.setHorizontalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnEvaluar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnEstadisticas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(btnTiposEquiv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelBotonesLayout.setVerticalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTiposEquiv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEvaluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEstadisticas))
                .addContainerGap())
        );

        menuArchivo.setMnemonic('A');
        menuArchivo.setText(bundle.getString("DialogoEvaluar.menuArchivo.text")); // NOI18N
        menuArchivo.setFont(Config.FUENTE_NORMAL);

        cargarExamen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_DOWN_MASK));
        cargarExamen.setFont(Config.FUENTE_NORMAL);
        cargarExamen.setText(bundle.getString("DialogoEvaluar.cargarExamen.text")); // NOI18N
        cargarExamen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cargarExamenActionPerformed(evt);
            }
        });
        menuArchivo.add(cargarExamen);

        guardarExamen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.SHIFT_DOWN_MASK));
        guardarExamen.setFont(Config.FUENTE_NORMAL);
        guardarExamen.setText(bundle.getString("DialogoEvaluar.guardarExamen.text")); // NOI18N
        guardarExamen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarExamenActionPerformed(evt);
            }
        });
        menuArchivo.add(guardarExamen);
        menuArchivo.add(separador);

        importarExamen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.SHIFT_DOWN_MASK));
        importarExamen.setFont(Config.FUENTE_NORMAL);
        importarExamen.setText(bundle.getString("DialogoEvaluar.importarExamen.text")); // NOI18N
        importarExamen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importarExamenActionPerformed(evt);
            }
        });
        menuArchivo.add(importarExamen);

        exportarExamen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.SHIFT_DOWN_MASK));
        exportarExamen.setFont(Config.FUENTE_NORMAL);
        exportarExamen.setText(bundle.getString("DialogoEvaluar.exportarExamen.text")); // NOI18N
        exportarExamen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportarExamenActionPerformed(evt);
            }
        });
        menuArchivo.add(exportarExamen);

        menuPrinciaplBarra.add(menuArchivo);

        menuOpciones.setText(bundle.getString("DialogoEvaluar.menuOpciones.text")); // NOI18N
        menuOpciones.setFont(Config.FUENTE_NORMAL);

        borrarTests.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.SHIFT_DOWN_MASK));
        borrarTests.setFont(Config.FUENTE_NORMAL);
        borrarTests.setText(bundle.getString("DialogoEvaluar.borrarTests.text")); // NOI18N
        borrarTests.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarTestsActionPerformed(evt);
            }
        });
        menuOpciones.add(borrarTests);

        menuPrinciaplBarra.add(menuOpciones);
        menuPrinciaplBarra.add(jMenu1);

        setJMenuBar(menuPrinciaplBarra);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1022, Short.MAX_VALUE)
                    .addComponent(panelBotones, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTiposEquivActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTiposEquivActionPerformed
        // Ventana que muestra el los tipos y equivalencias
        DialogoTipos ventaTipos = new DialogoTipos(this, true);
        ventaTipos.setVisible(true);
    }//GEN-LAST:event_btnTiposEquivActionPerformed

    private void btnEstadisticasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEstadisticasActionPerformed
        // Muestra los gráficos de los resultados. Habilitar sólo si se ha corregido ya
        if (heCorregido && Procesador.getEstadNotas().length > 0 && Procesador.getEstadPregs().length > 0) {
            DialogoEstadisticas estadis = new DialogoEstadisticas(this, false, Procesador.modeloTablaTestsCorregidos.getRowCount(),
                    Procesador.getEstadAprobados(), Procesador.getEstadNotas(), Procesador.getEstadPregs());
            estadis.setVisible(true);
        } else {
            JOptionPane.showOptionDialog(rootPane, idioma.getString("DialogoEvaluar.error.nohayCorreccion.text"), idioma.getString("Atencion.text"),
                    JOptionPane.NO_OPTION, JOptionPane.WARNING_MESSAGE, null, Config.OPCION_ACEPTAR, null);
        }
    }//GEN-LAST:event_btnEstadisticasActionPerformed

    private void btnEvaluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEvaluarActionPerformed
        // Evaluar tests
        DialogoParamCalific ventaCorregir = new DialogoParamCalific(this, true);
        ventaCorregir.setVisible(true);
        // Si pulsó corregir, lo hago. No si cerró el formulario, paso los valores
        // leidos para el calculo de la calificadion
        if (ventaCorregir.isCorregir()) {
            String[] mensajes = Procesador.CorregiryPuntuar();
            if (!"".equals(mensajes[0])) {
                // Hubo un error u otro mensaje
                JOptionPane.showOptionDialog(this.getContentPane(), idioma.getString(mensajes[0]), idioma.getString(mensajes[1]),
                        JOptionPane.NO_OPTION, Integer.parseInt(mensajes[2]), null, Config.OPCION_ACEPTAR, null);
            }
            if (!"Error.text".equals(mensajes[1])) {
                // Si no hubo error, marco que se ha corregido ya
                heCorregido = true;
            }
        } else {
            heCorregido = false;
        }

    }//GEN-LAST:event_btnEvaluarActionPerformed

    private void cargarExamenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cargarExamenActionPerformed
        // Muestro la lista de exámenes guardados, y leo el que seleccione

        DialogoLeerExamen ventaLeer = new DialogoLeerExamen(this, true);
        // Si no hay exámenes en la tabla, no muestro el diálogo
        if (ventaLeer.isHayExamenes()) {
            ventaLeer.setVisible(true);
            int idExamen = ventaLeer.getexamenSeleccionado();
            // Leo los test del examen elegido de la Base de datos

            MiModeloTabla modelo = Procesador.CargaTestsExamen(idExamen);
            if (modelo != null && modelo.getRowCount() > 0) {
                // pongo el nuevo modelo
                tablaTestsCorregidos.setModel(modelo);
                // Pongo el nombre del test
                etqNombreExamen.setText(idioma.getString("DialogoEvaluar.etqNombreExamen.text").concat(ventaLeer.getNombreExamenSeleccionado()));
                // Actualiza la configuración con los datos de tipos y equivalencias nuevos
                Config.setNumPreguntas(ventaLeer.getNumPreguntas());
                Config.setNumTipos(ventaLeer.getNumTipos());
                Config.guardarConfiguracion();
                // Vuelvo a formatear las columnas de la tabla de tests
                formateaColumnas(tablaTestsCorregidos);
                // Leo la tabla de equivalencias del examen dado, en el modelo de tablatipos
                Procesador.CargaEquivalenciasyTipos(idExamen);
            }
        } else {
            JOptionPane.showOptionDialog(rootPane, idioma.getString("BaseDatos.error.nohayguardados.text"), idioma.getString("Error.text"),
                    JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
            ventaLeer.dispose();
        }
        heCorregido = false;
    }//GEN-LAST:event_cargarExamenActionPerformed

    private void guardarExamenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarExamenActionPerformed
        // Guardo el examen completo en la base de datos
        //
        if (Procesador.modeloTablaTestsCorregidos.getRowCount() > 0) {
            DialogoGuardarExamen ventaGuardar = new DialogoGuardarExamen(this, true);
            ventaGuardar.setVisible(true);
            Procesador.nombreExamen = ventaGuardar.getNombreExamen();
            Procesador.descrExamen = ventaGuardar.getDescrExamen();
            etqNombreExamen.setText(idioma.getString("DialogoEvaluar.etqNombreExamen.text") + Procesador.nombreExamen);
            if (!Procesador.nombreExamen.isEmpty()) {
                BaseDatos.GuardarExamen(Procesador.nombreExamen, Procesador.descrExamen, Procesador.modeloTablaTestsCorregidos, Procesador.getModeloRespTipos(), Config.getNumPreguntas(), Config.getNumTipos());
            } else {
                JOptionPane.showOptionDialog(rootPane, idioma.getString("BaseDatos.error.indices.noguardados.text"), idioma.getString("Error.text"),
                        JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
            }
        } else {
            JOptionPane.showOptionDialog(rootPane, idioma.getString("VentanaInicio.btnGuardarExamen.error.text"), idioma.getString("Atencion.text"),
                    JOptionPane.NO_OPTION, JOptionPane.WARNING_MESSAGE, null, Config.OPCION_ACEPTAR, null);
        }
    }//GEN-LAST:event_guardarExamenActionPerformed

    private void importarExamenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importarExamenActionPerformed
        // Cargo la tabla desde un archivo csv
        // Borro todos los test de la tabla. Uso un JOptionPane personalizado que cambia el idioma, en en array de opciones de botón. Por defecto, paso el botón cancelar

        int loqueDice = JOptionPane.showOptionDialog(this, idioma.getString("VentanaInicio.aviso.borrar.lista.tests.text"), idioma.getString("Atencion.text"),
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
                // Cargo el CSV y actualizo los campos del formulario
                String[] mensajes = Procesador.importarCSV(SelectorFichero.getSelectedFile(), (DefaultTableModel) tablaTestsCorregidos.getModel(),
                        tablaTestsCorregidos.getColumnCount(), Config.MAX_NUM_TEST, Config.CSV_TEST_CORREGIDOS);
                if (!"".equals(mensajes[0])) {
                    // Hubo un error u otro mensaje
                    JOptionPane.showOptionDialog(this.getContentPane(), idioma.getString(mensajes[0]), idioma.getString(mensajes[1]),
                            JOptionPane.NO_OPTION, Integer.parseInt(mensajes[2]), null, Config.OPCION_ACEPTAR, null);
                }
            }
            heCorregido = false;
        }
    }//GEN-LAST:event_importarExamenActionPerformed

    private void exportarExamenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportarExamenActionPerformed
        // Guardo en un archivo csv
        // Guardo primero los cambios en la estructura, si los hubo. Si es correcto guardo el fichero

        if (tablaTestsCorregidos.getRowCount() > 0) {
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
                String[] mensajes = Procesador.exportarCSV(SelectorFichero.getSelectedFile(), (DefaultTableModel) tablaTestsCorregidos.getModel());
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
    }//GEN-LAST:event_exportarExamenActionPerformed

    private void borrarTestsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarTestsActionPerformed
        // Borro todos los test de la tabla. Uso un JOptionPane personalizado que cambia el idioma, en en array de opciones de botón. Por defecto, paso el botón cancelar

        int loqueDice = JOptionPane.showOptionDialog(this, idioma.getString("VentanaInicio.aviso.borrar.lista.tests.text"), idioma.getString("Atencion.text"),
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, Config.OPCIONES_ACEPTAR_CANCELAR,
                Config.OPCIONES_ACEPTAR_CANCELAR[Config.OPCIONES_ACEPTAR_CANCELAR.length - 1]);
        // El primer botón, el 0 es aceptar
        if (loqueDice == 0) {
            // Borro los test que hay en la tabla
            log.info("Borró los tests de la tabla");
            Procesador.modeloTablaTestsCorregidos.setRowCount(0);
            heCorregido = false;
        }
    }//GEN-LAST:event_borrarTestsActionPerformed

    private void btnAyudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAyudaActionPerformed
        String error = Procesador.mostrarAyuda(Config.getRutaAyudaEvaluaciones());
        if (!"".equals(error)) {
            log.error(error);
            JOptionPane.showOptionDialog(rootPane, error, idioma.getString("Error.text"),
                JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{idioma.getString("Aceptar.text")}, idioma.getString("Aceptar.text"));
        }
        
        
    }//GEN-LAST:event_btnAyudaActionPerformed

//    private void mostrarAyuda() {
//        String error = Procesador.mostrarAyuda(Config.getRutaAyudaEvaluaciones());
//        if (!"".equals(error)) {
//            log.error(error);
//            JOptionPane.showOptionDialog(rootPane, error, idioma.getString("Error.text"),
//                    JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{idioma.getString("Aceptar.text")}, idioma.getString("Aceptar.text"));
//        }
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem borrarTests;
    private javax.swing.JButton btnAyuda;
    private javax.swing.JButton btnEstadisticas;
    private javax.swing.JButton btnEvaluar;
    private javax.swing.JButton btnTiposEquiv;
    private javax.swing.JMenuItem cargarExamen;
    private javax.swing.JLabel etqInfoDobleClick;
    private javax.swing.JLabel etqNombreExamen;
    private javax.swing.JMenuItem exportarExamen;
    private javax.swing.JMenuItem guardarExamen;
    private javax.swing.JMenuItem importarExamen;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenu menuArchivo;
    private javax.swing.JMenu menuOpciones;
    private javax.swing.JMenuBar menuPrinciaplBarra;
    private javax.swing.JPanel panelBotones;
    private javax.swing.JPopupMenu.Separator separador;
    private javax.swing.JTable tablaTestsCorregidos;
    // End of variables declaration//GEN-END:variables
}
