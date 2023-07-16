package bugbank.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
        this.email = email;
        this.senha = senha;
    }

    public String retornaContaTratada()
    {
        return this.numeroConta+"-"+digitoConta;
    }
}