/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bolsav;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.jws.Oneway;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author geova
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
    public List<Stock> stocksLocal;
    /**
     * Lista de compradores e quais ações de desejo
     */
    public List<StockCli> buyers;
    /**
     * Lista de vendores e ações disponíveis pra compra, e o sta
     */
    public List<StockCli> sellers;

    public NewWebService(){
        TimerTask update = new TimerTask() {
            @Override
            public void run() {
                updatePrices();
            }
        };
        Timer t = new Timer();
        //a tarefa executa de 15 em 15 segundos
        t.schedule(update, 15000, 15000);
    }
    /**
     * Operação de Web service que apaga da lista de ações as ações do cliente
     * que saiu do mercado evitando que tente vender no futuro de um cliente
     * inexistente
     */
    @WebMethod(operationName = "logout")
    @Oneway
    public void logout(@WebParam(name = "client") String client) {
        for (StockCli st : stocksCli) {
            if (st.client.equals(client)) {
                stocksCli.remove(st);
            }
        }
    }

    /**
     * Operação de Web service que retorna uma ação de determinada empresa.
     * 
     */
    @WebMethod(operationName = "getStocks")
    public List<String> getStocks(@WebParam(name = "company") String company) {
        List<String> s = new ArrayList<String>();
        for (Stock st : stocksLocal) {
            if (st.company.equals(company)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Operação de Web service que adiciona uma ação na lista de ações que
     * relaciona um cliente
     */
    @WebMethod(operationName = "newStock_sell")
    @Oneway
    public void newStock_sell(@WebParam(name = "client") String client, @WebParam(name = "company") String company, @WebParam(name = "qntd") int qntd, @WebParam(name = "minPrice") double minPrice, @WebParam(name = "id") long id) {
        //verifica se a ação já estava na tabela de vendas, se já estava só atualiza quantidade e o preço mínimo de venda
        Stock stock = new Stock(company, qntd, minPrice);
        for (StockCli sc : sellers) {
            if (sc.stock.company.equals(stock.company) && client.equals(sc.client)) {
                sc.stock.setQt(stock.getQt());
                sc.stock.setMinPrice(stock.getMinPrice());
                return;
            }
        }
        //se não existe na lista adiciona uma nova
        //é necessário que os preços estejam setados corretamente
        StockCli sc = new StockCli(stock, client, id);
        sellers.add(sc);
        //verifica se existe compatibilidade entre as compras e vendas
    }

    /**
     * Operação de Web service para cadastrar o desejo de venda de algum cliente
     */
    @WebMethod(operationName = "newStock_buy")
    @Oneway
    public void newStock_buy(@WebParam(name = "client") String client, @WebParam(name = "company") String company, @WebParam(name = "qntd") int qntd, @WebParam(name = "maxPrice") double maxPrice, @WebParam(name = "id") long id) {
        //verifica se a ação já estava na tabela de compras, se já estava só atualiza quantidade e o preço máximo de compra
        Stock stock = new Stock(maxPrice, company, qntd);
        for (StockCli sc : buyers) {
            if (sc.stock.company.equals(stock.company) && client.equals(sc.client)) {
                sc.stock.setQt(stock.getQt());
                sc.stock.setMaxPrice(stock.getMaxPrice());
                return;
            }
        }
        //se não existe na lista adiciona uma nova
        //é necessário que os preços estejam setados corretamente
        StockCli sc = new StockCli(stock, client, id);
        buyers.add(sc);
        //verifica se existe compatibilidade entre as compras e vendas
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "newStock")
    @Oneway
    public void newStock(@WebParam(name = "client") String client, @WebParam(name = "company") String company, @WebParam(name = "qntd") int qntd, @WebParam(name = "minPrice") double minPrice, @WebParam(name = "id") long id) {
        //verifica se a ação e o cliente associada com ela já existe na lista
        //se existe apenas atualiza a quantidade e a disponibilidade de venda
        Stock stock = new Stock(company, qntd, minPrice);
        for (StockCli sc : stocksCli) {
            if (sc.stock.company.equals(stock.company) && client.equals(sc.client)) {
                sc.stock.setQt(stock.getQt());
                sc.stock.setAvailable(true);
                return;
            }
        }
        //se não existe na lista adiciona uma nova
        StockCli sc = new StockCli(stock, client, id);
        stocksCli.add(sc);
    }

    /**
     * Método que atualiza os preços das ações presentes na lista local do
     * servidor de forma randômica
     */
    public void updatePrices() {
        //cria um randomGenerator
        Random randomGenerator = new Random();
        //para todas as ações presentes na lista de ações
        for (Stock st : stocksLocal) {
            //gera um boolean para saber se soma ou subtrai o valor da ação
            boolean op = randomGenerator.nextBoolean();
            //cria um número para ser adicionado ou somado
            int range = randomGenerator.nextInt(5);
            //pega o valor atual da ação
            double old = st.actualPrice;
            //se op for true e o range maior q 0
            if (op && range > 0) { //faz a soma do preço atual com o range
                st.actualPrice += range;
                //chama o método que notifica clientes inscritos que o preço subiu
                //notifyClients(st, "rise", old, st.actualPrice);
            } //se o preço atual não fica menor que o preço mínimo que o cliente deseja quando subtraido o range7
            //e se o range maior que 0
            else if (st.actualPrice >= (range + st.minPrice) && range > 0) { //faz a subtracao do preço atual com o range
                st.actualPrice -= range;
                //chama o método que notifica clientes inscritos que o preço caiu
                //notifyClients(st, "drop", old, st.actualPrice);
            }
        }
    }
}
