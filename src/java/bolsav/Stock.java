package bolsav;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * A classe Stock representa uma ação do mercado, disponível para compra e
 * venda.
 *
 * @author Geovana Franco Santos
 */
public class Stock implements Serializable {

    public String company;      //empresa
    public int qt;              //quantidade disponível
    public double minPrice;     //preço mínimo de venda
    public double actualPrice;  //preço que é alterado pelo servidor
    public double maxPrice;     //preço máximo de compra
    public Integer qntd;        //quantidaded do tipo Integer para utilizar método toString()
    public double transactionPrice; //preço com o qual a ação foi vendida ou comprada

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

    /**
     * Construtor da classe para ação de compra, seta qual a empresa a ação
     * pertence, o preço máximo de compra e a quantidade desejada.
     *
     * @param maxPrice com o preço máximo
     * @param company com o nome da empresa
     * @param qt com a quantidade disponível
     */
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

    /**
     * Retorna a quantidade da ação em uma string
     *
     * @return do tipo string com a quantidade da ação
     */
    public String getQntd() {
        return qntd.toString();
    }

    /**
     * Retorna o preço máximo de compra da ação.
     *
     * @return do tipo double com o preço máximo da ação
     */
    public double getMaxPrice() {
        return maxPrice;
    }

    /**
     * Armazena o preço máximo de compra da ação
     *
     * @param maxPrice com o preço máximo da ação
     */
    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    /**
     * Retorna o valor com o qual a ação foi vendida ou comprada
     *
     * @return com o valor de transação da ação
     */
    public double getTransactionPrice() {
        return transactionPrice;
    }

    /**
     * Armazena o valor com o qual a ação foi vendida ou comprada
     *
     * @param transactionPrice com o valor de transação da ação
     */
    public void setTransactionPrice(double transactionPrice) {
        this.transactionPrice = transactionPrice;
    }

    /**
     * Método que retona os dados da ação em uma string, usado para venda.
     *
     * @return do tipo String com os dados da ação
     */
    public String toString2() {
        return this.getCompany() + " " + this.getMinPrice() + " " + this.getQntd();
    }

    /**
     * Método que retona os dados da ação em uma string, usado para compra.
     *
     * @return do tipo String com os dados da ação
     */
    public String toString3() {
        return this.getCompany() + " " + this.getMaxPrice() + " " + this.getQntd();
    }

    /**
     * Método que retona os dados da ação em uma string.
     *
     * @return do tipo String com os dados da ação
     */
    @Override
    public String toString() {
        return this.getCompany() + " " + this.getPrice() + " " + this.getQntd();
    }

}
