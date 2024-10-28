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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ResourceBundle;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author Jesus.delBuey
 */
public class DialogoLicencia extends javax.swing.JDialog {

    ResourceBundle idioma = Procesador.idioma;
    // Log de la aplicación
    private static final Loguero log = Procesador.getLog();

    /**
     * Creates new form DialogoLicencia
     *
     * @param parent Padre del diálogo, Ventana Inicial
     * @param modal Si es modal o no
     */
    public DialogoLicencia(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        InicializarFormulario();
    }

    public DialogoLicencia(JDialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
        InicializarFormulario();
    }

    private void InicializarFormulario() {
        // Pongo el icono de la aplicación
        this.setIconImage(Config.getIconoAplic().getImage());
        // Coloco el formulario en el centro de la pantalla
        Procesador.Centrame(this);
        // Cargo el texto de la licencia
        try {
            InputStream textoPlano = this.getClass().getResourceAsStream("LICENSE");
            if (textoPlano != null) {
                try (BufferedReader lector = new BufferedReader(new InputStreamReader(textoPlano))) {
                    String linea;
                    while ((linea = lector.readLine()) != null) {
                        textoLicencia.append("          ".concat(linea.concat(System.lineSeparator())));
                    }
                }
                textoLicencia.setCaretPosition(0);
                this.pack();
                this.repaint();
            } else {
                log.error(idioma.getString("DialogoVerLicencia.noExiste.text"));
                JOptionPane.showOptionDialog(rootPane, idioma.getString("DialogoVerLicencia.noExiste.text"),
                        idioma.getString("Error.text"), JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
            }
        } catch (IOException ex) {
            log.error(ex.getLocalizedMessage());
            JOptionPane.showOptionDialog(rootPane, idioma.getString("DialogoVerLicencia.noExiste.text") + ": " + ex.getLocalizedMessage(),
                    idioma.getString("Error.text"), JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
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
        textoLicencia = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("propiedades/Idioma"); // NOI18N
        setTitle(bundle.getString("DialogoLicencia.titulo.text")); // NOI18N

        textoLicencia.setEditable(false);
        textoLicencia.setColumns(20);
        textoLicencia.setFont(Config.FUENTE_NORMAL);
        textoLicencia.setRows(5);
        jScrollPane1.setViewportView(textoLicencia);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 701, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DialogoLicencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogoLicencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogoLicencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogoLicencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            DialogoLicencia dialog = new DialogoLicencia(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea textoLicencia;
    // End of variables declaration//GEN-END:variables
}
