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

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import main.Config;

/**
 *
 * @author Jesus.delBuey
 */
public class RenderAlineadoFuenteActual extends DefaultTableCellRenderer {
    // Para centrar el contenido de las cuatro primeras columnas
    // Y poner la fuente del tamaño que esté vigente
    private int alineacion = SwingConstants.LEFT;

    public RenderAlineadoFuenteActual(int alin) {
        alineacion = alin;
    }

    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // allow default preparation
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Cambia la Fuento y el alineamiento al centro
        setFont(Config.FUENTE_NORMAL);
        setHorizontalAlignment(alineacion);
        return this;
    }
}
