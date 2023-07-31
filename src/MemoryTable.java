import java.util.*;

import java.util.Comparator;
public class MemoryTable {
    HashMap<Integer, Object[]> memoryTable = new HashMap<Integer, Object[]>();


    public void addRecord(Integer id, Object[] o){
        if (id == null || o == null) {
            System.out.println("wrong object");
        } else
        memoryTable.put(id, o);
    }

    public void printMt() {
       Object[] keys =  memoryTable.keySet().toArray();
        for (Object key: keys) {
            System.out.print(key);
            Object[] val = memoryTable.get(key);
            for(Object v:val) {
                System.out.print(" " + v);
            }
            System.out.println();
        }
    }


    public static void printMas(Object[] o) {
        for (Object key: o) {
            Object[] rec = (Object[])key;
            for(Object v:rec) {
                System.out.print(" " + v);
            }
            System.out.println();
        }
    }

//-----------------------------------------------------------------------------


    public Object[] getRangeMT(int n, int m){
        Object[] ret = new Object[m + 1 - n];
        Object[] keys =  memoryTable.keySet().toArray();
        for (int i = n; i <= m; ++i) {
            ret[i - n] = memoryTable.get(keys[i]);
        }
        return ret;
    }


    //  прямая сортировка
    public Object[] getAscRangeMT(int n, int m, int fn){
       Object[] obj = getRangeMT(n,m);
       RowComparator rc = new RowComparator(fn);
       Arrays.parallelSort(obj, 0, obj.length, rc);
        //Arrays.sort(obj, rc);
        return obj;
    }

    //  обратная сортировка
    public Object[] getDescRangeMT(int n, int m, int fn){
        Object[] obj = getRangeMT(n,m);
        RowComparator rc = new RowComparator(fn);
        Arrays.parallelSort(obj, 0, obj.length, new Comparator(){
            public int compare(Object o1a, Object o2b){
                return -rc.compare(o1a,o2b);
            }
        });
        //Arrays.sort(obj, rc);
        return obj;
    }

//    public void

    // Количество строк
    public int getRowCount() {
        return this.memoryTable.size();
    }

    // Количество столбцов
    public int getColumnCount() {
        return 3;
    }



// -------------------------------------------------------------------------
    public void testFilDataMT(){
        for (int i = 1; i < 100; i++)
            this.addRecord(i, new Object[]{"bla " + i,"kva " + 2*i, "ewr" + (100 - i)});

    }



    public static void main(String[] args) {
        MemoryTable mt = new MemoryTable();
        mt.testFilDataMT();
        System.out.println("LALA");
        mt.printMt();
//        printMas(mt.getRangeMT(7, 15));
        printMas(mt.getDescRangeMT(0, 4, 2));
    }
}


