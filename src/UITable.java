import javax.swing.*;
import javax.swing.table.*;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


//class MyTableModel extends DefaultTableModel {



    //overridden method to add row in the table model
  //  public void addRow(DataHolder rowData) {
  //      insertRow(getRowCount(), rowData);
  //  }

 //   public void insertRow(int row, DataHolder rowData) {
 //       dataVector_.insertElementAt(rowData, row);
 //       fireTableRowsInserted(row, row);
 //   }


 //   Class<?>	getColumnClass(int columnIndex)
 //
 //   int	getColumnCount()
 //   Returns the number of columns in the model.

 //   String	getColumnName(int columnIndex)
  //  Returns the name of the column at columnIndex.
  //  int	getRowCount()
//    Returns the number of rows in the model.
 //   Object	getValueAt(int rowIndex, int columnIndex)
 //   Returns the value for the cell at columnIndex and rowIndex.

//    boolean	isCellEditable(int rowIndex, int columnIndex)
//    Returns true if the cell at rowIndex and columnIndex is editable.

//    void	setValueAt(Object aValue, int rowIndex, int columnIndex)
 //   Sets the value in the cell at columnIndex and rowIndex to aValue.
//}


public class UITable  extends JFrame  {

    Server srv;

    MemoryTable mt = new MemoryTable();

    SimpleModel mmodel = new SimpleModel();



    Client client = new Client();

    {
        ////mt.testFilDataMT();
        client.mt = mt;

        try {
            srv = new Server(9999);
            srv.mt = mt;
            srv.startService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Модель данных таблицы
    private DefaultTableModel tableModel;
//    private JTable table1;
    // Данные для таблиц
//    private Object[][] array = new String[][] {{ "Сахар" , "кг", "1.5" },
//            { "Мука"  , "кг", "4.0" },
//            { "Молоко", "л" , "2.2" }};
//    // Заголовки столбцов
//    private Object[] columnsHeader = new String[] {"Наименование", "Ед.измерения", "Количество"};

    public UITable()
    {
        super("Пример использования TableModel");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Создание стандартной модели
        tableModel = new DefaultTableModel();
//        // Определение столбцов
//        tableModel.setColumnIdentifiers(columnsHeader);
//        // Наполнение модели данными
//        for (int i = 0; i < array.length; i++)
//            tableModel.addRow(array[i]);

//        // Создание таблицы на основании модели данных
//        table1 = new JTable(tableModel);
        // Создание кнопки добавления строки таблицы
//        JButton add = new JButton("Добавить");
//        add.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                // Номер выделенной строки
//                int idx = table1.getSelectedRow();
//                // Вставка новой строки после выделенной
//                tableModel.insertRow(idx + 1, new String[] {
//                        "Товар №" + String.valueOf(table1.getRowCount()),
//                        "кг", "Цена"});
//            }
//        });

//        // Создание кнопки удаления строки таблицы
//        JButton remove = new JButton("Удалить");
//        remove.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                // Номер выделенной строки
//                int idx = table1.getSelectedRow();
//                // Удаление выделенной строки
//                tableModel.removeRow(idx);
//            }
//        });
        // Создание таблицы на основе модели данных
        JTable table2 = new JTable(mmodel);
        // Определение высоты строки
        table2.setRowHeight(24); //TODO


        // Создание кнопки удаления строки таблицы
        JButton remove = new JButton("Удалить");
        remove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Номер выделенной строки
                int idx = table2.getSelectedRow();
                // Удаление выделенной строки

 //               mt.dropRecord( Integer.parseInt(""+table2.getValueAt(idx,0)));
                DropRecRequest dropReq = new DropRecRequest();
                dropReq.id = Integer.parseInt(""+table2.getValueAt(idx,0));

                try {
                    client.call(dropReq);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }

                // Обновление данных
//                mt.reload();
                ReloadRecRequest reloadReq = new ReloadRecRequest();
                try {
                    client.call(reloadReq);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }

                mmodel.fireTableDataChanged();
               ////    tableModel.removeRow(idx);
            }
        });


