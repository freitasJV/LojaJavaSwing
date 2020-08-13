package sistema.telas;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
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
import sistema.entidades.Funcionario;

public class FuncionariosConsultar extends JPanel {

    Funcionario funcionarioAtual;
    JLabel labelTitulo;
    JLabel labelFuncionario;
    JTextField campoFuncionario;
    JButton botaoPesquisar;
    JButton botaoEditar;
    JButton botaoExcluir;
    DefaultListModel<Funcionario> listaFuncionarioModelo = new DefaultListModel();
    JList<Funcionario> listaFuncionarios;

    public FuncionariosConsultar() {
        criarComponentes();
        criarEventos();
    }

    private void criarComponentes() {
        setLayout(null);

        labelTitulo = new JLabel("Consulta de Funcionários", JLabel.CENTER);
        labelTitulo.setFont(new Font(labelTitulo.getFont().getName(), Font.PLAIN, 20));
        labelFuncionario = new JLabel("Nome do funcionário", JLabel.LEFT);
        campoFuncionario = new JTextField();
        botaoPesquisar = new JButton("Pesquisar Funcinário");
        botaoEditar = new JButton("Editar Funcionário");
        botaoEditar.setEnabled(false);
        botaoExcluir = new JButton("Excluir Funcionário");
        botaoExcluir.setEnabled(false);
        listaFuncionarioModelo = new DefaultListModel();
        listaFuncionarios = new JList();
        listaFuncionarios.setModel(listaFuncionarioModelo);
        listaFuncionarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        labelTitulo.setBounds(20, 20, 660, 40);
        labelFuncionario.setBounds(150, 120, 400, 20);
        campoFuncionario.setBounds(150, 140, 400, 40);
        botaoPesquisar.setBounds(560, 140, 130, 40);
        listaFuncionarios.setBounds(150, 200, 400, 240);
        botaoEditar.setBounds(560, 360, 130, 40);
        botaoExcluir.setBounds(560, 400, 130, 40);

        add(labelTitulo);
        add(labelFuncionario);
        add(campoFuncionario);
        add(listaFuncionarios);
        add(botaoPesquisar);
        add(botaoEditar);
        add(botaoExcluir);

        setVisible(true);
    }

    private void criarEventos() {
        botaoPesquisar.addActionListener((ActionEvent ae) -> {
            sqlPesquisarFuncionarios(campoFuncionario.getText());
        });
        botaoEditar.addActionListener((ActionEvent ae) -> {
            Navegador.funcionariosEditar(funcionarioAtual);
        });
        botaoExcluir.addActionListener((ActionEvent ae) -> {
            sqlDeletarFuncionario();
        });
        listaFuncionarios.addListSelectionListener((ListSelectionEvent e) -> {
            funcionarioAtual = listaFuncionarios.getSelectedValue();
            if (funcionarioAtual == null) {
                botaoEditar.setEnabled(false);
                botaoExcluir.setEnabled(false);
            } else {
                botaoEditar.setEnabled(true);
                botaoExcluir.setEnabled(true);
            }
        });
    }

    private void sqlPesquisarFuncionarios(String nome) {
        Connection conexao;
        Statement instrucao;
        ResultSet resultados;

        try {
            conexao = DriverManager.getConnection(BancoDeDados.stringDeConexao, BancoDeDados.usuario, BancoDeDados.senha);
            instrucao = conexao.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultados = instrucao.executeQuery("SELECT * FROM funcionarios WHERE nome LIKE '%" + nome + "%' order by nome");

            listaFuncionarioModelo.clear();

            while (resultados.next()) {
                Funcionario funcionario = new Funcionario();
                funcionario.setId(resultados.getInt("id"));
                funcionario.setNome(resultados.getString("nome"));
                funcionario.setSobrenome(resultados.getString("sobrenome"));
                funcionario.setEmail(resultados.getString("email"));
                funcionario.setSalario(Double.parseDouble(resultados.getString("salario")));

                if (resultados.getString("cargo") != null) {
                    funcionario.setCargo(Integer.parseInt(resultados.getString("cargo")));
                }

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String dataFormatada = sdf.format(resultados.getDate("data_nascimento"));

                funcionario.setDataNascimento(dataFormatada);
                
                listaFuncionarioModelo.addElement(funcionario);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao consultar o Funcionário");
            Logger.getLogger(CargosInserir.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void sqlDeletarFuncionario() {

        int confirmacao = JOptionPane.showConfirmDialog(null, "Deseja realmente excluir o(a) funcionário(a) "
                + funcionarioAtual.getNome() + "?", "Excluir", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {

            Connection conexao;
            Statement instrucao;

            try {
                conexao = DriverManager.getConnection(BancoDeDados.stringDeConexao, BancoDeDados.usuario, BancoDeDados.senha);
                instrucao = conexao.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                instrucao.executeUpdate("DELETE FROM FUNCIONARIOS WHERE id=" + funcionarioAtual.getId() + "");

                JOptionPane.showMessageDialog(null, "Funcionário deletado com sucesso!");
                listaFuncionarioModelo.clear();
                Navegador.inicio();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao excluir o Funcionário");
                Logger.getLogger(CargosInserir.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
}
