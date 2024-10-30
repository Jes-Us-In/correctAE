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

import java.awt.event.MouseAdapter;
import java.util.ResourceBundle;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Jesus
 */
public class DialogoLeerExamen extends javax.swing.JDialog {

    ResourceBundle idioma = Procesador.idioma;

    String[] columnasTabla = {idioma.getString("DialogoLeerExamen.col0.header.text"),
        idioma.getString("DialogoLeerExamen.col1.header.text"),
        idioma.getString("DialogoLeerExamen.col2.header.text"),
        idioma.getString("DialogoLeerExamen.col3.header.text"),
        idioma.getString("DialogoLeerExamen.col4.header.text"),
        idioma.getString("DialogoLeerExamen.col5.header.text")
    };
    private MiModeloTabla modeloTablaExamenes;

    /**
     * Get the value of modeloTablaExamenes
     *
     * @return the value of modeloTablaExamenes
     */
    public DefaultTableModel getModeloTablaExamenes() {
        return modeloTablaExamenes;
    }

    /**
     * Set the value of modeloTablaExamenes
     *
     * @param modelo Modelo de la tabla de Exámenes
     */
    public void setModeloTablaExamenes(MiModeloTabla modelo) {
        this.modeloTablaExamenes = modelo;
    }

    private int examenSeleccionado = -1;

    /**
     * Get the value of examenSeleccionado
     *
     * @return el id del examen seleccionado
     */
    public int getexamenSeleccionado() {
        return examenSeleccionado;
    }

    private String nombreExamenSeleccionado = "No puesto Aún";

    /**
     * Get the value of nombreExamenSeleccionado
     *
     * @return the value of nombreExamenSeleccionado
     */
    public String getNombreExamenSeleccionado() {
        return nombreExamenSeleccionado;
    }

    private int numPreguntas;

    /**
     * Get the value of numPreguntas
     *
     * @return the value of numPreguntas
     */
    public int getNumPreguntas() {
        return numPreguntas;
    }

    private int numTipos;

    /**
     * Get the value of numTipos
     *
     * @return the value of numTipos
     */
    public int getNumTipos() {
        return numTipos;
    }

    private boolean hayExamenes = false;

    /**
     * Get the value of hayExamenes
     *
     * @return the value of hayExamenes
     */
    public boolean isHayExamenes() {
        return hayExamenes;
    }

    /**
     * Creates new form DialogoLeerExamen
     *
     * @param parent Padre del diálogo
     * @param modal Si es modal o no
     */
    public DialogoLeerExamen(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        InicializarFormulario();
        // Si la tabla está vacia es false
        hayExamenes = InicializarFormulario();
    }

    public DialogoLeerExamen(JDialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
        InicializarFormulario();
        // Si la tabla está vacia es false
        hayExamenes = InicializarFormulario();
    }

