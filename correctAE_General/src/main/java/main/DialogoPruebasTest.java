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
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Jesus.delBuey
 */
public class DialogoPruebasTest extends javax.swing.JDialog {

    // Para escucher los eventos de teclado y raton
    AWTEventListener flechasListener;
    AWTEventListener dragListener;
    JScrollBar barraVertical;

    ResourceBundle idioma = Procesador.idioma;
    protected static Loguero log = Procesador.getLog();

    Casilla[] esquinasImagen = new Casilla[4];
    ArrayList<JCheckBox> lasCajas = new ArrayList<>();
    ArrayList<JCheckBox> cajasMarcadas = new ArrayList<>();

    // Dice si la imagen ya está corregida, para eviatar errores y repetiociones
    private boolean imagenCorredida = false;

    /**
     * Get the value of imagenCorredida
     *
     * @return the value of imagenCorredida
     */
    public boolean isImagenCorregida() {
        return imagenCorredida;
    }
    // Escuchador de eventos globales

    /**
     * Creates new form VentanaPruebasTest2
     *
     * @param parent Padre del diálogo
     * @param modal Si es modal o no
     */
    public DialogoPruebasTest(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        InicializarFormulario();
    }

    private void InicializarFormulario() {
        // Coloco el formulario en el centro de la pantalla tamaño vertical máximo
        // que permita la pantalla
        this.setSize(new Dimension(this.getWidth(), Procesador.getAltoPantalla() - 50));
        Procesador.Centrame(this);
        this.setLocation(this.getLocation().x, 5);
        // Para hacer scroll con pagina arriba y abajo
        this.barraVertical = panelScroll.getVerticalScrollBar();
        //
        this.flechasListener = (AWTEvent event) -> {
            // Como se usa la máscara AWTEvent.KEY_EVENT_MASK nunca va a fallar
            KeyEvent keyEvent = (KeyEvent) event;
            // Al soltar la tecla, evaluo cual es
            if (keyEvent.getID() == KeyEvent.KEY_RELEASED) {
                // Si pulsa la tecla m activo el selector
                if (keyEvent.getKeyCode() == KeyEvent.VK_M) {
                    chkSeleccionador.setSelected(!chkSeleccionador.isSelected());
                } else {
                    // Si está activa el check, al pulsar las flechas muevo las casillas
                    switch (keyEvent.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            moverCasillas(0, -1);
                            break;
                        case KeyEvent.VK_DOWN:
                            moverCasillas(0, 1);
                            break;
                        case KeyEvent.VK_LEFT:
                            moverCasillas(-1, 0);
                            break;
                        case KeyEvent.VK_RIGHT:
                            moverCasillas(1, 0);
                            break;
                        case KeyEvent.VK_Q:
                            // Deselecciono todas casillas marcadas
                            deseleccionarCasillas();
                            break;
                        case KeyEvent.VK_PAGE_DOWN:
                            panelScroll.getVerticalScrollBar().setValue(barraVertical.getMaximum());
                            break;
                        case KeyEvent.VK_PAGE_UP:
                            panelScroll.getVerticalScrollBar().setValue(barraVertical.getMinimum());
                            break;
                    }
                }
            }
        };
        // Escucho los eventos globales. Con la máscara de sólo eventos de teclado
        Toolkit.getDefaultToolkit().addAWTEventListener(flechasListener, AWTEvent.KEY_EVENT_MASK);
        //
        // Desplazo las cajas, si las arrastra con el ratón

        this.dragListener = (AWTEvent event) -> {
            JCheckBox unaCaja = new JCheckBox();

            if (event.getSource().getClass().equals(unaCaja.getClass())) {
                unaCaja = (JCheckBox) event.getSource();
                MouseEvent ratEvent = (MouseEvent) event;
                if (unaCaja.isSelected() && ratEvent.getID() == MouseEvent.MOUSE_DRAGGED) {
                    moverCasillas(ratEvent.getX(), ratEvent.getY());
                }
            }
        };
        // Escucho los eventos globales. Con la máscara de sólo eventos de movimiento de raton
        Toolkit.getDefaultToolkit().addAWTEventListener(dragListener, AWTEvent.MOUSE_MOTION_EVENT_MASK);

        // Configuro estado de menús y paneles
        menuEditandoPlantila.setSelected(false);
        menuGuardarPlantilla.setEnabled(false);
        panelEdicionPlantilla.setVisible(false);

        // Asocio un evento de teclado F1 para lanzar la ayuda
        Procesador.asociaAyudaF1(btnAyuda, Config.getRutaAyudaPruebas());
    }

    private void moverCasillas(int moverX, int moverY) {
        // muevo todas las cajas que estén marcadas

        for (JCheckBox unaCaja : cajasMarcadas) {
            unaCaja.setLocation(unaCaja.getX() + moverX, unaCaja.getY() + moverY);
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

        panelImagen = new javax.swing.JPanel();
        panelScroll = new javax.swing.JScrollPane();
        PanelPrincipal = new javax.swing.JPanel();
        PortaCapas = new javax.swing.JLayeredPane();
        etqLaImagen = new javax.swing.JLabel();
        PanelControl = new javax.swing.JPanel();
        panelBotones = new javax.swing.JPanel();
        btnAnalizar = new javax.swing.JButton();
        btnCorrigeImagen = new javax.swing.JButton();
        btnGuardaResultados = new javax.swing.JButton();
        btnRecargarImagen = new javax.swing.JButton();
        btnProbarAlineacion = new javax.swing.JButton();
        btnRecortaImagen = new javax.swing.JButton();
        panelEdicionPlantilla = new javax.swing.JPanel();
        chkSeleccionador = new javax.swing.JCheckBox();
        chkSeleccionarColumna = new javax.swing.JCheckBox();
        chkSeleccionarFila = new javax.swing.JCheckBox();
        etqMoverCasillas = new javax.swing.JLabel();
        btnQuitarSeleccion = new javax.swing.JButton();
        panelDatos = new javax.swing.JPanel();
        etqTamano = new javax.swing.JLabel();
        etqBrillo = new javax.swing.JLabel();
        etqCoordenadas = new javax.swing.JLabel();
        etqUltimoClick = new javax.swing.JLabel();
        etqNombreArchivo = new javax.swing.JLabel();
        etqBrilloMedio = new javax.swing.JLabel();
        btnAyuda = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        menuAbrirArchivo = new javax.swing.JMenuItem();
        opciones = new javax.swing.JMenu();
        configuracion = new javax.swing.JMenuItem();
        pruebas = new javax.swing.JMenu();
        buscarEsquinas = new javax.swing.JMenuItem();
        pintarCasillas = new javax.swing.JMenuItem();
        modificarPlantilla = new javax.swing.JMenu();
        menuEditandoPlantila = new javax.swing.JCheckBoxMenuItem();
        menuGuardarPlantilla = new javax.swing.JMenuItem();
        menuRestaurarPlantilla = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("propiedades/Idioma"); // NOI18N
        setTitle(bundle.getString("VENTANAPRUEBASTEST.TITULO")); // NOI18N
        setSize(new java.awt.Dimension(0, 0));
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.PAGE_AXIS));

