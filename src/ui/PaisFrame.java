package ui;

import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import datos_tokio2021.Pais;
import excepciones.DAOException;
import excepciones.StatementException;
import objetos_dao.ManagerDAOjdbc;
import utilities.ServerUtilities;

import javax.swing.AbstractCellEditor;

public class PaisFrame extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Frames
	private JPanel contentPane;
	public static NuevoPaisFrame frameNuevoPais;

	// Componentes
	private JTable table;
	private TableModel tableModel;
	private String[] columnasPais = { "id", "Pais", "Editar", "Eliminar" };
	public static String[][] paises;
	
	public static boolean modoEditarPais = false;
	public static String paisAEditar;
	public static int IDPaisAEditar;
	
	private boolean firstEntry = true;

	public PaisFrame(){

		// Configuracion Principal del JDialog
		setTitle("Gestor de Olimpiadas - PAIS");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		this.setModal(true);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		FlowLayout flowLayout = (FlowLayout) contentPane.getLayout();
		flowLayout.setVgap(30);
		setContentPane(contentPane);
		// Establece el icono de la ventana
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(mainFrame.countryIconStr));

		// Label Principal
		JLabel lblPrincipal = new JLabel("Paises", SwingConstants.CENTER);
		lblPrincipal.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPrincipal.setPreferredSize(new Dimension(700, 60));
		contentPane.add(lblPrincipal);

		// Boton Nuevo Pais
		JButton btnNuevo = new JButton("+ NUEVO");
		btnNuevo.setPreferredSize(new Dimension(150, 50));
		btnNuevo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					goToNuevoPais();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});

		// Boton Volver
		JButton btnVolver = new JButton("VOLVER");
		btnVolver.setPreferredSize(new Dimension(150, 50));
		btnVolver.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {				
				try {
					mainFrame.framePais.setVisible(false);
					mainFrame.mainFrame.setVisible(true);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});

		contentPane.add(btnNuevo);
		contentPane.add(btnVolver);

		// Configuracion JTable
		table = new JTable();
		try {
			paises = ServerUtilities.convertirListaPais();
		} catch(StatementException e1) {
			e1.printStackTrace();
		}
		catch(DAOException e2) {
			e2.printStackTrace();
		}
		tableModel = new DefaultTableModel(paises, columnasPais);
		table.setModel(tableModel);
		table.setPreferredScrollableViewportSize(new Dimension(200, 200));
		table.setPreferredSize(new Dimension(500, 300));
		table.getColumnModel().getColumn(0).setPreferredWidth(135);

		// Configuracion JScroll
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(500, 300));
		contentPane.add(scrollPane);

		// Dispone los botones de Editar y Eliminar en la Tabla
		ButtonColumn buttonColumnEditar = new ButtonColumn(table, 2);
		ButtonColumn buttonColumnEliminar = new ButtonColumn(table, 3);
	}

	// Inicializa los componentes en el Frame
	public void init() {
		// Carga la tabla
		if(!firstEntry) {
			DefaultTableModel dm = new DefaultTableModel(paises, columnasPais);
			table.setModel(dm);
			
			// Dispone los botones de Editar y Eliminar en la Tabla
			ButtonColumn buttonColumnEditar = new ButtonColumn(table, 2);
			ButtonColumn buttonColumnEliminar = new ButtonColumn(table, 3);
			
			dm.fireTableDataChanged();
			table.repaint();

		}
		firstEntry = false;
	}
	
	// Va al Frame de Nuevo/Editar Pais
		public void goToNuevoPais() throws Exception {
			mainFrame.framePais.setVisible(false);
			if (frameNuevoPais == null) {
				frameNuevoPais = new NuevoPaisFrame();
			}

			// Inicializar Frame y Visualizar
			frameNuevoPais.init();
			frameNuevoPais.setVisible(true);
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
			if (column == 2)
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
			if (table.getSelectedColumn() == 3) {
				int opcion = JOptionPane.showConfirmDialog(mainFrame.framePais, "¿Seguro que quieres eliminar el Pais?",
						"Eliminar Pais", JOptionPane.OK_CANCEL_OPTION);
				if (opcion == 0) {
					Pais paisAEliminar = null;
					
					boolean enUso;
					try {
						paisAEliminar = ManagerDAOjdbc.getPaisDAO().encontrar((String) table.getValueAt(table.getSelectedRow(), 1));
						enUso = ServerUtilities.validarPaisEnUso(paisAEliminar);
						
						if(!enUso) {
													
							ManagerDAOjdbc.getPaisDAO().eliminar(paisAEliminar);
							
							paises = ServerUtilities.eliminarFila(paises, table.getSelectedRow());
							
							DefaultTableModel model = (DefaultTableModel) table.getModel();
							
							model.removeRow(table.getSelectedRow());
							model.fireTableDataChanged();
							table.repaint();
						}
						else {
							JOptionPane.showConfirmDialog(mainFrame.framePais, "El pais esta siendo usado por algun deportista",
									"Error", JOptionPane.DEFAULT_OPTION);
						}
					} catch(StatementException e1) {
						e1.printStackTrace();
					}
					catch(DAOException e2) {
						e2.printStackTrace();
					}
					
					
					
					
				}
			} else { // Boton de Editar
				// TODO
				modoEditarPais = true;
				paisAEditar = (String) table.getValueAt(table.getSelectedRow(), 1);
				IDPaisAEditar = table.getSelectedRow();
				
				try {
					goToNuevoPais();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
