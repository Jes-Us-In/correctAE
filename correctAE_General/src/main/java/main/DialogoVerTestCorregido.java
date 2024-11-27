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
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Jesus.delBuey
 */
public class DialogoVerTestCorregido extends javax.swing.JDialog {

    protected Loguero log = Procesador.getLog();
    ResourceBundle idioma = Procesador.idioma;
    // 
    Map<String, String> test;
    JTableHeader cabecera;

    private boolean HayCambios;

    /**
     * Get the value of HayCambios
     *
     * @return the value of HayCambios
     */
    public boolean isHayCambios() {
        return HayCambios;
    }

    /**
     * Creates new form DialogoVerTest
     *
     * @param parent Padre del diálogo
     * @param modal Si es modal o no
     * @param tst Mapa con todos los campos del test corregido
     */
    public DialogoVerTestCorregido(JDialog parent, boolean modal, Map<String, String> tst) {
        super(parent, modal);
        initComponents();
        this.setIconImage(Config.getIconoAplic().getImage());
        test = tst;
        HayCambios = false;
        InicializarFormulario();
    }

    private void InicializarFormulario() {
        // Coloco el formulario en el centro de la pantalla
        Procesador.Centrame(this);
        //
        // Pongo el tamaño del formulario
        //this.setSize(panelRespuestas.getWidth() + PanelDatos1.getWidth() + 30, panelRespuestas.getHeight() + 125);
        //
        etqNumExamen.setText(idioma.getString("DialogoVerTestCorregido.etqNumExamen.text").concat(test.get("num")));
        campoNIF.setText(test.get("nif"));
        comboTipo.setSelectedIndex("?123456".indexOf(test.get("tipo")));
        comboGrupo.setSelectedIndex("?123456".indexOf(test.get("grupo")));
        etqAciertos.setText(idioma.getString("DialogoVerTestCorregido.etqAciertos.text").concat(test.get("aciertos")));
        etqFallos.setText(idioma.getString("DialogoVerTestCorregido.etqFallos.text").concat(test.get("fallos")));
        etqBlancos.setText(idioma.getString("DialogoVerTestCorregido.etqBlancos.text").concat(test.get("blancos")));
        etqDobles.setText(idioma.getString("DialogoVerTestCorregido.etqDobles.text").concat(test.get("dobles")));
        etqNota.setText(idioma.getString("DialogoVerTestCorregido.etqNota.text").concat(test.get("nota")));
        //
        formateaColumnas();
        DefaultTableModel modelo = (DefaultTableModel) tablaRespuestas.getModel();
        char[] resp = new char[Config.COLUMNAS_POR_HOJA];

        // Extraigo las respuestas
        // 5 Columnas de 20 preguntas
        //
        for (int i = 1; i <= Config.PREGUNTAS_POR_COLUMNA; i++) {
            resp[0] = test.get("respuestas").charAt(i - 1);
            resp[1] = test.get("respuestas").charAt(i + 19);
            resp[2] = test.get("respuestas").charAt(i + 39);
            resp[3] = test.get("respuestas").charAt(i + 59);
            resp[4] = test.get("respuestas").charAt(i + 79);
            // Marco true en la que esté marcada, o todas si hay una interrogacion
            Object[] fila = {
                i, resp[0] == 'A' || resp[0] == '?', resp[0] == 'B' || resp[0] == '?', resp[0] == 'C' || resp[0] == '?', resp[0] == 'D' || resp[0] == '?', resp[0] == 'E' || resp[0] == '?',
                i + 20, resp[1] == 'A' || resp[1] == '?', resp[1] == 'B' || resp[1] == '?', resp[1] == 'C' || resp[1] == '?', resp[1] == 'D' || resp[1] == '?', resp[1] == 'E' || resp[1] == '?',
                i + 40, resp[2] == 'A' || resp[2] == '?', resp[2] == 'B' || resp[2] == '?', resp[2] == 'C' || resp[2] == '?', resp[2] == 'D' || resp[2] == '?', resp[2] == 'E' || resp[2] == '?',
                i + 60, resp[3] == 'A' || resp[3] == '?', resp[3] == 'B' || resp[3] == '?', resp[3] == 'C' || resp[3] == '?', resp[3] == 'D' || resp[3] == '?', resp[3] == 'E' || resp[3] == '?',
                i + 80, resp[4] == 'A' || resp[4] == '?', resp[4] == 'B' || resp[4] == '?', resp[4] == 'C' || resp[4] == '?', resp[4] == 'D' || resp[4] == '?', resp[4] == 'E' || resp[4] == '?',};
            modelo.addRow(fila);
            //TableColumnModel m;
        }
    }

