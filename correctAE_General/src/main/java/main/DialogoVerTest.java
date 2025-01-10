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

import java.awt.AWTEvent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JTable;

/**
 *
 * @author Jesus.delBuey
 */
public class DialogoVerTest extends javax.swing.JDialog {

    private boolean imagenCargada;
    private JTable tablaOrigen;

    /**
     *
     * @return Si la imagen se ha cargado correctamente
     */
    public boolean isImagenCargada() {
        return imagenCargada;
    }

    private File fichero;

    protected Loguero log = Procesador.getLog();
    ResourceBundle idioma = Procesador.idioma;
    List<Casilla> casillasMarcadas;
    int indiceActual;
    // Para escucher los eventos de teclado
    AWTEventListener flechasListener;
    JScrollBar barraVertical;

    // TAmaño de la imagen que se ve en el formulario, más pequeña que la real
    private int altoImagenPresentacion;
    private int anchoImagenPresentacion;

    /**
     * Creates new form DialogoVerTest
     *
     * @param parent Padre del diálogo
     * @param modal Si es modal o no
     * @param unaTabla Tabla origen del elemento padre
     */
    //public DialogoVerTest(java.awt.Frame parent, boolean modal, File fichero, List<Casilla> casillasM) {
    public DialogoVerTest(java.awt.Frame parent, boolean modal, JTable unaTabla) {
        super(parent, modal);
        this.imagenCargada = false;
        tablaOrigen = unaTabla;
        indiceActual = tablaOrigen.getSelectedRow();
        initComponents();
        InicializarFormulario();
    }

