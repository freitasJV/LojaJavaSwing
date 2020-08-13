package sistema.telas;

import com.mysql.jdbc.MysqlDataTruncation;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;
import sistema.BancoDeDados;
import sistema.Navegador;
import sistema.entidades.Cargo;
import sistema.entidades.Funcionario;

public class FuncionariosInserir extends JPanel {

    JLabel labelTitulo;
    JLabel labelNome;
    JLabel labelSobrenome;
    JLabel labelDataNascimento;
    JLabel labelEmail;
    JLabel labelCargo;
    JLabel labelSalario;
    JTextField campoNome;
    JTextField campoSobrenome;
    JFormattedTextField campoDataNascimento;
    JTextField campoEmail;
    JComboBox comboboxCargo;
    JFormattedTextField campoSalario;
    JButton botaoGravar;

    public FuncionariosInserir() {
        criarComponentes();
        criarEventos();
    }

    private void criarComponentes() {
        setLayout(null);

        labelTitulo = new JLabel("Cadastro de Funcionário", JLabel.CENTER);
        labelTitulo.setFont(new Font(labelTitulo.getFont().getName(), Font.PLAIN, 20));
        labelNome = new JLabel("Nome:", JLabel.LEFT);
        campoNome = new JTextField();
        labelSobrenome = new JLabel("Sobrenome:", JLabel.LEFT);
        campoSobrenome = new JTextField();

        labelDataNascimento = new JLabel("Data de Nascimento:", JLabel.LEFT);
        campoDataNascimento = new JFormattedTextField();
        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.install(campoDataNascimento);
        } catch (ParseException e) {
            Logger.getLogger(FuncionariosInserir.class.getName()).log(Level.SEVERE, null, e);
        }

        labelEmail = new JLabel("Email:", JLabel.LEFT);
        campoEmail = new JTextField();

        labelCargo = new JLabel("Cargo:", JLabel.LEFT);
        comboboxCargo = new JComboBox();

        labelSalario = new JLabel("Salário:", JLabel.LEFT);
        DecimalFormat formatter = new DecimalFormat("###0.00", new DecimalFormatSymbols(new Locale("pr", "BR")));
        campoSalario = new JFormattedTextField(formatter);
        campoSalario.setValue(0.00);

        botaoGravar = new JButton("Adicionar");

        labelTitulo.setBounds(20, 20, 660, 40);
        labelNome.setBounds(150, 80, 400, 20);
        labelSobrenome.setBounds(150, 140, 400, 20);
        labelDataNascimento.setBounds(150, 200, 400, 20);
        labelEmail.setBounds(150, 260, 400, 20);
        labelCargo.setBounds(150, 320, 400, 20);
        labelSalario.setBounds(150, 380, 400, 20);
        campoNome.setBounds(150, 100, 400, 40);
        campoSobrenome.setBounds(150, 160, 400, 40);
        campoDataNascimento.setBounds(150, 220, 400, 40);
        campoEmail.setBounds(150, 280, 400, 40);
        comboboxCargo.setBounds(150, 340, 340, 40);
        campoSalario.setBounds(150, 400, 400, 40);
        botaoGravar.setBounds(560, 400, 130, 40);

        add(labelTitulo);
        add(labelNome);
        add(labelSobrenome);
        add(labelDataNascimento);
        add(labelEmail);
        add(labelCargo);
        add(labelSalario);
        add(campoNome);
        add(campoSobrenome);
        add(campoDataNascimento);
        add(campoEmail);
        add(comboboxCargo);
        add(campoSalario);
        add(botaoGravar);

        sqlCarregarCargos();

