package org.smigo.plants;

import java.util.ArrayList;
import java.util.List;

public class UploadBean {
    private List<PlantDataBean> plants = new ArrayList<>();

    public List<PlantDataBean> getPlants() {
        return plants;
    }

    public void setPlants(List<PlantDataBean> plants) {
        this.plants = plants;
    }
}
