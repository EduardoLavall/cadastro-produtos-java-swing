package cadastroprodutos;

import cadastroprodutos.beans.Categoria;
import cadastroprodutos.beans.Produto;
import cadastroprodutos.util.Validacao;
import java.math.BigDecimal;

/** Testes sem JUnit para compilar com NetBeans/Ant sem outras dependências. */
public final class TestesValidacao {
    private static int total=0;
    private static void igual(Object esperado,Object real){
        total++;
        if(!esperado.equals(real))throw new AssertionError(esperado+" != "+real);
    }
    private static void invalido(Runnable r){
        total++;
        try{r.run();throw new AssertionError("Esperava exceção de validação");}
        catch(IllegalArgumentException certo) { }
    }
    public static void main(String[] args) {
        igual("Notebook",Validacao.nomeProduto("  Notebook  "));
        igual("Tecnologia",Validacao.nomeCategoria("  Tecnologia "));
        invalido(() -> Validacao.nomeProduto(""));
        invalido(() -> Validacao.nomeCategoria(" "));
        invalido(() -> Validacao.nomeCategoria(new String(new char[81]).replace('\0','x')));
        igual(new BigDecimal("49.90"),Validacao.preco("49,90"));
        igual(new BigDecimal("12.00"),Validacao.preco("12"));
        igual(new BigDecimal("0.00"),Validacao.preco("0"));
        invalido(() -> Validacao.preco("-0,01"));
        invalido(() -> Validacao.preco("12,999"));
        invalido(() -> Validacao.preco("abc"));
        invalido(() -> Validacao.preco(""));
        invalido(() -> Validacao.preco("999999999.99"));
        igual(0,Validacao.quantidade("0"));
        igual(100,Validacao.quantidade("100"));
        invalido(() -> Validacao.quantidade("-1"));
        invalido(() -> Validacao.quantidade("2.5"));
        invalido(() -> Validacao.quantidade(""));
        invalido(() -> Validacao.categoriaSelecionada(-1));
        igual(4,Validacao.categoriaSelecionada(4));
        Categoria categoria=new Categoria(8,"Escritório");
        igual("Escritório",categoria.toString());
        igual(8,categoria.getId());
        Produto p=new Produto(2,"Caneta",new BigDecimal("2.50"),15,8,"Escritório");
        igual(2,p.getId());igual("Caneta",p.getNome());
        igual(new BigDecimal("2.50"),p.getPreco());
        igual(15,p.getQuantidade());igual(8,p.getCategoriaId());
        igual("Escritório",p.getCategoriaNome());
        System.out.println("OK: "+total+" testes de validação/modelos passaram.");
    }
}
