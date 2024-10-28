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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import static main.Procesador.idioma;

/**
 *
 * @author Jesus.delBuey
 */
public class DialogoParamCalific extends javax.swing.JDialog {

    // log de la aplicacion
    private final Loguero log = Procesador.getLog();

//public ResourceBundle idioma = Procesador.idioma;
    // Verificador de que es un número válido, para la puntucación
    VerificadorNumero verif = new VerificadorNumero();

    // Indico al dialogo padre, que pulsó el botón Corregir
    private boolean corregir = false;

    /**
     * Get the value of corregir
     *
     * @return the value of corregir
     */
    public boolean isCorregir() {
        return corregir;
    }

    // En un array de float guardo los valores de cálculo de la nota:
    // Acierto, Fallo, Blanco, Doble, aprobado y escalaCalificiacion;
    //
    private final float[] valsCalculo = {Config.getPuntosAcierto(), Config.getPuntosFallo(), Config.getPuntosBlanco(), Config.getPuntosDoble(),
        Config.getPuntAprobado(), Config.getEscalaCalificacion()};
    // Array de JTextField del formulario implicados. para poder manejarlos mejor
    private JTextField[] camposTexto;

    /**
     * Creates new form DialogoParamCalific
     *
     * @param parent Padre del dialogo
     * @param modal Si será modal o no
     */
    public DialogoParamCalific(javax.swing.JDialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
        InicializarFormulario();
    }

