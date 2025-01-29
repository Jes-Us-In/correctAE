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

import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author Jesus.delBuey
 */
public class DialogoLicencia extends javax.swing.JDialog {

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
        // Redimensiono al máximo alto disponible y a la mitad del ancho de pantalla
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.setLocation(env.getMaximumWindowBounds().width / 4, 0);
        this.setSize(env.getMaximumWindowBounds().width / 2, env.getMaximumWindowBounds().height);

        for (int i = 0; i < pestanasPanel.getTabCount(); i++) {
            switch (i) {
                case 0 -> {
                    //CorrectAE
                    textoLicencia1.setContentType("text/html");
                    textoLicencia1.setFont(Config.FUENTE_NORMAL);
                    textoLicencia1.setText(cargaLicenciaHTML("LICENSE_GNU_v3"));
                    textoLicencia1.setCaretPosition(0);
                }
                case 1 -> {
                    //Sqlite
                    textoLicencia2.setContentType("text/html");
                    textoLicencia2.setFont(Config.FUENTE_NORMAL);
                    textoLicencia2.setText(cargaLicenciaHTML("LICENSE_Apache_v2"));
                    textoLicencia2.setCaretPosition(0);
                }
                case 2 -> {
                    //jfreeChart
                    textoLicencia3.setContentType("text/html");
                    textoLicencia3.setFont(Config.FUENTE_NORMAL);
                    textoLicencia3.setText(cargaLicenciaHTML("LICENSE_GNU_LGPL_v3"));
                    textoLicencia3.setCaretPosition(0);
                }
                case 3 -> {
                    //AbsoluteLayout
                    textoLicencia4.setContentType("text/html");
                    textoLicencia4.setFont(Config.FUENTE_NORMAL);
                    textoLicencia4.setText(cargaLicenciaHTML("LICENSE_Apache_v2"));
                    textoLicencia4.setCaretPosition(0);
                }
            }
        }
    }

    private String cargaLicenciaHTML(String nombreArchivo) {
        // Cargo el texto de la licencia de CorrectAE
        StringBuilder textoLicencia = new StringBuilder();

        try {
            InputStream textoPlano = this.getClass().getResourceAsStream(nombreArchivo);

            if (textoPlano != null) {
                try (BufferedReader lector = new BufferedReader(new InputStreamReader(textoPlano))) {
                    String linea;
                    while ((linea = lector.readLine()) != null) {
                        //textoLicencia.append("          ".concat(linea.concat(System.lineSeparator())));
                        textoLicencia.append(linea.concat(System.lineSeparator()));
                    }
                }
            } else {
                Config.getLog().error(Config.getIdioma().getString("DialogoVerLicencia.noExiste.text") + ": " + nombreArchivo);
                JOptionPane.showOptionDialog(rootPane, Config.getIdioma().getString("DialogoVerLicencia.noExiste.text") + ": " + nombreArchivo,
                        Config.getIdioma().getString("Error.text"), JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
            }
        } catch (IOException ex) {
            Config.getLog().error(ex.getLocalizedMessage());
            JOptionPane.showOptionDialog(rootPane, Config.getIdioma().getString("DialogoVerLicencia.noExiste.text") + ": " + ex.getLocalizedMessage(),
                    Config.getIdioma().getString("Error.text"), JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
        }
        return textoLicencia.toString();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pestanasPanel = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        textoLicencia1 = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        textoLicencia2 = new javax.swing.JTextPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        textoLicencia3 = new javax.swing.JTextPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        textoLicencia4 = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("propiedades/Idioma"); // NOI18N
        setTitle(bundle.getString("DialogoLicencia.titulo.text")); // NOI18N

        pestanasPanel.setFont(Config.FUENTE_NORMAL);

        jScrollPane1.setFont(Config.FUENTE_NORMAL);
        jScrollPane1.setViewportView(textoLicencia1);

        pestanasPanel.addTab(bundle.getString("DialogoLicencia.CorrectAE"), jScrollPane1); // NOI18N

        jScrollPane2.setFont(Config.FUENTE_NORMAL);
        jScrollPane2.setViewportView(textoLicencia2);

        pestanasPanel.addTab(bundle.getString("DialogoLicencia.Sqlite"), jScrollPane2); // NOI18N

        jScrollPane3.setFont(Config.FUENTE_NORMAL);
        jScrollPane3.setViewportView(textoLicencia3);

        pestanasPanel.addTab(bundle.getString("DialogoLicencia.jfreeChart"), jScrollPane3); // NOI18N

        jScrollPane4.setFont(Config.FUENTE_NORMAL);
        jScrollPane4.setViewportView(textoLicencia4);

        pestanasPanel.addTab(bundle.getString("DialogoLicencia.AbsoluteLayout"), jScrollPane4); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pestanasPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pestanasPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 694, Short.MAX_VALUE)
                .addContainerGap())
        );

        pestanasPanel.getAccessibleContext().setAccessibleName(bundle.getString("DialogoLicencia.Sqlite")); // NOI18N
        pestanasPanel.getAccessibleContext().setAccessibleDescription(bundle.getString("DialogoLicencia.titulo.text")); // NOI18N

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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane pestanasPanel;
    private javax.swing.JTextPane textoLicencia1;
    private javax.swing.JTextPane textoLicencia2;
    private javax.swing.JTextPane textoLicencia3;
    private javax.swing.JTextPane textoLicencia4;
    // End of variables declaration//GEN-END:variables
}
