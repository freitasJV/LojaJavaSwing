package sistema;

import java.awt.event.ActionEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import sistema.entidades.Cargo;
import sistema.entidades.Funcionario;
import sistema.telas.CargosConsultar;
import sistema.telas.CargosEditar;
import sistema.telas.CargosInserir;
import sistema.telas.FuncionariosConsultar;
import sistema.telas.FuncionariosEditar;
import sistema.telas.FuncionariosInserir;
import sistema.telas.Inicio;
import sistema.telas.Login;
import sistema.telas.RelatoriosCargos;
import sistema.telas.RelatoriosSalarios;

public class Navegador {

    private static boolean menuConstruido;
    private static boolean menuHabilitado;
    private static JMenuBar menuBar;
    private static JMenu menuArquivo;
    private static JMenu menuFuncionarios;
    private static JMenu menuCargos;
    private static JMenu menuRelatorios;
    private static JMenuItem miSair;
    private static JMenuItem miFuncionariosConsultar;
    private static JMenuItem miFuncionariosCadastrar;
    private static JMenuItem miCargosConsultar;
    private static JMenuItem miCargosCadastrar;
    private static JMenuItem miRelatoriosCargos;
    private static JMenuItem miRelatoriosSalarios;

    public static void login() {
        Sistema.tela = new Login();
        Sistema.frame.setTitle("Funcionários Company SA");
        Navegador.atualizarTela();
    }

    public static void inicio() {
        Sistema.tela = new Inicio();
        Sistema.frame.setTitle("Funcionários Company SA");
        Navegador.atualizarTela();
    }

    public static void cargosCadastrar() {
        Sistema.tela = new CargosInserir();
        Sistema.frame.setTitle("Funcionários Company SA - Cadastrar Cargos");
        Navegador.atualizarTela();
    }

    public static void cargosConsultar() {
        Sistema.tela = new CargosConsultar();
        Sistema.frame.setTitle("Funcionários Company SA - Consultar Cargos");
        Navegador.atualizarTela();
    }

    public static void cargosEditar(Cargo cargo) {
        Sistema.tela = new CargosEditar(cargo);
        Sistema.frame.setTitle("Funcionários Company SA - Editar Cargos");
        Navegador.atualizarTela();
    }

    public static void funcionariosCadastrar() {
        Sistema.tela = new FuncionariosInserir();
        Sistema.frame.setTitle("Funcionários Company SA - Cadastrar Funcionários");
        Navegador.atualizarTela();
    }

    public static void funcionariosConsultar() {
        Sistema.tela = new FuncionariosConsultar();
        Sistema.frame.setTitle("Funcionários Company SA - Consultar Funcionários");
        Navegador.atualizarTela();
    }

    public static void funcionariosEditar(Funcionario funcionario) {
        Sistema.tela = new FuncionariosEditar(funcionario);
        Sistema.frame.setTitle("Funcionários Company SA - Editar Funcionário");
        Navegador.atualizarTela();
    }
    
    public static void relatoriosCargos() {
        Sistema.tela = new RelatoriosCargos();
        Sistema.frame.setTitle("Funcionários Company SA - Relatórios");
        Navegador.atualizarTela();
    }
    
    public static void relatoriosSalarios() {
        Sistema.tela = new RelatoriosSalarios();
        Sistema.frame.setTitle("Funcionários Company SA - Relatórios");
        Navegador.atualizarTela();
    }

    private static void atualizarTela() {
        Sistema.frame.getContentPane().removeAll();
        Sistema.tela.setVisible(true);
        Sistema.frame.add(Sistema.tela);

        Sistema.frame.setVisible(true);
    }

    private static void construirMenu() {
        if (!menuConstruido) {
            menuConstruido = true;

            menuBar = new JMenuBar();

            menuArquivo = new JMenu("Arquivo");
            menuBar.add(menuArquivo);
            miSair = new JMenuItem("Sair");
            menuArquivo.add(miSair);

            menuFuncionarios = new JMenu("Funcionários");
            menuBar.add(menuFuncionarios);
            miFuncionariosCadastrar = new JMenuItem("Cadastrar");
            menuFuncionarios.add(miFuncionariosCadastrar);
            miFuncionariosConsultar = new JMenuItem("Consultar");
            menuFuncionarios.add(miFuncionariosConsultar);

            menuCargos = new JMenu("Cargos");
            menuBar.add(menuCargos);
            miCargosCadastrar = new JMenuItem("Cadastrar");
            menuCargos.add(miCargosCadastrar);
            miCargosConsultar = new JMenuItem("Consultar");
            menuCargos.add(miCargosConsultar);

            menuRelatorios = new JMenu("Relatórios");
            menuBar.add(menuRelatorios);
            miRelatoriosCargos = new JMenuItem("Funcionários por cargos");
            menuRelatorios.add(miRelatoriosCargos);
            miRelatoriosSalarios = new JMenuItem("Salários dos funcionários");
            menuRelatorios.add(miRelatoriosSalarios);

            criarEventoMenu();
        }
    }

    public static void habilitaMenu() {
        if (!menuConstruido) {
            construirMenu();
        }
        if (!menuHabilitado) {
            menuHabilitado = true;
            Sistema.frame.setJMenuBar(menuBar);
        }
    }

    public static void desabilitaMenu() {
        if (menuHabilitado) {
            menuHabilitado = false;
            Sistema.frame.setJMenuBar(null);
        }
    }

    private static void criarEventoMenu() {
        miSair.addActionListener((ActionEvent ae) -> {
            System.exit(0);
        });

        miFuncionariosCadastrar.addActionListener((ActionEvent ae) -> {
            funcionariosCadastrar();
        });

        miFuncionariosConsultar.addActionListener((ActionEvent ae) -> {
            funcionariosConsultar();
        });

        miCargosCadastrar.addActionListener((ActionEvent ae) -> {
            cargosCadastrar();
        });

        miCargosConsultar.addActionListener((ActionEvent ae) -> {
            cargosConsultar();
        });

        miRelatoriosCargos.addActionListener((ActionEvent ae) -> {
            relatoriosCargos();
        });

        miRelatoriosSalarios.addActionListener((ActionEvent ae) -> {
            relatoriosSalarios();
        });
    }
}