    // Inicialización del formulario
    private void InicializarFormulario() {
        this.setIconImage(Config.getIconoAplic().getImage());
        // Coloco el formulario en el centro de la pantalla
        Procesador.Centrame(this);
        etqTipos.setText(idioma.getString("DialogoParamCalific.etqTipos.text").concat(String.valueOf(Config.getNumTipos())));
        etqPreguntas.setText(idioma.getString("DialogoParamCalific.etqPreguntas.text").concat(String.valueOf(Config.getNumPreguntas()))
                .concat(" (").concat(idioma.getString("DialogoTipos.etqPreguntasValidas.text")).concat(" ").concat(String.valueOf(Config.getNumPreguntasValidas())).concat(")"));
        // Asocio los JTextField
        camposTexto = new JTextField[]{valorAcierto, valorFallo, valorBlanco, valorDobleMarca, valorPuntAprobado, valorEscalaCalificacion};
        // Asigno a los campos los valores de la configuracion y el verificador de que es un valor numérico aceptable
        for (int i = 0; i < camposTexto.length; i++) {
            camposTexto[i].setInputVerifier(verif);
            camposTexto[i].setText(String.format("%.2f", valsCalculo[i]));
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
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        etqParametrosEvaluacion = new javax.swing.JLabel();
        etqTipos = new javax.swing.JLabel();
        etqPreguntas = new javax.swing.JLabel();
        panelParametrosCorreccion = new javax.swing.JPanel();
        etqPuntuacionAcierto = new javax.swing.JLabel();
        valorAcierto = new javax.swing.JTextField();
        etqPuntuacionFallo = new javax.swing.JLabel();
        valorFallo = new javax.swing.JTextField();
        etqPuntuacionBlanco = new javax.swing.JLabel();
        valorBlanco = new javax.swing.JTextField();
        etqPuntuacionDobleMarca = new javax.swing.JLabel();
        valorDobleMarca = new javax.swing.JTextField();
        etqPuntuacionAprobado = new javax.swing.JLabel();
        valorPuntAprobado = new javax.swing.JTextField();
        etqEscalaCalificacion = new javax.swing.JLabel();
        valorEscalaCalificacion = new javax.swing.JTextField();
        btnCorregir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("propiedades/Idioma"); // NOI18N
        setTitle(bundle.getString("DialogoParamCalific.titulo.text")); // NOI18N
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        etqParametrosEvaluacion.setFont(Config.FUENTE_TITULO.deriveFont(Font.BOLD));
        etqParametrosEvaluacion.setText(bundle.getString("DialogoParamCalific.etqParametrosEvaluacion.text")); // NOI18N
        etqParametrosEvaluacion.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        etqTipos.setFont(Config.FUENTE_TITULO);
        etqTipos.setText(bundle.getString("DialogoParamCalific.etqTipos.text")); // NOI18N

        etqPreguntas.setFont(Config.FUENTE_TITULO);
        etqPreguntas.setText(bundle.getString("DialogoParamCalific.etqPreguntas.text")); // NOI18N

        panelParametrosCorreccion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelParametrosCorreccion.setFont(Config.FUENTE_NORMAL);
        panelParametrosCorreccion.setLayout(new java.awt.GridBagLayout());

        etqPuntuacionAcierto.setFont(Config.FUENTE_NORMAL);
        etqPuntuacionAcierto.setText(bundle.getString("DialogoParamCalific.etqPuntuacionAcierto.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panelParametrosCorreccion.add(etqPuntuacionAcierto, gridBagConstraints);

        valorAcierto.setFont(Config.FUENTE_NORMAL);
        valorAcierto.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        valorAcierto.setText(bundle.getString("DialogoParamCalific.valorAcierto.text")); // NOI18N
        valorAcierto.setMinimumSize(new java.awt.Dimension(74, 22));
        valorAcierto.setPreferredSize(new java.awt.Dimension(60, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panelParametrosCorreccion.add(valorAcierto, gridBagConstraints);

        etqPuntuacionFallo.setFont(Config.FUENTE_NORMAL);
        etqPuntuacionFallo.setText(bundle.getString("DialogoParamCalific.etqPuntuacionFallo.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panelParametrosCorreccion.add(etqPuntuacionFallo, gridBagConstraints);

        valorFallo.setFont(Config.FUENTE_NORMAL);
        valorFallo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        valorFallo.setText(bundle.getString("DialogoParamCalific.valorFallo.text")); // NOI18N
        valorFallo.setMinimumSize(new java.awt.Dimension(74, 22));
        valorFallo.setPreferredSize(new java.awt.Dimension(60, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panelParametrosCorreccion.add(valorFallo, gridBagConstraints);

        etqPuntuacionBlanco.setFont(Config.FUENTE_NORMAL);
        etqPuntuacionBlanco.setText(bundle.getString("DialogoParamCalific.etqPuntuacionBlanco.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panelParametrosCorreccion.add(etqPuntuacionBlanco, gridBagConstraints);

        valorBlanco.setFont(Config.FUENTE_NORMAL);
        valorBlanco.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        valorBlanco.setText(bundle.getString("DialogoParamCalific.valorBlanco.text")); // NOI18N
        valorBlanco.setMinimumSize(new java.awt.Dimension(74, 22));
        valorBlanco.setPreferredSize(new java.awt.Dimension(60, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panelParametrosCorreccion.add(valorBlanco, gridBagConstraints);

        etqPuntuacionDobleMarca.setFont(Config.FUENTE_NORMAL);
        etqPuntuacionDobleMarca.setText(bundle.getString("DialogoParamCalific.etqPuntuacionDobleMarca.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panelParametrosCorreccion.add(etqPuntuacionDobleMarca, gridBagConstraints);

        valorDobleMarca.setFont(Config.FUENTE_NORMAL);
        valorDobleMarca.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        valorDobleMarca.setText(bundle.getString("DialogoParamCalific.valorDobleMarca.text")); // NOI18N
        valorDobleMarca.setMinimumSize(new java.awt.Dimension(74, 22));
        valorDobleMarca.setPreferredSize(new java.awt.Dimension(60, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panelParametrosCorreccion.add(valorDobleMarca, gridBagConstraints);

        etqPuntuacionAprobado.setFont(Config.FUENTE_NORMAL);
        etqPuntuacionAprobado.setText(bundle.getString("DialogoParamCalific.etqPuntuacionAprobado.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panelParametrosCorreccion.add(etqPuntuacionAprobado, gridBagConstraints);

        valorPuntAprobado.setFont(Config.FUENTE_NORMAL);
        valorPuntAprobado.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        valorPuntAprobado.setText(bundle.getString("DialogoParamCalific.valorPuntAprobado.text")); // NOI18N
        valorPuntAprobado.setMinimumSize(new java.awt.Dimension(74, 22));
        valorPuntAprobado.setPreferredSize(new java.awt.Dimension(60, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panelParametrosCorreccion.add(valorPuntAprobado, gridBagConstraints);

        etqEscalaCalificacion.setFont(Config.FUENTE_NORMAL.deriveFont(Font.BOLD)
        );
        etqEscalaCalificacion.setText(bundle.getString("DialogoParamCalific.etqEscalaCalificacion.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panelParametrosCorreccion.add(etqEscalaCalificacion, gridBagConstraints);

        valorEscalaCalificacion.setFont(Config.FUENTE_NORMAL.deriveFont(Font.BOLD));
        valorEscalaCalificacion.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        valorEscalaCalificacion.setText(bundle.getString("DialogoParamCalific.valorEscalaCalificacion.text")); // NOI18N
        valorEscalaCalificacion.setPreferredSize(new java.awt.Dimension(60, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panelParametrosCorreccion.add(valorEscalaCalificacion, gridBagConstraints);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(etqPreguntas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(etqTipos))
                    .addComponent(panelParametrosCorreccion, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                    .addComponent(etqParametrosEvaluacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(etqParametrosEvaluacion, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(etqTipos)
                    .addComponent(etqPreguntas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelParametrosCorreccion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnCorregir.setFont(Config.FUENTE_NORMAL);
        btnCorregir.setText(bundle.getString("DialogoParamCalific.btnCorregir.text")); // NOI18N
        btnCorregir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCorregirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnCorregir)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCorregir)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCorregirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCorregirActionPerformed
        try {
            // Sustituyo las comas decimales, si las hay, por puntos para evitar errores de cálculo
            for (int i = 0; i < camposTexto.length; i++) {
                camposTexto[i].setText(camposTexto[i].getText().replace(',', '.'));
            }
            // Corrijo y puntúo los exámenes, quito las comas decimales, y las cambio por un punto para que no falle
            // uso un formato de separador decimal, independiente del locale. Float.parseFloat sólo funciona con el punto inglés.
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator('.');
            DecimalFormat formato = new DecimalFormat("0.#");
            formato.setDecimalFormatSymbols(symbols);
            for (int i = 0; i < camposTexto.length; i++) {
                valsCalculo[i] = formato.parse(camposTexto[i].getText()).floatValue();
            }
            // Compruevo los valores
            // Acierto, Fallo, Blanco, Doble, aprobado y escalaCalificiacion;
            if (valsCalculo[0] < 0 || valsCalculo[0] > 10
                    || valsCalculo[1] < 0 || valsCalculo[1] > 10
                    || valsCalculo[2] < 0 || valsCalculo[2] > 10
                    || valsCalculo[3] < 0 || valsCalculo[3] > 10
                    || valsCalculo[4] < 0 || valsCalculo[4] > 10
                    || valsCalculo[5] < 1 || valsCalculo[5] > 100) {
                // Hay error en los valores
                JOptionPane.showOptionDialog(rootPane, idioma.getString("DialogoParamCalific.errorValor.text"), idioma.getString("Error.text"),
                        JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
            } else {
                // Está todo bien y puedo corregir
                // Guardo los valores en la configuracion
                Config.setPuntosAcierto(valsCalculo[0]);
                Config.setPuntosFallo(valsCalculo[1]);
                Config.setPuntosBlanco(valsCalculo[2]);
                Config.setPuntosDoble(valsCalculo[3]);
                Config.setPuntAprobado(valsCalculo[4]);
                Config.setEscalaCalificacion(valsCalculo[5]);
                Config.guardarConfiguracion();
                corregir = true;
                this.dispose();
            }
        } catch (ParseException ex) {
            log.error(ex.getMessage());
        }
    }//GEN-LAST:event_btnCorregirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCorregir;
    private javax.swing.JLabel etqEscalaCalificacion;
    private javax.swing.JLabel etqParametrosEvaluacion;
    private javax.swing.JLabel etqPreguntas;
    private javax.swing.JLabel etqPuntuacionAcierto;
    private javax.swing.JLabel etqPuntuacionAprobado;
    private javax.swing.JLabel etqPuntuacionBlanco;
    private javax.swing.JLabel etqPuntuacionDobleMarca;
    private javax.swing.JLabel etqPuntuacionFallo;
    private javax.swing.JLabel etqTipos;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel panelParametrosCorreccion;
    private javax.swing.JTextField valorAcierto;
    private javax.swing.JTextField valorBlanco;
    private javax.swing.JTextField valorDobleMarca;
    private javax.swing.JTextField valorEscalaCalificacion;
    private javax.swing.JTextField valorFallo;
    private javax.swing.JTextField valorPuntAprobado;
    // End of variables declaration//GEN-END:variables
}