    private void InicializarFormulario() {
        // En la llamada ya se ha comprobado que existe el fichero, pero puede fallar la carga
        this.casillasMarcadas = Procesador.listaTestsLeidos.get(indiceActual).getCasillasMarcadas();
        try {
            // Valores de tamaño de la imagen para mostrar
            altoImagenPresentacion = etqLaImagen.getHeight();
            // Ajusto el ancho proporcionalmente
            anchoImagenPresentacion = (Config.ANCHO_MODELO * etqLaImagen.getHeight() / Config.ALTO_MODELO);
            // Obtengo el numero de fila seleccionado en la tabla de test del padre para acceder al test de la lista de Tests en la clase Principal
            this.fichero = new File(Procesador.listaTestsLeidos.get(indiceActual).getNombreArchivo());
            this.setTitle("TEST " + fichero.getName());
            cargaLaImagen(this.fichero);
            imagenCargada = true;
            // Coloco el formulario en el centro de la pantalla tamaño vertical máximo
            // que permita la pantalla
            this.setSize(new Dimension(anchoImagenPresentacion + 200, Procesador.getAltoPantalla() - 50));
            // Coloco el formulario en el centro de la pantalla
            Procesador.Centrame(this);
            this.setLocation(this.getLocation().x, 5);
            this.barraVertical = jScrollPane1.getVerticalScrollBar();

            // Escucho los eventos de teclado para avanzar y retroceder en la tabla de test
            // Tambien para hacer scroll en la imagen
            //
            this.flechasListener = (AWTEvent event) -> {
                // Como se usa la máscara AWTEvent.KEY_EVENT_MASK nunca va a fallar
                KeyEvent keyEvent = (KeyEvent) event;
                // Al soltar la tecla, evaluo cual es
                if (keyEvent.getID() == KeyEvent.KEY_RELEASED) {
                    // Si está activa el check, al pulsar las flechas muevo las casillas
                    switch (keyEvent.getKeyCode()) {
                        case KeyEvent.VK_LEFT ->
                            btnAnteriorActionPerformed(new java.awt.event.ActionEvent(event, 1, "anterior"));
                        case KeyEvent.VK_RIGHT ->
                            btnSiguienteActionPerformed(new java.awt.event.ActionEvent(event, 1, "siguiente"));
                        case KeyEvent.VK_DOWN ->
                            jScrollPane1.getVerticalScrollBar().setValue(barraVertical.getValue() + barraVertical.getUnitIncrement(1));
                        case KeyEvent.VK_UP ->
                            jScrollPane1.getVerticalScrollBar().setValue(barraVertical.getValue() - barraVertical.getUnitIncrement(1));
                        case KeyEvent.VK_PAGE_DOWN ->
                            jScrollPane1.getVerticalScrollBar().setValue(barraVertical.getMaximum());
                        case KeyEvent.VK_PAGE_UP ->
                            jScrollPane1.getVerticalScrollBar().setValue(barraVertical.getMinimum());
                    }
                }
            };
            // Escucho los eventos globales. Con la máscara de sólo eventos de teclado
            Toolkit.getDefaultToolkit().addAWTEventListener(flechasListener, AWTEvent.KEY_EVENT_MASK);
            //
        } catch (Exception ex) {
            log.info(idioma.getString("DialogoVerTest.noExiste.text"));
            JOptionPane.showOptionDialog(rootPane, idioma.getString("DialogoVerTest.noExiste.text") + ex.getLocalizedMessage(),
                    idioma.getString("Error.text"),
                    JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
            imagenCargada = false;
        }
    }

    private void cargaLaImagen(File fichero) {
        BufferedImage laImg;
        int mitadAncho = Config.getAnchoMarcasRespuesta() / 2;

        // Cargo la imagen del test enviado
        Procesador.cargarImagen(fichero);
        laImg = Procesador.imagenCorregidaAnalizadaExtraida();
        if (laImg != null && fichero != null) {
            if (casillasMarcadas != null) {
                Graphics2D g = laImg.createGraphics();
                g.setColor(Color.RED);
                g.setStroke(new BasicStroke(Config.getGrosorCirculoMarca()));
                for (Casilla unPunto : casillasMarcadas) {
                    g.drawOval(unPunto.getCoordX() - mitadAncho, unPunto.getCoordY() - mitadAncho, Config.getAnchoMarcasRespuesta(), Config.getAnchoMarcasRespuesta());
                }
                etqLaImagen.setIcon(new ImageIcon(Procesador.imagenIconReducida(laImg, this.anchoImagenPresentacion, this.altoImagenPresentacion)));
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
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelPrincipal = new javax.swing.JPanel();
        panelBotonesImagen = new javax.swing.JPanel();
        panelBotones = new javax.swing.JPanel();
        btnAnterior = new javax.swing.JButton();
        btnSiguiente = new javax.swing.JButton();
        panelImagen = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        etqLaImagen = new javax.swing.JLabel();
        panelRutaArchivo = new javax.swing.JPanel();
        rutaArchivo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(950, 1200));

        panelBotones.setLayout(new java.awt.BorderLayout(5, 5));

        btnAnterior.setFont(Config.FUENTE_NORMAL);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("propiedades/Idioma"); // NOI18N
        btnAnterior.setText(bundle.getString("DialogoVerTest.btnAnterior.text")); // NOI18N
        btnAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnteriorActionPerformed(evt);
            }
        });
        panelBotones.add(btnAnterior, java.awt.BorderLayout.CENTER);

