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
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import sistema.BancoDeDados;
import sistema.Navegador;
import sistema.entidades.Cargo;

public class CargosConsultar extends JPanel {

    Cargo cargoAtual;
    JLabel labelTitulo;
    JLabel labelCargo;
    JTextField campoCargo;
    JButton botaoPesquisar;
    JButton botaoEditar;
    JButton botaoExcluir;
    DefaultListModel<Cargo> listasCargosModelo = new DefaultListModel();
    JList<Cargo> listaCargos;

    public CargosConsultar() {
        criarComponentes();
        criarEventos();
    }

    private void criarComponentes() {
        setLayout(null);

        labelTitulo = new JLabel("Consulta de Cargo", JLabel.CENTER);
        labelTitulo.setFont(new Font(labelTitulo.getFont().getName(), Font.PLAIN, 20));
        labelCargo = new JLabel("Nome do Cargo", JLabel.LEFT);
        campoCargo = new JTextField();
        botaoPesquisar = new JButton("Pesquisar Cargo");
        botaoEditar = new JButton("Editar Cargo");
        botaoEditar.setEnabled(false);
        botaoExcluir = new JButton("Excluir Cargo");
        botaoExcluir.setEnabled(false);
        listasCargosModelo = new DefaultListModel();
        listaCargos = new JList();
        listaCargos.setModel(listasCargosModelo);
        listaCargos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        labelTitulo.setBounds(20, 20, 660, 40);
        labelCargo.setBounds(150, 120, 400, 20);
        campoCargo.setBounds(150, 140, 400, 40);
        botaoPesquisar.setBounds(560, 140, 130, 40);
        listaCargos.setBounds(150, 200, 400, 240);
        botaoEditar.setBounds(560, 360, 130, 40);
        botaoExcluir.setBounds(560, 400, 130, 40);

        add(labelTitulo);
        add(labelCargo);
        add(campoCargo);
        add(botaoPesquisar);
        add(listaCargos);
        add(botaoEditar);
        add(botaoExcluir);

        setVisible(true);
    }

    private void criarEventos() {
        botaoPesquisar.addActionListener((ActionEvent ae) -> {
            sqlPesquisarCargos(campoCargo.getText());
        });
        botaoEditar.addActionListener((ActionEvent ae) -> {
            Navegador.cargosEditar(cargoAtual);
        });
        botaoExcluir.addActionListener((ActionEvent ae) -> {
            sqlDeletarCargo();
        });
        listaCargos.addListSelectionListener((ListSelectionEvent e) -> {
            cargoAtual = listaCargos.getSelectedValue();
            if (cargoAtual == null) {
                botaoEditar.setEnabled(false);
                botaoExcluir.setEnabled(false);
            } else {
                botaoEditar.setEnabled(true);
                botaoExcluir.setEnabled(true);
            }
        });
    }

    private void sqlPesquisarCargos(String nome) {
        Connection conexao;
        Statement instrucao;
        ResultSet resultados;

        try {
            conexao = DriverManager.getConnection(BancoDeDados.stringDeConexao, BancoDeDados.usuario, BancoDeDados.senha);
            instrucao = conexao.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultados = instrucao.executeQuery("SELECT * FROM cargos WHERE nome LIKE '%" + nome + "%'");

            listasCargosModelo.clear();

            while (resultados.next()) {
                Cargo cargo = new Cargo();
                cargo.setId(resultados.getInt("id"));
                cargo.setNome(resultados.getString("nome"));

                listasCargosModelo.addElement(cargo);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao consultar o Cargo");
            Logger.getLogger(CargosInserir.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void sqlDeletarCargo() {

        int confirmacao = JOptionPane.showConfirmDialog(null, "Deseja realmente excluir o cargo "
                + cargoAtual.getNome() + "?", "Excluir", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {

            Connection conexao;
            Statement instrucao;

            try {
                conexao = DriverManager.getConnection(BancoDeDados.stringDeConexao, BancoDeDados.usuario, BancoDeDados.senha);
                instrucao = conexao.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                instrucao.executeUpdate("DELETE FROM CARGOS WHERE id=" + cargoAtual.getId() + "");

                JOptionPane.showMessageDialog(null, "Cargo deletado com sucesso!");
                listasCargosModelo.clear();
                Navegador.inicio();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao excluir o Cargo");
                Logger.getLogger(CargosInserir.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
}
