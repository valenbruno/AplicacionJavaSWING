package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import datos_tokio2021.Deportista;
import datos_tokio2021.Disciplina;
import datos_tokio2021.Pais;
import excepciones.DAOException;
import excepciones.StatementException;
import objetos_dao.ManagerDAOjdbc;
import utilities.ErrorKeys;
import utilities.ServerUtilities;
import utilities.Utilities;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.DefaultComboBoxModel;

public class NuevoDeporFrame extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Frames
	private JPanel contentPane;

	// Componentes
	private JLabel lblError;
	private JTextField textFieldNombreDepor;
	private JTextField textFieldApellidoDepor;
	private JTextField textFieldEmailDepor;
	private JTextField textFieldTelefonoDepor;
	private JComboBox<String> comboBoxPais;
	private JComboBox<String> comboBoxDisciplina;

	// Strings
	private String nombreStr;
	private String apellidoStr;
	private String emailStr;
	private String telefonoStr;

	public NuevoDeporFrame() {

		// Configuracion Principal del JDialog		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		this.setModal(true);
		setBounds(100, 100, 800, 600);

		// Configuracion JPanel
		contentPane = new JPanel();
		contentPane.setBorder(BorderFactory.createEmptyBorder(5, 150, 5, 150)); // Margenes
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 20));
		String directorioPrincipal = System.getProperty("user.dir");
		String newIconStr = directorioPrincipal + "\\Entregable_3\\Images\\newIcon.png";
		// Establece el icono de la ventana
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(newIconStr));

		// Label de Error
		lblError = new JLabel("", SwingConstants.CENTER);
		lblError.setForeground(Color.RED);
		lblError.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblError.setPreferredSize(new Dimension(800, 60));
		contentPane.add(lblError);

		// Boton Guardar
		JButton btnGuardar = new JButton("GUARDAR");
		btnGuardar.setPreferredSize(new Dimension(150, 50));
		btnGuardar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!textFieldNombreDepor.getText().equals("") && !textFieldApellidoDepor.getText().equals("")
						&& !textFieldEmailDepor.getText().equals("") && !textFieldTelefonoDepor.getText().equals("")) {
					try {
						nombreStr = textFieldNombreDepor.getText().trim();
						apellidoStr = textFieldApellidoDepor.getText().trim();
						emailStr = textFieldEmailDepor.getText().trim();
						telefonoStr = textFieldTelefonoDepor.getText().trim();

						// Guarda en numError el numero que retorna la funcion que comprueba los
						// textFields
						int numError = ServerUtilities.checkNuevoDeportista(nombreStr, apellidoStr, emailStr,
								telefonoStr);

						// Comprueba el numero de Error e informa el error en el caso contrario
						if (numError == 0) {
							
							Pais pais = ManagerDAOjdbc.getPaisDAO().encontrar(comboBoxPais.getSelectedItem().toString());
							apellidoStr = Utilities.toUpperCaseEachWord(apellidoStr);
							nombreStr = Utilities.toUpperCaseEachWord(nombreStr);
							Deportista nuevoDeportista = new Deportista(apellidoStr, nombreStr, emailStr, telefonoStr, pais.getId());
							Disciplina dis = ManagerDAOjdbc.getDisciplinaDAO().encontrar(comboBoxDisciplina.getSelectedItem().toString());
							nuevoDeportista.setDisciplina(dis);
							
							// Si se esta creando un Nuevo Deportista
							if(!DeportistaFrame.modoEditarDepor) {
								// Agregar deportista a la base de datos.
								ManagerDAOjdbc.getDeportistaDAO().guardar(nuevoDeportista);
								DeportistaFrame.deportistas = ServerUtilities.agregarFila(DeportistaFrame.deportistas, nuevoDeportista);
							}
							else{ // Si se esta editando un Deportista
								nuevoDeportista.setId(Integer.valueOf(DeportistaFrame.deportistas[DeportistaFrame.idEditarDepor][0]));
								ManagerDAOjdbc.getDeportistaDAO().actualizar(nuevoDeportista);
								DeportistaFrame.deportistas[DeportistaFrame.idEditarDepor][1] = apellidoStr;
								DeportistaFrame.deportistas[DeportistaFrame.idEditarDepor][2] = nombreStr;
								DeportistaFrame.deportistas[DeportistaFrame.idEditarDepor][3] = emailStr;
								DeportistaFrame.deportistas[DeportistaFrame.idEditarDepor][4] = telefonoStr;
								DeportistaFrame.deportistas[DeportistaFrame.idEditarDepor][5] = comboBoxDisciplina.getSelectedItem().toString();
								DeportistaFrame.deportistas[DeportistaFrame.idEditarDepor][6] = comboBoxPais.getSelectedItem().toString();
							}
							
							goToDeportista();
							
						} 
						else
							lblError.setText(ErrorKeys.mapKeys.get(numError));

					} 
					catch (Exception e2) {
						e2.printStackTrace();
					}
				} 
				else {
					lblError.setText(ErrorKeys.mapKeys.get(5));
				}
			}
		});

		// Label Nombre
		JLabel lblNombre = new JLabel("NOMBRE:");
		lblNombre.setPreferredSize(new Dimension(100, 14));
		lblNombre.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lblNombre);

		// TextField Nombre
		textFieldNombreDepor = new JTextField();
		lblNombre.setLabelFor(textFieldNombreDepor);
		textFieldNombreDepor.setColumns(30);
		contentPane.add(textFieldNombreDepor);

		// Label Apellido
		JLabel lblApellido = new JLabel("APELLIDO:");
		lblApellido.setPreferredSize(new Dimension(100, 14));
		lblApellido.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lblApellido);

		// TextField Apellido
		textFieldApellidoDepor = new JTextField();
		lblApellido.setLabelFor(textFieldApellidoDepor);
		textFieldApellidoDepor.setColumns(30);
		contentPane.add(textFieldApellidoDepor);

		// Label E-mail
		JLabel lblEmail = new JLabel("E-MAIL:");
		lblEmail.setPreferredSize(new Dimension(100, 14));
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lblEmail);

		// TextField E-mail
		textFieldEmailDepor = new JTextField();
		lblEmail.setLabelFor(textFieldEmailDepor);
		textFieldEmailDepor.setColumns(30);
		contentPane.add(textFieldEmailDepor);

		// Label Telefono
		JLabel lblTelefono = new JLabel("TELEFONO:");
		lblTelefono.setPreferredSize(new Dimension(100, 14));
		lblTelefono.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lblTelefono);

		// TextField Telefono
		textFieldTelefonoDepor = new JTextField();
		lblTelefono.setLabelFor(textFieldTelefonoDepor);
		textFieldTelefonoDepor.setColumns(30);
		contentPane.add(textFieldTelefonoDepor);

		// Label Pais
		JLabel lblPais = new JLabel("PAIS:");
		lblPais.setPreferredSize(new Dimension(100, 14));
		lblPais.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lblPais);

		// Configuracion JComboBox Pais
		comboBoxPais = new JComboBox<String>();
		lblPais.setLabelFor(comboBoxPais);
		comboBoxPais.setPreferredSize(new Dimension(335, 20));
		comboBoxPais.setMaximumRowCount(190);
		contentPane.add(comboBoxPais);

		// Label Disciplina
		JLabel lblDisciplina = new JLabel("DISCIPLINA:");
		lblDisciplina.setPreferredSize(new Dimension(100, 14));
		lblDisciplina.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lblDisciplina);

		// Configuracion JComboBox Disciplina
		comboBoxDisciplina = new JComboBox<String>();
		lblDisciplina.setLabelFor(comboBoxDisciplina);
		comboBoxDisciplina.setPreferredSize(new Dimension(335, 20));
		comboBoxDisciplina.setMaximumRowCount(190);
		contentPane.add(comboBoxDisciplina);
		
		// Boton Volver
		JButton btnVolver = new JButton("VOLVER");
		btnVolver.setPreferredSize(new Dimension(150, 50));
		btnVolver.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {				
				try {
					goToDeportista();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});

		contentPane.add(btnGuardar);
		contentPane.add(btnVolver);
	}
	
	private void goToDeportista() throws Exception {
		DeportistaFrame.frameNuevoDepor.setVisible(false);
		mainFrame.frameDepor.init();
		mainFrame.frameDepor.setVisible(true);		
	}

	// Inicializa los componentes en el Frame
	public void init() {
		lblError.setText("");	
		String[] paises = null;
		String[] disciplinas = null;
		try {
			paises = ManagerDAOjdbc.getPaisDAO().cargarEnArray();
			disciplinas = ManagerDAOjdbc.getDisciplinaDAO().cargarEnArray(); 
		} catch (DAOException e) {
			e.printStackTrace();
		}	
		catch(StatementException e1) {
			e1.printStackTrace();
		}
		comboBoxPais.setModel(
				new DefaultComboBoxModel<String>(paises));
		comboBoxDisciplina
				.setModel(new DefaultComboBoxModel<String>(disciplinas));
		
		if(DeportistaFrame.modoEditarDepor) {
			setTitle("Gestor de Olimpiadas - EDITAR DEPORTISTA");	
			
			textFieldNombreDepor.setText(DeportistaFrame.deportistas[DeportistaFrame.idEditarDepor][2]);
			textFieldApellidoDepor.setText(DeportistaFrame.deportistas[DeportistaFrame.idEditarDepor][1]);
			textFieldEmailDepor.setText(DeportistaFrame.deportistas[DeportistaFrame.idEditarDepor][3]);
			textFieldTelefonoDepor.setText(DeportistaFrame.deportistas[DeportistaFrame.idEditarDepor][4]);
			comboBoxPais.setSelectedItem(DeportistaFrame.deportistas[DeportistaFrame.idEditarDepor][6]);
			comboBoxDisciplina.setSelectedItem(DeportistaFrame.deportistas[DeportistaFrame.idEditarDepor][5]);
		}
		else {
			setTitle("Gestor de Olimpiadas - NUEVO DEPORTISTA");
			
			textFieldNombreDepor.setText("");
			textFieldApellidoDepor.setText("");
			textFieldEmailDepor.setText("");
			textFieldTelefonoDepor.setText("");
		}
	}
}
