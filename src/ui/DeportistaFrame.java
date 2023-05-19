package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import excepciones.DAOException;
import excepciones.StatementException;
import objetos_dao.ManagerDAOjdbc;
import utilities.ExportCSV;
import utilities.ServerUtilities;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DeportistaFrame extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Frames
	public static NuevoDeporFrame frameNuevoDepor;
	private JPanel contentPane;

	// Componentes
	private JLabel lblPadding;
	private JTable table;
	private TableModel tableModel;
	private String[] columnasDepor = { "Apellido y Nombre", "Pais", "Disciplina", "Editar", "Eliminar" };

	public static boolean modoEditarDepor = false;
	public static int idEditarDepor;
	
	public static String[][] deportistas;
	private static boolean firstEntry = true;

	public DeportistaFrame() throws DAOException {		
		// Configuracion Principal del JDialog
		setTitle("Gestor de Olimpiadas - DEPORTISTAS");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		this.setModal(true);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		FlowLayout flowLayout = (FlowLayout) contentPane.getLayout();
		flowLayout.setVgap(30);
		// Establece el icono de la ventana
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(mainFrame.athleteIconStr));
		setContentPane(contentPane);
		
		// Label Principal
		JLabel lblPrincipal = new JLabel("Deportistas", SwingConstants.CENTER);
		lblPrincipal.setPreferredSize(new Dimension(300, 20));
		lblPrincipal.setHorizontalTextPosition(SwingConstants.CENTER);
		lblPrincipal.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		// Label Padding
		lblPadding = new JLabel("", SwingConstants.CENTER);
		lblPadding.setPreferredSize(new Dimension(600, 15));
		lblPadding.setHorizontalTextPosition(SwingConstants.CENTER);

		// Boton Nuevo Deportista
		JButton btnNuevo = new JButton("+ NUEVO");
		btnNuevo.setPreferredSize(new Dimension(200, 50));
		btnNuevo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					goToNuevoDeportista();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});			

		// Boton Exportar a CSV
		JButton btnExportarCSV = new JButton("EXPORTAR CSV");
		btnExportarCSV.setPreferredSize(new Dimension(200, 50));
		btnExportarCSV.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {				
				boolean exportadoCorrectamente = ExportCSV.exportar(table);
				if(exportadoCorrectamente)
					JOptionPane.showMessageDialog(mainFrame.frameDepor, "Tabla exportada correctamente");
				else
					JOptionPane.showMessageDialog(mainFrame.frameDepor, "Error al exportar la tabla");
			}
		});

		// Boton Volver
		JButton btnVolver = new JButton("VOLVER");
		btnVolver.setPreferredSize(new Dimension(200, 50));
		btnVolver.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					mainFrame.frameDepor.setVisible(false);
					mainFrame.mainFrame.setVisible(true);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		
		contentPane.add(lblPrincipal);				
		contentPane.add(lblPadding);
		contentPane.add(btnNuevo);
		contentPane.add(btnExportarCSV);
		contentPane.add(btnVolver);		

		// Configuracion JTable
		deportistas = ServerUtilities.arrayDeportistas();
		String[][] filas = ServerUtilities.convertirAFilas(deportistas);
		tableModel = new DefaultTableModel(filas, columnasDepor);
		table = new JTable();
		table.setModel(tableModel);
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(60);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.setPreferredScrollableViewportSize(new Dimension(200, 200));
		table.setPreferredSize(new Dimension(600, 300));

		// Configuracion JScroll
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(600, 300));
		contentPane.add(scrollPane);

		// Dispone los botones de Editar y Eliminar en la Tabla
		ButtonColumn buttonColumnEditar = new ButtonColumn(table, 3);
		ButtonColumn buttonColumnEliminar = new ButtonColumn(table, 4);
	}

	// Inicializa los componentes en el Frame
	public void init() {	

		// variable usada para indicarnos si es la primera vez que se abre la ventana.
		if (firstEntry == false) {
			
			String[][] filas = ServerUtilities.convertirAFilas(deportistas);
			DefaultTableModel dm = new DefaultTableModel(filas, columnasDepor);
			table.setModel(dm);
			
			// Dispone los botones de Editar y Eliminar en la Tabla
			ButtonColumn buttonColumnEditar = new ButtonColumn(table, 3);
			ButtonColumn buttonColumnEliminar = new ButtonColumn(table, 4);
			
			dm.fireTableDataChanged();
			table.repaint();

			// ((DefaultTableModel) tableModel).addRow(deportistas[deportistas.length - 1]);
			// ((AbstractTableModel) tableModel).fireTableDataChanged();
			if (modoEditarDepor) {
				modoEditarDepor = false;
			}

		}

		firstEntry = false;
	}
	
	// Va al Frame de Nuevo/Editar Deportista
	public void goToNuevoDeportista() throws Exception {
		mainFrame.frameDepor.setVisible(false);
		if (frameNuevoDepor == null) {
			frameNuevoDepor = new NuevoDeporFrame();
		}

		// Inicializar Frame y Visualizar
		frameNuevoDepor.init();
		frameNuevoDepor.setVisible(true);
	}

	private class ButtonColumn extends AbstractCellEditor
			implements TableCellRenderer, TableCellEditor, ActionListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		JTable table;
		JButton renderButton;
		JButton editButton;
		String text;

		public ButtonColumn(JTable table, int column) {
			super();
			this.table = table;
			renderButton = new JButton();

			editButton = new JButton();
			editButton.setFocusPainted(false);
			editButton.addActionListener((ActionListener) this);

			// Establece El texto del Boton de acuerdo al numero de Columna
			if (column == 3)
				text = "Editar";
			else
				text = "Eliminar";

			TableColumnModel columnModel = table.getColumnModel();
			columnModel.getColumn(column).setCellRenderer((TableCellRenderer) this);
			columnModel.getColumn(column).setCellEditor((TableCellEditor) this);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if (hasFocus) {
				renderButton.setForeground(table.getForeground());
				renderButton.setBackground(UIManager.getColor("Button.background"));
			} else if (isSelected) {
				renderButton.setForeground(table.getSelectionForeground());
				renderButton.setBackground(table.getSelectionBackground());
			} else {
				renderButton.setForeground(table.getForeground());
				renderButton.setBackground(UIManager.getColor("Button.background"));
			}

			renderButton.setText(text);
			return renderButton;
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			editButton.setText(text);
			return editButton;
		}

		public Object getCellEditorValue() {
			return text;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// Funcionalidad de los Botones de la tabla
			fireEditingStopped();

			// Boton de Eliminar
			if (table.getSelectedColumn() == 4) {
				int opcion = JOptionPane.showConfirmDialog(mainFrame.frameDepor, "¿Seguro que quieres eliminar al deportista?",
						"Eliminar Deportista", JOptionPane.OK_CANCEL_OPTION);
				if (opcion == 0) {	
					
					try {
						ManagerDAOjdbc.getDeportistaDAO().eliminar(Integer.valueOf(deportistas[table.getSelectedRow()][0]));
					} 
					catch(StatementException e1) {
						e1.printStackTrace();
					}
					catch(DAOException e2) {
						e2.printStackTrace();
					}
					
					deportistas = ServerUtilities.eliminarFila(deportistas, table.getSelectedRow());
					
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					model.removeRow(table.getSelectedRow());
					model.fireTableDataChanged();
					table.repaint();
				}
			} else { // Boton de Editar
				// TODO
				modoEditarDepor = true;
				idEditarDepor = table.getSelectedRow(); // ID deportista a editar en la tabla
				try {
					goToNuevoDeportista();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}