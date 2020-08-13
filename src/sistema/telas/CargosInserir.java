package sistema.telas;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import sistema.BancoDeDados;
import sistema.entidades.Cargo;

public class CargosInserir extends JPanel{
    JLabel labelTitulo;
    JLabel labelCargo;
    JTextField campoCargo;
    JButton botaoGravar;
    
    public CargosInserir(){
        criarComponentes();
        criarEventos();
    }

    private void criarComponentes() {
        setLayout(null);
        
        labelTitulo = new JLabel("Cadastro de Cargo", JLabel.CENTER);
        labelTitulo.setFont(new Font(labelTitulo.getFont().getName(), Font.PLAIN, 20));
        labelCargo = new JLabel("Nome do Cargo", JLabel.LEFT);
        campoCargo = new JTextField();
        botaoGravar = new JButton("Adicionar Cargo");
        
        labelTitulo.setBounds(20, 20, 660, 40);
        labelCargo.setBounds(150, 120, 400, 20);
        campoCargo.setBounds(150, 140, 400, 40);
        botaoGravar.setBounds(250, 380, 200, 40);
        
        add(labelTitulo);
        add(labelCargo);
        add(campoCargo);
        add(botaoGravar);
        
        setVisible(true);
    }

    private void criarEventos() {
        botaoGravar.addActionListener((ActionEvent ae) -> {
            Cargo novoCargo = new Cargo();
            novoCargo.setNome(campoCargo.getText());
            
            sqlInserirCargo(novoCargo);
        });
    }
    
    private void sqlInserirCargo(Cargo NovoCargo){
        if(campoCargo.getText().length() <= 3){
            JOptionPane.showMessageDialog(null, "Por favor, preencha o nome corretamente.");
            return;
        }
        
        Connection conexao;
        Statement instrucao;
        ResultSet resultados;
        
        try {
            conexao = DriverManager.getConnection(BancoDeDados.stringDeConexao, BancoDeDados.usuario, BancoDeDados.senha);
            instrucao = conexao.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            resultados = instrucao.executeQuery("SELECT * FROM cargos WHERE nome LIKE '%" + NovoCargo.getNome() + "%'");

            if (resultados.next()) {
                throw new Exception("Já existe um cargo com essa descrição");
            }
            
            instrucao.executeUpdate("INSERT INTO cargos (nome) VALUES ('"+NovoCargo.getNome()+"')");
            
            JOptionPane.showMessageDialog(null,"Cargo adicionado com sucesso");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Ocorreu um erro ao adicionar o Cargo");
            Logger.getLogger(CargosInserir.class.getName()).log(Level.SEVERE,null,e);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,ex.getMessage());
            Logger.getLogger(CargosInserir.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
