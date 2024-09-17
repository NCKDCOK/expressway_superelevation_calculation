package com.schdri.utils;

import com.schdri.domain.FormData;

public class FormDataManager {
    private static FormDataManager instance;
    private FormData formData;

    private FormDataManager() {
        formData = new FormData();
    }

    public static FormDataManager getInstance() {
        if (instance == null) {
            instance = new FormDataManager();
        }
        return instance;
    }

    public FormData getFormData() {
        return formData;
    }

    public void setFormData(FormData formData) {
        this.formData = formData;
    }
}
