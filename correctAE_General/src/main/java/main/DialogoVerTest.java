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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Jesus.delBuey
 */
public class DialogoVerTest extends javax.swing.JDialog {

    BufferedImage imagenTest;
    protected Loguero log = Procesador.getLog();
    ResourceBundle idioma = Procesador.idioma;
    List<Casilla> casillasMarcadas;

    /**
     * Creates new form DialogoVerTest
     *
     * @param parent Padre del diálogo
     * @param modal Si es modal o no
     * @param fichero Fichero que contiene el archivo de imagen del test
     * @param casillasM Lista de casillas marcadas
     */
    public DialogoVerTest(java.awt.Frame parent, boolean modal, File fichero, List<Casilla> casillasM) {
        super(parent, modal);
        initComponents();
        InicializarFormulario();
        // En la llamada ya se ha comprobado que existe el fichero, pero puede fallar la carga
        this.casillasMarcadas = casillasM;
        try {
            // Obtengo el numero de fila seleccionado en la tabla de test del padre para acceder al test de la lista de Tests en la clase Principal
            this.setTitle("TEST " + fichero.getName());
            cargarImagen(fichero);
        } catch (Exception ex) {
            log.info(idioma.getString("DialogoVerTest.noExiste.text"));
            this.dispose();
            JOptionPane.showOptionDialog(rootPane, idioma.getString("DialogoVerTest.noExiste.text"), idioma.getString("Error.text"),
                    JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
        }
    }

    private void InicializarFormulario() {
        // Coloco el formulario en el centro de la pantalla tamaño vertical máximo
        // que permita la pantalla
        this.setSize(new Dimension(this.getWidth(), Procesador.getAltoPantalla() - 50));
        // Coloco el formulario en el centro de la pantalla
        Procesador.Centrame(this);
        this.setLocation(this.getLocation().x, 5);
    }

    private void cargarImagen(File fichero) {
        int mitadAncho = Config.getAnchoMarcasRespuesta() / 2;

        // Cargo la imagen del test enviado
        Procesador.cargarImagen(fichero);
        imagenTest = Procesador.imagenCorregida();
        if (imagenTest != null && fichero != null) {
            if (casillasMarcadas != null) {
                Graphics2D g = imagenTest.createGraphics();// .getGraphics();
                g.setColor(Color.RED);
                g.setStroke(new BasicStroke(Config.getGrosorCirculoMarca()));
                for (Casilla unPunto : casillasMarcadas) {
                    //g.drawRect(unPunto.getCoordX() - mitadAncho, unPunto.getCoordY() - mitadAncho, Config.getAnchoMarcasRespuesta(), Config.getAnchoMarcasRespuesta());
                    g.drawOval(unPunto.getCoordX() - mitadAncho, unPunto.getCoordY() - mitadAncho, Config.getAnchoMarcasRespuesta(), Config.getAnchoMarcasRespuesta());
                }
                laImagen.setIcon(new ImageIcon(imagenIconPorCiento(imagenTest)));
                rutaArchivo.setText(idioma.getString("VentanaTest.rutaArchivo.text") + fichero.getAbsolutePath());
                repaint();
                g.dispose();
            } else {
                JOptionPane.showOptionDialog(rootPane, idioma.getString("VENTANAPRUEBASTESTS.btnAnalizar.ERROR_FICHERO_PLANTILLA.TEXT"), idioma.getString("Error.text"),
                        JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
            }
        } else {
            JOptionPane.showOptionDialog(rootPane, idioma.getString("VENTANAPRUEBASTESTS.btnAnalizar.ERROR_SIN_IMAGEN_O_LIMITE.TEXT"), idioma.getString("Error.text"),
                    JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
        }
    }

    /**
     *
     * @param imagen Imagen original
     * @return imagen redimensionada, según el tamaño del contenedor "laImagen"
     */
    private BufferedImage imagenIconPorCiento(BufferedImage imagen) {
        int ancho = laImagen.getWidth();
        int alto = (imagen.getHeight() * laImagen.getWidth() / imagen.getWidth());

        BufferedImage resultado = new BufferedImage(ancho, alto, imagen.getType());
        Graphics2D graphics2D = resultado.createGraphics();
        graphics2D.drawImage(imagen, 0, 0, ancho, alto, null);
        graphics2D.dispose();
        return resultado;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rutaArchivo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        laImagen = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(950, 1200));

        rutaArchivo.setFont(Config.FUENTE_NORMAL);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("propiedades/Idioma"); // NOI18N
        rutaArchivo.setText(bundle.getString("DialogoVerTest.rutaArchivo.text")); // NOI18N

        jScrollPane1.setViewportView(laImagen);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 882, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(rutaArchivo)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 811, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rutaArchivo)
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
            java.util.logging.Logger.getLogger(DialogoVerTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogoVerTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogoVerTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogoVerTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            DialogoVerTest dialog = new DialogoVerTest(new javax.swing.JFrame(), true, null, null);
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
    private javax.swing.JLabel laImagen;
    private javax.swing.JLabel rutaArchivo;
    // End of variables declaration//GEN-END:variables
}
