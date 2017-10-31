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
    public List<StockCli> stocksCli = new ArrayList();
    /**
     * Ações disponíveis no mercado
     */
    public List<Stock> stocksLocal = new ArrayList();
    /**
     * Lista de compradores e quais ações de desejo
     */
    public List<StockCli> buyers = new ArrayList();
    /**
     * Lista de vendores e ações disponíveis pra compra, e o sta
     */
    public List<StockCli> sellers = new ArrayList();

    public int idClients = 0;
    /*public NewWebService(){
        TimerTask update = new TimerTask() {
            @Override
            public void run() {
                updatePrices();
            }
        };
        Timer t = new Timer();
        //a tarefa executa de 15 em 15 segundos
        t.schedule(update, 15000, 15000);
    }*/
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
    
    @WebMethod(operationName = "login")
    public int login() {
        idClients++;
        return idClients;
    }
    
    /**
     * Operação de Web service que retorna uma ação de determinada empresa.
     *
     * @param company com o nome da empresa pesquisada
     * @return com a lista de correspondencias encontradas
     */
    @WebMethod(operationName = "getStocks")
    public List<String> getStocks(@WebParam(name = "company") String company) {
        //chama o método que atualiza os preços atuais das ações
        updatePrices();
        //chama o método que verifica se há alguma compatibilidade para compra e venda
        buyANDsell();
        List<String> s = new ArrayList();
        for (Stock st : stocksLocal) {
            if (st.company.equals(company)) {
                s.add(st.company + " " + st.getPrice() + " " + st.getQntd());
            }
        }
        return s;
    }
    
    /**
     * Operação de Web service que retorna uma ação de determinada empresa.
     *
     * @param company com o nome da empresa pesquisada
     * @return com a lista de correspondencias encontradas
     */
    @WebMethod(operationName = "getStocksClient")
    public List<String> getStocksClient(@WebParam(name = "client") String client) {
        //chama o método que atualiza os preços atuais das ações
        updatePrices();
        //chama o método que verifica se há alguma compatibilidade para compra e venda
        buyANDsell();
        List<String> s = new ArrayList();
        for (StockCli st : stocksCli) {
            if (st.client.equals(client)) {
                s.add(st.stock.company + " " + st.stock.minPrice + " " + st.stock.getQntd());
            }
        }
        return s;
    }

    /**
     * Operação de Web service que adiciona uma ação na lista de ações que
     * relaciona um cliente
     */
    @WebMethod(operationName = "newStock_sell")
    public String newStock_sell(@WebParam(name = "client") String client, @WebParam(name = "company") String company, 
            @WebParam(name = "qntd") int qntd, @WebParam(name = "minPrice") double minPrice, @WebParam(name = "id") int id) {
        //verifica se a ação já estava na tabela de vendas, se já estava só atualiza quantidade e o preço mínimo de venda
        Stock stock = new Stock(company, qntd, minPrice);
        for (StockCli sc : sellers) {
            if (sc.stock.company.equals(stock.company) && client.equals(sc.client)) {
                sc.stock.setQt(stock.getQt());
                sc.stock.setMinPrice(stock.getMinPrice());
                return "Foi atualizado a ação " + stock.toString2() + " na lista de vendas";
            }
        }
        //se não existe na lista adiciona uma nova
        //é necessário que os preços estejam setados corretamente
        StockCli sc = new StockCli(stock, client, id);
        sellers.add(sc);
        //chama o método que atualiza os preços atuais das ações
        updatePrices();
        //chama o método que verifica se há alguma compatibilidade para compra e venda
        buyANDsell();
        return "Adicionado ação " + stock.toString() + " na lista de vendas";

    }

    /**
     * Operação de Web service para cadastrar o desejo de venda de algum cliente
     */
    @WebMethod(operationName = "newStock_buy")
    public String newStock_buy(@WebParam(name = "client") String client, @WebParam(name = "company") String company, 
            @WebParam(name = "qntd") int qntd, @WebParam(name = "maxPrice") double maxPrice, @WebParam(name = "id") int id) {
        //verifica se a ação já estava na tabela de compras, se já estava só atualiza quantidade e o preço máximo de compra
        Stock stock = new Stock(maxPrice, company, qntd);
        for (StockCli sc : buyers) {
            if (sc.stock.company.equals(stock.company) && client.equals(sc.client)) {
                sc.stock.setQt(stock.getQt());
                sc.stock.setMaxPrice(stock.getMaxPrice());
                return "Foi atualizado a ação " + stock.toString3() + " na lista de compras";
            }
        }
        //se não existe na lista adiciona uma nova
        //é necessário que os preços estejam setados corretamente
        StockCli sc = new StockCli(stock, client, id);
        buyers.add(sc);
        
        //chama o método que atualiza os preços atuais das ações
        updatePrices();
        //chama o método que verifica se há alguma compatibilidade para compra e venda
        buyANDsell();
        return "Adicionado ação " + stock.toString() + " na lista de compras";
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "newStock")
    public String newStock(@WebParam(name = "client") String client, @WebParam(name = "company") String company, 
            @WebParam(name = "qntd") int qntd, @WebParam(name = "minPrice") double minPrice, @WebParam(name = "id") int id) {
        //verifica se a ação e o cliente associada com ela já existe na lista
        //se existe apenas atualiza a quantidade e a disponibilidade de venda

        Stock stock = new Stock(company, qntd, minPrice);
        newStockLocal(company, qntd, minPrice);
        for (StockCli sc : stocksCli) {
            if (sc.stock.company.equals(stock.company) && client.equals(sc.client)) {
                sc.stock.setQt(stock.getQt());
                return "Foi atualizado a ação " + stock.toString();
            }
        }
        //se não existe na lista adiciona uma nova
        StockCli sc = new StockCli(stock, client, id);
        stocksCli.add(sc);
        //chama o método que atualiza os preços atuais das ações
        updatePrices();
        //chama o método que verifica se há alguma compatibilidade para compra e venda
        buyANDsell();
        return "Adicionado ação " + stock.toString();
    }

    public void newStockLocal(String company, int qntd, double minPrice) {
        //verifica se a ação já existe na lista de ações local
        //se existe apenas atualiza a quantidade e a disponibilidade de venda
        Stock stock = new Stock(company, qntd, minPrice);
        for (Stock scL : stocksLocal) {
            if (scL.company.equals(stock.company)) {
                scL.setQt(scL.getQt() + stock.getQt());
                return;
            }
        }
        //se não existir adiciona nova
        stocksLocal.add(stock);
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


    /**
     * Método que busca e realiza compra e venda das ações cadastradas pelos
     * clientes, seguindo as regras de transação.
     */
    public void buyANDsell() {
        int qtde1 = 0;
        Stock sLocal = null;
        for (StockCli b : buyers) {
            for (StockCli s : sellers) {
                //se a compra não foi realizada ele tenta realizar a compra

                if (!b.statusSell) {
                    //busca a ação desejada da lista local de ações
                    for (Stock sL : stocksLocal) {
                        if (sL.company.equals(b.stock.company)) {
                            sLocal = sL;
                        }
                    }
                    if (sLocal != null) {
                        //se o vendedor não é o comprador, se as empresas batem, se o preço atual é maior 
                        //que o preço desejado pelo vendedor
                        //se  preço atual é menor que o desejado pelo comprador
                        if (!b.client.equals(s.client) && b.stock.company.equals(s.stock.company) && 
                                sLocal.actualPrice >= s.stock.minPrice && sLocal.actualPrice <= b.stock.maxPrice) {
                            //se a quantide da ação for menor do que a desejada transaciona o quanto ter
                            if (b.stock.qt <= s.stock.qt) {
                                qtde1 = s.stock.qt;
                            } else {
                                qtde1 = b.stock.qt;
                            }
                            //armazena o valor da transação pegando a média entre o valor máximo desejado e o valor 
                            //que o comprador quer pagar
                            b.stock.transactionPrice = (sLocal.actualPrice + b.stock.maxPrice) / 2;
                            s.stock.setTransactionPrice(b.stock.transactionPrice);
                            //atualiza a quantidade que foi transacionada nas listas
                            b.stock.setQt(qtde1);
                            s.stock.setQt(s.stock.qt - qtde1);
                            //sinaliza que essas ações já foram vendidas ou compradas
                            b.statusSell = true;
                            s.statusSell = true;
                            //atualiza nas tabelas dos clientes
                            String n = this.newStock(b.client, b.stock.company, b.stock.qt, b.stock.transactionPrice, b.id);
                            String m = this.newStock(s.client, s.stock.company, s.stock.qt, s.stock.transactionPrice, s.id);
                        }
                    }
                }
            }
        }
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "getTransactionsBuy")
    public List<String> getTransactionsBuy(@WebParam(name = "client") String client) {
        //chama o método que atualiza os preços atuais das ações
        updatePrices();
        //chama o método que verifica se há alguma compatibilidade para compra e venda
        buyANDsell();
        List<String> s = new ArrayList();
        for (StockCli sB : buyers) {
            if (sB.client.equals(client)) {
                String status = "Incompleta";
                if(sB.statusSell)
                    status = "Completa";   
                s.add(sB.stock.company + " " + sB.stock.maxPrice + " " + sB.stock.transactionPrice + " " + sB.stock.getQt() + " " +status);
            }
        }
        return s;
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "getTransactionsSell")
    public List<String> getTransactionsSell(@WebParam(name = "client") String client) {
        //chama o método que atualiza os preços atuais das ações
        updatePrices();
        //chama o método que verifica se há alguma compatibilidade para compra e venda
        buyANDsell();
        List<String> s = new ArrayList();
        for (StockCli sS : sellers) {
            if (sS.client.equals(client)) {
                String status = "Incompleta";
                if(sS.statusSell)
                    status = "Completa";                            
                s.add(sS.stock.company + " " + sS.stock.minPrice + " " + sS.stock.transactionPrice + " " + sS.stock.getQt() + " " +status);
            }
        }
        return s;
    }
}
