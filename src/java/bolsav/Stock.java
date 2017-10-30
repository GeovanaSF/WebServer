package bolsav;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * A classe Stock representa uma ação do mercado, disponível para compra e venda
 *
 * @author Davi Pereira Neto
 * @author Geovana Franco Santos
 */
public class Stock implements Serializable {

    public String company;      //empresa
    public int qt;              //quantidade disponível
    public double minPrice;     //preço mínimo de venda
    public double actualPrice;  //preço que é alterado pelo servidor
    public double maxPrice;     //preço máximo de compra
    public Integer qntd;
    public double transactionPrice;
    /**
     * Construtor da classe, seta qual a empresa a ação pertence, o preço mínino
     * de venda e a quantidade disponível.
     *
     * @param company com o nome da empresa
     * @param qt com a quantidade disponível
     * @param minPrice com o preço mínimo de venda
     */
    public Stock(String company, int qt, double minPrice) {
        this.company = company;
        this.qt = qt;
        this.qntd = qt;
        this.minPrice = minPrice;
        this.actualPrice = minPrice;
    }

    //construtor para compra
    public Stock(double maxPrice, String company, int qt) {
        this.company = company;
        this.qt = qt;
        this.qntd = qt;
        this.maxPrice = maxPrice;
    }
    /**
     * Retorna a empresa da ação.
     *
     * @return do tipo String com o nome da empresa
     */
    public String getCompany() {
        return company;
    }

    /**
     * Armazena o nome da companhia a qual a ação pertence
     *
     * @param company com o nome da empresa
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * Retorna a quantidade disponível da ação
     *
     * @return do tipo int com a quantidade da ação
     */
    public int getQt() {
        return qt;
    }

    /**
     * Armazena a quantidade disponível da ação
     *
     * @param qt com a quantidade de ação
     */
    public void setQt(int qt) {
        this.qt = qt;
        this.qntd = qt;
    }

    /**
     * Retorna o valor mínimo de venda que a ação possi
     *
     * @return do tipo double com o valor mínimo de venda
     */
    public double getMinPrice() {
        return minPrice;
    }

    /**
     * Armazena o valor mínimo de venda da ação
     *
     * @param minPrice com o valor mínimo da ação
     */
    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    /**
     * Retorna o preço atual da ação formatado
     *
     * @return do tipo String com o valor atual da ação formatado
     */
    public String getPrice() {
        NumberFormat formatter = new DecimalFormat("#0.00");
        return formatter.format(actualPrice).replace(',', '.');
    }
    
    public String getQntd(){
        return qntd.toString();
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public double getTransactionPrice() {
        return transactionPrice;
    }

    public void setTransactionPrice(double transactionPrice) {
        this.transactionPrice = transactionPrice;
    }
    
    public String toString2(){
        return this.getCompany() + " " + this.getMinPrice() + " " + this.getQntd();
    }
    
    public String toString3(){
        return this.getCompany() + " " + this.getMaxPrice() + " " + this.getQntd();
    }
    
    @Override
    public String toString(){
        return this.getCompany() + " " + this.getPrice() + " " + this.getQntd();
    }
    
}
