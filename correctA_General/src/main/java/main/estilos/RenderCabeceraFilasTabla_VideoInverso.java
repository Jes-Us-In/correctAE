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

package main.estilos;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Jesus.delBuey
 */
// Clase para negrita
public class RenderCabeceraFilasTabla_VideoInverso extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
            int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        //this.setFont(this.getFont().deriveFont(Font.BOLD));
        this.setHorizontalAlignment(SwingConstants.CENTER);
        Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
        Color fondo = new JTable().getBackground();
        // Cada 5 filas, cambio el color
        if ((row + 1) % 5 == 0) {
            c.setBackground(Color.DARK_GRAY);
            c.setForeground(Color.white);
            c.setFont(this.getFont().deriveFont(Font.BOLD));
        } else {
            c.setBackground(fondo);
            c.setForeground(Color.black);
        }

        return this;
    }
}
