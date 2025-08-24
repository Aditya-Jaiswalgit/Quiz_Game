package com.bharatiscript.padaayifruits;

public class Question {
    public int imageResId;
    public String[] options;
    public int correctIndex;

    public Question(int imageResId, String[] options, int correctIndex) {
        this.imageResId = imageResId;
        this.options = options;
        this.correctIndex = correctIndex;
    }
}
