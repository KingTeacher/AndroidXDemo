package com.example.module_mvvm.model.bean;

public class CellModel {
    private String title;
    private String value;
    private String hintValue;
    private boolean isShowIcon;
    private boolean isShowError;
    private String tips;

    public CellModel(String title, String value, boolean isShowIcon, boolean isShowError, String tips) {
        this.title = title;
        this.value = value;
        this.hintValue = value;
        this.isShowIcon = isShowIcon;
        this.isShowError = isShowError;
        this.tips = tips;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isShowIcon() {
        return isShowIcon;
    }

    public void setShowIcon(boolean showIcon) {
        isShowIcon = showIcon;
    }

    public boolean isShowError() {
        return isShowError;
    }

    public void setShowError(boolean showError) {
        isShowError = showError;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getHintValue() {
        return hintValue;
    }

    public void setHintValue(String hintValue) {
        this.hintValue = hintValue;
    }
}
