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
 * Classe de serviço WebService que cria um serviço para controle de um mercado
 * de ações.
 *
 * @author Geovana Franco Santos
 */
@WebService(serviceName = "NewWebService")
public class NewWebService {

    /**
     * Lista de ações que os clientes possuem.
     */
    public List<StockCli> stocksCli = new ArrayList();
    /**
     * Ações disponíveis no mercado.
     */
    public List<Stock> stocksLocal = new ArrayList();
    /**
     * Lista de compradores e quais ações de desejo.
     */
    public List<StockCli> buyers = new ArrayList();
    /**
     * Lista de vendores e ações disponíveis pra compra.
     */
    public List<StockCli> sellers = new ArrayList();

    //variável para controle dos ids
    public int idClients = 0;

    /**
     * Construtor do WebService que cria a tarefa que atualiza os preços das
     * ações e tenta concluir as compras e vendas.
     */
    public NewWebService() {
        TimerTask update = new TimerTask() {
            @Override
            public void run() {
                //chama o método que atualiza os preços atuais das ações
                updatePrices();
                //chama o método que verifica se há alguma compatibilidade para compra e venda
                buyANDsell();
            }
        };
        //instancia o timer
        Timer t = new Timer();
        //agenda a tarefa para executar de 5 em 5 segundos
        t.schedule(update, 0, 5000);
    }

    /**
     * Operação de Web service que apaga da lista de ações as ações do cliente
     * que saiu do mercado evitando que tente vender no futuro de um cliente
     * inexistente.
     *
     * @param client com o nome do cliente
     */
    @WebMethod(operationName = "logout")
    @Oneway
    public void logout(@WebParam(name = "client") String client) {
        //para todas as ações dos clientes
        for (StockCli st : stocksCli) {
            //se a ação é do cliente que está saindo
            if (st.client.equals(client)) {
                stocksCli.remove(st);
                //busca a ação na lista de ações local
                for (Stock s : stocksLocal) {
                    if (st.stock.equals(s)) //atualiza a quantidade de ações que estariam disponíveis
                    {
                        s.setQt(s.getQt() - st.stock.getQt());
                    }
                }
            }
        }
    }

    /**
     * Método que retorna um id não usado para o cliente.
     *
     * @return do tipo int com o id não utilizado
     */
    @WebMethod(operationName = "login")
    public int login() {
        idClients++;
        return idClients;
    }

    /**
     * Operação de Web service que retorna uma ação de determinada empresa.
     *
     * @param company com o nome da empresa pesquisada
     * @return com a lista de correspondências encontradas
     */
    @WebMethod(operationName = "getStocks")
    public List<String> getStocks(@WebParam(name = "company") String company) {
        //cria a list de retorno
        List<String> s = new ArrayList();
        //para todas as ações na lista local
        for (Stock st : stocksLocal) {
            //se a ação é a da empresa
            if (st.company.equals(company)) {
                //adiciona na lista a string com os dados necessários
                s.add(st.company + " " + st.getPrice() + " " + st.getQntd());
            }
        }
        //retorna a lista
        return s;
    }

    /**
     * Operação de Web service que retorna uma ação de um determinado cliente.
     *
     * @param company com o nome da empresa pesquisada
     * @return com a lista de correspondencias encontradas
     */
    @WebMethod(operationName = "getStocksClient")
    public List<String> getStocksClient(@WebParam(name = "client") String client) {
        //cria a list de retorno
        List<String> s = new ArrayList();
        //para todas as ações na lista de clientes
        for (StockCli st : stocksCli) {
            //se a ação é a do cliente
            if (st.client.equals(client)) {
                //adiciona na lista a string com os valores da ação
                s.add(st.stock.company + " " + st.stock.minPrice + " " + st.stock.getQntd());
            }
        }
        //retorna
        return s;
    }

