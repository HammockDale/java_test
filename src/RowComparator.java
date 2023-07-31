import java.util.Comparator;

public class RowComparator implements Comparator<Object> {

        private int fn;

        public RowComparator(int fn) {
            this.fn = fn;
        }

        public static int cc(Object a, Object b) {
            if( a == null&& b ==  null) {
                return 0;
            }else
            if( a == null) {
                return -1;
            }else
            if( b == null) {
                return 1;
            }else if (a instanceof Integer && b instanceof  Integer) {
                Integer A = (Integer) a;
                Integer B = (Integer) b;
                return A.compareTo(B);
            }
            else {
                return a.toString().compareTo(b.toString());
            }
        }


        @Override
        public int compare(Object o1a, Object o2b) {
            Object[] o1 =(Object[])o1a;
            Object[] o2 =(Object[])o2b;

            Object a = null;
            Object b = null;
            if (o1.length > fn) {
                a = o1[fn];
            }
            if (o2.length >fn) {
                b = o2[fn];
            }
            return cc(a,b);
        }
    }
