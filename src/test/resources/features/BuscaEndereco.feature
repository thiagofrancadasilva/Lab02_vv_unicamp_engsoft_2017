# language: pt
Funcionalidade: Buscar Endereco
  Como um usuario do sistema Bookstore
  Desejo consultar um endereco a partir do CEP
  Para que eu possa usar o endereco para fazer um pedido

  Cenario: Consultar um endereco valido
    Dado um CEP valido:
      | cep | 13083970 |
    Quando eu informo o CEP na busca de enderecos
    Entao O resultado deve ser o endereco: 
      | Logradouro       | Cidade   |
      | Rua Carlos Gomes | Campinas |

  Cenario: Consultar um endereco com CEP nao existente
    Dado um CEP nao existente:
      | cep | 99999999 |
    Quando eu informo o CEP na busca de enderecos
    Entao o retorno deve conter um valor de erro igual a "true"

  Cenario: Consultar um endereco com CEP invalido.
    Dado um CEP invalido:
      | cep | 1234567890 |
    Quando eu informo o CEP na busca de enderecos
    Entao Uma excecao deve ser lancada com a mensagem de erro:
    """
    O CEP informado é invalido
    """

  Cenario: Servico ViaCep nao responde
    Dado um CEP valido:
      | cep | 13083970 |
    E O servico ViaCep nao esta respondendo 
    Quando eu informo o CEP na busca de enderecos
    Entao Uma excecao deve ser lancada com a mensagem de erro:
    """
    Serviço indisponivel
    """
