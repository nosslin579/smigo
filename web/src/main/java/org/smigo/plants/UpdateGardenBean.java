package org.smigo.plants;

import java.util.ArrayList;
import java.util.List;

public class UpdateGardenBean {

    List<PlantDataBean> removeList = new ArrayList<PlantDataBean>();
    List<PlantDataBean> addList = new ArrayList<PlantDataBean>();

    public List<PlantDataBean> getRemoveList() {
        return removeList;
    }

    public void setRemoveList(List<PlantDataBean> removeList) {
        this.removeList = removeList;
    }

    public List<PlantDataBean> getAddList() {
        return addList;
    }

    public void setAddList(List<PlantDataBean> addList) {
        this.addList = addList;
    }
}