        JButton buttonAdd = new JButton("Добавить");
        buttonAdd.addActionListener(new ActionListener() {
            private int i = 0;
            public void actionPerformed(ActionEvent e) {
                ++i;

//                mt.addRecord( new Object[]{"id", "akrya " + i,"follya " + 2*i, "mur" + (100 - i)});
                AddRecRequest addReq = new AddRecRequest();
                addReq.row = new Object[]{"id", "akrya " + i,"follya " + 2*i, "mur" + (100 - i)};
                try {
                    client.call(addReq);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }

                // Обновление данных
//                mt.reload();
                ReloadRecRequest reloadReq = new ReloadRecRequest();
                try {
                    client.call(reloadReq);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }

                mmodel.fireTableDataChanged();
//                tableModel.removeRow(idx);
            }
        });

        JButton reloadTable = new JButton("Обновить");
        reloadTable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                ReloadRecRequest reloadReq = new ReloadRecRequest();
                try {
                    client.call(reloadReq);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }

//                mt.reload();
                mmodel.fireTableDataChanged();
            }
        });

        JButton sort1 = new JButton("Сортировать");
        sort1.addActionListener(new ActionListener() {
            private int sortDir = -1;
            public void actionPerformed(ActionEvent e) {
                System.out.println("Sorting");
                mt.sort(2, sortDir);
                sortDir =  - sortDir;
                mmodel.fireTableDataChanged();
            }
        });


        // Формирование интерфейса
        Box contents = new Box(BoxLayout.Y_AXIS);
//        contents.add(new JScrollPane(table1));
        JScrollPane jScrollPane = new JScrollPane(table2);
        contents.add(jScrollPane);
        getContentPane().add(contents);
        System.out.println("jScrollPane " + jScrollPane.getSize());

        JPanel buttons = new JPanel();
        buttons.add(remove);
        buttons.add(buttonAdd);
        buttons.add(reloadTable);
        buttons.add(sort1);


        getContentPane().add(buttons, "South");
        // Вывод окна на экран
        setSize(400, 300);
        setVisible(true);

        // Создание кнопки Сортировать таблицы

    }
    // Модель данных
//    class SimpleModel extends AbstractTableModel
//    {
//        // Количество строк
//        @Override
//        public int getRowCount() {
//            return 10;
//        }
//        // Количество столбцов
//        @Override
//        public int getColumnCount() {
//            return 3;
//        }
//        // Тип хранимых в столбцах данных
//        @Override
//        public Class<?> getColumnClass(int column) {
//            switch (column) {
//                case 1: return Boolean.class;
//                case 2: return Icon.class;
//                default: return Object.class;
//            }
//        }
//        // Функция определения данных ячейки
//        @Override
//        public Object getValueAt(int row, int column)
//        {
//            boolean flag = ( row % 2 == 1 ) ? true : false;
//            // Данные для стобцов
//            switch (column) {
//                case 0: return "" + row;
//                case 1: return new Boolean(flag);
//                case 2: return new ImageIcon("images/copy.png");
//            }
//            return "Не определена";
//        }
//    }

    class SimpleModel extends AbstractTableModel
    {
        // Количество строк
        @Override
        public int getRowCount() {
            return mt.getRowCount();
        }
        // Количество столбцов
        @Override
        public int getColumnCount() {
            return mt.getColumnCount();
        }
        // Тип хранимых в столбцах данных
        @Override
        public Class<?> getColumnClass(int column) {
            switch (column) {
//                case 1: return Boolean.class;
//                case 2: return Icon.class;
                default: return Object.class;
            }
        }
        // Функция определения данных ячейки
        @Override
        public Object getValueAt(int row, int column)
        {

            Object ret = "" ;


//            System.out.println("Server: getRange");
            RowRangeRequest rangeReq = new RowRangeRequest();
            rangeReq.n = row;
            rangeReq.m = row;

            RowRangeResponse resp = (RowRangeResponse) client.call(rangeReq);

            Object[] r = (Object[]) resp.rows[0];


//            Object[] o = mt.getRangeMT(row , row);
//            Object[] r = (Object[])o[0];
//
            ret = r[column];

            if (r != null && column > 0 && column < r.length )
                ret = r[column]+" "+row+" "+column;

            return ret;
        }
    }

    public static void main(String[] args) {
        new UITable();
    }
}
