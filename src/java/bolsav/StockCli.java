package bolsav;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A classe StockCli relaciona uma ação com a interface de um cliente
 *
 * @author Davi Pereira Neto
 * @author Geovana Franco Santos
 */
public class StockCli implements Serializable {

    public Stock stock;
    public String client;
    public int id;
    //variável para ver se a compra realizou ou não
    public boolean statusSell;

    /**
     * Construtor da classe seta a ação, o cliente a quem ela pertence, o id o
     * cliente e coloca que o status da transação é falso
     *
     * @param stock com a ação que o cliente possui
     * @param client com a referência do cliente
     * @param id com o id do cliente
     */
    public StockCli(Stock stock, String client, int id) {
        this.stock = stock;
        this.client = client;
        this.id = id;
        this.statusSell = false;
    }

    

    /**
     * Retorna a ação
     *
     * @return do tipo Stock com a ação desejada
     */
    public Stock getStock() {
        return stock;
    }

    /**
     * Retorna a referência do cliente
     *
     * @return do tipo String com o nome do cliente
     */
    public String getClient() {
        return client;
    }

    /**
     * Retorna o id do cliente
     *
     * @return do tipo long com o id do cliente
     */
    public int getId() {
        return id;
    }

}
