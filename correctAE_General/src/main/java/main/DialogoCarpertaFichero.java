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

import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.JFileChooser;
import javax.swing.UIManager;

/**
 *
 * @author Jesus.delBuey
 */
public class DialogoCarpertaFichero extends JFileChooser {

    // Para seleccionar un fichero para guardar
    ResourceBundle idioma = Procesador.idioma;
    private final ArrayList<String> claves;

    // Para seleccionar un fichero para leer o una carpeta
    public DialogoCarpertaFichero() {
        this.claves = new ArrayList<>();
        ponClaves();
    }

    private void ponClaves() {
        // Actualizao las claves en el idioma que esté
        this.claves.add("FileChooser.cancelButtonText");
        this.claves.add("FileChooser.lookInLabelText");
        this.claves.add("FileChooser.filesOfTypeLabelText");
        this.claves.add("FileChooser.upFolderToolTipText");
        this.claves.add("FileChooser.homeFolderToolTipText");
        this.claves.add("FileChooser.untitledFileName");
        this.claves.add("FileChooser.untitledFolderName");
        this.claves.add("FileChooser.newFolderButtonText");
        this.claves.add("FileChooser.newFolderToolTipText");
        this.claves.add("FileChooser.listViewButtonToolTipText");
        this.claves.add("FileChooser.detailsViewButtonToolTipText");
        this.claves.add("FileChooser.folderNameLabelText");
        this.claves.add("FileChooser.fileNameLabelText");
        this.claves.add("FileChooser.openButtonText");
        this.claves.add("FileChooser.saveButtonText");
        this.claves.add("FileChooser.folderOpenButtonText");

        // Recargo las claves en el idioma que esté
        for (String clave : claves) {
            UIManager.put(clave, idioma.getString(clave));
        }
        this.updateUI();
    }
    

//    private void IdiomaFileChooser(boolean esCarpeta) {
//        if (this.isFileSelectionEnabled()) {
//            this.setDialogTitle(idioma.getString("FileChooser.Fichero.title"));
//
//        } else {
//            this.setDialogTitle(idioma.getString("FileChooser.Carpeta.title"));
//        }
//        for (String clave : claves) {
//            // Si es carpeta el texto del boton de abrir es específico
//            if (esCarpeta && clave.equals("FileChooser.openButtonText")) {
//                UIManager.put(clave, idioma.getString("FileChooser.folderOpenButtonText"));
//
//            } else {
//                UIManager.put(clave, idioma.getString(clave));
//            }
//        }
//        this.updateUI();
    // Para sacar las claves del componente
//        UIDefaults defaults = UIManager.getDefaults();
//        Path filePath = Paths.get("C:\\Users\\Jesus.delBuey\\Documents\\Programacion\\Java", "Claves_JFileChooser.txt");
//        String linea = defaults.size() + " properties\n";
//        System.out.println(linea);
//        try {
//            Files.writeString(filePath, linea, StandardOpenOption.CREATE);
//            for (Enumeration e = defaults.keys(); e.hasMoreElements();) {
//                Object key = e.nextElement();
//                linea = key + " = " + defaults.get(key) + "\n";
//                if( linea.contains("FileChooser.")) {
//                    System.out.println(linea);
//                    Files.writeString(filePath, linea, StandardOpenOption.APPEND);
//                }
//            }
//        } catch (IOException ex) {
//            Exceptions.printStackTrace(ex);
//        }
//    }
}
//"FileChooser.acceptAllFileFilterText"
//         "FileChooser.filesOfTypeText"