        setVisible(true);
    }

    private void criarEventos() {
        botaoGravar.addActionListener((ActionEvent ae) -> {
            Funcionario novoFuncionario = new Funcionario();
            novoFuncionario.setNome(campoNome.getText());
            novoFuncionario.setSobrenome(campoSobrenome.getText());
            novoFuncionario.setDataNascimento(campoDataNascimento.getText());
            novoFuncionario.setEmail(campoEmail.getText());
            Cargo cargoSelecionado = (Cargo) comboboxCargo.getSelectedItem();

            if (cargoSelecionado != null) {
                novoFuncionario.setCargo(cargoSelecionado.getId());
            }

            novoFuncionario.setSalario(Double.valueOf(campoSalario.getText().replace(",", ".")));

            sqlInserirFuncionario(novoFuncionario);
        });
    }

    private void sqlCarregarCargos() {
        Connection conexao;
        Statement instrucao;
        ResultSet resultados;

        try {
            conexao = DriverManager.getConnection(BancoDeDados.stringDeConexao, BancoDeDados.usuario, BancoDeDados.senha);
            instrucao = conexao.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultados = instrucao.executeQuery("SELECT * FROM cargos ORDER BY nome");

            comboboxCargo.removeAll();

            while (resultados.next()) {
                Cargo cargo = new Cargo();
                cargo.setId(resultados.getInt("id"));
                cargo.setNome(resultados.getString("nome"));
                comboboxCargo.addItem(cargo);
            }
            comboboxCargo.updateUI();
            conexao.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao carregar os cargos");
            Logger.getLogger(CargosInserir.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void sqlInserirFuncionario(Funcionario novoFuncionario) {
        //Validação de nome e sobrenome
        if (campoNome.getText().length() <= 3 | campoSobrenome.getText().length() <= 3) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha os nomes e sobrenome corretamente.");
            return;
        }

        //Validação de salário
        if (Double.parseDouble(campoSalario.getText().replace(",", ".")) <= 100) {
            JOptionPane.showMessageDialog(null, "O campo salário deve ser preenchido com um valor acima de 100.");
            return;
        }

        //Validação do email
        Boolean emailValidado;
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])"
                + "|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(campoEmail.getText());
        emailValidado = m.matches();

        if (!emailValidado) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o email corretamente.");
            return;
        }

        Connection conexao;
        PreparedStatement instrucao;

        try {
            conexao = DriverManager.getConnection(BancoDeDados.stringDeConexao, BancoDeDados.usuario, BancoDeDados.senha);
            StringBuilder template = new StringBuilder("INSERT INTO funcionarios (nome,sobrenome,data_nascimento,email,cargo,salario)");
            template = template.append(" VALUES (?,?,?,?,?,?)");
            instrucao = conexao.prepareStatement(template.toString());
            instrucao.setString(1, novoFuncionario.getNome());
            instrucao.setString(2, novoFuncionario.getSobrenome());

            //Validação da data de nascimento
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dataFormatada = sdf.parse(novoFuncionario.getDataNascimento());
            if (!sdf.format(dataFormatada).equals(novoFuncionario.getDataNascimento())) {
                throw new ParseException("Por favor, preencha a data de nascimento corretamente.", 0);
            }

            java.sql.Date dataSQL = new java.sql.Date(dataFormatada.getTime());
            instrucao.setDate(3, dataSQL);

            instrucao.setString(4, novoFuncionario.getEmail());

            if (novoFuncionario.getCargo() > 0) {
                instrucao.setInt(5, novoFuncionario.getCargo());
            } else {
                instrucao.setNull(5, java.sql.Types.INTEGER);
            }
            instrucao.setString(6, novoFuncionario.getSalario().toString());
            instrucao.executeUpdate();

            JOptionPane.showMessageDialog(null, "Funcionário adicionado com sucesso");
            Navegador.inicio();

            conexao.close();
        } catch (MysqlDataTruncation e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro, verifique os dados preenchidos no formulário");
            Logger.getLogger(FuncionariosInserir.class.getName()).log(Level.SEVERE, null, e);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao adicionar o Funcionário");
            Logger.getLogger(FuncionariosInserir.class.getName()).log(Level.SEVERE, null, e);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            Logger.getLogger(FuncionariosInserir.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
