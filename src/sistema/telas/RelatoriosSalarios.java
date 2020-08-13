package sistema.telas;

import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import sistema.Navegador;
import sistema.BancoDeDados;

public class RelatoriosSalarios extends JPanel {

    JLabel labelTitulo;
    JLabel labelDescricao;

    public RelatoriosSalarios() {
        criarComponentes();
        criarEventos();
        Navegador.habilitaMenu();
    }

    private void criarComponentes() {
        setLayout(null);
        
        labelTitulo = new JLabel("Relatórios", JLabel.CENTER);
        labelTitulo.setFont(new Font(labelTitulo.getFont().getName(), Font.PLAIN, 20));
        labelDescricao = new JLabel("Esse gráfico representa a quantidade de funcionários por faixa salarial",
                 JLabel.CENTER);
        
        CategoryDataset dadosGrafico = this.criarDadosGrafico();
        
        JFreeChart someChart = ChartFactory.createBarChart3D("", null, "Quantidade de funcionários",
                dadosGrafico, PlotOrientation.VERTICAL, true, true, false);
        CategoryPlot plot = (CategoryPlot) someChart.getCategoryPlot();
        
        plot.setBackgroundPaint(null);
        plot.setOutlinePaint(null);
        someChart.setBackgroundPaint(null);
        
        plot.getRangeAxis().setLowerBound(0);
        plot.getRangeAxis().setRange(new Range(0, 5));
        plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
        plot.getRangeAxis().setAutoRange(false);
        Font font = new Font(labelTitulo.getFont().getName(), Font.PLAIN, 10);
        plot.getRangeAxis().setLabelFont(font);
        
        plot.getDomainAxis().setVisible(false);
        
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setItemMargin(-2);
        
        ChartPanel chartPanel = new ChartPanel(someChart){
            @Override
            public Dimension getPreferredSize(){
                return new Dimension(660, 340);
            }
        };
        
        labelTitulo.setBounds(20, 20, 660, 40);
        labelDescricao.setBounds(20, 50, 660, 40);
        chartPanel.setBounds(20, 100, 660, 340);
        
        add(labelTitulo);
        add(labelDescricao);
        add(chartPanel);
        
        setVisible(true);
    }

    private void criarEventos() {
        
    }

    @SuppressWarnings("empty-statement")
    private CategoryDataset criarDadosGrafico() {
        
        DefaultCategoryDataset dados = new DefaultCategoryDataset();
        
        Connection conexao;
        Statement instrucao;
        ResultSet resultados;
        
        try {
            conexao = DriverManager.getConnection(BancoDeDados.stringDeConexao, BancoDeDados.usuario, BancoDeDados.senha);
            instrucao = conexao.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            StringBuilder query = new StringBuilder("SELECT ");
            query.append("COUNT(CASE WHEN SALARIO < 1000 THEN 1 END) AS faixa1,");
            query.append("COUNT(CASE WHEN SALARIO >= 1000 AND SALARIO < 2000 THEN 1 END) AS faixa2, ");
            query.append("COUNT(CASE WHEN SALARIO >= 2000 AND SALARIO < 3000 THEN 1 END) AS faixa3, ");
            query.append("COUNT(CASE WHEN SALARIO >= 3000 AND SALARIO < 4000 THEN 1 END) AS faixa4, ");
            query.append("COUNT(CASE WHEN SALARIO >= 4000 AND SALARIO < 5000 THEN 1 END) AS faixa5, ");
            query.append("COUNT(CASE WHEN SALARIO > 5000 THEN 1 END) AS faixa6 ");
            query.append("FROM FUNCIONARIOS");
            resultados = instrucao.executeQuery(query.toString());
            
            while (resultados.next()) {                
                dados.addValue(resultados.getInt("faixa1"), "Até R$ 1.000,00", "< R$ 1.000,00");
                dados.addValue(resultados.getInt("faixa2"), "De 1.000,00 até R$ 2.000,00", "R$ 1.000,00 - R$ 2.000,00");
                dados.addValue(resultados.getInt("faixa3"), "De 2.000,00 até R$ 3.000,00", "R$ 2.000,00 - R$ 3.000,00");
                dados.addValue(resultados.getInt("faixa4"), "De 3.000,00 até R$ 4.000,00", "R$ 3.000,00 - R$ 4.000,00");
                dados.addValue(resultados.getInt("faixa5"), "De 4.000,00 até R$ 5.000,00", "R$ 4.000,00 - R$ 5.000,00");
                dados.addValue(resultados.getInt("faixa6"), "A partir de R$ 5.000,00", "> R$ 5.000,00");
            }
            
            return dados;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao criar o relatório.\n\n"+e.getLocalizedMessage());
            Navegador.inicio();
        }
        return null;
    }

}
