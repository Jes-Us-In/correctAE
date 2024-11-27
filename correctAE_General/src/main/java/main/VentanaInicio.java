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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.TableColumnModel;
import java.util.ResourceBundle;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import main.estilos.RenderAlineadoFuenteActual;

/**
 *
 * @author Jesus.delBuey
 */
public class VentanaInicio extends javax.swing.JFrame {

    VentanaInicio esteFrame = this;

    static ResourceBundle idioma = Procesador.idioma;
    private static final Loguero log = Procesador.getLog();

    /**
     * Actualiza el aspecto cuando se ha cambiado
     */
    public void actualizarLookAndFeel() {
        SwingUtilities.updateComponentTreeUI(this);
    }

    /**
     * Creates new form VentanaInicio
     */
    public VentanaInicio() {
        // Ventana Splash durante 3 segundos. Luego devuelve el control
        DialogoSplash ventaSplash = new DialogoSplash(this, true);
        ventaSplash.setVisible(true);
        initComponents();
        // Inicializo el formulario
        InicializarFormulario();
    }

    private void InicializarFormulario() {
        // Ajusto el tamanio
        this.setMaximumSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize());

        // Coloco el formulario en el centro de la pantalla
        Procesador.Centrame(this);

        // Asigno el modelo de tabla de los tests leídos
        tablaTests.setModel(Procesador.modeloTablaTestsLeidos);
        // Formateo las columnas de la tabla de tests
        formateaColumnas(tablaTests);

        // Inicializo el idioma
        if (Config.getIdiomaActual().equals("es")) {
            PonIdioma("es", "ES", this.Espanol);
        } else {
            if (Config.getIdiomaActual().equals("en")) {
                PonIdioma("en", "US", this.English);
            }
        }

