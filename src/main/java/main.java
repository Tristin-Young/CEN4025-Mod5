import entity.ItemsEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

public class main {

    //function to get user choice
    public static int getChoice(Scanner scanner) {
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    //function to display menu
    public static void displayMenu() {

        System.out.println("Press 1 to create a new item");
        System.out.println("Press 2 to delete an item");
        System.out.println("Press 3 to view all items");
        System.out.println("Press 4 to mark an item as done");
        System.out.println("Press 5 to exit\n");
    }

    //function to get next index (auto generated ID was not working on DB for some reason
    public static int getNextIdx(ToDoFunction toDoFunction, EntityManager em) {
        //create index variable
        int nextIdx = 0;
        //get list of items
        List<ItemsEntity> list = toDoFunction.returnAllItems(em);
        //loop through list
        for (ItemsEntity item : list) {
            if (item.getItemId() > nextIdx) {
                //set next index to the highest index in the list
                nextIdx = item.getItemId();
            }
        }
        //return next index + 1
        return nextIdx + 1;
    }

    //function to add item to DB
    public static void addItem(Scanner scanner, EntityManager entityManager, EntityTransaction transaction, ToDoFunction toDoFunction) {

        //query user input
        System.out.println("Enter item name: ");
        String itemName = scanner.nextLine();

        System.out.println("Enter item description: ");
        String itemDescription = scanner.nextLine();

        //create new item
        ItemsEntity item = new ItemsEntity();
        //set item properties
        item.setItemName(itemName);
        item.setItemDescription(itemDescription);
        item.setIsDone((byte) 0);
        item.setItemId(getNextIdx(toDoFunction, entityManager));

        try {
            transaction.begin();
            // Persist the object in the database
            entityManager.persist(item);
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

    //function to delete item from DB
    public static void deleteItem(EntityManager entityManager, EntityTransaction transaction, ToDoFunction toDoFunction, Scanner scanner) {

        //query user input
        toDoFunction.viewAllItems(entityManager);
        System.out.println("Enter item index to be deleted: ");
        int index = scanner.nextInt();

        //check if index is valid
        if (index >= 0 && index < toDoFunction.numOfItems(entityManager)) {
            try {
                transaction.begin();
                // Fetch items from the database
                List<ItemsEntity> itemList = entityManager.createNativeQuery("SELECT  * FROM items", ItemsEntity.class).getResultList();
                //select item to delete
                ItemsEntity itemToDelete = itemList.get(index);
                //delete item
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

    //function to mark item as done
    public static void updateIsDone(EntityManager entityManager, EntityTransaction transaction, ToDoFunction toDoFunction, Scanner scanner) {

        toDoFunction.viewAllItems(entityManager);
        //query user input
        System.out.println("Enter item index to be marked as done: ");
        int index = scanner.nextInt();
        //check if index is valid
        if (index >= 0 && index < toDoFunction.returnAllItems(entityManager).size()) {
            try {
                transaction.begin();
                //get item to mark
                ItemsEntity itemToMark = toDoFunction.returnAllItems(entityManager).get(index);
                //mark item as done
                itemToMark.setIsDone((byte) 1);
                //update item
                entityManager.persist(itemToMark);
                transaction.commit();
                System.out.println("Item marked as done successfully!\n");
                return;
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                e.printStackTrace();
                System.out.println("Failed to mark item as done.\n");
                return;
            }
        } else {
            System.out.println("Invalid index!");
        }

    }

    public static void main(String[] args) {

        //create new instance of ToDoFunction
        ToDoFunction toDoFunction = new ToDoFunction();
        //create variables
        boolean isExit = false;
        int choice;
        //create entity manager and transaction
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        //create scanner
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to your To-Do List!\n");

        //loop through menu while user has not chosen to exit
        while (!isExit) {
            //show menu and get user choice
            displayMenu();
            choice = getChoice(scanner);

            //depending on the users choice, call the appropriate function
            switch (choice) {

                case 1:
                    //create item
                    addItem(scanner, entityManager, transaction, toDoFunction);
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
                    //exit
                    isExit = true;
                    break;
            }
        }

        //close scanner and entity manager
        scanner.close();
        entityManager.close();
        entityManagerFactory.close();

    }
}