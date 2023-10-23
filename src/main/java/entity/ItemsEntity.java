package entity;

import javax.persistence.*;
//item entity to connect to backend DB
@Entity
@Table(name = "items", schema = "todo")
public class ItemsEntity {
    //creating private variables for item entity
    @Id
    @Column(name = "itemID")
    private int itemId;

    @Column(name = "itemName")
    private String itemName;

    @Column(name = "itemDescription")
    private String itemDescription;

    @Column(name = "isDone")
    private byte isDone;

    //getter and setter methods for item entity
    @Id
    @Column(name = "itemID")
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    @Basic
    @Column(name = "itemName")
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Basic
    @Column(name = "itemDescription")
    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    @Basic
    @Column(name = "isDone")
    public byte getIsDone() {
        return isDone;
    }

    public void setIsDone(byte isDone) {
        this.isDone = isDone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemsEntity that = (ItemsEntity) o;

        if (itemId != that.itemId) return false;
        if (isDone != that.isDone) return false;
        if (itemName != null ? !itemName.equals(that.itemName) : that.itemName != null) return false;
        if (itemDescription != null ? !itemDescription.equals(that.itemDescription) : that.itemDescription != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = itemId;
        result = 31 * result + (itemName != null ? itemName.hashCode() : 0);
        result = 31 * result + (itemDescription != null ? itemDescription.hashCode() : 0);
        result = 31 * result + (int) isDone;
        return result;
    }
}
