import java.util.*;

import java.util.Comparator;
public class MemoryTable implements MyDebug{
    HashMap<Integer, Object[]> memoryTable = new HashMap<>();

    public MemoryTable() {
        testFilDataMT();

        rowsToReturn = getUnsortedRangeMT(0,getRowCount()-1);
    }

    private int seqNo = 1;

    public int addRecord(Object[] o){
        int ret = -1;
        if (o == null ||o.length < 1) {
            System.out.println("wrong object");
        } else {
            addRecord(seqNo, o);
            ret = seqNo;
            ++seqNo;
        }
        return ret;
    }

    private void addRecord(Integer id, Object[] o){
        if (id == null || o == null) {
            System.out.println("wrong object");
        } else {
            o[0] = id;
            memoryTable.put(id, o);
        }
    }

    public void dropRecord(int id){
        System.out.println("MemoryTable: dropRecord("+id+")");
        memoryTable.remove(id);
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
   private Object[] rowsToReturn = null;

    public Object[] getRangeMT(int n, int m){
        Object[] ret = new Object[m - n + 1];

        if(rowsToReturn == null) rowsToReturn = getUnsortedRangeMT(0,getRowCount()-1);

        System.arraycopy(rowsToReturn, n, ret,0, ret.length);

       return ret;

    }

    public int getRangeRowByID(int id){
        int ret = -1;
        if(id < 0) return -1;

        for(int i=0; i<rowsToReturn.length; ++i){
            Object[] r = (Object[])rowsToReturn[i];
            if(r != null && r.length > 0 && Integer.parseInt(""+r[0]) == id){
                ret = i;
            }
        }
        return ret;
    }

    public void reload(){
        rowsToReturn = null;
        rowsToReturn = getUnsortedRangeMT(0,getRowCount()-1);
        //TODO: восстановливая сортировку как было
        //      при этом НЕ доставать все заново из таблицы
        //      а пересортировать уже имеющийся массив
        sort(sortFn, sortDir);

    }


    private int sortDir = 0;
    private int sortFn = 0;

    public void sort(int fn, int dir){
        sortDir = dir;
        sortFn = fn;

        if(dir < 0)
            rowsToReturn = getAscRangeMT(0,getRowCount()-1,fn);
        else if(dir > 0)
            rowsToReturn = getDescRangeMT(0,getRowCount()-1,fn);
        else
            rowsToReturn = getUnsortedRangeMT(0,getRowCount()-1);
    }

    private Object[] getUnsortedRangeMT(int n, int m){
        Object[] ret = new Object[m + 1 - n];
        Object[] keys =  memoryTable.keySet().toArray();
        for (int i = n; i <= m; ++i) {
            ret[i - n] = memoryTable.get(keys[i]);
        }
        return ret;
    }

    //  прямая сортировка
    private Object[] getAscRangeMT(int n, int m, int fn){
       Object[] obj = getUnsortedRangeMT(n,m);
       RowComparator rc = new RowComparator(fn);
        if (DEBUG > 0) System.out.println("SORTING TABLE ASC");
       Arrays.parallelSort(obj, 0, obj.length, rc);
        return obj;
    }

    //  обратная сортировка
    Object[] getDescRangeMT(int n, int m, int fn){
        Object[] obj = getUnsortedRangeMT(n,m);
        RowComparator rc = new RowComparator(fn);
        if (DEBUG > 0) System.out.println("SORTING TABLE DESC");
        Arrays.parallelSort(obj, 0, obj.length, new Comparator(){
            public int compare(Object o1a, Object o2b){
                return -rc.compare(o1a,o2b);
            }
        });
        rowsToReturn = obj;
        return obj;
    }


    // Количество строк
    public int getRowCount() {
        return this.memoryTable.size();
    }

    // Количество столбцов
    private int colNo = -1;

    public int getColumnCount() {
        if (colNo > 0)
            return colNo;
        colNo = 4;
        if (getRowCount() > 0) {
            Object[] o = memoryTable.get(memoryTable.keySet().toArray()[0]);
            colNo = o.length;
        }
        return colNo;
    }


// -------------------------------------------------------------------------
    public void testFilDataMT(){
        for (int i = 1; i < 1000; i++)
            this.addRecord( new Object[]{"id", "bla " + i,"kva " + 2*i, "ewr" + (100 - i)});

    }
}


