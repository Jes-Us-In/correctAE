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

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Jesus.delBuey
 */
public class VerificadorNumero extends InputVerifier {

    @Override
    public boolean verify(JComponent input) {
        String valorStr = ((JTextComponent) input).getText();
        try {
            float valor = Float.parseFloat(valorStr.replace(',', '.'));
            return true;
        } catch (NumberFormatException ex) {
            JOptionPane.showOptionDialog(input.getParent(), Config.getIdioma().getString("DialogoParamCalific.errorValor.text"), Config.getIdioma().getString("Atencion.text"),
                    JOptionPane.NO_OPTION, JOptionPane.WARNING_MESSAGE, null, Config.OPCION_ACEPTAR, null);
        }
        return false;
    }
}
