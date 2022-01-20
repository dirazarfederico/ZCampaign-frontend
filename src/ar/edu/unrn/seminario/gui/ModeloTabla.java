package ar.edu.unrn.seminario.gui;

public class ModeloTabla extends javax.swing.table.DefaultTableModel {
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	
	public ModeloTabla() {
		super();
	}
	
	public ModeloTabla(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }
	
}