        btnSiguiente.setFont(Config.FUENTE_NORMAL);
        btnSiguiente.setText(bundle.getString("DialogoVerTest.btnSiguiente.text")); // NOI18N
        btnSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSiguienteActionPerformed(evt);
            }
        });
        panelBotones.add(btnSiguiente, java.awt.BorderLayout.PAGE_START);

        javax.swing.GroupLayout panelBotonesImagenLayout = new javax.swing.GroupLayout(panelBotonesImagen);
        panelBotonesImagen.setLayout(panelBotonesImagenLayout);
        panelBotonesImagenLayout.setHorizontalGroup(
            panelBotonesImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBotonesImagenLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelBotones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelBotonesImagenLayout.setVerticalGroup(
            panelBotonesImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBotonesImagenLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(645, Short.MAX_VALUE))
        );

        panelImagen.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        etqLaImagen.setMinimumSize(new java.awt.Dimension(792, 800));
        jScrollPane1.setViewportView(etqLaImagen);

        javax.swing.GroupLayout panelImagenLayout = new javax.swing.GroupLayout(panelImagen);
        panelImagen.setLayout(panelImagenLayout);
        panelImagenLayout.setHorizontalGroup(
            panelImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelImagenLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 718, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelImagenLayout.setVerticalGroup(
            panelImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelImagenLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        panelRutaArchivo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        rutaArchivo.setFont(Config.FUENTE_NORMAL);
        rutaArchivo.setText(bundle.getString("DialogoVerTest.rutaArchivo.text")); // NOI18N

        javax.swing.GroupLayout panelRutaArchivoLayout = new javax.swing.GroupLayout(panelRutaArchivo);
        panelRutaArchivo.setLayout(panelRutaArchivoLayout);
        panelRutaArchivoLayout.setHorizontalGroup(
            panelRutaArchivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRutaArchivoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rutaArchivo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelRutaArchivoLayout.setVerticalGroup(
            panelRutaArchivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRutaArchivoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rutaArchivo)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelPrincipalLayout = new javax.swing.GroupLayout(panelPrincipal);
        panelPrincipal.setLayout(panelPrincipalLayout);
        panelPrincipalLayout.setHorizontalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPrincipalLayout.createSequentialGroup()
                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelPrincipalLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelRutaArchivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelPrincipalLayout.createSequentialGroup()
                        .addComponent(panelBotonesImagen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelImagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelPrincipalLayout.setVerticalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelBotonesImagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelImagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelRutaArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguienteActionPerformed
        // Cargo el test siguiente, compruebo que exista el fichero

        if (indiceActual < Procesador.listaTestsLeidos.size() - 1) {
            indiceActual++;
            tablaOrigen.setRowSelectionInterval(indiceActual, indiceActual);
            this.casillasMarcadas = Procesador.listaTestsLeidos.get(indiceActual).getCasillasMarcadas();
            try {
                String fichi = Procesador.listaTestsLeidos.get(indiceActual).getNombreArchivo();
                File fich = new File(fichi);
                if (fich.exists()) {
                    // Obtengo el numero de fila seleccionado en la tabla de test del padre para acceder al test de la lista de Tests del modelo
                    File fichero = new File(Procesador.listaTestsLeidos.get(indiceActual).getNombreArchivo());
                    this.setTitle("TEST " + fichero.getName());
                    cargaLaImagen(fichero);
                }
            } catch (Exception ex) {
                log.info(idioma.getString("DialogoVerTest.noExiste.text"));
                this.dispose();
                JOptionPane.showOptionDialog(rootPane, idioma.getString("DialogoVerTest.noExiste.text"), idioma.getString("Error.text"),
                        JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
            }
        }
    }//GEN-LAST:event_btnSiguienteActionPerformed

    private void btnAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnteriorActionPerformed
        // Cargo el test anterior, compruebo que exista el fichero

        if (indiceActual > 0) {
            indiceActual--;
            tablaOrigen.setRowSelectionInterval(indiceActual, indiceActual);
            this.casillasMarcadas = Procesador.listaTestsLeidos.get(indiceActual).getCasillasMarcadas();
            try {
                String fichi = Procesador.listaTestsLeidos.get(indiceActual).getNombreArchivo();
                File fich = new File(fichi);
                if (fich.exists()) {
                    // Obtengo el numero de fila seleccionado en la tabla de test del padre para acceder al test de la lista de Tests del modelo
                    File fichero = new File(Procesador.listaTestsLeidos.get(indiceActual).getNombreArchivo());
                    this.setTitle("TEST " + fichero.getName());
                    cargaLaImagen(fichero);
                }
            } catch (Exception ex) {
                log.info(idioma.getString("DialogoVerTest.noExiste.text"));
                this.dispose();
                JOptionPane.showOptionDialog(rootPane, idioma.getString("DialogoVerTest.noExiste.text"), idioma.getString("Error.text"),
                        JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
            }
        }
    }//GEN-LAST:event_btnAnteriorActionPerformed

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
//        java.awt.EventQueue.invokeLater(() -> {
//            DialogoVerTest dialog = new DialogoVerTest(new javax.swing.JFrame(), true, null, null);
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
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnSiguiente;
    private javax.swing.JLabel etqLaImagen;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelBotones;
    private javax.swing.JPanel panelBotonesImagen;
    private javax.swing.JPanel panelImagen;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JPanel panelRutaArchivo;
    private javax.swing.JLabel rutaArchivo;
    // End of variables declaration//GEN-END:variables
}
