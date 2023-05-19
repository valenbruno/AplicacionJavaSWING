package ui;

import java.awt.*;

import javax.swing.*;

import connector.MyConnector;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class mainFrame extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Frames
	private JPanel contentPane;
	public static mainFrame mainFrame;
	public static DeportistaFrame frameDepor;
	public static PaisFrame framePais;

	// Componentes
	private JTextField username = new JTextField();
	private JTextField password = new JPasswordField();
	private Object[] mensajeLogin = { "USUARIO BD:", username, "PASSWORD BD:", password };
	private boolean isConnected = false;

	// String Paths
	private String configIconStr;
	public static String athleteIconStr;
	public static String tokyoIconStr;
	public static String countryIconStr;
	public static String disciplineIconStr;

	public static void main(String[] args) {
		try {
			if (mainFrame == null) {
				mainFrame = new mainFrame();
			}
			mainFrame.setVisible(true);
		} catch (Exception e) {
			System.err.println("¡Error al Crear el Main Frame!");
			e.printStackTrace();
		}
	}

	public mainFrame() {

		// Configuracion Principal del JDialog
		setTitle("Gestor de Olimpiadas");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		this.setModal(true);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 50)); // Margenes
		String directorioPrincipal = System.getProperty("user.dir");
		tokyoIconStr = directorioPrincipal + "\\Entregable_3\\Images\\tokyoOlympicsIcon.png";
		// Establece el icono de la ventana
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(tokyoIconStr));

		JButton btnSinDef1 = new JButton("SIN DEFINIR");
		btnSinDef1.setPreferredSize(new Dimension(200, 60));

		JButton btnSinDef2 = new JButton("SIN DEFINIR");
		btnSinDef2.setPreferredSize(new Dimension(200, 60));

		JButton btnSinDef3 = new JButton("SIN DEFINIR");
		btnSinDef3.setPreferredSize(new Dimension(200, 60));

		JButton btnSinDef4 = new JButton("SIN DEFINIR");
		btnSinDef4.setPreferredSize(new Dimension(200, 60));

		JButton btnSinDef5 = new JButton("SIN DEFINIR");
		btnSinDef5.setPreferredSize(new Dimension(200, 60));

		JButton btnSinDef6 = new JButton("SIN DEFINIR");
		btnSinDef6.setPreferredSize(new Dimension(200, 60));

		// Boton Deportista
		athleteIconStr = directorioPrincipal + "\\Entregable_3\\Images\\athleteIcon.png";
		ImageIcon athleteIcon = new ImageIcon(athleteIconStr);
		Image athleteIconEscaled = athleteIcon.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
		athleteIcon.setImage(athleteIconEscaled);
		JButton btnDeportista = new JButton("DEPORTISTA", athleteIcon);
		btnDeportista.setPreferredSize(new Dimension(200, 60));
		btnDeportista.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnDeportista.setHorizontalTextPosition(SwingConstants.CENTER);
		btnDeportista.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (isConnected) {
					try {
						
						mainFrame.this.setVisible(false);
						
						if (frameDepor == null) {
							frameDepor = new DeportistaFrame();
						}

						// Inicializar Frame y Visualizar
						frameDepor.init();
						frameDepor.setVisible(true);

					} catch (Exception e2) {
						System.err.println("¡Error al cambiar de Frame!");
						e2.printStackTrace();
					}
				}
			}
		});

		// Boton Pais
		countryIconStr = directorioPrincipal + "\\Entregable_3\\Images\\countryIcon.png";
		ImageIcon countryIcon = new ImageIcon(countryIconStr);
		Image countryIconEscaled = countryIcon.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
		countryIcon.setImage(countryIconEscaled);
		JButton btnPais = new JButton("PAIS", countryIcon);
		btnPais.setPreferredSize(new Dimension(200, 60));
		btnPais.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnPais.setHorizontalTextPosition(SwingConstants.CENTER);
		btnPais.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(isConnected) {					
					try {
						
						mainFrame.this.setVisible(false);
						
						if (framePais == null) {
							framePais = new PaisFrame();
						}

						// Inicializar Frame y Visualizar
						framePais.init();
						framePais.setVisible(true);

					} catch (Exception e2) {
						System.err.println("¡Error al cambiar de Frame!");
						e2.printStackTrace();
					}	
				}				
			}
		});

		// Boton Disciplina
		disciplineIconStr = directorioPrincipal + "\\Entregable_3\\Images\\disciplineIcon.png";
		ImageIcon disciplineIcon = new ImageIcon(disciplineIconStr);
		Image disciplineIconEscaled = disciplineIcon.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
		disciplineIcon.setImage(disciplineIconEscaled);
		JButton btnDisciplina = new JButton("DISCIPLINA", disciplineIcon);
		btnDisciplina.setPreferredSize(new Dimension(200, 60));
		btnDisciplina.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnDisciplina.setHorizontalTextPosition(SwingConstants.CENTER);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 30));

		// Label Padding
		JLabel lblPadding = new JLabel("");
		lblPadding.setPreferredSize(new Dimension(45, 100));

		
		// Label Principal
		ImageIcon mainIcon = new ImageIcon(tokyoIconStr);
		Image mainIconEscaled = mainIcon.getImage().getScaledInstance(90, 90, Image.SCALE_DEFAULT);
		mainIcon.setImage(mainIconEscaled);
		JLabel lblPrincipal = new JLabel(mainIcon);
		lblPrincipal.setPreferredSize(new Dimension(500, 100));

		// Boton Configuracion
		configIconStr = directorioPrincipal + "\\Entregable_3\\Images\\configIcon.png";
		ImageIcon configIcon = new ImageIcon(configIconStr);
		Image configIconEscaled = configIcon.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
		configIcon.setImage(configIconEscaled);
		JButton btnConfig = new JButton("", configIcon);
		btnConfig.setPreferredSize(new Dimension(35, 35));
		btnConfig.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int opcion = JOptionPane.showConfirmDialog(mainFrame, mensajeLogin,
						"Gestor de Olimpiadas - CONFIGURACION", JOptionPane.OK_CANCEL_OPTION);

				// Si se selecciona "OK"
				if (opcion == 0) {
					if(MyConnector.setConnection(username.getText(), password.getText()) == null) {
						System.err.println("Error al conectar con la Base de Datos");
						JOptionPane.showMessageDialog(mainFrame, "Error en la conexion con la BD");
					}
					else {
						JOptionPane.showMessageDialog(mainFrame, "Se ha conectado a la BD");
						isConnected = true;
						btnDeportista.setEnabled(true);
						btnPais.setEnabled(true);
						btnDisciplina.setEnabled(true);
					}					
				}
			}
		});
		
		if(!isConnected) {
			btnDeportista.setEnabled(false);
			btnPais.setEnabled(false);
			btnDisciplina.setEnabled(false);
		}

		contentPane.add(lblPadding);
		contentPane.add(lblPrincipal);
		contentPane.add(btnConfig);
		contentPane.add(btnDeportista);
		contentPane.add(btnPais);
		contentPane.add(btnDisciplina);
		contentPane.add(btnSinDef1);
		contentPane.add(btnSinDef2);
		contentPane.add(btnSinDef3);
		contentPane.add(btnSinDef4);
		contentPane.add(btnSinDef5);
		contentPane.add(btnSinDef6);
	}
}
