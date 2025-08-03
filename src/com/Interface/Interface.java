package com.Interface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Interface extends JFrame{
	final  JPanel panelBotones;
	final JButton botonElegirCarpeta, themeButton;
	final JComboBox<String> themeSelector;
	final String[] temas = { "Claro", "Oscuro", "Retro" };
	private static final Map<String, String> iconosEmoji = new HashMap<>();
	static {
	    iconosEmoji.put("Claro", "☀️");
	    iconosEmoji.put("Oscuro", "🌙");
	    iconosEmoji.put("Retro", "🕹️");
	}
		
	public Interface(){
		// Panel con scroll para los botones
        panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        JScrollPane scrollPane = new JScrollPane(panelBotones); // Equivalente a un margin left de 20px
       
        // Panel Para los botones de Theme y Añadir
        JLayeredPane layeredPanel = getLayeredPane();      
        
        // Botón para elegir exe
        botonElegirCarpeta = new JButton("+");
        botonElegirCarpeta.addActionListener(e -> seleccionarExe());
        botonElegirCarpeta.setBackground(Color.WHITE);
        botonElegirCarpeta.setBounds(1110,5,50,50);
        
        themeButton = new JButton("Theme");
        themeButton.addActionListener(this::ChangeTheme);
        themeButton.setBackground(Color.WHITE);
        themeButton.setBounds(10,10,80,30);
        
        // DropDown Button para los Temas
        themeSelector = new JComboBox<>(temas);
        themeSelector.setBounds(10, 10, 120, 30);
        add(themeSelector);

        // Estilo del ComboBox con emojis
        themeSelector.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                String tema = value.toString();
                String emoji = iconosEmoji.getOrDefault(tema, "🎨");
                label.setText(emoji + " " + tema);
                return label;
            }
        });

        // Listener para cambio de tema
        themeSelector.addActionListener(e -> {
            String seleccionado = (String) themeSelector.getSelectedItem();
            cambiarTema(seleccionado);
            guardarTema(seleccionado);
        });

        // Cargar tema guardado al iniciar
        String temaGuardado = cargarTema();
        themeSelector.setSelectedItem(temaGuardado);
        cambiarTema(temaGuardado);

        // Añadir botones (LO PODRIA HABER PUESTO DEBAJO DE CADA BOTON :3, PERO ME DA PEREZA)
        setLayout(new BorderLayout());
        layeredPanel.add(botonElegirCarpeta, JLayeredPane.PALETTE_LAYER);
        layeredPanel.add(themeSelector, JLayeredPane.PALETTE_LAYER);
        //layeredPanel.add(themeButton, JLayeredPane.PALETTE_LAYER);
        add(scrollPane, BorderLayout.CENTER);
        //add(barraSuperior);
        setVisible(true);
	}
	
	private void cambiarTema(String tema) {
        Color fondo, texto, borde;

        switch (tema) {
            case "Oscuro":
                fondo = Color.DARK_GRAY;
                texto = Color.WHITE;
                borde = Color.WHITE;
                break;
            case "Retro":
                fondo = new Color(34, 17, 51);
                texto = new Color(255, 255, 102);
                borde = new Color(255, 204, 0);
                break;
            default:
                fondo = Color.WHITE;
                texto = Color.BLACK;
                borde = Color.BLACK;
                break;
        }

        getContentPane().setBackground(fondo);
        panelBotones.setBackground(fondo);
        panelBotones.setForeground(texto);

        themeSelector.setBackground(fondo);
        themeSelector.setForeground(texto);
        themeSelector.setBorder(BorderFactory.createLineBorder(borde, 1, true));

        // Colorear los botones dentro de panelBotones
        for (Component comp : panelBotones.getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                btn.setBackground(fondo);
                btn.setForeground(texto);
                btn.setBorder(BorderFactory.createLineBorder(borde, 1, true));
            }
        }
    }
	
	 private void guardarTema(String tema) {
	        try {
	            Properties props = new Properties();
	            props.setProperty("tema", tema);
	            FileOutputStream out = new FileOutputStream("config.properties");
	            props.store(out, "Configuración de tema");
	            out.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    private String cargarTema() {
	        Properties props = new Properties();
	        try {
	            FileInputStream in = new FileInputStream("config.properties");
	            props.load(in);
	            in.close();
	            return props.getProperty("tema", "Claro");
	        } catch (IOException e) {
	            return "Claro";
	        }
	    }

	public void ChangeTheme(ActionEvent e) {
		Color getBackground = getContentPane().getBackground();		
        if(getBackground == Color.WHITE){
            getContentPane().setBackground(Color.DARK_GRAY);
            themeButton.setBackground(Color.DARK_GRAY);
            themeButton.setForeground(Color.WHITE);
            themeButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            
            botonElegirCarpeta.setBackground(Color.DARK_GRAY);
            botonElegirCarpeta.setForeground(Color.WHITE);
            botonElegirCarpeta.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            
            panelBotones.setBackground(Color.DARK_GRAY);
            panelBotones.setForeground(Color.WHITE);
            
        } else {
        	getContentPane().setBackground(Color.WHITE);
            themeButton.setBackground(Color.WHITE);
            themeButton.setForeground(Color.BLACK);
            themeButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            
            botonElegirCarpeta.setBackground(Color.WHITE);
            botonElegirCarpeta.setForeground(Color.DARK_GRAY);
            botonElegirCarpeta.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            
            panelBotones.setBackground(Color.WHITE);
            panelBotones.setForeground(Color.BLACK);
        }
	}

	 private void seleccionarExe() {
	        JFileChooser fileChooser = new JFileChooser();
	        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	        fileChooser.setMultiSelectionEnabled(true); // Seleccionar varios .exe
	        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
	        	@Override
	        	public boolean accept(File f) {
	        		return f.isDirectory() || f.getName().toLowerCase().endsWith(".exe");			
 	        	}
	        	
	        	@Override
	        	public String getDescription() {
	        		return "Archivos ejecutables (*.exe)";
	        	}
	        });
	        
	        int resultado = fileChooser.showOpenDialog(this);
	        if (resultado == JFileChooser.APPROVE_OPTION) {
	        	File[] archivosSeleccionados = fileChooser.getSelectedFiles();
	        	if (archivosSeleccionados.length == 0) {
	        		archivosSeleccionados = new File[] {fileChooser.getSelectedFile()};
	        	}
	        	mostrarExeSeleccionados(archivosSeleccionados);
	        }
	    }

	 
	 private void mostrarExeSeleccionados(File[] archivosExe){  
		 for (File archivo : archivosExe) {
			 JButton boton = new JButton(archivo.getName());
			 boton.setAlignmentX(Component.LEFT_ALIGNMENT); // Centrar el botón
			 boton.addActionListener(e -> ejecutarExe(archivo));
		     boton.setBackground(Color.WHITE);
		     boton.setForeground(Color.BLACK);
			 panelBotones.add(Box.createVerticalStrut(50), Box.createHorizontalStrut(10)); // Espaciado
			 panelBotones.add(boton);
		 }
        panelBotones.revalidate(); // Refrescar el panel
        panelBotones.repaint();
	 }
	
	private void ejecutarExe(File archivo) {
        try {
            Runtime.getRuntime().exec(archivo.getAbsolutePath()); // Funciona XD
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al ejecutar: " + archivo.getName());
            e.printStackTrace();
        }
    }

}
