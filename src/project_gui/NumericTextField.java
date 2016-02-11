/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_gui;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author ahmedbarakat
 */
public class NumericTextField extends TextField {

    private int value;
    private int step;
    private int min;
    private int max;

    NumericTextField(int value, int step, int min, int max) {
        super(String.valueOf(value));
        this.value = value;
        this.step = step;
        this.min = min;
        this.max = max;

        this.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.DOWN) {
                    decrement();
                    keyEvent.consume();
                    updateValue();
                }
                if (keyEvent.getCode() == KeyCode.UP) {
                    increment();
                    keyEvent.consume();
                    updateValue();
                }
            }
        });
    }

    public int getValue() {
        return value;
    }

    private void increment() {
        if (value <= max - step) {
            value += step;
        }
    }

    private void decrement() {
        if (value >= min + step) {
            value -= step;
        }
    }

    private void updateValue() {
        this.setText(String.valueOf(value));
    }

    private void checkValue() {
        String text = this.getText();
        System.out.println(text);
        value = Integer.parseInt(text);
        if (value > max) {
            value = max;
            this.setText(String.valueOf(value));
        }
        if (value < min) {
            value = min;
            this.setText(String.valueOf(value));
        }
    }

    @Override
    public void replaceText(int start, int end, String text) {
        if (validate(text)) {
            super.replaceText(start, end, text);
            checkValue();
        }
    }

    @Override
    public void replaceSelection(String text) {
        if (validate(text)) {
            super.replaceSelection(text);
            checkValue();
        }
    }

    private boolean validate(String text) {
        return ("".equals(text) || text.matches("[0-9]"));
    }

}
