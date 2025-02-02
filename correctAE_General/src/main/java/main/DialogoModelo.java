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
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.DialogTypeSelection;
import javax.print.attribute.standard.PrinterIsAcceptingJobs;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Jesus.delBuey
 */
public class DialogoModelo extends javax.swing.JDialog {

    BufferedImage imgCargo;
    ModeloImprimible modelo;

    /**
     * Creates new form DdialogoModelo
     *
     * @param parent Padre del diálogo
     * @param modal Si es modal o no
     */
    public DialogoModelo(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        super.setIconImage(parent.getIconImage());
        initComponents();
        InicializarFormulario();
    }

    private void InicializarFormulario() {
        // Coloco el formulario en el centro de la pantalla tamaño vertical máximo
        // que permita la pantalla
        this.setPreferredSize(new Dimension(this.getWidth(), Procesador.getAltoPantalla() - 50));
        Procesador.Centrame(this);
        this.setLocation(this.getLocation().x, 5);

        //
        // Asocio un evento de teclado F1 para lanzar la ayuda
        Procesador.asociaAyudaF1(btnAyuda, Config.getRutaAyudaHojaRespuestas());
        // Configuro el diálogo de impresión
        float tama = (float) Config.FUENTE_TITULO.getSize() * 3;
        etqDialogoImprimiendo.setFont(Config.FUENTE_TITULO.deriveFont(tama).deriveFont(Font.BOLD));

        // Cargo el modelo de plantilla en pantalla
        try {
            imgCargo = ImageIO.read(this.getClass().getResourceAsStream(Config.getFICHERO_MODELO_TEST()));
            int anchoPresentacion = (Math.abs(mainPanel.getWidth() - 15));
            int altoPresentacion = (int) ((float) imgCargo.getHeight() * anchoPresentacion / imgCargo.getWidth());
            // En el escaldo prograsivo sólo uso el lado largo como referencia
            BufferedImage imagenTest = Procesador.cambiaTamano(imgCargo, altoPresentacion);
            modelo = new ModeloImprimible(imagenTest);
            modelo.setBounds(0, 0, anchoPresentacion, altoPresentacion);
            panelCapas.add(modelo, JLayeredPane.DEFAULT_LAYER); // Pongo las cajas en la capa 100, palette_layer
            pack();
            // Oculto el mensaje de Imprimiendo...
            panelImprimiendo.setVisible(false);
        } catch (IOException e) {
            Config.getLog().aviso(e.getMessage());
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

        etqNombreModelo = new javax.swing.JLabel();
        btnCambiarLogo = new javax.swing.JButton();
        btnImprimirModelo = new javax.swing.JButton();
        btnAyuda = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        mainPanel = new javax.swing.JPanel();
        panelCapas = new javax.swing.JLayeredPane();
        panelImprimiendo = new javax.swing.JPanel();
        etqDialogoImprimiendo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("propiedades/Idioma"); // NOI18N
        setTitle(bundle.getString("VentanaModelo.Titulo.Text")); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        etqNombreModelo.setFont(Config.FUENTE_NORMAL);
        etqNombreModelo.setText(bundle.getString("DialogoModelo.etqNombreModelo.text")); // NOI18N

        btnCambiarLogo.setFont(Config.FUENTE_NORMAL);
        btnCambiarLogo.setText(bundle.getString("DialogoModelo.btnCambiarLogo.text")); // NOI18N
        btnCambiarLogo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambiarLogoActionPerformed(evt);
            }
        });

        btnImprimirModelo.setFont(Config.FUENTE_NORMAL);
        btnImprimirModelo.setText(bundle.getString("DialogoModelo.btnImprimirModelo.text")); // NOI18N
        btnImprimirModelo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirModeloActionPerformed(evt);
            }
        });

        btnAyuda.setBackground(new java.awt.Color(61, 117, 105));
        btnAyuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/main/Azul_ayuda_chic.png"))); // NOI18N
        btnAyuda.setBorder(null);
        btnAyuda.setBorderPainted(false);
        btnAyuda.setContentAreaFilled(false);
        btnAyuda.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/main/Azul_brillo_ayuda_chic.png"))); // NOI18N
        btnAyuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAyudaActionPerformed(evt);
            }
        });

        jScrollPane1.setPreferredSize(new java.awt.Dimension(1020, 800));

        mainPanel.setOpaque(false);
        mainPanel.setPreferredSize(new java.awt.Dimension(896, 1360));

        panelCapas.setPreferredSize(new java.awt.Dimension(429, 500));

        panelImprimiendo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        etqDialogoImprimiendo.setFont(Config.FUENTE_TITULO);
        etqDialogoImprimiendo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etqDialogoImprimiendo.setText(bundle.getString("DialogoModelo.etqDialogoImprimiendo.text")); // NOI18N

        javax.swing.GroupLayout panelImprimiendoLayout = new javax.swing.GroupLayout(panelImprimiendo);
        panelImprimiendo.setLayout(panelImprimiendoLayout);
        panelImprimiendoLayout.setHorizontalGroup(
            panelImprimiendoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelImprimiendoLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(etqDialogoImprimiendo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        panelImprimiendoLayout.setVerticalGroup(
            panelImprimiendoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelImprimiendoLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(etqDialogoImprimiendo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        panelCapas.setLayer(panelImprimiendo, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout panelCapasLayout = new javax.swing.GroupLayout(panelCapas);
        panelCapas.setLayout(panelCapasLayout);
        panelCapasLayout.setHorizontalGroup(
            panelCapasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCapasLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelImprimiendo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(285, 285, 285))
        );
        panelCapasLayout.setVerticalGroup(
            panelCapasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCapasLayout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(panelImprimiendo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelCapas, javax.swing.GroupLayout.DEFAULT_SIZE, 996, Short.MAX_VALUE)
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelCapas, javax.swing.GroupLayout.DEFAULT_SIZE, 1348, Short.MAX_VALUE)
                .addContainerGap())
        );

        jScrollPane1.setViewportView(mainPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(etqNombreModelo)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(btnCambiarLogo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnImprimirModelo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAyuda)
                        .addGap(20, 20, 20))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnImprimirModelo)
                        .addComponent(etqNombreModelo)
                        .addComponent(btnCambiarLogo))
                    .addComponent(btnAyuda))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnImprimirModeloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirModeloActionPerformed
        // Muestro el diálogo de impresión
        dialogoImpresion();
    }//GEN-LAST:event_btnImprimirModeloActionPerformed

    private void btnAyudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAyudaActionPerformed
        String error = Procesador.mostrarAyuda(Config.getRutaAyudaHojaRespuestas());
        if (!"".equals(error)) {
            Config.getLog().error(error);
            JOptionPane.showOptionDialog(rootPane, error, Config.getIdioma().getString("Error.text"),
                    JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{Config.getIdioma().getString("Aceptar.text")}, Config.getIdioma().getString("Aceptar.text"));
        }
    }//GEN-LAST:event_btnAyudaActionPerformed

    private void btnCambiarLogoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambiarLogoActionPerformed
        // Cambiar el logo
        int quedice = JOptionPane.showOptionDialog(rootPane, Config.getIdioma().getString("DialogoModelo.AvisoCambiarLogo.text"), Config.getIdioma().getString("Atencion.text"),
                JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{Config.getIdioma().getString("Aceptar.text"), Config.getIdioma().getString("Cancelar.text")}, Config.getIdioma().getString("Cancelar.text"));
        if (quedice == 0) {
            DialogoCarpertaFichero SelectorFichero = new DialogoCarpertaFichero();
            // Compruevo y ajusto la escala si es necesario
            SelectorFichero.setFileSelectionMode(JFileChooser.FILES_ONLY); // Para que elija carpeta en lugar de ficheros individuales
            SelectorFichero.setDialogTitle(Config.getIdioma().getString("FileChooser.Fichero.title"));
            SelectorFichero.setCurrentDirectory(new File(Config.getCarpetaArchivosTests()));

            String[] extensions = ImageIO.getReaderFileSuffixes();
            SelectorFichero.setFileFilter(new FileNameExtensionFilter("IMAGE FILES", extensions));
            //SelectorFichero.setSelectedFile(new File(Config.getRutaUltimaImagen()));
            int resultado = SelectorFichero.showOpenDialog(this);
            if (resultado == DialogoCarpertaFichero.APPROVE_OPTION) {
                // Actualizo en Config la última ruta
                Config.setCarpetaArchivosTests(SelectorFichero.getSelectedFile().getAbsolutePath());
                cargarLogo(SelectorFichero.getSelectedFile());
            }
        }
    }//GEN-LAST:event_btnCambiarLogoActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // Muestro la ventana de información.
        if (Procesador.tiempoQueSalgo(20) > 0) {
            new DialogoInfo(this, true, 680, 400, Config.getIdioma().getString("Atencion.text"),
                    Config.getIdioma().getString("DialogoModeloVinfo.text"), Procesador.tiempoQueSalgo(20)).setVisible(true);
        }

    }//GEN-LAST:event_formWindowOpened

    private void cargarLogo(File fichero) {
        BufferedImage nuevoModelo;
        // Cargo un nuevo test en TestActual
        nuevoModelo = Procesador.cargarLogoenModelo(fichero, modelo);
        if (nuevoModelo != null) {
            modelo.setIcon(new ImageIcon(nuevoModelo));
        } else {
            JOptionPane.showOptionDialog(rootPane, Config.getIdioma().getString("DialogoModelo.errorFicheroLogo.text"),
                    Config.getIdioma().getString("Error.text"), JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
        }
    }

    private void dialogoImpresion() {

        // Averiguo que impresoras están disponibles y las Filtro
        PrintService[] availablePrintServices = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService[] acceptingPrintServices = java.util.Arrays.stream(availablePrintServices)
                .filter(printer -> {
                    PrintServiceAttributeSet attributes = printer.getAttributes();
                    Attribute acceptingJobs = attributes.get(PrinterIsAcceptingJobs.class);
                    //Attribute printerState = attributes.get(PrinterState.class);
                    return acceptingJobs != null && acceptingJobs.toString().toLowerCase().equals("accepting-jobs");
                })
                .toArray(PrintService[]::new);

        // Busco la impresora por defecto
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();

        // Crea y devuelve un printerjob que se asocia con la primera impresora disponible
        // del sistema, si no hay produce un error y salgo del método.
        PrinterJob impresion = PrinterJob.getPrinterJob();
        if (acceptingPrintServices.length > 0) {
            try {
                if (service != null) {
                    // Selecciona la primera impresora por defecto si esta entre las disponibles
                    if (Arrays.asList(acceptingPrintServices).contains(service)) {
                        impresion.setPrintService(service);
                    } else {
                        impresion.setPrintService(acceptingPrintServices[0]); // Selecciona la primera impresora disponible
                    }
                } else {
                    impresion.setPrintService(acceptingPrintServices[0]); // Selecciona la primera impresora disponible Si no hay por defecto
                }
            } catch (PrinterException ex) {
                Config.getLog().error(ex.getLocalizedMessage());
                JOptionPane.showOptionDialog(rootPane, Config.getIdioma().getString("VentanaModelo.error.imprimiendo.modelo.Text") + ": " + ex.getMessage(), Config.getIdioma().getString("Error.text"),
                        JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
                return;
            }
        } else {
            Config.getLog().error(Config.getIdioma().getString("VentanaModelo.error.noHayImpresoras.Text"));
            JOptionPane.showOptionDialog(rootPane, Config.getIdioma().getString("VentanaModelo.error.noHayImpresoras.Text"), Config.getIdioma().getString("Error.text"),
                    JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
            return;
        }
        //
        impresion.setPrintable(modelo);
        impresion.setJobName(Config.getIdioma().getString("Impresion.NombreTrabajo.text"));

        //muestra ventana de dialogo para imprimir
        // Muestro el mensaje de Imprimiendo...
        //
        PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
        attr.add(Chromaticity.MONOCHROME); // Blanco y negro
        attr.add(DialogTypeSelection.NATIVE); // Dialogo de impresión nativo
        PageFormat pf = impresion.defaultPage();
        Paper paper = pf.getPaper();

        // Ajustar márgenes
        double margin = 10; // Margen estrecho
        paper.setImageableArea(margin, margin, paper.getWidth() - 2 * margin, paper.getHeight() - 2 * margin);
        pf.setPaper(paper);

        // Muestro el mensaje de Imprimiendo...
        panelImprimiendo.setVisible(true);

        if (impresion.printDialog(attr)) {
            try {
                impresion.print(attr);
            } catch (PrinterException ex) {
                Config.getLog().error("Error:" + ex);
                JOptionPane.showOptionDialog(rootPane, Config.getIdioma().getString("VentanaModelo.error.imprimiendo.modelo.Text") + ": " + ex.getMessage(), Config.getIdioma().getString("Error.text"),
                        JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
            }
        }
        // Oculto el mensaje de Imprimiendo...
        panelImprimiendo.setVisible(false);
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DialogoModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogoModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogoModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogoModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAyuda;
    private javax.swing.JButton btnCambiarLogo;
    private javax.swing.JButton btnImprimirModelo;
    private javax.swing.JLabel etqDialogoImprimiendo;
    private javax.swing.JLabel etqNombreModelo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLayeredPane panelCapas;
    private javax.swing.JPanel panelImprimiendo;
    // End of variables declaration//GEN-END:variables
}