    /**
     * Operação de Web service que adiciona uma ação na lista de ações
     * disponíveis para venda.
     *
     * @param client com o nome do cliente que possui a ação
     * @param company com o nome da empresa
     * @param qntd com a quantidade para venda
     * @param minPrice com o preço mínimo de venda
     * @param id com o id do cliente
     */
    @WebMethod(operationName = "newStock_sell")
    public String newStock_sell(@WebParam(name = "client") String client, @WebParam(name = "company") String company,
            @WebParam(name = "qntd") int qntd, @WebParam(name = "minPrice") double minPrice, @WebParam(name = "id") int id) {
        //cria o objeto stock com os dados fornecidos
        Stock stock = new Stock(company, qntd, minPrice);
        //verifica se a ação já estava na tabela de vendas
        //se já estava só atualiza quantidade e o preço mínimo de venda
        for (StockCli sc : sellers) {
            if (sc.stock.company.equals(stock.company) && client.equals(sc.client)) {
                sc.stock.setQt(stock.getQt());
                sc.stock.setMinPrice(stock.getMinPrice());
                //retorna o status resultante do método
                return "Foi atualizado a ação " + stock.toString2() + " na lista de vendas";
            }
        }
        //se não existe na lista adiciona uma nova
        //é necessário que os preços estejam setados corretamente
        StockCli sc = new StockCli(stock, client, id);
        sellers.add(sc);
        //retorna o status resultante do método
        return "Adicionado ação " + stock.toString() + " na lista de vendas";

    }

    /**
     * Operação de Web service que adiciona uma ação na lista de ações
     * disponíveis para compra.
     *
     * @param client com o nome do cliente que quer comprar a ação
     * @param company com o nome da empresa
     * @param qntd com a quantidade para venda
     * @param maxPrice com o preço máximo de compra
     * @param id com o id do cliente
     */
    @WebMethod(operationName = "newStock_buy")
    public String newStock_buy(@WebParam(name = "client") String client, @WebParam(name = "company") String company,
            @WebParam(name = "qntd") int qntd, @WebParam(name = "maxPrice") double maxPrice, @WebParam(name = "id") int id) {
        Stock stock = new Stock(maxPrice, company, qntd);
        //verifica se a ação já estava na tabela de compras
        //se já estava só atualiza quantidade e o preço máximo de compra
        for (StockCli sc : buyers) {
            if (sc.stock.company.equals(stock.company) && client.equals(sc.client)) {
                sc.stock.setQt(stock.getQt());
                sc.stock.setMaxPrice(stock.getMaxPrice());
                //retorna o status resultante do método
                return "Foi atualizado a ação " + stock.toString3() + " na lista de compras";
            }
        }
        //se não existe na lista adiciona uma nova
        //é necessário que os preços estejam setados corretamente
        StockCli sc = new StockCli(stock, client, id);
        buyers.add(sc);
        //retorna o status resultante do método
        return "Adicionado ação " + stock.toString() + " na lista de compras";
    }

    /**
     * Operação de Web service que adiciona uma nova ação na lista dos clientes
     * e na lista local.
     *
     * @param client com o nome do cliente que possui a ação
     * @param company com o nome da empresa
     * @param qntd com a quantidade para venda
     * @param minPrice com o preço mínimo de venda
     * @param id com o id do cliente
     */
    @WebMethod(operationName = "newStock")
    public String newStock(@WebParam(name = "client") String client, @WebParam(name = "company") String company,
            @WebParam(name = "qntd") int qntd, @WebParam(name = "minPrice") double minPrice, @WebParam(name = "id") int id) {
        Stock stock = new Stock(company, qntd, minPrice);
        //adiciona a ação na lista local de ações
        newStockLocal(company, qntd, minPrice);
        //verifica se a ação e o cliente associada com ela já existe na lista
        //se existe apenas atualiza a quantidade e a disponibilidade de venda
        for (StockCli sc : stocksCli) {
            if (sc.stock.company.equals(stock.company) && client.equals(sc.client)) {
                sc.stock.setQt(stock.getQt());
                //retorna o status resultante do método
                return "Foi atualizado a ação " + stock.toString();
            }
        }
        //se não existe na lista adiciona uma nova
        StockCli sc = new StockCli(stock, client, id);
        stocksCli.add(sc);
        //retorna o status resultante do método
        return "Adicionado ação " + stock.toString();
    }

