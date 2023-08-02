import javax.swing.*;
import javax.swing.table.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.*;

public class UITable  extends JFrame  implements MyDebug {

    Server srv;

    MemoryTable mt = new MemoryTable();

    SimpleModel mmodel = new SimpleModel();

    Client client = new Client();

    {
        client.mt = mt;
        try {
            srv = new Server(9999);
            srv.mt = mt;
            srv.startService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    JTable table2;

    {
        this.addWindowFocusListener( new WindowFocusListener() {
            private boolean positioned = false;

        @Override
        public void windowGainedFocus(WindowEvent e) {

            if( positioned ) return;
            System.out.println("Window focus gained the first time");

            //// https://groups.google.com/g/comp.lang.java.gui/c/vtzN8Wf2WgY?pli=1
            int middleRow = mmodel.getRowCount()/2;
            table2.scrollRectToVisible(table2.getCellRect(middleRow, middleRow, false));
            table2.setRowSelectionInterval(middleRow, middleRow);

            positioned = true;
        }

        @Override
        public void windowLostFocus(WindowEvent e) {
        }
    }) ;
    }

    // Модель данных таблицы
    private final DefaultTableModel tableModel;

    public UITable()
    {
        super("Пример использования TableModel");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Создание стандартной модели
        tableModel = new DefaultTableModel();
//        // Определение столбцов
//        tableModel.setColumnIdentifiers(columnsHeader);

        // Создание таблицы на основе модели данных
        table2 = new JTable(mmodel);
        // Определение высоты строки
        table2.setRowHeight(24);
        int middleRow = mmodel.getRowCount()/2;
        table2.scrollRectToVisible(table2.getCellRect(middleRow, middleRow, false));
        table2.setRowSelectionInterval(middleRow, middleRow);


        // Создание кнопки удаления строки таблицы
        JButton remove = new JButton("Удалить");
        remove.addActionListener(e -> {
            // Номер выделенной строки
            int idx = table2.getSelectedRow();
            // Удаление выделенной строки

            DropRecRequest dropReq = new DropRecRequest();
            dropReq.id = Integer.parseInt(""+table2.getValueAt(idx,0));

            try {
                client.call(dropReq);
            } catch (Exception exc) {
                exc.printStackTrace();
            }

            // Обновление данных
            ReloadRecRequest reloadReq = new ReloadRecRequest();
            try {
                client.call(reloadReq);
            } catch (Exception exc) {
                exc.printStackTrace();
            }

            mmodel.fireTableDataChanged();
        });


        JButton buttonAdd = new JButton("Добавить");
        buttonAdd.addActionListener(new ActionListener() {
            private int i = 0;
            public void actionPerformed(ActionEvent e) {
                ++i;

                int id = -1;
                AddRecRequest addReq = new AddRecRequest();
                addReq.row = new Object[]{"id", "akrya " + i,"follya " + 2*i, "mur" + (100 - i)};
                try {
                    AddRecResponse addRecResp = (AddRecResponse)client.call(addReq);
                    id = addRecResp.id;
                } catch (Exception exc) {
                    exc.printStackTrace();
                }

                // Обновление данных
                ReloadRecRequest reloadReq = new ReloadRecRequest();
                try {
                    Response reloadResp = client.call(reloadReq);
                    mmodel.fireTableDataChanged();


                    GetRangeRowByIDRequest rowByIDRequest = new GetRangeRowByIDRequest();
                    rowByIDRequest.id = id;

                    // спросить у сервера номер строки с id-шиником addReqResp.id
                    GetRangeRowByIDResponse rowByIdResp = (GetRangeRowByIDResponse) client.call(rowByIDRequest);

                    int row = rowByIdResp.row;
                    System.out.println("row="+row+" id="+id);
                    if(row >= 0) {
                        table2.scrollRectToVisible(table2.getCellRect(row, row, false));
                        table2.setRowSelectionInterval(row, row);
                    }
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
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

                mmodel.fireTableDataChanged();
            }
        });

        JButton sort1 = new JButton("Сорт 1");
        sort1.addActionListener(new ActionListener() {
            private int sortDir = -1;
            public void actionPerformed(ActionEvent e) {
                if (DEBUG > 0) System.out.println("Sorting");
                SortRequest sortReq = new SortRequest();
                sortReq.fn = 1;
                sortReq.dir = sortDir;
                client.call(sortReq);
                sortDir =  - sortDir;
                mmodel.fireTableDataChanged();
            }
        });

        JButton sort2 = new JButton("Сорт 2");
        sort2.addActionListener(new ActionListener() {
            private int sortDir = -1;
            public void actionPerformed(ActionEvent e) {
                if (DEBUG > 0) System.out.println("Sorting");
                SortRequest sortReq = new SortRequest();
                sortReq.fn = 2;
                sortReq.dir = sortDir;
                mt.sort(2, sortDir);
                sortDir =  - sortDir;
                mmodel.fireTableDataChanged();
            }
        });
        JButton sort3 = new JButton("Сорт 3");
        sort3.addActionListener(new ActionListener() {
            private int sortDir = -1;
            public void actionPerformed(ActionEvent e) {
                if (DEBUG > 0) System.out.println("Sorting");
                SortRequest sortReq = new SortRequest();
                sortReq.fn = 3;
                sortReq.dir = sortDir;
                mt.sort(3, sortDir);
                sortDir =  - sortDir;
                mmodel.fireTableDataChanged();
            }
        });

        JButton testBtn = new JButton("Unit est");
        testBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Unittest");
                mmodel.fireTableDataChanged();

                System.out.println("selected row: "+table2.getSelectedRow());
                    //// https://groups.google.com/g/comp.lang.java.gui/c/vtzN8Wf2WgY?pli=1
                int middleRow = mmodel.getRowCount()/2;
                table2.scrollRectToVisible(table2.getCellRect(middleRow, middleRow, false));
                table2.setRowSelectionInterval(middleRow, middleRow);

                System.out.println("selected row: "+table2.getSelectedRow());
            }
        });

        // Формирование интерфейса
        Box contents = new Box(BoxLayout.Y_AXIS);

        contents.add(new JScrollPane(table2));
        getContentPane().add(contents);

        JPanel buttons = new JPanel();
        buttons.add(remove);
        buttons.add(buttonAdd);
        buttons.add(reloadTable);
        buttons.add(sort1);
        buttons.add(sort2);
        buttons.add(sort3);
        buttons.add(testBtn);

        getContentPane().add(buttons, "South");
        // Вывод окна на экран
        setSize(600, 700);
        setVisible(true);
    }

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
            return Object.class;
        }
        // Функция определения данных ячейки
        @Override
        public Object getValueAt(int row, int column)
        {

            Object ret = "" ;

            RowRangeRequest rangeReq = new RowRangeRequest();
            rangeReq.n = row;
            rangeReq.m = row;

            RowRangeResponse resp = (RowRangeResponse) client.call(rangeReq);

            Object[] r = (Object[]) resp.rows[0];

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
