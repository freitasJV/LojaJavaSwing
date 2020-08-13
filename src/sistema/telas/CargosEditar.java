package sistema.telas;

import java.awt.Font;
import java.awt.event.ActionEvent;
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
import sistema.Navegador;
import sistema.entidades.Cargo;

public class CargosEditar extends JPanel {
    
    Cargo cargoAtual;
    JLabel labelTitulo;
    JLabel labelCargo;
    JTextField campoCargo;
    JButton botaoGravar;

    public CargosEditar(Cargo cargo) {
        this.cargoAtual = cargo;
        criarComponentes();
        criarEventos();
    }

    private void criarComponentes() {
        setLayout(null);
        
        labelTitulo = new JLabel("Edição de Cargo", JLabel.CENTER);
        labelTitulo.setFont(new Font(labelTitulo.getFont().getName(), Font.PLAIN, 20));
        labelCargo = new JLabel("Nome do Cargo", JLabel.LEFT);
        campoCargo = new JTextField(cargoAtual.getNome());
        botaoGravar = new JButton("Salvar");
        
        labelTitulo.setBounds(20, 20, 660, 40);
        labelCargo.setBounds(150, 120, 400, 20);
        campoCargo.setBounds(150, 140, 400, 40);
        botaoGravar.setBounds(250, 380, 200, 40);
        
        add(labelTitulo);
        add(labelCargo);
        add(campoCargo);
        add(botaoGravar);
    }

    private void criarEventos() {
        botaoGravar.addActionListener((ActionEvent ae) -> {
            cargoAtual.setNome(campoCargo.getText());
            sqlAtualzarCargo();
        });
    }
    
    private void sqlAtualzarCargo() {
        Connection conexao;
        Statement instrucao;

        try {
            conexao = DriverManager.getConnection(BancoDeDados.stringDeConexao, BancoDeDados.usuario, BancoDeDados.senha);
            instrucao = conexao.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            instrucao.executeUpdate("UPDATE CARGOS SET NOME='"+campoCargo.getText()+"' WHERE ID="+cargoAtual.getId());
            JOptionPane.showMessageDialog(null, "Cargo atualizado com sucesso!");
            conexao.close();
            Navegador.inicio();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao editar o Cargo");
            Logger.getLogger(CargosInserir.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
