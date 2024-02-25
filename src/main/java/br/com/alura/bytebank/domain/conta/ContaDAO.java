package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.ConnectionFactory;
import br.com.alura.bytebank.domain.cliente.Cliente;
import br.com.alura.bytebank.domain.cliente.DadosCadastroCliente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ContaDAO {

    private Connection conn;
    ContaDAO(ConnectionFactory connection){
        this.conn = connection.recuperarConexao();
    }

    public void salva(DadosAberturaConta dadosDaConta){
        var cliente = new Cliente(dadosDaConta.dadosCliente());
        var conta = new Conta(dadosDaConta.numero(), cliente);

        String sql ="INSERT INTO conta (numero, saldo,cliente_nome, cliente_cpf, cliente_email) "+
                "VALUES(?,?,?,?,?)";

        try{
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1,conta.getNumero());
            preparedStatement.setBigDecimal(2, BigDecimal.ZERO);
            preparedStatement.setString(3,dadosDaConta.dadosCliente().nome());
            preparedStatement.setString(4, dadosDaConta.dadosCliente().cpf());
            preparedStatement.setString(5, dadosDaConta.dadosCliente().email());

            preparedStatement.execute();
            preparedStatement.close();
            conn.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    public Set<Conta> listar(){
        Set<Conta> contas = new HashSet<Conta>();
        String sql ="SELECT * FROM conta";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()){
                Integer numero = result.getInt(1);
                BigDecimal saldo = result.getBigDecimal(2);
                String nome = result.getString(3);
                String cpf = result.getString(4);
                String email = result.getString(5);

                DadosCadastroCliente cad = new DadosCadastroCliente(nome,cpf,email);
                Cliente cli = new Cliente(cad);
                contas.add(new Conta(numero,cli));
            }
            result.close();
            ps.close();
            conn.close();
            return contas;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void alterar(Integer numero, BigDecimal valor){
        PreparedStatement ps;
        String sql = "UPDATE FROM conta SET saldo=? WHERE numero=?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setBigDecimal(1,valor);
            ps.setInt(2,numero);
            ps.execute();
            ps.close();
            conn.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