        // 
        // Añado un listener a la tabla para el doble click sobre una fila
        VentanaInicio pasoPadre = this;
        tablaTests.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {

                if (e.getClickCount() == 2) {
                    // Cargo el archivo de imagen del test seleccionado
                    try {
                        // En la última columna de la tabla está el nombre del fichero con el path completo
                        String fichi = (String) tablaTests.getValueAt(tablaTests.getSelectedRow(), tablaTests.getColumnCount() - 1);
                        //System.out.println("El fichero a leer es : " + fichi);
                        File fich = new File(fichi);
                        if (fich.exists()) {
                            // Paso al Diálogo, ademaás del padre y el boolean, NO es modal, el fichero, que ya se que existe, y las casillas marcadas de dicho test
                            DialogoVerTest ventaTest = new DialogoVerTest(pasoPadre, false, fich, Procesador.listaTestsLeidos.get(tablaTests.getSelectedRow()).getCasillasMarcadas()); // permito abrir varios tests
                            ventaTest.setVisible(true);
                        } else {
                            JOptionPane.showOptionDialog(rootPane, idioma.getString("DialogoVerTest.noExiste.text") + ": " + fich.getCanonicalPath(),
                                    idioma.getString("Error.text"), JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
                        }
                    } catch (IndexOutOfBoundsException | IOException ex) {
                        // Se produce un error al obtener getCanonicalPath, si el nombre de archivo no es correcto
                        log.info(idioma.getString("DialogoVerTest.error.leyendo.archivo.text") + "\n" + ex.getMessage());
                        JOptionPane.showOptionDialog(rootPane, idioma.getString("DialogoVerTest.error.leyendo.archivo.text"), idioma.getString("Error.text"),
                                JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
                    }
                }
            }
        });
    }

    private void formateaColumnas(JTable tabla) {
        // Formateo las columnas de la tabla de tests
        TableColumnModel columnModel = tabla.getColumnModel();
        // Centrar las cabeceras y poner fuente
        DefaultTableCellRenderer renCab = (DefaultTableCellRenderer) tabla.getTableHeader().getDefaultRenderer();
        renCab.setHorizontalAlignment(JLabel.CENTER);
        JTableHeader cab = tabla.getTableHeader();
        cab.setFont(Config.FUENTE_NORMAL);
        // Para centrar el contenido de las cuatro primeras columnas
        // Y poner la fuente del tamaño que esté vigente
        // Las respuestas y la ruta al archivo, las dejo alineadas a la izquierda
        RenderAlineadoFuenteActual ren = new RenderAlineadoFuenteActual(SwingConstants.CENTER);
        for (int columna = 0; columna < columnModel.getColumnCount() - 2; columna++) {
            columnModel.getColumn(columna).setCellRenderer(ren);
        }

        //
        // Número de fila
        columnModel.getColumn(0).setMinWidth(50);
        columnModel.getColumn(0).setMaxWidth(80);
        //
        // DNI
        columnModel.getColumn(1).setMinWidth(128);
        columnModel.getColumn(1).setMaxWidth(260);
        // Tipo
        columnModel.getColumn(2).setMaxWidth(50);
        columnModel.getColumn(2).setMinWidth(50);
        // Grupo
        columnModel.getColumn(3).setMaxWidth(55);
        columnModel.getColumn(3).setMinWidth(55);
        // Respuestas
        columnModel.getColumn(4).setMinWidth(200);
        //Archivo
        columnModel.getColumn(5).setMinWidth(200);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        selectorIdioma = new javax.swing.ButtonGroup();
        etqCarpetaArchivos = new javax.swing.JLabel();
        etqInfoDobleClick = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaTests = new javax.swing.JTable();
        panelBotones = new javax.swing.JPanel();
        btnAgregarTests = new javax.swing.JButton();
        btnEvaluaciones = new javax.swing.JButton();
        btnBorrarTest = new javax.swing.JButton();
        btnMostrarModelo = new javax.swing.JButton();
        menuPrincipalBarra = new javax.swing.JMenuBar();
        menuArchivo = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        menuOpciones = new javax.swing.JMenu();
        probarTest = new javax.swing.JMenuItem();
        configuracion = new javax.swing.JMenuItem();
        menuLookAndFeel = menuAspectos()
        ;
        menuIdioma = new javax.swing.JMenu();
        Espanol = new javax.swing.JRadioButtonMenuItem();
        English = new javax.swing.JRadioButtonMenuItem();
        menuAyuda = new javax.swing.JMenu();
        mostrarAyuda = new javax.swing.JMenuItem();
        acercaDeMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("propiedades/Idioma"); // NOI18N
        setTitle(bundle.getString("VENTANAINICIO.TITULO")); // NOI18N
        setBackground(new java.awt.Color(159, 180, 188));

        etqCarpetaArchivos.setFont(Config.FUENTE_NORMAL);
        etqCarpetaArchivos.setText(bundle.getString("VENTANAINICIO.etqCarpetaArchivos.TEXT")); // NOI18N
        etqCarpetaArchivos.setName("etqCarpetaArchivos"); // NOI18N

        etqInfoDobleClick.setFont(Config.FUENTE_NORMAL.deriveFont(Font.BOLD)
        );
        etqInfoDobleClick.setText(bundle.getString("VentanaInicio.etqInfoDobleClick.text")); // NOI18N
        etqInfoDobleClick.setName("etqInfoDobleClick"); // NOI18N

        jScrollPane1.setMaximumSize(new java.awt.Dimension(2400, 300));
        jScrollPane1.setName("jScrollPane1"); // NOI18N
        jScrollPane1.setPreferredSize(new java.awt.Dimension(452, 302));

        tablaTests.setFont(Config.FUENTE_NORMAL);
        tablaTests.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tablaTests.setName("tablaTests"); // NOI18N
        jScrollPane1.setViewportView(tablaTests);

        panelBotones.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        panelBotones.setName("panelBotones"); // NOI18N

        btnAgregarTests.setFont(Config.FUENTE_TITULO);
        btnAgregarTests.setText(bundle.getString("VENTANAINICIO.btnAgregarTest.TEXT")); // NOI18N
        btnAgregarTests.setActionCommand(bundle.getString("VentanaInicio.btnAgregarTests.actionCommand")); // NOI18N
        btnAgregarTests.setName("btnAgregarTests"); // NOI18N
        btnAgregarTests.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarTestsActionPerformed(evt);
            }
        });

        btnEvaluaciones.setFont(Config.FUENTE_TITULO);
        btnEvaluaciones.setText(bundle.getString("VentanaInicio.btnEvaluaciones.text")); // NOI18N
        btnEvaluaciones.setActionCommand(bundle.getString("VentanaInicio.btnEvaluaciones.actionCommand")); // NOI18N
        btnEvaluaciones.setName("btnEvaluaciones"); // NOI18N
        btnEvaluaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEvaluacionesActionPerformed(evt);
            }
        });

        btnBorrarTest.setFont(Config.FUENTE_TITULO);
        btnBorrarTest.setText(bundle.getString("VentanaInicio.btnBorrarTest.text")); // NOI18N
        btnBorrarTest.setActionCommand(bundle.getString("VentanaInicio.btnBorrarTest.text")); // NOI18N
        btnBorrarTest.setName("btnBorrarTest"); // NOI18N
        btnBorrarTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarTestActionPerformed(evt);
            }
        });

        btnMostrarModelo.setFont(Config.FUENTE_TITULO);
        btnMostrarModelo.setText(bundle.getString("VentanaInicio.btnMostrarModelo.text")); // NOI18N
        btnMostrarModelo.setName("btnMostrarModelo"); // NOI18N
        btnMostrarModelo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarModeloActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBotonesLayout = new javax.swing.GroupLayout(panelBotones);
        panelBotones.setLayout(panelBotonesLayout);
        panelBotonesLayout.setHorizontalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAgregarTests)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEvaluaciones)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBorrarTest)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(btnMostrarModelo)
                .addContainerGap())
        );
        panelBotonesLayout.setVerticalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregarTests)
                    .addComponent(btnMostrarModelo)
                    .addComponent(btnEvaluaciones)
                    .addComponent(btnBorrarTest))
                .addContainerGap())
        );

        menuPrincipalBarra.setFont(Config.FUENTE_NORMAL);
        menuPrincipalBarra.setMaximumSize(new java.awt.Dimension(225, 500));
        menuPrincipalBarra.setName("menuPrincipalBarra"); // NOI18N

        menuArchivo.setMnemonic('a');
        menuArchivo.setText(bundle.getString("VENTANAINICIO.MENU_ARCHIVO.TEXT")); // NOI18N
        menuArchivo.setFont(Config.FUENTE_NORMAL);
        menuArchivo.setName("menuArchivo"); // NOI18N

        exitMenuItem.setFont(Config.FUENTE_NORMAL);
        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText(bundle.getString("VENTANAINICIO.MENU_ARCHIVO.SALIR.TEXT")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        menuArchivo.add(exitMenuItem);

        menuPrincipalBarra.add(menuArchivo);

        menuOpciones.setMnemonic('o');
        menuOpciones.setText(bundle.getString("VENTANAINICIO.MENU_OPCIONES.TEXT")); // NOI18N
        menuOpciones.setFont(Config.FUENTE_NORMAL);
        menuOpciones.setName("menuOpciones"); // NOI18N

        probarTest.setFont(Config.FUENTE_NORMAL);
        probarTest.setMnemonic('p');
        probarTest.setText(bundle.getString("VENTANAINICIO.MENU_OPCIONES.PROBAR_TEST.TEXT")); // NOI18N
        probarTest.setName("probarTest"); // NOI18N
        probarTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                probarTestActionPerformed(evt);
            }
        });
        menuOpciones.add(probarTest);

        configuracion.setFont(Config.FUENTE_NORMAL);
        configuracion.setMnemonic('c');
        configuracion.setText(bundle.getString("VENTANAINICIO.MENU_OPCIONES.CONFIGURACION.TEXT")); // NOI18N
        configuracion.setName("configuracion"); // NOI18N
        configuracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                configuracionActionPerformed(evt);
            }
        });
        menuOpciones.add(configuracion);

        menuPrincipalBarra.add(menuOpciones);

        menuLookAndFeel.setText(java.util.ResourceBundle.getBundle("propiedades/Idioma").getString("VentanaInicio.menu.temas.text"));
        menuLookAndFeel.setFont(Config.FUENTE_NORMAL);
        menuLookAndFeel.setName("menuLookAndFeel"); // NOI18N
        menuLookAndFeel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuLookAndFeelActionPerformed(evt);
            }
        });
        menuPrincipalBarra.add(menuLookAndFeel);

        menuIdioma.setMnemonic('i');
        menuIdioma.setText(bundle.getString("VENTANAINICIO.MENU_IDIOMA.TEXT")); // NOI18N
        menuIdioma.setAlignmentX(3.0F);
        menuIdioma.setFont(Config.FUENTE_NORMAL);
        menuIdioma.setName("menuIdioma"); // NOI18N

        selectorIdioma.add(Espanol);
        Espanol.setFont(Config.FUENTE_NORMAL);
        Espanol.setSelected(true);
        Espanol.setText(bundle.getString("VENTANAINICIO.MENU_IDIOMA.ESPANOL.TEXT")); // NOI18N
        Espanol.setName("Espanol"); // NOI18N
        Espanol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EspanolActionPerformed(evt);
            }
        });
        menuIdioma.add(Espanol);

        selectorIdioma.add(English);
        English.setFont(Config.FUENTE_NORMAL);
        English.setText(bundle.getString("VENTANAINICIO.MENU_IDIOMA.INGLES.TEXT")); // NOI18N
        English.setName("English"); // NOI18N
        English.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EnglishActionPerformed(evt);
            }
        });
        menuIdioma.add(English);

        menuPrincipalBarra.add(menuIdioma);

        menuAyuda.setText(bundle.getString("VentanaInicio.menuAyuda.text")); // NOI18N
        menuAyuda.setFont(Config.FUENTE_NORMAL);
        menuAyuda.setName("menuAyuda"); // NOI18N

        mostrarAyuda.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        mostrarAyuda.setFont(Config.FUENTE_NORMAL);
        mostrarAyuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/main/Azul_ayuda_chic.png"))); // NOI18N
        mostrarAyuda.setText(bundle.getString("VentanaInicio.mostrarAyuda.text")); // NOI18N
        mostrarAyuda.setName("mostrarAyuda"); // NOI18N
        mostrarAyuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarAyudaActionPerformed(evt);
            }
        });
        menuAyuda.add(mostrarAyuda);

        acercaDeMenuItem.setFont(Config.FUENTE_NORMAL);
        acercaDeMenuItem.setMnemonic('a');
        acercaDeMenuItem.setText(bundle.getString("VentanaInicio.acercaDeMenuItem.text")); // NOI18N
        acercaDeMenuItem.setName("acercaDeMenuItem"); // NOI18N
        acercaDeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acercaDeMenuItemActionPerformed(evt);
            }
        });
        menuAyuda.add(acercaDeMenuItem);

        menuPrincipalBarra.add(menuAyuda);

        setJMenuBar(menuPrincipalBarra);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(etqCarpetaArchivos)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(etqInfoDobleClick))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1135, Short.MAX_VALUE)
                    .addComponent(panelBotones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(etqCarpetaArchivos)
                    .addComponent(etqInfoDobleClick))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void menuLookAndFeelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuLookAndFeelActionPerformed
        // Este menú usa codigo personalizado, para añadir los items de menú en el editor se puede cambiar.
    }//GEN-LAST:event_menuLookAndFeelActionPerformed

    private void btnAgregarTestsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarTestsActionPerformed
        // Leo nuevos test y los añado a la tabla
        leerTests();
        etqCarpetaArchivos.setText(idioma.getString("VENTANAINICIO.etqCarpetaArchivos.TEXT") + Config.getCarpetaArchivosTests()); // NOI18N
    }//GEN-LAST:event_btnAgregarTestsActionPerformed

    private void leerTests() {
        // Selecciono la carpeta con los test a evaluar

        DialogoCarpertaFichero SelectorCarpeta = new DialogoCarpertaFichero(); // true indica que quiero seleccionar carpetas
        SelectorCarpeta.setCurrentDirectory(new File(Config.getCarpetaArchivosTests()));
        SelectorCarpeta.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Para que elija carpeta en lugar de ficheros individuales
        SelectorCarpeta.setDialogTitle(idioma.getString("FileChooser.Carpeta.title"));
        // Paso el padre para que coja el iconoAplic
        if (SelectorCarpeta.showDialog(this, idioma.getString("FileChooser.Carpeta.title")) == DialogoCarpertaFichero.APPROVE_OPTION) {
            Config.setCarpetaArchivosTests(SelectorCarpeta.getSelectedFile().getPath());
            File carpeta = new File(SelectorCarpeta.getSelectedFile().getPath());
            FilenameFilter imgFiltro = (dir, name) -> (name.toLowerCase().endsWith(".jpg") | name.toLowerCase().endsWith(".jpeg") | name.toLowerCase().endsWith(".png"));
            File[] fiches = carpeta.listFiles(imgFiltro);
            //
            // Utilizo Swingworker para que se haga la lectura en background, una vez terminada se avisa con un JOptionPane
            // Creo el trabajo y lo ejecuto.
            LeerTestEnBackground trabajo = new LeerTestEnBackground(this, fiches);
            trabajo.execute();
        }
    }

    // Utilizo Swingworker para que se haga la lectura en background, una vez terminada se avisa con un JOptionPane
    private class LeerTestEnBackground extends SwingWorker<Boolean, Object> {

        File[] fiches;
        VentanaInicio padre;

        LeerTestEnBackground(VentanaInicio papa, File[] fichs) {
            fiches = fichs;
            padre = papa;
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            for (File fichero : fiches) {
                // Cargo un nuevo Test Principal.TestActual
                // Si hay un error en el proceso, lo muestro y pregunto si quiere seguir
                if (!Procesador.leerTest(fichero)) {
                    int loqueDice = JOptionPane.showOptionDialog(padre, idioma.getString("Procesador.Error.Esquinas") + "\n"
                            + fichero.getPath(), idioma.getString("Error.text"),
                            JOptionPane.NO_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[]{idioma.getString("Procesador.btnCancelarLectura.text"),
                                idioma.getString("Procesador.btnContinuarLectura.text")}, idioma.getString("Procesador.btnContinuarLectura.text"));
                    // El primer botón, el 0 es cancelar lectura
                    if (loqueDice == 0) {
                        // Ya no leo más, no quiere seguir
                        log.info("Canceló la lectura de tests");
                        break;
                    }
                    Procesador.getTestActual().setNombreArchivo("ERROR en fichero :" + Procesador.getTestActual().getNombreArchivo());
                }
            }
            return false;
        }

        @Override
        protected void done() {
            // Cuando acabo aviso.
            JOptionPane.showOptionDialog(padre, idioma.getString("Procesador.LecturaTerminada.text"), idioma.getString("Atencion.text"),
                    JOptionPane.NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, Config.OPCION_ACEPTAR, null);
        }
    }

    private void PonIdioma(String idioma, String pais, Object objetoOrigen) {
        JRadioButtonMenuItem objetoMenu = (JRadioButtonMenuItem) objetoOrigen;
        Locale.setDefault(new Locale(idioma, pais));
        ResourceBundle.clearCache();
        cambiarIdioma();
        Config.guardarConfiguracion();
        objetoMenu.setSelected(true);
    }

    private void EspanolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EspanolActionPerformed
        // Cambio a Español
        PonIdioma("es", "ES", evt.getSource());
    }//GEN-LAST:event_EspanolActionPerformed

    private void EnglishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EnglishActionPerformed
        // Cambio a Inglés
        PonIdioma("en", "US", evt.getSource());
    }//GEN-LAST:event_EnglishActionPerformed

    private void probarTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_probarTestActionPerformed
        // Abro la ventana de análisis de TEST
        DialogoPruebasTest ventaPruebasTest = new DialogoPruebasTest(this, true);
        ventaPruebasTest.setVisible(true);
    }//GEN-LAST:event_probarTestActionPerformed

    private void configuracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_configuracionActionPerformed
        // Muestro el diálogo de la ventana de configuración. Le paso el padre y el modo Modal
        DialogoConfiguracion ventaConfig = new DialogoConfiguracion(esteFrame, true);
        // Actualizo el tamaño del control.
        ventaConfig.pack();
        ventaConfig.setVisible(true);
        // Vuelvo a cargar la ventaConfig, por si hubo cambios
        Config.cargarConfiguracion();
    }//GEN-LAST:event_configuracionActionPerformed

    private void btnMostrarModeloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarModeloActionPerformed
        // Ventana que muestra el modelo en HTML
        DialogoModelo ventaModel = new DialogoModelo(this, true);
        ventaModel.setVisible(true);
    }//GEN-LAST:event_btnMostrarModeloActionPerformed

    private void btnEvaluacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEvaluacionesActionPerformed
        // Abro el formulario de evaluaciones, le paso el padre, modal=true, y el modelo de la tabla leído

        DialogoEvaluar ventaEval = new DialogoEvaluar(this, true);
        // Si he leído exámenes, los agrego al modelo de test Corregidos
        if (tablaTests.getRowCount() > 0) {
            Procesador.AgregarTestLeidos();
        }
        ventaEval.setVisible(true);
    }//GEN-LAST:event_btnEvaluacionesActionPerformed

    private void btnBorrarTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarTestActionPerformed
        int queDice;
        // Borro todos los test de la tabla. Uso un DialogoAceptarCancelar que tiene en cuenta la escala en lugar de JOptionpane
        // Pregunto si quiere borrar todos o sólo los seleccionados
        queDice = JOptionPane.showOptionDialog(rootPane, idioma.getString("VentanaInicio.PerguntaQueBorrar.text"), idioma.getString("Atencion.text"),
                JOptionPane.NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{idioma.getString("SoloSeleccionadas.text"), idioma.getString("Todas.text")}, idioma.getString("SoloSeleccionadas.text"));
        if (queDice == 0) {
            int paBorrar = tablaTests.getSelectedRow();
            while (paBorrar > 0) {
                Procesador.BorraFilaModeloTestLeidos(paBorrar);
                paBorrar = tablaTests.getSelectedRow();
            }
        } else {
            String[] botones = {idioma.getString("Aceptar.text"), idioma.getString("Cancelar.text")};
            queDice = JOptionPane.showOptionDialog(rootPane, idioma.getString("VentanaInicio.aviso.borrar.lista.tests.text"), idioma.getString("Atencion.text"),
                    JOptionPane.NO_OPTION, JOptionPane.WARNING_MESSAGE, null, botones, idioma.getString("Cancelar.text"));
            // El primer botón, el 0 es aceptar
            if (queDice == JOptionPane.OK_OPTION) {
                // Borro los test que hay en la tabla
                log.info("Borró los tests de la tabla");
                Procesador.modeloTablaTestsLeidos.setRowCount(0);
            }
        }


    }//GEN-LAST:event_btnBorrarTestActionPerformed

    private void mostrarAyudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mostrarAyudaActionPerformed
        // Muestro la ayuda
        String error = Procesador.mostrarAyuda(Config.getRutaAyudaInicio());
        if (!"".equals(error)) {
            log.error(error);
            JOptionPane.showOptionDialog(rootPane, error, idioma.getString("Error.text"),
                    JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{idioma.getString("Aceptar.text")}, idioma.getString("Aceptar.text"));
        }
    }//GEN-LAST:event_mostrarAyudaActionPerformed

    private void acercaDeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acercaDeMenuItemActionPerformed
        // Muestra información Acerca de
        AcercaDe ventaAceraDe = new AcercaDe(this, true);
        ventaAceraDe.setVisible(true);
    }//GEN-LAST:event_acercaDeMenuItemActionPerformed

    private void cambiarIdioma() {
        // Recargo el idioma
        Procesador.ReCargarIdioma();
        idioma = Procesador.idioma;

        // Cambio el idioma en la ayuda
        this.setTitle(idioma.getString("VENTANAINICIO.TITULO"));
        etqCarpetaArchivos.setText(idioma.getString("VENTANAINICIO.etqCarpetaArchivos.TEXT")); // NOI18N
        etqInfoDobleClick.setText(idioma.getString("VentanaInicio.etqInfoDobleClick.text")); // NOI18N
        menuArchivo.setText(idioma.getString("VENTANAINICIO.MENU_ARCHIVO.TEXT")); // NOI18N
        exitMenuItem.setText(idioma.getString("VENTANAINICIO.MENU_ARCHIVO.SALIR.TEXT")); // NOI18N
        menuOpciones.setText(idioma.getString("VENTANAINICIO.OPCIONES.TEXT")); // NOI18N
        probarTest.setText(idioma.getString("VENTANAINICIO.MENU_OPCIONES.PROBAR_TEST.TEXT")); // NOI18N
        configuracion.setText(idioma.getString("VENTANAINICIO.MENU_OPCIONES.CONFIGURACION.TEXT")); // NOI18N
        menuLookAndFeel.setText("Look and Feel");
        menuIdioma.setText(idioma.getString("VENTANAINICIO.MENU_IDIOMA.TEXT")); // NOI18N
        menuAyuda.setText(idioma.getString("VentanaInicio.menuAyuda.text")); // NOI18N
        mostrarAyuda.setText(idioma.getString("VentanaInicio.mostrarAyuda.text")); // NOI18N
        acercaDeMenuItem.setText(idioma.getString("VentanaInicio.acercaDeMenuItem.text")); // NOI18N
        Espanol.setText(idioma.getString("VENTANAINICIO.MENU_IDIOMA.ESPANOL.TEXT")); // NOI18N
        English.setText(idioma.getString("VENTANAINICIO.MENU_IDIOMA.INGLES.TEXT")); // NOI18N
        btnAgregarTests.setText(idioma.getString("VENTANAINICIO.btnAgregarTest.TEXT")); // NOI18N
        btnBorrarTest.setText(idioma.getString("VentanaInicio.btnBorrarTest.text"));
        btnEvaluaciones.setText(idioma.getString("VentanaInicio.btnEvaluaciones.text"));
        btnMostrarModelo.setText(idioma.getString("VentanaInicio.btnMostrarModelo.text"));
        // Vuelvo a poner las cabeceras de todas las tabla, para que actualice el idioma
        // Inicializo lo modelo de la tablas de la aplicación
        Procesador.InicializarModelosAplicacion();
        formateaColumnas(tablaTests);
    }

    /**
     *
     * @return Menú con el nuevo aspecto, tema.
     */
    protected JMenu menuAspectos() {
        // Menú look and feel
        JMenu mimenu = new JMenu("Look and Feel");

        List<JMenuItem> LosLookAndfFeels = new ArrayList<>();
        List<String> laf = new ArrayList<>();
        int i = 0;
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            laf.add(info.getClassName());
            LosLookAndfFeels.add(new JMenuItem(info.getClassName().substring(info.getClassName().lastIndexOf(".") + 1, info.getClassName().length())));
            LosLookAndfFeels.get(i).addActionListener((ActionEvent event) -> {
                try {
                    String laClass = event.getActionCommand();
                    for (String unStr : laf) {
                        if (unStr.contains(laClass)) {
                            laClass = unStr;
                            break;
                        }
                    }
                    javax.swing.UIManager.setLookAndFeel(laClass);
                    // Actualizo los estilos de todas las ventanas declaradas en Principal
                    actualizarLookAndFeel();
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(DialogoPruebasTest.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            });
            mimenu.add(LosLookAndfFeels.get(i));
            //System.out.println(laf.get(laf.size() - 1));
            log.info(laf.get(laf.size() - 1));
            i++;
        }
        return mimenu;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        log.info("Pantalla - Alto : " + Procesador.getAltoPantalla() + " - Ancho " + Procesador.getAnchoPantalla());

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            // Cambio algunos colores del tema base Nimbus
            // Fondo base
            UIManager.put("nimbusBase", new Color(0x598944));
            // Fondo del control o ventana
            UIManager.put("control", new Color(0xd9cbbd));
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                log.info("Tema disponible - " + info.getName());
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        // Inicializo la aplicacion
        if (!Procesador.InicializarAplicacion()) {
            log.error(idioma.getString("ErrorInicioApliacion.text"));
            System.exit(0);
        }

        // Ruta de pruebas quitar en definitivo
        //Config.setRutaUltimaImagen("C:\\Users\\Jesus.delBuey\\Documents\\Programacion\\Java\\CorrectA_General Archivos de trabajo");
        //Config.setCarpetaArchivosTests("C:\\Users\\Jesus.delBuey\\Documents\\Programacion\\Java\\CorrectA_General Archivos de trabajo");

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            VentanaInicio.setDefaultLookAndFeelDecorated(true);
            JFrame.setDefaultLookAndFeelDecorated(true);
            VentanaInicio Principal = new VentanaInicio();
            Principal.setIconImage(Config.getIconoAplic().getImage());

            Principal.setVisible(true);
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButtonMenuItem English;
    private javax.swing.JRadioButtonMenuItem Espanol;
    private javax.swing.JMenuItem acercaDeMenuItem;
    private javax.swing.JButton btnAgregarTests;
    private javax.swing.JButton btnBorrarTest;
    private javax.swing.JButton btnEvaluaciones;
    private javax.swing.JButton btnMostrarModelo;
    private javax.swing.JMenuItem configuracion;
    private javax.swing.JLabel etqCarpetaArchivos;
    private javax.swing.JLabel etqInfoDobleClick;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenu menuArchivo;
    private javax.swing.JMenu menuAyuda;
    private javax.swing.JMenu menuIdioma;
    private javax.swing.JMenu menuLookAndFeel;
    private javax.swing.JMenu menuOpciones;
    private javax.swing.JMenuBar menuPrincipalBarra;
    private javax.swing.JMenuItem mostrarAyuda;
    private javax.swing.JPanel panelBotones;
    private javax.swing.JMenuItem probarTest;
    private javax.swing.ButtonGroup selectorIdioma;
    private javax.swing.JTable tablaTests;
    // End of variables declaration//GEN-END:variables
}