    private boolean InicializarFormulario() {
        // Coloco el formulario en el centro de la pantalla
        Procesador.Centrame(this);
        // MimodeloTabla es una clase DefaultTableModel con la edición de celda deshabilitada
        // Así funciona el doble click. Si no entra en edición y no llega al segundo.
        //modeloTablaExamenes = new DefaultTableModel();
        modeloTablaExamenes = new MiModeloTabla();
        modeloTablaExamenes.setColumnIdentifiers(columnasTabla);
        MiModeloTabla modelo = BaseDatos.CargaListaExamenes(modeloTablaExamenes);
        if (modelo != null && modelo.getRowCount() > 0) {
            tablaExamenes.setModel(modelo);
            TableColumnModel modeloColum = tablaExamenes.getColumnModel();
            modeloColum.getColumn(0).setMaxWidth(30);
            modeloColum.getColumn(1).setMinWidth(110);
            //modeloColum.getColumn(1).setMaxWidth(120);
            modeloColum.getColumn(2).setMinWidth(150);
            modeloColum.getColumn(3).setMinWidth(180);
            // Columnas centradas
            DefaultTableCellRenderer ren = new DefaultTableCellRenderer();
            ren.setHorizontalAlignment(SwingConstants.CENTER);
            //modeloColum.getColumn(4).setMaxWidth(70);
            modeloColum.getColumn(4).setCellRenderer(ren);
            //modeloColum.getColumn(5).setMaxWidth(40);
            modeloColum.getColumn(5).setCellRenderer(ren);
            // Solo puede seleccionar un examen
            tablaExamenes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            // Añado un listener para el doble click sobre una fila de la tabla
            tablaExamenes.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        // Cargo el archivo de imagen del test seleccionado
                        btnCargarExamenActionPerformed(new java.awt.event.ActionEvent(e, 1, "click"));
                    }
                }
            });
            return true;
        } else {
            return false;
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

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaExamenes = new javax.swing.JTable();
        btnCargarExamen = new javax.swing.JButton();
        btnBorrarExamen = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("propiedades/Idioma"); // NOI18N
        setTitle(bundle.getString("DialogoLeerExamen.titulo.text")); // NOI18N

        tablaExamenes.setFont(Config.FUENTE_NORMAL);
        tablaExamenes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tablaExamenes);

        btnCargarExamen.setFont(Config.FUENTE_NORMAL);
        btnCargarExamen.setText(bundle.getString("DialogoLeerExamen.btnCargarExamen.text")); // NOI18N
        btnCargarExamen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarExamenActionPerformed(evt);
            }
        });

        btnBorrarExamen.setFont(Config.FUENTE_NORMAL);
        btnBorrarExamen.setText(bundle.getString("DialogoLeerExamen.btnBorrarExamen.text")); // NOI18N
        btnBorrarExamen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarExamenActionPerformed(evt);
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
                        .addComponent(btnCargarExamen)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(btnBorrarExamen))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 746, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCargarExamen)
                    .addComponent(btnBorrarExamen))
                .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCargarExamenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarExamenActionPerformed
        // Selecciono el examen para Cargar
        if (seleccionarExamen(tablaExamenes.getSelectedRow())) {
            // Salgo del formulario si es correcto
            this.dispose();
        }
    }//GEN-LAST:event_btnCargarExamenActionPerformed

    private void btnBorrarExamenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarExamenActionPerformed
        // Selecciono el examen para Borrar
        if (seleccionarExamen(tablaExamenes.getSelectedRow())) {
            if (BaseDatos.BorrarExamen(modeloTablaExamenes, examenSeleccionado)) {
                // borro la línea del modelo también
                modeloTablaExamenes.removeRow(tablaExamenes.getSelectedRow());
                // Examen borrado correctamente
                JOptionPane.showOptionDialog(rootPane, idioma.getString("DialogoLeerExamen.examen.borrado.text"), idioma.getString("Atencion.text"),
                        JOptionPane.NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, Config.OPCION_ACEPTAR, null);
            }
        }
    }//GEN-LAST:event_btnBorrarExamenActionPerformed

    // Compruebo el examen seleccionado es correcto, almaceno la fila del modelo que tiene el mismo
    // en la propiedad "examenSeleccionado", que debe leer el formulario que llamó a este y salgo
    // en la propiedad "nombreExamenSeleccionado", está el nombre del test
    private boolean seleccionarExamen(int numFila) {
        // Cargo el examen seleccionado y salgo si es todo correcto
        if (numFila >= 0 && numFila < tablaExamenes.getRowCount()) {
            examenSeleccionado = (Integer) (modeloTablaExamenes.getValueAt(tablaExamenes.getSelectedRow(), 0));
            nombreExamenSeleccionado = (String) modeloTablaExamenes.getValueAt(tablaExamenes.getSelectedRow(), 2);
            numPreguntas = (Integer) (modeloTablaExamenes.getValueAt(tablaExamenes.getSelectedRow(), 4));
            numTipos = (Integer) (modeloTablaExamenes.getValueAt(tablaExamenes.getSelectedRow(), 5));
            // Selección correcta
            return true;
        } else {
            examenSeleccionado = -1;
            JOptionPane.showOptionDialog(rootPane, idioma.getString("DialogoLeerExamen.error.mala.seleccion.text"), idioma.getString("Atencion.text"),
                    JOptionPane.NO_OPTION, JOptionPane.WARNING_MESSAGE, null, Config.OPCION_ACEPTAR, null);
        }
        return false;
    }

    /**
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogoLeerExamen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(() -> {
//            DialogoLeerExamen dialog = new DialogoLeerExamen(new javax.swing.JFrame(), true);
//            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                @Override
//                public void windowClosing(java.awt.event.WindowEvent e) {
//                    System.exit(0);
//                }
//            });
//            dialog.setVisible(true);
//        });
        
        //</editor-fold>

        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(() -> {
//            DialogoLeerExamen dialog = new DialogoLeerExamen(new javax.swing.JFrame(), true);
//            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                @Override
//                public void windowClosing(java.awt.event.WindowEvent e) {
//                    System.exit(0);
//                }
//            });
//            dialog.setVisible(true);
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBorrarExamen;
    private javax.swing.JButton btnCargarExamen;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaExamenes;
    // End of variables declaration//GEN-END:variables
}
