/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parts.logic;

import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class EditingCellStr extends TableCell<Part, String> {

        private TextField textField;

        public EditingCellStr() {
        }

        @Override
        public void startEdit() {
            super.startEdit();
            
            if (textField == null) {
                createTextField();
            }

            setText(null);
            setGraphic(textField);
            textField.selectAll();

        }

        @Override
        public void cancelEdit() {
            
            super.cancelEdit();
            setText((String) getItem());
            setGraphic(null);
        }
    
        @Override
        public void updateItem(String item, boolean empty) {

            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);

            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);

                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }

        private void createTextField() {

            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            
            textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent t) {

                    if (t.getCode() == KeyCode.ENTER) {
                        commitEdit(textField.getText());
                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();

        }
        
       
}

    