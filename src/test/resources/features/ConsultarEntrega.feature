# language: pt
Funcionalidade: Consultar Entrega
  Como um usuario do sistema Bookstore
  Desejo consultar o status da entrega a partir do PROTOCOLO
  Para que eu possa saber a data de entrega

  Cenario: Consultar um protocolo valido
    Dado um PROTOCOLO valido:
      | protocolo | 13083970 |
    Quando eu informo o PROTOCOLO na consulta de entrega
    Entao o resultado deve ser:
      | Protocolo | Status       |
      | 13083970  | Em andamento |

  Cenario: Consultar uma entrega com PROTOCOLO nao existente
    Dado um PROTOCOLO nao existente:
      | protocolo | 99999999 |
    Quando eu informo o PROTOCOLO na consulta de entrega
    Entao o retorno deve conter um valor de erro igual a "true"

  Cenario: Consultar o status de entrega com PROTOCOLO invalido.
    Dado um PROTOCOLO invalido:
      | protocolo | 1234567890 |
    Quando eu informo o PROTOCOLO na consulta de entrega
    Entao uma excecao deve ser lancada com a mensagem de erro:
    """
    O PROTOCOLO informado e invalido
    """

  Cenario: Servico ConsultaEntrega nao responde
    Dado um PROTOCOLO valido:
      | protocolo | 13083970 |
    E o servico ViaCep nao esta respondendo
    Quando eu informo o PROTOCOLO na consulta de entrega
    Entao uma excecao deve ser lancada com a mensagem de erro:
    """
    Servico indisponivel
    """
