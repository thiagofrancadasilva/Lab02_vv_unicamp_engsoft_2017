# language: pt
Funcionalidade: Consultar Entrega
  Como um usuario do sistema Bookstore
  Desejo consultar o status da entrega a partir do PROTOCOLO
  Para que eu possa saber a data de entrega

  Cenario: Consultar um protocolo valido
    Dado um PROTOCOLO valido:
      | protocolo | SQ458226057BR |
    Quando eu informo o PROTOCOLO na consulta de entrega
    Entao o resultado deve ser o status:
      | Protocolo      | Status       |
      | SQ458226057BR  | Em andamento |

  Cenario: Consultar uma entrega com PROTOCOLO nao existente
    Dado um PROTOCOLO nao existente:
      | protocolo | SQ458226057BR |
    Quando eu informo o PROTOCOLO na consulta de entrega
    Entao o retorno deve conter um valor de erro igual a "true"

  Cenario: Consultar o status de entrega com PROTOCOLO invalido.
    Dado um PROTOCOLO invalido:
      | protocolo | SQ458226057BR |
    Quando eu informo o PROTOCOLO na consulta de entrega
    Entao Uma excecao deve ser lancada com a mensagem de erro:
    """
    O PROTOCOLO informado e invalido
    """

  Cenario: Servico ConsultaEntrega nao responde
    Dado um PROTOCOLO valido:
      | protocolo | SQ458226057BR |
    E o servico SROProtocolo nao esta respondendo
    Quando eu informo o PROTOCOLO na consulta de entrega
    Entao Uma excecao deve ser lancada com a mensagem de erro:
    """
    Servico indisponivel
    """
