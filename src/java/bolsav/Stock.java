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
    public boolean available;   //disponibilidade no marcado
    public double actualPrice;  //preço que é alterado pelo servidor
    public double maxPrice;     //preço máximo de compra
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
        this.minPrice = minPrice;
        this.actualPrice = minPrice;
    }

    //construtor para compra
    public Stock(double maxPrice, String company, int qt) {
        this.company = company;
        this.qt = qt;
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
     * Retorna se a ação está disponível ou não para venda
     *
     * @return do tipo boolean representando a disponibilidade ou não da ação
     * para venda
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Armazena se a ação está disponível ou não
     *
     * @param available com a disponibilidade da ação (true ou false)
     */
    public void setAvailable(boolean available) {
        this.available = available;
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

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    
}