    // Mequede
    private void formateaColumnas() {
        // Formateo la cabecera
        cabecera = tablaRespuestas.getTableHeader();
        cabecera.setDefaultRenderer(new main.estilos.RenderCabeceraTablas_AlineaCentro(tablaRespuestas));
        cabecera.setFont(tablaRespuestas.getTableHeader().getFont().deriveFont(Font.BOLD));

        TableColumnModel columnModel = tablaRespuestas.getColumnModel();
        TableColumn col;
        // Columna de respuesta correcta, son JComboBox. En la tabla se pone como booleano y es suficiente
        // Columna de número de respuesta, pongo estilo cabecera, el resto estilo checkbox
        DefaultTableCellRenderer ren = new main.estilos.RenderCabeceraFilasTabla_VideoInverso();
        tablaRespuestas.setRowHeight(18);
        int minAnch = tablaRespuestas.getFont().getSize() * 3 + 10;
        int maxAnch = tablaRespuestas.getFont().getSize() * 2 - 3;
        int maxAnch2X = maxAnch * 2;
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            col = columnModel.getColumn(i);
            if (i == 0 || i == 6 || i == 12 || i == 18 || i == 24) {
                col.setCellRenderer(ren);
                col.setHeaderRenderer(ren);
                // Ancho minimo de la columna Num.
                col.setMinWidth(minAnch);
                // Ancho maximo de la columna Num.
                col.setMaxWidth(maxAnch2X);
            } else {
                // Ancho máximo de columna de letra de respuesta
                col.setMaxWidth(maxAnch);
            }
        }
    }

    /**
     *
     * @param imagen Imagen original
     * @return imagen redimensionada, según el tamaño del contenedor "laImagen"
     */
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelDatos1 = new javax.swing.JPanel();
        etqNumExamen = new javax.swing.JLabel();
        etqNif = new javax.swing.JLabel();
        campoNIF = new javax.swing.JTextField();
        etqTipo = new javax.swing.JLabel();
        comboTipo = new javax.swing.JComboBox<>();
        etqGrupo = new javax.swing.JLabel();
        comboGrupo = new javax.swing.JComboBox<>();
        panelDatos2 = new javax.swing.JPanel();
        etqAciertos = new javax.swing.JLabel();
        etqFallos = new javax.swing.JLabel();
        etqDobles = new javax.swing.JLabel();
        etqBlancos = new javax.swing.JLabel();
        etqNota = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        panelRespuestas = new javax.swing.JPanel();
        panelScroll = new javax.swing.JScrollPane();
        tablaRespuestas = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("propiedades/Idioma"); // NOI18N
        setTitle(bundle.getString("DialogoVerTestCorregido.Titulo.text")); // NOI18N
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setResizable(false);
        setSize(new java.awt.Dimension(0, 0));

        PanelDatos1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        PanelDatos1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        PanelDatos1.setFont(Config.FUENTE_NORMAL);

        etqNumExamen.setFont(Config.FUENTE_NORMAL.deriveFont(Font.BOLD));
        etqNumExamen.setText(bundle.getString("DialogoVerTestCorregido.etqNumExamen.text")); // NOI18N
        etqNumExamen.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        etqNif.setFont(Config.FUENTE_TITULO);
        etqNif.setText(bundle.getString("DialogoVerTestCorregido.etqNif.text")); // NOI18N
        etqNif.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        campoNIF.setFont(Config.FUENTE_NORMAL.deriveFont(Font.BOLD));
        campoNIF.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        campoNIF.setMinimumSize(new java.awt.Dimension(200, 22));

        etqTipo.setFont(Config.FUENTE_TITULO);
        etqTipo.setText(bundle.getString("DialogoVerTestCorregido.etqTipo.text")); // NOI18N

        comboTipo.setFont(Config.FUENTE_NORMAL);
        comboTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "?", "1", "2", "3", "4", "5", "6" }));

        etqGrupo.setFont(Config.FUENTE_TITULO);
        etqGrupo.setText(bundle.getString("DialogoVerTestCorregido.etqGrupo.text")); // NOI18N

        comboGrupo.setFont(Config.FUENTE_NORMAL);
        comboGrupo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "?", "1", "2", "3", "4", "5", "6" }));

        javax.swing.GroupLayout PanelDatos1Layout = new javax.swing.GroupLayout(PanelDatos1);
        PanelDatos1.setLayout(PanelDatos1Layout);
        PanelDatos1Layout.setHorizontalGroup(
            PanelDatos1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDatos1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(PanelDatos1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelDatos1Layout.createSequentialGroup()
                        .addComponent(etqGrupo)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(comboGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelDatos1Layout.createSequentialGroup()
                        .addComponent(etqTipo)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(comboTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(etqNumExamen)
                    .addGroup(PanelDatos1Layout.createSequentialGroup()
                        .addComponent(etqNif)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(campoNIF, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        PanelDatos1Layout.setVerticalGroup(
            PanelDatos1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDatos1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(etqNumExamen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelDatos1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(etqNif)
                    .addComponent(campoNIF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelDatos1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(etqTipo)
                    .addComponent(comboTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelDatos1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(etqGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        panelDatos2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelDatos2.setFont(Config.FUENTE_NORMAL);
        panelDatos2.setMinimumSize(new java.awt.Dimension(298, 40));

        etqAciertos.setFont(Config.FUENTE_NORMAL);
        etqAciertos.setText(bundle.getString("DialogoVerTestCorregido.etqAciertos.text")); // NOI18N

        etqFallos.setFont(Config.FUENTE_NORMAL);
        etqFallos.setText(bundle.getString("DialogoVerTestCorregido.etqFallos.text")); // NOI18N

        etqDobles.setFont(Config.FUENTE_NORMAL);
        etqDobles.setText(bundle.getString("DialogoVerTestCorregido.etqDobles.text")); // NOI18N

        etqBlancos.setFont(Config.FUENTE_NORMAL);
        etqBlancos.setText(bundle.getString("DialogoVerTestCorregido.etqBlancos.text")); // NOI18N

        etqNota.setFont(Config.FUENTE_TITULO.deriveFont(Font.BOLD)
        );
        etqNota.setText(bundle.getString("DialogoVerTestCorregido.etqNota.text")); // NOI18N

        javax.swing.GroupLayout panelDatos2Layout = new javax.swing.GroupLayout(panelDatos2);
        panelDatos2.setLayout(panelDatos2Layout);
        panelDatos2Layout.setHorizontalGroup(
            panelDatos2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatos2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(panelDatos2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(etqAciertos)
                    .addComponent(etqFallos)
                    .addComponent(etqDobles)
                    .addComponent(etqBlancos)
                    .addComponent(etqNota))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelDatos2Layout.setVerticalGroup(
            panelDatos2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatos2Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(etqAciertos)
                .addGap(14, 14, 14)
                .addComponent(etqFallos)
                .addGap(14, 14, 14)
                .addComponent(etqDobles)
                .addGap(14, 14, 14)
                .addComponent(etqBlancos)
                .addGap(14, 14, 14)
                .addComponent(etqNota)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnGuardar.setFont(Config.FUENTE_TITULO);
        btnGuardar.setText(bundle.getString("DialogoVerTestCorregido.btnGuardar.text")); // NOI18N
        btnGuardar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        panelRespuestas.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelRespuestas.setPreferredSize(new java.awt.Dimension(1256, 410));

        panelScroll.setPreferredSize(new java.awt.Dimension(1240, 402));

        tablaRespuestas.setFont(Config.FUENTE_NORMAL);
        tablaRespuestas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Num.", "A", "B", "C", "D", "E", "Num.", "A", "B", "C", "D", "E", "Num.", "A", "B", "C", "D", "E", "Num.", "A", "B", "C", "D", "E", "Num.", "A", "B", "C", "D", "E"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, false, true, true, true, true, true, false, true, true, true, true, true, false, true, true, true, true, true, false, true, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaRespuestas.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tablaRespuestas.setShowGrid(true);
        panelScroll.setViewportView(tablaRespuestas);

        javax.swing.GroupLayout panelRespuestasLayout = new javax.swing.GroupLayout(panelRespuestas);
        panelRespuestas.setLayout(panelRespuestasLayout);
        panelRespuestasLayout.setHorizontalGroup(
            panelRespuestasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRespuestasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 846, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );
        panelRespuestasLayout.setVerticalGroup(
            panelRespuestasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRespuestasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnGuardar)
                    .addComponent(PanelDatos1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelDatos2, javax.swing.GroupLayout.PREFERRED_SIZE, 161, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelRespuestas, javax.swing.GroupLayout.DEFAULT_SIZE, 866, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelRespuestas, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(PanelDatos1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panelDatos2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnGuardar)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // Guardar los cambios. Compruebo que el nif tiene 8 digitos, un guion y la letra.
        campoNIF.setText(campoNIF.getText().toUpperCase());
        if (Pattern.matches("\\d{8}-[A-Z]", campoNIF.getText())) {
            actualizaCambios();
            HayCambios = true;
            this.dispose();
        } else {
            JOptionPane.showOptionDialog(rootPane, idioma.getString("DialogoVerTestCorregido.error.nif.text"), idioma.getString("Error.text"),
                    JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void actualizaCambios() {
        // Actualizo los datos del test y activo la bandera de cambios.
        // El campo num, no lo cambio, ni la corrección, sólo nif, tipo, grupo y respuestas.
        String valores1a6 = "?123456";
        test.put("nif", campoNIF.getText());
        if (comboTipo.getSelectedIndex() >= 0 && comboTipo.getSelectedIndex() < valores1a6.length()) {
            test.put("tipo", valores1a6.substring(comboTipo.getSelectedIndex(), comboTipo.getSelectedIndex() + 1));
        } else {
            test.put("tipo", " ");
        }
        if (comboGrupo.getSelectedIndex() >= 0 && comboGrupo.getSelectedIndex() < valores1a6.length()) {
            test.put("grupo", valores1a6.substring(comboGrupo.getSelectedIndex(), comboGrupo.getSelectedIndex() + 1));
        } else {
            test.put("grupo", " ");
        }
        StringBuilder respuestas = new StringBuilder();
        DefaultTableModel modelo = (DefaultTableModel) tablaRespuestas.getModel();
        int posicion, suma;

        char[] opciones = {' ', 'A', 'B', 'C', 'D', 'E'};

        // Relleno el string
        for (int pos = 0; pos < Config.MAX_NUM_PREGUNTAS; pos++) {
            //respuestas[pos] = '.';
            respuestas.append(' ');
        }
        // Extraigo las respuestas
        // Grupos de columnas. hay cinco, donde empieza la primera opción de respuesta. Columnas de la tabla, 1,7,13,19 y 25
        // Tiene 20 preguntas cada grupo/columna, Config.PREGUNTAS_POR_COLUMNA 
        int[] grupos = {1, 7, 13, 19, 25};
        int pregInicioGrupo;
        for (int grupoPreg = 0; grupoPreg < grupos.length; grupoPreg++) {
            // Asigno el inicio del grupo de 20 preguntas. Hay cinco grupos, en total 100 preguntas.
            pregInicioGrupo = grupoPreg * Config.PREGUNTAS_POR_COLUMNA;
            for (int numFila = 0; numFila < Config.PREGUNTAS_POR_COLUMNA; numFila++) {
                // Genero un string con las respuestas nuevas
                suma = 0;
                posicion = 0;
                for (int colLetra = grupos[grupoPreg]; colLetra < grupos[grupoPreg] + 5; colLetra++) {
                    suma = (boolean) modelo.getValueAt(numFila, colLetra) ? suma + 1 : suma;
                    posicion = (boolean) modelo.getValueAt(numFila, colLetra) ? colLetra - grupos[grupoPreg] + 1 : posicion;
                }
                //System.out.println("Pregunta " + (grupos[grupoPreg] + numFila) + " - Suma " + suma);
                if (suma > 1) {
                    respuestas.setCharAt(pregInicioGrupo + numFila, '?');
                } else {
                    respuestas.setCharAt(pregInicioGrupo + numFila, opciones[posicion]);
                }
            }
        }
        // Acutalizo las respuestas
        test.put("respuestas", respuestas.toString());
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelDatos1;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JTextField campoNIF;
    private javax.swing.JComboBox<String> comboGrupo;
    private javax.swing.JComboBox<String> comboTipo;
    private javax.swing.JLabel etqAciertos;
    private javax.swing.JLabel etqBlancos;
    private javax.swing.JLabel etqDobles;
    private javax.swing.JLabel etqFallos;
    private javax.swing.JLabel etqGrupo;
    private javax.swing.JLabel etqNif;
    private javax.swing.JLabel etqNota;
    private javax.swing.JLabel etqNumExamen;
    private javax.swing.JLabel etqTipo;
    private javax.swing.JPanel panelDatos2;
    private javax.swing.JPanel panelRespuestas;
    private javax.swing.JScrollPane panelScroll;
    private javax.swing.JTable tablaRespuestas;
    // End of variables declaration//GEN-END:variables
}
