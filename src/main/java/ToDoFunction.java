//import libraries
import entity.ItemsEntity;

import javax.persistence.EntityManager;
import java.util.List;

//create class
public class ToDoFunction {



    public void printItems(List<ItemsEntity> itemsList) {
        if (itemsList.isEmpty()) {
            System.out.println("No items found in the database.\n");
        } else {
            System.out.println("Items in the database:");
            for (int i = 0; i < itemsList.size(); i++) {
                ItemsEntity item = itemsList.get(i);
                System.out.println(i + " : " + item.getItemName());
                System.out.println("Description: " + item.getItemDescription());
                System.out.println("Status: " + (item.getIsDone() == 1 ? "Done" : "Not Done"));
                System.out.println("---------------------");
            }
        }
    }

    public void viewAllItems(EntityManager entityManager) {
        try {
            List<ItemsEntity> itemList = entityManager.createNativeQuery("SELECT  * FROM items", ItemsEntity.class).getResultList();
            printItems(itemList);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to fetch items.");
        }
    }

    public List<ItemsEntity> returnAllItems(EntityManager entityManager) {
        try {
            List<ItemsEntity> itemList = entityManager.createNativeQuery("SELECT  * FROM items", ItemsEntity.class).getResultList();
            return itemList;
        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("Failed to fetch items.");
        }
        return null;
    }

    public int numOfItems(EntityManager em){
        try {
            List<ItemsEntity> itemList = em.createNativeQuery("SELECT  * FROM items", ItemsEntity.class).getResultList();
            return itemList.size();
        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("Failed to fetch items.");
        }
        return 0;
    }

    }