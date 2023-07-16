package bugbank.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static bugbank.utils.GenericsUtils.removerAcentos;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    String nome, email, senha;
    double saldo;
    int numeroConta, digitoConta;

    public Usuario(String nome, String email, String senha)
    {
        this.nome = nome;
        this.email = removerAcentos(email);
        this.senha = senha;
    }

    public String retornaContaTratada()
    {
        return this.numeroConta+"-"+digitoConta;
    }
}