package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.ConnectionFactory;
import br.com.alura.bytebank.domain.RegraDeNegocioException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Set;

public class ContaService {

    private ConnectionFactory connFactory;

    public ContaService(){
        this.connFactory = new ConnectionFactory();
    }

    public Set<Conta> listarContasAbertas() {
        Connection connection = connFactory.recuperarConexao();
        return new ContaDAO(connection).listar();
    }

    public BigDecimal consultarSaldo(Integer numeroDaConta) {
        var conta = buscarContaPorNumero(numeroDaConta);
        return conta.getSaldo();
    }

    public void abrir(DadosAberturaConta dadosDaConta) {

        Connection connection = connFactory.recuperarConexao();

        new ContaDAO(connection).salva(dadosDaConta);

    }

    public void realizarTransferencia(Integer contaOrigem, Integer contaDestino, BigDecimal valor){
        realizarSaque(contaOrigem, valor);
        realizarDeposito(contaDestino,valor);
    }

    public void realizarSaque(Integer numeroDaConta, BigDecimal valor) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Valor do saque deve ser superior a zero!");
        }

        if (valor.compareTo(conta.getSaldo()) > 0) {
            throw new RegraDeNegocioException("Saldo insuficiente!");
        }

        Connection connection = connFactory.recuperarConexao();
        new ContaDAO(connection).sacar(numeroDaConta, valor);
    }

    public void realizarDeposito(Integer numeroDaConta, BigDecimal valor) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Valor do deposito deve ser superior a zero!");
        }
        if(conta!=null){
            Connection connection = connFactory.recuperarConexao();
            new ContaDAO(connection).alterar(numeroDaConta,conta.getSaldo().add(valor));
        }else{
            throw new RegraDeNegocioException("Conta não encontrada!");
        }

    }

    public void encerrar(Integer numeroDaConta) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (conta.possuiSaldo()) {
            throw new RegraDeNegocioException("Conta não pode ser encerrada pois ainda possui saldo!");
        }
        if(conta!=null){
            Connection connection = connFactory.recuperarConexao();
            new ContaDAO(connection).encerrar(conta.getNumero());
        }else{
            throw new RegraDeNegocioException("Conta não encontrada!");
        }
    }

    private Conta buscarContaPorNumero(Integer numero) {
        Connection con = connFactory.recuperarConexao();
        return new ContaDAO(con).listarPorNumero(numero);
    }
}
