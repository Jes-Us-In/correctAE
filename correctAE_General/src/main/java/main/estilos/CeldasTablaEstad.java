/*
 * Copyright (C) 2024 Jesus.delBuey
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
 */
package main.estilos;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Jesus.delBuey
 */
public class CeldasTablaEstad extends DefaultTableCellRenderer {

    Font fuente;
    private final Set<Point> celdas;
    Color fondoAlt = new Color(212, 235, 236);

    public CeldasTablaEstad(Font laFuente, Set<Point> celdasNegritas) {
        this.celdas = celdasNegritas;
        this.fuente = laFuente;
        setOpaque(true);
        setHorizontalAlignment(JLabel.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText(value != null ? value.toString() : "");
        if (celdas.contains(new Point(row, column))) {
            setFont(fuente.deriveFont(Font.BOLD));
        } else {
            setFont(fuente.deriveFont(Font.PLAIN));
        }
        if (row % 2 == 0) {
            setBackground(Color.WHITE);
        } else {
            setBackground(fondoAlt);
        }
        return this;
    }
}
