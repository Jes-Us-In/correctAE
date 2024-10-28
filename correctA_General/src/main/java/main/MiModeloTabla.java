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

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Jesus.delBuey
 */
public class MiModeloTabla extends DefaultTableModel {

    // Defino las columnas. En un array de strings vienen los nombres
    public MiModeloTabla(String[] columnas) {
        super.setColumnIdentifiers(columnas);
    }
    
    public MiModeloTabla() {
    }
    
// Deshabilito la edición de las filas
    @Override
    public boolean isCellEditable(int row, int column) {
        //return super.isCellEditable(row, column); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        return false;
    }

}
