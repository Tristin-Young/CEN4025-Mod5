import entity.ItemsEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {

    public static int getChoice(Scanner scanner) {
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    public static void displayMenu() {

        System.out.println("Press 1 to create a new item");
        System.out.println("Press 2 to delete an item");
        System.out.println("Press 3 to view all items");
        System.out.println("Press 4 to mark an item as done");
        System.out.println("Press 5 to exit\n");
    }

    public static int getNextIdx(ToDoFunction toDoFunction, EntityManager em) {
        int nextIdx = 0;
        List<ItemsEntity> list = toDoFunction.returnAllItems(em);
        for (ItemsEntity item : list) {
            if (item.getItemId() > nextIdx) {
                nextIdx = item.getItemId();
            }
        }
        return nextIdx + 1;
    }

    public static void addItem(Scanner scanner,EntityManager entityManager, EntityTransaction transaction, ToDoFunction toDoFunction){

        System.out.println("Enter item name: ");
        String itemName = scanner.nextLine();

        System.out.println("Enter item description: ");
        String itemDescription = scanner.nextLine();

        ItemsEntity item = new ItemsEntity();
        item.setItemName(itemName);
        item.setItemDescription(itemDescription);
        item.setIsDone((byte) 0);
        item.setItemId(getNextIdx(toDoFunction, entityManager));

        try {
            transaction.begin();
            entityManager.persist(item); // Don't explicitly set the ID
            transaction.commit();
            System.out.println("Item created successfully!\n");
            return;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();

        }
    }

    public static void deleteItem(EntityManager entityManager, EntityTransaction transaction, ToDoFunction toDoFunction, Scanner scanner){

        System.out.println("Enter item index to be deleted: ");
        int index = scanner.nextInt();

            if (index >= 0 && index < toDoFunction.numOfItems(entityManager)){
                try {
                    transaction.begin();
                    // Fetch items from the database
                    toDoFunction.viewAllItems(entityManager);


                    List<ItemsEntity> itemList = entityManager.createNativeQuery("SELECT  * FROM items", ItemsEntity.class).getResultList();
                    ItemsEntity itemToDelete = itemList.get(index);
                    entityManager.remove(itemToDelete);
                    transaction.commit();
                    System.out.println("Item deleted successfully!\n");
                    return;


                } catch (Exception e) {
                    if (transaction.isActive()) {
                        System.out.println("Invalid index!\n");
                        transaction.rollback();
                        e.printStackTrace();
                    }

                }


            }

        System.out.println("Failed to delete item.\n");
        return;
    }

    public static void updateIsDone(EntityManager entityManager, EntityTransaction transaction, ToDoFunction toDoFunction, Scanner scanner){
        //mark item as done
        try {
            transaction.begin();
            toDoFunction.viewAllItems(entityManager);

            System.out.println("Enter item index to be marked as done: ");
            int index = scanner.nextInt();

            if(index >= 0 && index < toDoFunction.returnAllItems(entityManager).size()){
                ItemsEntity itemToMark = toDoFunction.returnAllItems(entityManager).get(index);
                itemToMark.setIsDone((byte) 1);
                entityManager.persist(itemToMark);
                transaction.commit();
                System.out.println("Item marked as done successfully!\n");
            } else {
                System.out.println("Invalid index!");
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.out.println("Failed to mark item as done.\n");
        }
    }

    public static void main(String[] args) {

        ToDoFunction toDoFunction = new ToDoFunction();
        boolean isExit = false;
        int choice;

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to your To-Do List!\n");

        while (!isExit) {
            displayMenu();
            choice = getChoice(scanner);

            switch (choice) {

                case 1:
                    //create item
                    addItem(scanner,entityManager,transaction, toDoFunction);
                    break;

                case 2:
                    //delete item
                    deleteItem(entityManager, transaction, toDoFunction, scanner);
                    break;

                case 3:
                    //view all items
                    toDoFunction.viewAllItems(entityManager);
                    break;

                case 4:
                    //mark item as done
                    updateIsDone(entityManager, transaction, toDoFunction, scanner);
                    break;

                case 5:
                    isExit = true;
                    break;
            }
        }

        scanner.close();
        entityManager.close();
        entityManagerFactory.close();

    }
}