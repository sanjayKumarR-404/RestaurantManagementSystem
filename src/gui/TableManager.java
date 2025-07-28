package gui;

import java.util.ArrayList;

public class TableManager {
    private ArrayList<Table> tables;

    public TableManager(int numberOfTables) {
        tables = new ArrayList<>();
        for (int i = 1; i <= numberOfTables; i++) {
            tables.add(new Table(i));
        }
    }

    public ArrayList<Table> getTables() {
        return tables;
    }

    public Table getTableByNumber(int number) {
        for (Table table : tables) {
            if (table.getTableNumber() == number) {
                return table;
            }
        }
        return null;
    }

    public void occupyTable(int number) {
        Table table = getTableByNumber(number);
        if (table != null && !table.isOccupied()) {
            table.setOccupied(true);
        }
    }

    public void freeTable(int number) {
        Table table = getTableByNumber(number);
        if (table != null && table.isOccupied()) {
            table.setOccupied(false);
        }
    }
}