    /**
     * Método que adiciona na lista local uma ação disponível no mercado.
     *
     * @param company com o nome da empresa
     * @param qntd com a quantidade disponível
     * @param minPrice com o preço mínimo
     */
    public void newStockLocal(String company, int qntd, double minPrice) {
        Stock stock = new Stock(company, qntd, minPrice);
        //verifica se a ação já existe na lista de ações local
        //se existe apenas atualiza a quantidade e a disponibilidade de venda
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
     * servidor de forma randômica.
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
            //se op for true e o range maior q 0
            if (op && range > 0) { //faz a soma do preço atual com o range
                st.actualPrice += range;
            } //se o preço atual não fica menor que o preço mínimo que o cliente deseja quando subtraido o range
            //e se o range maior que 0
            else if (st.actualPrice >= (range + st.minPrice) && range > 0) { //faz a subtracao do preço atual com o range
                st.actualPrice -= range;
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
        //para todas as ações na lista de compras
        for (StockCli b : buyers) {
            //para cada ação na lista de compras procura uma correspondência na lista de vendas
            for (StockCli s : sellers) {
                //se a compra não foi realizada ele tenta realizar a compra
                if (!b.statusSell) {
                    //busca a ação desejada da lista local de ações 
                    //(importante porque o preço atual só é atualizado nessa lista)
                    for (Stock sL : stocksLocal) {
                        if (sL.company.equals(b.stock.company)) {
                            sLocal = sL;
                        }
                    }
                    //se encontrou a ação na lista local de ações
                    if (sLocal != null) {
                        //busca de acordo com as regras de transação
                        //se o vendedor não é o comprador, se as empresas batem, se o preço atual é maior 
                        //que o preço desejado pelo vendedor
                        //se  preço atual é menor que o desejado pelo comprador
                        if (!b.client.equals(s.client) && b.stock.company.equals(s.stock.company)
                                && sLocal.actualPrice >= s.stock.minPrice && sLocal.actualPrice <= b.stock.maxPrice) {
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
     * Operação de Web service que retorna as ações cadastradas para compras de
     * um determinado cliente.
     *
     * @param client com o nome do cliente
     */
    @WebMethod(operationName = "getTransactionsBuy")
    public List<String> getTransactionsBuy(@WebParam(name = "client") String client) {
        List<String> s = new ArrayList();
        //para todas as ações da lista
        for (StockCli sB : buyers) {
            //se a ação é do cliente
            if (sB.client.equals(client)) {
                String status;
                //se já foi concluída a venda 
                if (sB.statusSell) {
                    status = "Completa";
                } else//se não foi concluida a venda
                {
                    status = "Incompleta";
                }
                //adiciona na lista a string com os dados necessários para visualização
                s.add(sB.stock.company + " " + sB.stock.maxPrice + " " + sB.stock.transactionPrice + " " + sB.stock.getQt() + " " + status);
            }
        }
        //retorna lista
        return s;
    }

    /**
     * Operação de Web service que retorna as ações cadastradas para vendas de
     * um determinado cliente.
     *
     * @param client com o nome do cliente
     */
    @WebMethod(operationName = "getTransactionsSell")
    public List<String> getTransactionsSell(@WebParam(name = "client") String client) {
        List<String> s = new ArrayList();
        //para todas as ações da lista
        for (StockCli sS : sellers) {
            //se a ação é do cliente
            if (sS.client.equals(client)) {
                String status;
                //se já foi concluída a venda 
                if (sS.statusSell) {
                    status = "Completa";
                } else//se não foi concluida a venda
                {
                    status = "Incompleta";
                }
                //adiciona na lista a string com os dados necessários para visualização
                s.add(sS.stock.company + " " + sS.stock.minPrice + " " + sS.stock.transactionPrice + " " + sS.stock.getQt() + " " + status);
            }
        }
        //retorna lista
        return s;
    }
}
