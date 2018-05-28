/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customers.logic;

/**
 *
 * @author P Sanjeev v
 */
public class PartList {
    private String name;
    private String desc;
    private String install;

    public PartList(String name, String desc, String install) {
        this.name = name;
        this.desc = desc;
        this.install = install;
    }

    public String getInstall() {
        return install;
    }

    public void setInstall(String install) {
        this.install = install;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