        panelImagen.setMinimumSize(new java.awt.Dimension(0, 200));
        panelImagen.setPreferredSize(new java.awt.Dimension(1032, 800));

        panelScroll.setPreferredSize(new java.awt.Dimension(1020, 12));

        PortaCapas.setPreferredSize(new java.awt.Dimension(1020, 612));

        etqLaImagen.setAlignmentY(0.0F);
        etqLaImagen.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                etqLaImagenMouseMoved(evt);
            }
        });
        etqLaImagen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                etqLaImagenMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                etqLaImagenMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                etqLaImagenMouseExited(evt);
            }
        });

        PortaCapas.setLayer(etqLaImagen, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout PortaCapasLayout = new javax.swing.GroupLayout(PortaCapas);
        PortaCapas.setLayout(PortaCapasLayout);
        PortaCapasLayout.setHorizontalGroup(
            PortaCapasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PortaCapasLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(etqLaImagen, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
                .addContainerGap(533, Short.MAX_VALUE))
        );
        PortaCapasLayout.setVerticalGroup(
            PortaCapasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PortaCapasLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(etqLaImagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PanelPrincipalLayout = new javax.swing.GroupLayout(PanelPrincipal);
        PanelPrincipal.setLayout(PanelPrincipalLayout);
        PanelPrincipalLayout.setHorizontalGroup(
            PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1074, Short.MAX_VALUE)
            .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(PanelPrincipalLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(PortaCapas, javax.swing.GroupLayout.DEFAULT_SIZE, 1062, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        PanelPrincipalLayout.setVerticalGroup(
            PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(PanelPrincipalLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(PortaCapas, javax.swing.GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        panelScroll.setViewportView(PanelPrincipal);

        javax.swing.GroupLayout panelImagenLayout = new javax.swing.GroupLayout(panelImagen);
        panelImagen.setLayout(panelImagenLayout);
        panelImagenLayout.setHorizontalGroup(
            panelImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelImagenLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelScroll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelImagenLayout.setVerticalGroup(
            panelImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelImagenLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelScroll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(panelImagen);

        panelBotones.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnAnalizar.setFont(Config.FUENTE_NORMAL);
        btnAnalizar.setText(bundle.getString("DialogoPruebasTest.btnAnalizar.text")); // NOI18N
        btnAnalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalizarActionPerformed(evt);
            }
        });

        btnCorrigeImagen.setFont(Config.FUENTE_NORMAL);
        btnCorrigeImagen.setText(bundle.getString("DialogoPruebasTest.btnCorrigeImagen.text")); // NOI18N
        btnCorrigeImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCorrigeImagenActionPerformed(evt);
            }
        });

        btnGuardaResultados.setFont(Config.FUENTE_NORMAL);
        btnGuardaResultados.setText(bundle.getString("DialogoPruebasTest.btnGuardaResultados.text")); // NOI18N
        btnGuardaResultados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardaResultadosActionPerformed(evt);
            }
        });

        btnRecargarImagen.setFont(Config.FUENTE_NORMAL);
        btnRecargarImagen.setText(bundle.getString("DialogoPruebasTest.btnRecargarImagen.text")); // NOI18N
        btnRecargarImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecargarImagenActionPerformed(evt);
            }
        });

        btnProbarAlineacion.setFont(Config.FUENTE_NORMAL);
        btnProbarAlineacion.setText(bundle.getString("DialogoPruebasTest.btnProbarAlineacion.text")); // NOI18N
        btnProbarAlineacion.setActionCommand(bundle.getString("DialogoPruebasTest.btnProbarAlineacion.actionCommand")); // NOI18N
        btnProbarAlineacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProbarAlineacionActionPerformed(evt);
            }
        });

        btnRecortaImagen.setFont(Config.FUENTE_NORMAL);
        btnRecortaImagen.setText(bundle.getString("DialogoPruebasTest.btnRecortaImagen.text")); // NOI18N
        btnRecortaImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecortaImagenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBotonesLayout = new javax.swing.GroupLayout(panelBotones);
        panelBotones.setLayout(panelBotonesLayout);
        panelBotonesLayout.setHorizontalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBotonesLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnGuardaResultados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelBotonesLayout.createSequentialGroup()
                        .addComponent(btnProbarAlineacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRecargarImagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBotonesLayout.createSequentialGroup()
                        .addComponent(btnAnalizar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCorrigeImagen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRecortaImagen)))
                .addGap(10, 10, 10))
        );
        panelBotonesLayout.setVerticalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBotonesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAnalizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCorrigeImagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRecortaImagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRecargarImagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnProbarAlineacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGuardaResultados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelEdicionPlantilla.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        chkSeleccionador.setFont(Config.FUENTE_NORMAL);
        chkSeleccionador.setText(bundle.getString("DialogoPruebasTest.chkSeleccionador.text")); // NOI18N

        chkSeleccionarColumna.setFont(Config.FUENTE_NORMAL);
        chkSeleccionarColumna.setText(bundle.getString("DialogoPruebasTest.chkSeleccionarColumna.text")); // NOI18N
        chkSeleccionarColumna.setToolTipText(bundle.getString("DialogoPruebasTest.chkSeleccionarColumna.toolTipText")); // NOI18N
        chkSeleccionarColumna.setActionCommand(bundle.getString("DialogoPruebasTest.chkSeleccionarColumna.text")); // NOI18N

        chkSeleccionarFila.setFont(Config.FUENTE_NORMAL);
        chkSeleccionarFila.setText(bundle.getString("DialogoPruebasTest.chkSeleccionarFila.text")); // NOI18N
        chkSeleccionarFila.setToolTipText(bundle.getString("DialogoPruebasTest.chkSeleccionarFila.toolTipText")); // NOI18N

        etqMoverCasillas.setText(bundle.getString("DialogoPruebasTest.etqMoverCasillas.text")); // NOI18N

        btnQuitarSeleccion.setFont(Config.FUENTE_NORMAL);
        btnQuitarSeleccion.setText(bundle.getString("DialogoPruebasTest.btnQuitarSeleccion.text")); // NOI18N
        btnQuitarSeleccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarSeleccionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelEdicionPlantillaLayout = new javax.swing.GroupLayout(panelEdicionPlantilla);
        panelEdicionPlantilla.setLayout(panelEdicionPlantillaLayout);
        panelEdicionPlantillaLayout.setHorizontalGroup(
            panelEdicionPlantillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEdicionPlantillaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelEdicionPlantillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkSeleccionarFila)
                    .addGroup(panelEdicionPlantillaLayout.createSequentialGroup()
                        .addGroup(panelEdicionPlantillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkSeleccionador)
                            .addComponent(etqMoverCasillas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkSeleccionarColumna))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(btnQuitarSeleccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelEdicionPlantillaLayout.setVerticalGroup(
            panelEdicionPlantillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEdicionPlantillaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelEdicionPlantillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEdicionPlantillaLayout.createSequentialGroup()
                        .addComponent(chkSeleccionador)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkSeleccionarColumna))
                    .addComponent(btnQuitarSeleccion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkSeleccionarFila)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(etqMoverCasillas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelDatos.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        etqTamano.setFont(Config.FUENTE_NORMAL);
        etqTamano.setText(bundle.getString("DialogoPruebasTest.etqTamano.text")); // NOI18N

        etqBrillo.setFont(Config.FUENTE_NORMAL);
        etqBrillo.setText(bundle.getString("DialogoPruebasTest.etqBrillo.text")); // NOI18N

        etqCoordenadas.setFont(Config.FUENTE_NORMAL);
        etqCoordenadas.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        etqCoordenadas.setText(bundle.getString("DialogoPruebasTest.etqCoordenadas.text")); // NOI18N
        etqCoordenadas.setMaximumSize(new java.awt.Dimension(200, 16));
        etqCoordenadas.setMinimumSize(new java.awt.Dimension(200, 16));

        etqUltimoClick.setFont(Config.FUENTE_NORMAL);
        etqUltimoClick.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        etqUltimoClick.setText(bundle.getString("DialogoPruebasTest.etqUltimoClick.text")); // NOI18N
        etqUltimoClick.setMaximumSize(new java.awt.Dimension(200, 16));
        etqUltimoClick.setMinimumSize(new java.awt.Dimension(200, 16));

        etqNombreArchivo.setFont(Config.FUENTE_NORMAL);
        etqNombreArchivo.setText(bundle.getString("DialogoPruebasTest.etqNombreArchivo.text")); // NOI18N

        etqBrilloMedio.setFont(Config.FUENTE_NORMAL);
        etqBrilloMedio.setText(bundle.getString("DialogoPruebasTest.etqBrilloMedio.text")); // NOI18N

        javax.swing.GroupLayout panelDatosLayout = new javax.swing.GroupLayout(panelDatos);
        panelDatos.setLayout(panelDatosLayout);
        panelDatosLayout.setHorizontalGroup(
            panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatosLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDatosLayout.createSequentialGroup()
                        .addComponent(etqTamano)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(etqBrillo))
                    .addGroup(panelDatosLayout.createSequentialGroup()
                        .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(etqCoordenadas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(etqNombreArchivo))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelDatosLayout.createSequentialGroup()
                        .addComponent(etqUltimoClick, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(etqBrilloMedio)))
                .addGap(10, 10, 10))
        );
        panelDatosLayout.setVerticalGroup(
            panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(etqTamano)
                    .addComponent(etqBrillo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(etqCoordenadas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(etqUltimoClick, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(etqBrilloMedio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(etqNombreArchivo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnAyuda.setBackground(new java.awt.Color(61, 117, 105));
        btnAyuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/main/Azul_ayuda.png"))); // NOI18N
        btnAyuda.setToolTipText(bundle.getString("btnAyudaToolTip.text")); // NOI18N
        btnAyuda.setBorderPainted(false);
        btnAyuda.setContentAreaFilled(false);
        btnAyuda.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/main/Azul_brillo_ayuda.png"))); // NOI18N
        btnAyuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAyudaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelControlLayout = new javax.swing.GroupLayout(PanelControl);
        PanelControl.setLayout(PanelControlLayout);
        PanelControlLayout.setHorizontalGroup(
            PanelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelControlLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(panelBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelEdicionPlantilla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addComponent(btnAyuda)
                .addGap(18, 61, Short.MAX_VALUE)
                .addComponent(panelDatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );
        PanelControlLayout.setVerticalGroup(
            PanelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelControlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelEdicionPlantilla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelBotones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelDatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(9, Short.MAX_VALUE))
            .addGroup(PanelControlLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAyuda)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(PanelControl);

        fileMenu.setMnemonic('a');
        fileMenu.setText(bundle.getString("DialogoPruebasTest.fileMenu.text")); // NOI18N
        fileMenu.setFont(Config.FUENTE_NORMAL);

        menuAbrirArchivo.setFont(Config.FUENTE_NORMAL);
        menuAbrirArchivo.setMnemonic('o');
        menuAbrirArchivo.setText(bundle.getString("DialogoPruebasTest.menuAbrirArchivo.text")); // NOI18N
        menuAbrirArchivo.setActionCommand(bundle.getString("DialogoPruebasTest.menuAbrirArchivo.actionCommand")); // NOI18N
        menuAbrirArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAbrirArchivoActionPerformed(evt);
            }
        });
        fileMenu.add(menuAbrirArchivo);

        menuBar.add(fileMenu);

        opciones.setMnemonic('O');
        opciones.setText(bundle.getString("DialogoPruebasTest.opciones.text")); // NOI18N
        opciones.setFont(Config.FUENTE_NORMAL);

        configuracion.setMnemonic('C');
        configuracion.setText(bundle.getString("DialogoPruebasTest.configuracion.text")); // NOI18N
        configuracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                configuracionActionPerformed(evt);
            }
        });
        opciones.add(configuracion);

        menuBar.add(opciones);

        pruebas.setMnemonic('p');
        pruebas.setText(bundle.getString("DialogoPruebasTest.pruebas.text")); // NOI18N
        pruebas.setFont(Config.FUENTE_NORMAL);

        buscarEsquinas.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, 0));
        buscarEsquinas.setFont(Config.FUENTE_NORMAL);
        buscarEsquinas.setText(bundle.getString("DialogoPruebasTest.buscarEsquinas.text")); // NOI18N
        buscarEsquinas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarEsquinasActionPerformed(evt);
            }
        });
        pruebas.add(buscarEsquinas);

        pintarCasillas.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, 0));
        pintarCasillas.setFont(Config.FUENTE_NORMAL);
        pintarCasillas.setText(bundle.getString("DialogoPruebasTest.pintarCasillas.text")); // NOI18N
        pintarCasillas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pintarCasillasActionPerformed(evt);
            }
        });
        pruebas.add(pintarCasillas);

        menuBar.add(pruebas);

        modificarPlantilla.setText(bundle.getString("DialogoPruebasTest.modificarPlantilla.text")); // NOI18N
        modificarPlantilla.setFont(Config.FUENTE_NORMAL);

        menuEditandoPlantila.setFont(Config.FUENTE_NORMAL);
        menuEditandoPlantila.setText(bundle.getString("DialogoPruebasTest.menuEditandoPlantila.text")); // NOI18N
        menuEditandoPlantila.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEditandoPlantilaActionPerformed(evt);
            }
        });
        modificarPlantilla.add(menuEditandoPlantila);

        menuGuardarPlantilla.setFont(Config.FUENTE_NORMAL);
        menuGuardarPlantilla.setText(bundle.getString("DialogoPruebasTest.menuGuardarPlantilla.text")); // NOI18N
        menuGuardarPlantilla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuGuardarPlantillaActionPerformed(evt);
            }
        });
        modificarPlantilla.add(menuGuardarPlantilla);

        menuRestaurarPlantilla.setFont(Config.FUENTE_NORMAL);
        menuRestaurarPlantilla.setText(bundle.getString("DialogoPruebasTest.menuRestaurarPlantilla.text")); // NOI18N
        menuRestaurarPlantilla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRestaurarPlantillaActionPerformed(evt);
            }
        });
        modificarPlantilla.add(menuRestaurarPlantilla);

        menuBar.add(modificarPlantilla);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuAbrirArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAbrirArchivoActionPerformed
        // Cargo la imagen
        DialogoCarpertaFichero SelectorFichero = new DialogoCarpertaFichero();
        // Compruevo y ajusto la escala si es necesario
        SelectorFichero.setFileSelectionMode(JFileChooser.FILES_ONLY); // Para que elija carpeta en lugar de ficheros individuales
        SelectorFichero.setDialogTitle(idioma.getString("FileChooser.Fichero.title"));
        SelectorFichero.setCurrentDirectory(new File(Config.getCarpetaArchivosTests()));

        String[] extensions = ImageIO.getReaderFileSuffixes();
        SelectorFichero.setFileFilter(new FileNameExtensionFilter("IMAGE FILES", extensions));
        SelectorFichero.setSelectedFile(new File(Config.getRutaUltimaImagen()));
        int resultado = SelectorFichero.showOpenDialog(this);
        if (resultado == DialogoCarpertaFichero.APPROVE_OPTION) {
            // Actualizo en Config la última ruta
            Config.setCarpetaArchivosTests(SelectorFichero.getSelectedFile().getAbsolutePath());
            cargarImagen(SelectorFichero.getSelectedFile());
        }
    }//GEN-LAST:event_menuAbrirArchivoActionPerformed

    private void buscarEsquinasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarEsquinasActionPerformed
        // Busco las esquinas y las pinto
        if (Procesador.getImagenTest() != null) {
            esquinasImagen = Procesador.buscaEsquinas(Procesador.getImagenTest(), Config.esquinasZona); // Busco las esquinas de la imagen YA enderezada
            Procesador.setImagenTest(Procesador.pintaEsquinas(Procesador.getImagenTest(), esquinasImagen, Config.ANCHO_PUNTO_ESQUINA));
            //laImagen.setIcon(new ImageIcon(Procesador.getImagenTest().getScaledInstance(laImagen.getWidth(), laImagen.getWidth()*Config.MODELO_RELACION_ASPECTO, Image.SCALE_DEFAULT)));
            etqLaImagen.setIcon(new ImageIcon(Procesador.getImagenTest()));
            repaint();
        } else {
            // No hay imagen
            JOptionPane.showOptionDialog(this.getContentPane(), idioma.getString("Procesador.error.noImagen.text"), idioma.getString("Atencion.text"),
                    JOptionPane.NO_OPTION, JOptionPane.WARNING_MESSAGE, null, Config.OPCION_ACEPTAR, null);
        }
    }//GEN-LAST:event_buscarEsquinasActionPerformed

    private void pintarCasillasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pintarCasillasActionPerformed
        // Pinto todas las cajas que hay definidas en las coordenadas del fichero.
        // Las coordenadas son del CENTRO DE la casilla, de ancho VentanaInicio.anchoMarcasRespuesta
        int mitadAncho = Config.getAnchoMarcasRespuesta() / 2;

        if (Procesador.getImagenTest() != null) {
            Graphics g = Procesador.getImagenTest().getGraphics();
            g.setColor(Color.RED);
            if (Procesador.getTestActual() != null) {
                for (Casilla pun : Procesador.getCasillasTest()) {
                    g.drawRect(pun.getCoordX() - mitadAncho, pun.getCoordY() - mitadAncho,
                            Config.getAnchoMarcasRespuesta(), Config.getAnchoMarcasRespuesta());
                }
            }
            g.dispose();
            //laImagen.setIcon(new ImageIcon(Procesador.getImagenTest().getScaledInstance(laImagen.getWidth(), laImagen.getWidth()*Config.MODELO_RELACION_ASPECTO, Image.SCALE_DEFAULT)));
            etqLaImagen.setIcon(new ImageIcon(Procesador.getImagenTest()));
            repaint();
        } else {
            // No hay imagen
            JOptionPane.showOptionDialog(this.getContentPane(), idioma.getString("Procesador.error.noImagen.text"), idioma.getString("Atencion.text"),
                    JOptionPane.NO_OPTION, JOptionPane.WARNING_MESSAGE, null, Config.OPCION_ACEPTAR, null);
        }
    }//GEN-LAST:event_pintarCasillasActionPerformed

    private void menuEditandoPlantilaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEditandoPlantilaActionPerformed
        // Cambio el estado de los botones que no son de editar plantilla,
        if (menuEditandoPlantila.isSelected()) {
            btnRecargarImagenActionPerformed(evt);
            btnCorrigeImagenActionPerformed(evt);
            // Si se ha cargado la imagen correctamente
            if (Procesador.getImagenTest() != null) {
                ponCajas();
                menuGuardarPlantilla.setEnabled(true);
                panelEdicionPlantilla.setVisible(true);
                panelBotones.setVisible(false);
            } else {
                menuEditandoPlantila.setSelected(false);
            }
        } else {
            quitaCajas();
            btnRecargarImagenActionPerformed(evt);
            menuGuardarPlantilla.setEnabled(false);
            panelBotones.setVisible(true);
            panelEdicionPlantilla.setVisible(false);
        }
    }//GEN-LAST:event_menuEditandoPlantilaActionPerformed

    private void menuGuardarPlantillaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuGuardarPlantillaActionPerformed
        // Guardo las coordenadas de las cajas en el fichero csv

        int quedice = JOptionPane.showOptionDialog(rootPane, idioma.getString("VentanaPruebasTest.menuGuardandoPlantila.text"), idioma.getString("VentanaPruebasTest.menuGuardandoPlantila.titulo.text"),
                JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{idioma.getString("Aceptar.text"), idioma.getString("Cancelar.text")}, idioma.getString("Cancelar.text"));
        if (quedice == 0) {
            String[] mensajes = Procesador.guardaCasillas(lasCajas);
            if (!"".equals(mensajes[0])) {
                // Hubo un error u otro mensaje
                JOptionPane.showOptionDialog(this.getContentPane(), idioma.getString(mensajes[0]), idioma.getString(mensajes[1]),
                        JOptionPane.NO_OPTION, Integer.parseInt(mensajes[2]), null, Config.OPCION_ACEPTAR, null);
            }
        }
    }//GEN-LAST:event_menuGuardarPlantillaActionPerformed

    private void etqLaImagenMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etqLaImagenMouseMoved
        // Estado del punto por el que pasa el ratón
        if (Procesador.getImagenTest() != null) {
            try {
                Color queColor = new Color(Procesador.getImagenTest().getRGB(evt.getX(), evt.getY()));
                etqCoordenadas.setText(idioma.getString("DialogoPruebasTest.etqCoordenadas.text") + "(x,y): " + evt.getX() + "," + evt.getY());
                etqBrillo.setText(idioma.getString("DialogoPruebasTest.etqBrillo.text") + queColor.getRed());
            } catch (ArrayIndexOutOfBoundsException ex) {
                etqCoordenadas.setText(idioma.getString("VENTANAPRUEBASTESTS.etqCoordenadas.Cambiado.TEXT"));
            }
        }
    }//GEN-LAST:event_etqLaImagenMouseMoved

    private void etqLaImagenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etqLaImagenMouseClicked
        // Primero calculo el brillo y luego pinto, para no estropear la media
        int unaX, unaY;

        etqUltimoClick.setText(idioma.getString("VentanaPruebasTest.etqUltimoClick.text") + "(x,y): " + evt.getX() + "," + evt.getY());
        if (Procesador.getImagenTest() != null) {
            unaX = evt.getX() - 3;
            unaY = evt.getY() - 2;
            // Agrego una nueva caja
            //PortaCapas.add(nuevaCaja("nombre", unaX, unaY, Config.), JLayeredPane.PALETTE_LAYER); // Pongo las cajas en la capa 10
            BufferedImage resp = Procesador.getImagenTest().getSubimage(unaX, unaY, Config.getAnchoMarcasRespuesta(), Config.getAnchoMarcasRespuesta());
            etqBrilloMedio.setText(idioma.getString("DialogoPruebasTest.etqBrilloMedio.text") + Procesador.brilloRespuesta(resp));
            // Si está marcado el seleccionador de columnas o filas las selecciono todas
            if (chkSeleccionarColumna.isSelected() || chkSeleccionarFila.isSelected()) {
                // Columna entera a partir del click
                int limX1 = evt.getX() - Config.ANCHO_CAJA;
                int limX2 = evt.getX() + Config.ANCHO_CAJA;
                int limY1 = evt.getY() - Config.ANCHO_CAJA;
                int limY2 = evt.getY() + Config.ANCHO_CAJA;
                for (JCheckBox unaCaja : lasCajas) {
                    // Columna entera
                    if (chkSeleccionarColumna.isSelected()) {
                        if (unaCaja.getBounds().y >= evt.getY() && unaCaja.getBounds().x >= limX1 && unaCaja.getBounds().x <= limX2) {
                            unaCaja.setSelected(!unaCaja.isSelected());
                        }
                    }
                    // Fila entera
                    if (chkSeleccionarFila.isSelected()) {
                        if (unaCaja.getBounds().x >= evt.getX() && unaCaja.getBounds().y >= limY1 && unaCaja.getBounds().y <= limY2) {
                            unaCaja.setSelected(!unaCaja.isSelected());
                        }
                    }
                }

            }
        }
    }//GEN-LAST:event_etqLaImagenMouseClicked

    private void etqLaImagenMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etqLaImagenMouseEntered
        // Cambio el cursor
        this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }//GEN-LAST:event_etqLaImagenMouseEntered

    private void etqLaImagenMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etqLaImagenMouseExited
        // Dejo el cursor como estaba
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_etqLaImagenMouseExited

    private void btnQuitarSeleccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarSeleccionActionPerformed
        // Deselecciono todas las cajas
        deseleccionarCasillas();
    }//GEN-LAST:event_btnQuitarSeleccionActionPerformed

    private void btnProbarAlineacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProbarAlineacionActionPerformed
        // Comprobar alineación. Recargar imagen, giraImagen y casillas, las tres acciones.
        //btnRecargarImagenActionPerformed(evt);
        //btnCorrigeImagenActionPerformed(evt);
        pintarCasillasActionPerformed(evt);
    }//GEN-LAST:event_btnProbarAlineacionActionPerformed

    private void btnRecargarImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecargarImagenActionPerformed
        // Cargo la imagen de siempre. Quitar luego y descomentar
        if (Config.getRutaUltimaImagen().equals("") || Procesador.getImagenTest() == null) {
            menuAbrirArchivoActionPerformed(evt);
        } else {
            cargarImagen(new File(Config.getRutaUltimaImagen()));
        }
    }//GEN-LAST:event_btnRecargarImagenActionPerformed

    private void btnGuardaResultadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardaResultadosActionPerformed
        // Agrego los resultados del test a la tabla de la ventana inicial
        // Analizo los campos y almaceno el resultado en cada uno de ellos
        Procesador.setTestActual(Procesador.extraeResultadosCampos(Procesador.getTestActual(), Procesador.getCasillasTest()));
        // Actualizo la tabla de Test leídos
        Procesador.actualizaTablaTest(Procesador.listaTestsLeidos, Procesador.getTestActual());
    }//GEN-LAST:event_btnGuardaResultadosActionPerformed

    private void btnCorrigeImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCorrigeImagenActionPerformed
        // Busco el punto de la esquinasImagen superior izquierda

        // Si la imagen ya está corregida, no hago nada
        if (isImagenCorregida()) {
            JOptionPane.showOptionDialog(rootPane, idioma.getString("DialogoPruevasTest.imagen_ya_corregida.text"),
                    idioma.getString("Atencion.text"), JOptionPane.NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, Config.OPCION_ACEPTAR, null);
        } else {
            if (Procesador.getImagenTest() != null) {
                try {
                    Procesador.setImagenTest(Procesador.corrigeImagen(Procesador.getImagenTest(), false));
                    etqLaImagen.setIcon(new ImageIcon(Procesador.getImagenTest()));
                    imagenCorredida = true;
                    repaint();
                } catch (NullPointerException ex) {
                    JOptionPane.showOptionDialog(rootPane, idioma.getString("VENTANAPRUEBASTESTS.ERROR_EN_FICHERO.TEXT") + " - " + Procesador.getTestActual().getNombreArchivo(),
                            idioma.getString("VENTANAPRUEBASTESTS.btnAnalizar.TITULO_ERROR.TEXT"), JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
                } catch (RasterFormatException ex) {
                    log.error("Error en Procesador.corrigeImagen. Fuera de límites.\n" + ex.getMessage());
                }
            } else {
                JOptionPane.showOptionDialog(rootPane, idioma.getString("VENTANAPRUEBASTESTS.btnAnalizar.ERROR_SIN_IMAGEN_O_LIMITE.TEXT"),
                        idioma.getString("Error.text"), JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
            }
        }
    }//GEN-LAST:event_btnCorrigeImagenActionPerformed

    private void btnAnalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalizarActionPerformed
        // Pinto las cajas en las coordenadas del fichero.
        // Las coordenadas son del CENTRO DE la casilla, de ancho VentanaInicio.anchoMarcasRespuesta
        int mitadAncho = Config.getAnchoMarcasRespuesta() / 2;

        // Analizo el último archivo cargado
        if (Procesador.getImagenTest() != null && Procesador.getTestActual().getNombreArchivo() != null) {
            // Si no está corregida la imagen, lo hago
            if (!isImagenCorregida()) {
                btnCorrigeImagenActionPerformed(new java.awt.event.ActionEvent(evt.getSource(), 1, "corregir"));
            }
            Procesador.getTestActual().setCasillasMarcadas(Procesador.analizar(Procesador.getImagenTest(), Procesador.getTestActual()));
            if (Procesador.getTestActual().getCasillasMarcadas() != null) {
                Graphics2D g = Procesador.getImagenTest().createGraphics();// .getGraphics();
                g.setColor(Color.RED);
                g.setStroke(new BasicStroke(Config.getGrosorCirculoMarca())); // Grosor del circulo
                for (Casilla unPunto : Procesador.getTestActual().getCasillasMarcadas()) {
                    //g.drawRect(unPunto.getCoordX() - mitadAncho, unPunto.getCoordY() - mitadAncho, Config.getAnchoMarcasRespuesta(), Config.getAnchoMarcasRespuesta());
                    g.drawOval(unPunto.getCoordX() - mitadAncho, unPunto.getCoordY() - mitadAncho, Config.getAnchoMarcasRespuesta(), Config.getAnchoMarcasRespuesta());
                }
                //laImagen.setIcon(new ImageIcon(Procesador.getImagenTest().getScaledInstance(laImagen.getWidth(), laImagen.getWidth()*Config.MODELO_RELACION_ASPECTO, Image.SCALE_DEFAULT)));
                etqLaImagen.setIcon(new ImageIcon(Procesador.getImagenTest()));
                repaint();
                g.dispose();
            } else {
                JOptionPane.showOptionDialog(rootPane, idioma.getString("VENTANAPRUEBASTESTS.btnAnalizar.ERROR_SIN_IMAGEN_O_LIMITE.TEXT"),
                        idioma.getString("Error.text"), JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
            }
        } else {
            JOptionPane.showOptionDialog(rootPane, idioma.getString("VENTANAPRUEBASTESTS.btnAnalizar.ERROR_SIN_IMAGEN_O_LIMITE.TEXT"),
                    idioma.getString("Error.text"), JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
        }
    }//GEN-LAST:event_btnAnalizarActionPerformed

    private void configuracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_configuracionActionPerformed
        // Muestro el diálogo de la ventana de configuración. Le paso el padre y el modo Modal
        DialogoConfiguracion ventaConfig = new DialogoConfiguracion(this, true);
        ventaConfig.pack();
        ventaConfig.setVisible(true);
        // Vuelvo a cargar la ventaConfig, por si hubo cambios
        Procesador.CargarConfiguracion();
    }//GEN-LAST:event_configuracionActionPerformed

    private void btnAyudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAyudaActionPerformed
        // Muestro la ayuda de la ventana
        String error = Procesador.mostrarAyuda(Config.getRutaAyudaPruebas());
        if (!"".equals(error)) {
            log.error(error);
            JOptionPane.showOptionDialog(rootPane, error, idioma.getString("Error.text"),
                    JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{idioma.getString("Aceptar.text")}, idioma.getString("Aceptar.text"));
        }
    }//GEN-LAST:event_btnAyudaActionPerformed

    private void btnRecortaImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecortaImagenActionPerformed
        // Recorta la imagen según los valores de la configuración
        if (Procesador.getImagenTest() != null) {
            try {
                Procesador.setImagenTest(Procesador.recorteMargenesConfigurados(Procesador.getImagenTest()));
                Procesador.setImagenTest(Procesador.cambiaTamano(Procesador.getImagenTest(), Config.ALTO_MODELO));
                etqLaImagen.setIcon(new ImageIcon(Procesador.getImagenTest()));
                repaint();
            } catch (NullPointerException ex) {
                JOptionPane.showOptionDialog(rootPane, idioma.getString("VENTANAPRUEBASTESTS.ERROR_EN_FICHERO.TEXT") + " - " + Procesador.getTestActual().getNombreArchivo(),
                        idioma.getString("VENTANAPRUEBASTESTS.btnAnalizar.TITULO_ERROR.TEXT"), JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
            } catch (RasterFormatException ex) {
                log.error("Error en Procesador.corrigeImagen. Fuera de límites.\n" + ex.getMessage());
            }
        } else {
            JOptionPane.showOptionDialog(rootPane, idioma.getString("VENTANAPRUEBASTESTS.btnAnalizar.ERROR_SIN_IMAGEN_O_LIMITE.TEXT"),
                    idioma.getString("Error.text"), JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
        }
    }//GEN-LAST:event_btnRecortaImagenActionPerformed

    private void menuRestaurarPlantillaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRestaurarPlantillaActionPerformed
        // Restaura el archivo csv original de casillas del test
        // Guardo las coordenadas de las cajas en el fichero csv
        int quedice = JOptionPane.showOptionDialog(rootPane, idioma.getString("DialogoPruebasTest.menuRestaurandoPlantila.text"), idioma.getString("DialogoPruebasTest.menuRestaurandoPlantila.titulo.text"),
                JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{idioma.getString("Aceptar.text"), idioma.getString("Cancelar.text")}, idioma.getString("Cancelar.text"));
        if (quedice == 0) {
            String[] mensajes = Procesador.restauraCasillasTestInicial();
            if (!"".equals(mensajes[0])) {
                // Hubo un error u otro mensaje
                JOptionPane.showOptionDialog(this.getContentPane(), idioma.getString(mensajes[0]), idioma.getString(mensajes[1]),
                        JOptionPane.NO_OPTION, Integer.parseInt(mensajes[2]), null, Config.OPCION_ACEPTAR, null);
            } else {
                // Reinicio el formulario
                if (menuEditandoPlantila.isSelected()) {
                    btnRecargarImagenActionPerformed(evt);
                    btnCorrigeImagenActionPerformed(evt);
                    quitaCajas();
                    // Si se ha cargado la imagen correctamente
                    if (Procesador.getImagenTest() != null) {
                        ponCajas();
                        menuGuardarPlantilla.setEnabled(true);
                        panelEdicionPlantilla.setVisible(true);
                        panelBotones.setVisible(false);
                    } else {
                        menuEditandoPlantila.setSelected(false);
                    }
                }
            }
        }
    }//GEN-LAST:event_menuRestaurarPlantillaActionPerformed

    // Desleccionar todas las casillas
    private void deseleccionarCasillas() {
        for (JCheckBox unaCaja : lasCajas) {
            unaCaja.setSelected(false);
        }
        cajasMarcadas.removeAll(lasCajas);
    }

    /**
     *
     * @param fichero Fichero de imagen a cargar
     */
    private void cargarImagen(File fichero) {
        // Cargo un nuevo test en TestActual
        Procesador.cargarImagen(fichero);
        if (Procesador.getImagenTest() != null) {
            //panelScroll.add(etqLaImagen);
            etqLaImagen.setIcon(new ImageIcon(Procesador.getImagenTest()));
            //panelScroll.repaint();
            etqTamano.setText(idioma.getString("VENTANAPRUEBASTESTS.etqTamano.Cambiado.TEXT") + Procesador.getImagenTest().getWidth() + "x" + Procesador.getImagenTest().getHeight());
            etqNombreArchivo.setText(idioma.getString("VentanaPruebasTest.etqNombreArchivo.text") + fichero.getName());
            // Al estar recién cargada, la imagen no está corregida
            imagenCorredida = false;
        } else {
            JOptionPane.showOptionDialog(rootPane, idioma.getString("VENTANAPRUEBASTESTS.ERROR_EN_FICHERO.TEXT"),
                    idioma.getString("Error.text"), JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
        }
    }

    private void ponCajas() {
        // Cargo la configuración, por si han cambiado las casillas
        if (!Procesador.CargarConfiguracion()) {
            // Si no hay error cargando la configuración
            int ajuste = Config.getAnchoMarcasRespuesta();

            // Agrego en la capa 10 todas las cajas que hay definidas en las coordenadas del fichero.
            // Las coordenadas son del CENTRO DE la casilla
            if (etqLaImagen.getIcon() != null) {
                if (Procesador.getTestActual() != null) {
                    int i = 0;
                    for (Casilla pun : Procesador.getCasillasTest()) {
                        // Agrego una nueva caja
                        JCheckBox caja = nuevaCaja(String.valueOf(i), pun.getCoordX() - ajuste, pun.getCoordY() - ajuste, Config.ANCHO_CAJA);
                        PortaCapas.add(caja, JLayeredPane.PALETTE_LAYER); // Pongo las cajas en la capa 100,palette_layer
                        lasCajas.add(caja);
                        i++;
                    }
                }
            }
        }
    }

    private void quitaCajas() {
        // Quito las cajas de la capa
        if (Procesador.getImagenTest() != null) {
            if (Procesador.getTestActual() != null) {
                for (Component tal : PortaCapas.getComponents()) {
                    // Quito una nueva caja
                    if (tal.getClass().equals(new JCheckBox().getClass())) {
                        PortaCapas.remove(tal);
                    }
                }
                lasCajas.removeAll(lasCajas);
            }
        }
        repaint();
    }

    private JCheckBox nuevaCaja(String nombre, int unaX, int unaY, int anchoAlto) {
        JCheckBox caja = new JCheckBox();
        caja.setName(nombre);
        caja.setBounds(unaX, unaY, anchoAlto, anchoAlto);
        MouseListener ml = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // No hago nada
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // No hago nada
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // No hago nada
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Activo/desactivo el seleccionador automático.
                if (chkSeleccionador.isSelected()) {
                    caja.setSelected(!caja.isSelected());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // No hago nada
            }
        };
        // Oyente para seleccionar cajas
        ItemListener il = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // Caja sola
                if (caja.isSelected()) {
                    cajasMarcadas.add(caja);
                } else {
                    cajasMarcadas.remove(caja);
                }
                // Lanzo un click en la imagen, por si están marcadas
            }
        };
        // Agreo los listeners
        caja.addMouseListener(ml);
        caja.addItemListener(il);
        return caja;
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
            java.util.logging.Logger.getLogger(DialogoPruebasTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogoPruebasTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogoPruebasTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogoPruebasTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(() -> {
//            DialogoPruebasTest dialog = new DialogoPruebasTest(new javax.swing.JFrame(), true);
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
    private javax.swing.JPanel PanelControl;
    private javax.swing.JPanel PanelPrincipal;
    private javax.swing.JLayeredPane PortaCapas;
    private javax.swing.JButton btnAnalizar;
    private javax.swing.JButton btnAyuda;
    private javax.swing.JButton btnCorrigeImagen;
    private javax.swing.JButton btnGuardaResultados;
    private javax.swing.JButton btnProbarAlineacion;
    private javax.swing.JButton btnQuitarSeleccion;
    private javax.swing.JButton btnRecargarImagen;
    private javax.swing.JButton btnRecortaImagen;
    private javax.swing.JMenuItem buscarEsquinas;
    private javax.swing.JCheckBox chkSeleccionador;
    private javax.swing.JCheckBox chkSeleccionarColumna;
    private javax.swing.JCheckBox chkSeleccionarFila;
    private javax.swing.JMenuItem configuracion;
    private javax.swing.JLabel etqBrillo;
    private javax.swing.JLabel etqBrilloMedio;
    private javax.swing.JLabel etqCoordenadas;
    private javax.swing.JLabel etqLaImagen;
    private javax.swing.JLabel etqMoverCasillas;
    private javax.swing.JLabel etqNombreArchivo;
    private javax.swing.JLabel etqTamano;
    private javax.swing.JLabel etqUltimoClick;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem menuAbrirArchivo;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JCheckBoxMenuItem menuEditandoPlantila;
    private javax.swing.JMenuItem menuGuardarPlantilla;
    private javax.swing.JMenuItem menuRestaurarPlantilla;
    private javax.swing.JMenu modificarPlantilla;
    private javax.swing.JMenu opciones;
    private javax.swing.JPanel panelBotones;
    private javax.swing.JPanel panelDatos;
    private javax.swing.JPanel panelEdicionPlantilla;
    private javax.swing.JPanel panelImagen;
    private javax.swing.JScrollPane panelScroll;
    private javax.swing.JMenuItem pintarCasillas;
    private javax.swing.JMenu pruebas;
    // End of variables declaration//GEN-END:variables
}
