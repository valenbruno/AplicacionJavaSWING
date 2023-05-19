package ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import datos_tokio2021.Pais;
import objetos_dao.ManagerDAOjdbc;
import utilities.ErrorKeys;
import utilities.ServerUtilities;
import utilities.Utilities;

public class NuevoPaisFrame extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Frames
	private JPanel contentPane;

	// Componentes
	private JLabel lblError;
	private JTextField textFieldNombrePais;

	private String nombreStr;

	public NuevoPaisFrame() {

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

		// Label Nombre
		JLabel lblNombre = new JLabel("NOMBRE:");
		lblNombre.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lblNombre);

		// TextField Nombre del Pais
		textFieldNombrePais = new JTextField();
		textFieldNombrePais.setColumns(30);
		lblNombre.setLabelFor(textFieldNombrePais);
		contentPane.add(textFieldNombrePais);

		// Boton Guardar
		JButton btnGuardar = new JButton("GUARDAR");
		btnGuardar.setPreferredSize(new Dimension(150, 50));
		btnGuardar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!textFieldNombrePais.getText().equals("")) {
					try {
						nombreStr = textFieldNombrePais.getText().trim();

						// Guarda en numError el numero que retorna la funcion que comprueba el
						// textField
						int numError = ServerUtilities.checkNuevoPais(nombreStr);

						// Comprueba el numero de Error e informa el error en el caso contrario
						if (numError == 0) {
							nombreStr = Utilities.toUpperCaseEachWord(nombreStr);
							if(!PaisFrame.modoEditarPais) {
								Pais p = new Pais(nombreStr);
								ManagerDAOjdbc.getPaisDAO().guardar(p);
					
								PaisFrame.paises = ServerUtilities.agregarFilaPais(PaisFrame.paises, p);
							}
							else {
								Pais p = ManagerDAOjdbc.getPaisDAO().encontrar(PaisFrame.paisAEditar);
								p.setNombre(nombreStr);
								ManagerDAOjdbc.getPaisDAO().actualizar(p);	
								
								PaisFrame.paises[PaisFrame.IDPaisAEditar][1] = nombreStr;
								
							}
							goToPais();
						} else {
							lblError.setText(ErrorKeys.mapKeys.get(numError));
						}

					} catch (Exception e2) {
						e2.printStackTrace();
					}
				} else {
					lblError.setText(ErrorKeys.mapKeys.get(5));
				}
			}
		});
		
		// Boton Volver
		JButton btnVolver = new JButton("VOLVER");
		btnVolver.setPreferredSize(new Dimension(150, 50));
		btnVolver.setBounds(455, 100, 125, 50);
		btnVolver.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					goToPais();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});

		contentPane.add(btnGuardar);		
		contentPane.add(btnVolver);
	}
	
	private void goToPais() throws Exception {
		PaisFrame.modoEditarPais = false;
		PaisFrame.frameNuevoPais.setVisible(false);
		mainFrame.framePais.init();
		mainFrame.framePais.setVisible(true);
	}

	// Inicializa los componentes en el Frame
	public void init() {
		// Se establece el titulo del frame de acuerdo si esta editando o creando un nuevo Pais
		if(PaisFrame.modoEditarPais) {
			setTitle("Gestor de Olimpiadas - EDITAR PAIS");
			textFieldNombrePais.setText(PaisFrame.paisAEditar);			
		}			
		else {
			setTitle("Gestor de Olimpiadas - NUEVO PAIS");
			textFieldNombrePais.setText("");
		}
		
		lblError.setText("");
	}
}
