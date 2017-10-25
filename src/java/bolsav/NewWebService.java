/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bolsav;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.jws.Oneway;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 * Classe web services, onde são disponibilizados os métodos de manutenção da
 * conta bancária
 *
 * @author a1562711
 */
@WebService(serviceName = "NewWebService")
public class NewWebService {
    /**
     * Lista de ações que os clientes possuem
     */
    public List<StockCli> stocksCli;
    /**
     * Ações disponíveis no mercado
     */
    public List<Stock> stocks;
    /**
     * Lista de compradores e quais ações de desejo
     */
    public List<StockCli> buyers;
    /**
     * Lista de vendores e ações disponíveis pra compra, e o sta
     */
    public List<StockCli> sellers;
    
    /**
     * Operação de Web service que apaga da lista de ações as ações do cliente que saiu do mercado evitando que tente vender no futuro de um cliente inexistente
     */
    @WebMethod(operationName = "logout")
    public Integer logout(@WebParam(name = "client") String client) {
        for (StockCli st : stocksCli) {
            if (st.client.equals(client)) {
                stocksCli.remove(st);
            }
        }
        return 0;
    }


    /**
     * Operação de Web service que retorna uma ação de determinada empresa.
     */
    @WebMethod(operationName = "getStocks")
    public Stock getStocks(@WebParam(name = "company") String company) {
        for (Stock st : stocks) {
            if (st.company.equals(company)) {
                return st;
            }
        }
        return null;
    }

    /**
     * Operação de Web service que adiciona uma ação na lista de ações que relaciona um cliente
     */    
    @WebMethod(operationName = "newStock_sell")
    public Integer newStock_sell(@WebParam(name = "client") String client, @WebParam(name = "stock") Stock stock, @WebParam(name = "id") long id) {
        //verifica se a ação já estava na tabela de vendas, se já estava só atualiza quantidade e o preço mínimo de venda
        for (StockCli sc : sellers) {
            if (sc.stock.company.equals(stock.company) && client.equals(sc.client)) {
                sc.stock.setQt(stock.getQt());
                sc.stock.setMinPrice(stock.getMinPrice());
                return 0;
            }
        }
        //se não existe na lista adiciona uma nova
        //é necessário que os preços estejam setados corretamente
        StockCli sc = new StockCli(stock, client, id);
        sellers.add(sc);
        //verifica se existe compatibilidade entre as compras e vendas
        return 1;
    }

    /**
     * Operação de Web service para cadastrar o desejo de venda de algum cliente
     */
    @WebMethod(operationName = "newStock_buy")
    public Integer newStock_buy(@WebParam(name = "client") String client, @WebParam(name = "stock") Stock stock, @WebParam(name = "id") long id) {
        //verifica se a ação já estava na tabela de compras, se já estava só atualiza quantidade e o preço máximo de compra
        for (StockCli sc : buyers) {
            if (sc.stock.company.equals(stock.company) && client.equals(sc.client)) {
                sc.stock.setQt(stock.getQt());
                sc.stock.setMaxPrice(stock.getMaxPrice());
                return 0;
            }
        }
        //se não existe na lista adiciona uma nova
        //é necessário que os preços estejam setados corretamente
        StockCli sc = new StockCli(stock, client, id);
        buyers.add(sc);
        //verifica se existe compatibilidade entre as compras e vendas
        return 1;
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "newStock")
    public Integer newStock(@WebParam(name = "client") String client, @WebParam(name = "stock") Stock stock, @WebParam(name = "id") long id) {
        //verifica se a ação e o cliente associada com ela já existe na lista
        //se existe apenas atualiza a quantidade e a disponibilidade de venda
        for (StockCli sc : stocksCli) {
            if (sc.stock.company.equals(stock.company) && client.equals(sc.client)) {
                sc.stock.setQt(stock.getQt());
                sc.stock.setAvailable(true);
                return 0;
            }
        }
        //se não existe na lista adiciona uma nova
        StockCli sc = new StockCli(stock, client, id);
        stocksCli.add(sc);
        return 1;
    }

}
